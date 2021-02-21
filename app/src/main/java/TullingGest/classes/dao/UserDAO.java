package TullingGest.classes.dao;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import com.google.common.hash.Hashing;

import TullingGest.classes.entity.Base;

public abstract class UserDAO<T extends Base> extends DAO<T> {

    public Optional<T> get(String email, String senha) {
        var session = session();
        senha = Hashing.sha256().hashString(senha, StandardCharsets.UTF_8).toString();
        var op = Optional.ofNullable((T) session.createQuery("FROM %s E WHERE E.email='%s' AND E.senha='%s'"
                .formatted(tipo.getClass().getSimpleName(), email, senha)).uniqueResult());
        
        session.close();
        return op;
    }
}
