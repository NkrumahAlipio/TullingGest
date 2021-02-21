package TullingGest.classes.dao;

import java.util.List;

import TullingGest.classes.entity.Base;
import TullingGest.classes.entity.Familia;

public abstract class MoneyDAO<T extends Base> extends DAO<T> {

    public List<T> getAll(Familia familia) {

        var session = session();
        var result = (List<T>) session.createQuery(
                "FROM %s E WHERE E.pessoa.familia=%d".formatted(tipo.getClass().getSimpleName(), familia.getId()))
                .getResultList();
        session.close();
        return result;
    }
}
