package myandroid.broadcast;

import myandroid.tools.Develop;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

abstract public class GcmRecever extends BroadcastReceiver {
	protected Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		if (intent.getAction().equals(BroadcastAction.GCM_REGISTRATION))
			onRegistrationRecive(intent.getExtras());
		else if (intent.getAction().equals(BroadcastAction.GCM_RECEIVE))
			onMessageRecive(intent.getExtras());
		Develop.i(this, Develop.divider + "onReceive" + Develop.divider);
		Develop.v(this, intent.getAction());
		for (String key : intent.getExtras().keySet()) {
			Develop.v(this, key + ":::" + intent.getExtras().getString(key));
		}
	}

	protected void onRegistrationRecive(Bundle data) {
		if (data.containsKey("registration_id"))
			onRegistration(data.getString("registration_id"));
		else if (data.containsKey("unregistered"))
			onUnRegistered(data.getString("unregistered"));
		else if (data.containsKey("error"))
			onRegistrationError(data.getString("error"));
		else
			Develop.e(this, "unKnowRegistration");

	}

	abstract protected void onRegistration(String registration_id);

	abstract protected void onMessageRecive(Bundle data);

	abstract protected void onRegistrationError(String error);

	abstract protected void onUnRegistered(String unregistered);

}
