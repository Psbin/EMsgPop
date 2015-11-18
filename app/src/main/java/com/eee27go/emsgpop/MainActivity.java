package com.eee27go.emsgpop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //switch适配动作
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

    //覆写菜单创建
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }//onCreateOptionsMenu

    //覆写菜单选择动作
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
