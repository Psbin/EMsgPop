package com.eee27go.emsgpop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayout;
    private DesktopLayout mDesktopLayout;
    private long startTime;
    // 声明屏幕的宽高
    float x,y;
    int top;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createWindowManager();
        createDesktopLayout();

        /*test*/

        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDesk();
            }
        });




       /* test*/

        BroadcastReceiver BrdRcv;
        //短信接收者实例化
        BrdRcv=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("Test","New Msg!");
            }
        };//BroadcastReceiver

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



    /*创建窗体*/
    private void createDesktopLayout(){
        mDesktopLayout=new DesktopLayout(this);
        mDesktopLayout.setOnTouchListener(new View.OnTouchListener() {
            float mTouchStartX;
            float mTouchStartY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                x = event.getRawX();
                y = event.getRawY() - top;
                Log.i("startP", "startX" + mTouchStartX + "====startY" + mTouchStartY);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mTouchStartX = event.getX();
                        mTouchStartY = event.getY();
                        Log.i("startP", "startX" + mTouchStartX + "====startY" + mTouchStartY);
                        long end = System.currentTimeMillis() - startTime;

                        if (end < 300) {
                            closeDesk();
                        }
                        startTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mLayout.x = (int) (x - mTouchStartX);
                        mLayout.y = (int) (y - mTouchStartY);
                        mWindowManager.updateViewLayout(v, mLayout);
                        break;
                    case MotionEvent.ACTION_UP:
                        mLayout.x = (int) (x - mTouchStartX);
                        mLayout.y = (int) (y - mTouchStartY);
                        mWindowManager.updateViewLayout(v, mLayout);


                        mTouchStartX = mTouchStartY = 0;
                        break;
                }
                return true;
            }
        });

    }//createDesktopLayout



    /*改变焦点*/
    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        Rect rect=new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        top=rect.top;
        Log.i("top", "" + top);
    }//onWindowFocusChanged


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
        mLayout.type=WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;


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
