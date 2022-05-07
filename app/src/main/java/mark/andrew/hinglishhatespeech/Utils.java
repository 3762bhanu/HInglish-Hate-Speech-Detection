package mark.andrew.hinglishhatespeech;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class Utils {
    static ArrayList<Post> allPosts;


    public static ArrayList<Post> getAllPosts(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences",MODE_PRIVATE);
        String[] allCaptions = sharedPreferences.getString("allPosts", "").split("&&",-1);


        allPosts = new ArrayList<>();

        for(String caption:allCaptions){
            allPosts.add(new Post(caption,context));
            System.out.println(caption+"here1");
        }

        if(Utils.allPosts==null){
            Utils.allPosts = new ArrayList<>();
        }


        return Utils.allPosts;
    }

    public static void addToAllPosts(Post post, Context context) {
//        if(allPosts == null){
//            Utils.allPosts = getAllPosts(context);
//        }
//        Utils.allPosts.add(post);

        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("allPosts",sharedPreferences.getString("allPosts", "")+"&&"+ post.caption);
        editor.apply();

    }


}
