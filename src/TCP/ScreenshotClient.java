package TCP;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.net.Socket;
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

    public void run(){
        ObjectOutputStream oos = null;


        try{
            oos = new ObjectOutputStream(socket.getOutputStream());
//            oos.writeObject(rectangle);
        }catch(IOException ex){
            ex.printStackTrace();
        }

       while(flag){
            BufferedImage image = robot.createScreenCapture(rectangle);
            ImageIcon imageIcon = new ImageIcon(image);

            Data toSend = new Data();
//            Data toSend = new Data("TCP","IP","Package");

            toSend.setTotalMemory(osBean.getTotalPhysicalMemorySize());
            toSend.setFreeMemory(osBean.getFreePhysicalMemorySize());
            toSend.setCpuLoad(osBean.getSystemCpuLoad());
            toSend.setScreen(imageIcon);

            try {
                System.out.println("before sending image");
//                oos.writeObject(imageIcon);
                oos.writeObject(toSend);
                oos.reset();
                System.out.println("New screenshot sent");
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
