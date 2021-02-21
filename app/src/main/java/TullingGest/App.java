
package TullingGest;

import java.io.IOException;

import TullingGest.classes.dao.DAO;
import TullingGest.classes.dao.PessoaDAO;
import TullingGest.controllers.TelaPrincipalController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private static Scene scene;

    private static Stage stage = new Stage();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        DAO.session();
        DAO.session().close();

        scene = new Scene(loadFXML("Login"), 500, 600);
        App.stage.setScene(scene);
        App.stage.setResizable(false);
        App.stage.setTitle("3MDTullingGest");
        App.stage.show();
    }

    public static Stage getStage() {
        return stage;
    }

    public static Scene getScene() {
        return scene;
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

    public static void setRoot(String fxml, int largura, int altura) {
        scene = new Scene(loadFXML(fxml), 500, 600);
        App.stage.setScene(scene);
    }

}