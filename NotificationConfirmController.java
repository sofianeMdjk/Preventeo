/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pfeproject;

import Managers.Notification;
import Managers.DataBaseAccess;
import com.SY.myQueryFac.QueryGen;
import com.SY.myQueryFac.UpdateQuery;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sofiane
 */
public class NotificationConfirmController implements Initializable {

    @FXML
    private JFXButton accept;
    @FXML
    private JFXButton refuse;
    @FXML
    private JFXTextArea content;
    @FXML
    private Label name;
    
    Notification myNotif;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        myNotif = AdminUIController.lastClickedNotif;
        try {
            initNodes();
        } catch (SQLException ex) {
            Logger.getLogger(NotificationConfirmController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NotificationConfirmController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        accept.setOnAction((event) -> 
        {
            try 
            {
                UpdateQuery uq = new UpdateQuery(QueryGen.prefixes);
                System.out.println("query: " + myNotif.getNotif());
                uq.applyQuery(myNotif.getNotif());
                deleteNotif();
            } catch (Exception ex) {
                Logger.getLogger(NotificationConfirmController.class.getName()).log(Level.SEVERE, null, ex);
            }
          
            Stage stage = (Stage) accept.getScene().getWindow();
    // do what you have to do
    stage.close();
            try {
                AdminUIController._instance.refreshNotif();
            } catch (Exception ex) {
                Logger.getLogger(NotificationConfirmController.class.getName()).log(Level.SEVERE, null, ex);
            }
    
        });
        refuse.setOnAction((event) -> 
        {
            try 
            {
                deleteNotif();
            } catch (Exception ex) {
                Logger.getLogger(NotificationConfirmController.class.getName()).log(Level.SEVERE, null, ex);
            }
            Stage stage = (Stage) refuse.getScene().getWindow();
    // do what you have to do
    stage.close();
    
    try {
                AdminUIController._instance.refreshNotif();
            } catch (Exception ex) {
                Logger.getLogger(NotificationConfirmController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }


    private void deleteNotif() throws Exception
    {
        DataBaseAccess dba=new DataBaseAccess("jdbc:mysql://localhost:3306/informations", "root", "Resssay95");
        dba.deleteNotif(myNotif.getId());
    }
    
    
private void initNodes() throws SQLException, ClassNotFoundException
{
    
        String info=myNotif.getInfo();
      
      String entities[]=info.split(";");
      System.out.println(info);
      int type =Integer.parseInt(entities[0]);
      
      switch(type)
      {
          case 0 :
          {
              String textAreaContent="";
              //inserting the name
              String foodName ="Food : "+entities[1];  
              name.setText(foodName);
              //the code
             String code=entities[2];
             if(code.compareTo("?")!=0)
             {
                 textAreaContent+="Food Code : "+code+"\n";
             }
             //the origin
             String origin =entities[3];
              if(origin.compareTo("?")!=0)
             {
                 textAreaContent+="Food Origin : "+origin+"\n";
             }
              
              //the ingredients
              String ingredients=entities[4];
              if(ingredients.compareTo("?")!=0)
              {
                  String tab[]=ingredients.split(",");
                  String display="";
                  for(int j=0;j<tab.length;j++)
                  {
                      display+=tab[j]+"\n";
                  }
                textAreaContent+=" Ingredients of the food : \n"+display; 
                content.setText(textAreaContent);
               
              }
          }break;
          
          case 1 :
          {
              String diseaseName="Disease : "+entities[1];
              
              name.setText("Disease : "+diseaseName);
             String textAreaContent="";
              
              //pas la peine de tester il faut au minimum une cause
              String tab[] = entities[2].split(",");
              String display="";
              for(int j=0;j<tab.length;j++)
              {
                  display+=tab[j]+"\n";
              }
              textAreaContent+=" List of causes : \n"+display;
              content.setText(textAreaContent);
              
          }break;
          
          case 2 :
          {
              String RecipeName ="Recipe : "+entities[1];
              name.setText(RecipeName);
              String textAreaContent="";
              String composants[]=entities[2].split(",");
              System.out.println("kheuuuub" +entities[2]);
              String display="";
              for(int j=0;j<composants.length;j++)
              {
                  String tab[]= composants[j].split("-");
                  
                  display+="Ingredient : "+tab[0];
                  if(tab.length >= 2)
                      display += " Quantity : "+tab[1];
                  if(tab.length >= 3)
                      display += " "+tab[2];
                  display += "\n";
              }
              textAreaContent="List of ingredients : \n"+display;
              content.setText(textAreaContent);
          }break;
          
          
      }
      
//      DataBaseAccess db=new DataBaseAccess("localhost", "root", "sofsql");
      
}


    
}
