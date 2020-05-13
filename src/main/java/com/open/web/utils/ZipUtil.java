package com.open.web.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;



/**
 * @author 张健(TonyZhang)
 * @email messagelook@hotmail.com
 * @creatTime 2009-10-11
 * @Description 
 */
public class ZipUtil {

	/**
	 * @author 张健
	 * 压缩文件(传入路径或文件夹名称).
	 */
	public static ByteArrayOutputStream zip(String src) throws Exception {
		ZipOutputStream out = null;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			out = new ZipOutputStream(byteArrayOutputStream);

			File fileOrDirectory = new File(src);

			if (fileOrDirectory.isFile()) {
				zipFileOrDirectory(out, fileOrDirectory, "");
			} else {
				File[] entries = fileOrDirectory.listFiles();
				for (int i = 0; i < entries.length; i++) {
					// 递归压缩，更新curPaths
					zipFileOrDirectory(out, entries[i], "");
				}
			}

			return byteArrayOutputStream;
		} catch (Exception ex) {
			throw new Exception("压缩失败：" + ex.toString());
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (Exception ex) {
				}
			}
		}

	}

	/**
	 * @author 张健
	 * 压缩文件
	 * 参数为文件列表
	 */
	public static ByteArrayOutputStream zip(List<String> pathList, String folder) throws Exception {
		ZipOutputStream out = null;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		if (pathList == null) {
			return byteArrayOutputStream;
		}
		if(pathList.get(0).equals("nullIpRecord")){
			return byteArrayOutputStream;
		}
		try {
			out = new ZipOutputStream(byteArrayOutputStream);
			Iterator<String> fpIt = pathList.iterator();
			File fileOrDirectory = null;
			while (fpIt.hasNext()) {
				String str = fpIt.next();
				fileOrDirectory = new File(folder + System.getProperty("file.separator") + str);
				if (fileOrDirectory.isFile()) {
					zipFileOrDirectory(out, fileOrDirectory, "");
				} else {
					File[] entries = fileOrDirectory.listFiles();
					for (int i = 0; i < entries.length; i++) {
						// 递归压缩，更新curPaths
						zipFileOrDirectory(out, entries[i], "");
					}
				}
			}
			return byteArrayOutputStream;
		} catch (Exception ex) {
			throw new Exception("压缩失败：" + ex.toString());
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (Exception ex) {
				}
			}
		}

	}

	/**
	 * @author 张健
	 * 递归压缩文件或目录.
	 */
	private static void zipFileOrDirectory(ZipOutputStream out, File fileOrDirectory, String curPath) throws Exception {
		FileInputStream in = null;
		try {
			if (!fileOrDirectory.isDirectory()) {
				// 压缩文件
				byte[] buffer = new byte[4096];
				int bytes_read;
				in = new FileInputStream(fileOrDirectory);
				ZipEntry entry = new ZipEntry(curPath
						+ fileOrDirectory.getName());
				out.putNextEntry(entry);

				while ((bytes_read = in.read(buffer)) != -1) {
					out.write(buffer, 0, bytes_read);
				}
				out.closeEntry();
			} else {
				// 压缩目录
				File[] entries = fileOrDirectory.listFiles();
				for (int i = 0; i < entries.length; i++) {
					// 递归压缩，更新curPaths
					zipFileOrDirectory(out, entries[i], curPath + fileOrDirectory.getName() + File.separator);
				}
			}
		} catch (Exception ex) {
			throw new Exception("递归压缩文件或目录失败：" + ex.toString());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception ex) {
				}
			}
		}
	}

	/**
	 * @author 张健
	 * 解压缩文件
	 */
	public static List<String> extractZip(String zipPath, String folder) {
		List<String> fileNameList = new ArrayList<String>();
		String unzipfile = zipPath; //解压缩的文件名
		FileOutputStream fout = null;
		DataOutputStream dout = null;
		try {
			File olddirec = new File(unzipfile); //解压缩的文件路径(为了获取路径)
			ZipInputStream zin = new ZipInputStream(new FileInputStream(unzipfile));
			ZipEntry entry;
			//创建文件夹
			while ((entry = zin.getNextEntry()) != null) {
				if (entry.isDirectory()) {
					File directory = new File(olddirec.getParent(), entry.getName());
					if (!directory.exists())
						if (!directory.mkdirs())
							System.exit(0);
					zin.closeEntry();
				}
				if (!entry.isDirectory()) {
					File myFile = new File(entry.getName());
					String fileName = myFile.getPath();
					fileNameList.add(fileName);
					fout = new FileOutputStream(folder + System.getProperty("file.separator") + fileName);
					dout = new DataOutputStream(fout);
					byte[] b = new byte[1024];
					int len = 0;
					while ((len = zin.read(b)) != -1) {
						dout.write(b, 0, len);
					}
					
				}
			}
			
			if(dout != null){dout.close();}
			if(fout != null){fout.close();}
			zin.closeEntry();
			zin.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return fileNameList;
	}

	/**
	 * @author 张健
	 * 从文件形式转换为输入流
	 *
	 * @param filePath
	 * @return
	 */
	public static ByteArrayOutputStream fileToOutFlow(String filePath) {
		try {
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePath));
			ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
			System.out.println("Available bytes:" + in.available());

			byte[] temp = new byte[1024];
			int size = 0;
			while ((size = in.read(temp)) != -1) {
				out.write(temp, 0, size);
			}
			in.close();
			return out;
		} catch (IOException e) {			 
			return  new ByteArrayOutputStream(1024);
		}
		

	}
	
	/**
	
     * zip解压  

     * @param srcFile        zip源文件

     * @param destDirPath     解压后的目标文件夹

     * @throws RuntimeException 解压失败会抛出运行时异常

     */

    public static void unZip(File srcFile, String destDirPath) throws RuntimeException {

        long start = System.currentTimeMillis();

        // 判断源文件是否存在

        if (!srcFile.exists()) {

            throw new RuntimeException(srcFile.getPath() + "所指文件不存在");

        }

        // 开始解压

        ZipFile zipFile = null;

        try {

            zipFile = new ZipFile(srcFile, Charset.forName("GBK"));//设置编码(如果文件名有用中文的,会报java.lang.IllegalArgumentException: MALFORMED)

            Enumeration<?> entries = zipFile.entries();

            while (entries.hasMoreElements()) {

                ZipEntry entry = (ZipEntry) entries.nextElement();

                System.out.println("解压" + entry.getName());

                // 如果是文件夹，就创建个文件夹

                if (entry.isDirectory()) {

                    String dirPath = destDirPath + "/" + entry.getName();

                    File dir = new File(dirPath);

                    dir.mkdirs();

                } else {

                    // 如果是文件，就先创建一个文件，然后用io流把内容copy过去

                    File targetFile = new File(destDirPath + "/" + entry.getName());

                    // 保证这个文件的父文件夹必须要存在

                    if(!targetFile.getParentFile().exists()){

                        targetFile.getParentFile().mkdirs();

                    }

                    targetFile.createNewFile();

                    // 将压缩文件内容写入到这个文件中

                    InputStream is = zipFile.getInputStream(entry);

                    FileOutputStream fos = new FileOutputStream(targetFile);

                    int len;

                    byte[] buf = new byte[1024];

                    while ((len = is.read(buf)) != -1) {

                        fos.write(buf, 0, len);

                    }

                    // 关流顺序，先打开的后关闭

                    fos.close();

                    is.close();

                }

            }

            long end = System.currentTimeMillis();

            System.out.println("解压完成，耗时：" + (end - start) +" ms");

        } catch (Exception e) {

            throw new RuntimeException("unzip error from ZipUtil", e);

        } finally {

            if(zipFile != null){

                try {

                    zipFile.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }

        }

    }

	public static void main(String args[]) throws FileNotFoundException {
		ZipUtil.unZip(new File("C:\\Users\\lxw\\Desktop\\核查任务.zip"),"C:\\Users\\lxw\\Desktop\\zip\\");
//		 byte[] by = fileToOutFlow("D:\\321.zip").toByteArray();
//		FileOutputStream fo = new FileOutputStream("D:\\321123.zip");
//		try {
//
//			fo.write(by);
//			fo.close();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}
}
