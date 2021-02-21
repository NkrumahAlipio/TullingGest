package TullingGest.classes;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import TullingGest.classes.dao.DAO;
import TullingGest.classes.entity.Pessoa;
import TullingGest.controllers.FamiliaController;

public abstract class Validation {
    public static Optional<String> isNome(String nome) {
        return nome.length() >= 3 ? Optional.empty() : Optional.of("O Nome Digitado É Invalido");
    }

    public static Optional<String> isEmail(String email) {

        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        var match = matcher.matches();

        return match ? Optional.empty() : Optional.of("O Email Digitado é Invalido");
    }

    public static Optional<String> isSenha(String senha) {
        return senha.length() >= 8 ? Optional.empty() : Optional.of("A Senha Digitada É Invalida");

    }

    public static Optional<String> isTelefone(String telefone) {
        return telefone.startsWith("9") && telefone.length() == 9 && isNumeric(telefone) ? Optional.empty()
                : Optional.of("O Número de Telefone Digitado É Invalido");
    }

    public static boolean isNumeric(String texto) {
        return texto.matches("-?\\d+(\\.\\d+)?");
    }

    public static Optional<String> isUniqueEmail(String email) {
        String hql = "FROM Pessoa E WHERE E.email = '%s'".formatted(email);
        var mensagem = "O Email Inserido Já Foi Utilizado";
        var pessoa = (Pessoa) DAO.session().createQuery(hql).uniqueResult();
        Optional<String> r = Optional.empty();
        if (pessoa != null
                || FamiliaController.getPessoas().stream().map(t -> t.getEmail()).anyMatch(t -> t.equals(email))) {
            r = Optional.of(mensagem);
        }

        return r;
    }

    public static Optional<String> isUniqueTelefone(String telefone) {
        String hql = "FROM Pessoa E WHERE E.telefone = '%s'".formatted(telefone);
        var mensagem = "O Número De Telefone Inserido Já Foi Utilizado";
        var pessoa = (Pessoa) DAO.session().createQuery(hql).uniqueResult();
        Optional<String> r = Optional.empty();
        if (pessoa != null || FamiliaController.getPessoas().stream().map(t -> t.getTelefone())
                .anyMatch(t -> t.equals(telefone))) {
            r = Optional.of(mensagem);
        }

        return r;
    }

}