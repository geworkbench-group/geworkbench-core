package org.geworkbench.engine.parsers;

import org.geworkbench.engine.resource.AbstractResource;

import java.io.File;

/**
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: First Genetic Trust Inc.</p>
 * @author First Genetic Trust Inc.
 * @version 1.0
 */

/**
 * Extends <code>Resource</code> to allow handling Genepix input files.
 */
public class GenepixResource extends AbstractResource {
    /**
     * The name of the input Affy file that gives rise to the inputReader
     */
    File inputFile = null;

    public GenepixResource() {
    }

    /**
     * Set the file name for the input file from which this resource is generated.
     *
     * @param iFName The input file name.
     */
    public void setInputFile(File file) {
        inputFile = file;
    }

    /**
     * Return the name of the input file from which this resource is generated
     *
     * @return
     */
    public File getInputFile() {
        return inputFile;
    }

}

