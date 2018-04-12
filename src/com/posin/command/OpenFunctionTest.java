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
			// 1.创建客户端Socket，指定服务器地址和端口
			System.out.println("satrting connection server");
			socket = new Socket("localhost", 8888);
			// 2.获取输出流，向服务器端发送信息
			os = socket.getOutputStream();// 字节输出流

			String command = StringUtils.SpliceString(
					SocketConstant.SOCKET_MESSAGE_HEAD,
					SocketConstant.OPEN_FUNCTIONTEST,
					SocketConstant.SOCKET_MESSAGE_TAIL, "\n");
			System.out.println("starting send to command");
			os.write(command.getBytes("utf-8"));

			System.out.println("starting read receriver");
			// 3.获取输入流，并读取服务器端的响应信息
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
				// 4.关闭资源
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
