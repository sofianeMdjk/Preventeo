/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.util.LinkedList;

/**
 *
 * @author sofiane
 */
public class SpellCorrector {
    
    //in this inner class, each Ingredient has a rate
    //that represents how close it is to the written word in the search field
     class StringRate
                {
                    private String str;
                    private int rate;

                     public StringRate(String s,int r) 
                        {
                         this.str=s;
                         this.rate=r;
                        }    
                }
    
    
    public int matchPercentage(String s1, String s2)
    {
        //this function return the match rate between 2 words
        //it is based on ; size, letters matching and the order of the letters
        //We give the percentage here
        int SizePercentage=sizeCompare(40, s1, s2);
        int matchPercentage=lettersMatch(40, s1, s2);
        int orderPercentage=lettersOrder(20, s1, s2);
        
        return SizePercentage+matchPercentage+orderPercentage;
    }
    
        
    	private boolean containsChar(String word,char c)
	{
            //if a word contains a character
		boolean contains=false;
		int i=0;
		while(contains==false && i<word.length())
		{
			if(word.charAt(i)==c)
				contains=true;
			else
				i++;
		}
		return contains;
	}
	
	private int sizeCompare(int coefValue,String s1,String s2)
	{
		
		if(s1!=null && s2!=null)
		{
                  
		int a=s1.length(),b=s2.length();   
		int difference = Math.abs(a-b);
		int percentage=100;
                
		percentage=percentage-difference*20;
		if(percentage<0)
			percentage=0;
		
		return coefValue *percentage /100;
		}
		else
			return 0;
	}
	private int lettersMatch(int coefValue ,String s1,String s2)
	{
		if(s1!=null && s2!=null)
		{
		int a=s1.length(),b=s2.length();
                 if(b==0)
                    return 0; 
                 
		char allCharactersInOriginal[]=new char[a];
		for(int i=0;i<a;i++)
		{
			allCharactersInOriginal[i]=s1.charAt(i);
		}
		
		char comparedWordCharacters[]=new char[b];
		for(int i=0;i<b;i++)
		{
			comparedWordCharacters[i]=s2.charAt(i);
		}
		int differences=0;
		for(int i=0;i<b;i++)
		{
			if(! containsChar(s1, s2.charAt(i)))
				differences++;
		}

		int pourcentage = 100;

		pourcentage=pourcentage-differences*20;
		if(pourcentage<0)
			pourcentage=0;
		return coefValue* pourcentage/100;
		}
		else
			return 0;
	}
	private int lettersOrder(int coefValue, String s1,String s2)
	{
		if(s1!=null && s2!=null)
		{
                    
		int a=s1.length(),b=s2.length();
                if(b==0)
                {
                    
                    return 0;
                }
		char originalTab[]=new char[a];
		char compareTab[]=new char[b];
		for(int i=0;i<a;i++)
			originalTab[i]=s1.charAt(i);
		for(int i=0;i<b;i++)
			compareTab[i]=s2.charAt(i);
		
		int i=0,j=0,diffrences=0;
		while(i<a && j<b)
		{
			if(originalTab[i]!=compareTab[j])
				diffrences++;
			
			i++;
			j++;
		}

		int percentage =100;
		percentage-= diffrences*20;
		if(percentage<0)
			percentage=0;
		return coefValue * percentage/100;
		}
		else
		return 0;
	}
        
        
        
                public String[] getCloseMatches(String word,LinkedList<String> list,int nbr)
                {
                    int size=list.size();
                    LinkedList<StringRate> listRate=new LinkedList<>();
                    String temp;
                    for(int i=0;i<size;i++)
                    {
                        temp=list.get(i);
                        listRate.add(new StringRate(temp, matchPercentage(word, temp)));
                    }
                    String tab[]= getBestRates(listRate, nbr);
                    return tab;
                }
                private StringRate getMax(LinkedList<StringRate> list)
                     {
                         StringRate max=null;
                         int temp;
                         int size=list.size();
                         if(size>0)
                             max=list.get(0);
                         
                         for(int i=1;i<size;i++)
                         {
                             if(list.get(i).rate>max.rate)
                                 max=list.get(i);
                         }
                         return max;
                     }
                
                private String[] getBestRates(LinkedList<StringRate> list , int nbr)
                     {
                         String tab[]=new String[nbr];
                         int size=list.size();
                         StringRate maxElement =null;
                         for(int i=0;i<nbr;i++)
                         {
                             maxElement=getMax(list);
                             tab[i]=maxElement.str;
                             list.remove(maxElement);
                         }
                         return tab;
                     }
}
