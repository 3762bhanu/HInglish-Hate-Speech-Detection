package mark.andrew.hinglishhatespeech;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

public class AddPostActivity extends AppCompatActivity {
    View btnLeft,btnRight,btnAddImage;
    private Uri imagePath;
    private ImageView previewImg;
    private View imageFrame,btnRemoveImage;
    private View btnRemoveImg;
    private Bitmap bitmapImage;
    View btnBack;
    private EditText txtCaption;
    int isHateful=-1;
    private String currentText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);


        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);
        previewImg = findViewById(R.id.postImg);
        btnRemoveImage = findViewById(R.id.btnRemoveImage);
        btnAddImage = findViewById(R.id.btnAddImage);
        imageFrame = findViewById(R.id.imageFrame);
        btnRemoveImg = findViewById(R.id.btnRemoveImage);
        btnBack = findViewById(R.id.addFileBackButton);
        txtCaption = findViewById(R.id.editCaption);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("leftbtn");


                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(txtCaption.getText().toString().trim().equals("")){
                            Toast.makeText(AddPostActivity.this, "Add Text", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(isHateful!=-1 && currentText.equals(txtCaption.getText().toString().trim())){
                            overRule(isHateful);
                            return;
                        }

                        isHateful = 1;

                        currentText = txtCaption.getText().toString().trim();

                        showWarning();
                    }
                },2000);




            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("rightbtn");


                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(txtCaption.getText().toString().trim().equals("")){
                            Toast.makeText(AddPostActivity.this, "Add Text", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(isHateful!=-1 && currentText.equals(txtCaption.getText().toString().trim())){
                            overRule(isHateful);
                            return;
                        }

                        isHateful = 0;

                        addPost(false);

                    }
                },2000);

            }
        });
        
        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                startActivityForResult(intent,
                        0);
            }
        });


        btnRemoveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeImage();
            }
        });


    }

    private void overRule(int isHateful) {
        if(isHateful==1){
            showWarning();
            return;
        }
        if(isHateful==0){
            addPost(false);
        }
    }

    private void showWarning() {

        new AlertDialog.Builder(AddPostActivity.this)
                .setIcon(R.drawable.ic_round_warning_24)
                .setTitle("In-Appropriate content")
                .setMessage("the written content may contain hate-speech\ndo you wish to continue?")
                .setPositiveButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addPost(true);

                        dialog.dismiss();
                    }
                }).create().show();

    }

    private void addPost(boolean hatered) {
        Post post = new Post(txtCaption.getText().toString(),this);
        post.setHateful(hatered);

        int id = post.getId();

        if(bitmapImage!=null){
            File postImgFile = new File(
                    this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                            .getPath() + "/" + id);

            try {
                OutputStream outputStream = new FileOutputStream(postImgFile);
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        Utils.addToAllPosts(post,this);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


//        TODO : for multiple Imageset
        if (resultCode == RESULT_OK && (requestCode == 0)) {
            imagePath = data.getData();
            showImage();
            imageFrame.setVisibility(View.VISIBLE);

//            new Classifier(this).censorOCRText();
            InputStream inputStream = null;
            try {
                inputStream = this.getContentResolver().openInputStream(imagePath);
                bitmapImage = BitmapFactory.decodeStream(inputStream);

                new Classifier(this).censorOCRText(bitmapImage, new Classifier.OCRListner() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        bitmapImage = bitmap;
                        showImage();
                    }

                    @Override
                    public void onFailure(Bitmap bitmap) {

                    }
                });

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }



            }
        }

    private void showImage() {
        Glide.with(this)
                .asBitmap()
                .load(bitmapImage)
                .into(previewImg);

        imageFrame.setVisibility(View.VISIBLE);
        btnAddImage.setVisibility(View.GONE);

    }

    private void removeImage(){
        imagePath = null;
        bitmapImage = null;
        imageFrame.setVisibility(View.GONE);
        btnAddImage.setVisibility(View.VISIBLE);

    }
}

