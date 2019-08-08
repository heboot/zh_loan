package com.zh.loan.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;
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




}
