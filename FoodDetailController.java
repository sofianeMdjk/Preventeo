/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pfeproject;

import Managers.FoodManager;
import GeneralRDF.Triplet;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import Managers.FoodManager.Food;
import com.SY.GraphDrawer.Graph;
import com.SY.RDFToGraph.MyDoubleClickHandler;
import com.SY.RDFToGraph.myGraphGenerator;

/**
 * FXML Controller class
 *
 * @author sofiane
 */
public class FoodDetailController extends MyScene implements Initializable {

    int page = 1;
    int limit = 6;
    @FXML
    private JFXButton previousButton;
    @FXML
    private JFXButton nextButton;
    
    public class FoodAdditional implements MyDoubleClickHandler.Additional
    {

        @Override
        public void doSomething(String s) 
        {
            currentUri = s;
        }

    }
    @FXML
    private ImageView originImage;
    @FXML
    private Label foodName;
    private Food food=FoodManager.selectedFood;
    @FXML
    private JFXComboBox<String> choiceBox;
    @FXML
    private JFXComboBox<String> diseasesBox;
    @FXML
    private AnchorPane pane1;
    @FXML
    private JFXTextArea text;
    @FXML
    private JFXButton okayButton;
    @FXML
    private AnchorPane detailPane;
    @FXML
    private AnchorPane graphPane;
   
   String currentUri = null;
    Graph graph = null;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        String fName = food.toString();
    if(fName.compareTo("")!=0)
    {
        
     foodName.setText(fName);
     FoodManager a=new FoodManager(MainApp.url);
     
      LinkedList<String> ingredients=a.getIngredientsOfAfood(food);
      choiceBox.getItems().addAll(ingredients);
        LinkedList<Triplet> infos = a.getFoodDetails(food);
        for(int i=0;i<infos.size();i++)
        {
            text.appendText(infos.get(i).getPredicat()+" : "+infos.get(i).getValue()+"\n\n");   
        }
        
     diseasesBox.getItems().addAll(food.getProvokedDiseases());
     int origin= a.getFoodOrigin(food);
     switch(origin)
     {
         case 0 :
         {
             originImage.setImage(new Image(getClass().getResource("pics/other.png").toExternalForm()));
         }break;
         case 1 :
         {
             originImage.setImage(new Image(getClass().getResource("pics/animal.png").toExternalForm()));
         }break;
         case 2 :
         {
             originImage.setImage(new Image(getClass().getResource("pics/plant.png").toExternalForm()));
         }break;
     }
    }   
    currentUri = food.uri;
        
        graphPane.getChildren().add(myGraphGenerator.getGraphPane(MainApp.url, food.uri,page,limit,new FoodAdditional()));
        graph = myGraphGenerator.lastCreatedGraph;
    }
    
    
    
    
    @Override
    Scene getScene() {
      FXMLLoader fxmlLoader = new FXMLLoader();
        Pane root = null;
        try {
            root = fxmlLoader.load(
                    MainApp.getInstance().getClass().getResource("FoodDetail.fxml").openStream());
        } catch (IOException ex) {
            
        }
        Scene scene = new Scene(root);
        root.setStyle("-fx-background-image: url('" + MainApp.image + "');");
        setParent(root);
        return scene;
    }
    
    public void setFoodName(Food name)
    {
        this.food=name;
    }

    @FXML
    private void handleButton(ActionEvent event) {
       if(event.getSource()==okayButton)
        {
            Stage stage=(Stage) okayButton.getScene().getWindow();
            stage.close();
        }
        if(event.getSource()==nextButton)
        {
            page++;
            if(!myGraphGenerator.setGraph(MainApp.url,currentUri,page,limit,graph))
                page -- ;
        }
        if(event.getSource()==previousButton)
        {
            if(page>1)
            {
                page --;
                myGraphGenerator.setGraph(MainApp.url,currentUri,page,limit,graph);
            }
        }
        
        
    }
    
}
