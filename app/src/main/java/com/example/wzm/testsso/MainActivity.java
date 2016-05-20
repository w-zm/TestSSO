package com.example.wzm.testsso;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public String token;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                token = (String) msg.obj;
                //identify();
                Log.d("test", token);
            }
        }
    };

    private void identify() {
        if (token == null) {
            //没有token
        } else {
            //唤醒授权客户端，进行授权
            try {
                PackageManager packageManager = getPackageManager();
                Intent intent = new Intent();
                intent = packageManager.getLaunchIntentForPackage("com.tencent.mm");
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.baidu.com/"));
                startActivity(viewIntent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.init();

        AccountManager accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccounts();      //后续应该改成getAccountByType();
//        accountManager.setAuthToken(accounts[3], "Manage your tasks", "1234");
//        String token = accountManager.getUserData(accounts[3], AccountManager.KEY_AUTHTOKEN);
//        Log.e("haha", token);

        Logger.d("start");
//        final String[] token = new String[1];
//        token[0] = new String();
        /*
         * 取token
         */
        accountManager.getAuthToken(accounts[4], "Manage your tasks", null, this, new AccountManagerCallback<Bundle>() {
            @Override
            public void run(AccountManagerFuture<Bundle> future) {
                try {
                    Log.d("##", "&&&&&&&&&&&&" + future.getResult().getString(AccountManager.KEY_AUTHTOKEN) + "&&&&&&&&&&&&&&&&&&");
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);
                    handler.sendMessage(msg);
                }
                catch (Exception e) {
                    Log.e("hahaha", e.getMessage(), e);
                }
            }
        }, null);

//        Log.d("test", token);
    }

//    private class OnTokenAcquired implements AccountManagerCallback<Bundle> {
//        @Override
//        public void run(AccountManagerFuture<Bundle> result) {
//            // Get the result of the operation from the AccountManagerFuture.
//            Bundle bundle = null;
//            try {
//                bundle = result.getResult();
//            } catch (OperationCanceledException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (AuthenticatorException e) {
//                e.printStackTrace();
//            }
//
//            // The token is a named value in the bundle. The name of the value
//            // is stored in the constant AccountManager.KEY_AUTHTOKEN.
//            token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
//
//        }
//    }
}
