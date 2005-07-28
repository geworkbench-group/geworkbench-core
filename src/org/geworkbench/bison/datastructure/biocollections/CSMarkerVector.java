package org.geworkbench.bison.datastructure.biocollections;

import org.geworkbench.bison.datastructure.bioobjects.markers.DSGeneMarker;
import org.geworkbench.bison.datastructure.complex.panels.CSSequentialItemList;
import org.geworkbench.bison.datastructure.complex.panels.DSItemList;
import org.geworkbench.bison.util.HashVector;

import java.util.List;
import java.util.Vector;

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
public class CSMarkerVector extends CSSequentialItemList<DSGeneMarker> implements DSItemList<DSGeneMarker> {

    HashVector<Integer, DSGeneMarker> geneIdMap = new HashVector<Integer, DSGeneMarker>(true);
    HashVector<String, DSGeneMarker> geneNameMap = new HashVector<String, DSGeneMarker>(true);
    boolean mapGeneNames = true;

    public CSMarkerVector() {
    }

    public CSMarkerVector(List<DSGeneMarker> markerList) {
        super();
        this.addAll(markerList);
    }

    public DSGeneMarker getMarkerByUniqueIdentifier(String label) {
        return super.get(label);
    }

    public Vector<DSGeneMarker> getMatchingMarkers(String aString) {
        Vector<DSGeneMarker> matchingMarkers = new Vector<DSGeneMarker>();
        DSGeneMarker uniqueKeyMarker = super.get(aString);
        if (uniqueKeyMarker != null) {
            matchingMarkers.add(uniqueKeyMarker);
        }
        try {
            Vector<DSGeneMarker> markersSet;
            if (mapGeneNames) {
                markersSet = geneNameMap.get(aString);
                if (markersSet != null && markersSet.size() > 0) {
                    for (DSGeneMarker marker : markersSet) {
                        if (!matchingMarkers.contains(marker)) {
                            matchingMarkers.add(marker);
                        }
                    }
                }

                Integer geneId = Integer.parseInt(aString);
                markersSet = geneIdMap.get(geneId);
                if (markersSet != null && markersSet.size() > 0) {
                    for (DSGeneMarker marker : markersSet) {
                        if (!matchingMarkers.contains(marker)) {
                            matchingMarkers.add(marker);
                        }
                    }
                }


            }
        } catch (Exception e) {

        }
        return matchingMarkers;
    }

    public Vector<DSGeneMarker> getMatchingMarkers(int geneId) {
        return getMatchingMarkers(new Integer(geneId));
    }


    public Vector<DSGeneMarker> getMatchingMarkers(Integer geneId) {
        if (geneId.intValue() == -1) {
            return null;
        }

        return geneIdMap.get(geneId);
    }

    public DSGeneMarker get(String aString) {
        if (aString == null) {
            return null;
        }

        DSGeneMarker marker = super.get(aString);
        if (marker == null) {
            Vector<DSGeneMarker> matchingMarkers = getMatchingMarkers(aString);
            if (matchingMarkers != null && matchingMarkers.size() > 0) {
                marker = matchingMarkers.get(0);
            }
        }
        return marker;
    }

    public boolean add(DSGeneMarker item) {
        boolean result = false;
        if (item != null) {
            if (!this.contains(item)) {
                result = super.add(item);
                if (result) {
                    Integer geneId = new Integer(item.getGeneId());
                    if (geneId != null && geneId.intValue() != -1) {
                        geneIdMap.addItem(geneId, item);
                    }

                    if (mapGeneNames) {
                        String geneName = item.getGeneName();
                        if (geneName != null && (!"---".equals(geneName))) {
                            geneNameMap.addItem(geneName, item);
                        }
                    }
                }
            }
        }
        return result;
    }

    public void add(int i, DSGeneMarker item) {
        super.add(i, item);
        Integer geneId = new Integer(item.getGeneId());
        if (geneId != null && geneId.intValue() != -1) {
            geneIdMap.addItem(geneId, item);
        }

        if (mapGeneNames) {
            String geneName = item.getGeneName();
            if (geneName != null && (!"---".equals(geneName))) {
                geneNameMap.addItem(geneName, item);
            }
        }

    }

    public DSGeneMarker get(DSGeneMarker item) {
        DSGeneMarker marker = super.get(item);
        if (marker == null) {
            Vector<DSGeneMarker> matchingMarkers = getMatchingMarkers(item);
            if (matchingMarkers != null && matchingMarkers.size() > 0) {
                marker = matchingMarkers.get(0);
            }
        }

        //        if(marker == null){
        //            System.out.println("null marker");
        //        }
        return marker;
    }

    public Vector<DSGeneMarker> getMatchingMarkers(DSGeneMarker item) {
        if (mapGeneNames && (item.getGeneName() != null) && (item.getGeneName().length() > 0)) {
            return geneNameMap.get(item.getGeneName());
        } else {
            return geneIdMap.get(new Integer(item.getGeneId()));
        }
    }

    public boolean contains(Object item) {
        if (item instanceof DSGeneMarker) {
            return this.contains((DSGeneMarker) item);
        } else {
            return false;
        }
    }

    public boolean contains(DSGeneMarker item) {
        // Contains must use a strict sense of equality!
        DSGeneMarker marker = super.get(item);
        if (marker != null) {
            return true;
        } else {
            return false;
        }
    }

    //To change
    public void remove(DSGeneMarker item) {
        super.remove(item);
        geneIdMap.remove(new Integer(item.getGeneId()));
    }

    public void clear() {
        super.clear();
        geneIdMap.clear();
    }
}
