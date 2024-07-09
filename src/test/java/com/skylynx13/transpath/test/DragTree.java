package com.skylynx13.transpath.test;

import com.skylynx13.transpath.log.TransLog;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

class DragTree extends JTree implements DragGestureListener, DragSourceListener, DropTargetListener {
    private static final long serialVersionUID = -7123350256666099899L;
    private BufferedImage ghostImage;
    private Rectangle2D ghostRect = new Rectangle2D.Float();
    private Point ptOffset = new Point();
    private Point lastPoint = new Point();
    private TreePath lastPath;
    private Timer hoverTimer;
    private FileNode sourceNode;

    DragTree() {
        System.out.println("DragTree");
        DragSource dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(this,
                // component where
                // drag originates
                DnDConstants.ACTION_COPY_OR_MOVE,
                // actions
                this);
        // drag gesture recognizer
        setModel(createTreeModel());
        addTreeExpansionListener(new TreeExpansionListener() {
            public void treeCollapsed(TreeExpansionEvent e) {
            }

            public void treeExpanded(TreeExpansionEvent e) {
                TreePath path = e.getPath();
                if (path != null) {
                    FileNode node = (FileNode) path.getLastPathComponent();
                    if (!node.isExplored()) {
                        DefaultTreeModel model = (DefaultTreeModel) getModel();
                        node.explore();
                        model.nodeStructureChanged(node);
                    }
                }
            }
        });
        this.setCellRenderer(new DefaultTreeCellRenderer() {
            private static final long serialVersionUID = 7616415055309322562L;

            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                    boolean leaf, int row, boolean hasFocus) {
                TreePath tp = tree.getPathForRow(row);
                if (tp != null) {
                    FileNode node = (FileNode) tp.getLastPathComponent();
                    File f = node.getFile();
                    try {
                        Icon icon = FileSystemView.getFileSystemView().getSystemIcon(f);
                        this.setIcon(icon);
                        this.setLeafIcon(icon);
                        this.setOpenIcon(icon);
                        this.setClosedIcon(icon);
                        this.setDisabledIcon(icon);
                    } catch (Exception e) {
                        TransLog.getLogger().error("", e);
                    }
                }
                return super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            }
        });
        super.setScrollsOnExpand(true);
        new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
        // Set up a hover timer, so that a node will be automatically expanded
        // or collapsed
        // if the user lingers on it for more than a short time
        hoverTimer = new Timer(1000, e -> {
            if (lastPath == null) {
                return;
            }
            if (getRowForPath(lastPath) == 0)
                return;
            // Do nothing if we are hovering over the root node
            if (isExpanded(lastPath))
                collapsePath(lastPath);
            else
                expandPath(lastPath);
        });
        hoverTimer.setRepeats(false);
        // Set timer to one-shot mode
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                int modifiers = e.getModifiersEx();
                if (code == 'v' || code == 'V') {
                    System.out.println("find v");
                    System.out.println("modifiers:" + modifiers + "/t" + ((modifiers & KeyEvent.CTRL_MASK) != 0));
                }
                if ((modifiers & KeyEvent.CTRL_MASK) != 0 && (code == 'v' || code == 'V')) {
                    Transferable tr = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
                    TreePath path = getSelectionPath();
                    if (path == null) {
                        return;
                    }
                    FileNode node = (FileNode) path.getLastPathComponent();
                    if (node.isDirectory()) {
                        System.out.println("file cp");
                        try {
                            List<?> list = (List<?>) (tr.getTransferData(DataFlavor.javaFileListFlavor));
                            Iterator<?> iterator = list.iterator();
                            File parent = node.getFile();
                            while (iterator.hasNext()) {
                                File f = (File) iterator.next();
                                cp(f, new File(parent, f.getName()));
                            }
                            node.reexplore();
                        } catch (Exception ioe) {
                            TransLog.getLogger().error("", ioe);
                        }
                        updateUI();
                    }
                }
            }
        });
        this.setDragEnabled(true);
    }

    public void dragGestureRecognized(DragGestureEvent e) {
        // drag anything …
        TreePath path = getLeadSelectionPath();
        if (path == null)
            return;
        sourceNode = (FileNode) path.getLastPathComponent();
        // Work out the offset of the drag point from the TreePath bounding
        // rectangle origin
        Rectangle raPath = getPathBounds(path);
        Point ptDragOrigin = e.getDragOrigin();
        ptOffset.setLocation(ptDragOrigin.x - Objects.requireNonNull(raPath).x, ptDragOrigin.y - raPath.y);
        // Get the cell renderer (which is a JLabel) for the path being dragged
        int row = this.getRowForLocation(ptDragOrigin.x, ptDragOrigin.y);
        JLabel lbl = (JLabel) getCellRenderer().getTreeCellRendererComponent(this,
                // tree
                path.getLastPathComponent(),
                // value
                false,
                // isSelected (dont want a colored background)
                isExpanded(path),
                // isExpanded
                getModel().isLeaf(path.getLastPathComponent()),
                // isLeaf
                row,
                // row (not important for rendering)
                false
        // hasFocus (dont want a focus rectangle)
        );
        lbl.setSize((int) raPath.getWidth(), (int) raPath.getHeight());
        // <-
        // The
        // layout
        // manager
        // would
        // normally
        // do
        // this
        // Get a buffered image of the selection for dragging a ghost image
        this.ghostImage = new BufferedImage((int) raPath.getWidth(), (int) raPath.getHeight(),
                BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D g2 = ghostImage.createGraphics();
        // Ask the cell renderer to paint itself into the
        // BufferedImageg2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC,
        // 0.5f));
        // Make the image ghostlikelbl.paint(g2);g2.dispose();
        // this.getGraphics().drawImage(ghostImage, e.getDragOrigin().x,
        // e.getDragOrigin().y, this);e.startDrag(null,
        // cursorghostImage, new Point(5, 5),new StringSelection(getFilename()),
        // transferablethis);
        // drag source listener
    }

    public void dragDropEnd(DragSourceDropEvent e) {
        ghostImage = null;
        sourceNode = null;
    }

    public void dragEnter(DragSourceDragEvent e) {
    }

    public void dragExit(DragSourceEvent e) {
        if (!DragSource.isDragImageSupported()) {
            repaint(ghostRect.getBounds());
        }
    }

    public void dragOver(DragSourceDragEvent e) {
    }

    public void dropActionChanged(DragSourceDragEvent e) {
    }

    public String getFilename() {
        TreePath path = getLeadSelectionPath();
        FileNode node = (FileNode) path.getLastPathComponent();
        return ((File) node.getUserObject()).getAbsolutePath();
    }

    private DefaultTreeModel createTreeModel() {
        File root = FileSystemView.getFileSystemView().getRoots()[0];
        FileNode rootNode = new FileNode(root);
        rootNode.explore();
        return new DefaultTreeModel(rootNode);
    }

    public void dragEnter(DropTargetDragEvent dtde) {
    }

    public void dragOver(DropTargetDragEvent dtde) {
        Point pt = dtde.getLocation();
        if (pt.equals(lastPoint)) {
            return;
        }
        if (ghostImage != null) {
            Graphics2D g2 = (Graphics2D) getGraphics();
            // If a drag image is not supported by the platform, then draw my
            // own drag image
            if (!DragSource.isDragImageSupported()) {
                paintImmediately(ghostRect.getBounds());
            }
            // Rub out the last
            // ghost image and cue
            // line
            // And remember where we are about to draw the new ghost image
            ghostRect.setRect(pt.x - ptOffset.x, pt.y - ptOffset.y, ghostImage.getWidth(), ghostImage.getHeight());
            g2.drawImage((ghostImage), AffineTransform.getTranslateInstance(ghostRect.getX(), ghostRect.getY()), null);
        }
        TreePath path = getClosestPathForLocation(pt.x, pt.y);
        if (!(path == lastPath)) {
            lastPath = path;
            hoverTimer.restart();
        }
    }

    public void dropActionChanged(DropTargetDragEvent dtde) {
        System.out.println("DropChanged");
    }

    public void drop(DropTargetDropEvent e) {
        System.out.println("DropT");
        System.out.println("JFL: " + e.isDataFlavorSupported(DataFlavor.javaFileListFlavor));
        try {
            DataFlavor stringFlavor = DataFlavor.stringFlavor;
            Transferable tr = e.getTransferable();
            TreePath path = this.getPathForLocation(e.getLocation().x, e.getLocation().y);
            if (path == null) {
                e.rejectDrop();
                return;
            }
            FileNode node = (FileNode) path.getLastPathComponent();
            if (e.isDataFlavorSupported(DataFlavor.javaFileListFlavor) && node.isDirectory()) {
                e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                System.out.println("file cp");
                List<?> list = (List<?>) (e.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
                Iterator<?> iterator = list.iterator();
                File parent = node.getFile();
                while (iterator.hasNext()) {
                    File f = (File) iterator.next();
                    cp(f, new File(parent, f.getName()));
                }
                node.reexplore();
                e.dropComplete(true);
                this.updateUI();
            } else if (e.isDataFlavorSupported(stringFlavor) && node.isDirectory()) {
                String filename = (String) tr.getTransferData(stringFlavor);
                System.out.println(filename + "|");
                if (filename.endsWith(".txt") || filename.endsWith(".java") || filename.endsWith(".jsp")
                        || filename.endsWith(".html") || filename.endsWith(".htm")) {
                    e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                    File f = new File("D:\\temp\\" + filename);
                    if (f.exists()) {
                        f.renameTo(new File(node.getFile(), f.getName()));
                        node.reexplore();
                        ((FileNode) sourceNode.getParent()).remove(sourceNode);
                        e.dropComplete(true);
                        this.updateUI();
                    } else {
                        e.rejectDrop();
                    }
                } else {
                    e.rejectDrop();
                }
            } else {
                e.rejectDrop();
            }
        } catch (IOException | UnsupportedFlavorException ioe) {
            TransLog.getLogger().error("", ioe);
        } finally {
            ghostImage = null;
            this.repaint();
        }
    }

    private void cp(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            if (!dest.exists()) {
                boolean ret = dest.mkdir();
                if (!ret)
                    return;
            }
            File[] fs = src.listFiles();
            for (File f : Objects.requireNonNull(fs)) {
                cp(f, new File(dest, f.getName()));
            }
            return;
        }
        byte[] buf = new byte[1024];
        int len;
        try (FileInputStream in = new FileInputStream(src); FileOutputStream out = new FileOutputStream(dest)) {
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        }
    }

    public void dragExit(DropTargetEvent dte) {
    }
}

class FileNode extends DefaultMutableTreeNode {
    private static final long serialVersionUID = -6713369593920467035L;
    private boolean explored = false;

    FileNode(File file) {
        setUserObject(file);
    }

    public boolean getAllowsChildren() {
        return isDirectory();
    }

    public boolean isLeaf() {
        return !isDirectory();
    }

    File getFile() {
        return (File) getUserObject();
    }

    boolean isExplored() {
        return explored;
    }

    boolean isDirectory() {
        File file = getFile();
        return file.isDirectory();
    }

    public String toString() {
        File file = (File) getUserObject();
        String filename = file.toString();
        int index = filename.lastIndexOf(File.separator);
        return (index != -1 && index != filename.length() - 1) ? filename.substring(index + 1) : filename;
    }

    void explore() {
        if (!isDirectory())
            return;
        if (!isExplored()) {
            File file = getFile();
            File[] children = file.listFiles();
            for (File child : Objects.requireNonNull(children)) {
                if (child.isDirectory())
                    add(new FileNode(child));
            }
            for (File child : children) {
                if (!child.isDirectory())
                    add(new FileNode(child));
            }
            explored = true;
        }
    }

    void reexplore() {
        this.removeAllChildren();
        explored = false;
        explore();
    }
}
