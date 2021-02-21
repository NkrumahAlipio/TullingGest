package TullingGest.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import TullingGest.App;
import TullingGest.classes.Funcionalidades;
import TullingGest.classes.Validation;
import TullingGest.classes.dao.CreditoDAO;
import TullingGest.classes.dao.RendimentoDAO;
import TullingGest.classes.entity.Rendimento;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RendimentoController implements Initializable {
    @FXML
    private TextField ipValor;

    @FXML
    private TextField ipOrigem;

    @FXML
    private DatePicker dpData;

    @FXML
    private Label lblNotificacoes;

    @FXML
    private Label lblApresentacao;

    @FXML
    private Button btnRegistrar;

    private static Rendimento rendimento;
    private static String tarefa = "";

    @FXML
    void adicionarRendimento(ActionEvent event) {
        if (validar()) {
            var valor = Integer.parseInt(ipValor.getText());
            var origem = ipOrigem.getText();
            var data = dpData.getValue();

            if (tarefa == "actualizar") {
                btnRegistrar.setText("Actualizar Rendimento");
                lblApresentacao.setText("Actualize O Rendimento");
                if (rendimento.getCredito() != null) {
                    var credito = rendimento.getCredito();

                    if (credito.getValorDizida() > valor) {

                        var diferenca = credito.getValorDizida() - valor;
                        credito.setValorDizida(credito.getValorDizida() - diferenca);

                    } else if (credito.getValorDizida() < valor) {
                        
                        var diferenca = valor - credito.getValorDizida();
                        credito.setValorDizida(credito.getValorDizida() + diferenca);
                        
                    }
                    credito.setValorConcedido(valor);
                    (new CreditoDAO()).save(credito);
                }
                rendimento.setValor(valor);
                rendimento.setData(data);
                rendimento.setOrigem(origem);

                (new RendimentoDAO()).save(rendimento);

            } else {
                var user = TelaPrincipalController.getUser();
                var rendimento = new Rendimento(valor, origem, data);
                rendimento.setPessoa(user);
                user.getRendimentos().add(rendimento);
                (new RendimentoDAO()).save(rendimento);
            }

            App.setRoot("TelaPrincipal");
            TelaPrincipalController.getStage().close();

        }
    }

    private boolean validar() {

        var valor = ipValor.getText();
        var origem = ipOrigem.getText();
        var data = dpData.getValue();
        var r = false;
        if (!Validation.isNumeric(valor))
            lblNotificacoes.setText("O Valor Digitado é Invalido");
        else if (Validation.isNome(origem).isPresent())
            lblNotificacoes.setText("A Origem Digitada É Invalida");
        else if (data == null)
            lblNotificacoes.setText("Escolha Uma Data");
        else if (data.isBefore(LocalDate.now()))
            lblNotificacoes.setText("A Data Do Rendimeto Digitada Já Passou");
        else
            r = true;
        return r;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ipValor.setTextFormatter(Funcionalidades.filtroNumerico());
        if (tarefa == "actualizar") {
            btnRegistrar.setText("Actualizar Rendimento");
            lblApresentacao.setText("Actualize O Rendimento");
            ipValor.setText(rendimento.getValor().toString());
            ipOrigem.setText(rendimento.getOrigem());
            dpData.setValue(rendimento.getData());
        }
    }

    public static void setTarefa(String tarefa) {
        RendimentoController.tarefa = tarefa;
    }

    public static void setRendimento(Rendimento rendimento) {
        RendimentoController.rendimento = rendimento;
    }
}
