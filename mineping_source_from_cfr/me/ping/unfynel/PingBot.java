/*
 * Decompiled with CFR 0.150.
 */
package me.ping.unfynel;

import java.net.Proxy;
import me.ping.unfynel.Options;
import me.ping.unfynel.ProxyManager;
import me.ping.unfynel.Run;

public class PingBot {
    private final Options options;

    public PingBot(Options options) {
        this.options = options;
    }

    public void launch() throws InterruptedException {
        if (!this.options.isOption("host")) {
            System.out.println("");
            System.out.println("Example: java -jar MinePing.jar host-<IP> port-<PORT> threads-<THREADS>");
            return;
        }
        String proxiesType = this.options.getOption("proxymode--", "socks");
        String proxiesFile = this.options.getOption("file", "socks_proxies.txt");
        ProxyManager proxies = new ProxyManager();
        try {
            if (proxiesType.equalsIgnoreCase("socks")) {
                proxies.init(proxiesFile, Proxy.Type.SOCKS);
            }
        }
        catch (Exception ex) {
            return;
        }
        System.out.println("");
        System.out.println("Made by unfynel (1.0)");
        System.out.println("\u2588\u2588\u2588\u2557   \u2588\u2588\u2588\u2557\u2588\u2588\u2557\u2588\u2588\u2588\u2557   \u2588\u2588\u2557\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2557\u2588\u2588\u2588\u2588\u2588\u2588\u2557 \u2588\u2588\u2557\u2588\u2588\u2588\u2557   \u2588\u2588\u2557 \u2588\u2588\u2588\u2588\u2588\u2588\u2557 ");
        System.out.println("\u2588\u2588\u2588\u2588\u2557 \u2588\u2588\u2588\u2588\u2551\u2588\u2588\u2551\u2588\u2588\u2588\u2588\u2557  \u2588\u2588\u2551\u2588\u2588\u2554\u2550\u2550\u2550\u2550\u255d\u2588\u2588\u2554\u2550\u2550\u2588\u2588\u2557\u2588\u2588\u2551\u2588\u2588\u2588\u2588\u2557  \u2588\u2588\u2551\u2588\u2588\u2554\u2550\u2550\u2550\u2550\u255d ");
        System.out.println("\u2588\u2588\u2554\u2588\u2588\u2588\u2588\u2554\u2588\u2588\u2551\u2588\u2588\u2551\u2588\u2588\u2554\u2588\u2588\u2557 \u2588\u2588\u2551\u2588\u2588\u2588\u2588\u2588\u2557  \u2588\u2588\u2588\u2588\u2588\u2588\u2554\u255d\u2588\u2588\u2551\u2588\u2588\u2554\u2588\u2588\u2557 \u2588\u2588\u2551\u2588\u2588\u2551  \u2588\u2588\u2588\u2557");
        System.out.println("\u2588\u2588\u2551\u255a\u2588\u2588\u2554\u255d\u2588\u2588\u2551\u2588\u2588\u2551\u2588\u2588\u2551\u255a\u2588\u2588\u2557\u2588\u2588\u2551\u2588\u2588\u2554\u2550\u2550\u255d  \u2588\u2588\u2554\u2550\u2550\u2550\u255d \u2588\u2588\u2551\u2588\u2588\u2551\u255a\u2588\u2588\u2557\u2588\u2588\u2551\u2588\u2588\u2551   \u2588\u2588\u2551");
        System.out.println("\u2588\u2588\u2551 \u255a\u2550\u255d \u2588\u2588\u2551\u2588\u2588\u2551\u2588\u2588\u2551 \u255a\u2588\u2588\u2588\u2588\u2551\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2557\u2588\u2588\u2551     \u2588\u2588\u2551\u2588\u2588\u2551 \u255a\u2588\u2588\u2588\u2588\u2551\u255a\u2588\u2588\u2588\u2588\u2588\u2588\u2554\u255d");
        System.out.println("\u255a\u2550\u255d     \u255a\u2550\u255d\u255a\u2550\u255d\u255a\u2550\u255d  \u255a\u2550\u2550\u2550\u255d\u255a\u2550\u2550\u2550\u2550\u2550\u2550\u255d\u255a\u2550\u255d     \u255a\u2550\u255d\u255a\u2550\u255d  \u255a\u2550\u2550\u2550\u255d \u255a\u2550\u2550\u2550\u2550\u2550\u255d ");
        new Run(this.options, proxies).run();
    }
}

