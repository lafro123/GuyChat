package model;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class IPAddress {

    private static InetAddress ipAdress;

    static {
        try {
            ipAdress = getIPv4Address();
        } catch (SocketException e) {
            e.printStackTrace();
            ipAdress = null;
        }
    }

    public static InetAddress getIPv4Address() throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = inetAddresses.nextElement();
                if (!inetAddress.isLoopbackAddress() && inetAddress.getAddress().length == 4) {
                    return inetAddress;
                }
            }
        }
        return null;
    }

    public static InetAddress getIpAddress() {
        return ipAdress;
    }
}
