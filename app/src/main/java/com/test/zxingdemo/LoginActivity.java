package com.test.zxingdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.utils.AccessToken;

import net.sf.json.JSONObject;

import java.io.IOException;

/**
 * Created by ${pipi} on 2017/3/7.
 */

public class LoginActivity extends Activity {
    private EditText user, password;
    private Button login;
    private String userValue, passwordValue;
    final OkHttpClient client = new OkHttpClient();
    private Context context;
    Handler handler=new Handler(){
        public void  handleMessage(Message msg){
            //context.getMainLooper().prepare();
            //JSONObject msgInfo=(JSONObject) msg.obj;
            JSONObject msgInfo= JSONObject.fromObject((String) msg.obj);
            if( msg.what==1) {
                if ("ok".equals(msgInfo.getString("status"))) {

                    String toke= msgInfo.getJSONObject("data").getString("token");
                    AccessToken token = AccessToken.getInstance();
                    token.setToken(toke);

                    Intent intent=new Intent();
                    intent.setClass(LoginActivity.this, ReveivingActivity.class);
                    LoginActivity.this.startActivity(intent);
                } else {
                    Toast.makeText(context, "账号或密码错误", Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(context,"获取失败，请重新获取",Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login__activity);
        context=this;
        user = (EditText) findViewById(R.id.user_et);
        password = (EditText) findViewById(R.id.password_et);
        login = (Button) findViewById(R.id.login_bt);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userValue = user.getText().toString().trim();
                passwordValue = password.getText().toString().trim();
                if(TextUtils.isEmpty(userValue)||TextUtils.isEmpty(passwordValue)){
                    Toast.makeText(LoginActivity.this,"账号密码不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                postRequest(userValue,passwordValue);
            }
        });}
    private void postRequest(String mobile,String password)  {
        //建立请求表单，添加上传服务器的参数
        RequestBody formBody = new FormEncodingBuilder()
                .add("mobile",userValue)
                .add("password",passwordValue)
                .build();
        //发起请求
        final Request request = new Request.Builder()
                .url("http://wms-test.joyoio.net/api/v1/login")
                .post(formBody)
                .build();
        // 新建一个线程，用于得到服务器响应的参数
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response;
                try {
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String message=response.body().string();
                        //JSONObject msgInfo= JSONObject.fromObject(message);
                        Message msg=handler.obtainMessage();
                        //msg.obj=msgInfo;
                        msg.obj=message;
                        msg.what=1;
                        handler.sendMessage(msg);
                    } else {
                        throw new IOException("Unexpected code:" + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
