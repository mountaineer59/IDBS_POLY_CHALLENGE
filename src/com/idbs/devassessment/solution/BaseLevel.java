package com.idbs.devassessment.solution;

import com.idbs.devassessment.solution.constants.Constants;

import javax.json.JsonArray;

public class BaseLevel {

    protected Long calculateAnswer(Integer xValue, JsonArray jsonArray) {
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

    protected Long multiplyTwoNumbers(Integer multiplier, Long xValueProd) {
        Long added = Long.valueOf(0);
        for(int i = 1; i <= multiplier; i = i + 1){
            added = added + xValueProd;
        }
        return added;
    }

}
