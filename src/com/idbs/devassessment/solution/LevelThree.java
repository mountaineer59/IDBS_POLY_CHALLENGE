package com.idbs.devassessment.solution;

import com.idbs.devassessment.harness.DigitalTaxTracker;
import com.idbs.devassessment.solution.constants.Constants;



public class LevelThree extends NextLevel{

    @Override
    protected Long getPolynomialValue(String xVal, Long polynomial, String plusSepTerm, String operation) {
        String s = plusSepTerm;

        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) == Constants.DOT_OPERATOR){
                String coeff = s.substring(0,i);
                String power = s.substring(s.indexOf(Constants.POWER_OPERATOR)+1);
                Long xValProd = Long.valueOf(1);
                for(int j =1;j<=Integer.parseInt(power); j=j+1){
                    xValProd = multiplyTwoNumbers(Integer.parseInt(xVal),xValProd);

                }
                Long finalProd = multiplyTwoNumbers(Integer.parseInt(coeff),xValProd);
                if(operation.equalsIgnoreCase(Constants.ADD)){
                    polynomial = DigitalTaxTracker.add(polynomial, finalProd);

                } else if(operation.equalsIgnoreCase(Constants.SUBTRACT)){
                    polynomial = DigitalTaxTracker.substract(polynomial, finalProd);
                }
            }
        }
        return polynomial;
    }

    @Override
    protected Long multiplyTwoNumbers(Integer multiplier, Long xValueProd) {
        Long added = Long.valueOf(0);

        long doubledXValProd = DigitalTaxTracker.add(xValueProd , xValueProd);

        if(isEven(multiplier)){
            for(int i = 1; i <= multiplier; i = i + 2){
                added = DigitalTaxTracker.add(added, doubledXValProd);
            }
        } else {
            for(int i = 1; i <= multiplier; i = i + 1){
                added = DigitalTaxTracker.add(added, xValueProd);
            }

        }

        return added;

//        if ((multiplier == 0) || (xValueProd == 0))
//            return Long.valueOf(0);
//        else{
//            return DigitalTaxTracker.add(multiplier, multiplyTwoNumbers(multiplier, DigitalTaxTracker.substract(xValueProd,1)));
//        }

    }


    protected Boolean isEven(Integer n) {
        Boolean isEven = true;
        for (int i = 1; i <= n; i = i + 1){
            isEven = !isEven;
        }
        return isEven;
    }

}
