/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pfeproject;

import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * FXML Controller class
 *
 * @author soso
 */
public class DFieldsBisController extends MyScene implements Initializable {

    @FXML
    private ScrollPane MyBox;
    @FXML
    private AnchorPane MyPane;
    
        //my vars
     private static LinkedList<String> suggestions=new LinkedList<>();
     private static LinkedList<JFXTextField[]> fields = null;
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
        //MyPane.setStyle("-fx-background-image: url('" + MainApp.image2+ "');");
        fields=new LinkedList<JFXTextField[]>();
        //_instance=this;
        addField();
    }  
    

    
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
                for(i=0;this != fields.get(i)[0];i++);
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
        fields.get(k)[0].setText(strings.get(0));
        for(int i=k+1;i<k+shift;i++)
        {
            if(i+shift < size)
            fields.get(i+shift)[i].setText(fields.get(i)[0].getText());
            fields.get(i)[0].setText(strings.get(i-k));
        }
    }
    
    private void manageFields()
    {
        int size = fields.size();
        int i;
        for(i=0;i<size-1;i++)
        {
            String s = fields.get(i)[0].getText();
            if(s.length() == 0)
            {
                if(size > 1)
                rmv(i);
                else
                {
                    fields.get(i)[1].setDisable(true);
                    fields.get(i)[2].setDisable(true);
                }
                return;
            }
        }
        if(fields.get(i)[0].getText().length() > 0)
        {
        fields.get(i)[1].setDisable(false);
        fields.get(i)[2].setDisable(false);
        addField();
        }
    }
    
    public LinkedList<String[]> getAllTexts()
    {
       
        LinkedList<String[]> strings = new LinkedList<>();
        for(int i=0;i<fields.size();i++)
        {
           
            String s1 = new String(fields.get(i)[0].getText().toLowerCase());
            String s2 = new String(fields.get(i)[1].getText().toLowerCase());
            String s3 = new String(fields.get(i)[2].getText().toLowerCase());
            if(s1.length()>0)
            {
            String[] tab={s1,s2,s3};
            strings.add(tab);
            }
        }
        return strings;
    }
    
        
    private void addField()
    {
        JFXTextField[] tf=createThreeFields();
        //composants 
        tf[0].setPrefWidth(380);
        tf[0].setFocusColor(Color.BLUE);
        tf[0].setLayoutX(20);
        tf[0].setFont(Font.font("System",FontWeight.BOLD,15));
       
        MyPane.getChildren().add(tf[0]);
        
        //quantity
        tf[1].setPrefWidth(100);
        tf[1].setFocusColor(Color.BLUE);
        tf[1].setLayoutX(450);
        tf[1].setFont(Font.font("System",FontWeight.BOLD,15));
        tf[1].setDisable(true);
        MyPane.getChildren().add(tf[1]);
        
        //unity
        
        tf[2].setPrefWidth(125);
        tf[2].setFocusColor(Color.BLUE);
        tf[2].setLayoutX(625);
        tf[2].setFont(Font.font("System",FontWeight.BOLD,15));
        tf[2].setDisable(true);
        MyPane.getChildren().add(tf[2]);
        
        
     
        if(fields.size()>0)
        {
        tf[0].setLayoutY((double)fields.getLast()[0].getLayoutY()+50);
        tf[1].setLayoutY((double)fields.getLast()[1].getLayoutY()+50);
        tf[2].setLayoutY((double)fields.getLast()[2].getLayoutY()+50);
        }
        
        
       
        fields.add(tf);
        tf[0].setOnKeyTyped(new DFieldsBisController.Handler1());
        tf[0].setOnKeyReleased(new DFieldsBisController.Handler1());
        System.out.println("number of fields: " + fields.size());
    }
    
    private void rmv(int k)
    {
        if(fields.size()==1)
        {
            fields.get(0)[0].setText("");
            fields.get(0)[1].setText("");
            fields.get(0)[2].setText("");
            fields.get(0)[1].setDisable(true);
            fields.get(0)[2].setDisable(true);
            return;
        }
        int size = fields.size();
        for(int i=k;i<size-1;i++)
        {
            fields.get(i)[0].setText(fields.get(i+1)[0].getText());
            fields.get(i)[1].setText(fields.get(i+1)[1].getText());
            fields.get(i)[2].setText(fields.get(i+1)[2].getText());
        if(fields.get(i)[0].getText().length() > 0)
        {
        fields.get(i)[1].setDisable(false);
        fields.get(i)[2].setDisable(false);
        }
        else
        {
            fields.get(i)[1].setDisable(true);
        fields.get(i)[2].setDisable(true);
        }
        }
        JFXTextField[] ft = fields.removeLast();
        
        MyPane.getChildren().remove(ft[0]);
        MyPane.getChildren().remove(ft[1]);
        MyPane.getChildren().remove(ft[2]);
    }
        
    public void setValues(LinkedList<String> list)
    {
        int listSize=list.size();
        int i=0;
        while (i<listSize)
        {
            fields.get(i)[0].setText(list.get(i));
            fields.get(i)[1].setText(list.get(i));
            fields.get(i)[2].setText(list.get(i));
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
        {
            fields.get(i)[0].setEditable(false);
            fields.get(i)[1].setEditable(false);
            fields.get(i)[2].setEditable(false);
        }
    }
    public void enableEdit()
    {
        int size=fields.size();
        for(int i=0;i<size;i++)
        {
            fields.get(i)[0].setEditable(true);
            fields.get(i)[1].setEditable(true);
            fields.get(i)[2].setEditable(true);
        }
    }
    
    
    private JFXTextField[] createThreeFields()
    {
        JFXTextField[] fields=new JFXTextField[3];
        fields[0]=new JFXTextField();
        fields[1]=new JFXTextField();
        fields[2]=new JFXTextField();
        return fields;
    }
    
    public void resetAll()
    {
        for(;1<fields.size();)
            rmv(0);
        rmv(0);
    }
    
    
    @Override
    Scene getScene() {
        
          FXMLLoader fxmlLoader = new FXMLLoader();
        Pane root = null;
        try {
            root = fxmlLoader.load(
                    MainApp.getInstance().getClass().getResource("DFieldsBis.fxml").openStream());
        } catch (IOException ex) {
            
        }
        Scene scene = new Scene(root);
        root.setStyle("-fx-background-image: url('" + MainApp.image + "');");
        setParent(root);
        return scene;
    }
    
    
}
