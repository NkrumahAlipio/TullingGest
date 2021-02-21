package TullingGest.classes.dao;

import TullingGest.classes.entity.Dizimo;

public class DizimoDAO extends MoneyDAO<Dizimo> {
    public DizimoDAO() {
        tipo = new Dizimo();
    }
}
