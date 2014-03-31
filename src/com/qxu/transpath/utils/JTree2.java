package com.qxu.transpath.utils;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.event.*;
import javax.swing.event.*;

/**
 * 
 * ClassName: JTree2 <br/>
 * Description: Not My Code. <br/>
 * Date: 2014-3-31 下午10:15:02 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 *
 */
public class JTree2 extends JFrame {
	JPanel cp = new JPanel();
	JTree jtree;
	DefaultMutableTreeNode root;

	public JTree2() {
		this.setSize(600, 600);
		this.setTitle("try to use tree");
		cp = (JPanel) this.getContentPane();
		cp.setLayout(new BorderLayout());
		root = new DefaultMutableTreeNode("school");
		createTree(root);
		jtree = new JTree(root);
		cp.add(jtree, BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		JTree2 JTree2 = new JTree2();
		JTree2.setVisible(true);
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
}