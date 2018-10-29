package TCP;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;


import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.ImageIcon;


class ScreenshotClient extends Thread {

    private static Runtime runtime = Runtime.getRuntime();
    static OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

    Socket socket = null; 
    Robot robot = null; 
    Rectangle rectangle = null; 
    boolean flag = true; 
    
    public ScreenshotClient(Socket socket, Robot robot,Rectangle rect) {
        this.socket = socket;
        this.robot = robot;
        rectangle = rect;
        start();
    }

    private String getNetBytes(String type, String intf) throws IOException{
        String f = new String("/sys/class/net/"+intf+"/statistics/"+type+"_bytes");

        String targetFileStr = new String(Files.readAllBytes(Paths.get(f)), StandardCharsets.UTF_8);
//        System.out.println(targetFileStr);
        return targetFileStr;
    }


    public void run(){
        ObjectOutputStream oos = null;


        try{
            oos = new ObjectOutputStream(socket.getOutputStream());
        }catch(IOException ex){
            ex.printStackTrace();
        }

       while(flag){
            BufferedImage image = robot.createScreenCapture(rectangle);
            ImageIcon imageIcon = new ImageIcon(image);

            Data toSend = new Data();

            toSend.setTotalMemory(osBean.getTotalPhysicalMemorySize());
            toSend.setFreeMemory(osBean.getFreePhysicalMemorySize());
            toSend.setCpuLoad(osBean.getSystemCpuLoad());
            toSend.setScreen(imageIcon);

//            System.out.println(toSend.getOSName());
//           InetAddress address = null;
//           NetworkInterface nif = null;
//           try {
//               address = InetAddress.getLocalHost();
//           } catch (UnknownHostException e) {
//               e.printStackTrace();
//           }
//
//           try {
//               nif = NetworkInterface.getByInetAddress(
//                       InetAddress.getByName("localhost"));
//           } catch (SocketException e) {
//               e.printStackTrace();
//           } catch (UnknownHostException e) {
//               e.printStackTrace();
//           }
//           try {
//               nif = NetworkInterface.getByInetAddress(
//                       InetAddress.getByAddress(address.getAddress()));
//           } catch (SocketException e) {
//               e.printStackTrace();
//           } catch (UnknownHostException e) {
//               e.printStackTrace();
//           }

//           System.out.println(nif);

           if(toSend.getOSName().equals("Linux")){
                try {
                    toSend.setRxData(getNetBytes("rx","wlp2s0"));
                    toSend.setTxData(getNetBytes("tx","wlp2s0"));

//                    System.out.println(getNetBytes("tx","wlp2s0"));
//                    System.out.println(getNetBytes("rx","wlp2s0"));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                System.out.println("Sending...");
//                oos.writeObject(imageIcon);
                oos.writeObject(toSend);
                oos.reset();
                System.out.println("Sent");
            } catch (IOException ex) {
               ex.printStackTrace();
            }

            try{
                Thread.sleep(50);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }

    }

}
