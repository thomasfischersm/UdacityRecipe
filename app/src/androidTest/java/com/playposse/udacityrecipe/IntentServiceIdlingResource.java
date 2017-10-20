package com.playposse.udacityrecipe;

import android.app.ActivityManager;
import android.content.Context;
import android.support.test.espresso.IdlingResource;

import com.playposse.udacityrecipe.service.RecipeDownloadService;

/**
 * An {@link IdlingResource} that waits for {@link RecipeDownloadService}.
 *
 * <p>I copied this out of a GitHub project because I didn't want to include the whole project:
 * https://github.com/chiuki/espresso-samples.
 */
class IntentServiceIdlingResource implements IdlingResource {

    private final Context context;
    private ResourceCallback resourceCallback;

    IntentServiceIdlingResource(Context context) {
        this.context = context;
    }

    @Override
    public String getName() {
        return IntentServiceIdlingResource.class.getName();
    }

    @Override
    public boolean isIdleNow() {
        boolean idle = !isIntentServiceRunning();
        if (idle && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }
        return idle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }

    private boolean isIntentServiceRunning() {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo info : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (RecipeDownloadService.class.getName().equals(info.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
