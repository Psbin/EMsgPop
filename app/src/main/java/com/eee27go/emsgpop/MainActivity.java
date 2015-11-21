package com.eee27go.emsgpop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    private static TextView sender;
    private static TextView content;

    IntentFilter receiveFilter;
    MessageReceiver messageReceiver;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayout;
    private DesktopLayout mDesktopLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createWindowManager();
        createDesktopLayout();

        sender=(TextView)findViewById(R.id.textView);
        content=(TextView)findViewById(R.id.textView2);




        //注册
        receiveFilter=new IntentFilter();
        receiveFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        messageReceiver=new MessageReceiver();
        registerReceiver(messageReceiver,receiveFilter);


        //switch按钮适配动作
        Switch switch1=(Switch) findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(MainActivity.this, R.string.miui, Toast.LENGTH_SHORT).show();

                } else{
                    Toast.makeText(MainActivity.this, R.string.no_miui, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }//onCreate


    protected void onDestory(){
        super.onDestroy();
        unregisterReceiver(messageReceiver);
    }//onDestory




    /*创建窗体*/
    private void createDesktopLayout(){
        mDesktopLayout=new DesktopLayout(this);
    }//createDesktopLayout


    /*显示DesktopLayout*/
    private void showDesk(){
        mWindowManager.addView(mDesktopLayout,mLayout);
        finish();
    }//showDesk


    /*关闭DesktopLayout*/
    private void closeDesk(){
        mWindowManager.removeView(mDesktopLayout);
        finish();
    }//closeDesk


    /*设置WindowsManager*/
    private void createWindowManager(){

        // 取得系统窗体
        mWindowManager=(WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);

        // 窗体的布局样式
        mLayout=new WindowManager.LayoutParams();

        // 设置窗体显示类型——TYPE_SYSTEM_ALERT(系统提示)
        mLayout.type=WindowManager.LayoutParams.TYPE_TOAST;


        // 设置窗体焦点及触摸：
        // FLAG_NOT_FOCUSABLE(不能获得按键输入焦点)
        mLayout.flags=WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        // 设置显示的模式
        mLayout.format= PixelFormat.RGBA_8888;

        // 设置对齐的方法
        mLayout.gravity= Gravity.TOP |Gravity.START;

        // 设置窗体宽度和高度
        mLayout.width=WindowManager.LayoutParams.MATCH_PARENT;
        mLayout.height=WindowManager.LayoutParams.WRAP_CONTENT;

    }//createWindowManager

    /*创建广播接收器*/


    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context,Intent intent){
            Bundle bundle=intent.getExtras();
                Object[] pdus=(Object[]) bundle.get("pdus");//提取短信消息
            Log.e("bundle get", "0 has done");
                SmsMessage[] messages=new SmsMessage[pdus.length];
            Log.e("pdus.length", "1 has done");
                for(int i=0;i<messages.length;i++){
                messages[i]=SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
            Log.e("pdus get", "2 has done");
            String address=messages[0].getOriginatingAddress();//获取发送方号码
            String fullMessage="";
            for(SmsMessage message:messages){
                fullMessage+=message.getMessageBody();//获取短信内容
            }
            Log.e("msg get","3 has done");
            //3蛋4没蛋
            MainActivity.sender.setText(address);
            MainActivity.content.setText(fullMessage);
            Log.e("holder given", "4 has done");
            showDesk();
            Log.e("show", "5 has done");

        }


    }






    /*覆写 菜单创建*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }//onCreateOptionsMenu

    /*覆写 菜单选择动作*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.update_item:
                Toast.makeText(this,R.string.update_item,Toast.LENGTH_LONG).show();
                break;
            case R.id.help_item:
                Toast.makeText(this,R.string.help_item,Toast.LENGTH_LONG).show();
                break;
            default:
        }
        return true;
    }//onOptionsItemSelected

}
