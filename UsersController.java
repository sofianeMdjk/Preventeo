/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pfeproject;

import Utils.GraphicalTools;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import Managers.DiseaseManager.Disease;
import Managers.FoodManager.Food;

/**
 * FXML Controller class
 *
 * @author sofiane
 */
public class UsersController extends MyScene implements Initializable {

    @FXML
    private Hyperlink showFood;
    @FXML
    private Hyperlink addFood;
    @FXML
    private Hyperlink showDisease;
    @FXML
    private Hyperlink addDisease;
    @FXML
    private Hyperlink help;
    @FXML
    private Hyperlink admin;
    @FXML
    private AnchorPane pane;

    //my vars 
    private FXMLLoader fxmlLoader=new FXMLLoader();

    private LinkedList<Node> nodes =new LinkedList<Node>();
    @FXML
    private Hyperlink recipe;
    @FXML
    private Hyperlink addRecipe;
    @FXML
    private ImageView logo;
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        logo.setImage(new Image(MainApp._instance.myWin.getClass().getResource(
                "pics/preventeo-logo.png").toExternalForm()));
        nodes.add(logo);
    }    



    @FXML
    private void handleClick(ActionEvent event) {
         for(Node node : nodes)
            pane.getChildren().remove(node);
        
        fxmlLoader=new FXMLLoader();
        
        //changin the hyperlinks place
        int x=(int) showFood.getLayoutX();
        int y =(int) showFood.getLayoutY();
        
        int i=1;
        addFood.setLayoutX(x);
        addFood.setLayoutY(y + 70*i++);
        
        showDisease.setLayoutX(x);
        showDisease.setLayoutY(y + 70*i++);
        
        addDisease.setLayoutX(x);
        addDisease.setLayoutY(y + 70*i++);
        
        recipe.setLayoutX(x);
        recipe.setLayoutY(y + 70*i++);
        
        addRecipe.setLayoutX(x);
        addRecipe.setLayoutY(y + 70*i++);
        
        admin.setLayoutX(x);
        admin.setLayoutY(y + 70*i++);
        
        help.setLayoutX(x);
        help.setLayoutY(y + 70*i++);
        
        
        if(event.getSource()==admin)
        {
            Pane root=new Pane();
            root=GraphicalTools.loadScene(root, "AdminLogin.fxml");
            root.setPrefWidth(800);
            root.setPrefHeight(545);
            root.setLayoutX(200);
            root.setLayoutY(40);
            pane.getChildren().add(root);
            nodes.add(root);
        }
        else if(event.getSource()==addFood)
        {
            Pane root=new Pane();
            root=GraphicalTools.loadScene(root, "AddFood.fxml");
            root.setPrefWidth(752);
            root.setPrefHeight(545);
            root.setLayoutX(315);
            root.setLayoutY(15);
            pane.getChildren().add(root);
            nodes.add(root);
            
           /*ImageView[] images= GraphicalTools.setImages("pics/carrot.png", "pics/Steak.png");
           pane.getChildren().add(images[0]);
            nodes.add(images[0]);
            pane.getChildren().add(images[1]);
            nodes.add(images[0]);*/
            
            
        }
        else if(event.getSource()==showFood)
        {
            new FoodViewer(pane, nodes);
        }
        else if(event.getSource()==addDisease)
        {
          Pane root=new Pane();
            root=GraphicalTools.loadScene(root, "AddDisease.fxml");
            root.setPrefWidth(752);
            root.setPrefHeight(545);
            root.setLayoutX(230);
            root.setLayoutY(15);
            pane.getChildren().add(root);
            nodes.add(root);   
    
        }
        else if(event.getSource()==showDisease)
        {
            JFXListView<Disease> diseaseList=GraphicalTools.generateListOfDiseases();
            diseaseList.setPrefWidth(730);
            diseaseList.setPrefHeight(420);
            diseaseList.setLayoutX(270);
            diseaseList.setLayoutY(50);
            nodes.add(diseaseList);
            pane.getChildren().add(diseaseList); 
        }
        else if(event.getSource()==addRecipe)
        {
            Pane root=new Pane();
            root=GraphicalTools.loadScene(root, "Recipe.fxml");
            root.setPrefWidth(900);
            root.setPrefHeight(545);
            root.setLayoutX(205);
            root.setLayoutY(10);
            pane.getChildren().add(root);
            nodes.add(root); 
        }
        else if(event.getSource()==recipe)
        {
            new RecipeViewer(pane,nodes);
//            
        }
    }
    
    
        @Override
    Scene getScene() 
    {
          FXMLLoader fxmlLoader = new FXMLLoader();
        Pane root = null;
        try {
            root = fxmlLoader.load(
                    MainApp.getInstance().getClass().getResource("Users.fxml").openStream());
        } catch (IOException ex) {
            
        }
        Scene scene = new Scene(root);
        
        //root.setStyle("-fx-background-image: url('" + MainApp.image + "');");
        root.getStylesheets().add(MainApp.getInstance().getClass().getResource("list.css").toExternalForm());
        root.getStylesheets().add(MainApp.getInstance().getClass().getResource("menu.css").toExternalForm());
        setParent(root);
        return scene;
    }
    
}
