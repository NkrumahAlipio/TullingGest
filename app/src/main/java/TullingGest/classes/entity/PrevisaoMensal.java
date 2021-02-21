package TullingGest.classes.entity;

import java.time.Month;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import TullingGest.classes.Constantes;

@Entity
public class PrevisaoMensal extends ContemFamilia {
    @Column(nullable = false)
    String designacao;

    @Column(nullable = false)
    Boolean ativo;

    @Column(nullable = false)
    String finalidade;

    @Column(nullable = false)
    Integer valor;

    @Enumerated(EnumType.STRING)
    Month mes;

    @OneToOne(mappedBy = "previsao", cascade = CascadeType.ALL)
    Poupanca poupanca;

    @OneToMany(mappedBy = "previsao", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Set<Gasto> gastos;

    @ManyToOne
    Rendimento rendimento;

    public PrevisaoMensal(String finalidade, String designacao, Integer valor, Month mes) {
        this.designacao = designacao;
        this.finalidade = finalidade;
        this.mes = mes;
        this.valor = valor;
        this.ativo = true;
        this.gastos = new HashSet<Gasto>();
    }

    public Set<Gasto> getGastos() {
        return this.gastos;
    }

    public void setGastos(Set<Gasto> gastos) {
        this.gastos = gastos;
    }

    public PrevisaoMensal() {
        this.gastos = new HashSet<Gasto>();
    }

    public String getFinalidade() {
        return finalidade;
    }

    public void setFinalidade(String finalidade) {
        this.finalidade = finalidade;
    }

    public Month getMonth() {
        return mes;
    }

    public void setMonth(Month mes) {
        this.mes = mes;
    }

    public String getDesignacao() {
        return designacao;
    }

    public void setDesignacao(String designacao) {
        this.designacao = designacao;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public String getDinheiro() {
        return Constantes.currencyFormatter.format(this.valor);
    }

    @Override
    public String toString() {
        return this.designacao;
    }

    public Month getMes() {
        return this.mes;
    }

    public void setMes(Month mes) {
        this.mes = mes;
    }

    public Rendimento getRendimento() {
        return this.rendimento;
    }

    public void setRendimento(Rendimento rendimento) {
        this.rendimento = rendimento;
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

}
