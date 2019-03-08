package edu.fandm.pweiser.chitchat;

import java.util.Random;

public class positiveRandom {

    /** This code was taken from decompiled worldy.apk */
    /**code that we editted: Lines 3,21, and 29**/

    public static final String TAG = "positiveRandom";
    private static Random r;

    public static int nextInt(int bound) {
        if (r == null) {
            r = new Random();
        }
        if (bound - 1 < 0)
        {
            return -1;
        }
        int base = r.nextInt(bound - 1) + 1;
        return base;
    }

    public static int nextInt(int min, int max) {
        if (r == null) {
            r = new Random();
        }
        return (r.nextInt(max - min) + min);
    }
}

