package myandroid.async;

public interface AsyncCallBack<R> {
	public void onResult(boolean isSuccess, R obj);
}
