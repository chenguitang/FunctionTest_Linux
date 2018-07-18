package com.posin.ethernet;

public class MyThread implements Runnable {
	static Object mEventLock = new Object();
	private boolean mSettingChanged = false;

	final static MyThread myThread = new MyThread();

	public MyThread getInstance() {
		return myThread;
	}
	public void notifyChange() {
		
		synchronized(mEventLock) {
			System.out.println("start notifyChange ... ");
			mEventLock.notifyAll();
			mSettingChanged=true;
		}
	}

	@Override
	public void run() {
		while (true) {
			loop();
		}
	}

	public void start() {
		new Thread(this).start();
	}

	private void loop() {
		synchronized (mEventLock) {
			System.out.println("enter synchronized ");
			if (!mSettingChanged) {

				try {
					System.out.println("start wait ");
					mEventLock.wait(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					System.out.println("end wait ");
				}

				mSettingChanged=false;
			}
		}
	}
}
