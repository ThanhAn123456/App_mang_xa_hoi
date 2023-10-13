package Dto;
import android.widget.ImageView;
public class Home {
    public int avatar;
    public String name;
    public String time;
    public int image_content;

    public Home(int avatar, String name, String time, int image_content) {
        this.avatar = avatar;
        this.name = name;
        this.time = time;
        this.image_content = image_content;
    }

    public Home() {

    }
}
