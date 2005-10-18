package org.geworkbench.bison.datastructure.bioobjects.sequence;

import org.geworkbench.bison.datastructure.biocollections.CSDataSet;
import org.geworkbench.bison.datastructure.biocollections.DSAncillaryDataSet;

import javax.swing.*;
import java.io.File;

public class CSAlignmentResultSet extends CSDataSet implements DSAlignmentResultSet {
    public CSAlignmentResultSet(String fileName, String inputFile) {

        resultFile = new File(fileName);
        fastaFile = new File(inputFile);
        //System.out.println("in construtor" + resultFile.getAbsolutePath());
    }

    //private static ImageIcon icon = new ImageIcon("share/images/blast.gif");
    static private ImageIcon icon = new ImageIcon(CSAlignmentResultSet.class.getResource("blast.gif"));
    private String label = "Blast_Result";
    private File fastaFile = null;
    private File resultFile = null;

    /**
     * isDirty
     *
     * @return boolean
     * @todo Implement this geaw.bean.microarray.MAMemoryStatus method
     */
    public boolean isDirty() {
        return false;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    /**
     * getIcon
     *
     * @return ImageIcon
     * @todo Implement this medusa.components.projects.IDataSet method
     */
    public ImageIcon getIcon() {
        return icon;
    }

    /**
     * setDirty
     *
     * @param boolean0 boolean
     * @todo Implement this geaw.bean.microarray.MAMemoryStatus method
     */
    public void setDirty(boolean boolean0) {
    }

    /**
     * getDataSetName
     *
     * @return String
     * @todo Implement this medusa.components.projects.IDataSet method
     */
    public String getDataSetName() {
        // System.out.println("in get Datasetname " + resultFile.getAbsolutePath());
        return resultFile.getName();
    }

    public String getResultFilePath() {
        if (resultFile.canRead()) {
            return resultFile.getAbsolutePath();
        }
        return null;
    }

    public File getDataSetFile() {

        return fastaFile;
    }

    public void setDataSetFile(File _file) {
        fastaFile = _file;
        //System.out.println("in setDataSetFile " + fastaFile.getAbsolutePath());
    }

    /**
     * @param ads IAncillaryDataSet
     * @return boolean
     * @todo implement later.
     */
    public boolean equals(Object ads) {
        if (ads instanceof DSAncillaryDataSet) {
            return getDataSetName() == ((DSAncillaryDataSet) ads).getDataSetName();
        } else {
            return false;
        }
    }
    ;

    /**
     * getFile
     *
     * @return File
     * @todo Implement this medusa.components.projects.IDataSet method
     */
    public File getFile() {
        return resultFile;
    }

    /**
     * writeToFile
     *
     * @param fileName String
     */
    public void writeToFile(String fileName) {
    }

}
