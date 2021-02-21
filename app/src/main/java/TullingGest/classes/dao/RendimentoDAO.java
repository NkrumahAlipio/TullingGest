package TullingGest.classes.dao;

import TullingGest.classes.entity.Rendimento;

public class RendimentoDAO extends AtivoDAO<Rendimento> {
    public RendimentoDAO() {
        tipo = new Rendimento();
    }

}
