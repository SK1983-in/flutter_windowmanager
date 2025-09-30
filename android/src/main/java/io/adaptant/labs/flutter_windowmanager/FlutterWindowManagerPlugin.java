package io.adaptant.labs.flutter_windowmanager;

import android.app.Activity;
import android.os.Build;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * FlutterWindowManagerPlugin (updated for Flutter embedding v2)
 */
public class FlutterWindowManagerPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {

    private MethodChannel channel;
    private Activity activity;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        channel = new MethodChannel(binding.getBinaryMessenger(), "flutter_windowmanager");
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        if (channel != null) {
            channel.setMethodCallHandler(null);
            channel = null;
        }
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if (call.method.equals("addFlagsSecure")) {
            if (activity != null) {
                activity.runOnUiThread(() -> {
                    activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
                });
                result.success(true);
            } else {
                result.error("NO_ACTIVITY", "No active activity found", null);
            }
        } else if (call.method.equals("clearFlagsSecure")) {
            if (activity != null) {
                activity.runOnUiThread(() -> {
                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
                });
                result.success(true);
            } else {
                result.error("NO_ACTIVITY", "No active activity found", null);
            }
        } else {
            result.notImplemented();
        }
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        activity = null;
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivity() {
        activity = null;
    }
}
