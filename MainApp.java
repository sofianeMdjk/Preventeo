/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pfeproject;

import Utils.Security;
import Managers.FoodManager;
import com.SY.myQueryFac.QueryGen;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import static Managers.FoodManager.namePredicate;
import pfeproject.MainApp.Holder.MyAction;

/**
 *
 * @author sofiane
 */
public class MainApp extends Application {
    
    static public String url = "http://localhost:3030/ds/sparql";
    static public String image= MainApp.class.getResource("pics/bggris2.jpg").toExternalForm();
    static public String image2= MainApp.class.getResource("pics/bggris.png").toExternalForm();
    public static boolean isAdmin=true;
     public static FoodManager a=new FoodManager(url);
    @Override
    public void start(Stage stage) throws Exception {
        String username="yasser";
        String email="sofiane.mdjk@gmail.com";
        String password=Security.hash_to_sha512("6969");
       // DataBaseAccess db= new DataBaseAccess("jdbc:mysql://localhost:3306/informations", "root", "Resssay95");
        //db.createAdmin(username, email, password);
       
        
        
        
        init(stage);
        myWin.nextScene(new AdminUIController());
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        launch(args);
        
    }
    
    public static MainApp _instance = null;
    public  MainWindow myWin;
    
      public static MainApp getInstance()
    {
        
        return _instance;
    }
       public MainWindow getMyWin()
       {
           return myWin;
       }
       
    public static FoodManager getFoodManager()
    {
        return a;
    }
    void init(Stage s)
    {
        myWin = new MainWindow(s);
        _instance = this;
    }
    
    public static Pane loadScene(String name)
    {
        FXMLLoader fxmlLoader=new FXMLLoader();
        Pane root=null;
        try {
            root=fxmlLoader.load(MainApp.getInstance().getClass().getResource(name));
        } catch (IOException ex) {
            Logger.getLogger(AddDiseaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return root;
    }
    
    
    
    public static class Holder extends Task
    {

        @Override
        protected Object call()
        {
            System.out.println("in call");
            isRunning = true;
                            for(int d;ti > 0; ti-=50)
                            {
                            
//                            d= time.removeFirst();
                                System.out.println("s = " + ti);
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            }
                        System.out.println("in call running act");
                        Platform.runLater(()->{
                            action.act();
                        });
                        isRunning = false;
                        done = true;
            return null;
        }
        
        public interface MyAction
        {
            public void act();
        }
        boolean isRunning = false;
        boolean done = false;
        private LinkedList<Integer> time = new LinkedList<>();
        int toAdd;
        MyAction action;
        int ti;
        public Holder(MyAction a,int add)
        {
            toAdd= add;
            action = a;
        }
        
        public void runD()
        {
            runD(toAdd);            
        }
        
        public void runD(int dela) 
        {
            System.out.println(".run()11");
            time.add(dela);
            ti = dela;
                    if(!isRunning)
                    {
                        
                        System.out.println(".platform run");
                        //Platform.runLater(this);
                        new Thread(this).start();
                        System.out.println(".platform run");
//                        
                    }
                     else
                     ti = dela;
            System.out.println(".run()1122");
            
        }
    }
    
    public static class HolderManager
    {
        Holder holder = null;
        MyAction a;
        int toAdd;
        public HolderManager(Holder.MyAction a,int add)
        {
            toAdd= add;
            this.a = a;
        }
        
        public void runD()
        {
            runD(toAdd);
        }
        public void runD(int dela)
        {
            if(holder == null || holder.done == true)
            {
                holder = new Holder(a, dela);
                holder.runD();
            }
            else
            holder.runD(dela);
        }
    }
    
     public static String removeSpaces(String s)
    {
        Pattern pattern =Pattern.compile("(\\s+)(.*)(\\s+)");
        Matcher matcher = pattern.matcher(s);
        String modified =matcher.replaceAll("$2");
        return modified;
    }
    
    public static String formatString(String s)
    {
        s=s.toLowerCase();
        s=removeSpaces(s);
        s=s.replace(" ", "-");
        return s;
    }
    
    
}
