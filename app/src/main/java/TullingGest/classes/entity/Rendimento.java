package TullingGest.classes.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import TullingGest.classes.Constantes;

@Entity
public class Rendimento extends ContemPessoa {
    @Column(nullable = false)
    Integer valor;

    @Column(nullable = false)
    Boolean ativo;

    @Column(nullable = false)
    String origem;

    @Column(nullable = false)
    LocalDate data;

    @OneToOne(mappedBy = "rendimento", cascade = CascadeType.ALL)
    Credito credito;

    @OneToMany(mappedBy = "rendimento", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Set<PrevisaoMensal> previsoes;

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public Set<PrevisaoMensal> getPrevisoes() {
        return this.previsoes;
    }

    public void setPrevisoes(Set<PrevisaoMensal> previsoes) {
        this.previsoes = previsoes;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Rendimento(Integer valor, String origem, LocalDate data) {
        this.valor = valor;
        this.origem = origem;
        this.data = data;
        this.previsoes = new HashSet<PrevisaoMensal>();
        this.ativo = true;
    }

    public String getDinheiro() {
        return Constantes.currencyFormatter.format(this.valor);
    }

    public Rendimento() {
        this.previsoes = new HashSet<PrevisaoMensal>();
    }

    public Boolean isAtivo() {
        return this.ativo;
    }

    public Boolean getAtivo() {
        return this.ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return this.origem;
    }

    public Credito getCredito() {
        return this.credito;
    }

    public void setCredito(Credito credito) {
        this.credito = credito;
    }

}
