package TullingGest.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import TullingGest.App;
import TullingGest.classes.Funcionalidades;
import TullingGest.classes.Validation;
import TullingGest.classes.dao.CreditoDAO;
import TullingGest.classes.dao.RendimentoDAO;
import TullingGest.classes.entity.Credito;
import TullingGest.classes.entity.Rendimento;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class CreditoController implements Initializable {
    @FXML
    private AnchorPane painelLogin;

    @FXML
    private TextField ipValor;

    @FXML
    private DatePicker dpVencimento;

    @FXML
    private Label lblNotificacoes;

    @FXML
    private Button btnRegistrar;

    @FXML
    private Label lblApresentacao;

    private static String tarefa = "";
    private static Credito credito;

    @FXML
    void registrarCredito(ActionEvent event) {
        if (validar()) {
            var valor = Integer.parseInt(ipValor.getText());
            var vencimento = dpVencimento.getValue();

            if (tarefa.equals("actualizar")) {

                var rendimento = credito.getRendimento();

                credito.setDataSituacao(vencimento);

                credito.setValorDizida(credito.getValorDizida());

                if (credito.getValorConcedido() < valor) {
                    var diferenca = valor - credito.getValorConcedido();
                    rendimento.setValor(rendimento.getValor() + diferenca);
                    credito.setValorDizida(credito.getValorDizida() + diferenca);
                } else if (credito.getValorConcedido() > valor) {
                    var diferenca = credito.getValorConcedido() - valor;
                    rendimento.setValor(rendimento.getValor() - diferenca);
                    credito.setValorDizida(credito.getValorDizida() - diferenca);
                }

                if (credito.getValorDizida() <= 0 || rendimento.getValor() <= 0) {
                    lblNotificacoes.setText("Não é possível atribuir esse valor ao credito");
                    return;
                }

                credito.setValorConcedido(valor);

                (new RendimentoDAO()).save(rendimento);
                (new CreditoDAO()).save(credito);

            } else {
                var user = TelaPrincipalController.getUser();
                var credito = new Credito(valor, vencimento);
                var rendimento = new Rendimento(valor, "Credito Bancario", credito.getDataSituacao());

                credito.setPessoa(user);
                user.getCreditos().add(credito);

                user.getRendimentos().add(rendimento);
                rendimento.setPessoa(user);

                rendimento.setCredito(credito);
                credito.setRendimento(rendimento);

                (new RendimentoDAO()).save(rendimento);
                (new CreditoDAO()).save(credito);

            }

            App.setRoot("TelaPrincipal");
            TelaPrincipalController.getStage().close();
        }
    }

    boolean validar() {
        var valor = ipValor.getText();
        var vencimento = dpVencimento.getValue();
        var r = false;
        if (!Validation.isNumeric(valor))
            lblNotificacoes.setText("O Valor Digitado é Invalido");
        else if (vencimento == null)
            lblNotificacoes.setText("Escolha Uma Data De Vencimento");
        else if (!vencimento.isAfter(LocalDate.now()))
            lblNotificacoes.setText("A Data De Vencimento Deve Ser No Futuro");
        else
            r = true;
        return r;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ipValor.setTextFormatter(Funcionalidades.filtroNumerico());

        if (tarefa == "actualizar") {
            btnRegistrar.setText("Actualizar Credito");
            lblApresentacao.setText("Actualize O Credito");
            ipValor.setText(credito.getValorConcedido().toString());
            dpVencimento.setValue(credito.getDataVencimento());

        }

    }

    public static void setCredito(Credito credito) {
        CreditoController.credito = credito;
    }

    public static void setTarefa(String tarefa) {
        CreditoController.tarefa = tarefa;
    }

}
