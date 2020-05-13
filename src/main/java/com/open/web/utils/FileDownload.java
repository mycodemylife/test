package com.open.web.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

/**
 * 下载文件
 * 创建人：FH 创建时间：2014年12月23日
 * @version
 */
public class FileDownload {

	/**
	 * @param response 
	 * @param filePath		//文件完整路径(包括文件名和扩展名)
	 * @param fileName		//下载后看到的文件名
	 * @return  文件名
	 */
	public static void fileDownload(final HttpServletResponse response, String filePath, String fileName) throws Exception{  
		
		try {
			
			byte[] data = FileUtil.toByteArray2(filePath);
		    
		    fileName = URLEncoder.encode(fileName, "UTF-8");
		    
		    response.reset();
		    
		    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		    
		    response.addHeader("Content-Length", "" + data.length);
		    
		    response.setContentType("application/octet-stream;charset=UTF-8");
		    
		    OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
		    
		    outputStream.write(data);
		    
		    outputStream.flush();
		    
		    outputStream.close();
		    
		    response.flushBuffer();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			System.out.println("下载文件：" + filePath + "异常");
		}
	}
		
	/*
	 * 文件下载：按照指定路径 + 文件名
	 */
	public void Download(HttpServletResponse response, String path, String name){
		
		File file = new File(path);
		
		InputStream in = null;
		
		OutputStream out = null;
		
		try {
			
			in = new FileInputStream(file);
			
			byte[] bty = new  byte[(int)file.length()];
			
			try {
				
				in.read(bty);
				
				response.reset();
				
				response.setContentType("application/x-msdownload");
				
			    response.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
			    
			    response.addHeader("Content-Length", "" + bty.length);
			    
			    out=response.getOutputStream();
			    
			    out.write(bty);
			    
				out.flush();
				
				out.close();
				
			    response.flushBuffer();
			   
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
