package com.posin.power;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class InputReader {

	private static final String TAG = "InputReader";

	private final String mName;
	private final InputStream mStream;
	private boolean mRunning = false;
	private boolean mExitPending = false;

	protected abstract void onEvent(int type, int code, int value);

	protected abstract void onTerminated();

	// rk29-keypad
	public InputReader(String name) throws IOException {
		File file = getInputDevicePath(name);
		if (file == null)
			throw new IOException("failed to find input device " + name);

		System.out.println("open " + file.getAbsolutePath());
		mName = name;
		mStream = new FileInputStream(file);

		mRunning = true;
		(new Thread(new Runnable() {
			@Override
			public void run() {
				threadLoop();
			}
		})).start();
	}

	public synchronized void stop() {
		if (!mRunning)
			return;
		mExitPending = true;
		try {
			mStream.close();
		} catch (Throwable e) {
		}
	}

	private synchronized boolean exitPending() {
		return mExitPending;
	}

	private static final int SIZE_OF_EVENT = 16;
	private static final int SIZE_OF_TIMEVAL = 8;

	private void threadLoop() {
		final byte[] buffer = new byte[SIZE_OF_EVENT * 4];

		try {
			while (!exitPending()) {
				int rd = mStream.read(buffer);
				if (rd > 0) {
					// dump(buffer, rd);

					// int count = rd/mEventSize;
					int count = rd / SIZE_OF_EVENT;
					// System.out.println("ev count = " + count);

					ByteBuffer bb = ByteBuffer.wrap(buffer);
					bb.order(ByteOrder.LITTLE_ENDIAN);

					for (int i = 0; i < count; i++) {

						bb.position(i * SIZE_OF_EVENT + SIZE_OF_TIMEVAL); // skip
																			// struct
																			// timeval
																			// 8
																			// byte
						short type = bb.getShort();
						short code = bb.getShort();
						short value = (short) bb.getInt(); // 32 bit

						// System.out.println("key event : type="+type+", code="+code+", value="+value);

						onEvent(type, code, value);
					}
				} else {
					Thread.sleep(10);
					System.out.println("read nothing, try again.");
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			mRunning = false;
			try {
				mStream.close();
			} catch (Throwable e) {
			}
			onTerminated();
		}
	}

	private static File getInputDevicePath(String name) {
		File d = new File("/sys/class/input");
		File[] subs = d.listFiles();
		if (subs == null)
			return null;

		for (File f : subs) {
			if (!f.getName().startsWith("event"))
				continue;

			File n = new File(f, "device/name");
			String devname;

			try {
				devname = readTextFile(n.getAbsolutePath(), 128);
				if (devname != null)
					devname = devname.trim();
				// System.out.println("check dev : '" + devname + "'");
				if (name.equals(devname))
					return new File("/dev/input", f.getName());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static String readTextFile(String fn, int maxSize)
			throws IOException {

		FileInputStream is = null;

		try {
			is = new FileInputStream(fn);

			byte[] buffer = new byte[maxSize];
			int size;

			if ((size = is.read(buffer)) > 0) {
				return new String(buffer, 0, size, "UTF-8");
			}

		} finally {
			if (is != null) {
				is.close();
			}
		}

		return null;
	}

}
