package TullingGest.classes.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;

import TullingGest.classes.Constantes;

@Entity
public class Dizimo extends ContemPessoa {
    @Column(nullable = false)
    Integer valor;

    @Column(nullable = false)
    String instituicao;

    @Column(nullable = false)
    LocalDate data;

    public Dizimo() {

    }

    public Dizimo(Integer valor, String instituicao, LocalDate data) {
        this.valor = valor;
        this.instituicao = instituicao;
        this.data = data;
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

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

}
