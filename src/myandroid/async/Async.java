package myandroid.async;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Async {
	static ExecutorService executorService = Executors.newFixedThreadPool(50);

	public static void setExecutorService(ExecutorService executorService) {
		Async.executorService = executorService;
	}

	public static boolean awaitTermination(long timeout, TimeUnit unit)
			throws InterruptedException {
		return executorService.awaitTermination(timeout, unit);
	}

	public static void execute(Runnable command) {
		executorService.execute(command);
	}

	public static boolean isShutdown() {
		return executorService.isShutdown();
	}

	public static boolean isTerminated() {
		return executorService.isTerminated();
	}

	public static void shutdown() {
		executorService.shutdown();
	}

	public static void shutdownNow() {
		executorService.shutdownNow();
	}

	public static <T> Future<T> submit(Callable<T> list) {
		return executorService.submit(list);
	}

	public static Future<?> submit(Runnable command) {
		return executorService.submit(command);
	}
}