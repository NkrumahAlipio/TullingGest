package TullingGest.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import TullingGest.App;
import TullingGest.classes.Validation;
import TullingGest.classes.dao.FamiliaDAO;
import TullingGest.classes.entity.Animal;
import TullingGest.enums.Raca;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class AnimalController implements Initializable {

    @FXML
    private AnchorPane PainelCadastroMembro;

    @FXML
    private HBox boxVoltar;

    @FXML
    private Label lblApresentacao;

    @FXML
    private VBox vbCadastrarPessoa;

    @FXML
    private VBox vbInformacoesPessoais;

    @FXML
    private TextField ipNome;

    @FXML
    private ChoiceBox<Raca> cbRaca;

    @FXML
    private DatePicker dpDataDeNascimento;

    @FXML
    private Button btnCadastrar;

    @FXML
    private Label lblNotificacoes;

    private static String tarefa;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        PainelCadastroMembro.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                try {
                    cadastrar(new ActionEvent());
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        if (tarefa == "adicionar") {
            boxVoltar.setVisible(false);
            boxVoltar.setDisable(true);
        }

        boxVoltar.setOnMouseClicked((event) -> {
            AdminController.getStage().setWidth(570);
            AdminController.setRoot("Familia");
        });
        cbRaca.setValue(Raca.Cão);
        cbRaca.getItems().setAll(Raca.values());

    }

    @FXML
    private void cadastrar(ActionEvent event) throws IOException, SQLException {

        if (tarefa == null) {
            if (validar()) {
                var animal = new Animal(ipNome.getText(), dpDataDeNascimento.getValue(), cbRaca.getValue());
                FamiliaController.getAnimais().add(animal);
                FamiliaController.setTexto("O Animal Foi Cadastrado");
                AdminController.getStage().setWidth(570);
                AdminController.setRoot("Cadastro");
            }
        } else if (tarefa == "adicionar") {
            if (validar()) {
                var familia = TelaPrincipalController.getUser().getFamilia();
                var animal = new Animal(ipNome.getText(), dpDataDeNascimento.getValue(), cbRaca.getValue());
                animal.setFamilia(familia);
                familia.getMembros().add(animal);
                (new FamiliaDAO()).save(familia);
                TelaPrincipalController.getStage().close();
            }
        }

    }

    private boolean validar() {
        var nome = ipNome.getText();
        var data = dpDataDeNascimento.getValue();
        var r = false;
        if (Validation.isNome(nome).isPresent()) {
            lblNotificacoes.setText(Validation.isNome(nome).get());
        } else if (data == null) {
            lblNotificacoes.setText("Escolha Uma Data De Nascimento");
        } else if (!data.isBefore(LocalDate.now())) {
            lblNotificacoes.setText("A Data Inserida É Invalida");
        } else
            r = true;
        return true;

    }

    public static void setTarefa(String tarefa) {
        AnimalController.tarefa = tarefa;

    }

}
