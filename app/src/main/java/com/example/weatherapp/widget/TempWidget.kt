package com.example.weatherapp.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.weatherapp.HomeActivity
import com.example.weatherapp.R


class TempWidget : AppWidgetProvider() {

    companion object {
        const val TEMP = "50C"
        const val CITY = "Ban" +
                "dung"
        const val STATUS = "Slow Raining"
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, CITY, TEMP, STATUS)
        }
    }

    override fun onEnabled(context: Context) {
        // No specific actions needed on widget enabled
    }

    override fun onDisabled(context: Context) {
        // No specific actions needed on widget disabled
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == "UPDATE_WIDGET") {
            val cityName = intent.getStringExtra("CITY_NAME") ?: CITY
            val temperature = intent.getStringExtra("TEMPERATURE") ?: TEMP
            val status = intent.getStringExtra("STATUS") ?: STATUS
            updateWidget(context, cityName, temperature, status)
        }
    }

    private fun updateWidget(context: Context, city: String, temperature: String, status: String) {
        val views = RemoteViews(context.packageName, R.layout.temp_widget)
        views.setTextViewText(R.id.city_name, city)
        views.setTextViewText(R.id.temp, "$temperature°C")
        views.setTextViewText(R.id.status, status)

        val intent = Intent(context, HomeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent)
        AppWidgetManager.getInstance(context).updateAppWidget(
            AppWidgetManager.getInstance(context).getAppWidgetIds(
                ComponentName(context, TempWidget::class.java)
            ), views
        )
    }
}
