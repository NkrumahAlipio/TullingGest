package TullingGest.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.ResourceBundle;

import TullingGest.App;
import TullingGest.classes.Funcionalidades;
import TullingGest.classes.Validation;
import TullingGest.classes.dao.PrevisaoDAO;
import TullingGest.classes.dao.RendimentoDAO;
import TullingGest.classes.entity.PrevisaoMensal;
import TullingGest.classes.entity.Rendimento;
import TullingGest.enums.TipoDeMembro;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PrevisaoMensalController implements Initializable {
    @FXML
    private TextField ipDesignacao;

    @FXML
    private TextField ipValor;

    @FXML
    private TextField ipFinalidade;

    @FXML
    private ComboBox<Month> cbMes;

    @FXML
    private Label lblNotificacoes;

    @FXML
    private Label lblApresentacao;

    @FXML
    private Button btnRegistrar;

    @FXML
    private ComboBox<Rendimento> cbRendimento;
    private static String tarefa = "";
    private static PrevisaoMensal previsao;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ipValor.setTextFormatter(Funcionalidades.filtroNumerico());

        cbMes.getItems().addAll(Arrays.asList(Month.values()).stream()
                .filter((Month month) -> month.getValue() >= LocalDate.now().getMonthValue()).toArray(Month[]::new));
        if (TelaPrincipalController.getUser().getTipo() == TipoDeMembro.Filho) {
            var rendimentos = (new RendimentoDAO()).getAll(TelaPrincipalController.getUser().getFamilia()).stream()
                    .filter(rendimento -> rendimento.getPessoa().equals(TelaPrincipalController.getUser()))
                    .toArray(Rendimento[]::new);
            cbRendimento.getItems().addAll(rendimentos);
        } else {
            cbRendimento.getItems()
                    .addAll((new RendimentoDAO()).getAll(TelaPrincipalController.getUser().getFamilia()));
        }

        if (tarefa == "actualizar") {
            lblApresentacao.setText("Actualize A Previsão");
            btnRegistrar.setText("Actualizar Previsão");
            ipFinalidade.setText(previsao.getFinalidade());
            ipDesignacao.setText(previsao.getDesignacao());
            ipValor.setText(previsao.getValor().toString());
            cbRendimento.setValue(previsao.getRendimento());
            cbMes.setValue(previsao.getMes());
            cbRendimento.setDisable(true);
        }

    }

    @FXML
    void adicionarPrevisao(ActionEvent event) {
        if (validar()) {
            var mes = cbMes.getValue();
            var finalidade = ipFinalidade.getText();
            var designacao = ipDesignacao.getText();
            var valor = Integer.parseInt(ipValor.getText());
            var rendimento = cbRendimento.getValue();

            if (tarefa == "actualizar") {
                previsao.setMes(mes);
                previsao.setFinalidade(finalidade);
                previsao.setDesignacao(designacao);

                rendimento.setValor(rendimento.getValor() + previsao.getValor());
                rendimento.setValor(rendimento.getValor() - valor);
                if (rendimento.getValor() < 0) {
                    lblNotificacoes.setText("Não é possível colocar esse valor na previsão");
                    return;
                }

                previsao.setValor(valor);

                (new RendimentoDAO()).save(rendimento);
                (new PrevisaoDAO()).save(previsao);

            } else {
                var previsao = new PrevisaoMensal(designacao, finalidade, valor, mes);
                var familia = TelaPrincipalController.getUser().getFamilia();

                previsao.setFamilia(familia);
                previsao.setRendimento(rendimento);
                rendimento.setValor(rendimento.getValor() - valor);

                (new RendimentoDAO()).save(rendimento);
                (new PrevisaoDAO()).save(previsao);
            }
            App.setRoot("TelaPrincipal");
            TelaPrincipalController.getStage().close();
        }

    }

    boolean validar() {
        var mes = cbMes.getValue();
        var finalidade = ipFinalidade.getText();
        var designacao = ipDesignacao.getText();
        var valor = ipValor.getText();
        var rendimento = cbRendimento.getValue();
        var r = false;
        if (Validation.isNome(designacao).isPresent())
            lblNotificacoes.setText("A Designação Digitada é Invalida");
        if (Validation.isNome(finalidade).isPresent())
            lblNotificacoes.setText("A Finalidade Digitada é Invalida");
        if (!Validation.isNumeric(valor))
            lblNotificacoes.setText("O Valor Digitado É Invalido");
        else if (mes == null)
            lblNotificacoes.setText("Escolha Um Mês");
        else if (rendimento == null)
            lblNotificacoes.setText("Escolha Um Rendimento");
        else if (rendimento.getValor() < Integer.parseInt(valor))
            lblNotificacoes.setText("O Rendimento Escolhido Não Possui Dinheiro Suficiente Para Criar Esta Previsão");
        else
            r = true;

        return r;
    }

    public static void setTarefa(String tarefa) {
        PrevisaoMensalController.tarefa = tarefa;
    }

    public static void setPrevisao(PrevisaoMensal previsao) {
        PrevisaoMensalController.previsao = previsao;
    }
}
