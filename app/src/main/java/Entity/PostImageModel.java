package Entity;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class PostImageModel {
    private String imageUrl, id, description,uid;
    @ServerTimestamp
    private Date timetamp;
    public PostImageModel(){}

    public PostImageModel(String imageUrl, String id, String description, String uid, Date timetamp) {
        this.imageUrl = imageUrl;
        this.id = id;
        this.description = description;
        this.uid = uid;
        this.timetamp = timetamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getTimetamp() {
        return timetamp;
    }

    public void setTimetamp(Date timetamp) {
        this.timetamp = timetamp;
    }
}
