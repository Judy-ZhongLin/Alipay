package com.example.alipayclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alipaydemo.ThirdPartAction;
import com.example.alipaydemo.ThirdPartResultAction;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    TextView balance;
    Button commit;
    boolean isBind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        doBindService();
        initListener();
    }

    private void initListener() {
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(thirdPartAction != null){
                    try {
                        thirdPartAction.requestPay("充值Q币！！！", 100,new PayCallBack());
                        Log.d(TAG,"进去了，算是一大胜利了！");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }else{
                    Log.d(TAG, "传值传不过去");
                }

            }
        });
    }
    public class PayCallBack extends ThirdPartResultAction.Stub{

        @Override
        public void onSuccess() throws RemoteException {
            //当支付成功
            balance.setText("100");
        }

        @Override
        public void onFailed(String info, int code) throws RemoteException {
            //当支付失败
            Log.d(TAG, "onFailed:" + "error_info---->"+info+"code"+code);
        }
    }

    private void doBindService() {
        Intent intent = new Intent();
        intent.setAction("com.example.alipaydemo.THIRD_PART_PAY");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setPackage("com.example.alipaydemo");
        isBind =bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private ThirdPartAction thirdPartAction;
    private void initView() {
        balance = this.findViewById(R.id.tv_balance);
        commit = this.findViewById(R.id.btn_pay);
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            thirdPartAction = ThirdPartAction.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            thirdPartAction = null;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isBind && serviceConnection != null){
            unbindService(serviceConnection);
            isBind = false;
            serviceConnection = null;
        }
    }
}