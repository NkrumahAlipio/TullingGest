package TullingGest.classes.dao;

import java.util.List;

import TullingGest.classes.entity.Base;
import TullingGest.classes.entity.Familia;

public class AtivoDAO<T extends Base> extends MoneyDAO<T> {
    @Override
    public List<T> getAll() {
        var session = session();
        var result = (List<T>) session
                .createQuery("FROM %s E WHERE E.ativo=1".formatted(tipo.getClass().getSimpleName())).getResultList();
        session.close();
        return result;
    }

    @Override
    public List<T> getAll(Familia familia) {

        var session = session();
        var result = (List<T>) session.createQuery("FROM %s E WHERE E.pessoa.familia=%d AND E.ativo=1"
                .formatted(tipo.getClass().getSimpleName(), familia.getId())).getResultList();
        session.close();
        return result;
    }

    public List<T> getAllWithTrash(Familia familia) {
        var session = session();
        var result = (List<T>) session.createQuery("FROM %s E WHERE E.pessoa.familia=%d"
                .formatted(tipo.getClass().getSimpleName(), familia.getId())).getResultList();
        session.close();
        return result;
    }
}
