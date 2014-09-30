package com.qxu.transpath.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.event.*;
import javax.swing.event.*;

import com.qxu.transpath.tree.Node;
import com.qxu.transpath.tree.NodeTree;

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
	JPanel cp = new JPanel();
	JFrame subFrame = new JFrame();
	JTree jtree;
	JTree jtree1;
    DefaultMutableTreeNode root;

	public JTreeFrame() {
		this.setSize(1200, 600);
		this.setTitle("transpath");
		cp = (JPanel) this.getContentPane();
		cp.setLayout(new BorderLayout());
        root = new DefaultMutableTreeNode("school");
        
        NodeTree tree1 = new NodeTree(new Node(11, "tree1"));
        NodeTree node1 = new NodeTree(new Node(1, "node1"));
        tree1.addChild(node1);
        NodeTree node2 = new NodeTree(new Node(2, "node2"));
        tree1.addChild(node2);
        NodeTree node3 = new NodeTree(new Node(3, "node3"));
        NodeTree tree2 = new NodeTree(new Node(12, "tree2"));
        tree2.addChild(node3);
        tree1.addChild(tree2);

		createTree(root);
		jtree = new JTree(root);
		jtree1 = new JTree(tree1);
		JSplitPane mPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jtree, jtree1);
		mPane.setContinuousLayout(true);
		mPane.setOneTouchExpandable(true);
		mPane.setSize(super.getSize());
		mPane.setDividerLocation(0.5);
		mPane.setDividerSize(8);
        cp.add(mPane, BorderLayout.CENTER);
	}

	private void createTree(DefaultMutableTreeNode root) {
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