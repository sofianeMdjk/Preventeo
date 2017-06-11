package Managers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sofiane
 */
public class AdminManager {
    
    private static String username;
    private static String email;
    private static String password;
    private static String passwordConfirm;
    
    public AdminManager(String u,String e,String p,String pc)
    {
        
    }

    public static String getUsername() {
        return username;
    }

    public static String getEmail() {
        return email;
    }

    public static String getPassword() {
        return password;
    }

    public static String getPasswordConfirm() {
        return passwordConfirm;
    }
    
    public static void changeAdminInfos(String u,String e,String p,String pc)
    {
        
    }
    
}
