package com.posin.command;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import com.posin.global.SocketConstant;
import com.posin.utils.StringUtils;

public class OpenFunctionTest {

	public static void main(String[] args) {
		BufferedReader br = null;
		OutputStream os = null;
		InputStream is = null;
		Socket socket = null;
		try {
			// 1.�����ͻ���Socket��ָ����������ַ�Ͷ˿�
			System.out.println("satrting connection server");
			socket = new Socket("localhost", 8888);
			// 2.��ȡ���������������˷�����Ϣ
			os = socket.getOutputStream();// �ֽ������

			String command = StringUtils.SpliceString(
					SocketConstant.SOCKET_MESSAGE_HEAD,
					SocketConstant.OPEN_FUNCTIONTEST,
					SocketConstant.SOCKET_MESSAGE_TAIL, "\n");
			System.out.println("starting send to command");
			os.write(command.getBytes("utf-8"));

			System.out.println("starting read receriver");
			// 3.��ȡ������������ȡ�������˵���Ӧ��Ϣ
			is = socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			String info = null;
			while ((info = br.readLine()) != null) {
				System.out.println("server return message is:" + info);
				if (info.equals(SocketConstant.RECEIVE_MESSAGE_SUCCESS)) {
					break;
				}

			}
			System.out.println("I have jumped out of the loop ");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 4.�ر���Դ
				if (socket != null) {
					socket.close();
				}
				if (br != null) {
					br.close();
				}
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}

			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
