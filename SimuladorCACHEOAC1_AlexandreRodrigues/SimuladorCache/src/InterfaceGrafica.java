import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InterfaceGrafica extends Application {

    private Mapeamentos mapeamento;
    private String op = "aleatorio";

    @Override
    public void start(Stage primaryStage) throws Exception {

        int configs[] = new int[4];
        int i = 0, tamPrincipal, palavra, linha, tamCache;

        String[] configText = null;
        List<String> config = FileManager
                .stringReader("D:/SimuladorCacheOAC/SimuladorCache_AlexandreRodrigues/data/config.txt");

        for (i = 0; i < config.size(); i++) {

            configText = config.get(i).split(" ");
            if (configText[3].equals("GB")) {
                configs[i] = Integer.parseInt(configText[2]) * 1024 * 1024 * 8;
            } else if (configText[3].equals("KB;")) {
                configs[i] = Integer.parseInt(configText[2]) * 1024 * 8;
            } else if (configText[3].equals("B;")) {
                configs[i] = Integer.parseInt(configText[2]) * 8;
            } else if (configText[3].equals("pal;")) {
                configs[i] = Integer.parseInt(configText[2]);
            }
        }

        tamPrincipal = configs[0];
        palavra = configs[1];
        tamCache = configs[2];
        linha = configs[2] / (configs[1] * configs[3]);

        mapeamento = new Mapeamentos(tamPrincipal, tamCache, palavra, linha);

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();

        // criação dos botões
        Button btnDireto = new Button("Mapeamento Direto");
        Button btnAssoc = new Button("Mapeamento Associativo");
        Button btnAssocConj = new Button("Mapeamento Associativo Conjunto");

        // criação da área de texto
        TextArea txtResult = new TextArea();
        txtResult.setEditable(false);
        txtResult.setPrefHeight(100);
        txtResult.setPrefWidth(100);

        // adiciona os botões e a área de texto ao painel principal
        VBox vbox = new VBox(10, btnDireto, btnAssoc, btnAssocConj, txtResult);
        vbox.setAlignment(Pos.CENTER);
        root.setCenter(vbox);

        // define a ação dos botões
        btnDireto.setOnAction(e -> {
            Mapeamentos mapeamento = new Mapeamentos(tamPrincipal, tamCache, palavra, linha);
            mapeamento.Direto();
            txtResult.setText(mapeamento.getInfo());
            // txtResult.setText(mapeamento.getMissCache());
        });

        btnAssoc.setOnAction(e -> {
            // Cria um grupo de alternância para os botões radio
            Mapeamentos mapeamento = new Mapeamentos(tamPrincipal, tamCache, palavra, linha);

    // Cria o grupo de botões de toggle
    ToggleGroup group = new ToggleGroup();

    // Cria os botões de toggle e adiciona-os ao grupo e ao contêiner HBox
    HBox toggleContainer = new HBox();
    toggleContainer.setAlignment(Pos.CENTER);
    toggleContainer.setSpacing(10);
    RadioButton rbAleatorio = new RadioButton("Aleatório");
    rbAleatorio.setToggleGroup(group);
    toggleContainer.getChildren().add(rbAleatorio);
    RadioButton rbLRU = new RadioButton("LRU");
    rbLRU.setToggleGroup(group);
    toggleContainer.getChildren().add(rbLRU);
    RadioButton rbFIFO = new RadioButton("FIFO");
    rbFIFO.setToggleGroup(group);
    toggleContainer.getChildren().add(rbFIFO);
    RadioButton rbLFU = new RadioButton("LFU");
    rbLFU.setToggleGroup(group);
    toggleContainer.getChildren().add(rbLFU);

    // Adiciona o contêiner HBox à interface
    VBox vbox2 = new VBox(10, btnDireto, btnAssoc, btnAssocConj, toggleContainer, txtResult);
    vbox2.setAlignment(Pos.CENTER);
    root.setCenter(vbox2);

    group.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
        if (newToggle == rbAleatorio) {
            mapeamento.Associativo("aleatorio");
            txtResult.setText(mapeamento.getInfo());
        } else if (newToggle == rbFIFO) {
            mapeamento.Associativo("FIFO");
            txtResult.setText(mapeamento.getInfo());
        } else if (newToggle == rbLRU) {
            mapeamento.Associativo("LRU");
            txtResult.setText(mapeamento.getInfo());
        } else if (newToggle == rbLFU) {
            mapeamento.Associativo("LFU");
            txtResult.setText(mapeamento.getInfo());
        }

    });
        });

        btnAssocConj.setOnAction(e -> {
               // Cria um grupo de alternância para os botões radio
               Mapeamentos mapeamento = new Mapeamentos(tamPrincipal, tamCache, palavra, linha);

               // Cria o grupo de botões de toggle
               ToggleGroup group = new ToggleGroup();
           
               // Cria os botões de toggle e adiciona-os ao grupo e ao contêiner HBox
               HBox toggleContainer = new HBox();
               toggleContainer.setAlignment(Pos.CENTER);
               toggleContainer.setSpacing(10);
               RadioButton rbAleatorio = new RadioButton("Aleatório");
               rbAleatorio.setToggleGroup(group);
               toggleContainer.getChildren().add(rbAleatorio);
               RadioButton rbLRU = new RadioButton("LRU");
               rbLRU.setToggleGroup(group);
               toggleContainer.getChildren().add(rbLRU);
               RadioButton rbFIFO = new RadioButton("FIFO");
               rbFIFO.setToggleGroup(group);
               toggleContainer.getChildren().add(rbFIFO);
               RadioButton rbLFU = new RadioButton("LFU");
               rbLFU.setToggleGroup(group);
               toggleContainer.getChildren().add(rbLFU);
           
               // Adiciona o contêiner HBox à interface
               VBox vbox2 = new VBox(10, btnDireto, btnAssoc, btnAssocConj, toggleContainer, txtResult);
               vbox2.setAlignment(Pos.CENTER);
               root.setCenter(vbox2);
           
               group.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                   if (newToggle == rbAleatorio) {
                       mapeamento.AssociativoConjunto("aleatorio", tamCache);
                       txtResult.setText(mapeamento.getInfo());
                   } else if (newToggle == rbFIFO) {
                       mapeamento.AssociativoConjunto("FIFO", tamCache);
                       txtResult.setText(mapeamento.getInfo());
                   } else if (newToggle == rbLRU) {
                       mapeamento.AssociativoConjunto("LRU", tamCache);
                       txtResult.setText(mapeamento.getInfo());
                   } else if (newToggle == rbLFU) {
                       mapeamento.AssociativoConjunto("LFU", tamCache);
                       txtResult.setText(mapeamento.getInfo());
                   }
               
        });
    });


        Scene scene = new Scene(root, 500, 400);

        primaryStage.setTitle("Simulador de Cache");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
