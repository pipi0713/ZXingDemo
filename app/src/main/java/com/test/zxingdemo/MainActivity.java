package com.test.zxingdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zxing.activity.CaptureActivity;


/**
 * 调用扫描二维码,并将结果显示到界面
 * @author Tercel
 *
 */
public class MainActivity extends Activity {

	private Button scanning;		//点击扫描按钮
	private TextView result;		//扫描显示对象
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

	}
	
	
	
	
	
	public void scan_bt(View view){
		Intent intent=new Intent();
		intent.setClass(MainActivity.this,CaptureActivity.class);
		MainActivity.this.startActivityForResult(intent,0);
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){		//此处就是用result来区分,是谁返回的数据
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			//这就获取了扫描的内容了
			Toast.makeText(MainActivity.this,scanResult,Toast.LENGTH_SHORT).show();
		}
	}
	
	
}
