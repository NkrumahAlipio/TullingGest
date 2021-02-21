/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TullingGest.classes.entity;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.google.common.hash.Hashing;

import TullingGest.classes.Funcionalidades;
import TullingGest.enums.TipoDeMembro;

/**
 *
 * @author nkrumah
 */
@Entity
public class Pessoa extends MembroDaFamilia {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDeMembro tipo;

    @Column(nullable = false, unique = true)
    private String telefone;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @OneToMany(mappedBy = "pessoa", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Gasto> gastos;

    @OneToMany(mappedBy = "pessoa", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Credito> creditos;

    @OneToMany(mappedBy = "pessoa", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Dizimo> dizimos;

    @OneToMany(mappedBy = "pessoa", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Rendimento> rendimentos;

    public Pessoa() {
    }

    public Pessoa(String name, String telefone, String email, LocalDate dataNascimento, TipoDeMembro tipo,
            String senha) {
        super(name, dataNascimento);
        this.tipo = tipo;
        this.telefone = telefone;
        this.email = email;
        this.senha = Hashing.sha256().hashString(senha, StandardCharsets.UTF_8).toString();
        this.creditos = new HashSet<>();
        this.dizimos = new HashSet<>();
        this.rendimentos = new HashSet<>();
    }

    public void setTipo(TipoDeMembro tipo) {
        this.tipo = tipo;
    }

    public TipoDeMembro getTipo() {
        return tipo;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Set<Gasto> getGastos() {
        return this.gastos;
    }

    public void setGastos(Set<Gasto> gastos) {
        this.gastos = gastos;
    }

    public Set<Credito> getCreditos() {
        return this.creditos;
    }

    public void setCreditos(Set<Credito> creditos) {
        this.creditos = creditos;
    }

    public Set<Dizimo> getDizimos() {
        return this.dizimos;
    }

    public void setDizimos(Set<Dizimo> dizimos) {
        this.dizimos = dizimos;
    }

    public Set<Rendimento> getRendimentos() {
        return this.rendimentos;
    }

    public void setRendimentos(Set<Rendimento> rendimentos) {
        this.rendimentos = rendimentos;
    }

    public String getIdade() {
        return Funcionalidades.calcularIdade(this.getDataNascimento()) == 1
                ? Funcionalidades.calcularIdade(this.getDataNascimento()) + " ano"
                : Funcionalidades.calcularIdade(this.getDataNascimento()) + " anos";
    }

}
