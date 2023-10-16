package Dto;

public class Lv_ListNotification {
    public int avatar;
    public String name;
    public int status;
    public int notify;

    public Lv_ListNotification(int avatar, String name, int notify, int status) {
        this.avatar = avatar;
        this.name = name;
        this.notify = notify;
        this.status = status;
    }
}
