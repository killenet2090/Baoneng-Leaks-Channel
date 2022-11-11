package com.bnmotor.icv.tsp.ota.common.event;

import com.bnmotor.icv.tsp.ota.common.lock.Invocation;
import com.bnmotor.icv.tsp.ota.util.UUIDShort;
import org.springframework.util.StopWatch;
import org.springframework.util.StopWatch.TaskInfo;

import java.text.NumberFormat;

/**
 * @ClassName: OtaEventMonitor.java OtaEventMonitor
 * @Description: 增加计时器
 * @author E.YanLonG
 * @since 2020-11-20 11:28:08
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class OtaEventMonitor {

	static final ThreadLocal<StopWatch> THREADLOCALS = ThreadLocal.withInitial(() -> new StopWatch(UUIDShort.generate()));

	public static StopWatch start(String taskName) {
		StopWatch stopwatch = THREADLOCALS.get();
		if (stopwatch.isRunning()) {
			stopwatch.stop();
		}
		stopwatch.start(taskName);
		return stopwatch;
	}

	public static void stop() {
		StopWatch stopwatch = THREADLOCALS.get();
		if (stopwatch.isRunning()) {
			stopwatch.stop();
		}
	}

	public static StopWatch stick(String taskName) {
		StopWatch stopwatch = THREADLOCALS.get();
		if (stopwatch.isRunning()) {
			stopwatch.stop();
		}
		stopwatch.start(taskName);
		return stopwatch;
	}

	public static void stick(String taskName, Invocation invocation) {
		StopWatch stopWatch = stick(taskName);
		invocation.invoke();
		stopWatch.stop();
	}

	public static String init() {
		StopWatch stopwatch = THREADLOCALS.get();
		return stopwatch.getId();
	}

	public static void clean() {
		THREADLOCALS.remove();
	}

	private static String shortSummary0(StopWatch stopWatch) {
		return "StopWatch '" + stopWatch.getId() + "': running time = " + stopWatch.getTotalTimeMillis() + " mills";
	}

	public static String prettyPrint() {
		StopWatch stopWatch = THREADLOCALS.get();
		StringBuilder sb = new StringBuilder();
		sb.append('\n');
		sb.append(shortSummary0(stopWatch));
		sb.append('\n');
		{
			sb.append("---------------------------------------------\n");
			sb.append("millis         %     Task name\n");
			sb.append("---------------------------------------------\n");
			NumberFormat nf = NumberFormat.getNumberInstance();
			nf.setMinimumIntegerDigits(9);
			nf.setGroupingUsed(false);
			NumberFormat pf = NumberFormat.getPercentInstance();
			pf.setMinimumIntegerDigits(3);
			pf.setGroupingUsed(false);
			for (TaskInfo task : stopWatch.getTaskInfo()) {
				sb.append(nf.format(task.getTimeMillis())).append("  ");
				sb.append(pf.format((double) task.getTimeMillis() / stopWatch.getTotalTimeMillis())).append("  ");
				sb.append(task.getTaskName()).append("\n");
			}
		}

		return sb.toString();
	}

//	public static void main(String[] args) {
//		AtomicInteger i = new AtomicInteger(0);
//		do {
//			OtaEventMonitor.stick("task" + i, () -> {
//				task(i.get());
//			});
//		} while (i.getAndIncrement() < 5);
//
//		System.err.println(OtaEventMonitor.prettyPrint());
//	}
//
//	public static void task(int i) {
//		SecureRandom random = new SecureRandom();
//		int c = random.nextInt(100) + 1;
//		try {
//			TimeUnit.MILLISECONDS.sleep(c);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}

}