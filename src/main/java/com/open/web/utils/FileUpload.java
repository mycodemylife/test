package com.open.web.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUpload {
	
	/**
	 * 保存上传文件到固定文件夹中
	 * @param request
	 * @param file 上传文件
	 * @param folder 上传文件夹
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String saveFile(HttpServletRequest request, MultipartFile file, String folder) throws FileNotFoundException, IOException {
		
		String upFileName = "";
		
		try {
			
			String uploadPath = request.getSession().getServletContext().getRealPath("uploads");
			
			File upDir = new File(uploadPath + File.separator + folder);
			
			if (!upDir.exists()) {
				
				upDir.mkdirs();
			}
			
			String extension = FilenameUtils.getExtension(file.getOriginalFilename());
			
			upFileName = UUID.randomUUID().toString().replaceAll("-", "")+ "." + extension;
			
			File upFile = new File(uploadPath + File.separator + folder + File.separator + upFileName);
			
			FileOutputStream fos = new FileOutputStream(upFile);
			
			IOUtils.copy(file.getInputStream(), fos);
			
			IOUtils.closeQuietly(fos);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			System.out.println("文件：" + file.getOriginalFilename() + "上传异常");
		}
		
		return upFileName;
	}
}
