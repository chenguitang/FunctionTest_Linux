package com.posin.global;

public class SocketConstant {

	/**
	 * socket数据指令头
	 */
	public static final String SOCKET_MESSAGE_HEAD = "@*#&0@*#&#&*@";

	/**
	 * socket数据指令尾
	 */
	public static final String SOCKET_MESSAGE_TAIL = "#&*@^";

	/**
	 * 接收数据成功后，服务器向客户端发送此信息，代表成功接收数据
	 */
	public static final String RECEIVE_MESSAGE_SUCCESS = "#@%&*200#end*%*#@";
	
	/**
	 * 打开功能测试指令
	 */
	public static final String OPEN_FUNCTIONTEST="open_functiontest";
	
	/**
	 * 打开工厂设置指令
	 */
	public static final String OPEN_MINIPOS_SETTINGS="open_minipos_settings";
	
	
}
