package TullingGest.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import TullingGest.App;
import TullingGest.classes.Funcionalidades;
import TullingGest.classes.Validation;
import TullingGest.classes.dao.GastoDAO;
import TullingGest.classes.dao.PrevisaoDAO;
import TullingGest.classes.entity.Gasto;
import TullingGest.classes.entity.PrevisaoMensal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class GastoController implements Initializable {

    @FXML
    private AnchorPane painelLogin;

    @FXML
    private TextField tfDesignacao;

    @FXML
    private TextField tfValor;

    @FXML
    private TextField tfLocal;

    @FXML
    private Label lblNotificacoes;

    @FXML
    private Menu mRendimentos;

    @FXML
    private Menu mCredito;

    @FXML
    private ComboBox<PrevisaoMensal> cbPrevisao;

    @FXML
    private Menu mGasto;

    @FXML
    private Label lblApresentacao;

    @FXML
    private Button btnRegistrar;

    private static String tarefa = "";
    private static Gasto gasto;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tfValor.setTextFormatter(Funcionalidades.filtroNumerico());
        cbPrevisao.getItems().addAll((new PrevisaoDAO()).getAll(TelaPrincipalController.getUser().getFamilia()));
        if (tarefa == "actualizar") {
            lblApresentacao.setText("Actuaze O Gasto");
            btnRegistrar.setText("Registrar Gasto");
            tfValor.setText(gasto.getValor().toString());
            tfDesignacao.setText(gasto.getDesignacao());
            tfLocal.setText(gasto.getDesignacao());
            cbPrevisao.setValue(gasto.getPrevisao());
            cbPrevisao.setDisable(true);
        }
    }

    @FXML
    void registrarGasto(ActionEvent event) {
        if (validar()) {
            var valor = Integer.parseInt(tfValor.getText());
            var designacao = tfDesignacao.getText();
            var local = tfLocal.getText();
            var previsao = cbPrevisao.getValue();

            if (tarefa == "actualizar") {
                gasto.setLocal(local);
                gasto.setDesignacao(designacao);

                previsao.setValor(previsao.getValor() + gasto.getValor());
                previsao.setValor(previsao.getValor() - valor);

                if (previsao.getValor() < 0) {
                    lblNotificacoes.setText("Não é possível colocar esse valor no gasto");
                    return;
                }
                
                gasto.setValor(valor);

                (new GastoDAO()).save(gasto);
                (new PrevisaoDAO()).save(previsao);

            } else {
                var user = TelaPrincipalController.getUser();
                var gasto = new Gasto(designacao, valor, local);
                gasto.setPessoa(user);
                user.getGastos().add(gasto);
                gasto.setPrevisao(previsao);
                previsao.setValor(previsao.getValor() - valor);
                if (previsao.getValor() == 0)
                    previsao.setAtivo(false);
                (new PrevisaoDAO()).save(previsao);
                (new GastoDAO()).save(gasto);

            }
            App.setRoot("TelaPrincipal");
            TelaPrincipalController.getStage().close();
        }
    }

    private boolean validar() {
        var valor = tfValor.getText();
        var designacao = tfDesignacao.getText();
        var local = tfLocal.getText();
        var previsao = cbPrevisao.getValue();
        var r = false;

        if (Validation.isNome(designacao).isPresent()) {
            lblNotificacoes.setText("A Descrição Digitada é invalida");
        } else if (!Validation.isNumeric(valor)) {
            lblNotificacoes.setText("O Valor Digitado é invalido");
        } else if (Validation.isNome(local).isPresent()) {
            lblNotificacoes.setText("O Local Digitado é invalido");
        } else if (previsao == null) {
            lblNotificacoes.setText("Escolha Uma Previsão");
        } else if (previsao.getValor() < Integer.parseInt(valor)) {
            lblNotificacoes.setText("A Previsão Escolhida Não Tem Dinheiro Suficiente Para Realizar Esse Gasto");
        } else {
            r = true;
        }

        return r;
    }

    public static void setTarefa(String tarefa) {
        GastoController.tarefa = tarefa;
    }

    public static void setGasto(Gasto gasto) {
        GastoController.gasto = gasto;
    }

}