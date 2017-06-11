/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Managers;


import Managers.Admin;
import GeneralRDF.SPARQLQuery;
import GeneralRDF.Triplet;
import com.SY.myQueryFac.QueryGen;
import com.SY.myQueryFac.UpdateQuery;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import Managers.FoodManager.Food;
import pfeproject.MainApp;

/**
 *
 * @author sofiane
 */
public abstract class DiseaseManager {
    private static Disease selectedDisease; 
    private static HashMap<String,LinkedList<String>> listOfDiseases=new HashMap<>();
    private static String prefixD = "http://www.semanticweb.org/admin/rdf-base/Illnesses/";
    public static class Disease
    {
        public String uri;
        public String name;
        public String type;

        public Disease(String uri,String name, String type) 
        {
            this.uri = uri;
            this.name = name;
            this.type = type;
        }
        @Override
        public String toString()
        {
            return name;
        }
        
        public LinkedList<Food> getCauses()
        {
            LinkedList<String> causes = new LinkedList<>();
            String query=
                    QueryGen.prefixes+"\n"+
                    " SELECT ?x ?name\n" +
                    "WHERE {\n"
                   + "<"+uri+"> food:provokedBy ?y.\n"
               + "?y " + FoodManager.namePredicate + " ?name.\n"
                + "}";
        
        SPARQLQuery q = new SPARQLQuery(MainApp.url);
        q.loadQuery(query);
        LinkedList<String> vars=new LinkedList<>();
        vars.add("x");
        vars.add("name");
        LinkedList<String> results=q.execSelectAndPrint(vars);
        LinkedList<Food> foods = new LinkedList<>();
        int size = results.size();
        for (int i=0;i<size-1;i+=2) 
        {
            foods.add(new Food(results.get(i), results.get(i+1)));
        }
        return foods;
        }
    }
    
    public static void addDisease(String name,String type,LinkedList<String> list, LinkedList<Food> causes,String url) throws Exception
    {
        System.out.println("causes: " + causes.size());
        String uri = "<"+prefixD+MainApp.formatString(name)+">";
        UpdateQuery uq = new UpdateQuery(QueryGen.prefixes);
        uq.addTriplet(new Triplet(uri, "rdf:type", "food:"+type));
        uq.addTriplet(new Triplet(uri, FoodManager.namePredicate, "\""+name+"\""));
        for (Food cause : causes) 
        {
          uq.addTriplet(new Triplet(uri, "food:provokedBy", "<"+cause.uri+">"));
        }
//        uq.applyQuery(url, uq.getQuery());
          if(!  Admin.isAdmin)
          {
           DataBaseAccess dba = 
                new DataBaseAccess("jdbc:mysql://localhost:3306/informations", "root", "Resssay95");
           String str = generateInfos(name, list);
           dba.addNotification(str, uq.getQuery());
          }
          else
           uq.applyQuery(uq.getQuery());
    }
    
    static private String generateInfos(String NameField,LinkedList<String> list)
    {
        String info="1;"+NameField+";";
        String causes="";
        int i=0;
        for( i=0;i<list.size()-1;i++)
        {
            causes+=list.get(i)+",";
        }
        causes += list.get(i)+";";
        info+=causes;
                return info;
    }
 
    
    public static boolean alreadyExists(String disease)
    {
        String query=
                    QueryGen.prefixes+"\n"+
                    " SELECT ?subject\n" +
                    "WHERE {\n"
                + "{" +
                    "  ?x rdf:type food:Disease.\n" +
                    "  ?x "+FoodManager.namePredicate+" ?subject.\n" +
                    "  FILTER" + " (str(?subject) = \""+disease+"\")\n" +
                    "}"
                + "UNION"
                + "{"
                    + "?x rdf:type food:Allergy.\n" +
                    "  ?x "+FoodManager.namePredicate+" ?subject.\n" +
                    "  FILTER"
                + " (str(?subject) = \""+disease+"\")\n" +
                "}"
                + "}";
        SPARQLQuery q = new SPARQLQuery(MainApp.url);
        q.loadQuery(query);
        LinkedList<String> vars=new LinkedList<>();
        vars.add("subject");
        LinkedList<String> results=q.execSelectAndPrint(vars);
        
        return results.size()>0;
    }
//    public static LinkedList<String> getDiseaseCauses(String disease)
//    {
//        return listOfDiseases.get(disease);
//    }
//    public static boolean isEmpty()
//    {
//        String query=
//                    QueryGen.prefixes+"\n"+
//                    " SELECT ?x\n" +
//                    "WHERE {\n"
//                + "{" +
//                    "  ?x rdf:type food:Disease.\n"
//                + "UNION"
//                + "{"
//                + "?x rdf:type food:Allergy.\n"
//                + "}"
//                + "}";
//        
//        SPARQLQuery q = new SPARQLQuery(MainApp.url);
//        q.loadQuery(query);
//        LinkedList<String> vars=new LinkedList<>();
//        vars.add("x");
//        LinkedList<String> results=q.execSelectAndPrint(vars);
//        
//        return results.isEmpty();
//
//    }
    
    public static LinkedList<Disease> getD()
    {
       String query= QueryGen.prefixes+"\n"+
                    " SELECT ?x ?name\n" +
                    "WHERE {\n"
                    +"  ?x rdf:type food:Disease.\n"
               + "?x " + FoodManager.namePredicate + " ?name.\n"
                + "}";
        
        SPARQLQuery q = new SPARQLQuery(MainApp.url);
        q.loadQuery(query);
        LinkedList<String> vars=new LinkedList<>();
        vars.add("x");
        vars.add("name");
        LinkedList<String> results=q.execSelectAndPrint(vars);
        LinkedList<Disease> ds = new LinkedList<>();
        int size = results.size();
        for (int i=0;i<size-1;i+=2) 
        {
            ds.add(new Disease(results.get(i), results.get(i+1), "Disease"));
        }
        
        query= QueryGen.prefixes+"\n"+
                    " SELECT ?x ?name\n" +
                    "WHERE {\n"
                    +"  ?x rdf:type food:Allergy.\n"
               + "?x " + FoodManager.namePredicate + " ?name.\n"
                + "}";
        
        q.loadQuery(query);
        results=q.execSelectAndPrint(vars);
        size = results.size();
        for (int i=0;i<size-1;i+=2) 
        {
            ds.add(new Disease(results.get(i), results.get(i+1), "Allergy"));
        }
        return ds;
    }
    
//     public static HashMap<String,LinkedList<String>> getList()
//     {
//         return listOfDiseases;
//     }

    public static Disease getSelectedDisease() {
        return selectedDisease;
    }

    public static void setSelectedDisease(Disease selectedDisease) {
        DiseaseManager.selectedDisease = selectedDisease;
    }
    
//    public static void modifyDisease(String disease,LinkedList<String> ingredients)
//    {
//        listOfDiseases.put(disease, ingredients);
//    }
     
}
