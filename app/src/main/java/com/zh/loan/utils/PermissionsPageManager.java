package com.zh.loan.utils;

import android.content.Intent;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.zh.loan.utils.manufacturer.HUAWEI;
import com.zh.loan.utils.manufacturer.MEIZU;
import com.zh.loan.utils.manufacturer.OPPO;
import com.zh.loan.utils.manufacturer.PermissionsPage;
import com.zh.loan.utils.manufacturer.Protogenesis;
import com.zh.loan.utils.manufacturer.VIVO;
import com.zh.loan.utils.manufacturer.XIAOMI;

public class PermissionsPageManager {

    /**
     * Build.MANUFACTURER
     */
    static final String MANUFACTURER_HUAWEI = "HUAWEI";
    static final String MANUFACTURER_XIAOMI = "XIAOMI";
    static final String MANUFACTURER_OPPO = "OPPO";
    static final String MANUFACTURER_VIVO = "vivo";
    static final String MANUFACTURER_MEIZU = "meizu";
    static final String manufacturer = Build.MANUFACTURER;

    public static String getManufacturer() {
        return manufacturer;
    }

    public static Intent getIntent(Activity activity) {
        PermissionsPage permissionsPage = new Protogenesis(activity);
        try {
            if (MANUFACTURER_HUAWEI.equalsIgnoreCase(manufacturer)) {
                permissionsPage = new HUAWEI(activity);
            } else if (MANUFACTURER_OPPO.equalsIgnoreCase(manufacturer)) {
                permissionsPage = new OPPO(activity);
            } else if (MANUFACTURER_VIVO.equalsIgnoreCase(manufacturer)) {
                permissionsPage = new VIVO(activity);
            } else if (MANUFACTURER_XIAOMI.equalsIgnoreCase(manufacturer)) {
                permissionsPage = new XIAOMI(activity);
            } else if (MANUFACTURER_MEIZU.equalsIgnoreCase(manufacturer)) {
                permissionsPage = new MEIZU(activity);
            }

            return permissionsPage.settingIntent();
        } catch (Exception e) {
            Log.e("Permissions4M", "手机品牌为：" + manufacturer + "异常抛出，：" + e.getMessage());
            permissionsPage = new Protogenesis(activity);
            return ((Protogenesis) permissionsPage).settingIntent();
        }
    }

    public static Intent getSettingIntent(Activity activity) {
        return new Protogenesis(activity).settingIntent();
    }

    public static boolean isXIAOMI() {
        return getManufacturer().equalsIgnoreCase(MANUFACTURER_XIAOMI);
    }

    public static boolean isOPPO() {
        return getManufacturer().equalsIgnoreCase(MANUFACTURER_OPPO);
    }

    public static boolean isMEIZU() {
        return getManufacturer().equalsIgnoreCase(MANUFACTURER_MEIZU);
    }

}
