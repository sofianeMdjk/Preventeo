/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pfeproject;

import Utils.GraphicalTools;
import Managers.Notification;
import Managers.DataBaseAccess;
import Managers.FoodManager;
import Managers.Admin;
import Managers.DiseaseManager.Disease;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import java.util.LinkedList;
import java.util.ResourceBundle;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import javafx.scene.Scene;

import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import javafx.scene.control.Label;


/**
 * FXML Controller class
 *
 * @author sofiane
 */
public class AdminUIController extends MyScene implements Initializable {

    JFXListView <Notification> notifListView=new JFXListView<>();
    JFXTextArea notifDetails=new JFXTextArea();
    LinkedList<Node> nodes=new LinkedList<>();
    
    @FXML
    private AnchorPane pane;

    @FXML
    private MenuItem exit;
    @FXML
    private MenuItem addFood;
    @FXML
    private MenuItem viewFood;
    @FXML
    private MenuItem addDisease;
    @FXML
    private MenuItem viewDisease;
    @FXML
    private ImageView notifButton;
    @FXML
    private MenuItem recipe;
    @FXML
    private MenuItem addRecipe;
    //my vars
    FoodManager a= MainApp.a;
    FXMLLoader fxmlLoader=new FXMLLoader();
    JFXButton confirm=null;
    
    public static Notification lastClickedNotif=null;
    @FXML
    private ImageView logo;
    
    static AdminUIController _instance = null;
    @FXML
    private ImageView logout;
    @FXML
    private Label numberOfNotifs;
    
   DataBaseAccess dba= null;
    LinkedList<Notification> list = null;
   
    public static int selected=0;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    initShortcuts();
    
    //preparing the notifications
     notifButton.setImage(new Image(MainApp._instance.getClass().getResource("pics/notif.png").toExternalForm()));
     notifListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent event) {
              System.out.println("here");
         notifDetails.appendText("\t\t\t Add disease\n\n");
         notifDetails.appendText("Causes :\n Sucre\nSel");
         }
     });
     
        try {
            dba = new DataBaseAccess("jdbc:mysql://localhost:3306/informations", "root", "Resssay95");
            list = dba.getNotifications();
        } catch (SQLException ex) {
            Logger.getLogger(AdminUIController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AdminUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
            
        int size=list.size();
        if(size>0)
            numberOfNotifs.setText(""+size);
        
        
     logo.setImage(new Image(MainApp._instance.myWin.getClass().getResource(
                "pics/preventeo-logo.png").toExternalForm()));
        nodes.add(logo);
        _instance = this;
    }    
    
    

    @Override
    Scene getScene() {
         FXMLLoader fxmlLoader = new FXMLLoader();
        Pane root = null;
        try {
            root = fxmlLoader.load(
                    MainApp.getInstance().getClass().getResource("AdminUI.fxml").openStream());
        } catch (IOException ex) {
            
        }
        Scene scene = new Scene(root);
        
        //root.setStyle("-fx-background-image: url('" + MainApp.image + "');");
        root.getStylesheets().add(MainApp.getInstance().getClass().getResource("list.css").toExternalForm());
        root.getStylesheets().add(MainApp.getInstance().getClass().getResource("menu.css").toExternalForm());
        setParent(root);
        return scene;
    }



    @FXML
    private void itemsAction(ActionEvent event) {
        for(Node node : nodes)
            pane.getChildren().remove(node);
        
        fxmlLoader=new FXMLLoader();
//        if(event.getSource()==changeInfos)
//        {
//              JFXTextField username= new JFXTextField();
//              username.setPrefWidth(300);
//              username.setPrefHeight(20);
//              username.setLayoutX(450);
//              username.setLayoutY(250);
//              username.setPromptText("Username");
//              username.setLabelFloat(true);
//              nodes.add(username);
//              pane.getChildren().add(username);
//              
//              JFXTextField email= new JFXTextField();
//              email.setPrefWidth(300);
//              email.setPrefHeight(20);
//              email.setLayoutX(450);
//              email.setLayoutY(300);
//              email.setPromptText("Email");
//              email.setLabelFloat(true);
//              nodes.add(email);
//              pane.getChildren().add(email);
//              
//              JFXTextField password= new JFXTextField();
//              password.setPrefWidth(300);
//              password.setPrefHeight(20);
//              password.setLayoutX(450);
//              password.setLayoutY(350);
//              password.setPromptText("Password");
//              password.setLabelFloat(true);
//              nodes.add(password);
//              pane.getChildren().add(password);
//              
//              JFXTextField passwordConfirmation= new JFXTextField();
//              passwordConfirmation.setPrefWidth(300);
//              passwordConfirmation.setPrefHeight(20);
//              passwordConfirmation.setLayoutX(450);
//              passwordConfirmation.setLayoutY(400);
//              passwordConfirmation.setPromptText("Confirm password");
//              passwordConfirmation.setLabelFloat(true);
//              nodes.add(passwordConfirmation);
//              pane.getChildren().add(passwordConfirmation);
//              
//              confirm=new JFXButton("confirm");
//              confirm.setPrefWidth(100);
//              confirm.setPrefHeight(50);
//              confirm.setLayoutX(575);
//              confirm.setLayoutY(450);
//              confirm.setStyle("-fx-background-color : #4059a9");
//              nodes.add(confirm);
//              pane.getChildren().add(confirm);
//        }
        if(event.getSource()==viewFood)
        {
           
            new FoodViewer(pane, nodes);
        }
        if(event.getSource()==addDisease)
        {
           //we skip to the add disease window but as an admin, so we don't need to send a notif
           Pane root=new Pane();
            root.setPrefWidth(742);
            root.setPrefHeight(545);
            
            try {
            root=fxmlLoader.load(MainApp.getInstance().getClass().getResource("AddDisease.fxml").openStream());
             } catch (IOException ex) {
            Logger.getLogger(AddDiseaseController.class.getName()).log(Level.SEVERE, null, ex);
            }
             root.setLayoutX(150); 
             root.setLayoutY(15);
             nodes.add(root);
             pane.getChildren().add(root);
        }
        if(event.getSource()==viewDisease)
        {
   
            JFXListView<Disease> diseaseList=GraphicalTools.generateListOfDiseases();
            diseaseList.setPrefWidth(730);
            diseaseList.setPrefHeight(420);
            diseaseList.setLayoutX(240);
            diseaseList.setLayoutY(95);
            nodes.add(diseaseList);
            pane.getChildren().add(diseaseList);
        }
        if(event.getSource()==addFood)
        {
             Pane root=new Pane();
             root.setPrefWidth(742);
             root.setPrefHeight(545);
             root =GraphicalTools.loadScene(root, "AddFood.fxml");
             root.setLayoutX(215); 
             root.setLayoutY(10);
             nodes.add(root);
             pane.getChildren().add(root);
        }
        else if(event.getSource()==recipe)
        {
            new RecipeViewer(pane, nodes);
        }
        else if(event.getSource()==addRecipe)
        {
            Pane root=new Pane();
            root=GraphicalTools.loadScene(root, "Recipe.fxml");
            root.setPrefWidth(900);
            root.setPrefHeight(545);
            root.setLayoutX(135);
            root.setLayoutY(25);
            pane.getChildren().add(root);
            nodes.add(root); 
        }
        else if(event.getSource()==exit)
        {
            Admin.isAdmin=false;
            MainApp._instance.myWin.popScene();
        }
        
    }
    public void refreshNotif() throws Exception
    {
        for(Node node : nodes)
            pane.getChildren().remove(node);
        
        try {
            dba = new DataBaseAccess("jdbc:mysql://localhost:3306/informations", "root", "Resssay95");
            list = dba.getNotifications();
        } catch (SQLException ex) {
            Logger.getLogger(AdminUIController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AdminUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
            notifListView= new JFXListView<>();
            GraphicalTools.setNotifEvent(notifListView);
            notifListView.setPrefWidth(750);
            notifListView.setPrefHeight(350);
            notifListView.setLayoutX(200);
            notifListView.setLayoutY(100);
            
            
            
            for(int i=0;i<list.size();i++)
            {
               notifListView.getItems().add(list.get(i));
            }
            if(list.size()>0)
                numberOfNotifs.setText(""+list.size());
            else
                numberOfNotifs.setText("");
                
            nodes.add(notifListView);
            pane.getChildren().add(notifListView);
    }
    @FXML
    private void handleClick(MouseEvent event) throws Exception {
        
        if(event.getSource()==notifButton)
        {
              refreshNotif();
        }
    }

  public void initShortcuts()
    {
        viewFood.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN));
        addDisease.setAccelerator(new KeyCodeCombination
        (KeyCode.D, KeyCombination.CONTROL_DOWN,KeyCombination.SHIFT_DOWN));   
        viewDisease.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN));
        addFood.setAccelerator(new KeyCodeCombination
        (KeyCode.F, KeyCombination.CONTROL_DOWN,KeyCombination.SHIFT_DOWN));
        
        recipe.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN));
        addRecipe.setAccelerator(new KeyCodeCombination(
                KeyCode.R, KeyCombination.CONTROL_DOWN,KeyCombination.SHIFT_DOWN));
    }
  
  public void handleButtonAction(ActionEvent event)
  {
      if(event.getSource()==confirm)
      {
          System.out.println("action");
      }
  }
    
    
}
