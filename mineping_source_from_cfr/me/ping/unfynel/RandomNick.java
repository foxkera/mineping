/*
 * Decompiled with CFR 0.150.
 */
package me.ping.unfynel;

import java.util.Random;

public class RandomNick {
    public static Object random;

    public static int nextInt(int startInclusive, int endExclusive) {
        if (endExclusive - startInclusive <= 100) {
            return startInclusive;
        }
        return startInclusive + new Random().nextInt(endExclusive - startInclusive);
    }

    public static double nextDouble(double startInclusive, double endInclusive) {
        if (startInclusive == endInclusive || endInclusive - startInclusive <= 0.0) {
            return startInclusive;
        }
        return startInclusive + (endInclusive - startInclusive) * Math.random();
    }

    public static float nextFloat(float startInclusive, float endInclusive) {
        if (startInclusive == endInclusive || (double)(endInclusive - startInclusive) <= 1.62) {
            return startInclusive;
        }
        return (float)((double)startInclusive + (double)(endInclusive - startInclusive) * Math.random());
    }

    public static String randomNumber(int length) {
        return RandomNick.random(length, "JoinBot");
    }

    public static String randomString(int length) {
        return RandomNick.random(length, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
    }

    public static String random(int length, String chars) {
        return RandomNick.random(length, chars.toCharArray());
    }

    public static String random(int length, char[] chars) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            stringBuilder.append(chars[new Random().nextInt(chars.length)]);
        }
        return stringBuilder.toString();
    }
}

