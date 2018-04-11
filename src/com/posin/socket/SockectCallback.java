package com.posin.socket;

/**
 * 接收数据
 * 
 * @author Greetty
 * 
 */
public interface SockectCallback {

	/**
	 * 接收到的指令
	 * 
	 * @param command
	 */
	void receiveCommad(String command);

}
