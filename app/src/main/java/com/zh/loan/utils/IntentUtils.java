package com.zh.loan.utils;

import android.content.Context;
import android.content.Intent;

import com.waw.hr.mutils.MKey;
import com.zh.loan.R;
import com.zh.loan.activity.CashActivity;
import com.zh.loan.activity.StatusTipActivity;

public class IntentUtils {

    private static Intent intent;

    public static  void doIntent(Context context,Class cls){
        intent = new Intent(context,cls);
        context.startActivity(intent);
    }




    public static void intent2CashActivity(Context context,String balance,String bankNum){
        intent = new Intent(context,CashActivity.class);
        intent.putExtra(MKey.BANK_CARD,bankNum);
        intent.putExtra(MKey.BALANCE,balance);
        context.startActivity(intent);
    }

    public static void intent2StatusTipActivity(Context context,String title,String tip1,String tip2,int Resid){
        intent = new Intent(context,StatusTipActivity.class);
        intent.putExtra(MKey.TITLE,title);
        intent.putExtra(MKey.TIP,tip1);
        intent.putExtra(MKey.TYPE,Resid);
        intent.putExtra(MKey.TIP2,tip2);
        context.startActivity(intent);
    }
}
