package TullingGest.classes.entity;

import java.nio.charset.StandardCharsets;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.google.common.hash.Hashing;

@Entity
public class Admin extends Base {

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    public Admin() {

    }

    public Admin(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = Hashing.sha256().hashString(senha, StandardCharsets.UTF_8).toString();
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSenha() {
        return senha;
    }

}
