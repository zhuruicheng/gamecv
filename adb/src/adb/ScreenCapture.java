package adb;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

class ScreenCapture {

	public static String[] getDevices() {
		String command = "adb devices";
		System.out.println(command);
		ArrayList devices = new ArrayList();

		try {
			Process process = Runtime.getRuntime().exec(command);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line = bufferedReader.readLine();
			while (line != null) {
				System.out.println(line);
				if (line.endsWith("device")) {
					String device = line.substring(0, line.length() - "device".length()).trim();
					devices.add(device);
				}

				line = bufferedReader.readLine();
			}
			process.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (String[]) devices.toArray(new String[] {});
	}

	public static String getModel(String device) {
		String command = "adb -s " + device + " shell getprop";
		System.out.println(command);
		String model = null;

		try {
			Process process = Runtime.getRuntime().exec(command);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line = bufferedReader.readLine();
			while (line != null) {
				if (line.contains("[ro.product.model]:")) {
					model = line.substring(("[ro.product.model]:").length()).trim();
					model = model.substring(1, model.length() - 1);
					break;
				}
				line = bufferedReader.readLine();
			}
			process.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}

	public static void snapshot(String device, String toPath, String toFile) {
		String temp = "scrsnp.png";
		long t0 = new Date().getTime();
		String command1 = "adb -s " + device + " shell screencap -p /sdcard/" + temp;
		System.out.println(command1);
		cmdExecute(command1);
		long t1 = new Date().getTime();
		System.out.println(t1 - t0);
		String command2 = "adb -s " + device + " pull /sdcard/" + temp + " " + toPath + "/" + toFile;
		System.out.println(command2);
		cmdExecute(command2);
		long t2 = new Date().getTime();
		System.out.println(t2 - t1);
		String command3 = "adb -s " + device + " shell rm /sdcard/" + temp;
		System.out.println(command3);
		cmdExecute(command3);
 	}

	static byte[] fixBytes(byte[] src, int len) {
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		for (int i = 0; i < src.length; i++) {
			if (src[i] == 0x0d) {
				if (i == len) {
					i += 1;
					continue;
				}
				baos.write(src[i]);
			}
		}
		return baos.toByteArray();
	}

	public static void directSnapshot(String device, String toPath, String toFile) {

		try {
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(toPath + "/" + toFile));

			String command = "adb -s " + device + " shell screencap -p";
			Runtime runtime = Runtime.getRuntime();
			Process getProcess = runtime.exec(command);
			BufferedInputStream bis = new BufferedInputStream(getProcess.getInputStream());
			byte[] buf = new byte[1024 * 1024 * 4];
			int len = bis.read(buf);
			while (len != -1) {
				bos.write(fixBytes(buf, len));
				len = bis.read(buf);
			}
			bos.close();
			getProcess.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void cmdExecute(String command) {
		try {
			Process process = Runtime.getRuntime().exec(command);
			process.waitFor();
			process.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void jump(){
		String command1 = "adb shell screencap -p /sdcard/temp.jpeg";

		String command2 = "adb pull /sdcard/temp.jpeg D:/a.jpeg";
		cmdExecute(command1);
		cmdExecute(command2);
		String url = "D:\\";  
		String name  =  "a.jpeg"; 
		  try {
			ImgCompressUtil.Tosmallerpic(url,url,name,288,512,(float)1);
			System.out.println("开始计算"); 
			//裁剪
			ImgCompressUtil.setImgTofile(url+"c_"+name);  
			//二值化
			ImgCompressUtil.getImgBinary(url+"c_"+name);
			//去噪  
			File testDataDir = new File(url+"c_"+name);
		
			ClearImageHelper.cleanImage(testDataDir, url);
			try {
				int time = ImgCompressUtil.getImagePixel(url+"c_"+name);
				String junmp = "adb shell input swipe 500 500 501 501 "+time;
				cmdExecute(junmp);
				Thread.sleep(1000);
				jump();
				System.out.println("第四步完成了"); 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   


	}

	public static void main(String[] args) throws Exception {
		jump();

		// ImgCompressUtil.getImg(url+"c_"+name,true);
		// ImgCompressUtil.getImgBinary(url+"d_"+name);
		// File testDataDir = new File(url+"c_"+name);//去噪
		// ClearImageHelper.cleanImage(testDataDir, url);
	}
}