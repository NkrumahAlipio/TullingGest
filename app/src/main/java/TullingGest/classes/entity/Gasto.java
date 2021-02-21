package TullingGest.classes.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import TullingGest.classes.Constantes;

@Entity
public class Gasto extends ContemPessoa {

    @Column(nullable = false)
    String designacao;

    @Column(nullable = false)
    Integer valor;

    @Column(nullable = false)
    String local;

    @ManyToOne
    PrevisaoMensal previsao;

    public Gasto() {
    }

    public Gasto(String designacao, Integer valor, String local) {
        this.designacao = designacao;
        this.valor = valor;
        this.local = local;
    }

    public String getDesignacao() {
        return designacao;
    }

    public PrevisaoMensal getPrevisao() {
        return this.previsao;
    }

    public void setPrevisao(PrevisaoMensal previsao) {
        this.previsao = previsao;
    }

    public void setDesignacao(String designacao) {
        this.designacao = designacao;
    }

    public Integer getValor() {
        return valor;
    }

    public String getDinheiro() {
        return Constantes.currencyFormatter.format(this.valor);
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    @Override
    public String toString() {
        return this.designacao;
    }

}
