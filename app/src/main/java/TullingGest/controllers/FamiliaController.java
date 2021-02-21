package TullingGest.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import TullingGest.classes.Validation;
import TullingGest.classes.dao.FamiliaDAO;
import TullingGest.classes.entity.Animal;
import TullingGest.classes.entity.Endereco;
import TullingGest.classes.entity.Familia;
import TullingGest.classes.entity.MembroDaFamilia;
import TullingGest.classes.entity.Pessoa;
import TullingGest.enums.TipoDeMembro;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author nkrumah
 */
public class FamiliaController implements Initializable {

    @FXML
    private AnchorPane painelCadastro;

    @FXML
    private TextField ipNome;

    @FXML
    private TextField ipBairro;
    @FXML
    private TextField ipRua;
    @FXML
    private TextField ipTelefone;
    @FXML
    private Button btnCadastrarPai;
    @FXML
    private Button btnCadastrarMae;
    @FXML
    private Button btnCadastrarFilho;
    @FXML
    private Button btnCadastrarAnimal;
    @FXML
    private Button btnConcluirCadastro;
    @FXML
    private Label lblNotificacoes;
    @FXML
    private VBox vbMembros;
    @FXML
    private Label lblApresentacao;

    private static String texto = "";
    private static Pessoa pai;
    private static Pessoa mae;
    private static final List<Pessoa> filhos = new ArrayList<>();
    private static final List<Animal> animais = new ArrayList<>();
    private Image olho;
    private static String tarefa;
    private static String apresentacao = "Cadastre A Sua Família";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        painelCadastro.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                try {
                    concluirCadastroHandler(new ActionEvent());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        if (tarefa != null) {
            btnConcluirCadastro.setText("Concluir Alterações");
            var familia = TelaPrincipalController.getUser().getFamilia();

            vbMembros.setVisible(false);
            vbMembros.setDisable(true);
            ipNome.setText(familia.getNome());
            ipRua.setText(familia.getEndereco().getRua());
            ipBairro.setText(familia.getEndereco().getBairro());
            ipTelefone.setText(familia.getTelefone());
        }

        lblApresentacao.setText(apresentacao);
        lblNotificacoes.setText(texto);
        if (pai != null) {
            btnCadastrarPai.setDisable(true);
        }
        if (mae != null) {
            btnCadastrarMae.setDisable(true);
        }
    }

    @FXML
    private void cadastroHandler(ActionEvent event) throws IOException {
        if (event.getSource().equals(btnCadastrarPai)) {
            PessoaController.setMembro(TipoDeMembro.Pai);
            PessoaController.setTexto("Cadastre O Pai Da Família");
        } else if (event.getSource().equals(btnCadastrarMae)) {
            PessoaController.setMembro(TipoDeMembro.Mãe);
            PessoaController.setTexto("Cadastre A Mãe Da Família");
        } else if (event.getSource().equals(btnCadastrarFilho)) {
            PessoaController.setMembro(TipoDeMembro.Filho);
            PessoaController.setTexto("Cadastre Um Filho Da Família");
        } else {
            AdminController.getStage().setWidth(500);
            AdminController.setRoot("Animal");
            return;
        }
        AdminController.getStage().setWidth(500);
        AdminController.setRoot("Pessoa");
    }

    public static void setMae(Pessoa mae) {
        FamiliaController.mae = mae;
    }

    public static void setPai(Pessoa pai) {
        FamiliaController.pai = pai;
    }

    public static List<Pessoa> getFilhos() {
        return filhos;
    }

    public static List<Animal> getAnimais() {
        return animais;
    }

    public static Pessoa getPai() {
        return pai;
    }

    public static Pessoa getMae() {
        return mae;
    }

    public static List<Pessoa> getPessoas() {
        List<Pessoa> pessoas = new ArrayList<Pessoa>();
        if (pai != null)
            pessoas.add(pai);
        if (mae != null)
            pessoas.add(mae);
        if (!filhos.isEmpty())
            pessoas.addAll(filhos);
        return pessoas;

    }

    @FXML
    private void concluirCadastroHandler(ActionEvent event) throws IOException {
        if (tarefa == null) {
            if (validar()) {
                Set<MembroDaFamilia> membros = new HashSet<>();
                membros.add(pai);
                membros.add(mae);
                membros.addAll(filhos);
                membros.addAll(animais);
                var endereco = new Endereco(ipBairro.getText(), ipRua.getText());
                var familia = new Familia(ipNome.getText(), ipTelefone.getText(), membros, endereco);
                (new FamiliaDAO()).save(familia);
                AdminController.getFamilias().add(familia);
                clear();
                AdminController.getStage().close();
            }
        } else {
            if (validarAlteracao()) {
                var familia = TelaPrincipalController.getUser().getFamilia();
                familia.setNome(ipNome.getText());
                familia.setTelefone(ipTelefone.getText());
                familia.setEndereco(new Endereco(ipBairro.getText(), ipRua.getText()));
                (new FamiliaDAO()).save(familia);
                AdminController.getStage().close();
            }
        }
    }

    private boolean validar() {
        var nome = ipNome.getText();
        var telefone = ipTelefone.getText();

        var bairro = ipBairro.getText();
        var rua = ipRua.getText();
        var r = false;

        if (pai == null)
            lblNotificacoes.setText("Cadastre O Pai Da Familia");
        else if (mae == null)
            lblNotificacoes.setText("Cadastre A Mãe Da Familia");

        else if (Validation.isNome(nome).isPresent())
            lblNotificacoes.setText(Validation.isNome(nome).get());

        else if (Validation.isTelefone(telefone).isPresent())
            lblNotificacoes.setText(Validation.isTelefone(telefone).get());

        else if (Validation.isUniqueTelefone(telefone).isPresent())
            lblNotificacoes.setText(Validation.isUniqueTelefone(telefone).get());

        else if (Validation.isNome(bairro).isPresent())
            lblNotificacoes.setText("O Bairro Digitado é Invalido");

        else if (Validation.isNome(rua).isPresent())
            lblNotificacoes.setText("A Rua Digitado é Invalido");

        else
            r = true;

        return r;
    }

    boolean validarAlteracao() {
        var nome = ipNome.getText();
        var telefone = ipTelefone.getText();
        var rua = ipRua.getText();
        var bairro = ipBairro.getText();

        var r = false;
        var familia = TelaPrincipalController.getUser().getFamilia();

        if (nome.equals(familia.getNome()) && telefone.equals(familia.getTelefone())
                && rua.equals(familia.getEndereco().getRua()) && bairro.equals(familia.getEndereco().getBairro())) {
            lblNotificacoes.setText("Nenhum Campo Foi Alterado");
        } else if (!nome.equals(familia.getNome())) {

            if (Validation.isNome(nome).isPresent())
                lblNotificacoes.setText(Validation.isNome(nome).get());
            else
                r = true;

        } else if (!telefone.equals(familia.getTelefone())) {
            if (Validation.isTelefone(telefone).isPresent())
                lblNotificacoes.setText(Validation.isTelefone(telefone).get());
            else if (Validation.isUniqueTelefone(telefone).isPresent())
                lblNotificacoes.setText(Validation.isUniqueTelefone(telefone).get());
            else
                r = true;

        } else if (!bairro.equals(familia.getEndereco().getBairro())) {

            if (Validation.isNome(bairro).isPresent())
                lblNotificacoes.setText("O Bairro Digitado É Inválido");
            else
                r = true;

        } else if (!rua.equals(familia.getEndereco().getRua())) {

            if (Validation.isNome(rua).isPresent())
                lblNotificacoes.setText("A Rua Digitada É Inválida");
            else
                r = true;

        }

        return r;
    }

    public static void setTexto(String texto) {
        FamiliaController.texto = texto;
    }

    public static void clear() {
        pai = null;
        mae = null;
        filhos.clear();
        animais.clear();
        texto = "";
    }

    public static void setTarefa(String tarefa) {
        FamiliaController.tarefa = tarefa;
    }

    public static void setApresentacao(String texto) {
        apresentacao = texto;
    }

}
