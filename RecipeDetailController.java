/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pfeproject;

import Managers.Recipe;
import com.SY.GraphDrawer.CircularLayout;
import com.SY.GraphDrawer.Graph;
import com.SY.GraphDrawer.Layout;
import com.SY.RDFToGraph.MyDoubleClickHandler.Additional;
import com.SY.RDFToGraph.RDFToGraph;
import com.SY.RDFToGraph.myGraphGenerator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author soso
 */
public class RecipeDetailController implements Initializable {

    

    public class RecipeAdditional implements Additional
    {

        @Override
        public void doSomething(String s) 
        {
            currentUri = s;
        }

    }
    
    private class Ingredient
    {
        String name;
        int quantity;
        String unit;
            Ingredient(String n,int q, String u)
            {
                name=n;
                quantity=q;
                unit=u;
            }
            
    }
    Recipe elem=Recipe.selectedRecipe;
    @FXML
    private AnchorPane detailPane;
    @FXML
    private Label recipeName1;
    private JFXListView<String> listOfIngredients1;
    @FXML
    private JFXButton okayButton1;
    @FXML
    private AnchorPane graphPane;
    @FXML
    private JFXButton nextButton;
    @FXML
    private JFXButton previousButton;
    @FXML
    private TableView<Ingredient> table;
    
    
    int page = 1;
    int limit = 6;
    String currentUri = null;
    Graph graph = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        recipeName1.setText(elem.getName());
        initTable();
        /*LinkedList<RecipeController.RecipeIngredient> ingredients=elem.getIngredients();
        String display="";
        for(int i=0;i<ingredients.size();i++)
        {
            table.getColumns().add(ingredients.get(i).name,ingredients.get(i).quantity,ingredients.get(i).unit);
            display="Ingredient===>"+ingredients.get(i).name+"\tQuantity===>"+ingredients.get(i).quantity+"\tUnit===>"+ingredients.get(i).unit;
            listOfIngredients1.getItems().add(display);
        }*/
        currentUri = elem.getUri();
        graphPane.getChildren().add(myGraphGenerator.getGraphPane(MainApp.url, elem.getUri(),page,limit,new RecipeAdditional()));
        graph = myGraphGenerator.lastCreatedGraph;
       
    }    

    @FXML
    private void handleButtonsAction(ActionEvent event) {
        if(event.getSource()==okayButton1)
        {
            Stage stage = (Stage) okayButton1.getScene().getWindow();
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
    
    ObservableList<Ingredient> getIngredients()
    {
        ObservableList<Ingredient> ingredients = 
                                               FXCollections.observableArrayList();
        
        LinkedList<RecipeController.RecipeIngredient> ingredientList=elem.getIngredients();
        for(int i=0;i<ingredientList.size();i++)
        {
            ingredients.add(
                    new Ingredient(ingredientList.get(i).name, 
                                   ingredientList.get(i).quantity,
                                   ingredientList.get(i).unit));
        }
     return ingredients;          
    }
    
    void initTable()
    {
        //Name column
        TableColumn<Ingredient,String> nameCol=
                new TableColumn<>("Ingredient");
        nameCol.setMinWidth(200);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        //quantity column
        TableColumn<Ingredient,Integer> qCol=
                new TableColumn<>("Quantity");
        qCol.setMinWidth(100);
        qCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        //unit column
        
         TableColumn<Ingredient,String> unitCol=
                new TableColumn<>("Unit");
        unitCol.setMinWidth(150);
        unitCol.setCellValueFactory(new PropertyValueFactory<>("unit"));
        
        table.setItems(getIngredients());
        table.getColumns().addAll(nameCol,qCol,unitCol);
    }
    
}
