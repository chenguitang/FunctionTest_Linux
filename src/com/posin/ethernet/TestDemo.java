package com.posin.ethernet;

public class TestDemo {
	public static void main(String[] args) {

		final MyThread myThread = new MyThread();
		myThread.getInstance().start();

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(8000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				myThread.getInstance().notifyChange();
			}
		}).start();
	}

}
