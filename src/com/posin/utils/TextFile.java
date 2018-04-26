package com.posin.utils;

import java.io.FileInputStream;
import java.io.IOException;

public class TextFile {

	public static String loadAsString(String filename, int max)
			throws IOException {
		FileInputStream is = null;
		byte[] data = new byte[max];

		try {
			is = new FileInputStream(filename);
			int size = is.read(data);
			if (size > 0)
				return new String(data, 0, size);
			return "";
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

}
