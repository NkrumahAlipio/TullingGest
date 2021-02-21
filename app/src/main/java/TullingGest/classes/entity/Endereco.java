/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TullingGest.classes.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 *
 * @author nkrumah
 */
@Entity
public class Endereco extends Base {

    @Column(nullable = false)
    private String bairro;

    @Column(nullable = false)
    private String rua;

    @OneToOne(cascade = CascadeType.REMOVE)
    private Familia familia;

    public Endereco() {
    }

    public Endereco(String bairro, String rua) {
        this.bairro = bairro;
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public Familia getFamilia() {
        return familia;
    }

    public void setFamilia(Familia familia) {
        this.familia = familia;
    }

}
