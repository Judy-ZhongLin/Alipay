
package com.example.alipaydemo;

interface ThirdPartResultAction {
    void onSuccess();
    void onFailed(String info, int code);
}