package net.ncue.mart.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

public class GIFConvertor {
	public void changeImageSize(String inFile, String outFile, int width, int height) {
        try {
               // img = ImageIO.read( new File(fName));
               FileInputStream fis = new FileInputStream(inFile);
               byte[] data = new byte[fis.available()];
               // System.out.println(fis.available());
               fis.read(data);
               fis.close();

               Image image = Toolkit.getDefaultToolkit().createImage(data);
               Image rtnImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);

               MediaTracker tracker = new MediaTracker(new java.awt.Frame());
               tracker.addImage(rtnImage, 0);
               tracker.waitForAll();

               BufferedImage bi = new BufferedImage(rtnImage.getWidth(null), rtnImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
               Graphics g = bi.getGraphics();
               g.drawImage(rtnImage, 0, 0, null);
               g.dispose();
               ByteArrayOutputStream bas = new ByteArrayOutputStream();
               ImageIO.write(bi, "gif", bas);
               byte[] writeData = bas.toByteArray();

               DataOutputStream dos = new DataOutputStream( new BufferedOutputStream(new FileOutputStream(outFile)));
               dos.write(writeData);
               dos.close();
        } catch (Exception e) {
               e.printStackTrace();

        }
  }
		
	// //imgFile("C:\\", "http://sstatic.naver.net/search/img3/", "h1_naver.gif");
	public void makeImgFile(String inFile, String outFile) throws Exception{
	    BufferedImage image = null;
	    try{
	        image = ImageIO.read(new File(inFile));
	        BufferedImage bufferedImage = new BufferedImage(image.getWidth(),image.getHeight(), BufferedImage.TYPE_INT_BGR);
	          
	        Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
	        graphics.setBackground(Color.WHITE);
	        graphics.drawImage(image, 0, 0, null);
	          
	        ImageIO.write(bufferedImage, "gif", new File(outFile));
	        this.changeImageSize(inFile, outFile+"_L", 200, 200);
	        this.changeImageSize(outFile, outFile+"_S", 145, 145);
	    }catch(Exception e){
	        e.printStackTrace();
	    }
	}
	
	public static void main(String[] args) {
		GIFConvertor gifConvertor = new GIFConvertor();
		try {
			gifConvertor.makeImgFile("/Users/dsyoon/Downloads/1.png", "/Users/dsyoon/Downloads/1.gif");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
