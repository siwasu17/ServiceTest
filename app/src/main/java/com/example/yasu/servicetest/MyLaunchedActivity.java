package com.example.yasu.servicetest;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MyLaunchedActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_launched2);


        Button btn = (Button)findViewById(R.id.launchBtn2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                //intent.setClassName("com.example.yasu.calccomb","com.example.yasu.calccomb.MainActivity");
                intent.setClassName("com.example.yasu.servicetest","com.example.yasu.servicetest.MyLaunchedActivity");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("SEND_TEXT_KEY_A", "Okurimono");
                startActivity(intent);
            }
        });



        //Intentの受け取り
        Intent itt = getIntent();
        String data = (String)itt.getStringExtra("SEND_TEXT_KEY_A");
        Log.i("Launched",data);

        //結果を返してやる
        Intent ret = new Intent();
        ret.putExtra("SEND_TEXT_KEY_B","Okaeshi");
        setResult(RESULT_OK,ret);

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i("Launched","Restore");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_launched, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
