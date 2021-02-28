package com.idbs.devassessment.solution;

import javax.json.JsonArray;

public class LevelTwoAdapter implements PolynomialSolver{

    LevelTwo levelTwo;

    public LevelTwoAdapter(LevelTwo levelTwo) {
        this.levelTwo = levelTwo;
    }

    @Override
    public String parseEquation(String requiredString) {
        String dataForQuestion = requiredString;
        String result = levelTwo.handleInputType(dataForQuestion);
        return result;
    }

    @Override
    public Long calculateAnswer(Integer xValue, JsonArray jsonArray) {
        return null;
    }

    @Override
    public Long multiplyTwoNumbers(Integer multiplier, Long xValueProd) {
        return null;
    }
}
