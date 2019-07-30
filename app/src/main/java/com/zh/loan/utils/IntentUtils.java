package com.zh.loan.utils;

import android.content.Context;
import android.content.Intent;

public class IntentUtils {

    private static Intent intent;

    public static  void doIntent(Context context,Class cls){
        intent = new Intent(context,cls);
        context.startActivity(intent);
    }
}
