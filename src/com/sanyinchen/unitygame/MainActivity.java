package com.sanyinchen.unitygame;

import com.baidu.mobads.appoffers.OffersManager;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Window;

public class MainActivity extends UnityPlayerActivity implements
		SensorEventListener {
	SensorManager manager;
	PowerManager.WakeLock mWakeLock;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Contentdata.histdata = getSharedPreferences("mydata", MODE_PRIVATE);
		Contentdata.histdataedit = Contentdata.histdata.edit();
		Contentdata.historyscore = Contentdata.histdata.getInt("hist", 0);
		Contentdata.Isfirst = Contentdata.histdata.getBoolean("usered", true);
		PowerManager pm = (PowerManager) getSystemService(this.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
		manager = (SensorManager) getSystemService(SENSOR_SERVICE);
		OffersManager.setAppSid("b668ec8e");
		OffersManager.setAppSec("b668ec8e");
	}

	@SuppressLint("NewApi")
	public void getpoint(String content) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// System.out.println("已获得请求----------->"
				// + OffersManager.getPoints(MainActivity.this));
				// TODO Auto-generated method stub
				if (OffersManager.getPoints(MainActivity.this) >50
						&& !Contentdata.Isfirst) {
					String message = " 亲~试用已结束，获取50积分即可永久激活，赶快行动吧^v^\n\nPS:亲，当前已获得积分: "
							+ OffersManager.getPoints(MainActivity.this)
							+ "  , 感谢亲的支持！\"\n\n";
					new AlertDialog.Builder(MainActivity.this)
							.setTitle("温馨提示")
							.setMessage(message)
							.setPositiveButton("获取积分",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											OffersManager
													.showOffers(MainActivity.this);

										}
									})
							.setNegativeButton("再考虑一下",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub

										}
									}).show();

				} else {
					Contentdata.Isfirst = false;
					Contentdata.histdataedit.putBoolean("usered", false);
					Contentdata.histdataedit.commit();
					UnityPlayer.UnitySendMessage("shipgame", "getmessage",
							"yes");

				}
			}
		});
	}

	@SuppressLint("NewApi")
	public void Getdirection(final String score, final String miss,
			final String hit) {
		// System.out.println("----------------->" + content);

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Contentdata.nowscore = score;
				Contentdata.misscore = miss;
				Contentdata.hitscore = hit;
				int nowscore = Integer.parseInt(score);
				if (nowscore > Contentdata.historyscore) {
					Contentdata.historyscore = nowscore;
					Contentdata.histdataedit.putInt("hist", nowscore);
					Contentdata.histdataedit.commit();
				}
				startActivity(new Intent(MainActivity.this, ScoreActivity.class));
			}
		});
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		int sensorType = event.sensor.getType();
		float[] values = event.values;

		switch (sensorType) {
		case Sensor.TYPE_ACCELEROMETER:
			if (Math.abs(values[0]) <= 0.55) {
				UnityPlayer.UnitySendMessage("shipgame", "getvalue", "mid");
			} else {
				if (values[0] < 0) {
					UnityPlayer.UnitySendMessage("shipgame", "getvalue",
							"right");
				} else {
					if (values[0] > 0) {
						UnityPlayer.UnitySendMessage("shipgame", "getvalue",
								"left");
					}
				}
			}
			break;

		default:
			break;
		}
	}

	public void doaction(String content) {
		if (content.equals("exit"))
			UnityPlayer.UnitySendMessage("shipgame", "againgame", "exit");
		else
			UnityPlayer.UnitySendMessage("shipgame", "againgame", "again");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		manager.registerListener(this,
				manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_GAME);
		mWakeLock.acquire();
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		manager.unregisterListener(this);
		super.onPause();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		manager.unregisterListener(this);
		if (mWakeLock != null)
			mWakeLock.release();
		super.onStop();
	}
}
