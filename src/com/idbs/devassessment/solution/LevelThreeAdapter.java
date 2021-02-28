package com.idbs.devassessment.solution;

import javax.json.JsonArray;

public class LevelThreeAdapter implements PolynomialSolver{
    LevelThree levelThree;

    public LevelThreeAdapter(LevelThree levelThree) {
        this.levelThree = levelThree;
    }

    @Override
    public String parseEquation(String requiredString) {
        String dataForQuestion = requiredString;
        String result = levelThree.handleInputType(dataForQuestion);
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
