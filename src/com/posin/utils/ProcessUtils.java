package com.posin.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.posin.global.Appconfig;
import com.posin.utils.Proc.InputRunnable;
import com.posin.utils.Proc.MyStreamWriter;
import com.posin.utils.Proc.MySuProcess;

public class ProcessUtils {

	// private static MySuProcess mSu;

	/**
	 * ����ָ���Process
	 * 
	 * @return Process
	 * @throws IOException
	 */
	public Process createSuProcess() throws IOException {
		return Runtime.getRuntime().exec("su");
	}

	/**
	 * 
	 * @param cmd
	 *            ָ��
	 * @return Process
	 * @throws IOException
	 */
	public Process createSuProcess(String cmd) throws IOException {

		Process process = createSuProcess();
		DataOutputStream os = null;

		try {
			os = new DataOutputStream(process.getOutputStream());
			os.write((cmd + "\n").getBytes("utf-8"));
			os.write(("exit $?\n").getBytes("utf-8"));
			os.flush();
		} catch (Throwable e) {
			if (os != null) {
				os.close();
			}
		}
		return process;
	}

	/**
	 * ������ֵprocess����
	 * 
	 * @param process
	 * @param cmd
	 * @param callback
	 * @param timeout
	 * @return
	 * @throws IOException
	 */
	public synchronized int suExecCallback(String cmd, Callback callback,
			int timeout) throws IOException {
		Process process = createSuProcess();
		// if (mSu == null) {
		new Thread(new InputRunnable(process.getInputStream(), callback))
				.start();
		// readInputStream(process.getInputStream(), callback);
		MySuProcess mSu = new MySuProcess();
		// }
		return mSu.exec(process, cmd, "utf-8", callback, timeout);
	}

	/**
	 * ������ֵprocess����
	 * 
	 * @param process
	 * @param cmd
	 * @param callback
	 * @param timeout
	 * @return
	 * @throws IOException
	 */
	public synchronized int suExecCallbackByCode(String cmd, String decode,
			Callback callback, int timeout) throws IOException {
		Process process = createSuProcess();
		// if (mSu == null) {
		new Thread(new InputRunnable(process.getInputStream(), callback))
				.start();
		// readInputStream(process.getInputStream(), callback);
		MySuProcess mSu = new MySuProcess();
		// }
		System.out
				.println("----------------------------------------------------------------------------");
		System.out
				.println("----------------------------------------------------------------------------");
		System.out.println("set ssid code: " + decode);
		System.out
				.println("----------------------------------------------------------------------------");
		System.out
				.println("----------------------------------------------------------------------------");
		return mSu.exec(process, cmd, decode, callback, timeout);
	}

	/**
	 * ִ������
	 * 
	 * @author Greetty
	 * 
	 */
	public class MySuProcess {

		public int exec(Process process, String cmd, String decode,
				Callback callback, int timeout) throws IOException {
			System.out.println("exec cmd : " + cmd);
			System.out.println("cmd output code : " + decode);
			OutputStream os = process.getOutputStream();
			os.write(cmd.getBytes(decode));

			String endTip = "echo **CMD-RESULT**" + "\n";
			// System.out.println("endTip :  " + endTip);
			os.write(endTip.getBytes("utf-8"));
			// os.write("echo 111111111111111 \n".getBytes());
			os.flush();
			return 0;
		}
	}

	/**
	 * ��ȡ�ļ�������
	 * 
	 * @param mInputStream
	 *            InputStream
	 * @param mCallback
	 *            �ص�����
	 */
	public void readInputStream(InputStream mInputStream, Callback mCallback) {
		try {
			InputStreamReader reader = new InputStreamReader(mInputStream,
					"utf-8");
			BufferedReader bf = new BufferedReader(reader);
			String line = null;

			while ((line = bf.readLine()) != null) {
				// System.out.println(line);
				if (mCallback != null) {
					mCallback.readLine(line);
				}

			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ����ֵ
	 * 
	 * @author Greetty
	 * 
	 */
	private static class InputRunnable implements Runnable {

		private InputStream mInputStream = null;
		private Callback mCallback = null;

		public InputRunnable(InputStream inputStream, Callback callback) {
			this.mInputStream = inputStream;
			this.mCallback = callback;
		}

		@Override
		public void run() {
			try {
				Reader reader = new InputStreamReader(mInputStream);
				BufferedReader bf = new BufferedReader(reader);
				String line = null;
				while ((line = bf.readLine()) != null) {
					// System.out.println("InputRunnable read gbk: " + line);
					if (mCallback != null) {
						mCallback.readLine(line);
					}
				}
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
				e.printStackTrace();
			}
		}

	}

	/**
	 * ��ȡ���ݣ��ص��ӿ�
	 * 
	 * @author Greetty
	 * 
	 */
	public static interface Callback {
		public void readLine(String line);
	}

}
