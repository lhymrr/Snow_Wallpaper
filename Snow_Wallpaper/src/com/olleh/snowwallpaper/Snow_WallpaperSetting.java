package com.olleh.snowwallpaper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.Toast;

import com.waps.AppConnect;
import com.waps.UpdatePointsNotifier;

public class Snow_WallpaperSetting extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener, UpdatePointsNotifier {
	/** Called when the activity is first created. */
	private ProgressDialog pd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("LHY", "Go_onCreate");
		getPreferenceManager().setSharedPreferencesName(SnowWallpaper.SHARED_PREFS_NAME);
		addPreferencesFromResource(R.xml.settings);
		getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		AppConnect.getInstance(this).getPoints(this);
//		pd.setCancelable(false);
//		pd = new ProgressDialog(this);
//		pd.setMessage("请稍后,正在获得您的积分...");
//		pd.show();

		return;
	}

	@Override
	protected void onResume() {
		super.onResume();
		return;
	}

	@Override
	protected void onDestroy() {
		getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
		super.onDestroy();
		return;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		return;
	}

	@Override
	public boolean onPreferenceClick(Preference pref) {

		return (true);
	}

	public static final int HAN_SUCCESS = 1001;
	public static final int HAN_FAILED = 1002;

//	private Handler mHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			switch (msg.what) {
//			case HAN_SUCCESS: {
//				int arg1 = msg.arg1;
//				int num = 0;
//				if (arg1 >= num) {
//					Toast.makeText(Snow_WallpaperSetting.this, "您的积分已达到200积分,可以进行设置选项!", Toast.LENGTH_LONG).show();
//					pd.dismiss();
//				} else {
//					Toast.makeText(Snow_WallpaperSetting.this, "您的积分不足200积分,不可以进行设置选项!", Toast.LENGTH_LONG).show();
//					pd.dismiss();
//					AlertDialog ad = new AlertDialog.Builder(Snow_WallpaperSetting.this).create();
//					ad.setCancelable(false);
//					ad.setMessage("您的积分不足200积分,请免费下载积分");
//					ad.setButton("免费获取积分", new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							AppConnect.getInstance(Snow_WallpaperSetting.this).showOffers(Snow_WallpaperSetting.this);
//							finish();
//						}
//					});
//					ad.setButton2("取消", new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							finish();
//						}
//					});
//					ad.show();
//
//				}
//
//				break;
//			}
//
//			case HAN_FAILED: {
//				Toast.makeText(Snow_WallpaperSetting.this, "请检查网络...", Toast.LENGTH_LONG).show();
//				finish();
//				break;
//			}
//			}
//		}
//	};

	@Override
	public void getUpdatePoints(String arg0, int arg1) {
//		Message msg = new Message();
//		msg.what = HAN_SUCCESS;
//		msg.arg1 = arg1;
//		mHandler.sendMessage(msg);
	}
//
	@Override
	public void getUpdatePointsFailed(String arg0) {
//		mHandler.sendEmptyMessage(HAN_FAILED);
	}

}