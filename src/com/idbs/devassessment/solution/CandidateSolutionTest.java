package com.idbs.devassessment.solution;

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
    }
}