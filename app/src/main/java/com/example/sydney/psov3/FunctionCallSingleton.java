package com.example.sydney.psov3;

/**
 * Created by PROGRAMMER2 on 6/13/2018.
 * ABZTRAK INC.
 */

public class FunctionCallSingleton {
    private static FunctionCallSingleton ourInstance = null;

    private FunctionCallSingleton() {
    }

    public static FunctionCallSingleton getInstance() {
        if (ourInstance == null)
            ourInstance = new FunctionCallSingleton();
        return ourInstance;
    }
}
