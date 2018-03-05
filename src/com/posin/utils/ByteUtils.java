package com.posin.utils;

import java.nio.ByteBuffer;

public class ByteUtils {
	
	public static String bytesToHexString(byte[] data) {
	        StringBuilder sb = new StringBuilder();
	        for (int i = 0; i < data.length; i++)
	            sb.append(String.format("%02X ", data[i]));
	        return sb.toString();
	    }

	public static byte[] hexStringToBytes(String txt) throws Exception {
	        txt = txt.toLowerCase();
	        String[] items = txt.split(" ");
	        ByteBuffer bb = ByteBuffer.allocate(1024);
	        for (String t : items) {
	            if (t.length() == 1)
	                bb.put((byte) Character.digit(t.charAt(0), 16));
	            else if (t.length() == 2) {
	                int data = (Character.digit(t.charAt(0), 16) << 4)
	                        | Character.digit(t.charAt(1), 16);
	                bb.put((byte) data);
	            } else {
	                throw new Exception("error : unknow hex string format.");
	            }
	        }
	        if (bb.position() > 0) {
	            byte[] result = new byte[bb.position()];
	            bb.flip();
	            bb.get(result);
	            return result;
	        }
	        return null;
	    }

}
