/*
 * Decompiled with CFR 0.150.
 */
package me.ping.unfynel;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class PingHelper {
    public static void testPing(InetSocketAddress address, String host, int port) {
        try {
            Socket socket = new Socket();
            System.out.println("Connecting...");
            socket.connect(address, 2000);
            System.out.println("Done!");
            System.out.println("Making streams...");
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            DataInputStream input = new DataInputStream(socket.getInputStream());
            System.out.println("Done!");
            System.out.println("Attempting handshake... " + address.toString());
            byte[] handshakeMessage = PingHelper.createHandshakeMessage(host, port);
            PingHelper.writeVarInt(output, handshakeMessage.length);
            output.write(handshakeMessage);
            output.writeByte(1);
            output.writeByte(2);
            int size = PingHelper.readVarInt(input);
            int packetId = PingHelper.readVarInt(input);
            if (packetId == 1) {
                throw new IOException("Premature end of stream.");
            }
            if (packetId != 0) {
                throw new IOException("Invalid packetID");
            }
            int length = PingHelper.readVarInt(input);
            if (length == 1) {
                throw new IOException("Premature end of stream.");
            }
            if (length == 0) {
                throw new IOException("Invalid string length.");
            }
            byte[] in = new byte[length];
            input.readFully(in);
            String json = new String(in);
            long now = System.currentTimeMillis();
            output.writeByte(9);
            output.writeByte(2);
            output.writeLong(now);
            PingHelper.readVarInt(input);
            System.out.println("length:" + length + ", size: " + size);
            packetId = PingHelper.readVarInt(input);
            if (packetId == 1) {
                throw new IOException("Premature end of stream.");
            }
            if (packetId != 1) {
                throw new IOException("Invalid packetID");
            }
            long pingtime = input.readLong();
            System.out.println(json);
            System.out.println("Done!");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static byte[] createHandshakeMessage(String host, int port) {
        return null;
    }

    public static byte[] readByteArray(DataInputStream in) {
        int length = PingHelper.readVarInt(in);
        byte[] data = new byte[length];
        try {
            in.readFully(data);
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return data;
    }

    public static int readVarInt(DataInputStream in) {
        int result;
        block4: {
            int numRead = 0;
            result = 0;
            try {
                block3: {
                    byte read;
                    do {
                        read = in.readByte();
                        int value = read & 0x7F;
                        result |= value << 7 * numRead;
                        if (++numRead > 5) break block3;
                    } while ((read & 0x80) != 0);
                    break block4;
                }
                throw new RuntimeException("VarInt is too big");
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return result;
    }

    public static void writeVarInt(DataOutputStream out, int paramInt) {
        try {
            while (true) {
                if ((paramInt & 0xFFFFFF80) == 0) {
                    out.writeByte(paramInt);
                    return;
                }
                out.writeByte(paramInt & 0x7F | 0x80);
                paramInt >>>= 7;
            }
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void writeVarIntException(DataOutputStream out, int paramInt) throws IOException {
        while (true) {
            if ((paramInt & 0xFFFFFF80) == 0) {
                out.writeByte(paramInt);
                return;
            }
            out.writeByte(paramInt & 0x7F | 0x80);
            paramInt >>>= 7;
        }
    }

    public static void writeVarInt(ByteArrayOutputStream out, int paramInt) {
        while (true) {
            if ((paramInt & 0xFFFFFF80) == 0) {
                out.write(paramInt);
                return;
            }
            out.write(paramInt & 0x7F | 0x80);
            paramInt >>>= 7;
        }
    }

    public static void writeByteArray(DataOutputStream out, byte[] data) {
        try {
            PingHelper.writeVarInt(out, data.length);
            out.write(data, 0, data.length);
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void writePacket(DataOutputStream out, byte[] packet) {
        try {
            PingHelper.writeVarInt(out, packet.length);
            out.write(packet);
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void writePacketException(DataOutputStream out, byte[] packet) throws IOException {
        PingHelper.writeVarIntException(out, packet.length);
        out.write(packet);
    }

    public static byte[] serialize(Object obj) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(obj);
            return out.toByteArray();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Object deserialize(byte[] data) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream is = new ObjectInputStream(in);
            return is.readObject();
        }
        catch (IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static byte[] createEncryptionResponsePacket(byte[] encryptedKey, byte[] encryptedVerifyToken) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bytes);
        PingHelper.writeVarInt(out, 1);
        PingHelper.writeByteArray(out, encryptedKey);
        PingHelper.writeByteArray(out, encryptedVerifyToken);
        byte[] data = bytes.toByteArray();
        try {
            bytes.close();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return data;
    }

    public static byte[] createHandshakeMessage18(String host, int port, int state) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            DataOutputStream handshake = new DataOutputStream(buffer);
            handshake.writeByte(0);
            PingHelper.writeVarInt(handshake, 47);
            PingHelper.writeString(handshake, host, StandardCharsets.UTF_8);
            handshake.writeShort(port);
            PingHelper.writeVarInt(handshake, state);
            return buffer.toByteArray();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static byte[] createHandshakeMessageMods(String host, int port, int state) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            DataOutputStream handshake = new DataOutputStream(buffer);
            handshake.writeByte(0);
            PingHelper.writeVarInt(handshake, 47);
            PingHelper.writeString(handshake, host, StandardCharsets.UTF_8);
            handshake.writeUTF(host);
            handshake.writeShort(port);
            PingHelper.writeVarInt(handshake, state);
            return buffer.toByteArray();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static byte[] createLogin(String username) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            DataOutputStream login = new DataOutputStream(buffer);
            login.writeByte(0);
            PingHelper.writeString(login, username, StandardCharsets.UTF_8);
            return buffer.toByteArray();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void writeString(DataOutputStream out, String string, Charset charset) {
        try {
            byte[] bytes = string.getBytes(charset);
            PingHelper.writeVarInt(out, bytes.length);
            out.write(bytes);
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void writeString(ByteArrayOutputStream out, String string, Charset charset) {
        try {
            byte[] bytes = string.getBytes(charset);
            PingHelper.writeVarInt(out, bytes.length);
            out.write(bytes);
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void sendPacket(byte[] packet, DataOutputStream out) {
        PingHelper.writePacket(out, packet);
    }

    public static void sendPacketException(byte[] packet, DataOutputStream out) throws IOException {
        PingHelper.writePacketException(out, packet);
    }

    public static String readContent(URI uri) throws IOException {
        String currentLine;
        StringBuilder content = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(uri.toURL().openConnection().getInputStream()));
        while ((currentLine = reader.readLine()) != null) {
            content.append(currentLine);
        }
        reader.close();
        return content.toString();
    }

    private void Uri() {
    }
}

