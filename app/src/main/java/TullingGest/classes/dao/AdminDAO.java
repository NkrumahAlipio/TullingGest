package TullingGest.classes.dao;

import TullingGest.classes.entity.Admin;

public class AdminDAO extends UserDAO<Admin> {

    public AdminDAO() {
        tipo = new Admin();
    }

}
