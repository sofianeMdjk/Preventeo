/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pfeproject;

import Managers.Admin;
import Managers.FoodManager;
import Managers.DiseaseManager;
import com.SY.myQueryFac.UpdateQuery;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.jena.iri.impl.Main;
import org.controlsfx.control.Notifications;
import Managers.DiseaseManager.Disease;
import Managers.FoodManager.Food;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * FXML Controller class
 *
 * @author sofiane
 */
public class AddDiseaseController extends MyScene implements Initializable {

    @FXML
    private AnchorPane pane1;
    DFields controller;
    @FXML
    private JFXTextField NameField;
    @FXML
    private JFXButton addDisease;
//    @FXML
//    private JFXComboBox<String> comboBox;
//   @FXML
//    private JFXListView<Food> listView;
    private LinkedList<Disease> keys;
     
    FXMLLoader fxmlLoader=new FXMLLoader();
        Pane root=null;
        public static String foodName;
    
    FoodManager a=new FoodManager(MainApp.url);
    LinkedList<String> causes=new LinkedList<String>();
    LinkedList<String> list=new LinkedList<>();
    @FXML
    private JFXRadioButton allergy;
    @FXML
    private ToggleGroup grp;
    @FXML
    private JFXRadioButton disease;
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        try {
            root=fxmlLoader.load(MainApp.getInstance().getClass().getResource("FXMLDocument2.fxml").openStream());
        } catch (IOException ex) {
            Logger.getLogger(AddDiseaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
       pane1.getChildren().addAll(root);
       controller=fxmlLoader.getController();
        list=controller.getAllTexts();
        
        
        NameField.setFont(Font.font("System",FontWeight.BOLD,15));
    }   

    @Override
    Scene getScene() {
        FXMLLoader fxmlLoader = new FXMLLoader();
           Pane root = null;
           try {
               root = fxmlLoader.load(
                       MainApp.getInstance().getClass().getResource("AddDisease.fxml").openStream());
           } catch (IOException ex) {

           }
           root.getStylesheets().add(MainApp.getInstance().getClass().getResource("list.css").toExternalForm());
           root.setStyle("-fx-background-image: url('" + MainApp.image + "');");
           Scene scene = new Scene(root);
           setParent(root);
           return scene;
    }

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException, Exception {
        if(event.getSource()==addDisease)
        {
            
            String name=NameField.getText();
            String type = "";
                if(grp.getSelectedToggle() == disease)
                    type = "Disease";
                else if (grp.getSelectedToggle() == allergy)
                    type = "Allergy";
                else
                {
                    Notifications notif=  Notifications.create();
                    notif.title("Disease error");
                    notif.text(" Please make sure to select type of illness");
                    notif.position(Pos.TOP_CENTER);
                    notif.darkStyle();
                    notif.showWarning();
                    return;
                }
                LinkedList<String> list= controller.getAllTexts();
                if(list.isEmpty())
                {
                    Notifications notif=  Notifications.create();
                    notif.title("Disease error");
                    notif.text(" Please make sure to select at least one cause");
                    notif.position(Pos.TOP_CENTER);
                    notif.darkStyle();
                    notif.showWarning();
                    return;
                }
            if(name.compareTo("")==0 || DiseaseManager.alreadyExists(name))
            {
                NameField.setText("");
                  Notifications notif=  Notifications.create();
                  notif.title("Disease error");
                if(name.compareTo("")!=0)
                {
                    notif.text("This disease already exists");          
                }
                else
                  notif.text(" Please make sure to write the name of the disease");
                  notif.position(Pos.TOP_CENTER);
                  notif.darkStyle();
                  notif.showWarning();
            }
            else
            {
                
                
                
//                LinkedList<String> ingredients= new LinkedList<>();

//the disease has been succesfully added
                LinkedList<Food> fds = new LinkedList<>();
                for(int i=0;i<list.size();i++)
                {
                    fds.addAll(a.getIngredientsThatContainsOtherIngredients(list.get(i)));
                }
                
                
                DiseaseManager.addDisease(name,type,list, fds,UpdateQuery.updateUrl);
               
                Notifications notif= Notifications.create();
                if(Admin.isAdmin)
                notif.text("Disease succesfuly added");
                else
                notif.text("Notification sent to Admin");
                notif.position(Pos.TOP_CENTER);
                notif.darkStyle();
                notif.showInformation();
                
                //reset the fields
                NameField.setText("");
            }
            controller=null;
            pane1.getChildren().remove(root);
            root=null;
            fxmlLoader=new FXMLLoader();
            root=fxmlLoader.load(MainApp._instance.myWin.getClass()
                    .getResource("FXMLDocument2.fxml").openStream());
            pane1.getChildren().add(root);
            controller=fxmlLoader.getController();
            
        }
       
    }


    
    
}
