package Dto;

import android.widget.ImageView;

public class Friend_Group {
   public int avatar;
   public String name;
   public int numberfriend;
   public int sendmess;
   public int disablefriend;

   public Friend_Group(int avatar, String name, int numberfriend, int sendmess, int disablefriend) {
      this.avatar = avatar;
      this.name = name;
      this.numberfriend = numberfriend;
      this.sendmess = sendmess;
      this.disablefriend = disablefriend;
   }

   public Friend_Group() {
   }

}
