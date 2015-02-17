package com.qxu.transpath.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.event.*;

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
public class JTreeFrameSample extends JFrame {
	/**
     * serialVersionUID:TODO.
     * 
     */
    private static final long serialVersionUID = 1L;
    JPanel cp = new JPanel();
	JTree jtree;
	DefaultMutableTreeNode root;

	public JTreeFrameSample() {
		this.setSize(600, 600);
		this.setTitle("try to use tree");
		cp = (JPanel) this.getContentPane();
		cp.setLayout(new BorderLayout());
		root = new DefaultMutableTreeNode("school");
		createTree(root);
		jtree = new JTree(root);
		cp.add(jtree, BorderLayout.CENTER);
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