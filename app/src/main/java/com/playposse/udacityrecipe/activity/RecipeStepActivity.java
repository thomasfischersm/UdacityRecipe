package com.playposse.udacityrecipe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.playposse.udacityrecipe.R;

/**
 * An {@link Activity} that shows an individual step in a recipe.
 */
public class RecipeStepActivity extends ParentActivity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_recipe_step;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Go to full screen in landscape.
        if(ActivityNavigator.isLandscape(this) && !ActivityNavigator.isTablet(this)) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            hideToolbar();
        }
    }
}
