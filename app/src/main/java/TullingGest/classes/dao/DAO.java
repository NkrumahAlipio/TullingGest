package TullingGest.classes.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

import TullingGest.classes.entity.Base;

public abstract class DAO<T extends Base> {
    
    T tipo;

    static Configuration con = new Configuration()
            .configure(ClassLoader.getSystemClassLoader().getResource("META-INF/hibernate.cfg.xml"));

    public static Session session() {
        return con.buildSessionFactory().openSession();
    }

    public boolean save(T arg) {
        var session = session();
        var transation = session.beginTransaction();
        var r = true;
        try {
            session.saveOrUpdate(arg);
            transation.commit();
        } catch (Exception e) {
            transation.rollback();
            r = false;
        } finally {
            session.close();
        }
        return r;
    }

    public boolean delete(T arg) {
        var session = session();
        var transation = session.beginTransaction();
        var r = true;
        try {
            session.delete(arg);
            transation.commit();
        } catch (Exception e) {
            transation.rollback();
            r = false;
        } finally {
            session.close();
        }

        return r;
    }

    public T getById(Integer id) {
        var session = session();
        T elem = (T) session.find(tipo.getClass(), id);
        session.close();
        return elem;
    }

    public List<T> getAll() {
        var session = session();
        var result = (List<T>) session.createQuery("FROM " + tipo.getClass().getSimpleName()).getResultList();
        session.close();
        return result;
    }

}
