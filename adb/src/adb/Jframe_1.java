package adb;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Jframe_1 {
	static JFrame jf;
	static ImageIcon img;
	public static JLabel imgLabel;
	public static JLabel lb;
	public static int index = 0;
	public static int x = 0, y = 0, ex = 0, ey = 0;

	public static String url = "D:\\";
	public static String name = "a.jpeg";
	public static void UI() {
		jf = new JFrame("Jframe");
		jf.setLayout(new FlowLayout());
		jf.setSize(360, 680); // 设定窗体的宽和高
		jf.setLocation(500, 100); // 设定窗体的坐标
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		img = new ImageIcon("D:/a.jpeg");// 这是背景图片
		imgLabel = new JLabel(img);// 将背景图放在标签里。
		jf.add(imgLabel);
		jf.setVisible(true);
		// 添加标签到窗口上
		jf.addMouseListener(new MouseListener() { // 为窗口添加鼠标事件监听器
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				if (e.getButton() == e.BUTTON1) {
					String command1 = "adb shell screencap -p /sdcard/temp.jpeg";

					String command2 = "adb pull /sdcard/temp.jpeg D:/a.jpeg";
 
					ScreenCapture.cmdExecute(command1);
					ScreenCapture.cmdExecute(command2);

					System.out.println("裁剪完成");
					if (index % 2 == 0) {
						x = e.getX();
						y = e.getY();
						System.out.println(" x " + x + "  y " + y);
					} else {
						ex = e.getX();
						ey = e.getY(); 
						System.out.println(" ex " + ex + "  ey " + ey);
					}
					int time = 0;
					if(ex>x){
						float u = ((ex - x)*(ex - x)) +((y - ey)*(y - ey));
						float n =(float) Math.sqrt(u);
						time = (int) (n*4.0);
						System.out.println("n1   "+n +"  time  "+time); 
					}else{
						float u = ((x - ex)*(x - ex)) +((ey - y)*(ey - y));
						float n =(float) Math.sqrt(u);
						time = (int) (n*4.0);
						System.out.println("n2   "+n +"  time  "+time); 
					}
					String junmp = "adb shell input swipe 500 500 501 501 "+time;
					if(index>0){
						ScreenCapture.cmdExecute(junmp);
					}

					index++;
					ImgCompressUtil.Tosmallerpic(url, url, name, 360, 640, (float) 1);
					// 判断获取的按钮是否为鼠标的右击
					// 获得鼠标点击位置的坐标并发送到标签的文字上
					setBgd("D:/a.jpeg"); 
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		});

	}

	public static void setBgd(String path) {
		try {
			Icon icon = new ImageIcon(ImageIO.read(new File(path)));
			icon = new ImageIcon(ImageIO.read(new File(path)));
			imgLabel.setIcon(icon);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		UI();
		String command1 = "adb shell screencap -p /sdcard/temp.jpeg";

		String command2 = "adb pull /sdcard/temp.jpeg D:/a.jpeg";

		ScreenCapture.cmdExecute(command1);
		ScreenCapture.cmdExecute(command2);
		ImgCompressUtil.Tosmallerpic(url, url, name, 360, 640, (float) 1);

		setBgd("D:/a.jpeg"); 
		System.out.println("更换完成");
	}

}