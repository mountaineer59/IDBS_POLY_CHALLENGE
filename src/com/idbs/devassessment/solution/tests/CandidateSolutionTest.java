package com.idbs.devassessment.solution.tests;

import com.idbs.devassessment.solution.CandidateSolution;
import com.idbs.devassessment.solution.LevelOne;
import com.idbs.devassessment.solution.constants.Constants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CandidateSolutionTest {

    @Test
    void isEven(){
        CandidateSolution candidateSolution = new CandidateSolution();
        assertEquals(true, candidateSolution.isEven(2));
        assertEquals(false, candidateSolution.isEven(3));
        assertEquals(true, candidateSolution.isEven(0));
    }

    @Test
    void testMultiply() {
        CandidateSolution candidateSolution = new CandidateSolution();
        assertEquals(6,candidateSolution.multiply(2, Long.valueOf(3)));
        assertEquals(9,candidateSolution.multiply(3, Long.valueOf(3)));

    }

    @Test
    void testParseEquation(){
        LevelOne levelOne = new LevelOne();
        assertEquals(Constants.ERROR_MESSAGE, levelOne.parseEquation(""));

    }


}