package myandroid.async;


import android.os.Handler;
import android.os.Message;

public abstract class AsyncRunnable3<R> extends Handler implements Runnable {
	int status = AsyncStatus.PENDING;
	protected AsyncCallBack<R> callBack = null;

	@Override
	public void run() {
		postResoult(AsyncStatus.RUNNING, null);
		// Looper.prepare();
		onPrepare();
		R r = onExecute();
		postResoult(r==null?AsyncStatus.FAILURE:AsyncStatus.SUCCESS, r);
		postResoult(AsyncStatus.FINISHED, null);
	}

	abstract protected void onPrepare();

	abstract protected R onExecute();


	/**
	 * 
	 * @param result
	 * @param status
	 *            <li>AsyncStatus.PENDING：停止 <li>AsyncStatus.RUNNING：執行中 <li>
	 *            AsyncStatus.FINISHED：完成 <li>AsyncStatus.SUCCESS：成功 <li>
	 *            AsyncStatus.FAILURE：失敗
	 */
	abstract protected void onResoult(R result, int status);

	protected void postResoult(int asyncStatus, Object obj) {
		setStatus(asyncStatus);
		obtainMessage(asyncStatus, obj).sendToTarget();
	}

	private void setStatus(int status) {
		this.status = status;
	}

	protected int getStatus() {
		return this.status;
	}

	public void setCallBack(AsyncCallBack<R> callBack) {
		this.callBack = callBack;
	}

	public void removeCallBack() {
		this.callBack = null;
	}

	@Override
	public void handleMessage(Message msg) {
		onResoult((R) msg.obj, msg.what);
		if (callBack == null)
			return;
		if (msg.what == AsyncStatus.SUCCESS || msg.what == AsyncStatus.FAILURE)
			callBack.onResult(msg.what == AsyncStatus.SUCCESS, (R) msg.obj);
	};
}
