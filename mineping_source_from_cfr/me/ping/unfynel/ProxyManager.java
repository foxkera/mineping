/*
 * Decompiled with CFR 0.150.
 */
package me.ping.unfynel;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ProxyManager {
    private final List<Proxy> proxies = new ArrayList<Proxy>();
    private int index = 0;

    public void init(String fileName, Proxy.Type type) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains(" ")) {
                line = line.replace(" ", "");
            }
            if (line.isEmpty() || line.startsWith("##") || !line.contains(":")) continue;
            String[] ip = line.split(":");
            if (ip.length < 1) {
                return;
            }
            String hostname = ip[0];
            int port = 8080;
            try {
                port = Integer.parseInt(ip[1]);
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
            Proxy proxy = new Proxy(type, new InetSocketAddress(hostname, port));
            this.proxies.add(proxy);
        }
    }

    public int size() {
        return this.proxies.size();
    }

    public Proxy nextProxy() {
        if (this.proxies.size() == 0) {
            return Proxy.NO_PROXY;
        }
        if (this.index >= this.proxies.size()) {
            this.index = 0;
        }
        return Objects.requireNonNull(this.proxies.get(this.index++));
    }

    public void removeProxy(Proxy proxy) {
        this.proxies.remove(proxy);
    }
}

