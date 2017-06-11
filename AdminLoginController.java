/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pfeproject;

import Utils.Security;
import Managers.DataBaseAccess;
import Managers.Admin;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author sofiane
 */
public class AdminLoginController extends MyScene implements Initializable {

    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private ImageView check;
    @FXML
    private ImageView logo;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        check.setImage(new Image(
                MainApp.getInstance().getClass().getResource("pics/login.png").toExternalForm()));
        logo.setImage(new Image(
                MainApp.getInstance().getClass().getResource("pics/preventeo-logo.png").toExternalForm()));
    }    

    @Override
    Scene getScene() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane root = null;
        try {
            root = fxmlLoader.load(
                    MainApp.getInstance().getClass().getResource("AdminLogin.fxml").openStream());
        } catch (IOException ex) {
            
        }
        Scene scene = new Scene(root);
        
        root.setStyle("-fx-background-image: url('" + MainApp.image + "');");
        setParent(root);
        return scene;
    }

    @FXML
    private void hadnleMouse(MouseEvent event) throws NoSuchAlgorithmException, SQLException, SQLException, UnsupportedEncodingException, SQLException, ClassNotFoundException {
        if(usernameField.getText().compareTo("")==0 || passwordField.getText().compareTo("")==0)
        {
            Notifications notif= Notifications.create();
                notif.text("Missing informations make sur to fill the fields");
                notif.position(Pos.TOP_CENTER);
                notif.darkStyle();
                notif.showError();
        }
        else
        {
            String username=usernameField.getText();
            String password=Security.hash_to_sha512(passwordField.getText());
            DataBaseAccess dba=new DataBaseAccess("jdbc:mysql://localhost:3306/informations", "root", "Resssay95");
            boolean conResult=dba.adminLogin(username, password);
            if(conResult)
            {
                Admin.setUsername(username);
                Admin.isAdmin=true;
                MainApp.getInstance().myWin.nextScene(new AdminUIController());
            }
            else
            {
                Notifications notif= Notifications.create();
                notif.text("Wrong information");
                notif.position(Pos.CENTER);
                notif.darkStyle();
                notif.showError();
            }
        }
    }

    @FXML
    private void handleOk(KeyEvent event) throws Exception {
        if(event.getCode()==KeyCode.T   )
        {
             if(usernameField.getText().compareTo("")==0 || passwordField.getText().compareTo("")==0)
        {
            Notifications notif= Notifications.create();
                notif.text("Missing informations make sur to fill the fields");
                notif.position(Pos.TOP_CENTER);
                notif.darkStyle();
                notif.showError();
        }
        else
        {
            String username=usernameField.getText();
            String password=Security.hash_to_sha512(passwordField.getText());
            DataBaseAccess dba=new DataBaseAccess("jdbc:mysql://localhost:3306/informations", "root", "Resssay95");
            boolean conResult=dba.adminLogin(username, password);
            if(conResult)
            {
                Admin.setUsername(username);
                Admin.isAdmin=true;
                MainApp.getInstance().myWin.nextScene(new AdminUIController());
            }
            else
            {
                Notifications notif= Notifications.create();
                notif.text("Wrong information");
                notif.position(Pos.CENTER);
                notif.darkStyle();
                notif.showError();
            }
        }
        }
    }
    
    
    
    
}
