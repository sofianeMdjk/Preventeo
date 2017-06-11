/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Managers;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author sofiane
 */
public class Admin {
    
private static String username;
private static String email;
public static boolean isAdmin = false;





    public static String getUsername() {
        return username;
    }

    public static String getEmail() {
        return email;
    }

    public static void setUsername(String username) {
        Admin.username = username;
    }

    public static void setEmail(String email) {
        Admin.email = email;
    }
    
    public static boolean adminConnexion(String u, String ps)
    {
        
        return true;
    }

    
}
