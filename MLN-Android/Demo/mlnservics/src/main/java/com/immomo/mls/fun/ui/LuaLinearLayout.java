package com.immomo.mls.fun.ui;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.immomo.mls.base.ud.lv.ILViewGroup;
import com.immomo.mls.fun.ud.view.UDLinearLayout;
import com.immomo.mls.fun.ud.view.UDView;
import com.immomo.mls.fun.weight.BorderRadiusLinearLayout;
import com.immomo.mls.utils.ErrorUtils;

import androidx.annotation.NonNull;

public class LuaLinearLayout<U extends UDLinearLayout> extends BorderRadiusLinearLayout implements ILViewGroup<U> {
    protected U userdata;
    private ViewLifeCycleCallback cycleCallback;

    public LuaLinearLayout(Context context, U ud, int type) {
        super(context);
        userdata = ud;
        setViewLifeCycleCallback(userdata);
        setOrientation(type);
    }

    @Override
    public void bringSubviewToFront(UDView child) {
        ErrorUtils.debugLuaError("LinearLayout does not support bringSubviewToFront method", userdata.getGlobals());
    }

    @Override
    public void sendSubviewToBack(UDView child) {
        ErrorUtils.debugLuaError("LinearLayout does not support sendSubviewToBack method", userdata.getGlobals());
    }

    @NonNull
    @Override
    public ViewGroup.LayoutParams applyLayoutParams(ViewGroup.LayoutParams src, UDView.UDLayoutParams udLayoutParams) {
        LayoutParams ret = parseLayoutParams(src);
        ret.setMargins(udLayoutParams.realMarginLeft, udLayoutParams.realMarginTop, udLayoutParams.realMarginRight, udLayoutParams.realMarginBottom);
        ret.gravity = udLayoutParams.gravity;
        ret.priority = udLayoutParams.priority;
        ret.weight = udLayoutParams.weight;
        return ret;
    }

    @NonNull
    @Override
    public ViewGroup.LayoutParams applyChildCenter(ViewGroup.LayoutParams src, UDView.UDLayoutParams udLayoutParams) {
        return src;
    }

    @Override
    public U getUserdata() {
        return userdata;
    }

    @Override
    public void setViewLifeCycleCallback(ViewLifeCycleCallback cycleCallback) {
        this.cycleCallback = cycleCallback;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (cycleCallback != null) {
            cycleCallback.onAttached();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (cycleCallback != null) {
            cycleCallback.onDetached();
        }
    }

    private LayoutParams parseLayoutParams(ViewGroup.LayoutParams src) {
        if (src == null) {
            src = generateNewWrapContentLayoutParams();
        } else if (!(src instanceof LayoutParams)) {
            if (src instanceof ViewGroup.MarginLayoutParams) {
                src = generateNewLayoutParams((ViewGroup.MarginLayoutParams) src);
            } else {
                src = generateNewLayoutParams(src);
            }
        }
        return (LayoutParams) src;
    }

    protected @NonNull
    ViewGroup.LayoutParams generateNewWrapContentLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    protected @NonNull
    ViewGroup.LayoutParams generateNewLayoutParams(ViewGroup.MarginLayoutParams src) {
        return new LayoutParams(src);
    }

    protected @NonNull
    ViewGroup.LayoutParams generateNewLayoutParams(ViewGroup.LayoutParams src) {
        return new LayoutParams(src);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return isEnabled() && super.dispatchTouchEvent(ev);
    }
}