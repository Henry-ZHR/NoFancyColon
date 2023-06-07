package moe.henry_zhr.no_fancy_colon;

import android.content.Context;

import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class MainHook implements IXposedHookLoadPackage {
  @Override
  public void handleLoadPackage(LoadPackageParam lpparam) {
    if (!lpparam.packageName.equals("com.android.systemui"))
      return;

    // https://cs.android.com/android/platform/superproject/+/android-11.0.0_r48:frameworks/base/packages/SystemUI/src/com/android/keyguard/KeyguardStatusView.java;l=382-383
    final Class<?> clazz = XposedHelpers.findClass(
        "com.android.keyguard.KeyguardStatusView.Patterns",
        lpparam.classLoader
    );
    final Field[] views = new Field[]{
        XposedHelpers.findField(clazz, "clockView12"),
        XposedHelpers.findField(clazz, "clockView24")
    };
    XposedHelpers.findAndHookMethod(
        clazz,
        "update",
        Context.class,
        new XC_MethodHook() {
          @Override
          protected void afterHookedMethod(MethodHookParam param) throws IllegalAccessException {
            for (final Field f : views) {
              f.set(null, ((String) f.get(null)).replace("\uee01", ":"));
            }
          }
        }
    );
  }
}
