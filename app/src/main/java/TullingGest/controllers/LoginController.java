package TullingGest.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import TullingGest.App;
import TullingGest.classes.Funcionalidades;
import TullingGest.classes.Validation;
import TullingGest.classes.dao.AdminDAO;
import TullingGest.classes.dao.PessoaDAO;
import TullingGest.classes.entity.Admin;
import TullingGest.classes.entity.Pessoa;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author nkrumah
 */
public class LoginController implements Initializable {

    @FXML
    private AnchorPane painelLogin;
    @FXML
    private TextField ipEmail;
    @FXML
    private StackPane spSenha;
    @FXML
    private TextField ipSenha;
    @FXML
    private PasswordField pfSenha;
    @FXML
    private ImageView ivVerSenha;
    @FXML
    private Button btnLogin;
    @FXML
    private Label lblRecSenha;
    @FXML
    private Label lblCadastrar;
    @FXML
    private Label lblNotificacoes;
    @FXML
    private ProgressBar progressBar;

    private Image olho;

    private static String texto = "";

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        painelLogin.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                try {
                    loginHandler(new ActionEvent());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        lblNotificacoes.setText(texto);
        ipSenha.textProperty().bindBidirectional(pfSenha.textProperty());
        ivVerSenha.setOnMouseClicked((event) -> Funcionalidades.trocarPF(ivVerSenha, olho, spSenha));

    }

    public static void login(Pessoa pessoa) {
        texto = "";
        TelaPrincipalController.setUser(pessoa);
        App.getStage().close();
        App.getStage().setMaximized(true);
        App.getStage().setResizable(true);
        App.setRoot("TelaPrincipal");
        App.getStage().show();
    }

    public static void loginAdmin(Admin admin) {
        AdminController.setAdmin(admin);
        App.getStage().close();
        App.getStage().setMaximized(true);
        App.getStage().setResizable(true);
        App.setRoot("Admin");
        App.getStage().show();
    }

    public static void setText(String texto) {
        LoginController.texto = texto;
    }

    @FXML
    private void loginHandler(ActionEvent event) throws IOException {
        String email = ipEmail.getText();
        String senha = ipSenha.getText();

        if (Validation.isEmail(email).isPresent()) {
            lblNotificacoes.setText(Validation.isEmail(email).get());
        } else if (Validation.isSenha(senha).isPresent()) {
            lblNotificacoes.setText(Validation.isSenha(senha).get());
        } else {
            if (email.contains("@admin.com")) {

                var admin = (new AdminDAO()).get(email, senha);
                if (admin.isPresent()) {
                    loginAdmin(admin.get());
                } else {
                    lblNotificacoes.setText("Este Admin não se encontra na nossa base de dados");
                }
            } else {
                var pessoa = (new PessoaDAO()).get(email, senha);
                if (pessoa.isPresent()) {
                    login(pessoa.get());
                } else {
                    lblNotificacoes.setText("Este Usuário não se encontra na nossa base de dados");
                }
            }

        }

    }

}
