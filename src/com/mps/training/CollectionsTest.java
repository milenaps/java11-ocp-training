package com.mps.training;

import java.util.Arrays;
import java.util.List;

public class CollectionsTest {
    public static void main(String[] args) {
        String[] arr = {"Tea", "Cake"};
        List<String> texts = Arrays.asList(arr);
        texts.add("Cookie");//Throws UnsupportedOperationException
    }
}
