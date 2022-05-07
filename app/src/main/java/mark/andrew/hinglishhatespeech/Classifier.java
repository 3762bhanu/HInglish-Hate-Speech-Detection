package mark.andrew.hinglishhatespeech;

import static mark.andrew.hinglishhatespeech.ImageDisplayActivity.displayImageBitmap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText;
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentTextRecognizer;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Classifier {
    private final Set<String> hateHashSet;
    private AppCompatActivity context;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Classifier(AppCompatActivity context) {
        this.context = context;
        String[] array = new String[]{"land","bhosdivala","bhosdiwala","badir", "badirchand", "bakland", "bhadva", "bhootnika", "chinaal", "chup", "chutia", "ghasti", "chutiya", "haraami", "haraam", "hijra", "hinjda", "jaanvar", "kutta", "kutiya", "khota", "auladheen", "jaat", "najayaz", "gandpaidaish", "saala", "kutti", "soover", "tatti", "potty", "bahenchod", "bahanchod", "bahencho", "bancho", "bahenke", "laude", "takke", "betichod", "bhaichod", "bhains", "jhalla", "jhant", "nabaal", "pissu", "kutte", "maadherchod", "madarchod", "padma", "raand", "jamai", "randwa", "randi", "bachachod", "bachichod", "soower", "bachchechod", "ullu", "pathe", "banda", "booblay", "booby", "buble", "babla", "bhonsriwala", "bhonsdiwala", "ched", "chut", "chod", "chodu", "chodra", "choochi", "chuchi", "gaandu", "gandu", "gaand", "lavda", "lawda", "lauda", "lund", "balchod", "lavander", "muth", "maacho", "mammey", "tatte", "toto", "toota", "backar", "bhandwe", "bhosadchod", "bhosad", "bumchod", "bum", "bur", "chatani", "cunt", "cuntmama", "chipkali", "pasine", "jhaat", "chodela", "bhagatchod", "chhola", "chudai", "chudaikhana", "chunni", "choot", "bhoot", "dhakkan", "bhajiye", "fateychu", "gandnatije", "lundtopi", "gaandu", "gaandfat", "gaandmasti", "makhanchudai", "gaandmarau", "gandu", "chaatu", "beej", "choosu", "fakeerchod", "lundoos", "shorba", "binbheja", "bhadwe", "parichod", "nirodh", "pucchi", "baajer", "choud", "bhosda", "sadi", "choos", "maka", "chinaal", "gadde", "joon", "chullugand", "doob", "khatmal", "gandkate", "bambu", "lassan", "danda", "keera", "keeda", "hazaarchu", "paidaishikeeda", "kali", "safaid", "poot", "behendi", "chus", "machudi", "chodoonga", "baapchu", "laltern", "suhaagchudai", "raatchuda", "kaalu", "neech", "chikna", "meetha", "beechka", "chooche", "patichod", "rundi", "makkhi", "biwichod", "chodhunga", "haathi", "kute", "jhanten", "kaat", "gandi", "gadha", "bimaar", "badboodar", "dum", "raandsaala", "phudi", "chute", "kussi", "khandanchod", "ghussa", "maarey", "chipkili", "unday", "budh", "chaarpai", "chodun", "chatri", "chode", "chodho", "mullekatue", "mullikatui", "mullekebaal", "momedankatue", "katua", "chutiyapa", "bc", "mc", "chudwaya", "kutton", "jungli", "vahiyaat", "jihadi", "atankvadi", "atankwadi", "aatanki"};

        this.hateHashSet = Arrays.stream(array).collect(
                Collectors.toSet());
    }

    public void predict(String text) {

        String[] words = text.split(" ");

        for(String word:words){
            if(hateHashSet.contains(word)){
                Toast.makeText(this.context, "Hateful", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(this.context, "Neutral", Toast.LENGTH_SHORT).show();
        return;
    }

    public void predictOCRText(Bitmap bitmap,OCRListner ocrListner) {


        FirebaseVisionDocumentTextRecognizer detector = FirebaseVision.getInstance()
                .getCloudDocumentTextRecognizer();

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        detector.processImage(image)
                .addOnSuccessListener(
                        new OnSuccessListener<FirebaseVisionDocumentText>() {
                            @Override
                            public void onSuccess(FirebaseVisionDocumentText firebaseVisionText) {
                                for (FirebaseVisionDocumentText.Block block : firebaseVisionText.getBlocks()) {
                                    for(FirebaseVisionDocumentText.Paragraph para:block.getParagraphs()){
                                        for(FirebaseVisionDocumentText.Word word:para.getWords()){
                                            System.out.println(word.getText());
                                            if(hateHashSet.contains(word.getText().toLowerCase().trim())){
                                                Toast.makeText(context, "Hateful", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                    }
                                }
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "can't connect to server", Toast.LENGTH_SHORT).show();
                            }
                        });

    }

    public void censorOCRText(Bitmap bitmap,OCRListner ocrListner) {
        if(bitmap.isMutable()==false){
            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        }

        FirebaseVisionDocumentTextRecognizer detector = FirebaseVision.getInstance()
                .getCloudDocumentTextRecognizer();

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        Bitmap finalBitmap = bitmap;
        Bitmap finalBitmap1 = bitmap;
        detector.processImage(image)
                .addOnSuccessListener(
                        new OnSuccessListener<FirebaseVisionDocumentText>() {
                            @Override
                            public void onSuccess(FirebaseVisionDocumentText firebaseVisionText) {
                                Canvas cnvs=new Canvas(finalBitmap);
                                Paint paint=new Paint();
                                paint.setColor(Color.BLACK);
                                for (FirebaseVisionDocumentText.Block block : firebaseVisionText.getBlocks()) {
                                    for(FirebaseVisionDocumentText.Paragraph para:block.getParagraphs()){
                                        for(FirebaseVisionDocumentText.Word word:para.getWords()){
                                            if(hateHashSet.contains(word.getText().toLowerCase().trim())){
                                                Rect box = word.getBoundingBox();
                                                cnvs.drawRect(box.left, box.top,box.right,box.bottom , paint);


                                            }

                                        }

                                    }
                                }


                                ocrListner.onSuccess(finalBitmap);
//                                displayImageBitmap = bitmap;
//                                Intent intent = new Intent(context,ImageDisplayActivity.class);
//
//                                context.startActivity(intent);

                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "can't connect to server", Toast.LENGTH_SHORT).show();
                                ocrListner.onFailure(finalBitmap1);
                            }
                        });

    }

    public interface  OCRListner {
        public  void onSuccess(Bitmap bitmap);
        public  void onFailure(Bitmap bitmap);

    }
}
