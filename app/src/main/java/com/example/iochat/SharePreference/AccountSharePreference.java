package com.example.iochat.SharePreference;

import android.content.Context;
import android.content.SharedPreferences;

public class AccountSharePreference {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static final String SESSION_NAME = "ACCOUNT_SESSIONS";

    private AccountSharePreference(Context context){
        sharedPreferences = context.getSharedPreferences(SESSION_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static AccountSharePreference getInstance(Context context){
        return new AccountSharePreference(context);
    }


    public boolean clear(){
        editor.clear();
        return editor.commit();
    }
}
