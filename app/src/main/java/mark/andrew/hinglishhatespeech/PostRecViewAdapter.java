package mark.andrew.hinglishhatespeech;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

import com.bumptech.glide.Glide;

public class PostRecViewAdapter extends RecyclerView.Adapter<PostRecViewAdapter.ViewHolder> {

    private ArrayList<Post> posts = new ArrayList<>();
    private static final String TAG = "PostRecViewAdapter";
    private final Context mContext;
    private static boolean isClickable;

    public PostRecViewAdapter(ArrayList<Post> posts, Context mContext) {
        this.posts = posts;
        this.mContext = mContext;
        PostRecViewAdapter.isClickable = true;
    }


    public static void setClickable(boolean clickable) {
        PostRecViewAdapter.isClickable = clickable;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder:Called");
        holder.txtName.setText(posts.get(position).getCaption());

        File postImgFile = new File(
                mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                        .getPath() + "/" + position);

        if(!postImgFile.exists()){
            holder.imgPost.setVisibility(View.GONE);
            return;
        }else {
            holder.imgPost.setVisibility(View.VISIBLE);
        }

        Glide.with(mContext)
                .asBitmap()
                .load(Uri.fromFile( postImgFile))
                .into(holder.imgPost);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView parent;
        private final ImageView imgPost;
        private final TextView txtName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.ParentCard);
            imgPost = itemView.findViewById(R.id.Image);

            txtName = itemView.findViewById(R.id.txtCaption);

        }
    }
}
