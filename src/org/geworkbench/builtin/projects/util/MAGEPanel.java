package org.geworkbench.builtin.projects.util;

//import DBInterface.DBPool;
//import MAGE.*;
import org.geworkbench.bison.parsers.resources.MAGEResource;
import org.geworkbench.builtin.projects.LoadData;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


/**
 * <p>Title: Plug And Play</p>
 * <p>Description: Dynamic Proxy Implementation of enGenious</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: First Genetic Trust Inc.</p>
 *
 * @author Manjunath Kustagi
 * @version 1.0
 */

public class MAGEPanel extends JPanel {

//    /**
//     * Stores experiments from the remote server.
//     */
//    protected MAGEExperiment[] experiments = null;
//
//    /**
//     * Used to avoid querying the server for all experiments all the time.
//     */
//    protected boolean experimentsLoaded = false;
//
//    private JPanel jPanel6 = new JPanel();
//    private GridLayout grid4 = new GridLayout();
//    private JPanel jPanel7 = new JPanel();
//    private JPanel jPanel8 = new JPanel();
//    private BorderLayout borderLayout4 = new BorderLayout();
//    private BorderLayout borderLayout7 = new BorderLayout();
//    private Border border1 = null;
//    private Border border2 = null;
//    private JScrollPane jScrollPane1 = new JScrollPane();
//    private JPanel jPanel10 = new JPanel();
//    private JLabel jLabel4 = new JLabel();
//    private JScrollPane jScrollPane2 = new JScrollPane();
//    private JPanel jPanel14 = new JPanel();
//    private JPanel jPanel15 = new JPanel();
//    private JPanel jPanel16 = new JPanel();
//    private JLabel measuredLabel = new JLabel("Measured");
//    private JLabel derivedLabel = new JLabel("Derived");
//    private JTextField measuredField = new JTextField();
//    private JTextField derivedField = new JTextField();
//    private JTextArea experimentInfoArea = new JTextArea();
//    private JPanel jPanel13 = new JPanel();
//    private JButton openButton = new JButton();
//    private JButton cancelButton = new JButton();
//    private LoadData parent = null;
//    private DefaultMutableTreeNode root = new DefaultMutableTreeNode("MAGE experiments");
//    private DefaultTreeModel remoteTreeModel = new DefaultTreeModel(root);
//    private JTree remoteFileTree = new JTree(remoteTreeModel);
//    private JPopupMenu jRemoteDataPopup = new JPopupMenu();
//    private JMenuItem jGetRemoteDataMenu = new JMenuItem();
//
//    public MAGEPanel(LoadData p) {
//        parent = p;
//        try {
//            jbInit();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void jbInit() throws Exception {
//        border1 = BorderFactory.createLineBorder(SystemColor.controlText, 1);
//        border2 = BorderFactory.createLineBorder(SystemColor.controlText, 1);
//        grid4.setColumns(2);
//        grid4.setHgap(10);
//        grid4.setRows(1);
//        grid4.setVgap(10);
//        jLabel4.setMaximumSize(new Dimension(200, 15));
//        jLabel4.setMinimumSize(new Dimension(200, 15));
//        jLabel4.setPreferredSize(new Dimension(200, 15));
//        jLabel4.setText("Experiment Information:");
//
//        jPanel13.setLayout(new BoxLayout(jPanel13, BoxLayout.X_AXIS));
//        jPanel13.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
//        jPanel13.add(Box.createHorizontalGlue());
//        jPanel13.add(openButton);
//        jPanel13.add(Box.createRigidArea(new Dimension(10, 0)));
//        jPanel13.add(cancelButton);
//
//        jPanel15.setLayout(new BoxLayout(jPanel15, BoxLayout.Y_AXIS));
//        jPanel15.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
//        jPanel15.add(Box.createVerticalGlue());
//        jPanel15.add(measuredLabel);
//        jPanel15.add(Box.createRigidArea(new Dimension(10, 0)));
//        jPanel15.add(measuredField);
//        measuredField.setEditable(false);
//
//        openButton.setPreferredSize(new Dimension(73, 25));
//        openButton.setText("Open");
//        openButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                openRemoteFile_action(e);
//            }
//        });
//        cancelButton.setMinimumSize(new Dimension(73, 25));
//        cancelButton.setPreferredSize(new Dimension(73, 25));
//        cancelButton.setText("Cancel");
//        cancelButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                jButton1_actionPerformed(e);
//            }
//        });
//
//        jPanel16.setLayout(new BoxLayout(jPanel16, BoxLayout.Y_AXIS));
//        jPanel16.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
//        jPanel16.add(Box.createVerticalGlue());
//        jPanel16.add(derivedLabel);
//        jPanel16.add(Box.createRigidArea(new Dimension(10, 0)));
//        jPanel16.add(derivedField);
//        derivedField.setEditable(false);
//        jScrollPane2.getViewport().add(experimentInfoArea, null);
//        jScrollPane2.setPreferredSize(new Dimension(300, 300));
//        jPanel14.setLayout(new BoxLayout(jPanel14, BoxLayout.X_AXIS));
//        jPanel14.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
//        jPanel14.add(Box.createHorizontalGlue());
//        jPanel14.add(jPanel15);
//        jPanel14.add(Box.createRigidArea(new Dimension(10, 0)));
//        jPanel14.add(jPanel16);
//
//        experimentInfoArea.setPreferredSize(new Dimension(300, 300));
//        experimentInfoArea.setText("");
//        experimentInfoArea.setEditable(false);
//        experimentInfoArea.setLineWrap(true);
//        experimentInfoArea.setWrapStyleWord(true);
//
//        jPanel6.setLayout(grid4);
//        jPanel8.setLayout(borderLayout4);
//        jPanel7.setLayout(borderLayout7);
//        jPanel10.setLayout(new BoxLayout(jPanel10, BoxLayout.Y_AXIS));
//        jPanel10.add(jLabel4, null);
//        jPanel10.add(Box.createRigidArea(new Dimension(0, 10)));
//        jPanel10.add(jScrollPane2, null);
//        jPanel10.add(Box.createRigidArea(new Dimension(0, 10)));
//        jPanel10.add(jPanel14, null);
//        jPanel10.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        jPanel7.setBorder(border1);
//        jPanel8.setBorder(border2);
//        jScrollPane1.getViewport().add(remoteFileTree, null);
//        jScrollPane1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        jPanel8.add(jScrollPane1, BorderLayout.CENTER);
//        jPanel7.add(jPanel10, BorderLayout.CENTER);
//        jPanel7.add(jPanel13, BorderLayout.SOUTH);
//        jPanel6.setMaximumSize(new Dimension(675, 339));
//        jPanel6.setMinimumSize(new Dimension(675, 339));
//        jPanel6.setPreferredSize(new Dimension(675, 339));
//        jPanel6.add(jPanel8);
//        jPanel6.add(jPanel7);
//
//        remoteFileTree.setToolTipText("");
//        // Only one remote file can be selected at a time;
//        remoteFileTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
//        remoteFileTree.addTreeSelectionListener(new TreeSelectionListener() {
//            public void valueChanged(TreeSelectionEvent tse) {
//                remoteFileSelection_action(tse);
//            }
//        });
//        remoteFileTree.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseReleased(MouseEvent e) {
//                jRemoteFileTree_mouseReleased(e);
//            }
//        });
//        jGetRemoteDataMenu.setText("Get bioassays");
//        ActionListener listener = new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                jGetRemoteData_actionPerformed(e);
//            }
//        };
//        jGetRemoteDataMenu.addActionListener(listener);
//        jRemoteDataPopup.add(jGetRemoteDataMenu);
//        jPanel6.setMaximumSize(new Dimension(500, 300));
//        jPanel6.setMinimumSize(new Dimension(500, 300));
//        jPanel6.setPreferredSize(new Dimension(500, 300));
//        this.add(jPanel6, BorderLayout.CENTER);
//    }
//
//    /**
//     * Action listener invoked when the user presses the "Open" button
//     * after having selected a remote microarray. The listener will attempt
//     * to get the microarray data from the remote server and load them in the
//     * application.
//     *
//     * @param e
//     */
//    private void openRemoteFile_action(ActionEvent e) {
//        TreePath[] paths = remoteFileTree.getSelectionPaths();
//        Experiment exp = null;
//        if (paths.length > 0)
//            exp = ((MAGEExperiment) ((DefaultMutableTreeNode) paths[0].getPath()[1]).getUserObject()).getExperiment();
//        Vector tempAssays = new Vector();
//        boolean multipleAssays = false;
//        for (int i = 0; i < paths.length; i++) {
//            DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[i].getLastPathComponent();
//            if (node != null && (node.getUserObject() instanceof MAGEBioassay)) {
//                if (exp == ((MAGEExperiment) ((DefaultMutableTreeNode) node.getPath()[1]).getUserObject()).getExperiment()) {
//                    MAGEBioassay ba = (MAGEBioassay) node.getUserObject();
//                    if (ba != null) {
//                        tempAssays.add(ba.getBioAssayImpl());
//                    } else {
//                        JOptionPane.showMessageDialog(null, "No data in selected experiment " + ba.getBioAssayImpl().getName(), "Remote Open File Error", JOptionPane.ERROR_MESSAGE);
//                        return;
//                    }
//                } else {
//                    multipleAssays = true;
//                }
//            }
//        }
//        if (multipleAssays)
//            JOptionPane.showMessageDialog(null, "BioAssay selections only from " + exp.getName() + " will be used", "Multiple Experiment Selections", JOptionPane.WARNING_MESSAGE);
//
//        BioAssay[] assays = new BioAssay[tempAssays.size()];
//        assays = (BioAssay[]) tempAssays.toArray(assays);
//        if (exp != null && assays.length > 0)
//            parent.parentProjectPanel.remoteFileOpenAction(new MAGEResource(assays, exp));
//
//        dispose();
//        return;
//    }
//
//    /**
//     * This method is called if the cancel button is pressed.
//     *
//     * @param e - Event information.
//     */
//    private void jButton1_actionPerformed(ActionEvent e) {
//        dispose();
//    }
//
//    public void getExperiments(ActionEvent e) {
//        if (!experimentsLoaded) {
//            experiments = getExperiments();
//            if (experiments == null) {
//                JOptionPane.showMessageDialog(null, "Unable to connect to server.", "Open File Error", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//
//            for (int i = 0; i < experiments.length; ++i) {
//                DefaultMutableTreeNode node = new DefaultMutableTreeNode(experiments[i]);
//                remoteTreeModel.insertNodeInto(node, root, root.getChildCount());
//            }
//            remoteFileTree.expandRow(0);
//            experimentsLoaded = true;
//        }
//        this.validate();
//        this.repaint();
//    }
//
//    /**
//     * Menu action listener that brings up the popup menu that allows
//     * populating the tree node for a remote file.
//     *
//     * @param e
//     */
//    private void jRemoteFileTree_mouseReleased(MouseEvent e) {
//        TreePath path = remoteFileTree.getPathForLocation(e.getX(), e.getY());
//        if (path != null) {
//            Object selectedNode = ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
//            if (e.isMetaDown() && (selectedNode instanceof MAGEExperiment)) {
//                jRemoteDataPopup.show(remoteFileTree, e.getX(), e.getY());
//                if (((MAGEExperiment) selectedNode).hasBeenPopulated()) {
//                    jGetRemoteDataMenu.setEnabled(false);
//                } else {
//                    jGetRemoteDataMenu.setEnabled(true);
//                }
//            }
//        }
//    }
//
//    /**
//     * Action listener invoked when a remote file is selected in the remote file
//     * tree. Updates the experiment information text area.
//     *
//     * @param lse
//     */
//    private void remoteFileSelection_action(TreeSelectionEvent tse) {
//        if (tse == null) {
//            return;
//        }
//        updateTextArea(); // Update the contents of the text area.
//    }
//
//    /**
//     * Properly update the experiment text area, based on the currently
//     * selected node.
//     */
//    private void updateTextArea() {
//        DefaultMutableTreeNode node = (DefaultMutableTreeNode) remoteFileTree.getLastSelectedPathComponent();
//        if (node == null || node == root || experiments == null || experiments.length == 0) {
//            experimentInfoArea.setText("");
//            measuredField.setText("");
//            derivedField.setText("");
//        } else {
//            // get the parent experiment.
//            MAGEExperiment exp = (MAGEExperiment) ((DefaultMutableTreeNode) node.getPath()[1]).getUserObject();
//            experimentInfoArea.setText(exp.getExperimentInformation());
//            measuredField.setText(String.valueOf(exp.getMeasuredBioassaysCount()));
//            derivedField.setText(String.valueOf(exp.getDerivedBioassaysCount()));
//        }
//        experimentInfoArea.setCaretPosition(0); // For long text.
//    }
//
//    /**
//     * Populate the subtree corresponding to an Experiment node with data
//     * from the remote server.
//     *
//     * @param e
//     */
//    private void jGetRemoteData_actionPerformed(ActionEvent e) {
//        if (e == null) {
//            return;
//        }
//        DefaultMutableTreeNode node = (DefaultMutableTreeNode) remoteFileTree.getLastSelectedPathComponent();
//        if (node != null && node != root && experiments != null && experiments.length != 0) {
//            // Lazy computation of the children on an experiment.
//            DefaultMutableTreeNode assayNode = null;
//            DefaultMutableTreeNode dataNode = null;
//            if (node.getUserObject() instanceof MAGEExperiment) {
//                MAGEExperiment exp = (MAGEExperiment) node.getUserObject();
//                // Add in the tree the measured bioassays
//                if (!exp.hasBeenPopulated()) {
//                    MAGEBioassay[] measured = exp.getMeasuredAssays();
//                    if (measured != null && measured.length > 0) {
//                        for (int i = 0; i < measured.length; ++i) {
//                            assayNode = new DefaultMutableTreeNode(measured[i]);
//                            remoteTreeModel.insertNodeInto(assayNode, node, node.getChildCount());
//                        }
//                    }
//                    // Add in the tree the derived bioassays
//                    MAGEBioassay[] derived = exp.getDerivedAssays();
//                    if (derived != null && derived.length > 0) {
//                        for (int i = 0; i < derived.length; ++i) {
//                            assayNode = new DefaultMutableTreeNode(derived[i]);
//                            remoteTreeModel.insertNodeInto(assayNode, node, node.getChildCount());
//                        }
//                    }
//                    exp.setPopulated();
//                    remoteFileTree.expandPath(new TreePath(node.getPath()));
//                }
//            }
//        }
//    }
//
//    /**
//     * Gets a list of all experiments available on the remote server.
//     */
//    public MAGEExperiment[] getExperiments() {
//        MAGEExperiment[] exps = new MAGEExperiment[0];
//        Vector temp = new Vector();
//        try {
//            long low = 0, high = 10000000;
//
//            if (DBPool.getPool() == null)
//                DBPool.create(10);
//
//            Experiment.loadIntoCache(low, high);
//            Hashtable cache = Experiment.cache;
//            Enumeration keys = cache.keys();
//            for (BigDecimal index = null; keys.hasMoreElements();) {
//                index = (BigDecimal) keys.nextElement();
//                if (cache.get(index) instanceof Experiment) {
//                    Experiment e = (Experiment) cache.get(index);
//                    e.getBioAssays();
//                    e.getAnalysisResults();
//                    Vector bad = e.getBioAssayData();
//                    for (int y = 0; y < bad.size(); y++) {
//                        ((BioAssayData) bad.get(y)).getBioAssayDimension_ref();
//                        ((BioAssayData) bad.get(y)).getDesignElementDimension_ref();
//                        ((BioAssayData) bad.get(y)).getQuantitationTypeDimension_ref();
//                    }
//                    MAGEExperiment exp = new MAGEExperiment(e);
//                    if (exp.isPlatformTypeUsable()) {
//                        temp.add(exp);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//        Collections.sort(temp);
//        exps = new MAGEExperiment[temp.size()];
//        //dump the experiment objects into the return array.
//        temp.toArray(exps);
//        return exps;
//    }
//
//    private void dispose() {
//        parent.dispose();
//    }
//
//    /**
//     * This class provides a GUI wrapper for a BioAssayImpl object.  This object will
//     * be displayed as a node in the JTree.
//     */
//    class MAGEBioassay {
//        private Long m_id = null;
//        private BioAssay ba = null;
//        private BioAssayData[] baData = null;
//        private int dataCount = 0;
//        // Convenience flag.
//        private boolean countFlag = false;
//        // Convenience flag.
//        private boolean flag = false;
//
//        public MAGEBioassay(BioAssay b) {
//            ba = b;
//            try {
//                m_id = new Long(b.getId().longValue());
//            } catch (Exception e) {
//                System.out.println("Error getting id for bioasay: " + e.toString());
//                e.printStackTrace();
//            }
//        }
//
//        public long getId() {
//            long id = -1;
//            if (m_id != null) {
//                id = m_id.longValue();
//            }
//            return id;
//        }
//
//        public BioAssay getBioAssayImpl() {
//            return ba;
//        }
//
//        public int getDataCount() {
//            if (ba == null) {
//                return 0;
//            }
//            if (countFlag) {
//                return dataCount;
//            }
//            try {
//                if (ba instanceof MeasuredBioAssay) {
//                    dataCount = ((MeasuredBioAssay) ba).getMeasuredBioAssayData().size();
//                } else if (ba instanceof DerivedBioAssay) {
//                    dataCount = ((DerivedBioAssay) ba).getDerivedBioAssayData().size();
//                } else {
//                    dataCount = 0;
//                }
//            } catch (Exception e) {
//                return 0;
//            }
//            countFlag = true;
//            return dataCount;
//        }
//
//        BioAssayData[] getData() {
//            if (ba == null) {
//                return null;
//            }
//            if (flag) {
//                return baData;
//            }
//            try {
//                if (ba instanceof MeasuredBioAssay) {
//                    ((MeasuredBioAssay) ba).getMeasuredBioAssayData().toArray(baData);
//                } else if (ba instanceof DerivedBioAssay) {
//                    ((DerivedBioAssay) ba).getDerivedBioAssayData().toArray(baData);
//                } else {
//                    baData = null;
//                }
//            } catch (Exception e) {
//                return null;
//            }
//            flag = true;
//            return baData;
//        }
//
//        /**
//         * Return the display name for the bioassay.
//         */
//        public String toString() {
//            if (ba == null) {
//                return "null";
//            } else if (ba.getName() != null) {
//                return ba.getName();
//            } else if (ba instanceof MeasuredBioAssay) {
//                StringBuffer name = new StringBuffer("Measured");
//                if (m_id != null) {
//                    name.append(": ");
//                    name.append(m_id);
//                }
//                return name.toString();
//            } else if (ba instanceof DerivedBioAssay) {
//                StringBuffer name = new StringBuffer("Derived");
//                if (m_id != null) {
//                    name.append(":");
//                    name.append(m_id);
//                }
//                return name.toString();
//            } else {
//                return "Unknown";
//            }
//        }
//    }
//
//    /**
//     * This class represents an experiment in the load data JTree.
//     */
//    class MAGEExperiment implements Comparable {
//        /**
//         * The ID of this experiment.
//         */
//        Long m_id = null;
//        /**
//         * The name of this experiment.
//         */
//        String m_name = null;
//        String m_platform = "Unknown";
//        /**
//         * The reference <code>ExperimentImpl</code> object.
//         */
//        Experiment exp = null;
//        /**
//         * The number of measured Bioassays that are part of the
//         * experiment.
//         */
//        private int measuredNum = 0;
//
//        /**
//         * The number of derived Bioassays that are part of the
//         * experiment.
//         */
//        private int derivedNum = 0;
//
//        /**
//         * Convenience flag
//         */
//        private boolean expInfoComputed = false;
//
//        /**
//         * Convenience flags.
//         */
//        private boolean hasBeenComputedFlag = false;
//
//        /**
//         * The text of the experiment information
//         */
//        private String experimentInfo = "";
//
//        /**
//         * The list of measured bioassays in the experiment.
//         */
//        private MAGEBioassay[] measuredAssays = null;
//
//        /**
//         * The list of derived bioassays in the experiment.
//         */
//        private MAGEBioassay[] derivedAssays = null;
//
//        /**
//         * This is set to true when the tree node corresponding to the
//         * experiment has been populated with the proper bioassays.
//         */
//        private boolean treeNodePopulated = false;
//
//        /**
//         * Creates a new experiment object that will wrap the specified ExperimentImpl.
//         *
//         * @param e - The ExperimentImpl that this object represents.
//         */
//        public MAGEExperiment(Experiment e) {
//            exp = e;
//            //get the experiment id and name and store them locally
//            try {
//                m_id = new Long(e.getId().longValue());
//                m_name = e.getName();
//                //m_platform = (String)e.getProviders().get(0);
//            } catch (Exception ex) {
//                System.out.println("Error getting experiment information: " + ex.toString());
//                ex.printStackTrace();
//            }
//            computeArrayNum();
//        }
//
//        /**
//         * Returns the ID of this experiment if known, -1 otherwise.
//         */
//        public long getId() {
//            long id = -1;
//            if (m_id != null) {
//                id = m_id.longValue();
//            }
//            return id;
//        }
//
//        /**
//         * Returns the platform of this experiment.
//         */
//        public String getPlatform() {
//            return m_platform;
//        }
//
//        /**
//         * Checks if the platform of this experiment is usable by caWorkbench.
//         *
//         * @return true if the platform type is usable, false otherwise.
//         */
//        public boolean isPlatformTypeUsable() {
//            boolean ret = false;
//            if (isGenepix() || isAffymetrix()) {
//                ret = true;
//            }
//            return true;
//        }
//
//        /**
//         * Checks if the experiment platform is Affymetrix.
//         *
//         * @return <code>true</code> if the experiment platform is Affy.
//         *         <code>false</code> otherwise.
//         */
//        public boolean isAffymetrix() {
//            boolean ret = false;
//            if (m_platform.equalsIgnoreCase("Affymetrix") || m_platform.equalsIgnoreCase("Affy-MAS 5.0")) {
//                ret = true;
//            }
//            return ret;
//        }
//
//        /**
//         * Checks if the experiment platform is Genepix.
//         *
//         * @return <code>true</code> if the experiment platform is Genepix.
//         *         <code>false</code> otherwise.
//         */
//        public boolean isGenepix() {
//            boolean ret = false;
//            if (m_platform.equalsIgnoreCase("Long Oligo - Spotted") || m_platform.equalsIgnoreCase("cDNA - Spotted")) {
//                ret = true;
//            }
//            return ret;
//        }
//
//        /**
//         * Return the number of measured bioassays in the experiment.
//         *
//         * @return
//         */
//        public int getMeasuredBioassaysCount() {
//            computeArrayNum();
//            return measuredNum;
//        }
//
//        /**
//         * Return the number of derived bioassays in the experiment.
//         *
//         * @return
//         */
//        public int getDerivedBioassaysCount() {
//            computeArrayNum();
//            return derivedNum;
//        }
//
//        /**
//         * Returns the experiment name, which will be displayed within
//         * the experiment tree. This method is impicitly invoked by the
//         * <code>DefaultMutableTreeNode</code> "hosting" this experiment
//         * within the experiment tree.
//         *
//         * @return The name of this experiment.
//         */
//        public String toString() {
//            StringBuffer nameBuff = new StringBuffer();
//            if (m_id != null) {
//                nameBuff.append(m_id);
//                nameBuff.append(": ");
//            }
//
//            if (m_name != null) {
//                nameBuff.append(m_name);
//            }
//            nameBuff.append(" - ");
//            nameBuff.append(getPlatform());
//            return nameBuff.toString();
//        }
//
//        /**
//         * This method will compare this Experiment to another object.
//         *
//         * @param rhs - The object to compare to.
//         * @return 0 if the objects are equal or cannot be compared, negative int if this object
//         *         has an ID less than that of rhs object, positive otherwise.
//         */
//        public int compareTo(Object rhs) {
//            int ret = 0;
//            if (rhs instanceof MAGEExperiment) {
//                ret = (int) (getId() - ((MAGEExperiment) rhs).getId());
//            }
//            return ret;
//        }
//
//        /**
//         * Returns the wrapped ExperimentImpl object.
//         */
//        public Experiment getExperiment() {
//            return exp;
//        }
//
//        /**
//         * Return the information associated with this experiment.
//         *
//         * @return
//         */
//        public String getExperimentInformation() {
//            if (exp != null && !expInfoComputed) {
//                Description[] desc = new Description[exp.getDescriptions().size()];
//                exp.getDescriptions().toArray(desc);
//                if (desc == null || desc.length == 0) {
//                    experimentInfo = "";
//                } else {
//                    return experimentInfo = desc[0].getText();
//                }
//                expInfoComputed = true;
//            }
//            return experimentInfo;
//        }
//
//        /**
//         * Return an array containing the measured bioassays (if there are any).
//         *
//         * @return
//         */
//        public MAGEBioassay[] getMeasuredAssays() {
//            computeArrayNum();
//            return measuredAssays;
//        }
//
//        /**
//         * Return an array containing the derived bioassays (if there are any).
//         *
//         * @return
//         */
//        public MAGEBioassay[] getDerivedAssays() {
//            computeArrayNum();
//            return derivedAssays;
//        }
//
//        public boolean hasBeenPopulated() {
//            return treeNodePopulated;
//        }
//
//        public void setPopulated() {
//            treeNodePopulated = true;
//        }
//
//        /**
//         * Compute the number of measured and derived bioassays in the
//         * experiment.
//         */
//        private void computeArrayNum() {
//            // No need for repeated computation.
//            if (hasBeenComputedFlag || exp == null) {
//                return;
//            }
//            try {
//                measuredNum = derivedNum = 0;
//                Vector bioassays = exp.getBioAssays();
//                if (bioassays.size() > 0) {
//                    for (int j = 0; j < bioassays.size(); ++j) {
//                        BioAssay_POLY bap = (BioAssay_POLY) bioassays.get(j);
//                        if (bap.getMeasuredBioAssay_ref() != null) {
//                            measuredNum++;
//                        } else if (bap.getDerivedBioAssay_ref() != null) {
//                            derivedNum++;
//                        }
//                    }
//                }
//
//                // Allocate space for the assays.
//                if (bioassays.size() > 0) {
//                    measuredAssays = new MAGEBioassay[measuredNum];
//                    derivedAssays = new MAGEBioassay[derivedNum];
//                    int k = 0, l = 0;
//                    for (int j = 0; j < bioassays.size(); ++j) {
//                        BioAssay_POLY bap = (BioAssay_POLY) bioassays.get(j);
//                        if (bap.getMeasuredBioAssay_ref() != null) {
//                            measuredAssays[k++] = new MAGEBioassay(bap.getMeasuredBioAssay_ref());
//                        } else if (bap.getDerivedBioAssay_ref() != null) {
//                            derivedAssays[l++] = new MAGEBioassay(bap.getDerivedBioAssay_ref());
//                        }
//                    }
//                }
//                hasBeenComputedFlag = true;
//            } catch (Exception e) {
//                measuredNum = derivedNum = 0;
//                measuredAssays = derivedAssays = null;
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//        JFrame frame = new JFrame();
//        MAGEPanel panel = new MAGEPanel(null);
//        frame.setSize(300, 200);
//        frame.getContentPane().add(panel, BorderLayout.CENTER);
//        frame.setVisible(true);
//    }
}
