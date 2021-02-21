package TullingGest.classes.entity;

import java.time.Month;

import TullingGest.classes.Constantes;

public class BalancoMensal {
    Month mes;
    Integer valor;

    public Month getMes() {
        return this.mes;
    }

    public void setMes(Month mes) {
        this.mes = mes;
    }

    public Integer getValor() {
        return this.valor;
    }

    public String getDinheiro() {
        return Constantes.currencyFormatter.format(this.valor);
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public BalancoMensal() {
    }

    public BalancoMensal(Month mes, Integer valor) {
        this.mes = mes;
        this.valor = valor;
    }

}
