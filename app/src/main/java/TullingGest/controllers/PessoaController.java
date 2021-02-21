package TullingGest.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import TullingGest.classes.Funcionalidades;
import TullingGest.classes.Validation;
import TullingGest.classes.dao.FamiliaDAO;
import TullingGest.classes.dao.PessoaDAO;
import TullingGest.classes.entity.Pessoa;
import TullingGest.enums.TipoDeMembro;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class PessoaController implements Initializable {

    @FXML
    private StackPane spSenha;

    @FXML
    private TextField ipSenha;

    @FXML
    private PasswordField pfSenha;

    @FXML
    private ImageView ivVerSenha;

    @FXML
    private AnchorPane PainelCadastroMembro;
    @FXML
    private Label lblApresentacao;
    @FXML
    private DatePicker dpDataDeNascimento;
    @FXML
    private VBox vbContactos;
    @FXML
    private TextField ipTelefone;
    @FXML
    private TextField ipEmail;
    @FXML
    private Label lblNotificacoes;
    @FXML
    private Button btnCadastrar;
    @FXML
    private HBox boxVoltar;
    @FXML
    private VBox vbCadastrarPessoa;
    @FXML
    private TextField ipNome;

    private static TipoDeMembro membro;
    private static String texto;
    private static String tarefa;
    private static Image olho;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ipSenha.textProperty().bindBidirectional(pfSenha.textProperty());
        ivVerSenha.setOnMouseClicked((event) -> Funcionalidades.trocarPF(ivVerSenha, olho, spSenha));
        PainelCadastroMembro.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                try {
                    cadastrar(new ActionEvent());
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        if (tarefa != null) {
            boxVoltar.setVisible(false);
            boxVoltar.setDisable(true);
        }

        if (tarefa == "alterar") {
            dpDataDeNascimento.setVisible(false);
            dpDataDeNascimento.setDisable(true);
            var user = TelaPrincipalController.getUser();
            ipNome.setText(user.getNome());
            ipTelefone.setText(user.getTelefone());
            ipEmail.setText(user.getEmail());
        }

        boxVoltar.setOnMouseClicked((event) -> {
            AdminController.getStage().setWidth(570);
            AdminController.setRoot("Familia");
        });

        lblApresentacao.setText(texto);
    }

    @FXML
    private void cadastrar(ActionEvent event) throws IOException, SQLException {

        if (tarefa == null) {
            if (validar()) {
                var pessoa = new Pessoa(ipNome.getText(), ipTelefone.getText(), ipEmail.getText(),
                        dpDataDeNascimento.getValue(), membro, ipSenha.getText());
                switch (membro) {
                    case Pai:
                        FamiliaController.setPai(pessoa);
                        FamiliaController.setTexto("O Pai Foi Cadastrado");
                        break;
                    case Mãe:
                        FamiliaController.setMae(pessoa);
                        FamiliaController.setTexto("A Mãe Foi Cadastrada");
                        break;
                    case Filho:
                        FamiliaController.getFilhos().add(pessoa);
                        FamiliaController.setTexto("Um Filho Foi Cadastrado");
                }
                AdminController.getStage().setWidth(570);
                AdminController.setRoot("Familia");
            }
        } else {
            switch (tarefa)

            {
                case "alterar":
                    if (validarAlteracao()) {
                        var user = TelaPrincipalController.getUser();
                        user.setNome(ipNome.getText());
                        user.setTelefone(ipTelefone.getText());
                        user.setEmail(ipEmail.getText());
                        (new PessoaDAO()).save(user);
                        TelaPrincipalController.getStage().close();
                    }
                    break;
                case "adicionar":

                    if (validar()) {
                        var familia = TelaPrincipalController.getUser().getFamilia();
                        var pessoa = new Pessoa(ipNome.getText(), ipTelefone.getText(), ipEmail.getText(),
                                dpDataDeNascimento.getValue(), TipoDeMembro.Filho, ipSenha.getText());
                        pessoa.setFamilia(familia);
                        familia.getMembros().add(pessoa);
                        (new FamiliaDAO()).save(familia);
                        TelaPrincipalController.getStage().close();
                    }

                    break;

            }
        }

    }

    public static void setMembro(TipoDeMembro membro) {
        PessoaController.membro = membro;
    }

    public static void setTexto(String texto) {
        PessoaController.texto = texto;
    }

    public boolean validar() {

        var nome = ipNome.getText();
        var email = ipEmail.getText();
        var telefone = ipTelefone.getText();
        var data = dpDataDeNascimento.getValue();
        var r = false;
        if (Validation.isNome(nome).isPresent()) {
            lblNotificacoes.setText(Validation.isNome(nome).get());
        } else if (Validation.isEmail(email).isPresent()) {
            lblNotificacoes.setText(Validation.isEmail(email).get());
        } else if (Validation.isUniqueEmail(email).isPresent()) {
            lblNotificacoes.setText(Validation.isUniqueEmail(email).get());
        } else if (Validation.isTelefone(telefone).isPresent()) {
            lblNotificacoes.setText(Validation.isTelefone(telefone).get());
        } else if (Validation.isUniqueTelefone(telefone).isPresent()) {
            lblNotificacoes.setText(Validation.isUniqueTelefone(telefone).get());
        } else if (data == null) {
            lblNotificacoes.setText("Escolha Uma Data De Nascimento");
        } else if (!data.isBefore(LocalDate.now())) {
            lblNotificacoes.setText("A Data Inserida É Invalida");
        } else {
            r = true;
        }

        return r;
    }

    private boolean validarAlteracao() {
        var nome = ipNome.getText();
        var email = ipEmail.getText();
        var telefone = ipTelefone.getText();
        var r = false;
        var pessoa = TelaPrincipalController.getUser();
        if (pessoa.getNome().equals(nome) && pessoa.getEmail().equals(email) && pessoa.getTelefone().equals(telefone)) {
            lblNotificacoes.setText("Nenhuma Informação Foi Alterada");
        } else if (!nome.equals(pessoa.getNome())) {
            if (Validation.isNome(nome).isPresent())
                lblNotificacoes.setText(Validation.isNome(nome).get());
            else
                r = true;

        } else if (!email.equals(pessoa.getEmail())) {
            if (Validation.isEmail(email).isPresent())
                lblNotificacoes.setText(Validation.isEmail(email).get());
            else if (Validation.isUniqueEmail(email).isPresent())
                lblNotificacoes.setText(Validation.isUniqueEmail(email).get());
            else
                r = true;

        } else if (!telefone.equals(pessoa.getTelefone())) {
            if (Validation.isTelefone(telefone).isPresent())
                lblNotificacoes.setText(Validation.isTelefone(telefone).get());
            else if (Validation.isUniqueTelefone(telefone).isPresent())
                lblNotificacoes.setText(Validation.isUniqueTelefone(telefone).get());
            else
                r = true;
        }

        return r;
    }

    public static void setTarefa(String tarefa) {
        PessoaController.tarefa = tarefa;
    }

}
