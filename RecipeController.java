/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pfeproject;

import Utils.Security;
import Managers.DataBaseAccess;
import Managers.FoodManager;
import Managers.Admin;
import GeneralRDF.Triplet;
import com.SY.myQueryFac.QueryGen;
import com.SY.myQueryFac.UpdateQuery;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.controlsfx.control.Notifications;


public class RecipeController extends MyScene implements Initializable
{
    @FXML
    Button showB;
   
    @FXML
    AnchorPane pane1;
        
    
    @FXML
    private JFXTextField recipeName;
   
    Pane root = null;
    FXMLLoader fxmlLoader = null;
    DFieldsBisController controller;
    LinkedList<String[]> list=null;
    public static String prefixR = "http://www.semanticweb.org/admin/rdf-base/Recipes/";
    public static class RecipeIngredient
    {
        String uri;
        String unit;
        int quantity = -1;
        String name;

        public RecipeIngredient(String uri, String unit, int quantity,String name)
        {
            this.uri = uri;
            this.unit = unit;
            this.quantity = quantity;
            this.name = name;
        }
        
    }
    
     @Override
    public void initialize(URL url, ResourceBundle rb) {
         fxmlLoader = new FXMLLoader();
         root = null;
        try 
        {
            root = fxmlLoader.load(MainApp.getInstance().getClass().getResource("DFieldsBis.fxml").openStream());
        } catch (IOException ex) 
        {
            Logger.getLogger(RecipeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        pane1.getChildren().add(root);
        controller = fxmlLoader.getController();
        
    }
    
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception 
    {
        list= controller.getAllTexts();
        Notifications notif=  Notifications.create();
             notif.title("Recipe error");
             notif.position(Pos.TOP_CENTER);
             notif.darkStyle();
        if(recipeName.getText().compareTo("")==0 && list.size()==0)
        {
             notif.text("Name and ingredients are missing");
             notif.showWarning();
        }
        else if(recipeName.getText().compareTo("")==0)
        {
             notif.text("Write the name of the recipe");
             notif.showError();
        }
        else if(list.size()==0)
        {
             notif.text("You need at least one ingredient to make a recipe");
             notif.showError();
        }
        else 
        {
            String s = testSecondField(list);
        if(!s.equals(""))
        {
             notif.text(s);
             notif.showError();
        }
        else
        {
            LinkedList<RecipeIngredient> ri = new LinkedList<>();
            for (String[] strings : list) 
            {
                int q = -1;
                if(Security.isInteger(strings[1]))
                q =  Integer.parseInt(strings[1]);
             ri.add(new RecipeIngredient(FoodManager.getFoodFromName(strings[0], FoodManager.prefixIng).toString(), strings[2], q,strings[0]));
            }
            addRecipe(recipeName.getText(), ri);
            System.out.println(generateIinfos());  
            
         //reseting infos   
        recipeName.setText(""); 
        controller.resetAll();  
        
            
        }
        }
       
    }
    
    
    public void addRecipe(String name,LinkedList<RecipeIngredient> list) throws Exception
    {
        String uri = "<"+prefixR+MainApp.formatString(name)+">";
        UpdateQuery uq = new UpdateQuery(QueryGen.prefixes);
        uq.addTriplet(new Triplet(uri, "rdf:type", "food:Recipe"));
        uq.addTriplet(new Triplet(uri, FoodManager.namePredicate, "\""+name+"\""));
        int i=0;
        for (RecipeIngredient ing : list) 
        {
          uq.addTriplet(new Triplet(uri, "food:isMadeOf", "_:b"+i));
          uq.addTriplet(new Triplet("_:b"+i, "food:food", "<"+ing.uri+">"));
          uq.addTriplet(new Triplet("<"+ing.uri+">", FoodManager.namePredicate , "\""+ing.name+"\"" ));
          if(ing.quantity != -1)
          uq.addTriplet(new Triplet("_:b"+i, "food:quantity", "\""+ing.quantity+"\""));
          if(ing.unit != null && !ing.unit.equals(""))
          uq.addTriplet(new Triplet("_:b"+i, "food:unit", "\""+ing.unit+"\""));
          i++;
        }
//        uq.applyQuery(url, uq.getQuery());
        if(!Admin.isAdmin)
        {
        DataBaseAccess dba = new DataBaseAccess("jdbc:mysql://localhost:3306/informations", "root", "Resssay95");
        String str = generateIinfos();
        dba.addNotification(str, uq.getQuery());
        }
        else
        uq.applyQuery(uq.getQuery());
    }
    
       

    @Override
    Scene getScene() 
    {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane root = null;
        try {
            root = fxmlLoader.load(MainApp.getInstance().getClass().getResource("Recipe.fxml").openStream());
        } catch (IOException ex) {
            Logger.getLogger(RecipeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scene scene = new Scene(root);
        setParent(root);
        return scene;
    }
    
    String generateIinfos()
    {
        String info="2;"+recipeName.getText()+";";
        String[] tab=null;
        int i=0;
        for(i=0;i<list.size()-1;i++)
        {
            tab=list.get(i);
            info+=tab[0]+"-"+tab[1]+"-"+tab[2]+",";
        }
          tab=list.get(i);
          info+=tab[0]+"-"+tab[1]+"-"+tab[2]+";";
          
          
     return info;
    }
    
    
    private String testSecondField(LinkedList<String[]> list)
    {
        String test="";
        int i=0;
        while(test.equals("") && i<list.size())
        {
//            if(!list.get(i)[0].equals(""))
            if(!list.get(i)[1].equals(""))
            {
            if(!Security.isInteger(list.get(i)[1]))
                test="Make sure that the quantity is a number";
            }
            else if(!list.get(i)[2].equals(""))
                test="Make sure that the unit has a quantity";
                i++;
        }
            
        return test;
    }
    
}
