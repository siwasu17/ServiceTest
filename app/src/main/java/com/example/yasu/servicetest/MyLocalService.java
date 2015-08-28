package com.example.yasu.servicetest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyLocalService extends Service {
    private static final String TAG = "MyLocalService";
    public MyLocalService() {
    }

    public void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    //サービスがアクティビティにバインドされたときの処理
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"Service#onBind()");
        return new LocalBinder();

    }

    //アクティビティがサービスの実体にアクセスするためのクラス
    public class LocalBinder extends Binder {
        MyLocalService getService(){
            return MyLocalService.this;
        }
    }


}
