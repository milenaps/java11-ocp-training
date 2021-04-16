package com.mps.training;

public class Primitives {

    public static void main(String[] args) {
        //int 4bla = 0; //does not compile

//        char x = 'x';
//        char y = ++x;
//        System.out.println(y);

        int x = 11, y = 3;
//        double e = x/y;                               //prints 3.0
//        double e = (double)x/y;                       //prints 3.6666665
//        double e = Math.round((double)x/y*100)/100;   //prints 3.0
        double e = Math.round((double)x/y*100)/100.0; //prints 3.67
        System.out.println(e);
    }
}
