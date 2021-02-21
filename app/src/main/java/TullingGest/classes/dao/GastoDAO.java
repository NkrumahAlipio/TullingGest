package TullingGest.classes.dao;

import TullingGest.classes.entity.Gasto;

public class GastoDAO extends MoneyDAO<Gasto> {
    public GastoDAO() {
        tipo = new Gasto();
    }
}
