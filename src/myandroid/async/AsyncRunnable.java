package myandroid.async;

import android.os.Handler;
import android.os.Message;

public abstract class AsyncRunnable<R> extends Handler implements Runnable {
	protected AsyncCallBack<R> callBack = null;
	boolean execute = false;
	int id;

	@Override
	public void run() {
		R r = onExecute();
		obtainMessage(r == null ? AsyncStatus.FAILURE : AsyncStatus.SUCCESS, r)
				.sendToTarget();
	}

	abstract protected R onExecute();

	public void setCallBack(AsyncCallBack<R> callBack) {
		this.callBack = callBack;
	}

	public void removeCallBack() {
		this.callBack = null;
	}

	@Override
	public void handleMessage(Message msg) {
		if (callBack != null)
			callBack.onResult(msg.what == AsyncStatus.SUCCESS, (R) msg.obj);
		onResult(msg.what == AsyncStatus.SUCCESS, (R) msg.obj);
		execute = false;
	};

	public void execute() {
		Async.execute(this);
		execute = true;
	}

	public void executeWithSleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Async.execute(this);
	}

	public void onResult(boolean isSuccess, R r) {

	}

	public boolean isExecute() {
		return execute;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
