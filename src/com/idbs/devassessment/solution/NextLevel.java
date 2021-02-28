package com.idbs.devassessment.solution;

import com.idbs.devassessment.solution.constants.Constants;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

public class NextLevel extends BaseLevel{
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


    protected String parseEquation(String requiredString) {
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
//        return
        polynomial =  Long.toString(polynomialAnswer);
        return polynomial;
    }


    protected String buildEquation(String xVal, String equation) {
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

    protected Long getPolynomialValue(String xVal, Long polynomial, String plusSepTerm, String operation) {
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
                    // here
                    polynomial = polynomial + finalProd;

                } else if(operation.equalsIgnoreCase(Constants.SUBTRACT)){
                    // here
                    polynomial = polynomial - finalProd;
                }
            }
        }
        return polynomial;
    }


}
