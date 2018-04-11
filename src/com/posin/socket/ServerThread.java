package com.posin.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import com.posin.global.Appconfig;
import com.posin.global.SocketConstant;
import com.posin.utils.StringUtils;

/*
 * 服务器线程处理类
 */
public class ServerThread extends Thread {

	private Socket socket = null;
	private SockectCallback mScokectCallback = null;
	private int messageHeadLength = 0; // 指令头长度
	private int messageTaiLength = 0; // 指令尾长度

	public ServerThread(Socket socket, SockectCallback sockectCallback) {
		this.socket = socket;
		this.mScokectCallback = sockectCallback;
	}

	// 线程执行的操作，响应客户端的请求
	public void run() {

		messageHeadLength = SocketConstant.SOCKET_MESSAGE_HEAD.length();
		messageTaiLength = SocketConstant.SOCKET_MESSAGE_TAIL.length();

		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		OutputStream os = null;
		PrintWriter pw = null;
		try {
			is = socket.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			os = socket.getOutputStream();

			String info = null;
			while ((info = br.readLine()) != null) {// 循环读取客户端的信息
				System.out.println("The server receives data as： " + info);
				if (info.length() > (messageHeadLength + messageTaiLength)) {
					String command = info.substring(messageHeadLength,
							info.length() - messageTaiLength);
					System.out.println("Received instruction is： " + command);
					mScokectCallback.receiveCommad(command);
					os.write(StringUtils.SpliceString(
							SocketConstant.RECEIVE_MESSAGE_SUCCESS, "\n")
							.getBytes("utf-8"));
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			try {
				if (pw != null)
					pw.close();
				if (os != null)
					os.close();
				if (br != null)
					br.close();
				if (isr != null)
					isr.close();
				if (is != null)
					is.close();
				if (socket != null)
					socket.close();
				System.out.println("This client thread terminated. ");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
