package TullingGest.classes.dao;

import TullingGest.classes.entity.Credito;

/**
 * CreditoDAO
 */
public class CreditoDAO extends MoneyDAO<Credito> {
    public CreditoDAO() {
        tipo = new Credito();
    }
}