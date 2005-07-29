package org.geworkbench.bison.datastructure.biocollections.views;

import org.geworkbench.bison.annotation.DSCriteria;
import org.geworkbench.bison.datastructure.biocollections.microarrays.DSMicroarraySet;
import org.geworkbench.bison.datastructure.bioobjects.DSBioObject;
import org.geworkbench.bison.datastructure.bioobjects.markers.DSGeneMarker;
import org.geworkbench.bison.datastructure.bioobjects.microarray.DSMicroarray;
import org.geworkbench.bison.datastructure.complex.panels.CSPanel;
import org.geworkbench.bison.datastructure.complex.panels.DSItemList;
import org.geworkbench.bison.datastructure.complex.panels.DSPanel;
import org.geworkbench.bison.util.CSCriterionManager;
import org.geworkbench.bison.util.CSMarkerManager;


/**
 * <p>Title: caWorkbench</p>
 * <p/>
 * <p>Description: Modular Application Framework for Gene Expession, Sequence
 * and Genotype Analysis</p>
 * <p/>
 * <p>Copyright: Copyright (c) 2003 -2004</p>
 * <p/>
 * <p>Company: Columbia University</p>
 *
 * @author Adam Margolin
 * @version 3.0
 */
public class CSMicroarraySetView <T extends DSGeneMarker, Q extends DSMicroarray> extends CSDataSetView<Q> implements DSMicroarraySetView<T, Q> {
    //    DSMicroarraySet<DSMicroarray> dataSet = null;

    /**
     * Contains the active markers, organized as a DSPanel.
     */
    protected DSPanel<T> markerPanel = new CSPanel<T>("");

    /**
     * Designates if the marker subselection imposed by the activated panels
     * is imposed on the this microarray set view.
     */
    protected boolean useMarkerPanel = false;

    // @todo I don't think this should be allowed - watkin: let's review
    public CSMicroarraySetView() {
    }

    public CSMicroarraySetView(DSMicroarraySet<Q> dataSet) {
        this.dataSet = dataSet;
    }

    public DSItemList<T> markers() {
        if (dataSet instanceof DSMicroarraySet) {
            if (useMarkerPanel && markerPanel.size() > 0) {
                return markerPanel;
            } else {
                return (DSItemList) ((DSMicroarraySet) dataSet).getMarkers();
            }
        } else {
            return markerPanel;
        }
    }

    /**
     * Set/resets marker subselection based on activated panels.
     *
     * @param status
     */
    public void useMarkerPanel(boolean status) {
        useMarkerPanel = status;
    }

    /**
     * Gets the status of marker activation
     *
     * @return the status of marker activation
     */
    public boolean useMarkerPanel() {
        return useMarkerPanel;
    }

    public void setMarkerPanel(DSPanel<T> markerPanel) {
        this.markerPanel = markerPanel;
    }

    public DSPanel<T> getMarkerPanel() {
        return markerPanel;
    }

    public DSItemList<T> allMarkers() {
        if (dataSet instanceof DSMicroarraySet) {
            return (DSItemList) ((DSMicroarraySet) dataSet).getMarkers();
        } else {
            return null;
        }
    }

    public double getValue(int markerIndex, int arrayIndex) {
        DSMicroarray ma = get(arrayIndex);
        DSGeneMarker marker = markers().get(markerIndex);
        return ma.getMarkerValue(marker).getValue();
    }

    public double getValue(T marker, int arrayIndex) {
        DSMicroarray ma = get(arrayIndex);
        //        DSGeneMarker markerValue = markers().get(marker);
        return ma.getMarkerValue(marker.getSerial()).getValue();
    }

    public double getMeanValue(T marker, int arrayIndex) {
        DSMicroarray ma = get(arrayIndex);
        //This is a bit incorrect because it does not limit to only the selected markers
        return getMicroarraySet().getMeanValue(marker, ma.getSerial());
    }

    public double[] getRow(int index) {
        double[] rowVals = new double[this.size()];
        for (int itemCtr = 0; itemCtr < rowVals.length; itemCtr++) {
            rowVals[itemCtr] = getValue(index, itemCtr);
        }
        return rowVals;
    }

    public double[] getRow(T marker) {
        DSGeneMarker markerValue = markers().get(marker);
        if (markerValue != null)
            return getRow(markerValue.getSerial());
        return null;
    }

    /**
     * Sets the reference microarray set for this <code>MicroarraySetView</code>.
     *
     * @param ma The new reference microarray set.
     */
    public void setMicroarraySet(DSMicroarraySet<Q> ma) {
        if (ma != null) {
            dataSet = ma;
            DSPanel mp = CSMarkerManager.getMarkerPanel(ma);
            if (mp != null) {
                markerPanel = mp;
            }
            DSCriteria<DSBioObject> criteria = CSCriterionManager.getCriteria(dataSet);
            classCriteria = CSCriterionManager.getClassCriteria(dataSet);
            mp = criteria.getSelectedCriterion();
            if (mp != null) {
                itemPanel = mp;
            }
        }
    }

    public DSMicroarraySet<Q> getMicroarraySet() {
        return (DSMicroarraySet<Q>) getDataSet();
    }

}
