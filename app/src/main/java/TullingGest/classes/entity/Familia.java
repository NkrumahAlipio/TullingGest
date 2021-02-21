/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TullingGest.classes.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import TullingGest.classes.dao.FamiliaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author nkrumah
 */
@Entity
public class Familia extends Base {

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String telefone;

    @OneToMany(mappedBy = "familia", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<MembroDaFamilia> membros;

    @OneToMany(mappedBy = "familia", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<PrevisaoMensal> previsoes;

    @OneToMany(mappedBy = "familia", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Poupanca> poupancas;

    @OneToOne(mappedBy = "familia", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Endereco endereco;

    public Familia() {
        this.endereco = null;
    }

    public Familia(String nome, String telefone, Set<MembroDaFamilia> membros, Endereco endereco) {
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.membros = membros;
        this.endereco.setFamilia(this);
        this.membros.forEach(t -> t.setFamilia(this));
        this.previsoes = new HashSet<>();
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Set<MembroDaFamilia> getMembros() {
        return membros;
    }

    public void setMembros(Set<MembroDaFamilia> membros) {
        this.membros = membros;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Set<PrevisaoMensal> getPrevisoes() {
        return previsoes;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPrevisoes(Set<PrevisaoMensal> previsoes) {
        this.previsoes = previsoes;
    }

    public Pessoa getPai() {
        return FamiliaDAO.getPai(this);
    }

    public Pessoa getMae() {
        return FamiliaDAO.getMae(this);
    }

    public List<Pessoa> getFilhos() {
        return FamiliaDAO.getFilhos(this);
    }

    public List<Animal> getAnimais() {
        return FamiliaDAO.getAnimais(this);
    }

    @Override
    public String toString() {
        return this.nome;
    }

    public static ObservableList<Familia> observableFamilias() {
        ObservableList<Familia> ob = FXCollections.observableArrayList();
        ob.addAll((new FamiliaDAO()).getAll());
        return ob;
    }

}
