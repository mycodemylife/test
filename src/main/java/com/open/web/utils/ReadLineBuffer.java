package com.open.web.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

public class ReadLineBuffer {

	private File file;
	private String encoding;
	private boolean end = false;
	private BufferedReader bf;
	private boolean marked = false;
	
	public ReadLineBuffer(String path) {
		this.file = new File(path);
		this.encoding = charset(file);
	}
	
	public ReadLineBuffer(File file) {
		this.file = file;
		this.encoding = charset(file);
	}
	
	public ReadLineBuffer(String path,String encoding) {
		this.file = new File(path);
		this.encoding = encoding;
	}
	
	public ReadLineBuffer(File file,String encoding) {
		this.file = file;
		this.encoding = encoding;
	}
	
	public boolean isFinish() {
		return end;
	}
	
	public boolean isNotFinish() {
		return !end;
	}
	
	public List<String> readFileLines() throws IOException{
		return readFileLines(1000);
	}
	
	/**
	 * 读取文件的指定行数
	 * @param count
	 * @return
	 * @throws IOException
	 */
	public List<String> readFileLines(int count) throws IOException {
		if(!file.exists() || !file.isFile()) {
			throw new RuntimeException("该文件不存在：\"" + file.getPath() + "\"");
		}
		
		if(null == this.bf) {
			this.bf = new LineNumberReader(new InputStreamReader(new FileInputStream(file),encoding));
		}
		
		List<String> list = new ArrayList<>();
		int lines = 0; 
		String s = "";
		if(bf.markSupported() && marked) {
			bf.reset();
		}
        while (lines < count && !this.end) {  
            lines++;
            s = bf.readLine();  
            if(null != s) {  
            	if(StringUtils.isNotBlank(s))
            		list.add(s);
            }else {
				this.end = true;
			}
        }  
        bf.mark(0); 
        marked = true;
		
		return list;
	}
	
	public void close() throws IOException {
		if(null != bf) {
			try {
				bf.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取文件的总行数
	 * @param file
	 * @return
	 * @throws IOException
	 */
    private static int getTotalLines(File file) throws IOException {  
        FileReader in = new FileReader(file);  
        LineNumberReader reader = new LineNumberReader(in);  
        String s = reader.readLine();  
        int lines = 0;  
        while (s != null) {  
            lines++;  
            s = reader.readLine();  
        }  
        reader.close();  
        in.close();  
        return lines;  
    } 
    
    /**
	 * 获取文件编码方式
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private String charset(File file) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            bis.mark(0); // bis.mark(0);修改为 bis.mark(100);我用过这段代码，需要修改上面标出的地方。 
                        // 不过暂时使用正常，遂不改之
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) {
                bis.close();
                return charset; // 文件编码为 ANSI
            } else if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE"; // 文件编码为 Unicode
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE"; // 文件编码为 Unicode big endian
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8"; // 文件编码为 UTF-8
                checked = true;
            }
            bis.reset();
            if (!checked) {
                while ((read = bis.read()) != -1) {
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)
                            // (0x80 - 0xBF),也可能在GB编码内
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) { // 也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("--文件-> [" + file.getPath() + "] 采用的字符集为: [" + charset + "]");
        return charset;
    }
	
	public static void main(String[] args) throws IOException {
		
		File file = new File("D:\\Test\\test_uft16_BE.txt");
		ReadLineBuffer buffer = new ReadLineBuffer(file);
		
		int i = 0;
		int lines = 0;
		while(buffer.isNotFinish()) {
			i ++;
			System.out.println("第 " + i + " 次读取文件 >>>>>>>>>>");
			List<String> list = buffer.readFileLines(10);
			System.out.println("读取内容为：");
			for (String string : list) {
				System.out.println("\t\" " + string + " \"");
			}
			System.out.println("本次读取行数为 ： " + list.size());
			lines += list.size();
 		}
		
		System.out.println("文件读取结束，共读取行数： " + lines);
		buffer.close();
	}
}
