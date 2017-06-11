/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pfeproject;

import Utils.GraphicalTools;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author soso
 */
public class TabsController extends MyScene implements Initializable {

    @FXML
    private Tab tab1;
    @FXML
    private Tab tab2;
    @FXML
    private Pane pane1;
    @FXML
    private Pane pane2;

    private JFXButton previous;
    @FXML
    private JFXButton next;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        int select =AdminUIController.selected;
        Pane pane = new Pane();
    }    

    @Override
    Scene getScene() {
         FXMLLoader fxmlLoader = new FXMLLoader();
        Pane root = null;
        try {
            root = fxmlLoader.load(
                    MainApp.getInstance().getClass().getResource("Tabs.fxml").openStream());
        } catch (IOException ex) {
            
        }
        Scene scene = new Scene(root);
        
        root.setStyle("-fx-background-image: url('" + MainApp.image + "');");
        root.getStylesheets().add(MainApp.getInstance().getClass().getResource("list.css").toExternalForm());
        root.getStylesheets().add(MainApp.getInstance().getClass().getResource("menu.css").toExternalForm());
        setParent(root);
        return scene;
    }
    
}
