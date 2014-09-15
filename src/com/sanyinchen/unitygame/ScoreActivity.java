package com.sanyinchen.unitygame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mobads.appoffers.OffersManager;
import com.unity3d.player.UnityPlayer;

public class ScoreActivity extends Activity implements OnClickListener {
	TextView historyscore;
	TextView nowscore;
	TextView hitscore;
	TextView misscore;
	Button again;
	Button exit;
	MainActivity activity;
	Typeface tf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_score);
		overridePendingTransition(R.anim.activity_right_in,
				R.anim.activity_left_out);
		activity = new MainActivity();
		tf = Typeface.createFromAsset(getAssets(), "kristi.ttf");
		historyscore = (TextView) findViewById(R.id.historyscore);
		historyscore.setTypeface(tf);
		nowscore = (TextView) findViewById(R.id.nowscore);
		nowscore.setTypeface(tf);
		hitscore = (TextView) findViewById(R.id.hitscore);
		hitscore.setTypeface(tf);
		misscore = (TextView) findViewById(R.id.missedscore);
		misscore.setTypeface(tf);
		again = (Button) findViewById(R.id.again);
		exit = (Button) findViewById(R.id.exit);
		again.setOnClickListener(this);
		exit.setOnClickListener(this);
		historyscore.setText(Contentdata.historyscore + "");
		nowscore.setText(Contentdata.nowscore);
		misscore.setText("-" + Contentdata.misscore);
		hitscore.setText(Contentdata.hitscore);
		OffersManager.setAppSid("b668ec8e");
		OffersManager.setAppSec("b668ec8e");

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.exit:
			UnityPlayer.UnitySendMessage("shipgame", "againgame", "exit");
			// activity.doaction("exit");
			finish();
			break;
		case R.id.again:
			if (OffersManager.getPoints(ScoreActivity.this) < 50) {
				String message = " ��~�����ѽ�������ȡ50���ּ������ü���Ͽ��ж���^v^\nPS:��,��ǰ�ѻ�û���:"
						+ OffersManager.getPoints(ScoreActivity.this)
						+ " , ��л�׵�֧�֣�\"\n\n";
				new AlertDialog.Builder(ScoreActivity.this)
						.setTitle("��ܰ��ʾ")
						.setMessage(message)
						.setPositiveButton("��ȡ����",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										OffersManager
												.showOffers(ScoreActivity.this);

									}
								})
						.setNegativeButton("�ٿ���һ��",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub

									}
								}).show();

			} else {
				UnityPlayer.UnitySendMessage("shipgame", "againgame", "again");
				// activity.doaction("again");
				finish();
			}
			break;
		}
	}

}
