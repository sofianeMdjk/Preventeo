/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pfeproject;

import Managers.FoodManager;
import GeneralRDF.Triplet;
import com.SY.myQueryFac.QueryGen;
import com.SY.myQueryFac.UpdateQuery;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import Managers.FoodManager.Food;
import javafx.geometry.Pos;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author sofiane
 */
public class AddFoodController extends MyScene implements Initializable {

    @FXML
    private JFXTextField foodName;
    @FXML
    private JFXComboBox<String> origin;
    @FXML
    private JFXTextField code;
    @FXML
    private AnchorPane pane1;
    @FXML
    private JFXButton addButton;
    private DFields df;
    
    //my vars
    FXMLLoader fxmlLoader=new FXMLLoader();
    Pane root=null;
    String name;
    String foodCode;
    String foodOrigin;
    LinkedList<String> ingredients=new LinkedList<>();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
       origin.getItems().addAll("Animal","Vegetal","other");
        try {
            root=fxmlLoader.load(MainApp.getInstance().getClass().getResource("FXMLDocument2.fxml").openStream());
            df = fxmlLoader.getController();
        } catch (IOException ex) {
            Logger.getLogger(AddDiseaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
       pane1.getChildren().addAll(root);
    }    

    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception 
    {
        name=foodName.getText();
        foodCode=MainApp.removeSpaces(code.getText());
        foodOrigin=origin.getSelectionModel().getSelectedItem();
         ingredients=df.getAllTexts();
        
        
        System.out.println("pfeproject.AddFoodController.handleButtonAction()");
        int error = 0;
        if(foodName.getText().equals(""))
            error = 1;
        if(error == 0)
        {
        String n = foodName.getText();
        Food f = FoodManager.getFoodFromName(n,FoodManager.prefixPro);
        if(origin.getSelectionModel().getSelectedItem() != null)
            f.origin = origin.getSelectionModel().getSelectedItem();
        if(!code.getText().equals(""))
            f.code = code.getText();
        
        LinkedList<String> list = df.getAllTexts();
        FoodManager.addFood(f, list, FoodManager.prefixIng);
        
        //reseting
        foodName.setText("");
        code.setText("");
        origin.getSelectionModel().selectFirst();
        
        df.resetAll();
        
        }
        else
        {
            Notifications notif=  Notifications.create();
            notif.title("Food error");
            notif.text("Please make sure to write the name of food");
            notif.position(Pos.TOP_CENTER);
            notif.darkStyle();
            notif.showWarning();
            return;
        }
        
        
    }

    @Override
    Scene getScene() {
        FXMLLoader fxmlLoader = new FXMLLoader();
           Pane root = null;
           try {
               root = fxmlLoader.load(
                       MainApp.getInstance().getClass().getResource("AddFood.fxml").openStream());
           } catch (IOException ex) {

           }
         
           root.setStyle("-fx-background-image: url('" + MainApp.image + "');");
           Scene scene = new Scene(root);
           setParent(root);
           return scene;
    }

    @FXML
    private void handleComboAction(ActionEvent event) 
    {
    }
    
    
    
}
