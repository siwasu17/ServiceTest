package com.example.yasu.servicetest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.yasu.aidlservicetest.IMyAidlInterface;


public class MainActivity extends ActionBarActivity {
    static final int REQUEST_CODE = 100;

    //ローカルサービスのインスタンス保持用
    private MyLocalService mService = null;

    //AIDLサービスのインスタンス保持用
    private IMyAidlInterface aidlService = null;

    //サービスにメッセージを送るためのMessenger
    Messenger messenger = null;

    //ServiceConnectionの実装
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((MyLocalService.LocalBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    //AIDL用のServiceConnectionの実装
    private ServiceConnection aidlServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            aidlService = IMyAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            aidlService = null;
        }
    };

    //メッセンジャー用のサービスコネクション
    private ServiceConnection msgServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messenger = new Messenger(service);
            sendMessage(MessengerService.MSG_ACTIVITY_CONNECTED,"MainActivityと接続しました");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            messenger = null;
        }
    };

    private void sendMessage(int what,Object obj){
        if(messenger != null){
            Message msg = Message.obtain();
            msg.what = what;
            msg.obj = obj;
            try {
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onDestroy() {
        //ここで止めないと自動復元してしまうため
        stopMyService();
        super.onDestroy();

    }

    public void connectAIDLService(){
        Intent i = new Intent();
        i.setClassName("com.example.yasu.aidlservicetest", "com.example.yasu.aidlservicetest.MyAIDLService");
        bindService(
                i,
                aidlServiceConnection,
                BIND_AUTO_CREATE);
    }

    public void connectMessengerService(){
        Intent i = new Intent();
        i.setClass(getApplicationContext(),MessengerService.class);
        bindService(i,msgServiceConnection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("MyApp", "CREATE!!!");

        //AIDLサービスのバインド
        connectAIDLService();

        //メッセンジャーサービスのバインド
        connectMessengerService();

        Button btn = (Button)findViewById(R.id.launchBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MyLaunchedActivity.class);
                //intent.setClassName("com.example.yasu.calccomb","com.example.yasu.servicetest.MyLaunchedActivity");
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("SEND_TEXT_KEY_A", "Okurimono");
                //startActivity(intent);
                //データを受け取れるような起動の仕方
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        //サービス起動用のボタン
        Button sbtn = (Button)findViewById(R.id.serviceBtn);
        sbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MyApp", "Start Service");
                startMyService();
            }
        });


        Button cbtn = (Button)findViewById(R.id.cameraBtn);
        cbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://yahoo.co.jp/"));
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //新しいアクティビティを新しいタスク上で展開する
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //暗黙的インテントで起動
                startActivity(intent);

            }
        });

        //LocalServiceの開始ボタン
        Button startbtn = (Button)findViewById(R.id.startLSBtn);
        startbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent itt = new Intent();
                itt.setClassName("com.example.yasu.servicetest", "com.example.yasu.servicetest.MyLocalService");
                //AUTOで開始とバインドを一緒に
                bindService(itt, mServiceConnection, Context.BIND_AUTO_CREATE);
            }
        });


        //LocalServiceの停止ボタン
        Button stopbtn = (Button)findViewById(R.id.stopLSBtn);
        stopbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                unbindService(mServiceConnection);
                mService = null;
            }
        });

        //LocalServiceへの操作命令ボタン
        Button msgBtn = (Button)findViewById(R.id.messageLSBtn);
        msgBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mService != null){
                    mService.showToast("Message from Activity");
                }
            }
        });

        //インテントサービス
        Button ittbtn = (Button)findViewById(R.id.ittBtn);
        ittbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent itt = new Intent();
                itt.setClass(getApplicationContext(),MyIntentService.class);
                startService(itt);
            }
        });

        //AIDL
        Button aidlbtn = (Button)findViewById(R.id.aidlBtn);
        aidlbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    aidlService.doSomething(100);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        //Messenger
        Button msgSrvBtn = (Button)findViewById(R.id.msgSrvBtn);
        msgSrvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(MessengerService.MSG_COMMAND, "Hello, Messenger!");
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case REQUEST_CODE:
                switch(resultCode){
                    case RESULT_OK :
                        Log.i("MyApp",data.getStringExtra("SEND_TEXT_KEY_B"));
                        break;
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //保存する文字列
        String s = "saved instance text";
        outState.putString("TEXT_KEY",s);

        Log.i("MyApp", "SAVE!!");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        TextView tv = (TextView)findViewById(R.id.textView);
        String s = savedInstanceState.getString("TEXT_KEY");
        tv.setText(s);

        Log.i("MyApp","RESTORE!!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        stopMyService();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startMyService(){
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(),MyService.class);
        startService(intent);
    }


    public void stopMyService(){
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(),MyService.class);
        stopService(intent);

    }
}
