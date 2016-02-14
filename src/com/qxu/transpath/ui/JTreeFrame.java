package com.qxu.transpath.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.event.*;

import com.qxu.transpath.tree.Node;
import com.qxu.transpath.tree.NodeList;
import com.qxu.transpath.tree.NodeTree;
import com.qxu.transpath.utils.TranspathConstants;

/**
 * 
 * ClassName: JTreeFrame <br/>
 * Description: Show the NodeTree using JTree. <br/>
 * Date: 2014-3-31 下午10:15:02 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 *
 */
public class JTreeFrame extends JFrame {
	/**
     * serialVersionUID:TODO.
     * 
     */
    private static final long serialVersionUID = 1L;
    JPanel cp = new JPanel();
	JFrame subFrame = new JFrame();
	JTree jtree1;
	JTree jtree2;
    DefaultMutableTreeNode root;

	public JTreeFrame() {
	    initJTree();
	    initMenuBar();
	    
	}

    public void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
	    this.setJMenuBar(menuBar);
	    
        JMenu sysMenu = new JMenu("Sys");
        menuBar.add(sysMenu);
        
        Action exitAction = new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };
        JMenuItem sysExitItem = sysMenu.add(exitAction);
        
        JMenu taskMenu = new JMenu("Task");
        menuBar.add(taskMenu);	    
	    
	    JMenuItem taskKeepItem = new JMenuItem("Keep");
	    taskMenu.add(taskKeepItem);

	    JMenu storeMenu = new JMenu("Store");
	    menuBar.add(storeMenu);
	    JMenuItem storeKeepItem = new JMenuItem("Keep");
	    storeMenu.add(storeKeepItem);
	    
	    ActionListener listener = new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }};
	    taskKeepItem.addActionListener(listener);
    }
	
	public void initJTree() {
		this.setSize(1200, 600);
		this.setTitle("STARS");
		cp = (JPanel) this.getContentPane();
		cp.setLayout(new BorderLayout());
        
		//NodeList.keepList("resource/pflist.txt", NodeList.buildFromRoot("qtest"));
        //NodeTree ntree1 = NodeTree.buildFromList(NodeList.buildFromFile("D:\\_TF\\_Update\\TFLib_A2013_1st_2nd_fin.txt"), TranspathConstants.BRANCH_1ST);
      NodeTree ntree1 = NodeTree.buildFromList(NodeList.buildFromFile("D:\\_TF\\_Update\\transpath\\store16.txt"), TranspathConstants.BRANCH_1ST);
//        NodeTree ntree1 = NodeTree.buildFromList(NodeList.buildFromFile("D:\\_TF\\_Update\\TFLib_A2013_0_1_2.txt"), TranspathConstants.BRANCH_1ST);
//        ntree1.appendFromList(NodeList.buildFromFile("D:\\_TF\\_Update\\TFLib_A2014_0_1_2.txt"), TranspathConstants.BRANCH_1ST);
//        ntree1.appendFromList(NodeList.buildFromFile("D:\\_TF\\_Update\\TFLib_A2015_b0928.txt"), TranspathConstants.BRANCH_1ST);
        ntree1.recursivelySort();
		jtree1 = new JTree(ntree1);
        //jtree1 = new JTree(this.buildTestTree());
        JScrollPane spaneLeft = new JScrollPane(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        spaneLeft.setViewportView(jtree1);
        jtree1.revalidate();
        
		//NodeList.keepList("resource/tflist.txt", NodeList.buildFromRoot("D:\\Book\\TFLib\\"));
        NodeTree ntree2 = NodeTree.buildFromList(NodeList.buildFromFile("D:\\_TF\\_Update\\transpath\\store16.txt"), TranspathConstants.BRANCH_2ND);
//      NodeTree ntree2 = NodeTree.buildFromList(NodeList.buildFromFile("D:\\_TF\\_Update\\TFLib_A2013_0_1_2.txt"), TranspathConstants.BRANCH_2ND);
//		ntree2.appendFromList(NodeList.buildFromFile("D:\\_TF\\_Update\\TFLib_A2014_0_1_2.txt"), TranspathConstants.BRANCH_2ND);
//		ntree2.appendFromList(NodeList.buildFromFile("D:\\_TF\\_Update\\TFLib_A2015_b0726.txt"), TranspathConstants.BRANCH_2ND);
        ntree2.recursivelySort();
		jtree2 = new JTree(ntree2);

		jtree2.setShowsRootHandles(true);
		jtree2.setRootVisible(true);
		jtree2.setEditable(true);
		UIManager.getSystemLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
            //UIManager.setLookAndFeel("javax.swing.plaf.mac.MacLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(jtree2);
        
        JScrollPane spaneRight = new JScrollPane(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        spaneRight.setViewportView(jtree2);
        jtree2.revalidate();

        
        
        
        
        JSplitPane mPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, spaneLeft, spaneRight);
		mPane.setContinuousLayout(true);
		mPane.setOneTouchExpandable(true);
		mPane.setSize(super.getSize());
		mPane.setDividerLocation(0.5);
		mPane.setDividerSize(8);
		cp.add(mPane, BorderLayout.CENTER);
		
		
	}
	
	@SuppressWarnings("unused")
    private NodeTree buildTestTree() {
        NodeTree tree1 = new NodeTree(new Node(11, "tree1"));
        NodeTree node1 = new NodeTree(new Node(1, "node1"));
        tree1.addChild(node1);
        NodeTree node2 = new NodeTree(new Node(2, "node2"));
        tree1.addChild(node2);
        NodeTree node3 = new NodeTree(new Node(3, "node3"));
        NodeTree tree2 = new NodeTree(new Node(12, "tree2"));
        tree2.addChild(node3);
        tree1.addChild(tree2);
        node3.addChild(new NodeTree(new Node(4, "node4")));
        return tree1;
	}

	@SuppressWarnings("unused")
    private void createTree() {
	    DefaultMutableTreeNode root = new DefaultMutableTreeNode("school");
		DefaultMutableTreeNode classroom = null;
		DefaultMutableTreeNode number = null;
		classroom = new DefaultMutableTreeNode("classroom");
		root.add(classroom);
		for (int i = 1; i <= 8; i++) {
			number = new DefaultMutableTreeNode("No." + String.valueOf(i));
			if (i == 4) {
				for (int j = 1; j <= 5; j++) {
					number.add(new DefaultMutableTreeNode("seat"
							+ String.valueOf(j)));
				}
			}
			classroom.add(number);
		}
	}

	@Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        JTreeFrame treeFrame = new JTreeFrame();
        treeFrame.setVisible(true);
    }

}