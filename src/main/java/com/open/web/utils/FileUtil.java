package com.open.web.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import org.apache.commons.lang.StringUtils;

public class FileUtil {

	public static void main(String[] args) {
		/*String dirName = "d:/FH/topic/";// 创建目录
		FileUtil.createDir(dirName);*/
		
		delDirAll("C:\\Users\\lxw\\workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\IcpCheckPlatform\\uploads\\checkData\\hecharenwu1");
	}
	
	/**
	 * @category 读取文件
	 * 
	 * @param file
	 * 
	 * @author npf
	 * 
	 * @return List<String>
	 */
	public static String readerFileToString(File file) {
		BufferedReader br;
		String listTask= "";
		String temp=null;
		try {
			br = new BufferedReader(new FileReader(file));
			while (br.ready()) {
				temp=br.readLine();
				if(null!=temp&&!temp.trim().equals(""))
				listTask+=temp;
			}
			br.close();
			return listTask;
		} catch (FileNotFoundException e) {
			System.out.println("FileUtil类中readerFileToList方法，文件没有被找到！");
			return null;
		} catch (IOException e) {
			System.out.println("FileUtil类中readerFileToList方法，读取文件出现异常！");
			return null;
		}

	}

	/**
	 * @author 张健 创建文件目录
	 */
	public static boolean bulidDirectory(String path) {
		
		boolean creadok = false;
		
		try {
			
			if(!StringUtils.isBlank(path)){
				
		         byte[] b = path.getBytes("UTF-8");//编码  
		         
		         String newPath = new String(b, "UTF-8");//解码  
		         
		         File dirFile = new File(newPath);
					
				if (!(dirFile.exists()) && !(dirFile.isDirectory())) {
					
					creadok = dirFile.mkdirs();
				}
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return creadok;
	}
	
	/**
	 * 创建目录
	 * 
	 * @param destDirName
	 *            目标目录名
	 * @return 目录创建成功返回true，否则返回false
	 */
	public static boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.exists()) {
			return false;
		}
		if (!destDirName.endsWith(File.separator)) {
			destDirName = destDirName + File.separator;
		}
		// 创建单个目录
		if (dir.mkdirs()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filePathAndName
	 *            String 文件路径及名称 如c:/fqf.txt
	 * @return boolean
	 */
	public static void delFile(String filePathAndName) {
		try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			java.io.File myDelFile = new java.io.File(filePath);
			myDelFile.delete();

		} catch (Exception e) {
			System.out.println("删除文件操作出错");
			e.printStackTrace();

		}

	}
	/**
	 * 删除文件，允许文件不存在
	 * @param filePathAndName
	 */
	public static void delDoc(String filePathAndName) {
		try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			java.io.File myDelFile = new java.io.File(filePath);
			if (myDelFile.exists())
			myDelFile.delete();

		} catch (Exception e) {
			System.out.println("删除文件操作出错");
			e.printStackTrace();

		}

	}
	
	/**
	 * 删除目录下所有文件以及子目录（包括目录本身）
	 * @param pathname
	 */
	public static void delDirAll(String pathname) {
		
		File dir = new File(pathname);
		
		if (dir.isDirectory()) {
			
			File[] files = dir.listFiles();
			
			for (File file : files) {
				
				if(file.isDirectory()){
					
					delDirAll(file.getPath());
				}
				
				file.delete();
			}
			
			dir.delete();
		}
	}

	/**
	 * 读取到字节数组0
	 * 
	 * @param filePath //路径
	 * @throws IOException
	 */
	public static byte[] getContent(String filePath) throws IOException {
		File file = new File(filePath);
		long fileSize = file.length();
		if (fileSize > Integer.MAX_VALUE) {
			System.out.println("file too big...");
			return null;
		}
		FileInputStream fi = new FileInputStream(file);
		byte[] buffer = new byte[(int) fileSize];
		int offset = 0;
		int numRead = 0;
		while (offset < buffer.length
				&& (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
			offset += numRead;
		}
		// 确保所有数据均被读取
		if (offset != buffer.length) {
			fi.close();
			throw new IOException("Could not completely read file "+ file.getName());
		}
		fi.close();
		return buffer;
	}

	/**
	 * 读取到字节数组1
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray(String filePath) throws IOException {

		File f = new File(filePath);
		if (!f.exists()) {
			throw new FileNotFoundException(filePath);
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(f));
			int buf_size = 1024;
			byte[] buffer = new byte[buf_size];
			int len = 0;
			while (-1 != (len = in.read(buffer, 0, buf_size))) {
				bos.write(buffer, 0, len);
			}
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			bos.close();
		}
	}

	/**
	 * 读取到字节数组2
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray2(String filePath) throws IOException {

		File f = new File(filePath);
		if (!f.exists()) {
			throw new FileNotFoundException(filePath);
		}

		FileChannel channel = null;
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(f);
			channel = fs.getChannel();
			ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
			while ((channel.read(byteBuffer)) > 0) {
				// do nothing
				// System.out.println("reading");
			}
			return byteBuffer.array();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Mapped File way MappedByteBuffer 可以在处理大文件时，提升性能
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray3(String filePath) throws IOException {

		FileChannel fc = null;
		RandomAccessFile rf = null;
		try {
			rf = new RandomAccessFile(filePath, "r");
			fc = rf.getChannel();
			MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0,
					fc.size()).load();
			//System.out.println(byteBuffer.isLoaded());
			byte[] result = new byte[(int) fc.size()];
			if (byteBuffer.remaining() > 0) {
				// System.out.println("remain");
				byteBuffer.get(result, 0, byteBuffer.remaining());
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				rf.close();
				fc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
