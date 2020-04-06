package com.example.iochat.Registry;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivitymainRegistry {

    private HashMap<String, Object> mainRegistry = new HashMap<>();

    private ArrayList<Observer> observers = new ArrayList<>();

    private ActivitymainRegistry(){}

    private static ActivitymainRegistry instance = new ActivitymainRegistry();

    public static ActivitymainRegistry getInstance(){
        return instance;
    }

    public void setRegistryFor(String key, Object object){
        this.mainRegistry.put(key, object);
        notifyObserver(key, object);
    }

    public Object getRegistryFor(String key){
        Object object = this.mainRegistry.get(key);
        if(object != null){
            return object;
        }else {
            return null;
        }
    }

    public void registerObserver(Observer observer){
        this.observers.add(observer);
    }

    public void notifyObserver(String key, Object obsr){
        for(Observer observer : observers){
            observer.onUpdate(key, obsr);
        }
    }

    public void onCreate(){}
    public void onDestroy(){mainRegistry.clear(); observers.clear();}

}
