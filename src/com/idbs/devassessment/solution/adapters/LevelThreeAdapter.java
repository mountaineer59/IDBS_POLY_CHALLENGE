package com.idbs.devassessment.solution.adapters;

import com.idbs.devassessment.solution.LevelThree;
import com.idbs.devassessment.solution.interfaces.PolynomialSolver;

public class LevelThreeAdapter implements PolynomialSolver {
    private LevelThree levelThree;

    public LevelThreeAdapter(LevelThree levelThree) {
        this.levelThree = levelThree;
    }

    @Override
    public String parseEquation(String requiredString) {
        String dataForQuestion = requiredString;
        String result = levelThree.handleInputType(dataForQuestion);
        return result;
    }

}
