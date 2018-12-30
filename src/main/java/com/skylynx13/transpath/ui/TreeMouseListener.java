package com.skylynx13.transpath.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.tree.TreePath;

import com.skylynx13.transpath.store.StoreNode;
import com.skylynx13.transpath.tree.BranchNode;
import com.skylynx13.transpath.tree.Node;
import com.skylynx13.transpath.tree.NodeTree;

/**
 * ClassName: TreeMouseListener
 * Description: Tree mouse listener
 * Date: 2017-10-23 11:14:43
 */
public class TreeMouseListener implements MouseListener {

    private static final String[] POPITEMS = {"New Child", "Rename", "Cut", "Copy", "Paste", "Delete"};

    private JTree jTree;

    private JPopupMenu popupMenu;

    TreeMouseListener(JTree pTree) {
        jTree = pTree;
        popupMenu = new JPopupMenu();
        for (String item : POPITEMS) {
            popupMenu.add(new JMenuItem(item));
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (jTree.getSelectionPath() == null) {
            return;
        }
        NodeTree selectTree = (NodeTree)jTree.getSelectionPath().getLastPathComponent();

        JTextArea infoTextArea = TranspathFrame.getInfoTextArea();
        infoTextArea.setText("Tree Selected: " + selectTree.toString()+"\n");

        Node selectNode = selectTree.getNode();
        if (selectNode instanceof StoreNode) {
            infoTextArea.append("Store Node Selected: " + selectNode.keepNode()+"\n");
        } else if (selectNode instanceof BranchNode) {
            infoTextArea.append("Simple Node Selected: " + selectNode.keepNode()+"\n");
        }
        TranspathFrame.setInfoTable(selectTree);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        TreePath selPath = jTree.getPathForLocation(e.getX(), e.getY());
        if (selPath == null) {
            return;
        }

        if (e.getButton() == MouseEvent.BUTTON3) {
            popupMenu.show(jTree, e.getX(), e.getY());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
