package Entity;

public class Lv_ListNews {
    public int avatar;
    public String name;
    public int statuspost;
    public String time;
    public String content;
    public int image_content;
    public int numberlike;
    public int numbercmt;
    public int numbershare;

    public Lv_ListNews(int avatar, String name, String time,String content, int image_content,int statuspost,int numberlike,int numbercmt,int numbershare) {
        this.avatar = avatar;
        this.name = name;
        this.time = time;
        this.content=content;
        this.image_content = image_content;
        this.statuspost=statuspost;
        this.numberlike=numberlike;
        this.numbercmt=numbercmt;
        this.numbershare=numbershare;
    }

    public Lv_ListNews() {

    }
}
