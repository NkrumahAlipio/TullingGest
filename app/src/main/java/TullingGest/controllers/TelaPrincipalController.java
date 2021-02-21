package TullingGest.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.ResourceBundle;

import TullingGest.App;
import TullingGest.classes.Constantes;
import TullingGest.classes.Funcionalidades;
import TullingGest.classes.dao.CreditoDAO;
import TullingGest.classes.dao.GastoDAO;
import TullingGest.classes.dao.PessoaDAO;
import TullingGest.classes.dao.PoupancaDAO;
import TullingGest.classes.dao.PrevisaoDAO;
import TullingGest.classes.dao.RendimentoDAO;
import TullingGest.classes.entity.Animal;
import TullingGest.classes.entity.BalancoMensal;
import TullingGest.classes.entity.Credito;
import TullingGest.classes.entity.Familia;
import TullingGest.classes.entity.Gasto;
import TullingGest.classes.entity.Pessoa;
import TullingGest.classes.entity.Poupanca;
import TullingGest.classes.entity.PrevisaoMensal;
import TullingGest.classes.entity.Rendimento;
import TullingGest.enums.Raca;
import TullingGest.enums.TipoDeMembro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TelaPrincipalController implements Initializable {

    @FXML
    private TableView<Rendimento> tvRendimentos;

    @FXML
    private TableColumn<Rendimento, String> tcRendimentoValor;

    @FXML
    private TableColumn<Rendimento, String> tcRendimentoOrigem;

    @FXML
    private TableColumn<Rendimento, LocalDate> tcRendimentoData;

    @FXML
    private TableColumn<Rendimento, Pessoa> tcRendimentoCriador;

    @FXML
    private TableView<PrevisaoMensal> tvPrevisoes;

    @FXML
    private TableColumn<PrevisaoMensal, String> tcPrevisaoDesignacao;

    @FXML
    private TableColumn<PrevisaoMensal, String> tcPrevisaoValor;

    @FXML
    private TableColumn<PrevisaoMensal, String> tcPrevisaoFinalidade;

    @FXML
    private TableColumn<PrevisaoMensal, Month> tcPrevisaoMes;

    @FXML
    private TableView<Gasto> tvGastos;

    @FXML
    private TableColumn<Gasto, String> tcGastoDesignacao;

    @FXML
    private TableColumn<Gasto, String> tcGastoValor;

    @FXML
    private TableColumn<Gasto, String> tcGastoLocal;

    @FXML
    private TableColumn<Gasto, Pessoa> tcGastoCriador;

    @FXML
    private Label lblApresentacao;

    @FXML
    private AnchorPane apPainelCentral;

    @FXML
    private MenuItem miInfoFamilia;
    @FXML
    private MenuItem miAddFilho;

    @FXML
    private MenuItem miAddAnimal;

    @FXML
    private Menu mRendimentos;

    @FXML
    private Menu mCredito;

    @FXML
    private Menu mGasto;

    @FXML
    private Menu mPrevisao;

    @FXML
    private Menu mConsultar;

    @FXML
    private Label lblDinheiro;

    private static Stage stage = new Stage();
    private static Pessoa user;
    private ObservableList<Rendimento> rendimentos = FXCollections.observableArrayList();
    private ObservableList<Gasto> gastos = FXCollections.observableArrayList();
    private ObservableList<PrevisaoMensal> previsoes = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        user = (new PessoaDAO()).getById(user.getId());

        (new PrevisaoDAO()).getAll().forEach(t -> {
            if (LocalDate.now().getMonthValue() > t.getMes().getValue()) {
                t.setAtivo(false);
                var poupanca = new Poupanca(t.getMes(), t.getValor(), t);
                (new PoupancaDAO()).save(poupanca);
                (new PrevisaoDAO()).save(t);
            }
        });

        var valorTotal = (new RendimentoDAO()).getAll(user.getFamilia()).stream().mapToInt(t -> t.getValor()).sum();
        lblApresentacao.setText("Seja bem-vindo " + user.getNome());
        lblDinheiro.setText(Constantes.currencyFormatter.format(valorTotal));
        stage.setTitle("3MDTullingGest");
        stage.setResizable(false);

        if (user.getTipo() == TipoDeMembro.Filho) {
            miInfoFamilia.setVisible(false);
            miAddAnimal.setVisible(false);
            miAddFilho.setVisible(false);
        }

        if (user.getTipo() == TipoDeMembro.Filho) {
            miAddAnimal.setDisable(true);
            miAddFilho.setDisable(true);
            miInfoFamilia.setDisable(true);
        }

        rendimentos.addAll((new RendimentoDAO()).getAllWithTrash(user.getFamilia()));
        gastos.addAll((new GastoDAO()).getAll(user.getFamilia()));
        previsoes.addAll((new PrevisaoDAO()).getAllWithTrash(user.getFamilia()));

        tcRendimentoValor.setCellValueFactory(new PropertyValueFactory<>("dinheiro"));
        tcRendimentoOrigem.setCellValueFactory(new PropertyValueFactory<>("origem"));
        tcRendimentoData.setCellValueFactory(new PropertyValueFactory<>("data"));
        tcRendimentoCriador.setCellValueFactory(new PropertyValueFactory<>("pessoa"));

        tcPrevisaoDesignacao.setCellValueFactory(new PropertyValueFactory<>("designacao"));
        tcPrevisaoValor.setCellValueFactory(new PropertyValueFactory<>("dinheiro"));
        tcPrevisaoFinalidade.setCellValueFactory(new PropertyValueFactory<>("finalidade"));
        tcPrevisaoMes.setCellValueFactory(new PropertyValueFactory<>("month"));

        tcGastoDesignacao.setCellValueFactory(new PropertyValueFactory<>("designacao"));
        tcGastoValor.setCellValueFactory(new PropertyValueFactory<>("dinheiro"));
        tcGastoLocal.setCellValueFactory(new PropertyValueFactory<>("local"));
        tcGastoCriador.setCellValueFactory(new PropertyValueFactory<>("pessoa"));

        tvGastos.setItems(gastos);
        tvRendimentos.setItems(rendimentos);
        tvPrevisoes.setItems(previsoes);

    }

    public static Stage getStage() {
        return stage;
    }

    public static void setUser(Pessoa user) {
        TelaPrincipalController.user = user;
    }

    public static Pessoa getUser() {
        return user;
    }

    @FXML
    void actualizarCreditos(ActionEvent event) {
        Stage stage = new Stage();
        AnchorPane anchorPane = new AnchorPane();
        ListView<HBox> listView = new ListView<>();
        var creditos = user.getCreditos();
        if (creditos.size() == 0) {
            Label label = new Label("Ainda Não Registrou Nenhum Credito");
            label.setAlignment(Pos.CENTER);
            label.setFont(Font.font(20));
            anchorPane.getChildren().addAll(label);
            setCenter(label);
        } else {
            creditos.forEach(credito -> {
                var hbox = new HBox();
                Button button = new Button("Actualizar Credito");
                hbox.getChildren().add(new Label(credito.toString()));
                hbox.getChildren().add(button);
                hbox.setAlignment(Pos.CENTER);
                hbox.setSpacing(10);
                button.setOnAction(t -> {
                    CreditoController.setTarefa("actualizar");
                    CreditoController.setCredito(credito);
                    Scene scene = new Scene(App.loadFXML("Credito"));
                    TelaPrincipalController.getStage().setScene(scene);
                    stage.close();
                    TelaPrincipalController.getStage().show();
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
    void actualizarGasto(ActionEvent event) {
        Stage stage = new Stage();
        AnchorPane anchorPane = new AnchorPane();
        ListView<HBox> listView = new ListView<>();
        var gastos = user.getGastos();
        if (gastos.size() == 0) {
            Label label = new Label("Ainda Não Registrou Nenhum Gasto");
            label.setAlignment(Pos.CENTER);
            label.setFont(Font.font(20));
            anchorPane.getChildren().addAll(label);
            setCenter(label);
        } else {
            gastos.forEach(gasto -> {
                var hbox = new HBox();
                Button button = new Button("Actualizar Gasto");
                hbox.getChildren().add(new Label(gasto.toString()));
                hbox.getChildren().add(button);
                hbox.setAlignment(Pos.CENTER);
                hbox.setSpacing(10);
                button.setOnAction(t -> {
                    GastoController.setTarefa("actualizar");
                    GastoController.setGasto(gasto);
                    Scene scene = new Scene(App.loadFXML("Gasto"));
                    TelaPrincipalController.getStage().setScene(scene);
                    stage.close();
                    TelaPrincipalController.getStage().show();
                });

                listView.getItems().add(hbox);
            });
            anchorPane.getChildren().addAll(listView);
            setCenter(listView);
            stage.show();
        }
        Scene scene = new Scene(anchorPane, 500, 600);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void actualizarPrevisao(ActionEvent event) {
        Stage stage = new Stage();
        AnchorPane anchorPane = new AnchorPane();
        ListView<HBox> listView = new ListView<>();
        var previsoes = user.getFamilia().getPrevisoes();
        if (previsoes.size() == 0) {
            Label label = new Label("Ainda Não Registrou Nenhuma Rendimento");
            label.setAlignment(Pos.CENTER);
            label.setFont(Font.font(20));
            anchorPane.getChildren().addAll(label);
            setCenter(label);
        } else {
            previsoes.forEach(previsao -> {
                var hbox = new HBox();
                Button button = new Button("Actualizar Rendimento");
                button.setStyle("btnLogin");
                hbox.getChildren().add(new Label(previsao.toString()));
                hbox.getChildren().add(button);
                hbox.setAlignment(Pos.CENTER);
                hbox.setSpacing(10);
                button.setOnAction(t -> {
                    PrevisaoMensalController.setTarefa("actualizar");
                    PrevisaoMensalController.setPrevisao(previsao);
                    Scene scene = new Scene(App.loadFXML("PrevisaoMensal"));
                    TelaPrincipalController.getStage().setScene(scene);
                    stage.close();
                    TelaPrincipalController.getStage().show();
                });

                listView.getItems().add(hbox);
            });
            anchorPane.getChildren().addAll(listView);
            setCenter(listView);
            stage.show();
        }
        Scene scene = new Scene(anchorPane, 500, 600);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void actualizarRendimento(ActionEvent event) {
        Stage stage = new Stage();
        AnchorPane anchorPane = new AnchorPane();
        ListView<HBox> listView = new ListView<>();
        var rendimentos = user.getRendimentos();
        if (rendimentos.size() == 0) {
            Label label = new Label("Ainda Não Registrou Nenhuma Rendimento");
            label.setAlignment(Pos.CENTER);
            label.setFont(Font.font(20));
            anchorPane.getChildren().addAll(label);
            setCenter(label);
        } else {
            rendimentos.forEach(rendimento -> {
                var hbox = new HBox();
                Button button = new Button("Actualizar Rendimento");
                button.setStyle("btnLogin");
                hbox.getChildren().add(new Label(rendimento.toString()));
                hbox.getChildren().add(button);
                hbox.setAlignment(Pos.CENTER);
                hbox.setSpacing(10);
                button.setOnAction(t -> {
                    RendimentoController.setTarefa("actualizar");
                    RendimentoController.setRendimento(rendimento);
                    Scene scene = new Scene(App.loadFXML("Rendimento"));
                    TelaPrincipalController.getStage().setScene(scene);
                    stage.close();
                    TelaPrincipalController.getStage().show();
                });

                listView.getItems().add(hbox);
            });
            anchorPane.getChildren().addAll(listView);
            setCenter(listView);
            stage.show();
        }
        Scene scene = new Scene(anchorPane, 500, 600);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void addAnimal(ActionEvent event) {
        AnimalController.setTarefa("adicionar");
        Scene scene = new Scene(App.loadFXML("Animal"));
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void addFilho(ActionEvent event) {
        PessoaController.setTarefa("adicionar");
        PessoaController.setTexto("Adicione O Novo Membro Da Familia");
        Scene scene = new Scene(App.loadFXML("Pessoa"));
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void ajudaOnline(ActionEvent event) {

    }

    @FXML
    void alterarInfoConta(ActionEvent event) {
        PessoaController.setTarefa("alterar");
        PessoaController.setTexto("Altere As informações Da Sua Conta");
        Scene scene = new Scene(App.loadFXML("Pessoa"));
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void alterarInfoFamilia(ActionEvent event) {
        FamiliaController.setTarefa("alterar");
        FamiliaController.setApresentacao("Altere As Informações Da Familia");
        Scene scene = new Scene(App.loadFXML("Familia"));
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void consultarFamilia(ActionEvent event) {
        VBox vBox = new VBox();
        Stage stage = new Stage();
        AnchorPane anchorPane = new AnchorPane();

        TableView<Familia> tableViewFamilia = new TableView<>();

        TableView<Pessoa> tableViewFilhos = new TableView<>();

        TableView<Animal> tableViewAnimais = new TableView<>();

        TableColumn<Familia, String> tableColumnFamiliaNome = new TableColumn<>("Nome Da Família");
        TableColumn<Familia, String> tableColumnPai = new TableColumn<>("Pai Da Família");
        TableColumn<Familia, String> tableColumnMae = new TableColumn<>("Mãe Da Família");

        TableColumn<Pessoa, String> tableColumFilhosNome = new TableColumn<>("Nome");
        TableColumn<Pessoa, Integer> tableColumFilhosIdade = new TableColumn<>("Idade");
        TableColumn<Pessoa, String> tableColumFilhosEmail = new TableColumn<>("Email");
        TableColumn<Pessoa, String> tableColumFilhosTelefone = new TableColumn<>("Telefone");

        TableColumn<Animal, String> tableColumAnimaisNome = new TableColumn<>("Nome");
        TableColumn<Animal, Integer> tableColumAnimaisIdade = new TableColumn<>("Idade");
        TableColumn<Animal, Raca> tableColumAnimaisRaca = new TableColumn<>("Raça");

        ObservableList<Familia> ob = FXCollections.observableArrayList();
        ob.add(user.getFamilia());

        ObservableList<Pessoa> ob2 = FXCollections.observableArrayList();
        ob2.addAll(user.getFamilia().getFilhos());

        ObservableList<Animal> ob3 = FXCollections.observableArrayList();
        ob3.addAll(user.getFamilia().getAnimais());

        tableViewFamilia.setItems(ob);
        tableViewFilhos.setItems(ob2);
        tableViewAnimais.setItems(ob3);

        tableViewFamilia.getColumns().addAll(tableColumnFamiliaNome, tableColumnPai, tableColumnMae);

        tableViewAnimais.getColumns().addAll(tableColumAnimaisNome, tableColumAnimaisRaca, tableColumAnimaisIdade);

        tableViewFilhos.getColumns().addAll(tableColumFilhosNome, tableColumFilhosEmail, tableColumFilhosTelefone,
                tableColumFilhosIdade);

        tableColumnMae.setCellValueFactory(new PropertyValueFactory<>("mae"));
        tableColumnPai.setCellValueFactory(new PropertyValueFactory<>("pai"));
        tableColumnFamiliaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        tableColumAnimaisNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumAnimaisIdade.setCellValueFactory(new PropertyValueFactory<>("idade"));
        tableColumAnimaisRaca.setCellValueFactory(new PropertyValueFactory<>("raca"));

        tableColumFilhosNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumFilhosIdade.setCellValueFactory(new PropertyValueFactory<>("idade"));
        tableColumFilhosTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        tableColumFilhosEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        vBox.getChildren().addAll(tableViewFamilia, tableViewFilhos, tableViewAnimais);

        anchorPane.getChildren().add(vBox);
        setCenter(vBox);

        Scene scene = new Scene(anchorPane, 800, 800);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void eliminarCredito(ActionEvent event) {
        Stage stage = new Stage();
        AnchorPane anchorPane = new AnchorPane();
        ListView<HBox> listView = new ListView<>();
        var creditos = user.getCreditos();
        if (creditos.size() == 0) {
            Label label = new Label("Ainda Não Registrou Nenhum Credito");
            label.setAlignment(Pos.CENTER);
            label.setFont(Font.font(20));
            anchorPane.getChildren().addAll(label);
            setCenter(label);
        } else {
            creditos.forEach(credito -> {
                var hbox = new HBox();
                Button button = new Button("Pagar Credito");
                hbox.getChildren().add(new Label(credito.toString()));
                hbox.getChildren().add(button);
                hbox.setAlignment(Pos.CENTER);
                hbox.setSpacing(10);
                button.setOnAction(t -> {
                    DividaController.setCredito(credito);
                    Scene scene = new Scene(App.loadFXML("Divida"));
                    TelaPrincipalController.getStage().setScene(scene);
                    stage.close();
                    TelaPrincipalController.getStage().show();
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
    void eliminarGasto(ActionEvent event) {
        Stage stage = new Stage();
        AnchorPane anchorPane = new AnchorPane();
        ListView<HBox> listView = new ListView<>();
        var gastos = user.getGastos();
        if (gastos.size() == 0) {
            Label label = new Label("Ainda Não Registrou Nenhum Gasto");
            label.setAlignment(Pos.CENTER);
            label.setFont(Font.font(20));
            anchorPane.getChildren().addAll(label);
            setCenter(label);
        } else {
            gastos.forEach(gasto -> {
                var hbox = new HBox();
                Button button = new Button("Remover Gasto");
                button.setStyle("btnLogin");
                hbox.getChildren().add(new Label(gasto.toString()));
                hbox.getChildren().add(button);
                hbox.setAlignment(Pos.CENTER);
                hbox.setSpacing(10);
                button.setOnAction(t -> {
                    var previsao = gasto.getPrevisao();
                    if (!previsao.isAtivo())
                        previsao.setAtivo(true);
                    previsao.setValor(previsao.getValor() + gasto.getValor());
                    (new PrevisaoDAO()).save(previsao);
                    (new GastoDAO()).delete(gasto);
                    App.setRoot("TelaPrincipal");
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
    void eliminarPrevisao(ActionEvent event) {
        Stage stage = new Stage();
        AnchorPane anchorPane = new AnchorPane();
        ListView<HBox> listView = new ListView<>();
        var previsoes = (new PrevisaoDAO()).getAll(user.getFamilia());
        if (previsoes.size() == 0) {
            Label label = new Label("Ainda Não Registrou Nenhuma Previsão Mensal");
            label.setAlignment(Pos.CENTER);
            label.setFont(Font.font(20));
            anchorPane.getChildren().addAll(label);
            setCenter(label);
        } else {
            previsoes.forEach(previsao -> {
                var hbox = new HBox();
                Button button = new Button("Remover Previsão");
                button.setStyle("btnLogin");
                hbox.getChildren().add(new Label(previsao.toString()));
                hbox.getChildren().add(button);
                hbox.setAlignment(Pos.CENTER);
                hbox.setSpacing(10);
                button.setOnAction(t -> {
                    var gastos = previsao.getGastos();
                    var rendimento = previsao.getRendimento();
                    if (!rendimento.isAtivo())
                        rendimento.setAtivo(true);

                    gastos.forEach(gasto -> {
                        rendimento.setValor(rendimento.getValor() + gasto.getValor());
                    });

                    rendimento.setValor(rendimento.getValor() + previsao.getValor());

                    (new RendimentoDAO()).save(rendimento);
                    (new PrevisaoDAO()).delete(previsao);
                    App.setRoot("TelaPrincipal");
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
    void eliminarRendimento(ActionEvent event) {
        Stage stage = new Stage();
        AnchorPane anchorPane = new AnchorPane();
        ListView<HBox> listView = new ListView<>();
        var rendimentos = user.getRendimentos();
        if (rendimentos.size() == 0) {
            Label label = new Label("Ainda Não Registrou Nenhuma Rendimento");
            label.setAlignment(Pos.CENTER);
            label.setFont(Font.font(20));
            anchorPane.getChildren().addAll(label);
            setCenter(label);
        } else {
            rendimentos.forEach(rendimento -> {
                var hbox = new HBox();
                Button button = new Button("Remover Rendimento");
                button.setStyle("btnLogin");
                hbox.getChildren().add(new Label(rendimento.toString()));
                hbox.getChildren().add(button);
                hbox.setAlignment(Pos.CENTER);
                hbox.setSpacing(10);
                button.setOnAction(t -> {
                    (new RendimentoDAO()).delete(rendimento);
                    App.setRoot("TelaPrincipal");
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
    void novaPrevisao(ActionEvent event) {
        PrevisaoMensalController.setTarefa("");
        if (user.getRendimentos().size() != 0) {
            var scene = new Scene(App.loadFXML("PrevisaoMensal"));
            stage.setScene(scene);
            stage.show();
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("A Família não possui rendimentos");
            alert.setContentText("A Família precisa de pelo menos um rendimento para criar uma previsão");
            alert.show();
        }

    }

    @FXML
    void novoCredito(ActionEvent event) {
        RendimentoController.setTarefa("");
        Scene scene = new Scene(App.loadFXML("Credito"));
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void novoGasto(ActionEvent event) {
        GastoController.setTarefa("");
        if (previsoes.size() != 0) {
            var scene = new Scene(App.loadFXML("Gasto"));
            stage.setScene(scene);
            stage.show();
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("A Família não possui nenhuma Previsão Mensal");
            alert.setContentText("A Família precisa de pelo menos uma previsão para criar um gasto");
            alert.show();
        }

    }

    @FXML
    void novoRendimento(ActionEvent event) {
        RendimentoController.setTarefa("");
        Scene scene = new Scene(App.loadFXML("Rendimento"));
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void sair(ActionEvent event) {
        clear();
        App.getStage().setMaximized(false);
        App.setRoot("Login", 500, 600);
        App.getStage().setResizable(false);
        App.getStage().show();
    }

    @FXML
    void sobre(ActionEvent event) {
        Scene scene = new Scene(App.loadFXML("Sobre"));
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void verBalancoMensal(ActionEvent event) {
        Stage stage = new Stage();
        AnchorPane anchorPane = new AnchorPane();

        ObservableList<BalancoMensal> balanco = FXCollections.observableArrayList();

        Arrays.asList(Month.values()).forEach(mes -> {
            var ganho = rendimentos.stream().filter(rendimento -> rendimento.getData().getMonth().equals(mes))
                    .mapToInt(t -> t.getValor()).sum()
                    + rendimentos.stream().filter(rendimento -> rendimento.getData().getMonth().equals(mes))
                            .flatMap(t -> t.getPrevisoes().stream()).mapToInt(t -> t.getValor()).sum();

            var gasto = rendimentos.stream().filter(rendimento -> rendimento.getData().getMonth().equals(mes))
                    .flatMap(t -> t.getPrevisoes().stream()).flatMap(t -> t.getGastos().stream())
                    .mapToInt(t -> t.getValor()).sum();

            balanco.add(new BalancoMensal(mes, ganho - gasto));
        });

        TableView<BalancoMensal> tv = new TableView();
        TableColumn<BalancoMensal, Month> tcMes = new TableColumn<>("Mês");
        TableColumn<BalancoMensal, String> tcValor = new TableColumn<>("Valor");

        tcMes.setCellValueFactory(new PropertyValueFactory<>("mes"));
        tcValor.setCellValueFactory(new PropertyValueFactory<>("dinheiro"));
        tv.setItems(balanco);
        tv.getColumns().addAll(tcMes, tcValor);

        anchorPane.getChildren().addAll(tv);
        setCenter(tv);

        Scene scene = new Scene(anchorPane, 500, 600);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void verGastoMensal(ActionEvent event) {
        Stage stage = new Stage();
        AnchorPane anchorPane = new AnchorPane();

        ObservableList<BalancoMensal> balanco = FXCollections.observableArrayList();

        Arrays.asList(Month.values()).forEach(mes -> {
            var gasto = rendimentos.stream().filter(rendimento -> rendimento.getData().getMonth().equals(mes))
                    .flatMap(t -> t.getPrevisoes().stream()).flatMap(t -> t.getGastos().stream())
                    .mapToInt(t -> t.getValor()).sum();

            balanco.add(new BalancoMensal(mes, gasto));
        });

        TableView<BalancoMensal> tv = new TableView();
        TableColumn<BalancoMensal, Month> tcMes = new TableColumn<>("Mês");
        TableColumn<BalancoMensal, String> tcValor = new TableColumn<>("Valor");

        tcMes.setCellValueFactory(new PropertyValueFactory<>("mes"));
        tcValor.setCellValueFactory(new PropertyValueFactory<>("dinheiro"));
        tv.setItems(balanco);
        tv.getColumns().addAll(tcMes, tcValor);

        anchorPane.getChildren().addAll(tv);
        setCenter(tv);

        Scene scene = new Scene(anchorPane, 500, 600);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void verPoupancas(ActionEvent event) {
        Stage stage = new Stage();
        AnchorPane anchorPane = new AnchorPane();

        TableView<Poupanca> tv = new TableView();
        TableColumn<Poupanca, Month> tcMes = new TableColumn<>("Mês");
        TableColumn<Poupanca, String> tcValor = new TableColumn<>("Valor Poupado");
        TableColumn<Poupanca, PrevisaoMensal> tcPrevisao = new TableColumn<>("Previsão");

        tcMes.setCellValueFactory(new PropertyValueFactory<>("mes"));
        tcValor.setCellValueFactory(new PropertyValueFactory<>("dinheiro"));
        tcPrevisao.setCellValueFactory(new PropertyValueFactory<>("previsao"));

        ObservableList<Poupanca> poupancas = FXCollections.observableArrayList();
        poupancas.addAll((new PoupancaDAO()).getAll(user.getFamilia()));

        tv.getColumns().addAll(tcMes, tcValor, tcPrevisao);
        tv.setItems(poupancas);
        anchorPane.getChildren().addAll(tv);
        setCenter(tv);

        Scene scene = new Scene(anchorPane, 500, 600);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void visualizarCredito(ActionEvent event) {
        Stage stage = new Stage();
        AnchorPane anchorPane = new AnchorPane();

        TableView<Credito> tableViewFamilia = new TableView<>();

        TableColumn<Credito, String> tableColumnValorDivida = new TableColumn<>("Valor em divida");
        TableColumn<Credito, String> tableColumnValorConcedido = new TableColumn<>("Valor concedido");

        TableColumn<Credito, LocalDate> tableColumnDataSituacao = new TableColumn<>("Data da situação");
        TableColumn<Credito, LocalDate> tableColumnDataVencimento = new TableColumn<>("Data de vencimento");
        TableColumn<Credito, Pessoa> tableColumnCriador = new TableColumn<>("Criador");

        ObservableList<Credito> ob = FXCollections.observableArrayList();

        ob.addAll((new CreditoDAO()).getAll(user.getFamilia()));

        tableViewFamilia.getColumns().addAll(tableColumnValorDivida, tableColumnValorConcedido, tableColumnDataSituacao,
                tableColumnDataVencimento, tableColumnCriador);

        tableColumnValorDivida.setCellValueFactory(new PropertyValueFactory<>("dinheiroDivida"));
        tableColumnValorConcedido.setCellValueFactory(new PropertyValueFactory<>("dinheiroConcedido"));

        tableColumnDataSituacao.setCellValueFactory(new PropertyValueFactory<>("dataSituacao"));
        tableColumnDataVencimento.setCellValueFactory(new PropertyValueFactory<>("dataVencimento"));
        tableColumnCriador.setCellValueFactory(new PropertyValueFactory<>("pessoa"));

        tableViewFamilia.setItems(ob);
        anchorPane.getChildren().add(tableViewFamilia);
        setCenter(tableViewFamilia);
        Scene scene = new Scene(anchorPane, 600, 500);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void visualizarGastos(ActionEvent event) {
        Stage stage = new Stage();
        AnchorPane anchorPane = new AnchorPane();

        TableView<Gasto> tableViewFamilia = new TableView<>();

        TableColumn<Gasto, String> tableColumnDesignacao = new TableColumn<>("Designacao");
        TableColumn<Gasto, String> tableColumnValor = new TableColumn<>("Valor");
        TableColumn<Gasto, String> tableColumnLocal = new TableColumn<>("Local Do Gasto");

        TableColumn<Gasto, Pessoa> tableColumnCriador = new TableColumn<>("Criador");

        ObservableList<Gasto> ob = FXCollections.observableArrayList();

        ob.addAll((new GastoDAO()).getAll(user.getFamilia()));

        tableViewFamilia.getColumns().addAll(tableColumnDesignacao, tableColumnValor, tableColumnLocal,
                tableColumnCriador);

        tableColumnDesignacao.setCellValueFactory(new PropertyValueFactory<>("designacao"));
        tableColumnValor.setCellValueFactory(new PropertyValueFactory<>("dinheiro"));
        tableColumnLocal.setCellValueFactory(new PropertyValueFactory<>("local"));

        tableColumnCriador.setCellValueFactory(new PropertyValueFactory<>("pessoa"));

        tableViewFamilia.setItems(ob);
        anchorPane.getChildren().add(tableViewFamilia);
        setCenter(tableViewFamilia);
        Scene scene = new Scene(anchorPane, 600, 500);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void visualizarPrevisao(ActionEvent event) {
        Stage stage = new Stage();
        AnchorPane anchorPane = new AnchorPane();

        TableView<PrevisaoMensal> tableViewFamilia = new TableView<>();

        TableColumn<PrevisaoMensal, String> tableColumnDesignacao = new TableColumn<>("Designação");
        TableColumn<PrevisaoMensal, String> tableColumnValor = new TableColumn<>("Valor");
        TableColumn<PrevisaoMensal, String> tableColumnFinalidade = new TableColumn<>("Finalidade");
        TableColumn<PrevisaoMensal, Month> tableColumnMes = new TableColumn<>("Mês");

        ObservableList<PrevisaoMensal> ob = FXCollections.observableArrayList();

        ob.addAll((new PrevisaoDAO()).getAll(user.getFamilia()));

        tableViewFamilia.getColumns().addAll(tableColumnDesignacao, tableColumnValor, tableColumnFinalidade,
                tableColumnMes);

        tableColumnDesignacao.setCellValueFactory(new PropertyValueFactory<>("designacao"));
        tableColumnValor.setCellValueFactory(new PropertyValueFactory<>("dinheiro"));
        tableColumnFinalidade.setCellValueFactory(new PropertyValueFactory<>("finalidade"));
        tableColumnMes.setCellValueFactory(new PropertyValueFactory<>("month"));

        tableViewFamilia.setItems(ob);
        anchorPane.getChildren().add(tableViewFamilia);
        setCenter(tableViewFamilia);
        Scene scene = new Scene(anchorPane, 600, 500);
        stage.setScene(scene);

        stage.show();
    }

    @FXML
    void visualizarRendimentos(ActionEvent event) {
        Stage stage = new Stage();
        AnchorPane anchorPane = new AnchorPane();

        TableView<Rendimento> tableViewFamilia = new TableView<>();

        TableColumn<Rendimento, String> tableColumnValor = new TableColumn<>("Valor");
        TableColumn<Rendimento, String> tableColumnLocal = new TableColumn<>("Origem");
        TableColumn<Rendimento, LocalDate> tableColumnData = new TableColumn<>("Data");
        TableColumn<Rendimento, Pessoa> tableColumnCriador = new TableColumn<>("Criador");

        ObservableList<Rendimento> ob = FXCollections.observableArrayList();

        ob.addAll((new RendimentoDAO()).getAll(user.getFamilia()));

        tableViewFamilia.getColumns().addAll(tableColumnValor, tableColumnLocal, tableColumnData, tableColumnCriador);

        tableColumnValor.setCellValueFactory(new PropertyValueFactory<>("dinheiro"));
        tableColumnLocal.setCellValueFactory(new PropertyValueFactory<>("origem"));
        tableColumnData.setCellValueFactory(new PropertyValueFactory<>("data"));
        tableColumnCriador.setCellValueFactory(new PropertyValueFactory<>("pessoa"));

        tableViewFamilia.setItems(ob);

        anchorPane.getChildren().add(tableViewFamilia);
        setCenter(tableViewFamilia);
        Scene scene = new Scene(anchorPane, 600, 500);
        stage.setScene(scene);
        stage.show();
    }

    public void clear() {

        if (stage.isShowing()) {
            stage.close();
        }

        FamiliaController.setApresentacao("Cadastre A Sua Familia");
        user = null;
        FamiliaController.setTarefa(null);
        PessoaController.setTarefa(null);
        AnimalController.setTarefa(null);
        PessoaController.setTexto("");
        PessoaController.setMembro(null);
        PessoaController.setTarefa(null);
        AnimalController.setTarefa(null);

    }

    private static void setCenter(Node node) {
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
    }

}
