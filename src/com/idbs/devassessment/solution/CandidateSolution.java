/*
 * Copyright (C) 1993-2020 ID Business Solutions Limited
 * All rights reserved
 */
package com.idbs.devassessment.solution;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.idbs.devassessment.core.IDBSSolutionException;
import com.idbs.devassessment.core.DifficultyLevel;

/**
 * Example solution for the example question
 */

public class CandidateSolution extends CandidateSolutionBase
{
    @Override
    public DifficultyLevel getDifficultyLevel()
    {
        /*
         * 
         * CHANGE this return type to YOUR selected choice of difficulty level to which you will code an answer to.
         * 
         */

        return DifficultyLevel.LEVEL_2;
    }


    @Override
    public String getAnswer() throws IDBSSolutionException
    {
        /*
         * This is the default solution and provides some example code on how to extract data from Json in java.
         *
         * As an initial start we suggest you comment ALL the code below and return a null value from the method. Run
         * this in the assessment application and you'll see many examples of the Json that question produces.
         */

        String result = handleAllDifficultyLevel();

        return result;
    }



    /*

      extracts terms of equation from given JSON data.
     */
    private String handleAllDifficultyLevel() {
        if(getDifficultyLevel() == DifficultyLevel.LEVEL_1){
            String json = getDataForQuestion();

            // now use the json api to read the json to give a JsonObject representing the Json...
            return getValsFromJSON(json);

        } else if( getDifficultyLevel() == DifficultyLevel.LEVEL_2){
            String dataForQuestion = getDataForQuestion();

            if (dataForQuestion.startsWith("json")){
                String requiredString = dataForQuestion.substring(5);

                return getValsFromJSON(requiredString);

            } else if(dataForQuestion.startsWith("numeric")){
                //remove "numeric:" from the string
                String requiredString = dataForQuestion.substring(8);

                //seperate "x = ..." and "y = ..." and put them in "terms"
                String[] terms = requiredString.split(";");//splits the string based on whitespace
                String xVal = "";
                String equation = "";

                for(String t:terms){
                    if(t.startsWith("x")){
                        xVal = t.substring(4);
                        System.out.println("xVal:" + xVal);
                        System.out.println();
                    }
                    if(t.startsWith(" y")){
                        equation = t.substring(4);
                        System.out.println("e:" + equation);
                        System.out.println();
                    }
                }

                //extracts values from the equation and builds the equation
                if(xVal != "" && equation != ""){
                    long polynomial = 0;

                    String[] parts = equation.substring(1).split("-");

                    /*
                    first, we separate the terms by "-" sign. We can straightaway subtract those separated terms.
                    Some terms may be grouped together because they have "+" sign in them.
                     */
                    //these "parts" are seperated by negative signs
                    for(String p: parts) {
                        System.out.println("separated by negative signs:"+p);
                        if(p.contains("+") || p.startsWith(" +")|| p.startsWith("+")){
                            String[] plusSepTerms =  p.split("\\+");
                            for(int k = 0; k < plusSepTerms.length; k++){
                                //add all other terms
                                if(k != 0){
                                    //add
                                    polynomial = getPolynomialValue(xVal, polynomial, plusSepTerms[k],"add");
                                } else {
                                    // subtract the first term only because its separated by "-"
                                    polynomial = getPolynomialValue(xVal, polynomial, plusSepTerms[k],"subtract");
//                                    String s = plusSepTerms[k];
//                                    System.out.println("plus terms:"+ s);
//
//                                    for(int i = 0; i < s.length(); i++){
//                                        if(s.charAt(i) == '.'){
//                                            String coeff = s.substring(0,i);
//                                            long firstTwoTerms = multiply(Integer.parseInt(coeff),Integer.parseInt(xVal));
//
//                                            System.out.println("coeff"+ coeff);
//
//                                            String power = s.substring(s.indexOf("^")+1);
//                                            System.out.println("power:"+ power);
//
//                                            long xValProd = 1;
//                                            for(int j =1;j<=Integer.parseInt(power); j=j+1){
//                                                xValProd = multiply(Integer.parseInt(xVal),xValProd);
//                                            }
//                                            long finalProd = multiply(Integer.parseInt(coeff),xValProd);
//
//                                            polynomial = polynomial - finalProd;
//                                            System.out.println("polynomial"+polynomial);
//
//                                        }
//
//                                    }
                                }
                            }
                        } else {
                            //subtract
                            polynomial = getPolynomialValue(xVal, polynomial, p,"subtract");
//                            for(int i = 0; i < p.length(); i++){
//                                if(p.charAt(i) == '.'){
//                                    String coeff = p.substring(0,i);
//                                    long firstTwoTerms = multiply(Integer.parseInt(coeff),Integer.parseInt(xVal));
//
//                                    System.out.println("coeff"+ coeff);
//
//                                    String power = p.substring(p.indexOf("^")+1);
//                                    System.out.println("power:"+ power);
//
////                                    long firstTwoProds = multiply(Integer.parseInt(coeff), Long.parseLong(xVal));
//                                    long xValProd = 1;
//                                    for(int j =1;j<=Integer.parseInt(power); j=j+1){
//                                        xValProd = multiply(Integer.parseInt(xVal),xValProd);
//                                    }
//                                    long finalProd = multiply(Integer.parseInt(coeff),xValProd);
////                                    System.out.println("finalProd:" + finalProd);
//
//                                    polynomial = polynomial - finalProd;
//                                    System.out.println("polynomial"+polynomial);
//
//                                }
//                            }
                        }
                    }
                    System.out.println("polynomial just from subtraction:"+ polynomial);
                    System.out.println();
                    return Long.toString(polynomial);
                }
                System.out.println();
                return "";
            }
            return "";
        } else if(getDifficultyLevel() == DifficultyLevel.LEVEL_3){


            return "";
        } else {

            return "";
        }
    }

    private long getPolynomialValue(String xVal, long polynomial, String plusSepTerm, String operation) {
        String s = plusSepTerm;
        System.out.println("plus terms:"+ s);

        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) == '.'){
                String coeff = s.substring(0,i);
//                long firstTwoTerms = multiply(Integer.parseInt(coeff),Integer.parseInt(xVal));

                System.out.println("coeff"+ coeff);

                String power = s.substring(s.indexOf("^")+1);
                System.out.println("power:"+ power);

                long xValProd = 1;
                for(int j =1;j<=Integer.parseInt(power); j=j+1){
                    xValProd = multiply(Integer.parseInt(xVal),xValProd);
                }
                long finalProd = multiply(Integer.parseInt(coeff),xValProd);

                if(operation.equals("add")){
                    polynomial = polynomial + finalProd;
                } else if(operation.equals("subtract")){
                    polynomial = polynomial - finalProd;
                }
                System.out.println("polynomial"+polynomial);
            }
        }
        return polynomial;
    }

    /*
    pulls off value of x and other terms of the equation from a given JSON data
     */
    private String getValsFromJSON(String requiredString) {
        JsonReader reader = Json.createReader(new StringReader(requiredString));
        JsonObject jsonObject = reader.readObject();
        reader.close();

        // now start extracting the data you need from the json....

        // get the xValue from the Json
        int xValue = jsonObject.getInt("xValue");

        // read the terms array from the json
        JsonArray jsonArray = jsonObject.getJsonArray("terms");

        //check the condition
        if (xValue > 50 || xValue < 0)
            return "";
        //check the condition
        if (jsonArray.size() > 10)
            return "";

        Long polynomialAnswer = calculateAnswer(xValue, jsonArray);
        if (polynomialAnswer == null) return "";

        return Long.toString(polynomialAnswer);
    }

    /*
    Method for calcuating answer
    - takes value of x , JsonArray of terms
    - returns long
     */
    private Long calculateAnswer(int xValue, JsonArray jsonArray) {
        // now create the polynomial
        long polynomialAnswer = 0;

        for (int i = 0; i < jsonArray.size(); i++)
        {

             int power = jsonArray.getJsonObject(i).getInt("power");
             int multiplier = jsonArray.getJsonObject(i).getInt("multiplier");

            //check the condition
             if(multiplier > 10 || multiplier < 0 || power < 0)
                 return null;
             String action = jsonArray.getJsonObject(i).getString("action");
//            System.out.println("power of : "+ xValue  +": "+ power);
//            System.out.println("multiplier of x : " + i +"th: "+ multiplier);
//            System.out.println("action of x : " + i +"th: "+ action);
//            System.out.println();
            long xValueProd = 1;
            for(int j = 1; j <= power; j = j + 1){
//                xValueProd = xValueProd * xValue;
                xValueProd = multiply(xValue,xValueProd);
            }
//            System.out.println("xValueProd "+ xValueProd);
            long currentTerm = multiply(multiplier, xValueProd);// multiplier * xValueProd ;
//            System.out.println("currentTerm " + currentTerm);
//            System.out.println();
            if(action.equals("add")){
                polynomialAnswer = polynomialAnswer + currentTerm;
//                System.out.println("added");
//                System.out.println("polynomial after adding: " + polynomialAnswer);
//                System.out.println();
//                System.out.println();
            } else if(action.equals("subtract")){
                polynomialAnswer = polynomialAnswer - currentTerm;
//                System.out.println("Subtracted");
//                System.out.println("polynomial after subtracting: " + polynomialAnswer);
//                System.out.println();
//                System.out.println();
            }
        }
//        System.out.println("polynomialAnswer: " + polynomialAnswer);
//        System.out.println();

//        return Integer.toString(polynomialAnswer);
        return polynomialAnswer;
    }

    /*
    generic method for multiplication using addition
     */
    private long multiply(int multiplier, long xValueProd) {
        long added = 0;
        for(int i = 1; i <= multiplier; i = i + 1){
            added = added + xValueProd;
        }
        return added;
    }

}
