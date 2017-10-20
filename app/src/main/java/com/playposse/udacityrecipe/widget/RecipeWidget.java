package com.playposse.udacityrecipe.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.playposse.udacityrecipe.R;
import com.playposse.udacityrecipe.activity.ActivityNavigator;
import com.playposse.udacityrecipe.data.RecipePhotoLibrary;

/**
 * A widget that displays the recipe steps of the last selected recipe.
 */
public class RecipeWidget extends AppWidgetProvider {

    static void updateAppWidget(
            Context context,
            AppWidgetManager appWidgetManager,
            int appWidgetId,
            long recipeId,
            String recipeName,
            String recipeSteps,
            String recipePhotoUrl) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        // Set text.
        views.setTextViewText(R.id.recipe_name_text_view, recipeName);
        views.setTextViewText(R.id.recipe_steps_text_view, recipeSteps);
        views.setViewVisibility(R.id.select_recipe_hint_text_view, View.GONE);

        // Load background photo.
        RecipePhotoLibrary.loadPhotoInWidget(
                context,
                views,
                appWidgetId,
                R.id.recipe_photo_image_view,
                recipePhotoUrl,
                recipeName);

        // Prepare click listener.
        Intent intent = ActivityNavigator.createRecipeActivityIntent(context, recipeId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.root_view, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Ignore. Only update when a recipe is selected.
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(WidgetCommunication.RECIPE_SELECTED_ACTION)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, RecipeWidget.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);

            long recipeId = intent.getLongExtra(WidgetCommunication.RECIPE_ID, -1);
            String recipeName = intent.getStringExtra(WidgetCommunication.RECIPE_NAME);
            String recipeSteps = intent.getStringExtra(WidgetCommunication.RECIPE_STEPS);
            String recipePhotoUrl = intent.getStringExtra(WidgetCommunication.RECIPE_PHOTO_URL);

            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(
                        context,
                        appWidgetManager,
                        appWidgetId,
                        recipeId,
                        recipeName,
                        recipeSteps,
                        recipePhotoUrl);
            }
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

