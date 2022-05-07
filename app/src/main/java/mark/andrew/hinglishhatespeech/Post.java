package mark.andrew.hinglishhatespeech;

import android.content.Context;


public class Post {

    final Context context;
    String caption;
    boolean isHateful;
    int id;

    public boolean isHateful() {
        return isHateful;
    }

    public void setHateful(boolean hateful) {
        isHateful = hateful;
    }

    public Post(String caption, Context context) {
        this.caption = caption;
        this.context = context;
        this.id = Utils.allPosts.size();
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
