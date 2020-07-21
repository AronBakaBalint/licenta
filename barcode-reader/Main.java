
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

//source
//https://github.com/vivekkairi/webcam-qr-barcode-scanner
public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        Webcam webcam = Webcam.getDefault();   //Generate Webcam Object
        webcam.setViewSize(new Dimension(640, 480));
        WebcamPanel webcamPanel = new WebcamPanel(webcam);
        webcamPanel.setBackground(Color.black);
        webcamPanel.setMirrored(true);
        
        JFrame jFrame = new JFrame();
        showWebcam(jFrame, webcamPanel);
        do {
            try {
                BufferedImage image = webcam.getImage();
                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                Result result = new MultiFormatReader().decode(bitmap);
                if(result.getText() != null) {
                    sendDecoded(result.getText());
                    System.out.println(result.getText());
                    webcamPanel.setVisible(false);
                    jFrame.setTitle("Welcome");
                    Thread.sleep(10000);
                    jFrame.setTitle("");
                    webcamPanel.setVisible(true);
                }
            }catch (NotFoundException e ) {
                //do nothing
            }
        } while(true);
    }
    
    private static void sendDecoded(String decodedString) throws IOException {
    	InetAddress host = InetAddress.getLocalHost();
		Socket socket = null;
		ObjectOutputStream oos = null;
		socket = new Socket(host.getHostName(), 9876);
		oos = new ObjectOutputStream(socket.getOutputStream());
		oos.writeObject(decodedString);
		oos.close();
		socket.close();
    }
    
    private static void showWebcam(JFrame jFrame, WebcamPanel webcamPanel) {
    	jFrame.add(webcamPanel);
        jFrame.pack();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }
  
}
