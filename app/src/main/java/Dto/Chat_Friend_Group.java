package Dto;

public class Chat_Friend_Group {
    public int avatar;
    public String name;
    public String status;
    public int offline_time;
    public int notification;

    public Chat_Friend_Group() {

    }
    public Chat_Friend_Group(int avatar, String name, String status, int offline_time, int notification) {
        this.avatar = avatar;
        this.name = name;
        this.status = status;
        this.offline_time = offline_time;
        this.notification = notification;
    }
}
