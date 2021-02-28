package com.idbs.devassessment.solution;

import javax.json.JsonArray;

public interface PolynomialSolver {
     String parseEquation(String requiredString);
     Long calculateAnswer(Integer xValue, JsonArray jsonArray);
     Long multiplyTwoNumbers(Integer multiplier, Long xValueProd);
}
