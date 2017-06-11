/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Managers;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.MysqlIO;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author sofiane
 */
public class DataBaseAccess {
    
    Connection con;
    Statement statement;
    public DataBaseAccess(String url,String username,String password) throws SQLException, ClassNotFoundException
    {
         Class.forName("com.mysql.jdbc.Driver");
         con= (Connection) DriverManager.getConnection(url,username,password);
         statement= con.createStatement();
    }
    
    public void createAdmin(String username,String email,String password) throws SQLException
    {
        String query="INSERT INTO `Admin` VALUES (NULL,'"+username+"','"+email+"','"+password+"')";
        statement.executeUpdate(query);
    }
    public void addNotification(String notif,String q) throws SQLException
    {
        System.out.println("query: " + q);
        String query ="INSERT INTO `Notification` VALUES (NULL, '"+notif+"', '"+q+"')";
        statement.executeUpdate(query);
    }
    public void deleteNotif(int id) throws SQLException
    {
        String query ="DELETE FROM `Notification` WHERE `id`="+id+"";
        statement.executeUpdate(query);
    }
    public LinkedList<Notification> getNotifications() throws SQLException
    {
        String query ="SELECT `id`,`infos`,`query` FROM `Notification`";
        ResultSet results= statement.executeQuery(query);
        int id;
        String infos;
        String sparqlQ;
        
        LinkedList<Notification> res=new LinkedList<>();
        while(results.next())
        {
            id=results.getInt("id");
            infos= results.getString("infos");
            sparqlQ=results.getString("query");
            res.add(new Notification(id, infos, sparqlQ));
        }
        return res;
    }
    
    public boolean adminLogin(String u,String p) throws SQLException
    {
        String query ="CALL verify_login('"+u+"','"+p+"')";
        ResultSet result = statement.executeQuery(query);
        int value=0;
        while (result.next())
        {
            value= result.getInt("myVar");
        }
        if(value==0)
        return false;
        else
            return true;
    }
    
}
