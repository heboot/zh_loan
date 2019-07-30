package com.zh.loan.utils;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zh.loan.R;

public class ImageUtils {

    private static RequestOptions avatarRequestOptions;

    public static void showAvatar(Activity activity, ImageView iv, String url) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {

        } else {
            if (avatarRequestOptions == null) {
                avatarRequestOptions = new RequestOptions();
                avatarRequestOptions.placeholder(R.mipmap.icon_head_normal);
                avatarRequestOptions.error(R.mipmap.icon_head_normal);
            }
            Glide.with(activity).setDefaultRequestOptions(avatarRequestOptions).load(url).into(iv);
        }

    }

    public static void showImage(Activity activity, ImageView iv, String url) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {

        } else {
            Glide.with(activity).load(url).into(iv);
        }

    }

}
