package com.zh.loan.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;
import com.zh.loan.R;
import com.zh.loan.databinding.ViewRefreshBinding;
import com.zh.loan.utils.MMMV;

public class MRefreshView extends QMUIPullRefreshLayout {
    public MRefreshView(Context context) {
        super(context);
    }

    public MRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View createRefreshView() {
        return new MMMV(getContext());
    }

    public static class RefreshViewT extends LinearLayout {
        private static final int MAX_ALPHA = 255;
        private static final float TRIM_RATE = 0.85f;
        private static final float TRIM_OFFSET = 0.4f;

        static final int CIRCLE_DIAMETER = 20;
        static final int CIRCLE_DIAMETER_LARGE = 56;

        private CircularProgressDrawable mProgress;
        private int mCircleDiameter;

        public RefreshViewT(Context context) {
            super(context);
            View view =  LayoutInflater.from(getContext()).inflate(R.layout.view_refresh, this);
            view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getResources().getDimensionPixelOffset(R.dimen.y100)));
        }

        public RefreshViewT(Context context, AttributeSet attrs) {
            super(context, attrs); LayoutInflater.from(getContext()).inflate(R.layout.view_refresh, this);
        }

        public RefreshViewT(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr); LayoutInflater.from(getContext()).inflate(R.layout.view_refresh, this);
        }
//
//        @Override
//        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//            setMeasuredDimension(mCircleDiameter, mCircleDiameter);
//        }



        public void setSize(@CircularProgressDrawable.ProgressDrawableSize int size) {
            if (size != CircularProgressDrawable.LARGE && size != CircularProgressDrawable.DEFAULT) {
                return;
            }
            final DisplayMetrics metrics = getResources().getDisplayMetrics();
            if (size == CircularProgressDrawable.LARGE) {
                mCircleDiameter = (int) (CIRCLE_DIAMETER_LARGE * metrics.density);
            } else {
                mCircleDiameter = (int) (CIRCLE_DIAMETER * metrics.density);
            }
            // force the bounds of the progress circle inside the circle view to
            // update by setting it to null before updating its size and then
            // re-setting it
            mProgress.setStyle(size);
        }

        public void stop() {
            mProgress.stop();
        }

        public void doRefresh() {
            mProgress.start();
        }

        public void setColorSchemeResources(@ColorRes int... colorResIds) {
            final Context context = getContext();
            int[] colorRes = new int[colorResIds.length];
            for (int i = 0; i < colorResIds.length; i++) {
                colorRes[i] = ContextCompat.getColor(context, colorResIds[i]);
            }
            setColorSchemeColors(colorRes);
        }

        public void setColorSchemeColors(@ColorInt int... colors) {
            mProgress.setColorSchemeColors(0xffFA4169);
        }
    }

    public static class RefreshView extends AppCompatImageView implements IRefreshView {
        private static final int MAX_ALPHA = 255;
        private static final float TRIM_RATE = 0.85f;
        private static final float TRIM_OFFSET = 0.4f;

        static final int CIRCLE_DIAMETER = 20;
        static final int CIRCLE_DIAMETER_LARGE = 56;

        private CircularProgressDrawable mProgress;
        private int mCircleDiameter;

        public RefreshView(Context context) {
            super(context);
            mProgress = new CircularProgressDrawable(context);
            setColorSchemeColors(QMUIResHelper.getAttrColor(context, R.attr.qmui_config_color_blue));
            mProgress.setStyle(CircularProgressDrawable.DEFAULT);
            mProgress.setAlpha(MAX_ALPHA);
            mProgress.setArrowScale(0.8f);
            setImageDrawable(mProgress);
            final DisplayMetrics metrics = getResources().getDisplayMetrics();
            mCircleDiameter = (int) (CIRCLE_DIAMETER * metrics.density);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(mCircleDiameter, mCircleDiameter);
        }

        @Override
        public void onPull(int offset, int total, int overPull) {
            if (mProgress.isRunning()) {
                return;
            }
            float end = TRIM_RATE * offset / total;
            float rotate = TRIM_OFFSET * offset / total;
            if (overPull > 0) {
                rotate += TRIM_OFFSET * overPull / total;
            }
            mProgress.setArrowEnabled(true);
            mProgress.setStartEndTrim(0, end);
            mProgress.setProgressRotation(rotate);
        }

        public void setSize(@CircularProgressDrawable.ProgressDrawableSize int size) {
            if (size != CircularProgressDrawable.LARGE && size != CircularProgressDrawable.DEFAULT) {
                return;
            }
            final DisplayMetrics metrics = getResources().getDisplayMetrics();
            if (size == CircularProgressDrawable.LARGE) {
                mCircleDiameter = (int) (CIRCLE_DIAMETER_LARGE * metrics.density);
            } else {
                mCircleDiameter = (int) (CIRCLE_DIAMETER * metrics.density);
            }
            // force the bounds of the progress circle inside the circle view to
            // update by setting it to null before updating its size and then
            // re-setting it
            setImageDrawable(null);
            mProgress.setStyle(size);
            setImageDrawable(mProgress);
        }

        public void stop() {
            mProgress.stop();
        }

        public void doRefresh() {
            mProgress.start();
        }

        public void setColorSchemeResources(@ColorRes int... colorResIds) {
            final Context context = getContext();
            int[] colorRes = new int[colorResIds.length];
            for (int i = 0; i < colorResIds.length; i++) {
                colorRes[i] = ContextCompat.getColor(context, colorResIds[i]);
            }
            setColorSchemeColors(colorRes);
        }

        public void setColorSchemeColors(@ColorInt int... colors) {
            mProgress.setColorSchemeColors(0xffFA4169);
        }
    }


}
