package com.example.starshow

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


const val channelID = "notification_channel"
const val channelName = "com.example.starshow"
class MessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null)
        {
            generateNotification(remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!)
        }
    }

    fun getRemoteView(title:String, message: String) : RemoteViews {
        val remoteView = RemoteViews("com.example.starshow", R.layout.notification)

        remoteView.setTextViewText(R.id.title, title)
        remoteView.setTextViewText(R.id.description, message)
        remoteView.setImageViewResource(R.id.app_logo, R.drawable.circlepng)

        return remoteView
    }


    fun generateNotification(title:String, message:String){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelID).setSmallIcon(R.drawable.circlepng).setAutoCancel(true).setVibrate(
            longArrayOf(1000, 1000, 1000, 1000)).setOnlyAlertOnce(true).setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title, message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0, builder.build())
    }
}