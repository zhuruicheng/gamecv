package adb;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

import org.w3c.dom.Element;

public class ImgCompressUtil {
	public static List<PyPoint> point = new ArrayList();

	/**
	 * 图片压缩测试
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		// 读文件夹所有文件进行压缩处理
		// String f = "/var/upload_bak/";
		// File file = new File(f);
		//
		// if(file.exists())
		// {
		// File[] filelist = file.listFiles();
		// for(int i = 0;i<filelist.length;i++)
		// {
		// String n = filelist[i].getName();
		// Tosmallerpic(f,filelist[i],"_middle",n,185,160,(float)0.7);
		// Tosmallerpic(f,filelist[i],"_small",n,45,45,(float)0.7);
		// Tosmallerpic(f,filelist[i],"_smaller",n,116,100,(float)0.7);
		// }
		// }

		// 对一个文件进行压缩处理

	}

	/**
	 * 裁剪图片方法
	 * 
	 * @param bufferedImage
	 *            图像源
	 * @param startX
	 *            裁剪开始x坐标
	 * @param startY
	 *            裁剪开始y坐标
	 * @param endX
	 *            裁剪结束x坐标
	 * @param endY
	 *            裁剪结束y坐标
	 * @return
	 */
	public static BufferedImage cropImage(BufferedImage bufferedImage, int startX, int startY, int endX, int endY) {
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		if (startX == -1) {
			startX = 0;
		}
		if (startY == -1) {
			startY = 0;
		}
		if (endX == -1) {
			endX = width - 1;
		}
		if (endY == -1) {
			endY = height - 1;
		}
		BufferedImage result = new BufferedImage(endX - startX, endY - startY, 4);
		for (int x = startX; x < endX; ++x) {
			for (int y = startY; y < endY; ++y) {
				int rgb = bufferedImage.getRGB(x, y);
				result.setRGB(x - startX, y - startY, rgb);
			}
		}
		return result;
	}

	/**
	 * 裁剪图片
	 * 
	 * @path 图片路径
	 */
	public static void setImgTofile(String path) {
		File newfile = new File(path);
		BufferedImage bufferedimage;
		try {
			bufferedimage = ImageIO.read(newfile);
			bufferedimage = cropImage(bufferedimage, 0, 112, 288, 470);
			ImageIO.write(bufferedimage, "jpg", new File("D:/c_a.jpeg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param srcFilePath
	 *            图片所在的文件夹路径
	 * @param destFilePath
	 *            存放路径
	 * @param name
	 *            图片名
	 * @param w
	 *            目标宽
	 * @param h
	 *            目标高
	 * @param per
	 *            百分比
	 */
	public static void Tosmallerpic(String srcFilePath, String destFilePath, String name, int w, int h, float per) {
		Image src;
		try {
			src = javax.imageio.ImageIO.read(new File(srcFilePath + File.separator + name)); // 构造Image对象
			String img_midname = destFilePath + File.separator + name;
			int old_w = src.getWidth(null); // 得到源图宽
			int old_h = src.getHeight(null);
			int new_w = 0;
			int new_h = 0; // 得到源图长

			double w2 = (old_w * 1.00) / (w * 1.00);
			double h2 = (old_h * 1.00) / (h * 1.00);

			if (old_w > w)
				new_w = (int) Math.round(old_w / w2);
			else
				new_w = old_w;
			if (old_h > h)
				new_h = (int) Math.round(old_h / h2);// 计算新图长宽
			else
				new_h = old_h;
			BufferedImage image_to_save = new BufferedImage(new_w, new_h, BufferedImage.TYPE_INT_RGB);
			image_to_save.getGraphics().drawImage(src.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH), 0, 0, null);
			FileOutputStream fos = new FileOutputStream(img_midname); // 输出到文件流

			saveAsJPEG(100, image_to_save, per, fos);

			fos.close();
		} catch (IOException ex) {
			// Logger.getLogger(Img_Middle.class.getName()).log(Level.SEVERE,
			// null, ex);
		}
	}

	/**
	 * 读取一张图片的RGB值
	 * 
	 * @throws Exception
	 */
	public static void getImagePixel(String image, boolean isLeft) throws Exception {
		int[] rgb = new int[3];
		File file = new File(image);
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int width = bi.getWidth();
		int height = bi.getHeight();
		int minx = bi.getMinX();
		int miny = bi.getMinY();
		int index = 0;
		// 背景色的色值
		String bgdc = "";
		if (isLeft) {
			for (int i = minx; i < width; i++) {
				for (int j = miny; j < height; j++) {
					index++;
					int pixel = bi.getRGB(i, j); // 下面三行代码将一个数字转换为RGB数字
					rgb[0] = (pixel & 0xff0000) >> 16;
					rgb[1] = (pixel & 0xff00) >> 8;
					rgb[2] = (pixel & 0xff);
					if (bgdc == "") {
						bgdc = rgb[0] + "" + rgb[1] + "" + rgb[2] + "";
						System.out.print("bgdc   " + bgdc);
					}
					if (!bgdc.equals(rgb[0] + "" + rgb[1] + "" + rgb[2] + "")) {
						System.out.print("   jl   " + "左边 i=" + i + ",j=" + j);
						return;
					}
					// System.out.println("左边 i=" + i + ",j=" + j + ":(" +
					// rgb[0] + ","
					// + rgb[1] + "," + rgb[2] + ")"+" index "+index);

				}
			}
		} else {
			for (int i = width - 1; i >= minx; i--) {
				for (int j = height - 1; j > miny; j--) {
					index++;
					int pixel = bi.getRGB(i, j); // 下面三行代码将一个数字转换为RGB数字
					rgb[0] = (pixel & 0xff0000) >> 16;
					rgb[1] = (pixel & 0xff00) >> 8;
					rgb[2] = (pixel & 0xff);
					// System.out.println("右边 i=" + i + ",j=" + j + ":(" +
					// rgb[0] + ","
					// + rgb[1] + "," + rgb[2] + ")"+" index "+index);
				}
			}
		}

	}

	/**
	 * 图像的二值化
	 * 
	 */
	public static void getImgBinary(String url) {
		// TODO Auto-generated method stub
		BufferedImage image;
		try {
			image = ImageIO.read(new File(url));

			int w = image.getWidth();
			int h = image.getHeight();
			float[] rgb = new float[3];
			double[][] zuobiao = new double[w][h];
			int R = 0;
			float red = 0;
			float green = 0;
			float blue = 0;
			BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);
			;
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					int pixel = image.getRGB(x, y);
					// sop(pixel);
					rgb[0] = (pixel & 0xff0000) >> 16;
					rgb[1] = (pixel & 0xff00) >> 8;
					rgb[2] = (pixel & 0xff);
					red += rgb[0];
					green += rgb[1];
					blue += rgb[2];
					R = (x + 1) * (y + 1);
					float avg = (rgb[0] + rgb[1] + rgb[2]) / 3;
					zuobiao[x][y] = avg;
				}
			}
			double SW = 170;
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					if (zuobiao[x][y] <= SW) {
						int max = new Color(0, 0, 0).getRGB();
						bi.setRGB(x, y, max);
					} else {
						int min = new Color(255, 255, 255).getRGB();
						bi.setRGB(x, y, min);
					}
				}
			}

			ImageIO.write(bi, "jpg", new File(url));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void sop(Object obj) {
		System.out.println(obj);
	}

	/**
	 * 以JPEG编码保存图片
	 * 
	 * @param dpi
	 *            分辨率
	 * @param image_to_save
	 *            要处理的图像图片
	 * @param JPEGcompression
	 *            压缩比
	 * @param fos
	 *            文件输出流
	 * @throws IOException
	 */
	public static void saveAsJPEG(Integer dpi, BufferedImage image_to_save, float JPEGcompression, FileOutputStream fos)
			throws IOException {

		// useful documentation at
		// http://docs.oracle.com/javase/7/docs/api/javax/imageio/metadata/doc-files/jpeg_metadata.html
		// useful example program at
		// http://johnbokma.com/java/obtaining-image-metadata.html to output
		// JPEG data

		// old jpeg class
		// com.sun.image.codec.jpeg.JPEGImageEncoder jpegEncoder =
		// com.sun.image.codec.jpeg.JPEGCodec.createJPEGEncoder(fos);
		// com.sun.image.codec.jpeg.JPEGEncodeParam jpegEncodeParam =
		// jpegEncoder.getDefaultJPEGEncodeParam(image_to_save);

		// Image writer
		// JPEGImageWriter imageWriter = (JPEGImageWriter)
		// ImageIO.getImageWritersBySuffix("jpeg").next();
		ImageWriter imageWriter = ImageIO.getImageWritersBySuffix("jpg").next();
		ImageOutputStream ios = ImageIO.createImageOutputStream(fos);
		imageWriter.setOutput(ios);
		// and metadata
		IIOMetadata imageMetaData = imageWriter.getDefaultImageMetadata(new ImageTypeSpecifier(image_to_save), null);

		if (dpi != null && !dpi.equals("")) {

			// old metadata
			// jpegEncodeParam.setDensityUnit(com.sun.image.codec.jpeg.JPEGEncodeParam.DENSITY_UNIT_DOTS_INCH);
			// jpegEncodeParam.setXDensity(dpi);
			// jpegEncodeParam.setYDensity(dpi);

			// new metadata
			Element tree = (Element) imageMetaData.getAsTree("javax_imageio_jpeg_image_1.0");
			Element jfif = (Element) tree.getElementsByTagName("app0JFIF").item(0);
			jfif.setAttribute("Xdensity", Integer.toString(dpi));
			jfif.setAttribute("Ydensity", Integer.toString(dpi));

		}

		if (JPEGcompression >= 0 && JPEGcompression <= 1f) {

			// old compression
			// jpegEncodeParam.setQuality(JPEGcompression,false);

			// new Compression
			JPEGImageWriteParam jpegParams = (JPEGImageWriteParam) imageWriter.getDefaultWriteParam();
			jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
			jpegParams.setCompressionQuality(JPEGcompression);

		}

		// old write and clean
		// jpegEncoder.encode(image_to_save, jpegEncodeParam);

		// new Write and clean up
		imageWriter.write(imageMetaData, new IIOImage(image_to_save, null, null), null);
		ios.close();
		imageWriter.dispose();

	}

	/**
	 * 彩色转为黑白
	 * 
	 * @param source
	 * @param result
	 */
	public static void gray(String source, String result) {
		try {
			BufferedImage src = ImageIO.read(new File(source));
			ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
			ColorConvertOp op = new ColorConvertOp(cs, null);
			src = op.filter(src, null);
			ImageIO.write(src, "JPEG", new File(result));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取一张图片的RGB值
	 * 
	 * @throws Exception
	 */
	public static int getImagePixel(String image) throws Exception {
		int[] rgb = new int[3];
		File file = new File(image);
		point.clear();
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int width = bi.getWidth();
		int height = bi.getHeight();
		int minx = bi.getMinX();
		int miny = bi.getMinY(); 
		// 横第一点
		hy: for (int j = miny; j < height; j++) {
			for (int i = minx; i < width; i++) {
				int pixel = bi.getRGB(i, j); // 下面三行代码将一个数字转换为RGB数字
				rgb[0] = (pixel & 0xff0000) >> 16;
				rgb[1] = (pixel & 0xff00) >> 8;
				rgb[2] = (pixel & 0xff);
				if (rgb[2] == 0) {
					point.add(new PyPoint(0, i, j));
					break hy;
				}
			}
		}
		zy:
		// 左纵一点
		for (int i = minx; i < width; i++) {
			for (int j = miny; j < height; j++) {
				int pixel = bi.getRGB(i, j); // 下面三行代码将一个数字转换为RGB数字
				rgb[0] = (pixel & 0xff0000) >> 16;
				rgb[1] = (pixel & 0xff00) >> 8;
				rgb[2] = (pixel & 0xff);
				if (rgb[2] == 0) {
					point.add(new PyPoint(1, i, j));
					break zy;
				}
			}
		}
		yz:
		// 右纵一点
		for (int i = width - 1; i > minx; i--) {
			for (int j = miny; j < height; j++) {
				int pixel = bi.getRGB(i, j); // 下面三行代码将一个数字转换为RGB数字
				rgb[0] = (pixel & 0xff0000) >> 16;
				rgb[1] = (pixel & 0xff00) >> 8;
				rgb[2] = (pixel & 0xff);
				if (rgb[2] == 0) {
					point.add(new PyPoint(2, i, j));
					break yz;
				}
			}
		}
		xz:
		// 下横一点
		for (int j = height - 1; j > miny; j--) {
			for (int i = minx; i < width; i++) {

				int pixel = bi.getRGB(i, j); // 下面三行代码将一个数字转换为RGB数字
				rgb[0] = (pixel & 0xff0000) >> 16;
				rgb[1] = (pixel & 0xff00) >> 8;
				rgb[2] = (pixel & 0xff);
				if (rgb[2] == 0) {
					point.add(new PyPoint(3, i, j));
					break xz;
				}
			}
		}
		System.out.println(point);
		return getJump(width,height);
	}

	// 根据测算abc三点的散列值计算要跳的距离
	public static int getJump(int width,int height) {
		int time = 0;
		float bx = 0,by = 0,qx = 0,qy = 0,eqx = 0,eqy = 0;
		double zj = 0;
		
		PyPoint a = point.get(0);
		PyPoint b = point.get(1);
		PyPoint c = point.get(2);
		PyPoint d = point.get(3); 
		if(d.y == height -1&&b.x == 0&&c.y != height -1&&a.x>c.x){
			bx = c.x - b.x;
			zj = bx*2.6;
			System.out.println("q8  zj  "+zj);
		} 
		if(c.x != width-1&&d.x != 0&&d.x <144&&a.x < 144){
			if(c.y - a.y>100){
				bx = c.x - a.x;
				zj = bx*3;
				System.out.println("q11  zj    "+zj); 
			}
		}
		if(b.x == 0&&d.y == height-1&&c.y<b.y){
			bx = c.x;
			zj = bx*2;
			time = (int) zj;
			System.out.println("q13  zj    "+zj); 

		}
		if(d.y == height-1 &&a.x>144&&c.y>a.y){
			bx = a.x - b.x ;
			zj = bx*3.5;
			time = (int) zj;
			System.out.println("q17  time    "+time); 

		}
		if(c.x != width-1&&d.x != 0&&d.x <144&&d.x-b.x<100){
			if(b.y>c.y){
				bx = c.x - b.x;
				zj = bx*2.5;
				System.out.println("q19  zj    "+zj); 
			}
		}
		if (a.x > 144) { 
			
			if(d.y != height-1&&c.x != width-1&&d.x>80){
				bx = c.x - b.x;
				zj = bx*2.8;
				System.out.println("q0  ");
			}
			if(d.x == 0 &&c.x != width-1){
				bx = c.x - b.x;
				zj = bx*2.9;
				System.out.println("q1  ");
			}
			if(d.y == height-1 &&b.x!= 0){
				bx = a.x - b.x;
				zj = bx*3.5;
				System.out.println("q7  ");
			}
			
			if(zj<0){
				zj = -zj;
			}
			time = (int) zj;
			System.out.println("bx  "+bx +"  c.x  "+c.x+"  zj  "+zj+"  time  "+time);// 换行
		}else{
			if(d.x != 0&&c.x == width -1&&a.y>c.y){
				System.out.println("q6  ");
				bx = c.x - a.x;
				zj = bx*3.6;
			}
			if(d.y ==  height-1 && c.x == width -1){
				System.out.println("q5  ");
				bx = c.x - b.x;
				zj = bx*3.0;
			}
			if(d.y ==  height-1 && c.x == width -1&&c.x != width-1){
				System.out.println("q21  ");
				bx = c.x - b.x;
				zj = bx*2.0;
			}
			if(d.y != height-1&&c.x != width-1&&d.y-c.y>80){
				bx = c.x - a.x;
				zj = bx*3.6;
				System.out.println("q4  ");
			}
			time = (int) zj;
			System.out.println("bx  "+bx +"  c.x  "+c.x+"  zj  "+zj+"  time  "+time);// 换行
		}
		return time;
	}

	/**
	 * 扫描输出图片
	 * 
	 * @url 图片路径
	 * @isLeft 左跳还是右跳
	 */
	public static void getImg(String url) {

		BufferedImage bi;
		try {
			bi = (BufferedImage) ImageIO.read(new File(url));
			// 获取图像的宽度和高度
			int width = bi.getWidth();
			int height = bi.getHeight();
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {// 行扫描
					int dip = bi.getRGB(j, i);
					System.out.println();
				}
				// System.out.println();// 换行

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}