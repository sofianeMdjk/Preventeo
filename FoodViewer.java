/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pfeproject;

import Utils.GraphicalTools;
import Managers.FoodManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import pfeproject.MainApp.Holder;

/**
 *
 * @author ressay
 */
public class FoodViewer 
{
    AnchorPane pane;
    LinkedList<Node> nodes;
    int page = 1;
    int limit = 20;
    MainApp.HolderManager holder = null;

    public FoodViewer(AnchorPane pane,LinkedList<Node> nodes) 
    {
        this.pane = pane;
        this.nodes = nodes;
        JFXListView<FoodManager.Food> foodList= GraphicalTools.generateListOfFoods(page);
            foodList.setPrefWidth(730);
            foodList.setPrefHeight(420);
            foodList.setLayoutX(300);
            foodList.setLayoutY(70);
            nodes.add(foodList);
            pane.getChildren().add(foodList);  
            
            
            JFXTextField foodField=new JFXTextField();
            foodField.setPromptText("Enter a food name");
            foodField.setLabelFloat(true);
            foodField.setPrefWidth(730);
            foodField.prefHeight(30);
            foodField.setLayoutX(300);
            foodField.setLayoutY(40);
            
            holder = new MainApp.HolderManager(new Holder.MyAction() {
                            @Override
                            public void act() 
                            {
                                page = 1;
                                if(!foodField.getText().equals(""))
                                GraphicalTools.parseElement(foodField.getText(),foodList,1,page,limit);
                                else
                                {
                                foodList.getItems().clear();
                                foodList.getItems().addAll(GraphicalTools.generateListOfFoods(page).getItems());
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
            next.setStyle("-fx-background: #4059a9;");
            next.setLayoutX(180+730/2);
            next.setLayoutY(540);
            nodes.add(next);
            pane.getChildren().add(next);
            next.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) 
                {
                    if(foodList.getItems().size() == limit)
                    {
                    page++;
                    if(!foodField.getText().equals(""))
                    GraphicalTools.parseElement(foodField.getText(),foodList,1,page,limit);
                    else
                    {
                    foodList.getItems().clear();
                    foodList.getItems().addAll(GraphicalTools.generateListOfFoods(page).getItems());
                    }
                    }
                }
            });
            
            JFXButton precedent = new JFXButton("precedent");
            
            precedent.setLayoutX(60+730/2);
            precedent.setStyle("-fx-background: #4059a9;");
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
                    GraphicalTools.parseElement(foodField.getText(),foodList,1,page,limit);
                    else
                    {
                    foodList.getItems().clear();
                    foodList.getItems().addAll(GraphicalTools.generateListOfFoods(page).getItems());
                    }
                    }
                }
            });
    }
    
}
