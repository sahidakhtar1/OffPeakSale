package com.appsauthority.appwiz.utils;

import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.appsauthority.appwiz.SplashscreenActivity;
import com.appsauthority.appwiz.VoucherDisplayActivity;
import com.appsauthority.appwiz.models.Voucher;
import com.appsauthority.appwiz.models.VoucherList;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.offpeaksale.consumer.R;

public class GCMIntentService extends IntentService {

	public static int notification_id = 1;

	private static String TAG = "GCMIntentService";
	private Bundle extras;
	private SQLiteHelper sqliteHelper;

	private NotificationManager mNotificationManager;
	// private Retailer retailer;
	private String msg, type,alert,pid;
	private SharedPreferences spref;

	public GCMIntentService() {
		super(Constants.SENDER_ID);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		Log.i(TAG, gcm.getMessageType(intent));

		extras = intent.getExtras();

		spref = PreferenceManager.getDefaultSharedPreferences(this);
		msg = extras.getString("msg");
		type = extras.getString("pnType");
		pid = extras.getString("pid");
		if (type.equalsIgnoreCase("text")) {
			alert = msg;
		}else{
			alert = "New Voucher";
		}
		

		Voucher voucher = new Voucher();
		voucher.setMsg(msg);
		voucher.setType(type);
		voucher.setPid(pid);
		addVouchers(voucher);
		// sqliteHelper = new SQLiteHelper(this);
		//
		// sqliteHelper.openDataBase();
		// sqliteHelper.insertOrReplaceVoucher(voucher);
		// sqliteHelper.close();
		Boolean isOff = spref.getBoolean(Constants.KEY_PN, false);
		if (!isOff) {
			sendNotification();
		}
		
		
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	public void addVouchers(Voucher v) {

		if (v.getType().equalsIgnoreCase("text")) {
			return;
		}
		Gson gson = new Gson();
		VoucherList vouchres;
		String voucherListString = spref.getString(Constants.KEY_VOUCHERS, "");
		if (voucherListString.equals("")) {
			vouchres = new VoucherList();
		} else {
			vouchres = gson.fromJson(voucherListString, VoucherList.class);

		}
		if (vouchres.getVouchers().size() > 0) {
			vouchres.getVouchers().add(0, v);
		} else {
			vouchres.getVouchers().add(v);
		}
		voucherListString = gson.toJson(vouchres);
		spref.edit().putString(Constants.KEY_VOUCHERS, voucherListString)
				.commit();
	}

	// --- Send Notification --- //
	private void sendNotification() {
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent intent ;
		if (type.equalsIgnoreCase("text")) {
			intent = new Intent(this, SplashscreenActivity.class);
		}else{
			intent = new Intent(this, VoucherDisplayActivity.class);
		}
//		intent.setAction(Intent.ACTION_MAIN);
//		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.putExtra("type", type);
		intent.putExtra("msg", msg);
		intent.putExtra("pid", pid);
		boolean isInForeGround = false;
		try {
			isInForeGround = new ForegroundCheckTask().execute(
					getApplicationContext()).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		intent.putExtra(Constants.KEY_IS_APP_RUNNING, isInForeGround);
		
		if (type.equalsIgnoreCase("text")) {
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);

			String appname =  getResources().getString(R.string.app_name);
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					this)
					.setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle(appname)
					.setStyle(
							new NotificationCompat.BigTextStyle()
									.bigText(alert))
					.setContentText(alert);

			mBuilder.setContentIntent(contentIntent);
			Notification notif = mBuilder.build();
			notif.flags |= Notification.FLAG_AUTO_CANCEL;

			mNotificationManager.notify(notification_id, notif);
			Uri notification = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			Ringtone ringer = RingtoneManager.getRingtone(getApplicationContext(),
					notification);
			ringer.play();
		}else{
			if (isInForeGround) {
				sendGCMBROADCAST(msg, type,pid);
			}else{
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				this.getApplicationContext().startActivity(intent);
			}
			
		}
		
		//FLAG_ACTIVITY_CLEAR_TOP;

//		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//				intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//		String appname =  getResources().getString(R.string.app_name);
//		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//				this)
//				.setSmallIcon(R.drawable.ic_launcher)
//				.setContentTitle(appname)
//				.setStyle(
//						new NotificationCompat.BigTextStyle()
//								.bigText(alert))
//				.setContentText(alert);
//
//		mBuilder.setContentIntent(contentIntent);
//		Notification notif = mBuilder.build();
//		notif.flags |= Notification.FLAG_AUTO_CANCEL;
//
//		mNotificationManager.notify(notification_id, notif);
//		Uri notification = RingtoneManager
//				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//		Ringtone ringer = RingtoneManager.getRingtone(getApplicationContext(),
//				notification);
//		ringer.play();
//		if (type.equalsIgnoreCase("text")) {
//			return;
//		}
		//sendGCMBROADCAST(msg, type);
		
	}

	private void sendGCMBROADCAST(String theMessage, String type,String pid) {
		
		Intent i = new Intent(this,
				VoucherDisplayActivity.class);
		i.putExtra(Constants.KEY_PUSH_MSG, msg);
		i.putExtra(Constants.KEY_PUSH_TYPE, type);
		i.putExtra("pid", pid);
		i.putExtra(Constants.KEY_IS_APP_RUNNING, true);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.getApplicationContext().startActivity(i);
		
//		startActivity(i);
		
//		Intent aBroadcastIntent = new Intent();
//		aBroadcastIntent.setAction("DISPLAY_MESSAGE_ACTION");
//		aBroadcastIntent.putExtra(Constants.KEY_PUSH_MSG, theMessage);
//
//		aBroadcastIntent.putExtra(Constants.KEY_PUSH_TYPE, type);
//		this.sendBroadcast(aBroadcastIntent);
	}
	class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Context... params) {
			final Context context = params[0].getApplicationContext();
			return isAppOnForeground(context);
		}

		private boolean isAppOnForeground(Context context) {
			ActivityManager activityManager = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> appProcesses = activityManager
					.getRunningAppProcesses();
			if (appProcesses == null) {
				return false;
			}

			final String packageName = context.getPackageName();
			for (RunningAppProcessInfo appProcess : appProcesses) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND
						&& appProcess.processName.equals(packageName)) {
					return true;
				}
			}
			return false;
		}
	}

}
