package org.learn.algorithm;

/**
 * Created by qianqian on 04/01/2018.
 */
public class Fibonacci {
    public static long fibRecurNaive(int n) {
        if (n < 0)
            return -1;
        else if (n == 0)
            return 0;
        else if (n == 1 || n == 2)
            return 1;
        else
            return (fibRecurNaive(n-1) + fibRecurNaive(n-2));
    }

    public static long fibBottomUp(int n) {
        long fibN = 0;
        long fibN_Minus2 = 0;
        long fibN_Minus1 = 1;

        if (n < 0)
            return -1;
        if (n == 0)
            return 0;
        if (n == 1)
            return 1;

        for (int i = 2; i <= n; i++) {
            fibN = fibN_Minus1 + fibN_Minus2;
            fibN_Minus2 = fibN_Minus1;
            fibN_Minus1 = fibN;
        }
        return fibN;
    }

    public static long fibRecurSquare(int n) {
        long[][] fibMatrix;
        long[] result = {0, 1};
        if (n < 0)
            return -1;
        if (n < 2)
            return result[n];

        fibMatrix = matrixPower(n);
        return fibMatrix[0][1];

    }

    public static long[][] matrixPower(int n){
        long[][] resultMatrix;
        long[][] baseMatrix = {{1, 1}, {1, 0}};
        long[][] matrixA;
        long[][] matrixB;

        if (n == 1){
            resultMatrix = baseMatrix;
            return resultMatrix;
        }

        if (n%2 == 0) {
            matrixA = matrixPower(n/2);
            resultMatrix = matrixMUL(matrixA, matrixA);
            return resultMatrix;
        }
        else {
            matrixA = matrixPower((n-1)/2);
            matrixB = matrixMUL(matrixA, matrixA);
            resultMatrix = matrixMUL(matrixB, baseMatrix);
            return resultMatrix;
        }
    }

    public static long[][] matrixMUL(long[][] matrixA, long[][] matrixB) {
        long[][] resultMatrix = new long[2][2];
        for (int i = 0; i < 2; i++) {  // matrix A row number
            for (int j = 0; j < 2; j++) {   // matrix B column number
                resultMatrix[i][j] = 0;
                for (int k = 0; k < 2; k++) {  // matrix B row number
                    resultMatrix[i][j] = resultMatrix[i][j] + matrixA[i][k]*matrixB[k][j];
                }
            }
        }
        return resultMatrix;
    }

    public static double power(double x, int n) {
        double result;

        if (n == 0)
            return 1;
        else {
            if (n%2 == 0) {   // n is even
                result = power(x, n/2);
                return result*result;
            }
            else {
                result = power(x, (n-1)/2);
                return x*result*result;
            }
        }

    }

    public static double myPower(double x, int n) {
        if (x == 0 && n == 0) {
            throw new IllegalArgumentException();
        }

        boolean isNegative = false;

        if (n < 0) {
            n = -n;
            isNegative = true;
        }

        double result = power(x, n);

        if (isNegative) {
            return 1.0 / result;
        } else {
            return result;
        }
    }

    public static void main(String[] args) {
        System.out.println("********** Power Method **********");
        double result = power(5, 3);
        System.out.println("x power n is: " + result);
        double result1 = myPower(6, -3);
        System.out.println("x power n is: " + result1);


        System.out.println("********** org.learn.algorithm.Fibonacci in Naive Recurse Method **********");
        long startTime = System.currentTimeMillis();
        int n = 40;
        long fib = fibRecurNaive(n);
        long endTime = System.currentTimeMillis();
        System.out.printf("org.learn.algorithm.Fibonacci num of %d is %d", n, fib);
        System.out.println("\nRunning Time is " + (endTime - startTime) + "ms");

        System.out.println("********** org.learn.algorithm.Fibonacci in Bottom-Up Method **********");
        long startTime_2 = System.currentTimeMillis();
        int n_2 = 60;
        long fib_2 = fibBottomUp(n_2);
        long endTime_2 = System.currentTimeMillis();
        System.out.printf("org.learn.algorithm.Fibonacci num of %d is %d", n_2, fib_2);
        System.out.println("\nRunning Time is " + (endTime_2 - startTime_2) + "ms");


        System.out.println("********** org.learn.algorithm.Fibonacci in Recurse Squaring Method **********");
        long startTime_3 = System.currentTimeMillis();
        int n_3 = 60;
        long fib_3 = fibRecurSquare(n_3);
        long endTime_3 = System.currentTimeMillis();
        System.out.printf("org.learn.algorithm.Fibonacci num of %d is %d", n_3, fib_3);
        System.out.println("\nRunning Time is " + (endTime_3 - startTime_3) + "ms");

    }
}