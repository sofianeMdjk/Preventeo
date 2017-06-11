/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pfeproject;

import com.jfoenix.controls.JFXTextField;
import java.awt.Font;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.FontPosture;
import org.controlsfx.control.textfield.TextFields;
import Managers.FoodManager.Food;
import javafx.scene.text.FontWeight;

/**
 *
 * @author ressay
 */
public class DFields implements Initializable 
{
    //my vars
    private static LinkedList<String> suggestions=new LinkedList<>();
    //private static DFields _instance=null;
    
    
      @FXML
    private ScrollPane MyBox;
    
    @FXML
    private AnchorPane MyPane; 
    private static LinkedList<TextField> fields = null;
    private int fieldsNumber;
    
    
    
    public static void setSuggestion(LinkedList<String> l)
    {
        suggestions=null;
        suggestions = l;
    }
    private void handleButtonAction(ActionEvent event) 
    {
        System.out.println("You clicked me!");
    }
    
    private void handleTextFieldAction(ActionEvent event) 
    {
        System.out.println("You typed me!");
    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        // TODO
       // MyPane.setStyle("-fx-background-image: url('" + MainApp.image   + "');");
        fields=new LinkedList<TextField>();
        //_instance=this;
        addField();
    }  
    String s = "khobz";
    public class Handler1 implements EventHandler<Event>
    {

        @Override
        public void handle(Event event) 
        {
//            JFXTextField tf = (JFXTextField)event.getSource();
//            LinkedList<String> list = new LinkedList<>();
//            LinkedList<Food> fs = MainApp.getFoodManager().parseFood(tf.getText(), 1, 5);
//            for (Food f : fs) 
//            {
//                list.add(f.name);
//                System.out.println(f);
//            }
//            TextFields.bindAutoCompletion(tf,fs);
            manageFields();
            
        }
        
    }
    public class Handler2 implements EventHandler<Event>
    {
        @Override
        public void handle(Event event)
        {
            checkSpelling();
        }
    }
    
    
    
    

    public class MyTextField extends JFXTextField
    {

        @Override
        public void paste() 
        {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                if (clipboard.hasString()) 
                {
                    replaceSelection(clipboard.getString());
                }
                int i;
                for(i=0;this != fields.get(i);i++);
                split(clipboard.getString(),i);
                //System.out.println("pasted!!");
        }
        
    }
    
    
    private void checkSpelling()
    {
        int size=fields.size();
       if(size>0)
       {
           System.out.println("the text in the previous field is "+fields.get(size-1));
       }
    }
    
    LinkedList<String> getSplitedString(String S)
    {
        LinkedList<String> strings = new LinkedList<String>();
        int i;
        for(i=1;i*10 < S.length();i++)
        {
            //String s = new String(S.substring((i-1)*10, i*10));
            strings.add(S.substring((i-1)*10, i*10));
        }
        strings.add(S.substring((i-1)*10));
        return strings;
    }
    
    private void split(String S,int k)
    {
        LinkedList<String> strings = getSplitedString(S);
        int size = strings.size();
        for(int i=0;i<size-1;i++)
        addField();
        int shift = size;
        size = fields.size();
        fields.get(k).setText(strings.get(0));
        for(int i=k+1;i<k+shift;i++)
        {
            if(i+shift < size)
            fields.get(i+shift).setText(fields.get(i).getText());
            fields.get(i).setText(strings.get(i-k));
        }
    }
    
    private void manageFields()
    {
        int size = fields.size();
        int i;
        for(i=0;i<size-1;i++)
        {
            String s = fields.get(i).getText();
            if(s.length() == 0)
            {
                if(size > 1)
                rmv(i);
                return;
            }
        }
        if(fields.get(i).getText().length() > 0)
        addField();
    }
    
    public LinkedList<String> getAllTexts()
    {
       
        LinkedList<String> strings = new LinkedList<String>();
        for(int i=0;i<fields.size();i++)
        {
           
            String s = new String(fields.get(i).getText().toLowerCase());
         
            if(s.length()>0)
            strings.add(s);
        }
        return strings;
    }
    
        
    private void addField()
    {
        JFXTextField tf = new MyTextField();
        tf.setPrefWidth(380);
        tf.setFocusColor(Color.BLUE);
        MyPane.getChildren().add(tf);
        if(fields.size()>0)
        tf.setLayoutY((double)fields.getLast().getLayoutY()+50);
        
        tf.setFont(javafx.scene.text.Font.font("System",FontWeight.BOLD,15));
      
        tf.setLayoutX(10);
        fields.add(tf);
        tf.setOnKeyTyped(new DFields.Handler1());
        tf.setOnKeyReleased(new DFields.Handler1());
        System.out.println("number of fields: " + fields.size());
    }
    
    private void rmv(int k)
    {
        if(fields.size()==1)
        {
            fields.get(0).setText("");
            return;
        }
        int size = fields.size();
        for(int i=k;i<size-1;i++)
        {
            fields.get(i).setText(fields.get(i+1).getText());
        }
        TextField ft = fields.removeLast();
        MyPane.getChildren().remove(ft);
    }
        
    public void setValues(LinkedList<String> list)
    {
        int listSize=list.size();
        int i=0;
        while (i<listSize)
        {
            fields.get(i).setText(list.get(i));
            addField();
            i++;
        }
        addField();
    }
    
    
//    public static DFields getInstance()
//    {
//        return _instance;
//    }
    
    public void disableEdit()
    {
        int size=fields.size();
        for(int i=0;i<size;i++)
            fields.get(i).setEditable(false);
    }
    public void enableEdit()
    {
        int size=fields.size();
        for(int i=0;i<size;i++)
            fields.get(i).setEditable(true);
    }
    
    public void resetAll()
    {
        for(;1<fields.size();)
            rmv(0);
        rmv(0);
    }
    
}
