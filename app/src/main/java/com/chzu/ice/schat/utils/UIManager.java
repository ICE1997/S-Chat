package com.chzu.ice.schat.utils;

import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class UIManager {
    private UIManager() {
    }

    public static void setImmersiveFullScreen(Window window) {
//这一步最好要做，因为如果这两个flag没有清除的话下面没有生效
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//设置布局能够延伸到状态栏(StatusBar)和导航栏(NavigationBar)里面
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//设置状态栏(StatusBar)颜色透明
        window.setStatusBarColor(Color.TRANSPARENT);
//设置导航栏(NavigationBar)颜色透明
        window.setNavigationBarColor(Color.TRANSPARENT);
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE//保持系统的稳定性
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION//隐藏导航栏的布局，但是SYSTEM_UI_FLAG_HIDE_NAVIGATION不设置不会生效
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION//隐藏号航栏
                | View.SYSTEM_UI_FLAG_IMMERSIVE//沉浸式，会全屏
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY//粘性沉浸式，下滑和上滑才能显示状态栏和导航栏
                | 0x00200000 |//隐藏导航栏的back键
                0x00400000 |//隐藏导航栏的home键
                0x01000000;//隐藏导航栏的recent键
        window.getDecorView().setSystemUiVisibility(flags);
    }

    public static void setFullScreen(Window window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
