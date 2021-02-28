package com.idbs.devassessment.solution;

import com.idbs.devassessment.solution.constants.Constants;
import com.idbs.devassessment.solution.interfaces.PolynomialSolver;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

public class LevelOne extends BaseLevel implements PolynomialSolver {
    String polynomialAnswerString = "";

    @Override
    public String parseEquation(String requiredString) {
        //use the json api to read the json to give a JsonObject representing the Json...

        if(requiredString.isEmpty() || requiredString == ""|| requiredString.equalsIgnoreCase("")){
           return Constants.ERROR_MESSAGE;
        }

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

        polynomialAnswerString =  Long.toString(polynomialAnswer);
        return polynomialAnswerString;
    }


}
