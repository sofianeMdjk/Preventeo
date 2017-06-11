/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pfeproject;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author sofiane
 */
public class QueryCenterController extends MyScene implements Initializable {

    @FXML
    private JFXTextArea queryArea;
    @FXML
    private JFXRadioButton queryRadio;
    @FXML
    private JFXButton queryButton;
    @FXML
    private JFXRadioButton updateRadio;

    //my vars
    boolean queryType=true;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        queryRadio.setSelected(true);
    }    

    @FXML
    private void handleRadioAction(ActionEvent event) {
        if(event.getSource()==queryRadio)
        {
         queryRadio.setSelected(true);
         updateRadio.setSelected(false);
         queryType=true;
        }
        else if(event.getSource()==updateRadio)
        {
            updateRadio.setSelected(true);
            queryRadio.setSelected(false);
            queryType=false;
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        
    }

    @Override
    Scene getScene() {
          FXMLLoader fxmlLoader = new FXMLLoader();
           Pane root = null;
           try {
               root = fxmlLoader.load(
                       MainApp.getInstance().getClass().getResource("QueryCenter.fxml").openStream());
           } catch (IOException ex) {

           }
           root.getStylesheets().add(MainApp.getInstance().getClass().getResource("list.css").toExternalForm());
           root.setStyle("-fx-background-image: url('" + MainApp.image + "');");
           Scene scene = new Scene(root);
           setParent(root);
           return scene;
    }
    
}
