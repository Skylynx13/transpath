package com.skylynx13.transpath.db;

import com.skylynx13.transpath.log.TransLog;
import com.skylynx13.transpath.store.StoreNode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.TypedQuery;
import java.util.List;

public class DbNodeProcessor {
    private static SessionFactory sessionFactory;
    public static void main(String[] args) {
        try {
            // default param for configure() is hibernate.cfg.xml
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable throwable) {
            TransLog.getLogger().error("Failed to create sessionFactory object. ", throwable);
        }
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
        dbNodeProcessor.deleteDbNode(id1);
        dbNodeProcessor.listDbNode();
    }

    private void listDbNode() {
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

    private int addDbNode(DbNode dbNode) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        int id = 0;
        try {
            tx = session.beginTransaction();
            id = (int) session.save(dbNode);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            TransLog.getLogger().error("", e);
        } finally {
            session.close();
        }
        return id;
    }

    private void updateDbNode(int id, String name) {
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

    private void deleteDbNode(int id) {
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
}
