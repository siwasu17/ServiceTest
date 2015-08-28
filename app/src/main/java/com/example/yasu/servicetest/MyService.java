package com.example.yasu.servicetest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    //内部のスレッド制御用
    //サービスが止まったら停止したいので
    private boolean thRunning = true;

    //ノーティフィケーションと紐付けてフォアグラウンドサービスとする
    private static final int NOTIFY_ID = 1;

    public MyService() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        thRunning = true;

        Thread th = new Thread(new MyRunner());
        th.start();


        //Notificationの生成
        Notification notification = new Notification.Builder(this)
                .setContentTitle("MyService Notification").setContentText("I'll be back!")
                .setSmallIcon(R.drawable.abc_ic_commit_search_api_mtrl_alpha).build();

        //Notificationの表示
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFY_ID,notification);

        //Foregroundサービスとして登録
        startForeground(NOTIFY_ID,notification);

        return super.onStartCommand(intent, flags, startId);
    }

    //スレッド実行用
    public class MyRunner implements Runnable{
        public int counter = 0;
        @Override
        public void run() {
            while(thRunning) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                counter++;
                Log.i("MyRunner", "Counting... " + counter);

                if(counter == 20){
                    //サービスの役割は完了なので止める
                    thRunning = false;
                    //30秒たったら再開してみる
                    Intent itt = new Intent();
                    itt.setClassName("com.example.yasu.servicetest","com.example.yasu.servicetest.MainActivity");
                    itt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(itt);
                }
            }
        }
    }
}
