/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pfeproject;

import Utils.GraphicalTools;
import Managers.Recipe;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.util.LinkedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author soso
 */
public class RecipeViewer {
    
    AnchorPane pane;
    LinkedList<Node> nodes;
    int page = 1;
    int limit = 20;
    MainApp.HolderManager holder = null;

    public RecipeViewer(AnchorPane pane,LinkedList<Node> nodes) 
    {
        this.pane = pane;
        this.nodes = nodes;
        JFXListView<Recipe> recipeList= GraphicalTools.generateListOfREcipes(page);
            recipeList.setPrefWidth(730);
            recipeList.setPrefHeight(420);
            recipeList.setLayoutX(200);
            recipeList.setLayoutY(70);
            nodes.add(recipeList);
            pane.getChildren().add(recipeList);  
            
            
            JFXTextField foodField=new JFXTextField();
            foodField.setPromptText("Enter a recipe name");
            foodField.setLabelFloat(true);
            foodField.setPrefWidth(730);
            foodField.prefHeight(30);
            foodField.setLayoutX(200);
            foodField.setLayoutY(30);
            
            holder = new MainApp.HolderManager(new MainApp.Holder.MyAction() {
                            @Override
                            public void act() 
                            {
                                page = 1;
                                if(!foodField.getText().equals(""))
                                GraphicalTools.parseElement(foodField.getText(),recipeList,2,page,limit);
                                else
                                {
                                recipeList.getItems().clear();
                                recipeList.getItems().addAll(GraphicalTools.generateListOfREcipes(page).getItems());
                                }
                            }
                        }, 300);
            foodField.setOnKeyReleased(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) 
                {
                            holder.runD();
          
                }
            });
            nodes.add(foodField);
            pane.getChildren().add(foodField);
            
            JFXButton next = new JFXButton("next");
            
            next.setLayoutX(180+730/2);
            next.setLayoutY(540);
            nodes.add(next);
            pane.getChildren().add(next);
            next.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) 
                {
                    if(recipeList.getItems().size() == limit)
                    {
                    page++;
                    if(!foodField.getText().equals(""))
                    GraphicalTools.parseElement(foodField.getText(),recipeList,2,page,limit);
                    else
                    {
                    recipeList.getItems().clear();
                    recipeList.getItems().addAll(GraphicalTools.generateListOfREcipes(page).getItems());
                    }
                    }
                }
            });
            
            JFXButton precedent = new JFXButton("precedent");
            
            precedent.setLayoutX(60+730/2);
            precedent.setLayoutY(540);
            nodes.add(precedent);
            pane.getChildren().add(precedent);
            precedent.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) 
                {
                    if(page > 1)
                    {
                    page--;
                    if(!foodField.getText().equals(""))
                    GraphicalTools.parseElement(foodField.getText(),recipeList,2,page,limit);
                    else
                    {
                    recipeList.getItems().clear();
                    recipeList.getItems().addAll(GraphicalTools.generateListOfREcipes(page).getItems());
                    }
                    }
                }
            });
    }
    
}
