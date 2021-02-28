package com.idbs.devassessment.solution.adapters;

import com.idbs.devassessment.solution.LevelTwo;
import com.idbs.devassessment.solution.interfaces.PolynomialSolver;

public class LevelTwoAdapter implements PolynomialSolver {

    private LevelTwo levelTwo;

    public LevelTwoAdapter(LevelTwo levelTwo) {
        this.levelTwo = levelTwo;
    }

    @Override
    public String parseEquation(String requiredString) {
        String dataForQuestion = requiredString;
        String result = levelTwo.handleInputType(dataForQuestion);
        return result;
    }

}
