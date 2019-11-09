package rmi.clocks.synchronization.swing.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class SystemIpAddress {
    public String getInet4AddresString() throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        String inet4AddresString = null;
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            Enumeration<InetAddress> inetAddressess = networkInterface.getInetAddresses();
            while (inetAddressess.hasMoreElements()) {
                InetAddress inetAddress = inetAddressess.nextElement();
                if (!inetAddress.isLoopbackAddress() && inetAddress.isSiteLocalAddress()) {
                    inet4AddresString = inetAddress.getHostAddress();
                }
            }
        }
        return inet4AddresString;
    }
}
