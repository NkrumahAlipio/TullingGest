package TullingGest.classes.entity;

import java.time.Month;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import TullingGest.classes.Constantes;

@Entity
public class Poupanca extends Base {
    private Month mes;
    private Integer valor;

    @ManyToOne
    private Familia familia;

    @OneToOne
    private PrevisaoMensal previsao;

    public Poupanca() {
    }

    public Poupanca(Month mes, Integer valor, PrevisaoMensal previsao) {
        this.mes = mes;
        this.valor = valor;
        this.previsao = previsao;
    }

    public Month getMes() {
        return this.mes;
    }

    public void setMes(Month mes) {
        this.mes = mes;
    }

    public String getDinheiro() {
        return Constantes.currencyFormatter.format(this.valor);
    }

    public Integer getValor() {
        return this.valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public PrevisaoMensal getPrevisao() {
        return this.previsao;
    }

    public void setPrevisao(PrevisaoMensal previsao) {
        this.previsao = previsao;
    }

}
