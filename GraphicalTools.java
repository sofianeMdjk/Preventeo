/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import Managers.Recipe;
import Managers.Notification;
import Managers.FoodManager;
import Managers.DiseaseManager;
import com.jfoenix.controls.JFXListView;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import Managers.DiseaseManager.Disease;
import Managers.FoodManager.Food;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pfeproject.AddDiseaseController;
import pfeproject.AdminUIController;
import pfeproject.FoodDetailController;
import pfeproject.MainApp;

/**
 *
 * @author sofiane
 */
public class GraphicalTools {
    
    public static void updateListView(JFXListView listView, LinkedList tab)
    {
        if(listView!=null)
            listView.getItems().clear();
        for(int i=0;i<tab.size();i++)
        {
            listView.getItems().add(tab.get(i));
        }
        
        

    }
    public static void openWIndow(String name) throws IOException
    {
        FXMLLoader fxmlLoader=new FXMLLoader();
        Parent root=fxmlLoader.load(MainApp.getInstance().myWin.getClass().getResource(name));
        Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
    }
    
     public static void parseElement(String typed, JFXListView list,int type,int page,int limit)
    {
         LinkedList results=null;
        switch(type)
        {
            case 1:
            {
            results=MainApp.getFoodManager().parseFood(typed,page,limit);
            list.getItems().clear();
            list.getItems().addAll(results);
            }break;
            case 2 :
            {
            results=Recipe.parseRecipe(typed,page,limit);
            list.getItems().clear();
            list.getItems().addAll(results);
            }break;
        }
        
    }
    
    public static JFXListView<Food> generateListOfFoods(int page)
    {
             JFXListView<Food> foodList=new JFXListView();
             foodList.getItems().addAll(MainApp.a.getAllFood(page));
              setFoodEvent(foodList);
        return foodList;
    }
    
    
    public static JFXListView<Disease> generateListOfDiseases()
    {
        JFXListView<Disease> diseaseList=new JFXListView();
             diseaseList.getItems().addAll(DiseaseManager.getD());
             setDiseaseEvent(diseaseList);
                return diseaseList;
    }
    
     public static JFXListView<Recipe> generateListOfREcipes(int page)
    {
             JFXListView<Recipe> recipeList=new JFXListView();
             recipeList.getItems().addAll(Recipe.getAllRecipes(page));
              setRecipeEvent(recipeList);
        return recipeList;
    }
    
    
    public static void setFoodEvent(JFXListView<Food> listView)
    {
          listView.setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent click) {
                  Food currentItemSelected = listView.getSelectionModel()
                                                    .getSelectedItem();
                  if(currentItemSelected!=null)
                 {
                     FoodManager.selectedFood=currentItemSelected;
                     //we open the new window and send it's parameters
                     FXMLLoader fxml=new FXMLLoader();
                     Parent root=null;
                      try {
                          root=fxml.load(
                                  MainApp.getInstance().myWin.getClass().getResource("FoodDetail.fxml"));
                      } catch (IOException ex) {
                          Logger.getLogger(AddDiseaseController.class.getName()).log(Level.SEVERE, null, ex);
                      }
                      FoodDetailController controller=fxml.getController();
                      root.setStyle("-fx-background-image: url('" + MainApp.image + "');");
                      Scene scene = new Scene(root);
                      Stage stage=new Stage();
                      stage.setScene(scene);    
                      stage.show();
                 }
                    
                  
                }
                }
            );
    }
    
    public static void setNotifEvent(JFXListView<Notification> listView)
    {
        
        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                
                JFXListView <Notification> notifListView = (JFXListView <Notification>) event.getSource();
//                System.out.println(notifListView.getSelectionModel().getSelectedItem().toString());
                    AdminUIController.lastClickedNotif = notifListView.getSelectionModel().getSelectedItem();
                   FXMLLoader fxml=new FXMLLoader();
                     Parent root=null;
                      try {
                          root=fxml.load(
                                  MainApp.getInstance().myWin.getClass().getResource("NotificationConfirm.fxml"));
                      } catch (IOException ex) {
                          Logger.getLogger(AddDiseaseController.class.getName()).log(Level.SEVERE, null, ex);
                      }
                      root.setStyle("-fx-background-image: url('" + MainApp.image + "');");
                      Scene scene = new Scene(root);
                      Stage stage=new Stage();
                      stage.setTitle("Notification");
                      stage.setScene(scene);    
                      stage.show();
            }
            });
        
        }
       
    
    public static void setDiseaseEvent(JFXListView<Disease> listView)
    {
                listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                 Disease diseaseName= listView.getSelectionModel().getSelectedItem();
                    DiseaseManager.setSelectedDisease(diseaseName);
                    FXMLLoader fxml=new FXMLLoader();
                     Parent root=null;
                      try {
                          root=fxml.load(
                                  MainApp.getInstance().myWin.getClass().getResource("DiseaseDetail.fxml"));
                      } catch (IOException ex) {
                          Logger.getLogger(AddDiseaseController.class.getName()).log(Level.SEVERE, null, ex);
                      }
                      FoodDetailController controller=fxml.getController();
                      root.setStyle("-fx-background-image: url('" + MainApp.image + "');");
                      Scene scene = new Scene(root);
                      Stage stage=new Stage();
                      stage.setScene(scene);    
                      stage.show();
            }
        });
    }
    
    public static Pane loadScene(Pane root,String sceneName)
    {
        FXMLLoader fxmlLoader = new FXMLLoader();
         try {
            root=fxmlLoader.load(MainApp.getInstance().getClass().getResource(sceneName).openStream());
             } catch (IOException ex) {
            Logger.getLogger(AddDiseaseController.class.getName()).log(Level.SEVERE, null, ex);
            }
         return root;
    }
    
    public static void setRecipeEvent(JFXListView<Recipe> listView)
    {
        listView.setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent click) {
                  Recipe currentItemSelected = listView.getSelectionModel()
                                                    .getSelectedItem();
                  if(currentItemSelected!=null)
                 {
                     Recipe.selectedRecipe=currentItemSelected;
                     //we open the new window and send it's parameters
                     FXMLLoader fxml=new FXMLLoader();
                     Parent root=null;
                      try {
                          root=fxml.load(
                                  MainApp.getInstance().myWin.getClass().getResource("RecipeDetail.fxml"));
                      } catch (IOException ex) {
                          Logger.getLogger(AddDiseaseController.class.getName()).log(Level.SEVERE, null, ex);
                      }
                      FoodDetailController controller=fxml.getController();
                      root.setStyle("-fx-background-image: url('" + MainApp.image + "');");
                      Scene scene = new Scene(root);
                      Stage stage=new Stage();
                      stage.setScene(scene);    
                      stage.show();
                 }
                    
                  
                }
                }
            );
    }
    
    
    public static ImageView[] setImages(String img1,String img2)
    {
        
        
        ImageView images[]=new ImageView[2];
        images[0]=new ImageView();
        images[0].setImage(new Image(MainApp._instance.myWin.getClass().getResource(
                img1).toExternalForm()));
        images[0].setLayoutX(20);
        images[0].setLayoutY(80);
        images[0].setFitWidth(100);
        images[0].setFitHeight(400);
        
        
        
        images[1]=new ImageView();
        images[1].setImage(new Image(MainApp._instance.myWin.getClass().getResource(
                img2).toExternalForm()));
        images[1].setLayoutX(850);
        images[0].setLayoutY(80);
        images[0].setFitWidth(100);
        images[0].setFitHeight(400);
        
        return images;
    }
}
