package TullingGest.classes.dao;

import java.util.List;

import TullingGest.classes.entity.Familia;
import TullingGest.classes.entity.PrevisaoMensal;

public class PrevisaoDAO extends AtivoDAO<PrevisaoMensal> {

    public PrevisaoDAO() {
        tipo = new PrevisaoMensal();
    }

    @Override
    public List<PrevisaoMensal> getAll(Familia familia) {
        var session = DAO.session();
        var result = (List<PrevisaoMensal>) session
                .createQuery("FROM PrevisaoMensal E WHERE E.familia=%d AND E.ativo=1".formatted(familia.getId()))
                .getResultList();
        session.close();
        return result;
    }

    @Override
    public List<PrevisaoMensal> getAllWithTrash(Familia familia) {
        var session = session();
        var result = (List<PrevisaoMensal>) session
                .createQuery("FROM PrevisaoMensal E WHERE E.familia=%d".formatted(familia.getId())).getResultList();
        session.close();
        return result;
    }

}
