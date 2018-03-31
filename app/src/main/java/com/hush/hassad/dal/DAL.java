package com.hush.hassad.dal;

public class DAL {

    private static DAL instance = null;

    private DAL(){}

    public static DAL getInstance(){
        if(instance == null)
            instance = new DAL();
        return instance;
    }
}
