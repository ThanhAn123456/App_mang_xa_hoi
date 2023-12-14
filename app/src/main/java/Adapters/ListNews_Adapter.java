package Adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ltdd_app_mang_xa_hoi.NewsDetailActivity;
import com.example.ltdd_app_mang_xa_hoi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import Entity.HomeModel;
import Entity.Users;
import Util.FireBaseUtil;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ListNews_Adapter extends RecyclerView.Adapter<ListNews_Adapter.HomeHolder> {
    private List<HomeModel> list;
    Context context;
    FirebaseUser user;
    String token;

    public ListNews_Adapter(Context context, List<HomeModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_listnews, parent, false);
        return new HomeHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {
         user = FirebaseAuth.getInstance().getCurrentUser();
        HomeModel item = list.get(position);
        holder.name.setText(item.getName());
        List<String> likeList = item.getLikes();
        int count = likeList.size();
        Random random = new Random();
        Glide.with(context.getApplicationContext())
                .load(item.getProfileImage())
                .timeout(6500)
                .into(holder.profileImage);
        holder.statuspost.setImageResource(R.drawable.ic_public);
        Date timestamp = item.getTimestamp();

        holder.time.setText(calculateTime(list.get(position).getTimestamp()));
        holder.content.setText(item.getDescription());
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        ColorDrawable colorDrawable = new ColorDrawable(color);

        Glide.with(context.getApplicationContext())
                .load(item.getImageUrl())
                .placeholder(colorDrawable)
                .timeout(7000)
                .into(holder.image_content);

            holder.numlike.setText(count + "");

        //check if already like
        assert user != null;
        holder.clickListener(position,
                list.get(position).getId(),
                list.get(position).getName(),
                list.get(position).getUid(),
                list.get(position).getLikes(),
                list.get(position).getImageUrl()
        );

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    String calculateTime(Date date) {
        if (date != null) {
            long millis = date.toInstant().toEpochMilli();
            return DateUtils.getRelativeTimeSpanString(millis, System.currentTimeMillis(), 60000, DateUtils.FORMAT_ABBREV_TIME).toString();
        } else {
            return ""; // hoặc giá trị mặc định khác tùy vào yêu cầu của bạn
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class HomeHolder extends RecyclerView.ViewHolder {
        private CircleImageView profileImage;
        private TextView name, time, content, numlike, numcmt, numshare;
        private ImageView image_content, statuspost;
        private ImageButton imagecmt, imageshare;
        private CheckBox imagelike;

        public HomeHolder(@NonNull View itemview) {
            super(itemview);
            profileImage = itemview.findViewById(R.id.profileImage);
            statuspost = itemview.findViewById(R.id.statuspost);
            name = itemview.findViewById(R.id.name);
            time = itemview.findViewById(R.id.time);
            content = itemview.findViewById(R.id.contentnews);
            image_content = itemview.findViewById(R.id.imagecontent);
            numlike = itemview.findViewById(R.id.numberlike);
            numcmt = itemview.findViewById(R.id.numbercmt);
            numshare = itemview.findViewById(R.id.numbershare);
            imagelike = itemview.findViewById(R.id.like);
            imagecmt = itemview.findViewById(R.id.cmt);
            imageshare = itemview.findViewById(R.id.share);
        }

        public void clickListener(final int position, final String id, String name, final String uid, final List<String> likeList, final String imageUrl) {
            imagelike.setChecked(likeList.contains(user.getUid()));
            imagelike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (likeList.contains(user.getUid())) {
                        likeList.remove(user.getUid()); // Bỏ like
                        readToken(uid);
                        sendNotification(user.getDisplayName()+" đã bỏ like bài viết của bạn");
                    } else {
                        likeList.add(user.getUid()); // Like
                        readToken(uid);
                        sendNotification(user.getDisplayName()+" đã like bài viết của bạn");
                    }
                    numlike.setText(likeList.size()+"");

                    // Gửi cập nhật đến Firestore
                    DocumentReference reference = FirebaseFirestore.getInstance().collection("User")
                            .document(uid)
                            .collection("Post Images")
                            .document(id);

                    Map<String, Object> map = new HashMap<>();
                    map.put("likes", likeList);
                    reference.update(map);
                }

            });
            imageshare.setOnClickListener(v -> {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, imageUrl);
                intent.setType("text/*");
                context.startActivity(Intent.createChooser(intent, "Share link using..."));

            });
            if (context != null) {
                imagecmt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, NewsDetailActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("uid", uid);

                        context.startActivity(intent);
                    }
                });
            }
        }
    }
    public void sendNotification(String message) {
        FireBaseUtil.currentUserDetail().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Users currentUser = task.getResult().toObject(Users.class);
                    try {
                        JSONObject jsonObject = new JSONObject();
                        JSONObject notificationObj = new JSONObject();
                        notificationObj.put("title", currentUser.getName());
                        notificationObj.put("body", message);

                        JSONObject dataObj = new JSONObject();
                        dataObj.put("userIdLike", currentUser.getUid());

                        jsonObject.put("notification", notificationObj);
                        jsonObject.put("data", dataObj);
                        jsonObject.put("to", token);

                        callApi(jsonObject);
                    } catch (Exception e) {

                    }
                }
            }
        });
    }
    public void readToken(String uid) {
        DocumentReference reference = FirebaseFirestore.getInstance().collection("User").document(uid);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Lấy trường token từ tài liệu người dùng
                        token = document.getString("fcmToken");
                        Log.d("Firestore", "Token: " + token);
                    } else {
                        Log.d("Firestore", "Không tìm thấy tài liệu");
                    }
                } else {
                    Log.e("Firestore", "Lỗi khi lấy dữ liệu", task.getException());
                }
            }
        });
    }
    void callApi(JSONObject jsonObject) {
        MediaType JSON = MediaType.get("application/json");

        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", "Bearer AAAAro3DwRA:APA91bGkU5CTR11JSryE0ZwIdoSCgufYoSNhVajESiy5cMCxS9P8xYm40KJFMzsCSqm8IGZyn_1gx0cpUxY006o-zOywYEYH9EbKxQb2tq71qkZvTw62bLFKK90csF8ExGcCxhnPPdox")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });
    }

}
