package TullingGest.classes.dao;

import java.util.List;

import TullingGest.classes.entity.Animal;
import TullingGest.classes.entity.Familia;
import TullingGest.classes.entity.Pessoa;

public class FamiliaDAO extends DAO<Familia> {

    public FamiliaDAO() {
        tipo = new Familia();
    }

    @Override
    public boolean save(Familia familia) {
        var session = DAO.session();
        var transation = session.beginTransaction();
        var r = true;
        try {
            familia.getMembros().forEach(session::saveOrUpdate);
            session.saveOrUpdate(familia.getEndereco());
            session.saveOrUpdate(familia);
            transation.commit();
        } catch (Exception e) {
            transation.rollback();
            r = false;
        } finally {
            session.close();
        }
        return r;
    }

    public static Pessoa getPai(Familia familia) {
        var session = DAO.session();

        var op = (Pessoa) session
                .createQuery("FROM Pessoa E WHERE E.tipo='Pai' AND E.familia='%d'".formatted(familia.getId()))
                .uniqueResult();
        session.close();
        return op;
    }

    public static Pessoa getMae(Familia familia) {
        var session = DAO.session();

        var op = (Pessoa) session
                .createQuery("FROM Pessoa E WHERE E.tipo='Mãe' AND E.familia=%d".formatted(familia.getId()))
                .uniqueResult();
        session.close();
        return op;
    }

    public static List<Pessoa> getFilhos(Familia familia) {
        var session = DAO.session();
        var result = (List<Pessoa>) session
                .createQuery("FROM Pessoa E WHERE E.tipo='Filho' AND E.familia=%d".formatted(familia.getId()))
                .getResultList();
        session.close();
        return result;
    }

    public static List<Animal> getAnimais(Familia familia) {
        var session = DAO.session();
        var result = (List<Animal>) session.createQuery("FROM Animal E WHERE E.familia=%d".formatted(familia.getId()))
                .getResultList();
        session.close();
        return result;
    }

}
