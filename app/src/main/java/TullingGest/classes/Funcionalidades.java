package TullingGest.classes;

import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.function.UnaryOperator;

import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public abstract class Funcionalidades {
    static URL urlImagem = ClassLoader.getSystemClassLoader().getResource("Imagens/iconeOlho_32.png");

    public static void trocarPF(ImageView ivVerSenha, Image olho, StackPane spSenha) {

        olho = new Image(urlImagem.toString());
        Image imagem = ivVerSenha.getImage();
        ivVerSenha.setImage(olho);
        olho = imagem;

        spSenha.getChildren().get(1).setDisable(true);
        spSenha.getChildren().get(1).setVisible(false);
        spSenha.getChildren().get(0).setDisable(false);
        spSenha.getChildren().get(0).setVisible(true);
        spSenha.getChildren().get(1).toBack();
    }

    public static Integer calcularIdade(LocalDate dataNascimento) {
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    public static TextFormatter filtroNumerico() {
        UnaryOperator<TextFormatter.Change> filtro = change -> {
            String texto = change.getText();
            return texto.matches("[0-9]*") ? change : null;
        };
        
        return new TextFormatter<>(filtro);
    }

}
