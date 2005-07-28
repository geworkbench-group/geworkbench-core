package org.systemsbiology.chem;

/*
 * Copyright (C) 2003 by Institute for Systems Biology,
 * Seattle, Washington, USA.  All rights reserved.
 *
 * This source code is distributed under the GNU Lesser
 * General Public License, the text of which is available at:
 *   http://www.gnu.org/copyleft/lesser.html
 */

import org.systemsbiology.math.*;
import org.systemsbiology.util.DataNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public abstract class Simulator implements Runnable {
    private static final int MIN_NUM_REACTION_STEPS_FOR_USING_DELAY_FUNCTION = 15;
    protected static final long DEFAULT_MIN_NUM_MILLISECONDS_FOR_UPDATE = 1000;
    protected static final int NULL_REACTION = -1;

    protected String[] mDynamicSymbolNames;
    protected double[] mDynamicSymbolValues;
    protected double[] mInitialDynamicSymbolValues;  // saves a copy of the initial data
    protected Value[] mNonDynamicSymbolValues;
    protected boolean mHasExpressionValues;
    protected SymbolEvaluatorChemSimulation mSymbolEvaluator;
    protected Reaction[] mReactions;
    protected double[] mReactionProbabilities;
    protected SpeciesRateFactorEvaluator mSpeciesRateFactorEvaluator;
    protected HashMap mSymbolMap;
    protected SimulationController mSimulationController;
    protected DelayedReactionSolver[] mDelayedReactionSolvers;
    protected Species[] mDynamicSymbols;
    protected Object[] mDynamicSymbolAdjustmentVectors;
    protected boolean mInitialized;
    protected long mMinNumMillisecondsForUpdate;
    protected SimulationProgressReporter mSimulationProgressReporter;

    //These are the variables that were passed in the simulate() methods.  I
    //add them as method variables so they can be set outside of the method
    //and this can be run as a separate thread
    double pStartTime;
    double pEndTime;
    SimulatorParameters pSimulatorParameters;
    int pNumTimePoints;
    String[] pRequestedSymbolNames;
    double[] pRetTimeValues;
    Object[] pRetSymbolValues;

    public double getPStartTime() {
        return this.pStartTime;
    }

    public void setPStartTime(double pStartTime) {
        this.pStartTime = pStartTime;
    }

    public double getPEndTime() {
        return this.pEndTime;
    }

    public void setPEndTime(double pEndTime) {
        this.pEndTime = pEndTime;
    }

    public SimulatorParameters getPSimulatorParameters() {
        return this.pSimulatorParameters;
    }

    public void setPSimulatorParameters(SimulatorParameters pSimulatorParameters) {
        this.pSimulatorParameters = pSimulatorParameters;
    }

    public int getPNumTimePoints() {
        return this.pNumTimePoints;
    }

    public void setPNumTimePoints(int pNumTimePoints) {
        this.pNumTimePoints = pNumTimePoints;
    }

    public String[] getPRequestedSymbolNames() {
        return this.pRequestedSymbolNames;
    }

    public void setPRequestedSymbolNames(String[] pRequestedSymbolNames) {
        this.pRequestedSymbolNames = pRequestedSymbolNames;
    }

    public double[] getPRetTimeValues() {
        return this.pRetTimeValues;
    }

    public void setPRetTimeValues(double[] pRetTimeValues) {
        this.pRetTimeValues = pRetTimeValues;
    }

    public Object[] getPRetSymbolValues() {
        return this.pRetSymbolValues;
    }

    public void setPRetSymbolValues(Object[] pRetSymbolValues) {
        this.pRetSymbolValues = pRetSymbolValues;
    }


    static void indexSymbolArray(SymbolValue[] pSymbolArray, HashMap pSymbolMap, double[] pDoubleArray, Value[] pValueArray) {
        assert (null == pDoubleArray || null == pValueArray) : "either pDoubleArray or pValueArray must be null";

        int numSymbols = pSymbolArray.length;
        for (int symbolCtr = 0; symbolCtr < numSymbols; ++symbolCtr) {
            SymbolValue symbolValue = pSymbolArray[symbolCtr];
            Symbol symbol = (Symbol) symbolValue.getSymbol();
            String symbolName = symbol.getName();
            Value value = (Value) symbolValue.getValue();

            assert (null == symbol.getDoubleArray()) : "array not null for new symbol " + symbolName;
            assert (null == symbol.getValueArray()) : "array not null for new symbol " + symbolName;
            assert (Symbol.NULL_ARRAY_INDEX == symbol.getArrayIndex()) : "array index not null for new symbol " + symbolName;
            assert (null != value) : "null value object for symbol: " + symbolName;

            pSymbolMap.put(symbolName, symbol);

            if (null != pDoubleArray) {

                symbol.setArray(pDoubleArray);
                pDoubleArray[symbolCtr] = value.getValue();
            } else {
                symbol.setArray(pValueArray);
                pValueArray[symbolCtr] = (Value) value;
            }

            symbol.setArrayIndex(symbolCtr);
        }
    }

    public boolean isInitialized() {
        return (mInitialized);
    }

    private void clearDelayedReactionSolvers() {
        int numDelayedReactionSolvers = mDelayedReactionSolvers.length;
        for (int ctr = 0; ctr < numDelayedReactionSolvers; ++ctr) {
            DelayedReactionSolver solver = mDelayedReactionSolvers[ctr];
            solver.clear();
        }
    }

    protected final void prepareForSimulation(double pStartTime) {
        // set initial values for dynamic symbols
        System.arraycopy(mInitialDynamicSymbolValues, 0, mDynamicSymbolValues, 0, mDynamicSymbolValues.length);
        MathFunctions.vectorZeroElements(mReactionProbabilities);
        clearDelayedReactionSolvers();
        clearExpressionValueCaches();
        mSymbolEvaluator.setTime(pStartTime);
    }

    public abstract boolean usesExpressionValueCaching();

    private static void handleDelayedReaction(Reaction pReaction, ArrayList pReactions, int pReactionIndex, ArrayList pDynamicSpecies, ArrayList pDelayedReactionSolvers, MutableInteger pRecursionDepth, boolean pIsStochasticSimulator) {
        String reactionName = pReaction.getName();

        HashMap reactantsMap = pReaction.getReactantsMap();
        if (reactantsMap.size() != 1) {
            throw new IllegalStateException("a multi-step reaction must have excactly one reactant species; reaction is: " + reactionName);
        }

        HashMap productsMap = pReaction.getProductsMap();
        if (productsMap.size() != 1) {
            throw new IllegalStateException("a multi-step reaction must have exactly one product species; reaction is: " + reactionName);
        }

        int numSteps = pReaction.getNumSteps();

        Species reactant = (Species) ((Reaction.ReactionElement) reactantsMap.values().iterator().next()).getSpecies();
        Species product = (Species) ((Reaction.ReactionElement) productsMap.values().iterator().next()).getSpecies();

        if (!reactant.getCompartment().equals(product.getCompartment())) {
            throw new IllegalStateException("the reactant and product for a multi-step reaction must be the same compartment");
        }

        Value rateValue = pReaction.getRate();
        if (rateValue.isExpression()) {
            throw new IllegalStateException("a multi-step reaction must have a numeric reaction rate, not a custom rate expression");
        }
        double rate = rateValue.getValue();

        Reaction firstReaction = new Reaction(reactionName);
        firstReaction.setRate(rate);
        firstReaction.addReactant(reactant, 1);
        String intermedSpeciesName = new String("delayed_species_" + reactionName + "_" + product.getName() + "_0");
        Compartment reactantCompartment = reactant.getCompartment();
        Species intermedSpecies = new Species(intermedSpeciesName, reactantCompartment);
        intermedSpecies.setSpeciesPopulation(0.0);
        firstReaction.addProduct(intermedSpecies, 1);
        pReactions.set(pReactionIndex, firstReaction);
        pDynamicSpecies.add(intermedSpecies);

        numSteps--;

        if (numSteps > 0 && numSteps < MIN_NUM_REACTION_STEPS_FOR_USING_DELAY_FUNCTION) {
            Species lastIntermedSpecies = intermedSpecies;

            for (int ctr = 0; ctr < numSteps; ++ctr) {
                Reaction reaction = new Reaction("delayed_reaction_" + reactionName + "_" + product.getName() + "_" + ctr);
                reaction.setRate(rate);

                reaction.addReactant(lastIntermedSpecies, 1);

                if (ctr < numSteps - 1) {
                    intermedSpeciesName = new String("delayed_species_" + reactionName + "_" + product.getName() + "_" + (ctr + 1));
                    intermedSpecies = new Species(intermedSpeciesName, reactantCompartment);
                    intermedSpecies.setSpeciesPopulation(0.0);
                    reaction.addProduct(intermedSpecies, 1);
                    pDynamicSpecies.add(intermedSpecies);
                    lastIntermedSpecies = intermedSpecies;
                } else {
                    reaction.addProduct(product, 1);
                }

                pReactions.add(reaction);
            }
        } else {
            Reaction delayedReaction = new Reaction("delayed_reaction_" + reactionName + "_" + product.getName());
            delayedReaction.addReactant(intermedSpecies, 1);
            delayedReaction.addProduct(product, 1);
            pReactions.add(delayedReaction);

            double delay = 0.0;
            if (numSteps > 0) {
                delay = (numSteps - 1) / rate;
            } else {
                delay = pReaction.getDelay();
            }

            DelayedReactionSolver solver = new DelayedReactionSolver(reactant, intermedSpecies, delay, rate, pReactions.size() - 1, pIsStochasticSimulator);
            pDelayedReactionSolvers.add(solver);
            delayedReaction.setRate(solver);
        }
    }

    public abstract boolean isStochasticSimulator();

    protected void initializeSimulator(Model pModel) throws DataNotFoundException {
        clearSimulatorState();

        // obtain and save an array of all reactions in the model
        ArrayList reactionsList = pModel.constructReactionsList();
        int numReactions = reactionsList.size();

        // get an array of all dynamical SymbolValues in the model
        ArrayList dynamicSymbolsList = pModel.constructDynamicSymbolsList();

        // get an array of all symbols in the model
        SymbolValue[] nonDynamicSymbols = pModel.constructGlobalNonDynamicSymbolsArray();
        int numNonDynamicSymbols = nonDynamicSymbols.length;

        ArrayList delayedReactionSolvers = new ArrayList();

        boolean isStochasticSimulator = isStochasticSimulator();

        // for each multi-step reaction, split the reaction into two separate reactions
        for (int reactionCtr = 0; reactionCtr < numReactions; ++reactionCtr) {
            Reaction reaction = (Reaction) reactionsList.get(reactionCtr);
            int numSteps = reaction.getNumSteps();
            double delay = reaction.getDelay();
            if (numSteps == 1 && delay == 0.0) {
                continue;
            }
            MutableInteger recursionDepth = new MutableInteger(1);

            handleDelayedReaction(reaction, reactionsList, reactionCtr, dynamicSymbolsList, delayedReactionSolvers, recursionDepth, isStochasticSimulator);
        }

        mDelayedReactionSolvers = (DelayedReactionSolver[]) delayedReactionSolvers.toArray(new DelayedReactionSolver[0]);

        int numDelayedReactions = mDelayedReactionSolvers.length;

        Species[] dynamicSymbols = (Species[]) dynamicSymbolsList.toArray(new Species[0]);
        ;
        mDynamicSymbols = dynamicSymbols;
        int numDynamicSymbols = dynamicSymbols.length;

        Reaction[] reactions = (Reaction[]) reactionsList.toArray(new Reaction[0]);
        mReactions = reactions;
        numReactions = reactions.length;

        // create an array of doubles to hold the dynamical symbols' values
        double[] dynamicSymbolValues = new double[numDynamicSymbols];
        String[] dynamicSymbolNames = new String[numDynamicSymbols];

        mDynamicSymbolValues = dynamicSymbolValues;

        // store the initial dynamic symbol values as a "vector" (native double array)
        for (int symbolCtr = 0; symbolCtr < numDynamicSymbols; ++symbolCtr) {
            SymbolValue symbolValue = dynamicSymbols[symbolCtr];

            Symbol symbol = symbolValue.getSymbol();
            String symbolName = symbol.getName();

            Value value = symbolValue.getValue();
            assert (null != value) : "null value for symbol: " + symbolName;

            double symbolValueDouble = symbolValue.getValue().getValue();
            dynamicSymbolValues[symbolCtr] = symbolValueDouble;
            dynamicSymbolNames[symbolCtr] = symbolName;
        }

        mDynamicSymbolNames = dynamicSymbolNames;

        double[] initialDynamicSymbolValues = new double[numDynamicSymbols];
        System.arraycopy(dynamicSymbolValues, 0, initialDynamicSymbolValues, 0, numDynamicSymbols);
        mInitialDynamicSymbolValues = initialDynamicSymbolValues;

        // create a map between symbol names, and their corresponding indexed
        // "Symbol" object
        HashMap symbolMap = new HashMap();
        mSymbolMap = symbolMap;

        indexSymbolArray(dynamicSymbols, symbolMap, dynamicSymbolValues, null);

        // build a map between symbol names and array indices

        Value[] nonDynamicSymbolValues = new Value[numNonDynamicSymbols];
        mNonDynamicSymbolValues = nonDynamicSymbolValues;

        indexSymbolArray(nonDynamicSymbols, symbolMap, null, nonDynamicSymbolValues);

        boolean hasExpressionValues = false;
        for (int ctr = 0; ctr < numNonDynamicSymbols; ++ctr) {
            if (mNonDynamicSymbolValues[ctr].isExpression()) {
                hasExpressionValues = true;
            }
        }
        mHasExpressionValues = hasExpressionValues;

        for (int ctr = 0; ctr < numDelayedReactions; ++ctr) {
            DelayedReactionSolver solver = mDelayedReactionSolvers[ctr];
            solver.initializeSpeciesSymbols(symbolMap, dynamicSymbols, nonDynamicSymbols);

        }

        SymbolEvaluatorChemSimulation evaluator = new SymbolEvaluatorChemSimulation(symbolMap, 0.0, usesExpressionValueCaching());
        mSymbolEvaluator = evaluator;

        checkSymbolsValues();

        for (int reactionCtr = 0; reactionCtr < numReactions; ++reactionCtr) {
            Reaction reaction = reactions[reactionCtr];
            reaction.prepareSymbolVectorsForSimulation(dynamicSymbols, nonDynamicSymbols, symbolMap);
        }

        mSpeciesRateFactorEvaluator = pModel.getSpeciesRateFactorEvaluator();

        // create an array of doubles to hold the reaction probabilities
        mReactionProbabilities = new double[numReactions];

        checkReactionRates();

        mInitialized = true;
    }

    private void checkReactionRates() throws DataNotFoundException {
        int numReactions = mReactions.length;
        for (int reactionCtr = 0; reactionCtr < numReactions; ++reactionCtr) {
            Reaction reaction = mReactions[reactionCtr];
            double reactionRate = reaction.computeRate(mSpeciesRateFactorEvaluator, mSymbolEvaluator);
            reaction.checkReactantValues(mSymbolEvaluator);
        }
    }

    private void checkSymbolsValues() throws DataNotFoundException {
        // test getting value for each symbol in the symbol table
        Iterator symbolNameIter = mSymbolMap.keySet().iterator();
        while (symbolNameIter.hasNext()) {
            String symbolName = (String) symbolNameIter.next();
            Symbol symbol = (Symbol) mSymbolMap.get(symbolName);
            assert (null != symbol) : "found null Symbol where a valid Symbol object was expected";
        }
    }

    protected void clearSimulatorState() {
        mInitialized = false;
        mDynamicSymbolValues = null;
        mInitialDynamicSymbolValues = null;
        mNonDynamicSymbolValues = null;
        mSymbolEvaluator = null;
        mReactions = null;
        mReactionProbabilities = null;
        mSpeciesRateFactorEvaluator = null;
        mSymbolMap = null;
        mSimulationController = null;
        mDelayedReactionSolvers = null;
        mDynamicSymbols = null;
        mDynamicSymbolAdjustmentVectors = null;
        setMinNumMillisecondsForUpdate(DEFAULT_MIN_NUM_MILLISECONDS_FOR_UPDATE);
    }

    public Simulator() {
        clearSimulatorState();
    }

    protected void initializeDynamicSymbolAdjustmentVectors(Species[] pDynamicSymbols) {
        Reaction[] reactions = mReactions;

        int numReactions = reactions.length;
        Object[] dynamicSymbolAdjustmentVectors = new Object[numReactions];

        for (int ctr = 0; ctr < numReactions; ++ctr) {
            Reaction reaction = reactions[ctr];
            dynamicSymbolAdjustmentVectors[ctr] = reaction.constructDynamicSymbolAdjustmentVector(pDynamicSymbols);
        }

        mDynamicSymbolAdjustmentVectors = dynamicSymbolAdjustmentVectors;
    }


    protected final int addRequestedSymbolValues(double pCurTime, int pLastTimeIndex, Symbol[] pRequestedSymbols, SymbolEvaluatorChemSimulation pSymbolEvaluator, double[] pTimeValues, Object[] pRetSymbolValues) throws DataNotFoundException {
        int numTimePoints = pTimeValues.length;
        int numRequestedSymbolValues = pRequestedSymbols.length;

        double saveTime = pSymbolEvaluator.getTime();

        int timeCtr = pLastTimeIndex;

        for (; timeCtr < numTimePoints; ++timeCtr) {
            double timeValue = pTimeValues[timeCtr];
            pSymbolEvaluator.setTime(timeValue);
            if (timeValue <= pCurTime) {
                double[] symbolValues = (double[]) pRetSymbolValues[timeCtr];
                if (null == symbolValues) {
                    symbolValues = new double[numRequestedSymbolValues];
                    for (int symCtr = numRequestedSymbolValues - 1; symCtr >= 0; --symCtr) {
                        symbolValues[symCtr] = 0.0;
                    }
                    pRetSymbolValues[timeCtr] = symbolValues;
                }
                for (int symCtr = numRequestedSymbolValues - 1; symCtr >= 0; --symCtr) {
                    Symbol symbol = pRequestedSymbols[symCtr];
                    symbolValues[symCtr] += pSymbolEvaluator.getValue(symbol);
                }
            } else {
                break;
            }
        }

        pSymbolEvaluator.setTime(saveTime);

        return (timeCtr);
    }

    protected static double[] createTimesArray(double pStartTime, double pEndTime, int pNumTimePoints) {
        assert (pNumTimePoints > 1) : " invalid number of time points";

        double[] retTimesArray = new double[pNumTimePoints];

        double deltaTime = (pEndTime - pStartTime) / ((double) (pNumTimePoints - 1));

        for (int timeCtr = 0; timeCtr < pNumTimePoints; ++timeCtr) {
            double time = ((double) timeCtr) * deltaTime;
            if (time > pEndTime) {
                time = pEndTime;
            }
            retTimesArray[timeCtr] = time;
        }

        return (retTimesArray);
    }

    protected Symbol[] createRequestedSymbolArray(HashMap pSymbolMap, String[] pRequestedSymbols) throws DataNotFoundException {
        int numSymbols = pRequestedSymbols.length;
        Symbol[] symbols = new Symbol[numSymbols];
        for (int symbolCtr = numSymbols - 1; symbolCtr >= 0; --symbolCtr) {
            String symbolName = pRequestedSymbols[symbolCtr];
            Symbol symbol = (Symbol) pSymbolMap.get(symbolName);
            if (null == symbol) {
                throw new DataNotFoundException("requested symbol not found in model: " + symbolName);
            }
            symbols[symbolCtr] = symbol;
        }
        return (symbols);
    }

    protected void conductPreSimulationCheck(double pStartTime, double pEndTime, SimulatorParameters pSimulatorParameters, int pNumResultsTimePoints, String[] pRequestedSymbolNames, double[] pRetTimeValues, Object[] pRetSymbolValues) throws IllegalArgumentException, IllegalStateException {
        if (!mInitialized) {
            throw new IllegalStateException("simulator has not been initialized yet");
        }

        if (pNumResultsTimePoints <= 1) {
            throw new IllegalArgumentException("number of time points must be greater than or equal to 1");
        }

        if (pStartTime >= pEndTime) {
            throw new IllegalArgumentException("end time must be greater than the start time");
        }

        if (pRetTimeValues.length != pNumResultsTimePoints) {
            throw new IllegalArgumentException("illegal length of pRetTimeValues array");
        }

        if (pRetSymbolValues.length != pNumResultsTimePoints) {
            throw new IllegalArgumentException("illegal length of pRetSymbolValues array");
        }
    }


    public void run() {
        try {
            this.simulate(this.pStartTime, this.pEndTime, this.pSimulatorParameters, this.pNumTimePoints, this.pRequestedSymbolNames, this.pRetTimeValues, this.pRetSymbolValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void simulate(double pStartTime, double pEndTime, SimulatorParameters pSimulatorParameters, int pNumTimePoints, String[] pRequestedSymbolNames, double[] pRetTimeValues, Object[] pRetSymbolValues) throws DataNotFoundException, IllegalStateException, IllegalArgumentException, SimulationAccuracyException, SimulationFailedException;


    protected static final void computeReactionProbabilities(SpeciesRateFactorEvaluator pSpeciesRateFactorEvaluator, SymbolEvaluatorChemSimulation pSymbolEvaluator, double[] pReactionProbabilities, Reaction[] pReactions) throws DataNotFoundException {
        // loop through all reactions, and for each reaction, compute the reaction probability
        int numReactions = pReactions.length;

        Reaction reaction = null;
        double reactionProbability = 0.0;

        for (int reactionCtr = numReactions; --reactionCtr >= 0;) {
            reaction = pReactions[reactionCtr];

            reactionProbability = reaction.computeRate(pSpeciesRateFactorEvaluator, pSymbolEvaluator);

            // store reaction probability
            pReactionProbabilities[reactionCtr] = reactionProbability;
        }
    }

    protected static final double getDelayedReactionEstimatedAverageFutureRate(SymbolEvaluatorChemSimulation pSymbolEvaluator, DelayedReactionSolver[] pDelayedReactionSolvers) throws DataNotFoundException {
        double compositeRate = 0.0;
        int numDelayedReactions = pDelayedReactionSolvers.length;
        for (int ctr = numDelayedReactions; --ctr >= 0;) {
            DelayedReactionSolver solver = pDelayedReactionSolvers[ctr];
            double estimatedFutureRate = solver.getEstimatedAverageFutureRate(pSymbolEvaluator);
            compositeRate += estimatedFutureRate;
        }
        return (compositeRate);
    }

    protected final void clearExpressionValueCaches() {
        if (mHasExpressionValues) {
            for (int ctr = mNonDynamicSymbolValues.length; --ctr >= 0;) {
                mNonDynamicSymbolValues[ctr].clearExpressionValueCache();
            }
        }
    }

    protected static final void computeDerivative(SpeciesRateFactorEvaluator pSpeciesRateFactorEvaluator, SymbolEvaluatorChemSimulation pSymbolEvaluator, Reaction[] pReactions, Object[] pDynamicSymbolAdjustmentVectors, double[] pReactionProbabilities, double[] pTempDynamicSymbolValues, double[] pDynamicSymbolDerivatives) throws DataNotFoundException {
        computeReactionProbabilities(pSpeciesRateFactorEvaluator, pSymbolEvaluator, pReactionProbabilities, pReactions);

        int numReactions = pReactions.length;
        Reaction reaction = null;
        double reactionRate = 0.0;

        MathFunctions.vectorZeroElements(pDynamicSymbolDerivatives);

        Object[] dynamicSymbolAdjustmentVectors = pDynamicSymbolAdjustmentVectors;

        for (int reactionCtr = numReactions; --reactionCtr >= 0;) {
            reaction = pReactions[reactionCtr];
            reactionRate = pReactionProbabilities[reactionCtr];

            double[] symbolAdjustmentVector = (double[]) dynamicSymbolAdjustmentVectors[reactionCtr];

            // we want to multiply this vector by the reaction rate and add it to the derivative
            MathFunctions.vectorScalarMultiply(symbolAdjustmentVector, reactionRate, pTempDynamicSymbolValues);

            MathFunctions.vectorAdd(pTempDynamicSymbolValues, pDynamicSymbolDerivatives, pDynamicSymbolDerivatives);
        }
    }

    public void setProgressReporter(SimulationProgressReporter pSimulationProgressReporter) {
        mSimulationProgressReporter = pSimulationProgressReporter;
    }

    public void setController(SimulationController pSimulationController) {
        mSimulationController = pSimulationController;
    }

    protected final boolean handleSimulationController() {
        boolean isCancelled = false;
        SimulationController simulationController = mSimulationController;
        if (null != simulationController) {
            if (simulationController.handlePauseOrCancel()) {
                isCancelled = true;
            }
        }
        return (isCancelled);
    }

    protected final void updateSimulationProgressReporter(double pFractionComplete, long pIterationCounter) {
        SimulationProgressReporter progressReporter = mSimulationProgressReporter;
        if (null != progressReporter) {
            progressReporter.updateProgressStatistics(pFractionComplete, pIterationCounter);
        }
    }

    public void setStatusUpdateIntervalSeconds(double pUpdateIntervalSeconds) throws IllegalArgumentException {
        if (pUpdateIntervalSeconds <= 0.0) {
            throw new IllegalArgumentException("invalid minimum number of seconds for update: " + pUpdateIntervalSeconds);
        }
        long updateIntervalMilliseconds = (long) (1000.0 * pUpdateIntervalSeconds);
        setMinNumMillisecondsForUpdate(updateIntervalMilliseconds);
    }

    protected void setMinNumMillisecondsForUpdate(long pMinNumMillisecondsForUpdate) {
        mMinNumMillisecondsForUpdate = pMinNumMillisecondsForUpdate;
    }


    //AM--3/31 added this
    public HashMap getSymbolMap() {
        return this.mSymbolMap;
    }

}
