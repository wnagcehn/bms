package com.jiuyescm.bms.test;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Threads {
	public static void run(int count, List<Runnable> runs) {
		ScheduledThreadPoolExecutor poll = new ScheduledThreadPoolExecutor(
				count);
		poll.setMaximumPoolSize(count);
		while (poll.prestartCoreThread())
			;
		Queue<Runnable> bomDatas = new LinkedBlockingDeque<>(runs);
		while (!bomDatas.isEmpty()) {
			poll.execute(bomDatas.poll());
		}
		boolean hasStart = false;
		while (true) {
			if (!hasStart && poll.getActiveCount() > 0) {
				hasStart = true;
			}
			if (hasStart && poll.getActiveCount() == 0) {
				poll.shutdown();
				break;
			}
			try {
				Thread.sleep(20);
			} catch (Exception e) {
			}
		}
	}
}
