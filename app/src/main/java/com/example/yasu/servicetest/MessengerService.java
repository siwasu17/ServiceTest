package com.example.yasu.servicetest;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

public class MessengerService extends Service {
    static final int MSG_ACTIVITY_CONNECTED = 1;
    static final int MSG_ACTIVITY_DISCONNECTED = 2;
    static final int MSG_COMMAND = 3;

    static final String TAG = "MessengerService";

    final Messenger messenger = new Messenger(new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case MSG_ACTIVITY_CONNECTED:
                    Log.i(TAG,(String)msg.obj);
                    break;
                case MSG_ACTIVITY_DISCONNECTED:
                    Log.i(TAG, (String) msg.obj);
                    break;
                case MSG_COMMAND:
                    Toast.makeText(getApplicationContext(),(String)msg.obj,Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    });

    public MessengerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }


}
