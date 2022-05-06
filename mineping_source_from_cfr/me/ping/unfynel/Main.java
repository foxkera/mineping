/*
 * Decompiled with CFR 0.150.
 */
package me.ping.unfynel;

import me.ping.unfynel.Options;
import me.ping.unfynel.PingBot;

public class Main {
    public static void main(String ... args) throws InterruptedException {
        PingBot breaker = new PingBot(Options.Builder.of(args));
        breaker.launch();
    }
}

