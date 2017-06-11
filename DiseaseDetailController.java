/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pfeproject;

import Utils.GraphicalTools;
import Managers.DiseaseManager;
import com.SY.myQueryFac.UpdateQuery;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
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
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import Managers.DiseaseManager.Disease;
import Managers.FoodManager.Food;
import com.SY.GraphDrawer.Graph;
import com.SY.RDFToGraph.MyDoubleClickHandler;
import com.SY.RDFToGraph.myGraphGenerator;
import javafx.scene.control.ScrollPane;

/**
 * FXML Controller class
 *
 * @author sofiane
 */
public class DiseaseDetailController extends MyScene implements Initializable {
    
    int page = 1;
    int limit = 10;
    int page1 = 1;
    int limit1 = 6;
    
    public class DiseaseAdditional implements MyDoubleClickHandler.Additional
    {

        @Override
        public void doSomething(String s) 
        {
            currentUri = s;
        }

    }
    
    //my vars
    private FXMLLoader fxmlLoader=new FXMLLoader();
    private Pane root=null;
    private Disease disease=DiseaseManager.getSelectedDisease();
    DFields df;
    private JFXButton okayButton;
    @FXML
    private AnchorPane detailsPane;
    @FXML
    private Label diseaseName1;
    @FXML
    private AnchorPane pane11;
    @FXML
    private JFXListView<Food> list1;
    @FXML
    private JFXButton confirmButton1;
    @FXML
    private JFXButton modifyButton1;
    @FXML
    private JFXButton okayButton1;
    @FXML
    private Pane graphPane;
    @FXML
    private JFXButton previousButton;
    @FXML
    private JFXButton nextButton;
    
    String currentUri = null;
    Graph graph = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
       
        diseaseName1.setText(disease.name);
         try {
            root=fxmlLoader.load(MainApp.getInstance().getClass().getResource("FXMLDocument2.fxml").openStream());
        } catch (IOException ex) {
            Logger.getLogger(AddDiseaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
         pane11.getChildren().add(root);
         df = fxmlLoader.getController();
         
         GraphicalTools.updateListView(list1, MainApp.a.getFoodThatCauses(disease,page,limit));
         GraphicalTools.setFoodEvent(list1);
         df.setValues(MainApp.a.getIngredientsNames(disease.getCauses()));
         df.disableEdit();
         modifyButton1.setText("next");
         confirmButton1.setText("previous");
//         LinkedList<String> suggestions =MainApp.a.getIngredientsNames(1);
//         DFields.setSuggestion(suggestions);
        currentUri = disease.uri;
        
        graphPane.getChildren().add(myGraphGenerator.getGraphPane(MainApp.url, disease.uri,page1,limit1,new DiseaseAdditional()));
        graph = myGraphGenerator.lastCreatedGraph;
         
    }    

    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception {
        if(event.getSource()==modifyButton1)
        {
            page++;
            LinkedList<Food> lis = MainApp.a.getFoodThatCauses(disease,page,limit);
            if(lis != null || lis.size()>0)
                GraphicalTools.updateListView(list1,lis);
            else page--;
        }
        else if(event.getSource()==confirmButton1)
        {
            if(page > 1) 
            {
                page--;
                GraphicalTools.updateListView(list1, MainApp.a.getFoodThatCauses(disease,page,limit));
            }
              
        }
        else if(event.getSource()==okayButton1)
        {
            Stage stage=(Stage) okayButton1.getScene().getWindow();
            stage.close();
        }
        if(event.getSource()==nextButton)
        {
            page1++;
            if(!myGraphGenerator.setGraph(MainApp.url,currentUri,page1,limit1,graph))
                page1-- ;
        }
        if(event.getSource()==previousButton)
        {
            if(page1>1)
            {
                page1--;
                myGraphGenerator.setGraph(MainApp.url,currentUri,page1,limit1,graph);
            }
        }
    }
    
    
        @Override
    Scene getScene() {
        FXMLLoader fxmlLoader = new FXMLLoader();
           Pane root = null;
           try {
               root = fxmlLoader.load(
                       MainApp.getInstance().getClass().getResource("DiseaseDetail.fxml").openStream());
           } catch (IOException ex) {

           }
           root.getStylesheets().add(MainApp.getInstance().getClass().getResource("list.css").toExternalForm());
           root.setStyle("-fx-background-image: url('" + MainApp.image + "');");
           Scene scene = new Scene(root);
           setParent(root);
           return scene;
    }
    
}
