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
 * �������̴߳�����
 */
public class ServerThread extends Thread {

	private Socket socket = null;
	private SockectCallback mScokectCallback = null;
	private int messageHeadLength = 0; // ָ��ͷ����
	private int messageTaiLength = 0; // ָ��β����

	public ServerThread(Socket socket, SockectCallback sockectCallback) {
		this.socket = socket;
		this.mScokectCallback = sockectCallback;
	}

	// �߳�ִ�еĲ�������Ӧ�ͻ��˵�����
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
			while ((info = br.readLine()) != null) {// ѭ����ȡ�ͻ��˵���Ϣ
				System.out.println("The server receives data as�� " + info);
				if (info.length() > (messageHeadLength + messageTaiLength)) {
					String command = info.substring(messageHeadLength,
							info.length() - messageTaiLength);
					System.out.println("Received instruction is�� " + command);
					mScokectCallback.receiveCommad(command);
					os.write(StringUtils.SpliceString(
							SocketConstant.RECEIVE_MESSAGE_SUCCESS, "\n")
							.getBytes("utf-8"));
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// �ر���Դ
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
