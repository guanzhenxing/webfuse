package cn.webfuse.framework.kit;

import cn.webfuse.core.kit.SystemPropertiesKits;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ServerSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 1. 获取本机IP地址与HostName
 * <p>
 * 2. 查找空闲端口
 * <p>
 * From: com.vip.vjtools.vjkit.net.NetUtil
 */
public class LocalHostInfoKits {

    public static final int PORT_RANGE_MIN = 1024;
    public static final int PORT_RANGE_MAX = 65535;
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalHostInfoKits.class);
    private static Random random = new Random();


    /////// LocalAddress //////

    /**
     * 获得本地地址
     */
    public static InetAddress getLocalAddress() {
        return LocalAddressHolder.INSTANCE.localInetAddress;
    }

    /**
     * 获得本地Ip地址
     */
    public static String getLocalHost() {
        return LocalAddressHolder.INSTANCE.localHost;
    }

    /**
     * 获得本地HostName
     */
    public static String getHostName() {
        return LocalAddressHolder.INSTANCE.hostName;
    }

    /**
     * 测试端口是否空闲可用, from Spring SocketUtils
     */
    public static boolean isPortAvailable(int port) {
        try {
            ServerSocket serverSocket = ServerSocketFactory.getDefault().createServerSocket(port, 1,
                    InetAddress.getByName("localhost"));
            serverSocket.close();
            return true;
        } catch (Exception ex) { // NOSONAR
            return false;
        }
    }

    /**
     * 从1024到65535， 随机找一个空闲端口 from Spring SocketUtils
     */
    public static int findRandomAvailablePort() {
        return findRandomAvailablePort(PORT_RANGE_MIN, PORT_RANGE_MAX);
    }

    /////////// 查找空闲端口 /////////

    /**
     * 在范围里随机找一个空闲端口,from Spring SocketUtils.
     *
     * @throws IllegalStateException 最多尝试(maxPort-minPort)次，如无空闲端口，抛出此异常.
     */
    public static int findRandomAvailablePort(int minPort, int maxPort) {
        int portRange = maxPort - minPort;
        int candidatePort;
        int searchCounter = 0;

        do {
            if (++searchCounter > portRange) {
                throw new IllegalStateException(
                        String.format("Could not find an available tcp port in the range [%d, %d] after %d attempts",
                                minPort, maxPort, searchCounter));
            }
            candidatePort = minPort + random.nextInt(portRange + 1);
        } while (!isPortAvailable(candidatePort));

        return candidatePort;
    }

    /**
     * 从某个端口开始，递增直到65535，找一个空闲端口.
     *
     * @throws IllegalStateException 范围内如无空闲端口，抛出此异常
     */
    public static int findAvailablePortFrom(int minPort) {
        for (int port = minPort; port < PORT_RANGE_MAX; port++) {
            if (isPortAvailable(port)) {
                return port;
            }
        }

        throw new IllegalStateException(
                String.format("Could not find an available tcp port in the range [%d, %d]", minPort, PORT_RANGE_MAX));
    }

    /**
     * 懒加载进行探测
     */
    private static class LocalAddressHolder {
        static final LocalAddress INSTANCE = new LocalAddress();
    }


    /**
     * 获取Unix网卡的mac地址.
     *
     * @return mac地址
     */
    private static String getUnixMACAddress() {
        String mac = null;
        BufferedReader bufferedReader = null;
        Process process;
        try {
            // Unix下的命令，一般取eth0作为本地主网卡 显示信息中包含有mac地址信息
            process = Runtime.getRuntime().exec("ifconfig eth0");
            bufferedReader = new BufferedReader(new InputStreamReader(
                    process.getInputStream(), "UTF-8"));
            String line;
            int index;
            while ((line = bufferedReader.readLine()) != null) {
                // 寻找标示字符串[hwaddr]
                index = line.toLowerCase().indexOf("hwaddr");
                // 找到了
                if (index != -1) {
                    // 取出mac地址并去除2边空格
                    mac = line.substring(index + "hwaddr".length() + 1).trim();
                    break;
                }
            }
        } catch (IOException e) {
            LOGGER.error("获取Unix网卡的mac地址异常", e);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e1) {
                LOGGER.error("关闭流异常，{}", e1.getMessage());
            }
        }

        return mac;
    }

    /**
     * 获取Linux网卡的mac地址.
     *
     * @return mac地址
     */
    private static String getLinuxMACAddress() {
        String mac = null;
        BufferedReader bufferedReader = null;
        Process process;
        try {
            // linux下的命令，一般取eth0作为本地主网卡 显示信息中包含有mac地址信息
            process = Runtime.getRuntime().exec("ifconfig eth0");
            bufferedReader = new BufferedReader(new InputStreamReader(
                    process.getInputStream(), "UTF-8"));
            String line;
            int index;
            while ((line = bufferedReader.readLine()) != null) {
                index = line.toLowerCase().indexOf("硬件地址");
                // 找到了
                if (index != -1) {
                    // 取出mac地址并去除2边空格
                    mac = line.substring(index + 4).trim();
                    break;
                }
            }
        } catch (IOException e) {
            LOGGER.error("获取Linux网卡的mac地址", e);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e1) {
                LOGGER.error("关闭流异常，{}", e1.getMessage());
            }
        }

        // 取不到，试下Unix取发
        if (mac == null) {
            return getUnixMACAddress();
        }

        return mac;
    }

    /**
     * 获取Windows网卡的mac地址.
     *
     * @return mac地址
     */
    private static String getWindowsMACAddress() {
        String mac = null;
        BufferedReader bufferedReader = null;
        Process process;
        try {
            // windows下的命令，显示信息中包含有mac地址信息
            process = Runtime.getRuntime().exec("ipconfig /all");
            bufferedReader = new BufferedReader(new InputStreamReader(
                    process.getInputStream(), "UTF-8"));
            String line;
            int index;
            while ((line = bufferedReader.readLine()) != null) {
                // 寻找标示字符串[physical address]
                if (line.split("-").length == 6) {
                    index = line.indexOf(":");
                    if (index != -1) {
                        // 取出mac地址并去除2边空格
                        mac = line.substring(index + 1).trim();
                    }
                    break;
                }
                index = line.toLowerCase().indexOf("物理地址");
                if (index != -1) {
                    index = line.indexOf(":");
                    if (index != -1) {
                        // 取出mac地址并去除2边空格
                        mac = line.substring(index + 1).trim();
                    }
                    break;
                }
            }
        } catch (IOException e) {
            LOGGER.error("获取Windows网卡的mac地址", e);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e1) {
                LOGGER.error("关闭流异常，{}", e1.getMessage());
            }
        }

        return mac;
    }

    /**
     * 获取Mac地址
     *
     * @return String
     */
    public static String getMac() {
        String os = SystemUtils.OS_NAME.toLowerCase();
        String mac;
        if (os.startsWith("windows")) {
            mac = getWindowsMACAddress();
        } else if (os.startsWith("linux")) {
            mac = getLinuxMACAddress();
        } else {
            mac = getUnixMACAddress();
        }
        return mac == null ? "" : mac;
    }

    private static class LocalAddress {

        private InetAddress localInetAddress;
        private String localHost;
        private String hostName;

        public LocalAddress() {
            initLocalAddress();
            // from Common Lang SystemUtils
            hostName = SystemUtils.IS_OS_WINDOWS ? System.getenv("COMPUTERNAME") : System.getenv("HOSTNAME");
        }

        /**
         * 根据preferNamePrefix 与 defaultNicList的配置网卡，找出合适的网卡
         */
        private static InetAddress findLocalAddressViaNetworkInterface() {
            // 如果hostname +/etc/hosts 得到的是127.0.0.1, 则首选这块网卡
            String preferNamePrefix = SystemPropertiesKits.getString("localhost.prefer.nic.prefix",
                    "LOCALHOST_PREFER_NIC_PREFIX", "bond0.");
            // 如果hostname +/etc/hosts 得到的是127.0.0.1, 和首选网卡都不符合要求，则按顺序遍历下面的网卡
            String defaultNicList = SystemPropertiesKits.getString("localhost.default.nic.list",
                    "LOCALHOST_DEFAULT_NIC_LIST", "bond0,eth0,em0,br0");

            InetAddress resultAddress = null;
            Map<String, NetworkInterface> candidateInterfaces = new HashMap<>();

            // 遍历所有网卡，找出所有可用网卡，尝试找出符合prefer前缀的网卡
            try {
                for (Enumeration<NetworkInterface> allInterfaces = NetworkInterface
                        .getNetworkInterfaces(); allInterfaces.hasMoreElements(); ) {
                    NetworkInterface nic = allInterfaces.nextElement();
                    // 检查网卡可用并支持广播
                    try {
                        if (!nic.isUp() || !nic.supportsMulticast()) {
                            continue;
                        }
                    } catch (SocketException ignored) { // NOSONAR
                        continue;
                    }

                    // 检查是否符合prefer前缀
                    String name = nic.getName();
                    if (name.startsWith(preferNamePrefix)) {
                        // 检查有否非ipv6 非127.0.0.1的inetaddress
                        resultAddress = findAvailableInetAddress(nic);
                        if (resultAddress != null) {
                            return resultAddress;
                        }
                    } else {
                        // 不是Prefer前缀，先放入可选列表
                        candidateInterfaces.put(name, nic);
                    }
                }

                for (String nifName : defaultNicList.split(",")) {
                    NetworkInterface nic = candidateInterfaces.get(nifName);
                    if (nic != null) {
                        resultAddress = findAvailableInetAddress(nic);
                        if (resultAddress != null) {
                            return resultAddress;
                        }
                    }
                }
            } catch (SocketException e) {// NOSONAR
                return null;
            }
            return null;
        }

        /**
         * 检查有否非ipv6，非127.0.0.1的inetaddress
         */
        private static InetAddress findAvailableInetAddress(NetworkInterface nic) {
            for (Enumeration<InetAddress> indetAddresses = nic.getInetAddresses(); indetAddresses.hasMoreElements(); ) {
                InetAddress inetAddress = indetAddresses.nextElement();
                if (!(inetAddress instanceof Inet6Address) && !inetAddress.isLoopbackAddress()) {
                    return inetAddress;
                }
            }
            return null;
        }

        /**
         * 初始化本地地址
         */
        private void initLocalAddress() {
            NetworkInterface nic = null;
            // 根据命令行执行hostname获得本机hostname， 与/etc/hosts 中该hostname的第一条ip配置，获得ip地址
            try {
                localInetAddress = InetAddress.getLocalHost();
                nic = NetworkInterface.getByInetAddress(localInetAddress);
            } catch (Exception ignored) { // NOSONAR
            }

            // 如果结果为空，或是一个loopback地址(127.0.0.1), 或是ipv6地址，再遍历网卡尝试获取
            if (localInetAddress == null || nic == null || localInetAddress.isLoopbackAddress()
                    || localInetAddress instanceof Inet6Address) {
                InetAddress lookedUpAddr = findLocalAddressViaNetworkInterface();
                // 仍然不符合要求，只好使用127.0.0.1
                try {
                    localInetAddress = lookedUpAddr != null ? lookedUpAddr : InetAddress.getByName("127.0.0.1");
                } catch (UnknownHostException ignored) {// NOSONAR
                }
            }

            localHost = IPKits.toIpString(localInetAddress);

            LOGGER.info("localhost is {}", localHost);
        }
    }


}
