package Entity;

public class AddMemberModel {
    private String uid, name, profileImage;

    public AddMemberModel(String uid, String name, String profileImage) {
        this.uid = uid;
        this.name = name;
        this.profileImage = profileImage;
    }

    public AddMemberModel() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
