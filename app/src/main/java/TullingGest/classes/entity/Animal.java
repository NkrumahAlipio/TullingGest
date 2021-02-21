/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TullingGest.classes.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import TullingGest.classes.Funcionalidades;
import TullingGest.enums.Raca;

/**
 *
 * @author nkrumah
 */

@Entity
public class Animal extends MembroDaFamilia {

    @Enumerated(EnumType.STRING)
    private Raca raca;

    public Animal() {
    }

    public Animal(String nome, LocalDate dataNascimento, Raca raca) {
        super(nome, dataNascimento);
        this.raca = raca;
    }

    public Raca getRaca() {
        return raca;
    }

    public void setRaca(Raca raca) {
        this.raca = raca;
    }

    public String getIdade() {
        return Funcionalidades.calcularIdade(this.getDataNascimento()) == 1
                ? Funcionalidades.calcularIdade(this.getDataNascimento()) + " ano"
                : Funcionalidades.calcularIdade(this.getDataNascimento()) + " anos";
    }

}
