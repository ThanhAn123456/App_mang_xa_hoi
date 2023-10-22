package Entity;

public class Lv_ListCmt {
    public int avatar;
    public String name;
    public int status;
    public String cmt;
    public int image_cmt;

    public Lv_ListCmt(int avatar, String name, String cmt,int image_cmt, int status) {
        this.avatar = avatar;
        this.name = name;
        this.cmt = cmt;
        this.status = status;
        this.image_cmt = image_cmt;
    }

}
