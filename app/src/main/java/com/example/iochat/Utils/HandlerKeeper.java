package com.example.iochat.Utils;

import android.os.Handler;

import java.util.HashMap;

public class HandlerKeeper {

    private static Handler handler;


    public static void setHandler(Handler handler){
        HandlerKeeper.handler = handler;
    }

    public static Handler getHandler(){
        return handler;
    }
}
