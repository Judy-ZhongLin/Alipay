package com.example.alipaydemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class PayActivity extends Activity {
    private static final String TAG = "PayActivity";
    private Intent intent;
    private PayService.PayAction payAction;
    private Boolean isBind;
    private TextView tvMoney;
    private TextView tvBillInfo;
    private float money;
    private String billInfo;
    private EditText etPassword;
    private Button btnCommit;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        initView();
        doBindService();
    }

    private void doBindService() {
        Intent intent = new Intent(PayActivity.this, PayService.class);
        isBind = bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            payAction = (PayService.PayAction)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            payAction = null;
        }
    };

    private void initView() {
        intent = getIntent();
        billInfo = intent.getStringExtra(Constants.KEY_PAY_INFO);
        money = intent.getFloatExtra(Constants.KEY_PAY_MONEY, 0f);
        tvBillInfo = this.findViewById(R.id.tv_bill_info);
        tvMoney = this.findViewById(R.id.tv_money);
        etPassword = this.findViewById(R.id.et_password);
        btnCommit = this.findViewById(R.id.btn_pay);
        tvBillInfo.setText("账单信息"+billInfo);
        tvMoney.setText("金额："+ money);
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString();
                Log.d(TAG,"密码"+password);
                if("123456".equals(password)){
                    Log.d(TAG,"支付页面进去了");
                    payAction.pay(money);
                    finish();
                }else{
                    Log.d(TAG,"支付页面未进去");
                    Toast.makeText(PayActivity.this, "密码错误，请重新输入",Toast.LENGTH_SHORT).show();;
                    etPassword.setText("");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isBind && serviceConnection != null){
            unbindService(serviceConnection);
            isBind = false;
            serviceConnection = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        payAction.userCancel();
    }
}
