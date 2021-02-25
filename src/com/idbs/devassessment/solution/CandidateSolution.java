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

        return DifficultyLevel.LEVEL_3;
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
            return handleLevel();
        } else if(getDifficultyLevel() == DifficultyLevel.LEVEL_3){
            return handleLevel();
        } else {
            return "";
        }
    }

    /*
    handles level 2 and 3
    this level has varied inputs
     */
    private String handleLevel() {
        String dataForQuestion = getDataForQuestion();

        if (dataForQuestion.startsWith(Constants.JSON)){
            String requiredString = dataForQuestion.substring(5);
            return getValsFromJSON(requiredString);
        } else if(dataForQuestion.startsWith(Constants.NUMERIC)){
            String requiredString = dataForQuestion.substring(8);//remove "numeric:" from the string
            String[] terms = requiredString.split(Constants.SEMI_COLON);//seperate "x = ..." and "y = ..." and put them in "terms"
            String xVal = "";
            String equation = "";
            for(String t:terms){
                if(t.startsWith(Constants.X_FROM_EQUATION)){
                    xVal = t.substring(4);
                }
                if(t.startsWith(Constants.Y_FROM_EQUATION)){
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
    Extracts values from the equation and builds the polynomial & calls subsequent functions
    Approach : first, we separate the terms by "-" sign. We can straightaway subtract those separated terms.
    Some terms may be grouped together because they have "+" sign in them.
     */
    private String buildEquation(String xVal, String equation) {
        if(xVal != "" && equation != ""){
            Long polynomial = Long.valueOf(0);
            String[] parts = equation.substring(1).split(Constants.HYPHEN);
            //"parts" is seperated by negative signs
            for(String p: parts) {
                if(p.contains(Constants.PLUS_STRING) || p.startsWith(Constants.PLUS_STRING)){
                    String[] plusSepTerms =  p.split(Constants.PLUS_SIGN);
                    for(int k = 0; k < plusSepTerms.length; k++){
                        //add all other terms
                        if(k != 0){
                            //add
                            polynomial = getPolynomialValue(xVal, polynomial, plusSepTerms[k], Constants.ADD);
                        } else {
                            // subtract the first term only because its separated by "-"
                            polynomial = getPolynomialValue(xVal, polynomial, plusSepTerms[k],Constants.SUBTRACT);
                        }
                    }
                } else {
                    //subtract
                    polynomial = getPolynomialValue(xVal, polynomial, p,Constants.SUBTRACT);
                }
            }
            return Long.toString(polynomial);
        }
        return null;
    }

    /*
    helper function
     */
    private Long getPolynomialValue(String xVal, Long polynomial, String plusSepTerm, String operation) {
        String s = plusSepTerm;

        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) == Constants.DOT_OPERATOR){
                String coeff = s.substring(0,i);
                String power = s.substring(s.indexOf(Constants.POWER_OPERATOR)+1);
                Long xValProd = Long.valueOf(1);
                for(int j =1;j<=Integer.parseInt(power); j=j+1){
                    xValProd = multiply(Integer.parseInt(xVal),xValProd);
                }
                Long finalProd = multiply(Integer.parseInt(coeff),xValProd);
                if(operation.equalsIgnoreCase(Constants.ADD)){
                    // here
                    if (getDifficultyLevel() == DifficultyLevel.LEVEL_2) polynomial = polynomial + finalProd;
                    else if (getDifficultyLevel() == DifficultyLevel.LEVEL_3) polynomial = DigitalTaxTracker.add(polynomial, finalProd);

                } else if(operation.equalsIgnoreCase(Constants.SUBTRACT)){
                    // here
                    if (getDifficultyLevel() == DifficultyLevel.LEVEL_2) polynomial = polynomial - finalProd;
                    else if (getDifficultyLevel() == DifficultyLevel.LEVEL_3) polynomial = DigitalTaxTracker.substract(polynomial, finalProd);
                }
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

        Integer xValue = jsonObject.getInt(Constants.X_VALUE);// get the xValue from the Json
        JsonArray jsonArray = jsonObject.getJsonArray(Constants.TERMS);// read the terms array from the json
        if (xValue > 50 || xValue < 0) return "";//check the condition
        if (jsonArray.size() > 10) return "";//check the condition
        Long polynomialAnswer = calculateAnswer(xValue, jsonArray);
        if (polynomialAnswer == null) return "";
        return Long.toString(polynomialAnswer);
    }



    /*
    Method for calcuating answer
    - takes value of x , JsonArray of terms
    - returns long
     */
    private Long calculateAnswer(Integer xValue, JsonArray jsonArray) {
        // now create the polynomial
        Long polynomialAnswer = Long.valueOf(0);

        for (int i = 0; i < jsonArray.size(); i++)
        {
             Integer power = jsonArray.getJsonObject(i).getInt(Constants.POWER);
             Integer multiplier = jsonArray.getJsonObject(i).getInt(Constants.MULTIPLIER);

            if(multiplier > 10 || multiplier < 0 || power < 0) return null;//check the condition
            String action = jsonArray.getJsonObject(i).getString(Constants.ACTION);
            Long xValueProd = Long.valueOf(1);
            for(int j = 1; j <= power; j = j + 1){
                xValueProd = multiply(xValue,xValueProd);
            }
            Long currentTerm = multiply(multiplier, xValueProd);
            if(action.equalsIgnoreCase(Constants.ADD)){
                if (getDifficultyLevel() == DifficultyLevel.LEVEL_3) polynomialAnswer = DigitalTaxTracker.add(polynomialAnswer, currentTerm);
                else polynomialAnswer = polynomialAnswer + currentTerm;
            } else if(action.equalsIgnoreCase(Constants.SUBTRACT)){
                if (getDifficultyLevel() == DifficultyLevel.LEVEL_3) polynomialAnswer = DigitalTaxTracker.substract(polynomialAnswer, currentTerm);
                else polynomialAnswer = polynomialAnswer - currentTerm;
            }
        }
        return polynomialAnswer;
    }



    /*
    generic method for multiplication using addition
     */
    protected Long multiply(Integer multiplier, Long xValueProd) {
        Long added = Long.valueOf(0);
        if(getDifficultyLevel() == DifficultyLevel.LEVEL_3){
            if(isEven(multiplier)){
                for(int i = 1; i <= multiplier; i = i + 2){
                    added = DigitalTaxTracker.add(added, xValueProd + xValueProd);
                }
            } else {
                for(int i = 1; i <= multiplier; i = i + 1){
                    added = DigitalTaxTracker.add(added, xValueProd);
                }
            }
        } else {
            for(int i = 1; i <= multiplier; i = i + 1){
                added = added + xValueProd;
            }
        }
        return added;
    }

    /*
    check if even without modulus operator
     */
    protected Boolean isEven(Integer n) {
        Boolean isEven = true;
        for (int i = 1; i <= n; i = i + 1){
            isEven = !isEven;
        }
        return isEven;
    }

}
