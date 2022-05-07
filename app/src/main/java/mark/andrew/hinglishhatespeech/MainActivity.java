package mark.andrew.hinglishhatespeech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    private PostRecViewAdapter postRecViewAdapter;
    private RecyclerView postRecView;

    View btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        postRecView = findViewById(R.id.PostsRecView);
        btnAdd = findViewById(R.id.fabAddFile);

        postRecViewAdapter = new PostRecViewAdapter(Utils.getAllPosts(this),this);
        postRecView.setAdapter(postRecViewAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        layoutManager.setReverseLayout(true);
        postRecView.setLayoutManager(layoutManager);
        postRecView.scrollToPosition(postRecViewAdapter.getItemCount()-1);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddPostActivity.class);
                startActivity(intent);



            }
        });

        Utils.getAllPosts(this);

    }

}
