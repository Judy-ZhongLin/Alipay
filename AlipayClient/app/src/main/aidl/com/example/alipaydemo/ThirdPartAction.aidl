// ThirdPartAction.aidl
package com.example.alipaydemo;

// Declare any non-default types here with import statements
import com.example.alipaydemo.ThirdPartResultAction;

interface ThirdPartAction {
    void requestPay(String info, float money, ThirdPartResultAction callback);
}