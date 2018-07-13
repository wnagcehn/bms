package com.jiuyescm.bms.test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 子线程 和 主线程 协助工作
 * 
 * main 打印一次 sub 打印2次
 * 
 * @ClassName: ThreadMutex
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author ZhangWei
 * @date 2015年8月17日 下午5:24:54
 * 
 */
public class ThreadMutexByThreeThread {

	public static void main(String[] args) {

		final Buiness2 b = new Buiness2();

		new Thread() {
			@Override
			public void run() {
				for (int i = 1; i <= 50; i++) {
					b.sub1();
				}
			}
		}.start();

		new Thread() {
			@Override
			public void run() {
				for (int i = 1; i <= 50; i++) {
					b.sub2();
				}
			}
		}.start();

		for (int i = 1; i <= 50; i++) {
			b.main();
		}
	}

}

class Buiness2 {

	/**
	 * 
	 * 1 main 走 2 sub1 走 3 sub2 走
	 * 
	 */
	private volatile int shouldWho = 0;

	private Lock lock = new ReentrantLock();

	private Condition con1 = lock.newCondition();
	private Condition con2 = lock.newCondition();
	private Condition con3 = lock.newCondition();

	public void sub1() {

		lock.lock();
		try {
			while (shouldWho != 1) {
				try {

					// this.wait();
					con2.await();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (int j = 1; j <= 2; j++) {
				System.out.println("sub1 Thread  sequnced:" + j);
			}
			shouldWho = 2;
			con3.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public void sub2() {

		lock.lock();
		try {
			while (shouldWho != 2) {
				try {

					// this.wait();
					con3.await();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (int j = 1; j <= 2; j++) {
				System.out.println("sub2 Thread  sequnced:" + j);
			}
			shouldWho = 0;
			con1.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

	}

	public void main() {
		lock.lock();
		try {
			while (shouldWho != 0) {
				try {
					con1.await();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			for (int j = 1; j <= 1; j++) {
				System.out.println("main Thread  sequnced:" + j);
			}

			shouldWho = 1;
			// this.notify();
			con2.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

	}
}