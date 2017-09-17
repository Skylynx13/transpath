package com.qxu.transpath.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;

import java.awt.event.*;

import com.qxu.transpath.tree.NodeTree;
import com.qxu.transpath.tree.PubList;
import com.qxu.transpath.tree.SimpleNode;
import com.qxu.transpath.tree.StoreList;
import com.qxu.transpath.utils.FileUtils;
import com.qxu.transpath.utils.TransLog;
import com.qxu.transpath.utils.TransProp;
import com.qxu.transpath.worker.NameEditor;
import com.qxu.transpath.worker.PubKeeper;
import com.qxu.transpath.worker.StoreKeeper;
import com.qxu.transpath.worker.TaskKeeper;

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
	    initMenuBar();
	    initJTree();
        
	}

    @SuppressWarnings({ "serial", "unused" })
    public void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
	    this.setJMenuBar(menuBar);
	    
        JMenu sysMenu = new JMenu("Sys");
        menuBar.add(sysMenu);
        Action reloadAction = new AbstractAction("Reload") {
            @Override
            public void actionPerformed(ActionEvent e) {
                initJTree();
            }
        };
        JMenuItem sysReloadItem = sysMenu.add(reloadAction);
        Action exitAction = new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };
        JMenuItem sysExitItem = sysMenu.add(exitAction);
        
        JMenu taskMenu = new JMenu("Task");
        menuBar.add(taskMenu);	    
	    JMenuItem taskWeekItem = new JMenuItem("Weekly Digging");
	    taskMenu.add(taskWeekItem);
        ActionListener listenerWeek = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TaskKeeper.weekFresh();
                TaskKeeper.digNewFresh();
            }
        };
        taskWeekItem.addActionListener(listenerWeek);
        JMenuItem taskSpecItem = new JMenuItem("Spec Digging");
        taskMenu.add(taskSpecItem);
        ActionListener listenerSpec = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TaskKeeper.digSpecFresh();
            }
        };
        taskSpecItem.addActionListener(listenerSpec);
        JMenuItem taskNameItem = new JMenuItem("Name Revise");
        taskMenu.add(taskNameItem);
        ActionListener listenerName = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NameEditor.renameNormalize();
                NameEditor.renameSpecialReplace();
            }
        };
        taskNameItem.addActionListener(listenerName);

        JMenu storeMenu = new JMenu("Store");
        menuBar.add(storeMenu);
        JMenuItem storeCombineItem = new JMenuItem("Combine");
        storeMenu.add(storeCombineItem);
        storeCombineItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StoreKeeper.buildCombinedList();
            }
        });
        JMenuItem storeCombineTestItem = new JMenuItem("CombineTest");
        storeMenu.add(storeCombineTestItem);
        storeCombineTestItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StoreKeeper.testCombinedList();
            }
        });
        
        JMenu pubMenu = new JMenu("Pub");
        menuBar.add(pubMenu);
        JMenuItem pubRefreshItem = new JMenuItem("Refresh");
        pubMenu.add(pubRefreshItem);
        pubRefreshItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PubKeeper.refreshPubList();
            }
        });
        
        JMenu helpMenu = new JMenu("Help");
        JMenuItem helpAboutItem = new JMenuItem("About");
        helpAboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TransLog.getLogger().error("It's ok anyway.");
                TransLog.getLogger().info("Info can be seen.");
            }
        });
        helpMenu.add(helpAboutItem);
        menuBar.add(helpMenu);
        
    }
	
	public void initJTree() {
		this.setSize(1200, 600);
		this.setTitle("Storage Archivist");
		cp = (JPanel) this.getContentPane();
		cp.setLayout(new BorderLayout());
        
        StoreList aList = new StoreList();
        String storeNameOfVersion = FileUtils.storeNameOfVersion(TransProp.get("CURR_VER"));
        aList.load(storeNameOfVersion);
        TransLog.getLogger().info("List: " + storeNameOfVersion + " loaded.");
        NodeTree ntree1 = NodeTree.buildFromList(aList);
        ntree1.recursivelySort();
		jtree1 = new JTree(ntree1);
        JScrollPane spaneLeft = new JScrollPane(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        spaneLeft.setViewportView(jtree1);
        jtree1.revalidate();
        
        PubList bList = new PubList();
        bList.load(FileUtils.pubNameOfVersion(TransProp.get("CURR_VER")));
        NodeTree ntree2 = NodeTree.buildFromList(bList);
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
		
		this.setVisible(true);
	}
	
	@SuppressWarnings("unused")
    private NodeTree buildTestTree() {
        NodeTree tree1 = new NodeTree(new SimpleNode("tree1"));
        NodeTree node1 = new NodeTree(new SimpleNode("node1"));
        tree1.addChild(node1);
        NodeTree node2 = new NodeTree(new SimpleNode("node2"));
        tree1.addChild(node2);
        NodeTree node3 = new NodeTree(new SimpleNode("node3"));
        NodeTree tree2 = new NodeTree(new SimpleNode("tree2"));
        tree2.addChild(node3);
        tree1.addChild(tree2);
        node3.addChild(new NodeTree(new SimpleNode("node4")));
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