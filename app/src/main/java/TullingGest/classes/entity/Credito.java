package TullingGest.classes.entity;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import TullingGest.classes.Constantes;

@Entity
public class Credito extends ContemPessoa {
    @Column(nullable = false)
    Integer valorDizida;

    @Column(nullable = false)
    Integer valorConcedido;

    @Column(nullable = false)
    LocalDate dataSituacao;

    @Column(nullable = false)
    LocalDate dataVencimento;

    @OneToOne
    Rendimento rendimento;

    public String getDinheiroDivida() {
        return Constantes.currencyFormatter.format(this.valorDizida);
    }

    public String getDinheiroConcedido() {
        return Constantes.currencyFormatter.format(this.valorConcedido);
    }

    public LocalDate getDataSituacao() {
        return dataSituacao;
    }

    public void setDataSituacao(LocalDate dataSituacao) {
        this.dataSituacao = dataSituacao;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Credito() {
    }

    public Credito(Integer valor, LocalDate dataVencimento) {
        this.valorDizida = valor;
        this.valorConcedido = valor;
        this.dataSituacao = LocalDate.now();
        this.dataVencimento = dataVencimento;
    }

    public Integer getValorDizida() {
        return this.valorDizida;
    }

    public void setValorDizida(Integer valorDizida) {
        this.valorDizida = valorDizida;
    }

    public Integer getValorConcedido() {
        return this.valorConcedido;
    }

    public void setValorConcedido(Integer valorConcedido) {
        this.valorConcedido = valorConcedido;
    }

    public Rendimento getRendimento() {
        return this.rendimento;
    }

    public void setRendimento(Rendimento rendimento) {
        this.rendimento = rendimento;
    }

    @Override
    public String toString() {
        return "Credito Bancário de " + dataSituacao.toString();
    }

}
