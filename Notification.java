/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Managers;

/**
 *
 * @author soso
 */
public class Notification {
    
    private int id;
    private String info;
    private String notif;
    
    public Notification(int i,String inf,String not)
    {
        this.id=i;
        this.info=inf;
        this.notif=not;
    }
    
    public String toString()
    {
        
        String str="";
        String tab[] = info.split(";");
        System.out.println(tab[0]+"     "+tab.length);
        String types[]={"Food","Disease","Recipe"};
        
        
        int type =Integer.parseInt(tab[0]);
        str+=types[type] + " : "+tab[1];
        return str;
    }

    public int getId() {
        return id;
    }

    public String getInfo() {
        return info;
    }

    public String getNotif() {
        return notif;
    }
    
}
