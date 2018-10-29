package TCP;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Main{

    Socket socket = null;

    public static void main(String[] args){
        String ip = JOptionPane.showInputDialog("Please enter server IP");
        String port = "15001";//JOptionPane.showInputDialog("Please enter server port");
        new Main().initialize(ip, Integer.parseInt(port));
    }

    public void initialize(String ip, int port ){

        Robot robot = null; 
        Rectangle rectangle = null; 

        try {
            System.out.println("Connecting to server.");
            socket = new Socket(ip, port);
            System.out.println("Connection Established.");

            GraphicsEnvironment gEnv=GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gDev=gEnv.getDefaultScreenDevice();

            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            rectangle = new Rectangle(dim);

            robot = new Robot(gDev);

            drawGUI();
            new ScreenshotClient(socket,robot,rectangle);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (AWTException ex) {
                ex.printStackTrace();
        }
    }

    private void drawGUI() {
        JFrame frame = new JFrame("Remote System Monitor");
        JButton button= new JButton();
        try {
            ImageIcon power = new ImageIcon("p.png","") ;//("p.png");
//            Image img = ImageIO.read(getClass().getResource("p.png"));
            button.setIcon(power);
        } catch (Exception ex) {
            System.out.println("Image error");
            System.out.println(ex);
        }
//        button.setBorder(null);

        frame.setBounds(100,100,150,150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(button);

        button.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }
}
