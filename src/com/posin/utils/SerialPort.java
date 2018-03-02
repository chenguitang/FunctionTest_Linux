package com.posin.utils;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPort {

	private static final String TAG = "SerialPortImpl";

	private FileDescriptor mFile = null;
	private InputStream mIn = null;
	private OutputStream mOut = null;

	/**
	 * 校验方式<br>
	 */
	public static final int PARITY_NONE = 0;
	public static final int PARITY_ODD = 1;
	public static final int PARITY_EVEN = 2;
	// public static final int PARITY_MARK =3;
	// public static final int PARITY_SPACE =4;

	/**
	 * 数据位
	 */
	public static final int DATABITS_5 = 5;
	public static final int DATABITS_6 = 6;
	public static final int DATABITS_7 = 7;
	public static final int DATABITS_8 = 8;

	/**
	 * 停止位
	 */
	public static final int STOPBITS_1 = 1;
	public static final int STOPBITS_2 = 2;
	public static final int STOPBITS_1_5 = 3;

	/**
	 * 流控制
	 */
	public static final int FLOWCONTROL_NONE = 0;
	public static final int FLOWCONTROL_RTSCTS_IN = 1;
	public static final int FLOWCONTROL_RTSCTS_OUT = 2;
	public static final int FLOWCONTROL_RTSCTS = FLOWCONTROL_RTSCTS_IN
			| FLOWCONTROL_RTSCTS_OUT;
	public static final int FLOWCONTROL_XONXOFF_IN = 4;
	public static final int FLOWCONTROL_XONXOFF_OUT = 8;
	public static final int FLOWCONTROL_XONXOFF = FLOWCONTROL_XONXOFF_IN
			| FLOWCONTROL_XONXOFF_OUT;

	private native static FileDescriptor jni_open(String path, int baudrate,
			int databits, int parity, int stopbits, int flowControl);

	static {
		System.loadLibrary("PosinSerialPort");
	}

	public SerialPort() {
	}

	public synchronized void open(String port, int baudrate, int databits,
			int parity, int stopbits, int flowControl) throws IOException {
		System.out.println("open serial port : " + port);

		if (mFile != null) {
			throw new IOException("mFile != null");
		}
		mFile = jni_open(port, baudrate, databits, parity, stopbits,
				flowControl);
		if (mFile == null) {
			throw new IOException("failed to open serialport " + port);
		}
	}

	public synchronized void close() {
		if (mFile != null) {
			mFile = null;
		}

		if (mIn != null) {
			try {
				mIn.close();
			} catch (IOException e) {
			}
			mIn = null;
		}

		if (mOut != null) {
			try {
				mOut.close();
			} catch (IOException e) {
			}
			mOut = null;
		}
	}

	public synchronized InputStream getInputStream() throws IOException {
		if (mFile == null)
			throw new IOException("file had been closed.");
		if (mIn == null)
			mIn = new FileInputStream(mFile);
		return mIn;
	}

	public synchronized OutputStream getOutputStream() throws IOException {
		if (mFile == null)
			throw new IOException("file had been closed.");
		if (mOut == null)
			mOut = new FileOutputStream(mFile);
		return mOut;
	}
}
