package com.skylynx13.transpath.db;

import com.skylynx13.transpath.Transpath;
import com.skylynx13.transpath.log.TransLog;
import com.skylynx13.transpath.store.StoreList;
import com.skylynx13.transpath.store.StoreNode;
import com.skylynx13.transpath.utils.ProgressReport;
import com.skylynx13.transpath.utils.ProgressTracer;
import com.skylynx13.transpath.utils.TransConst;
import com.skylynx13.transpath.utils.TransProp;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.swing.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DbNodeProcessor extends SwingWorker<StringBuilder, ProgressReport> {
    private static SessionFactory sessionFactory;
    private final ProgressTracer progressTracer = new ProgressTracer();

    public DbNodeProcessor() {
        try {
            // default param for configure() is hibernate.cfg.xml
            //Configuration configuration = new Configuration().addResource("conf/hibernate.cfg.xml");
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable throwable) {
            TransLog.getLogger().error("Failed to create sessionFactory object. ", throwable);
        }
    }
    @Override
    protected StringBuilder doInBackground() {
        long startTimeMillis = System.currentTimeMillis();

        this.truncateDbNode();
        TransLog.getLogger().info("DB nodes truncated.");

        StoreList storeList = Transpath.getTranspathFrame().getStoreList();
        resetProgress(storeList.size(), storeList.size());
        this.addDbNodeList(storeList);

        long endTimeMillis =  System.currentTimeMillis();
         return new StringBuilder("DB initialized.")
                .append(" Init Rows: ").append(storeList.size())
                .append(" Init Time: ").append(endTimeMillis - startTimeMillis).append("ms.");
    }

    @Override
    protected void process(List<ProgressReport> progressReportList) {
        ProgressReport lastProgressReport = progressReportList.get(progressReportList.size()-1);
        Transpath.getProgressBar().setValue(lastProgressReport.getProgress());
        Transpath.getStatusLabel().setText(lastProgressReport.getReportLine());
    }

    @Override
    protected void done() {
        try {
            StringBuilder result = get();
            TransLog.getLogger().info(result.toString());
            Transpath.getStatusLabel().setText(result.toString());
        } catch (InterruptedException | ExecutionException e) {
            TransLog.getLogger().error("", e);
        }
    }

    private void resetProgress(long totalSize, long totalCount) {
        TransLog.getLogger().info("DB initializing.");
        progressTracer.reset(totalSize, totalCount, "DB initializing");
        publish(progressTracer.report());
    }

    private void updateProgress(long cSize, long cCount) {
        TransLog.getLogger().info("Processed: {}", cCount);
        progressTracer.updateCurrent(cSize, cCount);
        publish(progressTracer.report());
    }

    public static void main(String[] args) {
        DbNodeProcessor dbNodeProcessor = new DbNodeProcessor();

        DbNode dbNode = new DbNode(new StoreNode("0:123:456:1:2:3:/aaa/bbb/:ccc11"));
        int id1 = dbNodeProcessor.addDbNode(dbNode);
        dbNode =  new DbNode(new StoreNode("1:234:567:4:5:6:/ddd/eee/:fff22"));
        int id2 = dbNodeProcessor.addDbNode(dbNode);

        dbNodeProcessor.listDbNode();
        dbNodeProcessor.updateDbNode(id1, "ccc22");
        dbNodeProcessor.listDbNode();
        dbNodeProcessor.updateDbNode(id2, "fff33");
        dbNodeProcessor.listDbNode();
        dbNodeProcessor.deleteDbNode(id2);
        dbNodeProcessor.listDbNode();
//        dbNodeProcessor.deleteDbNode(id1);
        dbNodeProcessor.truncateDbNode();
        dbNodeProcessor.listDbNode();
    }

    public void listDbNode() {
        System.out.println("listing...");
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            //Will get a Raw use of parameterized class or an Unchecked Assignment warning here
            //List qhRows = session.createQuery("FROM QhRow").list();
            // FROM a class name, not table name
            TypedQuery<DbNode> query = session.createQuery("FROM DbNode", DbNode.class);
            List<DbNode> dbNodes = query.getResultList();

            for (DbNode dbNode : dbNodes) {
                System.out.println(dbNode.toNodeString());
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            TransLog.getLogger().error("", e);
        }
    }

    public int addDbNode(DbNode dbNode) {
        Transaction tx = null;
        int id = 0;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            id = (int) session.save(dbNode);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            TransLog.getLogger().error("", e);
        }
        return id;
    }

    public void addDbNodeList(StoreList storeList) {
        Transaction tx = null;
        int interval = TransProp.getInt(TransConst.DB_COMMIT_INTERVAL);
        int counter = 0;
        DbNode dbNode;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            for (StoreNode storeNode : storeList.getStoreList()) {
                dbNode = new DbNode(storeNode);
                session.save(dbNode);
                counter++;
                /*
                    Commit interval
                    Raw     Millis
                    100     130408
                    1000    32515
                    10000   23072
                    100000  22227
                    1000000 21866
                 */
                if (0 == counter % interval) {
                    tx.commit();
                    updateProgress(counter, counter);
                    tx = session.beginTransaction();
                }
            }
            tx.commit();
            updateProgress(counter, counter);
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            TransLog.getLogger().error("", e);
        }
    }

    public void updateDbNode(int id, String name) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            DbNode dbNode = session.get(DbNode.class, id);
            dbNode.setName(name);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            TransLog.getLogger().error("", e);
        }
    }

    public void deleteDbNode(int id) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            DbNode dbNode = session.get(DbNode.class, id);
            session.delete(dbNode);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            TransLog.getLogger().error("", e);
        }
    }

    public void truncateDbNode() {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            String sql = "TRUNCATE TABLE node";
            Query query = session.createSQLQuery(sql);
            query.executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            TransLog.getLogger().error("", e);
        }
    }
}
