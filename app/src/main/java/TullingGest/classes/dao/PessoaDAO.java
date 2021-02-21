package TullingGest.classes.dao;

import TullingGest.classes.entity.Pessoa;

/**
 * PessoaDAO
 */
public class PessoaDAO extends UserDAO<Pessoa> {
    public PessoaDAO() {
        tipo = new Pessoa();
    }
}