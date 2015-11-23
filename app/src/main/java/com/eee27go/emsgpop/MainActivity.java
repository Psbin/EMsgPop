package com.eee27go.emsgpop;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.service.carrier.CarrierMessagingService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    private TextView sender;
    private TextView content;
    private EditText sendto;
    private EditText msg_input;
    private Button sendbtn;

    private TextView dlv;


    String SE_S_ACT="SENT_SMS_ACTION";
    String DLV_S_ACT="DELIVERED_SMS_ACTION";

    IntentFilter receiveFilter;
    MessageReceiver messageReceiver;

    Intent sentIntent=new Intent(SE_S_ACT);
    PendingIntent sentPI=PendingIntent.getBroadcast(
            getApplicationContext(),0,sentIntent,0);

    Intent deliveryIntent=new Intent(DLV_S_ACT);
    PendingIntent deliverPI=PendingIntent.getBroadcast(
            getApplicationContext(),0,deliveryIntent,0);



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sender=(TextView)findViewById(R.id.sender);
        content=(TextView)findViewById(R.id.content);
        sendto=(EditText)findViewById(R.id.sendto);
        msg_input=(EditText)findViewById(R.id.msg_input);
        sendbtn=(Button)findViewById(R.id.sendbtn);


        dlv=(TextView)findViewById(R.id.msg_get);





        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsManager smsManager=SmsManager.getDefault();
                smsManager.sendTextMessage(sendto.getText().toString(),null,
                        msg_input.getText().toString(), sentPI,
                        deliverPI);
            }
        });




        //短信接收器注册
        receiveFilter=new IntentFilter();
        receiveFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        messageReceiver=new MessageReceiver();
        registerReceiver(messageReceiver,receiveFilter);




        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch(getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(context,getString(R.string.succ_send),Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context,getString(R.string.fail_gen),Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context,getString(R.string.fail_radio),Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context,getString(R.string.fail_null),Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        },new IntentFilter(SE_S_ACT));


        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context _context, Intent _intent) {
                dlv.setText(getString(R.string.msg_get));
            }
        },new IntentFilter(DLV_S_ACT));


        /*
        switch按钮适配动作
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
        */
    }//onCreate


    protected void onDestory(){
        super.onDestroy();
        unregisterReceiver(messageReceiver);
    }//onDestory


    /*接收短信部分*/

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context,Intent intent){
            Bundle bundle=intent.getExtras();
            Object[] pdus=(Object[]) bundle.get("pdus");//提取短信消息

            SmsMessage[] messages=new SmsMessage[pdus.length];

            for(int i=0;i<messages.length;i++){
                messages[i]=SmsMessage.createFromPdu((byte[]) pdus[i]);
            }

            String address=messages[0].getOriginatingAddress();//获取发送方号码
            String fullMessage="";
            for(SmsMessage message:messages){
                fullMessage+=message.getMessageBody();//获取短信内容
            }

            sender.setText(address);
            content.setText(fullMessage);
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
