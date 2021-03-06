package org.geworkbench.bison.datastructure.bioobjects.microarray;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geworkbench.bison.datastructure.biocollections.microarrays.DSMicroarraySet;
import org.geworkbench.bison.datastructure.bioobjects.markers.DSGeneMarker;
import org.geworkbench.bison.datastructure.properties.CSExtendable;
import org.geworkbench.bison.util.RandomNumberGenerator;

/**
 * 
 * @author zji
 * @version $Id$
 *
 */
public class CSMicroarray implements DSMicroarray, Serializable {

	private static final long serialVersionUID = -1624438489587615570L;
	private static Log log = LogFactory.getLog(CSMicroarray.class);
	
    /**
     * The relative position (index) of the microarray within its host
     * <code>MicroarraySet</code>.
     */
    private int serial = -1;
    /**
     * Used in the implementation of the <code>Identifiable</code> interface.
     */
    private String arrayId = null;
    /**
     * Used in the implementation of the <code>Extendable</code> interface.
     */
    private CSExtendable extend = new CSExtendable();


    /**
     * Label of the Microarray
     */
    private String label = null;

    /**
     * Array of JMarkers containing the actual Microarray data
     */
    private CSMarkerValue[] markerArray = null;

    public CSMicroarray(int markerNo) {
        markerArray = new CSMarkerValue[markerNo];
    }

    public CSMicroarray(int serial, int markerNo, String label, int type) {
        this.serial = serial;
        this.label = label;
        markerArray = new CSMarkerValue[markerNo];

		if (type == DSMicroarraySet.expPvalueType) {
			for (int i = 0; i < markerNo; i++)
				markerArray[i] = new CSExpressionMarkerValue(0);
		} else if (type == DSMicroarraySet.genepixGPRType) {
			for (int i = 0; i < markerNo; i++)
				markerArray[i] = new CSGenepixMarkerValue(0);
		} else if (type == DSMicroarraySet.affyTxtType) {
			for (int i = 0; i < markerNo; i++)
				markerArray[i] = new CSAffyMarkerValue();
		} else if (type == DSMicroarraySet.DO_NOT_CREATE_VALUE_OBJECT) {
			// do not create marker value objects;
		} else {
			// should never happen
			log.error("unknown type in constructor");
		}
    }

    public boolean isMarkerValid(int i) {
        if (i >= markerArray.length) {
            return false;
        }
        return markerArray[i].isValid();
    }

    public boolean isMarkerUndefined(int i) {
        if (i >= markerArray.length) {
            return true;
        }
        return markerArray[i].isMissing();
    }

    public int getMarkerNo() {
        return markerArray.length;
    }

    public void setMarkerValue(int index, DSMarkerValue markerValue) {
        markerArray[index] = (CSMarkerValue) markerValue;
    }

    public void setLabel(String label) {
        this.label = new String(label);
    }

    public String getLabel() {
        return label;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int id) {
        serial = id;
    }

    public String getID() {
        return arrayId;
    }

    public void setID(String id) {
        arrayId = id;
    }

    public String toString() {
        return getLabel();
    }

    public void setDescription(String desc) {
        log.error("CSMicroArray does not support addDescription");
    }

    public String getDescription() {
        log.error("CSMicroArray does not support getDescriptions");
        return null;
    }

    public void addNameValuePair(String name, Object value) {
        extend.addNameValuePair(name, value);
    }

    public Object[] getValuesForName(String name) {
        return extend.getValuesForName(name);
    }

    public void forceUniqueValue(String name) {
        extend.forceUniqueValue(name);
    }

    public void allowMultipleValues(String name) {
        extend.allowMultipleValues(name);
    }

    public boolean isUniqueValue(String name) {
        return extend.isUniqueValue(name);
    }

    public void clearName(String name) {
        extend.clearName(name);
    }


    public DSMarkerValue[] getMarkerValues() {
        return markerArray;
    }

    public DSMarkerValue getMarkerValue(DSGeneMarker mInfo) {
    	if(mInfo==null) return null;
    	
        int markerIndex = mInfo.getSerial();
    	if(markerIndex<0 || markerIndex>=markerArray.length)
    		return null;
    	else
    		return markerArray[markerIndex];
    }

    public DSMarkerValue getMarkerValue(int i) {
        try{
            return markerArray[i];
        }catch(ArrayIndexOutOfBoundsException e){
            CSMarkerValue newAbsentValue = new CSExpressionMarkerValue();
            newAbsentValue.setAbsent();
            return newAbsentValue;
        }
    }

    public DSMicroarray deepCopy() {
        CSMicroarray copy = new CSMicroarray(serial, markerArray.length, label, DSMicroarraySet.DO_NOT_CREATE_VALUE_OBJECT);
        for (int i = 0; i < this.getMarkerNo(); i++) {
            copy.markerArray[i] = (CSMarkerValue) markerArray[i].deepCopy();
        }
        copy.setID(RandomNumberGenerator.getID());
        return copy;
    }

    public void resize(int size) {
        markerArray = new CSMarkerValue[size];
    }

    public float[] getRawMarkerData() {
        DSMarkerValue[] values = getMarkerValues();
        float[] data = new float[values.length];
        for (int j = 0; j < values.length; j++) {
            data[j] = (float) values[j].getValue();
        }
        return data;
    }
}
