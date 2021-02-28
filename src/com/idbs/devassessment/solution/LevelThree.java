package com.idbs.devassessment.solution;

import com.idbs.devassessment.harness.DigitalTaxTracker;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;


public class LevelThree {
    String polynomial = "";

    public String handleInputType(String dataForQuestion){
        if (dataForQuestion.startsWith(Constants.JSON)){
            String requiredString = dataForQuestion.substring(5);
            return parseEquation(requiredString);

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
            polynomial = buildEquation(xVal, equation);
            if (polynomial != null) return polynomial;
        }
        return "";
    }

    private String parseEquation(String requiredString) {
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
        polynomial =  Long.toString(polynomialAnswer);
        return polynomial;
    }


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
                xValueProd = multiplyTwoNumbers(xValue,xValueProd);
            }
            Long currentTerm = multiplyTwoNumbers(multiplier, xValueProd);
            if(action.equalsIgnoreCase(Constants.ADD)){

                polynomialAnswer = DigitalTaxTracker.add(polynomialAnswer, currentTerm);
            } else if(action.equalsIgnoreCase(Constants.SUBTRACT)){
                polynomialAnswer = DigitalTaxTracker.substract(polynomialAnswer, currentTerm);
            }
        }
        return polynomialAnswer;

//        return null;
    }
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

    private Long getPolynomialValue(String xVal, Long polynomial, String plusSepTerm, String operation) {
        String s = plusSepTerm;

        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) == Constants.DOT_OPERATOR){
                String coeff = s.substring(0,i);
                String power = s.substring(s.indexOf(Constants.POWER_OPERATOR)+1);
                Long xValProd = Long.valueOf(1);
                for(int j =1;j<=Integer.parseInt(power); j=j+1){
                    xValProd = multiplyTwoNumbers(Integer.parseInt(xVal),xValProd);

                }
                Long finalProd = multiplyTwoNumbers(Integer.parseInt(coeff),xValProd);
                if(operation.equalsIgnoreCase(Constants.ADD)){
                    polynomial = DigitalTaxTracker.add(polynomial, finalProd);

                } else if(operation.equalsIgnoreCase(Constants.SUBTRACT)){
                    polynomial = DigitalTaxTracker.substract(polynomial, finalProd);
                }
            }
        }
        return polynomial;
    }

    public Long multiplyTwoNumbers(Integer multiplier, Long xValueProd) {
        Long added = Long.valueOf(0);

            long doubledXValProd = DigitalTaxTracker.add(xValueProd , xValueProd);

            if(isEven(multiplier)){
                for(int i = 1; i <= multiplier; i = i + 2){
                    added = DigitalTaxTracker.add(added, doubledXValProd);
                }
            } else {
                for(int i = 1; i <= multiplier; i = i + 1){
                    added = DigitalTaxTracker.add(added, xValueProd);
                }
            }

        return added;
    }

    protected Boolean isEven(Integer n) {
        Boolean isEven = true;
        for (int i = 1; i <= n; i = i + 1){
            isEven = !isEven;
        }
        return isEven;
    }

}
