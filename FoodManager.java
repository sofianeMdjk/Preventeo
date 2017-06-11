/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Managers;


import GeneralRDF.RDFNode;
import GeneralRDF.Triplet;
import GeneralRDF.SPARQLQuery;
import com.SY.myQueryFac.QueryGen;
import com.SY.myQueryFac.UpdateQuery;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.html.HTML;
import Managers.DiseaseManager.Disease;
import Utils.SpellCorrector;
import pfeproject.MainApp;

/**
 *
 * @author sofiane
 */

public class FoodManager {
    
    public static final String namePredicate = "foaf:name";
    public static final String originPredicate = "food:origin";//"food:hasOrigin";
    public static final String prefixIng = "http://world-fr.openfoodfacts.org/ingredient/";
    public static final String prefixPro = "http://world-fr.openfoodfacts.org/produit/";
    
    static public class Food extends RDFNode
    {
        public String name = "name";
        public String code = null;
        public String origin = null;
        public Food(String uri, SPARQLQuery querier) 
        {
            super(uri, querier);
            getName();
        }
        
        public Food(String uri, String n) 
        {
            super(uri);
            name = n;
        }
        
        public String getName()
        {
            
            String q = QueryGen.prefixes+
                       "\nSELECT ?n\n"
                + "WHERE {"
                + "<"+uri+"> "+namePredicate+" ?n"
                + "}";
            LinkedList<String> varsToPrint = new LinkedList<>();
            varsToPrint.add("?n");
            if(querier == null) querier = new SPARQLQuery(MainApp.url);
            LinkedList<String> l = querier.execSelectAndPrint(varsToPrint, q);
            if(l.size() != 1) System.out.println("pfeproject.FoodManager.Food.getName: something went Wrong!");
            
            name = l.get(0);
            return name;
        }
        
        public LinkedList<String> getProvokedDiseases()
        {
            String q = QueryGen.prefixes+
                       "\nSELECT DISTINCT ?name\n"
                + "WHERE {"
                    + "{"
                + "<"+uri+"> food:containsIngredient ?n."
                    + " ?n food:food ?y."
                    + " ?x food:provokedBy ?y."
                    + " ?x "+namePredicate+ " ?name"
                    + "}"
                    + "UNION"
                    + "{"
                    + " ?x food:provokedBy <"+uri+">."
                    + " ?x "+namePredicate+ " ?name"
                    + "}"
                + "}";
            LinkedList<String> varsToPrint = new LinkedList<>();
            varsToPrint.add("?name");
            if(querier == null) querier = new SPARQLQuery(MainApp.url);
            LinkedList<String> l = querier.execSelectAndPrint(varsToPrint, q);
            return l;
        }

        @Override
    public String toString()
    {
        return name;
    }
        
    }
   
     public static String url;
     public SPARQLQuery q=null;
     
     
     
    public static Food selectedFood= null;
    public FoodManager(String url)
    {
        this.url=url;
       q=new SPARQLQuery(url);
    }
    
    public LinkedList<Food> getIngredientsThatContainsOtherIngredients(String name)
    {
        //from here we get the ingredients that contain the selected word
            //It's helpful when you write sucre for example
            //you get from here sucre, glucose-fructose-et-ou-sucre etc...
            
        String query=
                    QueryGen.prefixes+"\n"+
                    " SELECT DISTINCT ?x ?name\n" +
                    "WHERE {\n"
                + "{" +
                    "?x rdf:type food:Food.\n"
                + "?x "+namePredicate+" ?name." +
"  OPTIONAL\n" +
"  {\n" +
"    ?x food:doesntContain ?n2\n" +
"  }\n"
                + "  FILTER ( (regex(?name , \""+name+"\" ,\"i\") && (!bound(?n2) || !regex(?n2 , \""+name+"\" ,\"i\"))))"
                + "}"
                + "UNION"
                + "{" +
                "?x rdf:type food:FoodProduct.\n"
                + "?x "+namePredicate+" ?name." +
"  OPTIONAL\n" +
"  {\n" +
"    ?x food:doesntContain ?n2\n" +
"  }\n" +
"  FILTER ( (regex(?name , \""+name+"\" ,\"i\") && (!bound(?n2) || !regex(?n2 , \""+name+"\" ,\"i\"))))"
                + "}"
+  "}";
        q.loadQuery(query);
        LinkedList<String> vars=new LinkedList<>();
        vars.add("x");
        vars.add("name");
        LinkedList<String> results=q.execSelectAndPrint(vars);
        LinkedList<Food> foods = new LinkedList<>();
        for (int i=0; i<results.size();i++) 
        {
          String str = results.get(i);
          i++;
          String n = results.get(i);
          foods.add(new Food(str, n));
        }
        
        
        if(foods.isEmpty()) foods.add(new Food(prefixIng+MainApp.formatString(name),name));
        return foods;
    }
    
    public LinkedList<String> alimentsThatContainIngredient(Food ingredient)
            {
                  
            LinkedList<String> allResults=new LinkedList<>();  
            //We get the name of the foods that contain the ingredient
            String mainQuery =   QueryGen.prefixes+"\n"+
                     "SELECT ?name\n" +
                    "WHERE {\n" +
                    "  ?x food:name ?name.\n" +
                    "  ?x food:containsIngredient ?y.\n" +
                    "  ?y food:food <"+ingredient.uri+">\n" +
                    "}";   
             q.loadQuery(mainQuery);
             //we load the new variables
             
             LinkedList<String> vars=new LinkedList<>();
             vars.add("name");
             LinkedList<String> results=q.execSelectAndPrint(vars);
             
             for(int j=0;j<results.size();j++)
                 allResults.add(results.get(j));
            
            return allResults;
            }
    
    
        public  LinkedList<Triplet> getFoodDetails(Food food)
        {
        
        //for a food we select all the relations
        String query =QueryGen.prefixes+"\n"+
        "SELECT ?x ?y\n" +
        "WHERE\n" +
        "{\n" +
        "  <"+food.uri+"> ?x ?y\n" +
        "  FILTER (?x!= food:IngredientListAsText)\n" +
        "  FILTER (?x!= food:containsIngredient)\n" +
        "  FILTER (?x!= rdf:type)\n" +
        "  FILTER (?x!= food:doesntContain)\n" +
        "  FILTER (?x!= "+originPredicate+")\n" +
        "}";
        q.loadQuery(query);
        LinkedList<String> vars=new LinkedList<>();
        
        //Now we laod 2 variables
        vars.add("x");
        vars.add("y");
        LinkedList<String> results= q.execSelectAndPrint(vars);
        LinkedList<Triplet> list= new LinkedList<Triplet>();
        Pattern pattern = Pattern.compile("http://data.lirmm.fr/ontologies/food#(.*)");
        int i=0;
        String predicat=null;
        String value=null;
        
        while (i<results.size())
        {
            //We get the result of the first variable
            predicat=results.get(i);
            Matcher matcher= pattern.matcher(predicat);
                  if(matcher.find())
                  {
                    predicat=matcher.replaceAll("$1");
            i++;
            //we get the result of the second variable
            value=results.get(i);
            i++;
            list.add(new Triplet(predicat,value));
                  }
                  else
                  {
                      i++;
                      i++;
                  }
           
        }
        
        return list;
    }
    
    public  LinkedList<String> getIngredientsNames(int page)
    {
        //with this we get the list of all the ingredients to use the spellcorrector 
        String query=QueryGen.prefixes+"\n"+
        "SELECT ?ingredient\n" +
        "WHERE "
      + "{\n" +
        "  ?x food:containsIngredient ?y.\n" +
        "  ?y food:food ?o.\n"
      + "  ?o "+namePredicate + " ?ingredient"+
        "}";
        QueryGen qg = new QueryGen(url, query, page);
            q.loadQuery(qg.getQuery(page));
            LinkedList<String> vars =new LinkedList<String>();
            vars.add("ingredient");
            
                LinkedList<String> allIngredients=
                    q.execSelectAndPrint(vars);
//                
//                //We apply a regexp on the ingredients
//                int size=allIngredients.size();
//                
//                Pattern pattern = Pattern.compile(prefixIng+"(.*)");
//                  for(int i=0;i<size;i++)
//                    {          
//                    Matcher matcher= pattern.matcher(allIngredients.get(i)); 
//                    allIngredients.set(i,matcher.replaceAll("$1"));
//                      }
                return allIngredients;    
    }
    
     public LinkedList<Food> parseFood(String name,int page,int limit)
       {
           
           LinkedList<String> vars=new LinkedList<>();
           vars.add("food");
           vars.add("name");
           
           
           String query=QueryGen.prefixes+"\n"+
                        "SELECT distinct ?food ?name\n" +
                        "where\n" +
                        "{\n"
                   + "?food rdf:type food:FoodProduct." +
                        "?food "+namePredicate+" ?name\n" +
                        "FILTER regex(?name, str(\""+name+"\"), \"i\")\n" +
                        "}"
                   + "ORDER BY strlen(str(?name))";
//           q.loadQuery(query);
           QueryGen qg = new QueryGen(url, query, limit);
           q.loadQuery(qg.getQuery(page));
           
           LinkedList<String> results=q.execSelectAndPrint(vars);
           LinkedList<Food> foods = new LinkedList<>();
           for (int i=0; i<results.size();i++) 
        {
          String str = results.get(i);
          i++;
          String n = results.get(i);
          foods.add(new Food(str, n));
        }
           return foods;
       }
     
    public  LinkedList<Food> getAllFood(int page)
    {
       
        String query= QueryGen.prefixes +
        "\nSELECT DISTINCT ?subject ?object\n" +
        "WHERE \n" +
        "{\n"
                + "?subject rdf:type food:FoodProduct." +
        "?subject "+namePredicate+" ?object.\n"
        + "FILTER(?object != \"\" && ?object != \" \" && ?object != \".\")" +
        "}"
        + "ORDERBY ?object";
        QueryGen qg = new QueryGen(url, query, 20);
        q.loadQuery(qg.getQuery(page));
        LinkedList<String> vars=new LinkedList<>();
        vars.add("?subject");
        vars.add("?object");
        LinkedList<String> results= q.execSelectAndPrint(vars);
        System.out.println("size = " + results.size());
        LinkedList<Food> foods = new LinkedList<>();
        for (int i=0; i<results.size();i++) 
        {
          String str = results.get(i);
          i++;
          String n = results.get(i);
            System.out.println("str = " + str + " name = " + n);
          foods.add(new Food(str, n));
        }
        System.out.println("size = " + results.size());
        return foods;
    }
    
    public String[] getListOfSuggestions(String aliment,int number)
    {
        //This function gives us a list of suggestions in case
        //the results of ta search are null
        
        LinkedList<String> list=getIngredientsNames(1);  
        SpellCorrector s=new SpellCorrector();
        String tab[]=s.getCloseMatches(aliment, list, number);
       
        return tab;
    }
    
    public LinkedList getIngredientsOfAfood(Food food)
    {
       
        String query=QueryGen.prefixes+"\n"+
        "SELECT ?name \n" +
        "WHERE \n" +
        "{\n" +
        "<"+food.uri+"> food:containsIngredient ?y.\n" +
        "  ?y food:food ?i.\n"
        + "?i " + namePredicate + " ?name" +
        "}";
        q.loadQuery(query);
        LinkedList<String> vars=new LinkedList<>();
        vars.add("name");
        LinkedList<String> allIngredients= q.execSelectAndPrint(vars);
//        int size=allIngredients.size();
//        allIngredients=getIngredientName(allIngredients);
                
//                Pattern pattern = Pattern.compile("http://fr.openfoodfacts.org/ingredient/(.*)");
//                  for(int i=0;i<size;i++)
//                    {          
//                    Matcher matcher= pattern.matcher(allIngredients.get(i)); 
//                    allIngredients.set(i,matcher.replaceAll("$1"));
//                      }
                  
        return allIngredients;
    }
    public int getFoodOrigin(Food food)
    {
        String query=QueryGen.prefixes+"\n"+
        "SELECT ?origin \n" +
        "WHERE \n" +
        "{\n" +
        "  <"+food.uri+"> "+originPredicate+" ?origin\n" +
        "}";

        q.loadQuery(query);
        LinkedList<String> vars=new LinkedList<>();
        vars.add("origin");
       
        LinkedList<String> res= q.execSelectAndPrint(vars);
        if(res.size()>0)
        {
            if(res.get(0).compareTo("other")==0)
                return 0;
            else if(res.get(0).compareTo("Animal")==0)
                return 1;
            else if(res.get(0).compareTo("Vegetal")==0)
                return 2;
        }
        return 0;
        
    }
    
    //for example if  a food contains sucre
    //or "xxxsucrexxx" then it causes diabete
  
//    public LinkedList<String> foodProvokes(LinkedList<String> ingredients)
//    {
//        LinkedList<String> diseases=new LinkedList<>();
//        String query= QueryGen.prefixes+"\n"+
//                    " SELECT DISTINCT ?name\n" +
//                    "WHERE {\n"
//                + "{" +
//                    "  ?x food:provokedBy ?y.\n"
//               + "?x" + FoodManager.namePredicate + "?name.\n"
//                + "FILTER regex(?y, str(\""+name+"\"), \"i\")\n"
//                + "}";
//        
//        SPARQLQuery q = new SPARQLQuery(MainApp.url);
//        q.loadQuery(query);
//        LinkedList<String> vars=new LinkedList<>();
//        vars.add("x");
//        vars.add("name");
//        LinkedList<String> results=q.execSelectAndPrint(vars);
//        return diseases;
//    }
    
    
    
    //Here you can also find things like
    //Diabete is caused by "sucre"
    //pepsi doesn't contain "sucre" it contains "xxxsucrexxx"
    //So pepsi causes diabete
    //which is pretty cool :D
    public LinkedList<Food> getFoodThatCauses(Disease disease,int page,int limit)
    {
        LinkedList<String> vars=new LinkedList<>();
           vars.add("food");
           vars.add("name");
           
           
           String query=QueryGen.prefixes+"\n"+
                        "SELECT distinct ?food ?name\n" +
                        "where\n" +
                        "{\n"
                   + "{"
                   + "?food rdf:type food:FoodProduct."
                   + "?food "+namePredicate+" ?name." +
                        "?food food:containsIngredient ?ing."
                   + "?ing food:food ?y."
                   + "<"+disease.uri+"> food:provokedBy ?y."
                   + "}"
                   + "UNION"
                   + "{"
                   + "?food rdf:type food:FoodProduct." 
                   + "?food "+namePredicate+" ?name."
                   + "<"+disease.uri+"> food:provokedBy ?food."
                   + "}"+
                        "}";
           QueryGen qg = new QueryGen(url, query, limit);
           q.loadQuery(qg.getQuery(page));
           
           LinkedList<String> results=q.execSelectAndPrint(vars);
           LinkedList<Food> foods = new LinkedList<>();
           for (int i=0; i<results.size();i++) 
        {
          String str = results.get(i);
          i++;
          String n = results.get(i);
          foods.add(new Food(str, n));
        }
           return foods;
    }
       
       
       private LinkedList<String> temp()
       {
           return null;
       }
       
       public LinkedList<String> getIngredientsNames(LinkedList<Food> list)
       {
           LinkedList<String> temp=new LinkedList<>();
           int size = list.size();
                  for(int i=0;i<size;i++)
                    {          
                        temp.add(list.get(i).name);
                      }
           return temp;
       }
   
       public LinkedList<String> getIngredientName(LinkedList<String> list)
       {
           LinkedList<String> temp=list;
           
           int size=temp.size();
           Pattern pattern = Pattern.compile("http://fr.openfoodfacts.org/ingredient/(.*)");
                  for(int i=0;i<size;i++)
                    {          
                    Matcher matcher= pattern.matcher(temp.get(i)); 
                    temp.set(i,matcher.replaceAll("$1"));
                      }
           return temp;
       }
       public LinkedList<String> getRessourceIngredient(LinkedList<String> list)
       {
           LinkedList<String> temp=new LinkedList<>();
           int size=list.size();
           String tempS="";
           for(int i=0;i<size;i++)
           {
               tempS="http://fr.openfoodfacts.org/ingredient/"+list.get(i);
               temp.add(tempS);
               tempS="";
           }
           return temp;
       }
       
       public static void addFood(Food food,LinkedList<String> ings,String pref) throws Exception
       {
           UpdateQuery uq = new UpdateQuery(QueryGen.prefixes);
           String fooduri = "<" + food.uri + ">";
           uq.addTriplet(new Triplet(fooduri, "rdf:type", "food:FoodProduct"));
           uq.addTriplet(new Triplet(fooduri, namePredicate, "\""+food.name+"\""));
           if(food.origin != null)
               uq.addTriplet(new Triplet(fooduri, originPredicate, "\""+food.origin+"\""));
           if(food.code != null)
               uq.addTriplet(new Triplet(fooduri, "food:code", "\""+food.code+"\""));
           int i=0;
           LinkedList<Food> fs = getFoodsFromNames(ings,pref);
           for(Food f : fs)
           {
               String furi = "<" + f.uri + ">";
               //uq.addTriplet(new Triplet(furi, "rdf:type", "food:Food"));
               uq.addTriplet(new Triplet(fooduri, "food:containsIngredient", "_:b"+i));
               uq.addTriplet(new Triplet("_:b"+i, "food:food", furi));
               i++;
           }
           if(!Admin.isAdmin)
           {
           DataBaseAccess dba = new DataBaseAccess("jdbc:mysql://localhost:3306/informations", "root", "Resssay95");
           String str = generateInfos(food.name, food.code, food.origin, ings);
           dba.addNotification(str, uq.getQuery());
           System.out.println(uq.getQuery());
           }
           else
            uq.applyQuery(uq.getQuery());
           
           
       }
       
       static public String generateInfos(String name,String foodCode, String foodOrigin, LinkedList<String> ingredients)
    {
        String info="0;"+name+";";
        if(foodCode != null && foodCode.compareTo("")!=0)
            info+=foodCode+";";
        else
            info+="?;";
        if(foodOrigin != null && foodOrigin.compareTo("")!=0)
            info+=foodOrigin+";";
        else
            info+="?;";
        
        String ing="";
        if(ingredients.isEmpty())
        {
            info+="?;";
        }
        else
        {
            int i=0;
            for(i=0;i<ingredients.size()-1;i++)
                ing+=ingredients.get(i)+",";
            
            ing+=ingredients.get(i);
            info+=ing+";";
            
        }
        return info;
    }
       
       static public LinkedList<Food> getFoodsFromNames(LinkedList<String> list,String pref)
       {
           LinkedList<Food> foods = new LinkedList<>();
           for (String string : list) {
               foods.add(getFoodFromName(string,pref));
           }
           return foods;
       }
       static public Food getFoodFromName(String name,String pref)
       {
           LinkedList<String> vars=new LinkedList<>();
           vars.add("food");           
           String query=QueryGen.prefixes+"\n"+
                        "SELECT distinct ?food\n" +
                        "where\n" +
                        "{\n" +
                        "?food "+namePredicate+" ?name\n" +
                        "FILTER regex(?name, str(\""+name+"\"), \"i\")\n" +
                        "}"
                   + "ORDER BY strlen(str(?name))";
           QueryGen qg = new QueryGen(url, query, 1);
           SPARQLQuery q=new SPARQLQuery(url);
           q.loadQuery(qg.getQuery(1));
           
           LinkedList<String> results=q.execSelectAndPrint(vars);
           Food food;
           if(results.size()>0)
           {
          String str = results.get(0);
          food = new Food(str, name);
           }
           else
           food = new Food(pref+MainApp.formatString(name),name);
          
          
           return food;
       }
    
}
