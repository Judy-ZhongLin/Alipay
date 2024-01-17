
package com.example.alipaydemo;

import com.example.alipaydemo.ThirdPartResultAction;

interface ThirdPartAction {
    void requestPay(String info, float money, ThirdPartResultAction callback);
}