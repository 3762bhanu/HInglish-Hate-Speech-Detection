package mark.andrew.hinglishhatespeech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.function.BiConsumer;


public class ImageDisplayActivity extends AppCompatActivity {

    public static Bitmap displayImageBitmap;
    ImageView btnShare,displayImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        displayImage = findViewById(R.id.displayImage);
        btnShare = findViewById(R.id.btnShare);

        displayImage.setImageBitmap(displayImageBitmap);
        
        btnShare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(ImageDisplayActivity.this,getString(R.string.please_wait),Toast.LENGTH_SHORT).show();
                Intent shareIntent;

                Bitmap bitmap = displayImageBitmap;
                String path = ImageDisplayActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/Share.png";
                OutputStream out = null;
                File file=new File(path);
                try {
                    out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                path=file.getPath();
                Uri bmpUri = Uri.parse("file://"+path);

                bmpUri = FileProvider.getUriForFile(ImageDisplayActivity.this, ImageDisplayActivity.this.getApplicationContext().getPackageName() + ".provider", file);

                shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                shareIntent.putExtra(Intent.EXTRA_TEXT,"Censored by HinglishHateSpeechDetection system");
                shareIntent.setType("image/png");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent,"Share with"));

            }
        });


    }
}