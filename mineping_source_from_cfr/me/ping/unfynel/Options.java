/*
 * Decompiled with CFR 0.150.
 */
package me.ping.unfynel;

import java.util.HashMap;
import java.util.Map;

public class Options {
    private final Map<String, Object> options = new HashMap<String, Object>();

    public boolean isOption(String id) {
        return this.options.containsKey(id);
    }

    public <T> T getOption(String id, T defaultValue) {
        return (T)this.options.getOrDefault(id, defaultValue);
    }

    public Map<String, Object> getOptions() {
        return this.options;
    }

    public static class Builder {
        public static Options of(String ... args) {
            Options options = new Options();
            String[] arrstring = args;
            int n = args.length;
            for (int i = 0; i < n; ++i) {
                String arg = arrstring[i];
                String[] part = arg.split("-");
                if (part.length <= 1) continue;
                Object value = part[1];
                try {
                    value = Integer.parseInt((String)value);
                }
                catch (Exception exception) {
                    // empty catch block
                }
                if (value instanceof String && value.equals("true") || value.equals("false")) {
                    try {
                        value = Boolean.parseBoolean((String)value);
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
                options.options.put(part[0].replaceAll("-", arg), value);
            }
            return options;
        }
    }
}

