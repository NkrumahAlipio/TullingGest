package TullingGest.classes.dao;

import java.util.List;

import TullingGest.classes.entity.Familia;
import TullingGest.classes.entity.Poupanca;

public class PoupancaDAO extends DAO<Poupanca> {

    public List<Poupanca> getAll(Familia familia) {
        var session = DAO.session();
        var result = (List<Poupanca>) session
                .createQuery("FROM Poupanca E WHERE E.familia=%d".formatted(familia.getId())).getResultList();
        session.close();
        return result;
    }
}
