package TullingGest.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import TullingGest.App;
import TullingGest.classes.dao.FamiliaDAO;
import TullingGest.classes.entity.Admin;
import TullingGest.classes.entity.Familia;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AdminController implements Initializable {
    private static Stage stage = new Stage();

    @FXML
    private AnchorPane apPainelCentral;

    @FXML
    private ListView<Label> lvFamilias;
    private static Scene scene;
    private static Admin admin;
    private static ObservableList<Familia> familias = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        stage.setTitle("3MDTullingGest");
        stage.setResizable(false);
        TableView<Familia> tableView = new TableView<>();
        TableColumn<Familia, String> tableColumnNome = new TableColumn<>("Nome Da Família");
        TableColumn<Familia, String> tableColumnPai = new TableColumn<>("Pai Da Família");
        TableColumn<Familia, String> tableColumnMae = new TableColumn<>("Mãe Da Família");
        tableView.setItems(Familia.observableFamilias());
        tableView.getColumns().addAll(tableColumnNome, tableColumnPai, tableColumnMae);

        tableColumnMae.setCellValueFactory(new PropertyValueFactory<>("mae"));
        tableColumnPai.setCellValueFactory(new PropertyValueFactory<>("pai"));
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        apPainelCentral.getChildren().add(tableView);

        setCenter(tableView);

        familias.addAll((new FamiliaDAO()).getAll());
    }

    public static void setAdmin(Admin admin) {
        AdminController.admin = admin;
    }

    public static Admin getAdmin() {
        return admin;
    }

    public static Stage getStage() {
        return stage;
    }

    @FXML
    void eliminarFamilia(ActionEvent event) {
        Stage stage = new Stage();
        AnchorPane anchorPane = new AnchorPane();
        ListView<HBox> listView = new ListView<>();
        var familias = (new FamiliaDAO()).getAll();
        if (familias.size() == 0) {
            Label label = new Label("Ainda Não Registrou Nenhuma Família");
            label.setAlignment(Pos.CENTER);
            label.setFont(Font.font(20));
            anchorPane.getChildren().addAll(label);
            setCenter(label);
        } else {
            familias.forEach(familia -> {
                var hbox = new HBox();
                Button button = new Button("Remover Familia");
                button.setStyle("btnLogin");
                hbox.getChildren().add(new Label(familia.toString()));
                hbox.getChildren().add(button);
                hbox.setAlignment(Pos.CENTER);
                hbox.setSpacing(10);
                button.setOnAction(t -> {
                    (new FamiliaDAO()).delete(familia);
                    stage.close();
                });

                listView.getItems().add(hbox);
            });
            anchorPane.getChildren().addAll(listView);
            setCenter(listView);
        }

        Scene scene = new Scene(anchorPane, 500, 600);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void novaFamilia(ActionEvent event) {
        stage.setWidth(570);
        setRoot("Familia");
        stage.show();
    }

    @FXML
    void sair(ActionEvent event) {
        clear();
        App.getStage().setMaximized(false);
        App.setRoot("Login", 500, 600);
        App.getStage().setResizable(false);
        App.getStage().show();
        stage.close();
    }

    public void clear() {

        if (stage.isShowing()) {
            stage.close();
        }

        FamiliaController.setApresentacao("Cadastre A Sua Familia");
        admin = null;
        FamiliaController.setTarefa(null);
        PessoaController.setTarefa(null);
        AnimalController.setTarefa(null);
        PessoaController.setTexto("");
        PessoaController.setMembro(null);
        PessoaController.setTarefa(null);
        AnimalController.setTarefa(null);
    }

    public static Parent loadFXML(String fxml) {
        Parent r = null;
        FXMLLoader fxmlLoader = new FXMLLoader(
                ClassLoader.getSystemClassLoader().getResource("fxml/" + fxml + ".fxml"));
        try {
            r = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return r;
    }

    public static void setRoot(String fxml) {
        scene.setRoot(loadFXML(fxml));
    }

    public static ObservableList<Familia> getFamilias() {
        return familias;
    }

    private static void setCenter(Node node) {
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
    }

}
