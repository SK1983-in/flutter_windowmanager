package io.adaptant.labs.flutter_windowmanager;

import android.os.Build;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

public class FlutterWindowManagerPlugin implements FlutterPlugin, MethodCallHandler {

    private MethodChannel channel;
    private Activity activity;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        channel = new MethodChannel(binding.getBinaryMessenger(), "flutter_windowmanager");
        channel.setMethodCallHandler(this);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if (call.method.equals("addFlagsSecure")) {
            if (activity != null) {
                activity.runOnUiThread(() -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
                    }
                });
                result.success(true);
            } else {
                result.error("NO_ACTIVITY", "Activity not attached", null);
            }
        } else {
            result.notImplemented();
        }
    }
}

