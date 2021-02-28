package com.idbs.devassessment.solution;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

public class LevelOne implements PolynomialSolver{
    String polynomialAnswerString = "";
    @Override
    public String parseEquation(String requiredString) {
        //use the json api to read the json to give a JsonObject representing the Json...
        JsonReader reader = Json.createReader(new StringReader(requiredString));
        JsonObject jsonObject = reader.readObject();
        reader.close();

        // now start extracting the data you need from the json....

        Integer xValue = jsonObject.getInt(Constants.X_VALUE);// get the xValue from the Json
        JsonArray jsonArray = jsonObject.getJsonArray(Constants.TERMS);// read the terms array from the json
        if (xValue > 50 || xValue < 0) return "";//check the condition
        if (jsonArray.size() > 10) return"";//check the condition
        Long polynomialAnswer = calculateAnswer(xValue, jsonArray);
        if (polynomialAnswer == null) return "";
//        return
        polynomialAnswerString =  Long.toString(polynomialAnswer);
        return polynomialAnswerString;
    }

    @Override
    public Long calculateAnswer(Integer xValue, JsonArray jsonArray) {
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
                    polynomialAnswer = polynomialAnswer + currentTerm;
            } else if(action.equalsIgnoreCase(Constants.SUBTRACT)){
                    polynomialAnswer = polynomialAnswer - currentTerm;
            }
        }
        return polynomialAnswer;
    }

    @Override
    public Long multiplyTwoNumbers(Integer multiplier, Long xValueProd) {
        Long added = Long.valueOf(0);
            for(int i = 1; i <= multiplier; i = i + 1){
                added = added + xValueProd;
            }
        return added;
    }
}
