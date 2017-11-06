/**
 * Copyright (c) 2017,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.libra42.transpath.test
 * File Name:Test.java
 * Date:2017年10月23日 下午5:52:31
 * 
 */
package com.skylynx13.transpath.test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;

public class Test extends JFrame implements DropTargetListener {
    private static final long serialVersionUID = -5037743965292608371L;
    private JEditorPane textPane = new JEditorPane();
    DragTree tree = new DragTree();
    private JScrollPane jsPane = new JScrollPane(tree);

    public Test() {
        super("Drag and Drop With Swing");
        System.out.println("test");
//        DropTarget dt = new DropTarget(textPane, DnDConstants.ACTION_COPY_OR_MOVE, this);
        DropTarget dt = new DropTarget(tree, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter() {

            @Override
            public void drop(DropTargetDropEvent dtde) {
                Transferable tr = dtde.getTransferable();
                System.out.println("JFL: " + dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor));
                try {
                    System.out.println("JFL: " + tr.getTransferData(DataFlavor.javaFileListFlavor).toString());
                } catch (UnsupportedFlavorException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createTreePanel(), createTextPanel());
        splitPane.setDividerLocation(250);
        splitPane.setOneTouchExpandable(true);
        getContentPane().add(splitPane, BorderLayout.CENTER);
    }

    public static void main(String args[]) {
        System.out.println("main");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        Test test = new Test();
        test.setBounds(300, 300, 850, 350);
        test.setVisible(true);
        test.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        test.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private JPanel createTreePanel() {
        JPanel treePanel = new JPanel();
        treePanel.setLayout(new BorderLayout());
        treePanel.add(new JScrollPane(new DragTree()), BorderLayout.CENTER);
        treePanel.setBorder(BorderFactory.createTitledBorder("Drag source for filenames"));
        return treePanel;
    }

    private JPanel createTextPanel() {
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        textPanel.add(new JScrollPane(tree), BorderLayout.CENTER);
//        textPanel.setMinimumSize(new Dimension(375, 0));
        textPanel.setBorder(BorderFactory.createTitledBorder("Drop target for filenames"));
        return textPanel;
    }

    private void readFile(final String filename) {
        try {
            textPane.setPage(new File(filename).toURL());
        } catch (IOException e) {
            EditorKit kit = textPane.getEditorKit();
            Document document = textPane.getDocument();
            try {
                document.remove(0, document.getLength());
                kit.read(new FileReader(filename), document, 0);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void readFile(final File f) {
        try {
            textPane.setPage(f.toURL());
        } catch (IOException e) {
            EditorKit kit = textPane.getEditorKit();
            Document document = textPane.getDocument();
            try {
                document.remove(0, document.getLength());
                kit.read(new FileReader(f), document, 0);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void drop(DropTargetDropEvent e) {
        System.out.println("Drop");
        try {
            DataFlavor stringFlavor = DataFlavor.stringFlavor;
            Transferable tr = e.getTransferable();
            System.out.println("JFL: " + e.isDataFlavorSupported(DataFlavor.javaFileListFlavor));
            System.out.println("JFL: " + tr.getTransferData(DataFlavor.javaFileListFlavor).toString());
            if (e.isDataFlavorSupported(stringFlavor)) {
                String filename = (String) tr.getTransferData(stringFlavor);
                System.out.println(filename + "|");
                if (filename.endsWith(".txt") || filename.endsWith(".java") || filename.endsWith(".jsp")
                        || filename.endsWith(".html") || filename.endsWith(".htm") || filename.endsWith(".log")) {
                    e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                    //readFile("D:\\temp\\" + filename);
                    readFile((File)((java.util.List)tr.getTransferData(DataFlavor.javaFileListFlavor)).get(0));
                    textPane.setCaretPosition(0);
                    e.dropComplete(true);
                } else {
                    e.rejectDrop();
                }
            } else {
                e.rejectDrop();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (UnsupportedFlavorException ufe) {
            ufe.printStackTrace();
        }
    }

    public void dragEnter(DropTargetDragEvent e) {
        System.out.println("testenter1");
    }

    public void dragExit(DropTargetEvent e) {
        System.out.println("testenter2");
    }

    public void dragOver(DropTargetDragEvent e) {
//        System.out.println("testenter3");
    }

    public void dropActionChanged(DropTargetDragEvent e) {
        System.out.println("testenter4");
    }
}