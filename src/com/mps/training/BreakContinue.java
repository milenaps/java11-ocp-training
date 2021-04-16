package com.mps.training;

public class BreakContinue {

    public static void main(String[] args) {
        char[][] matrix = {{'A','B','C','D','E'},
                            {'F','G','H','I','K'},
                            {'L','M','N','O','P'},
                            {'Q','R','S','T','U'},
                            {'V','W','X','Y','Z'}};
        StringBuilder sb = new StringBuilder();
        outerLoopLabel:
        for (char[] row: matrix) {
            for (char value: row) {
                if (value == 'C') { continue; }
                if (value == 'H') { continue outerLoopLabel; }
                if (value == 'N') { break; }
                if (value == 'S') { break outerLoopLabel; }
                sb.append(value);
            }
            sb.append('\n');
        }
        assert(sb.toString().equals("ABDE\nFGLM\nQR"));
    }
}
