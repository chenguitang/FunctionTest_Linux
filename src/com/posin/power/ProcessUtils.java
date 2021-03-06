package com.posin.power;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

public class ProcessUtils {

	// private static MySuProcess mSu;

	private static final ProcessUtils PROCESS_UTILS_INSTANCE=new ProcessUtils();
	private ProcessUtils(){}
	public static ProcessUtils getInstance() {
		return PROCESS_UTILS_INSTANCE;
	}
	
	
	/**
	 * 不带指令的Process
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
	 *            指令
	 * @return Process
	 * @throws IOException
	 */
	public Process createSuProcess(String cmd) throws IOException {

		Process process = createSuProcess();
		DataOutputStream os = null;

		try {
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(cmd + "\n");
			os.writeBytes("exit $?\n");
			os.flush();
		} catch (Throwable e) {
			if (os != null) {
				os.close();
			}
		}
		return process;
	}

	/**
	 * 带返回值process进制
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
		return mSu.exec(process, cmd, callback, timeout);
	}

	/**
	 * 执行命令
	 * 
	 * @author Greetty
	 * 
	 */
	public class MySuProcess {

		public int exec(Process process, String cmd, Callback callback,
				int timeout) throws IOException {
			System.out.println("exec cmd : " + cmd);
			OutputStream os = process.getOutputStream();
			os.write(cmd.getBytes());

			String endTip = "echo **CMD-RESULT** \n";
			// System.out.println("endTip :  " + endTip);
			os.write(endTip.getBytes());
			// os.write("echo 111111111111111 \n".getBytes());
			os.flush();
			return 0;
		}
	}

	/**
	 * 读取文件输入流
	 * 
	 * @param mInputStream
	 *            InputStream
	 * @param mCallback
	 *            回调函数
	 */
	public void readInputStream(InputStream mInputStream, Callback mCallback) {
		Reader reader = new InputStreamReader(mInputStream);
		BufferedReader bf = new BufferedReader(reader);
		String line = null;
		try {
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
	 * 获取返回值
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
			Reader reader = new InputStreamReader(mInputStream);
			BufferedReader bf = new BufferedReader(reader);
			String line = null;
			try {
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

	}

	/**
	 * 读取数据，回调接口
	 * 
	 * @author Greetty
	 * 
	 */
	public static interface Callback {
		public void readLine(String line);
	}

}
