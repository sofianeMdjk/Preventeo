/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Managers;

import Managers.FoodManager;
import GeneralRDF.SPARQLQuery;
import com.SY.myQueryFac.QueryGen;
import java.util.LinkedList;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import Managers.FoodManager.Food;
import pfeproject.MainApp;
import pfeproject.RecipeController;
import static Managers.FoodManager.namePredicate;
import static Managers.FoodManager.url;


public class Recipe {
    public static final String namePredicate = "foaf:name";
    public static final String prefixIng = "http://world-fr.openfoodfacts.org/ingredient/";
    public static Recipe selectedRecipe= null;

    public String getUri() {
        return uri;
    }

    public String getName() {
        return name;
    }

    
   private String uri;
   private String name;
   private LinkedList<RecipeController.RecipeIngredient> ingredients=null;
    
    
    public Recipe(String u,String n)
    {
        this.uri=u;
        this.name=n;
    }
    
    
      public static  LinkedList<Recipe> getAllRecipes(int page)
      {
       
        String query= QueryGen.prefixes +
        "\nSELECT DISTINCT ?subject ?object\n" +
        "WHERE \n" +
        "{\n"
                + "?subject rdf:type food:Recipe." +
        "?subject "+namePredicate+" ?object.\n"
        + "FILTER(?object != \"\" && ?object != \" \" && ?object != \".\")" +
        "}"
        + "ORDERBY strlen(str(?object))";
        QueryGen qg = new QueryGen(url, query, 20);
        SPARQLQuery q =new SPARQLQuery(MainApp.url);
        q.loadQuery(qg.getQuery(page));
        LinkedList<String> vars=new LinkedList<>();
        vars.add("?subject");
        vars.add("?object");
        LinkedList<String> results= q.execSelectAndPrint(vars);
        System.out.println("size = " + results.size());
        LinkedList<Recipe> recipes = new LinkedList<>();
        for (int i=0; i<results.size();i++) 
        {
          String str = results.get(i);
          i++;
          String n = results.get(i);
            System.out.println("str = " + str + " name = " + n);
          recipes.add(new Recipe(str, n));
        }
        System.out.println("size = " + results.size());
        return recipes;
    }
      
      public static LinkedList<Recipe> parseRecipe(String name,int page,int limit)
       {

           String query=QueryGen.prefixes+"\n"+
                        "\nSELECT DISTINCT ?subject ?object\n" +
        "WHERE \n" +
        "{\n"
                + "?subject rdf:type food:Recipe." +
        "?subject "+namePredicate+" ?object.\n" +
                        "FILTER regex(?object, str(\""+name+"\"), \"i\")\n" +
                        "}"
                   + "ORDER BY strlen(str(?object))";
        QueryGen qg = new QueryGen(url, query, limit);
        SPARQLQuery q =new SPARQLQuery(MainApp.url);
        q.loadQuery(qg.getQuery(page));
        LinkedList<String> vars=new LinkedList<>();
        vars.add("?subject");
        vars.add("?object");
        LinkedList<String> results= q.execSelectAndPrint(vars);
        System.out.println("size = " + results.size());
        LinkedList<Recipe> recipes = new LinkedList<>();
        for (int i=0; i<results.size();i++) 
        {
          String str = results.get(i);
          i++;
          String n = results.get(i);
            System.out.println("str = " + str + " name = " + n);
          recipes.add(new Recipe(str, n));
        }
        System.out.println("size = " + results.size());
        return recipes;
       }
      
      
      public LinkedList<RecipeController.RecipeIngredient> getIngredients()
      {
          LinkedList<RecipeController.RecipeIngredient> list =new LinkedList<>();
           String query= QueryGen.prefixes +"SELECT DISTINCT ?name ?uri ?q ?u\n" +
            "WHERE\n" +
            "  { \n" +
            "  	<"+this.uri+"> food:isMadeOf ?y.\n" +
            "    ?y food:food ?uri.\n"
                   + "?uri "+FoodManager.namePredicate+" ?name." +
            "  OPTIONAL { ?y food:quantity ?q.\n" +
                        "  ?y food:unit ?u}\n" +
            "  }\n" ;
           QueryGen qg = new QueryGen(url, query, 1000);
            ResultSet rs;
            if((rs = qg.getPage(1)) != null)
            {
            QuerySolution qs;
            while(rs.hasNext())
            {
                qs = rs.next();
                String uri = qs.get("uri").toString();
                String name=qs.get("name").toString();
                String quantity=qs.get("q").toString();
                String unit=qs.get("u").toString();
                if(quantity==null || quantity.compareTo("")==0)
                    quantity="not specified";
                if(unit==null || unit.compareTo("")==0)
                    unit="not specified";
                
                list.add(new RecipeController.RecipeIngredient(uri, unit, Integer.parseInt(quantity), name));
                
            }
            }
        
          
          return list;
      }
      
      public String toString()
      {
          return this.name;
      }
}
