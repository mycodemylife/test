package com.open.web.utils;

import java.security.SecureRandom;
import java.util.Random;

import org.apache.commons.lang.math.RandomUtils;

/**
 * 根据随机数生成随机字符串
 * 
 * @author lh
 * 
 */
public class GeneratePasswordUtil {

	private final static char[] chars = new char[] { 'A', 'B', 'C', 'D', 'E',
			'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T',
			'U', 'V', 'W', 'X', 'Y', 'Z' };
	private final static int[] ints = new int[] { 2, 3, 4, 5, 6, 7, 8, 9 };

	private GeneratePasswordUtil() {
	}

    public static String generateRandomCode() {
        char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
        SecureRandom random = new SecureRandom();
        StringBuffer buffer = new StringBuffer(20);
        for (int i = 0 ; i < 20; i++) {
            buffer.append(BASE62[random.nextInt(BASE62.length)]);
        }
        return buffer.toString();
    }
	
	public static String generateIcpUserPassword_old(int size) {
		if (size % 2 != 0) {
			size = size + 1;
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < size / 2; i++) {
			buffer.append(chars[RandomUtils.nextInt(chars.length)]);
		}

		for (int i = 0; i < size / 2; i++) {
			buffer.append(ints[RandomUtils.nextInt(ints.length)]);
		}
		return buffer.toString();
	}

	// 测试
	public static void main(String[] args) {
			System.out
					.println(GeneratePasswordUtil.generateIcpUserPassword(20));
			
			System.out.println(generateIcpUserPassword_old(20));
	}

	
	/**
	 * 根据随机数生成随机字符串
	 * @param size
	 * @author xzf
	 * @return
	 */
	public static String generateIcpUserPassword(int size) {
		char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
        SecureRandom random = new SecureRandom();
        StringBuffer buffer = new StringBuffer(size);
        for (int i = 0 ; i < size; i++) {
            buffer.append(BASE62[random.nextInt(BASE62.length)]);
        }
        return buffer.toString();
	}
}
