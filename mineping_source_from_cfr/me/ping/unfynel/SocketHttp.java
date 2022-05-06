/*
 * Decompiled with CFR 0.150.
 */
package me.ping.unfynel;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Hashtable;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class SocketHttp
extends Socket {
    private static final DirContext srvContext;

    static {
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
        env.put("java.naming.provider.url", "dns:");
        try {
            srvContext = new InitialDirContext(env);
        }
        catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String resolve(String domain, String type) {
        try {
            Attribute attrib;
            Attributes e = srvContext.getAttributes("_" + type + "._tcp." + domain, new String[]{"SRV"});
            if (e != null && (attrib = e.get("srv")) != null) {
                Object obj = attrib.get(0);
                String[] array = obj.toString().split(" ");
                if (obj != null) {
                    return String.valueOf(array[3].substring(0, array[3].length() - 1)) + ":" + array[2];
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return "";
    }

    public static String srv(String ip) {
        String resolved = SocketHttp.resolve(ip, "minecraft");
        if (resolved.length() > 0) {
            return String.valueOf(resolved.split(":")[0]) + ":" + resolved.split(":")[1];
        }
        return String.valueOf(ip) + ":25565";
    }

    public SocketHttp(String targetHost, int targetPort, SocketAddress socketAddress, int timeout) throws IOException {
    }

    private void connectToTarget(String targetHost, int targetPort) throws IOException {
        PrintStream printStream = new PrintStream(this.getOutputStream());
        printStream.println("CONNECT " + targetHost + ":" + targetPort + " HTTP/1.1");
        printStream.println("HOST: " + targetHost + ":" + targetPort);
        printStream.println();
        printStream.flush();
    }
}

