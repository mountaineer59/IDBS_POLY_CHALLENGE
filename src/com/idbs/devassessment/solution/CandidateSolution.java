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
import com.idbs.devassessment.harness.DigitalTaxTracker;

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

            return getValsFromJSON(json);

        } else if( getDifficultyLevel() == DifficultyLevel.LEVEL_2){

            return handleLevelTwo();
        } else if(getDifficultyLevel() == DifficultyLevel.LEVEL_3){
            /*
            add here
             */


            /*
            till here
             */
            return "";
        } else {

            return "";
        }
    }

    /*
    handles level 2
    this level has varied inputs
     */
    private String handleLevelTwo() {
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
                }
                if(t.startsWith(" y")){
                    equation = t.substring(4);
                }
            }
            String polynomial = buildEquation(xVal, equation);
            if (polynomial != null) return polynomial;
            return "";
        }
        return "";
    }

    /*
    extracts values from the equation and builds the polynomial & calls subsequent functions
     */
    private String buildEquation(String xVal, String equation) {
        if(xVal != "" && equation != ""){
            long polynomial = 0;

            String[] parts = equation.substring(1).split("-");

            /*
            first, we separate the terms by "-" sign. We can straightaway subtract those separated terms.
            Some terms may be grouped together because they have "+" sign in them.
             */

            //"parts" is seperated by negative signs
            for(String p: parts) {
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
                        }
                    }
                } else {
                    //subtract
                    polynomial = getPolynomialValue(xVal, polynomial, p,"subtract");
                }
            }
            return Long.toString(polynomial);
        }
        return null;
    }

    /*
    helper function
     */
    private long getPolynomialValue(String xVal, long polynomial, String plusSepTerm, String operation) {
        String s = plusSepTerm;

        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) == '.'){
                String coeff = s.substring(0,i);
                String power = s.substring(s.indexOf("^")+1);
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
                System.out.println("polynomial" + polynomial);
            }
        }
        return polynomial;
    }

    /*
    pulls off value of x and other terms of the equation from a given JSON data
     */
    private String getValsFromJSON(String requiredString) {
        //use the json api to read the json to give a JsonObject representing the Json...
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
            long xValueProd = 1;
            for(int j = 1; j <= power; j = j + 1){
                xValueProd = multiply(xValue,xValueProd);
            }

            long currentTerm = multiply(multiplier, xValueProd);
            if(action.equals("add")){
                polynomialAnswer = polynomialAnswer + currentTerm;
            } else if(action.equals("subtract")){
                polynomialAnswer = polynomialAnswer - currentTerm;
//                DigitalTaxTracker.substract(polynomialAnswer,currentTerm);

            }
        }
        return polynomialAnswer;
    }



    /*
    generic method for multiplication using addition
     */
    private long multiply(int multiplier, long xValueProd) {
        long added = 0;
        for(int i = 1; i <= multiplier; i = i + 1){
            added = added + xValueProd;
//            if(getDifficultyLevel() == DifficultyLevel.LEVEL_3)
//                DigitalTaxTracker.add(multiplier, xValueProd);
        }
        return added;
    }

}
