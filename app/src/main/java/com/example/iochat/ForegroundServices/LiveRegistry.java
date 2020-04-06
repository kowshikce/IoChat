package com.example.iochat.ForegroundServices;

import androidx.lifecycle.LiveData;

import java.util.HashMap;
import java.util.List;

public class LiveRegistry<K extends HashMap> extends LiveData<K>{



    @Override
    protected void onActive() {
        super.onActive();
    }

    @Override
    protected void onInactive() {
        super.onInactive();
    }
}
