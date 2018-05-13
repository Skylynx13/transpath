/**
 * Copyright (c) 2017,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.libra42.transpath.ui
 * File Name:TreeMouseListener.java
 * Date:2017年10月23日 上午11:14:43
 * 
 */
package com.skylynx13.transpath.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import com.skylynx13.transpath.pub.PubNode;
import com.skylynx13.transpath.store.StoreNode;
import com.skylynx13.transpath.tree.Node;
import com.skylynx13.transpath.tree.NodeTree;
import com.skylynx13.transpath.tree.SimpleNode;

/**
 * ClassName: TreeMouseListener <br/>
 * Description: TODO <br/>
 * Date: 2017年10月23日 上午11:14:43 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class TreeMouseListener implements MouseListener {
    
    JTree jTree = null;
    
    public TreeMouseListener(JTree pTree) {
        jTree = pTree;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (jTree.getSelectionPath() == null) {
            return;
        }
        NodeTree selectTree = (NodeTree)jTree.getSelectionPath().getLastPathComponent();
        Node selectNode = selectTree.getNode();
        TranspathFrame.getInfoTextArea().setText("Tree Selected: " + selectTree.toString()+"\n");
        String nodeType = "";
        if (selectNode instanceof StoreNode) {
            TranspathFrame.getInfoTextArea().append("Store Node Selected: " + selectNode.keepNode()+"\n");
            TranspathFrame.getInfoTextArea().append("Related Pub Node: " + TranspathFrame.getCurrPubList().getListByIds(TranspathFrame.getCurrLinkList().getPubIdList(selectNode.id))+"\n");
        } else if (selectNode instanceof PubNode) {
            TranspathFrame.getInfoTextArea().append("Pub Node Selected: " + selectNode.keepNode()+"\n");
            TranspathFrame.getInfoTextArea().append("Related Store Node: " + TranspathFrame.getCurrStoreList().getListByIds(TranspathFrame.getCurrLinkList().getStoreIdList(selectNode.id))+"\n");
        } else if (selectNode instanceof SimpleNode) {
            TranspathFrame.getInfoTextArea().append("Simple Node Selected: " + selectNode.keepNode()+"\n");
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
            JPopupMenu popupMenu = new JPopupMenu();
            popupMenu.add(new JMenuItem("Cut"));
            popupMenu.add(new JMenuItem("Copy"));
            popupMenu.add(new JMenuItem("Paste"));
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
