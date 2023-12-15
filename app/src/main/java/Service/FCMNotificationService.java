package Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.ltdd_app_mang_xa_hoi.ChatActivity;
import com.example.ltdd_app_mang_xa_hoi.ProfileActivity;
import com.example.ltdd_app_mang_xa_hoi.R;
import com.example.ltdd_app_mang_xa_hoi.SplashActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;

public class FCMNotificationService extends FirebaseMessagingService {
    Intent resultIntent = null;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("FCMNotificationService", "Message received from: " + remoteMessage.getFrom());
        // Hiển thị thông báo tùy chỉnh
        showCustomNotification(remoteMessage);
    }

    private void showCustomNotification(RemoteMessage remoteMessage) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (remoteMessage.getData().get("userIdFollow") != null) {
            resultIntent = new Intent(this, ProfileActivity.class);
            resultIntent.putExtra("userId", remoteMessage.getData().get("userIdFollow"));
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            sendNotification(notificationManager, resultIntent, remoteMessage);
        } else if (remoteMessage.getData().get("ChatID") != null) {
            String ChatID = remoteMessage.getData().get("ChatID");
            FirebaseFirestore.getInstance().collection("Messages").document(ChatID)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    ArrayList<String> uid = (ArrayList<String>) document.get("uid");
                                    boolean isGroupChat = document.getBoolean("isGroupChat");
                                    Intent intent= new Intent(FCMNotificationService.this,ChatActivity.class);
                                    intent.putStringArrayListExtra("uid",uid);
                                    intent.putExtra("isGroupChat",isGroupChat);
                                    intent.putExtra("id",ChatID);
                                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                   // startActivity(intent);
                                    sendNotification(notificationManager, intent, remoteMessage);
                                }
                            }
//                            resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            sendNotification(notificationManager, resultIntent, remoteMessage);
                        }
                    });


        }
    }

    private void sendNotification(NotificationManager notificationManager, Intent resultIntent, RemoteMessage remoteMessage) {
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE // Chọn FLAG_MUTABLE hoặc FLAG_IMMUTABLE
        );
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true);
        notificationManager.notify(1, notificationBuilder.build());
    }

}
