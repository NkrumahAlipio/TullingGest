package TullingGest.classes.dao;

import TullingGest.classes.entity.Animal;

public class AnimalDAO extends DAO<Animal> {
    
    public AnimalDAO() {
        tipo = new Animal();
    }

}
