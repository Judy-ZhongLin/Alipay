package com.example.alipaydemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.LongDef;
import androidx.annotation.Nullable;

public class PayService extends Service {

    private static final String TAG = "PayService";
    private ThirdPayImpl thirdPay;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        String action = intent.getAction();
        if(!TextUtils.isEmpty(action) && "com.example.alipaydemo.THIRD_PART_PAY".equals(action)){
            thirdPay = new ThirdPayImpl();
            return thirdPay;
        }
        return new PayAction();
    }
    public class PayAction extends Binder {
        public void pay(float money){
            if(thirdPay != null){
                thirdPay.paySuccess();
                Log.d(TAG, "支付服务的支付活动");
            }
        }
        public void userCancel(){
            if(thirdPay != null){
                thirdPay.payFailed("用户取消支付", 1);
                Log.d(TAG, "支付服务的支付活动的取消");
            }
        }
    }
    private class ThirdPayImpl extends ThirdPartAction.Stub{

        private ThirdPartResultAction mCallBack;
        @Override
        public void requestPay(String info, float money, ThirdPartResultAction callback) throws RemoteException {
            this.mCallBack =callback;
            //开始我的支付Activity
            Intent intent = new Intent();
            Log.d(TAG, "还没进去第三极支付活动");
            intent.setClass(PayService.this, PayActivity.class);
            Log.d(TAG,"进去了");
            intent.putExtra(Constants.KEY_PAY_INFO, info);
            intent.putExtra(Constants.KEY_PAY_MONEY, money);

            //新开启的活动在新任务栈中
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.d(TAG,"开启了，新任务线");
            startActivity(intent);

        }

        public void paySuccess(){
            try {
                mCallBack.onSuccess();
                Log.d(TAG,"zhifuchengong");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public void payFailed(String info, int code){
            try {
                Log.d(TAG,"zhifushibai");
                mCallBack.onFailed(info, code);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
