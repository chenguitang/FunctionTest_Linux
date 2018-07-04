package com.posin.utils;

public class DecodeUtils {

	/**
	 * UTF-8编码格式判断
	 * 
	 * @param bytes
	 *            需要分析的数据
	 * @return 是否为UTF-8编码格式
	 */
	public static boolean isUTF8(byte[] bytes) {
		int score = 0;
		int i, byteslen = 0;
		int goodbytes = 0, asciibytes = 0;
		// Maybe also use UTF8 Byte Order Mark: EF BB BF
		// Check to see if characters fit into acceptable ranges
		byteslen = bytes.length;
		for (i = 0; i < byteslen; i++) {
			if ((bytes[i] & (byte) 0x7F) == bytes[i]) {
				// 最高位是0的ASCII字符
				asciibytes++;
				// Ignore ASCII, can throw off count
			} else if (-64 <= bytes[i]
					&& bytes[i] <= -33
					// -0x40~-0x21
					&& // Two bytes
					i + 1 < byteslen && -128 <= bytes[i + 1]
					&& bytes[i + 1] <= -65) {
				goodbytes += 2;
				i++;
			} else if (-32 <= bytes[i]
					&& bytes[i] <= -17
					&& // Three bytes
					i + 2 < byteslen && -128 <= bytes[i + 1]
					&& bytes[i + 1] <= -65 && -128 <= bytes[i + 2]
					&& bytes[i + 2] <= -65) {
				goodbytes += 3;
				i += 2;
			}
		}
		if (asciibytes == byteslen) {
			return false;
		}
		score = 100 * goodbytes / (byteslen - asciibytes);
		// If not above 98, reduce to zero to prevent coincidental matches
		// Allows for some (few) bad formed sequences
		if (score > 98) {
			return true;
		} else if (score > 95 && goodbytes > 30) {
			return true;
		} else {
			return false;
		}
	}

}
