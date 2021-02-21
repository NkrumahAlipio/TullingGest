package TullingGest.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import TullingGest.App;
import TullingGest.classes.Funcionalidades;
import TullingGest.classes.dao.CreditoDAO;
import TullingGest.classes.dao.RendimentoDAO;
import TullingGest.classes.entity.Credito;
import TullingGest.classes.entity.Rendimento;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DividaController implements Initializable {

    @FXML
    private Label lblValorDivida;

    @FXML
    private ComboBox<Credito> cbCredito;

    @FXML
    private ComboBox<Rendimento> cbRendimento;

    @FXML
    private TextField ipValor;

    @FXML
    private Label lblNotificacoes;

    @FXML
    private Label lblApresentacao;

    private static Credito credito;

    @FXML
    void pagarCredito(ActionEvent event) {
        if (validar()) {
            var valor = Integer.parseInt(ipValor.getText());
            var rendimento = cbRendimento.getValue();
            credito.setValorDizida(credito.getValorDizida() - valor);
            rendimento.setValor(rendimento.getValor() - valor);

            if (rendimento.getValor() == 0) {
                rendimento.setAtivo(false);
            }

            (new RendimentoDAO()).save(rendimento);

            if (credito.getValorDizida() == 0) {
                (new CreditoDAO()).delete(credito);

            } else {
                (new CreditoDAO()).save(credito);
            }

            App.setRoot("TelaPrincipal");
            TelaPrincipalController.getStage().close();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblValorDivida.setText(credito.getDinheiroDivida());
        ipValor.setTextFormatter(Funcionalidades.filtroNumerico());
        cbCredito.getItems().add(credito);
        cbCredito.getSelectionModel().select(0);
        cbRendimento.getItems().addAll((new RendimentoDAO()).getAll());

    }

    public static Credito getCredito() {
        return credito;
    }

    public static void setCredito(Credito credito) {
        DividaController.credito = credito;
    }

    boolean validar() {
        var valor = ipValor.getText();
        var rendimento = cbRendimento.getValue();
        var r = false;

        if (rendimento == null) {
            lblNotificacoes.setText("Escolha um rendimento");
        } else if (rendimento.getValor() < Integer.parseInt(valor)) {
            lblNotificacoes.setText("O Rendimento escolhido não possui dinheiro suficiente");
        } else if (credito.getValorDizida() < Integer.parseInt(valor)) {
            lblNotificacoes.setText("O Valor escolhido é maior que o valor em divida");
        } else {
            r = true;
        }

        return r;
    }

}
