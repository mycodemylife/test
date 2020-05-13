package com.open.web.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * java压缩成zip 创建人：FH 创建时间：2015年1月14日
 * 
 * @version
 */
public class FileZip {

	/**
	 * @param inputFileName
	 *            你要压缩的文件夹(整个完整路径)
	 * @param zipFileName
	 *            压缩后的文件(整个完整路径)
	 */
	public static void zip(String inputFileName, String zipFileName)
			throws Exception {
		zip(zipFileName, new File(inputFileName));
	}

	private static void zip(String zipFileName, File inputFile)
			throws Exception {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				zipFileName));
		zip(out, inputFile, "");
		out.flush();
		out.close();
	}

	private static void zip(ZipOutputStream out, File f, String base)
			throws Exception {
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			out.putNextEntry(new ZipEntry(base + "/"));
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + fl[i].getName());
			}
		} else {
			out.putNextEntry(new ZipEntry(base));
			FileInputStream in = new FileInputStream(f);
			int b;
			// System.out.println(base);
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			in.close();
		}
	}

	public static void main(String[] temp) {
		try {

			zip("E:/ftl", "E:/test.zip");// 你要压缩的文件夹 和 压缩后的文件
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/** 压缩单个文件 */
	public static void ZipFileByPath(String filepath, String zippath) {
		try {
			File file = new File(filepath);
			File zipFile = new File(zippath);
			InputStream input = new FileInputStream(file);
			ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(
					zipFile));
			zipOut.putNextEntry(new ZipEntry(file.getName()));
			int temp = 0;
			while ((temp = input.read()) != -1) {
				zipOut.write(temp);
			}
			input.close();
			zipOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下
	 * 
	 * @param sourceFilePath
	 *            :待压缩的文件路径
	 * @param zipFilePath
	 *            :压缩后存放路径
	 * @param fileName
	 *            :压缩后文件的名称
	 * @return
	 */
	public static boolean fileToZip(String sourceFilePath, String zipFilePath,
			String fileName) {
		boolean flag = false;
		File sourceFile = new File(sourceFilePath);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;

		if (sourceFile.exists() == false) {
			System.out.println("待压缩的文件目录：" + sourceFilePath + "不存在.");
		} else {
			try {
				File zipFile = new File(zipFilePath + "/" + fileName + ".zip");
				if (zipFile.exists()) {
					System.out.println(zipFilePath + "目录下存在名字为:" + fileName
							+ ".zip" + "打包文件.");
				} else {
					File[] sourceFiles = sourceFile.listFiles();
					if (null == sourceFiles || sourceFiles.length < 1) {
						System.out.println("待压缩的文件目录：" + sourceFilePath
								+ "里面不存在文件，无需压缩.");
					} else {
						fos = new FileOutputStream(zipFile);
						zos = new ZipOutputStream(new BufferedOutputStream(fos));
						byte[] bufs = new byte[1024 * 10];
						for (int i = 0; i < sourceFiles.length; i++) {
							// 创建ZIP实体，并添加进压缩包
							ZipEntry zipEntry = new ZipEntry(
									sourceFiles[i].getName());
							zos.putNextEntry(zipEntry);
							// 读取待压缩的文件并写进压缩包里
							fis = new FileInputStream(sourceFiles[i]);
							bis = new BufferedInputStream(fis, 1024 * 10);
							int read = 0;
							while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
								zos.write(bufs, 0, read);
							}
						}
						flag = true;
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} finally {
				// 关闭流
				try {
					if (fis != null)
						fis.close();
					if (null != bis)
						bis.close();
					if (null != zos)
						zos.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		return flag;
	}

	/*
	 * 压缩
	 */
	public static File zipFile(File sourceFile, String zipFileName)
			throws IOException {
		// ZipOutputStream 用来输出ZIP流
		ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(
				zipFileName));
		// 被压缩文件输入流
		FileInputStream in = new FileInputStream(sourceFile);
		// 定位ZIP文件中的文件位置
		zipOut.putNextEntry(new ZipEntry(sourceFile.getName()));
		// 将 被压缩文件输入流 通过 zipOut 去写入 zip文件 ，具体位置即 putNextEntry 的定位
		int b;
		while ((b = in.read()) != -1) {
			zipOut.write(b);
		}
		in.close();
		zipOut.close();
		File returnFile = new File(zipFileName);
		return returnFile;
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;

	}

	public static boolean zips(String tsFile, String destFile) {
		boolean isZip = false;
		// 如果为目标文件名或者需要压缩文件名为空，则为返回false
		if (destFile == null || destFile.length() == 0 || tsFile == null
				|| tsFile == "") {
			return false;
		}
		// 如果目标文件名不是以‘zip’结尾，则加上‘zip’后缀
		if (!destFile.toLowerCase().endsWith(".zip")) {
			destFile = destFile + ".zip";
		}
		try {
			File temp = new File(destFile);
			File temp2 = new File(temp.getParent());
			if (!temp2.exists()) {
				temp2.mkdir();
			}
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					destFile));

			File inputFile = new File(tsFile);
			zip(out, inputFile, "");

			out.close();
			isZip = true;
		} catch (Exception e) {
			isZip = false;
		}
		return isZip;
	}

}

// =====================文件压缩=========================
/*
 * //把文件压缩成zip File zipFile = new File("E:/demo.zip"); //定义输入文件流 InputStream
 * input = new FileInputStream(file); //定义压缩输出流 ZipOutputStream zipOut = null;
 * //实例化压缩输出流,并制定压缩文件的输出路径 就是E盘下,名字叫 demo.zip zipOut = new ZipOutputStream(new
 * FileOutputStream(zipFile)); zipOut.putNextEntry(new
 * ZipEntry(file.getName())); //设置注释 zipOut.setComment("www.demo.com"); int temp
 * = 0; while((temp = input.read()) != -1) { zipOut.write(temp); }
 * input.close(); zipOut.close();
 */
// ==============================================
