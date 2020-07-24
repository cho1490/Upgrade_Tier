package sjsh.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ScreenshotRecordActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecordRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenshot_record);

        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        recyclerView = findViewById(R.id.screenshot_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecordRecyclerAdapter(dm.widthPixels);
        recyclerView.setAdapter(adapter);
        Intent intent = getIntent();
        adapter.setListData((ArrayList<RecordData>)intent.getSerializableExtra("data"));
        adapter.notifyDataSetChanged();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                File screenShot = ScreenShot(viewToBitmap(recyclerView)); //여기에 딜레이 후 시작할 작업들을 입력
                System.out.println("csh : " + Uri.fromFile(screenShot));

                if(screenShot!=null){
                    //갤러리에 추가
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(screenShot)));
                    finish();
                }
            }
        }, 1000);

    }

    public File ScreenShot(Bitmap bitmap){
        SimpleDateFormat format1 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Date time = new Date();
        String fileName = format1.format(time)+".JPG";

        File file = new File(Environment.getExternalStorageDirectory()+"/Pictures", fileName); // Pictures 폴더
        FileOutputStream os = null;
        try{
            os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, os); // 비트맵을 PNG 파일로 변환
            os.close();
        }catch (IOException e){
            System.out.println("csh screenshot : " + e.toString());
            return null;
        }

        return file;
    }


    public static Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawARGB(100, 250, 250, 250);

        if(view instanceof SurfaceView) {
            SurfaceView surfaceView = (SurfaceView) view;
            surfaceView.setZOrderOnTop(true);
            surfaceView.draw(canvas);
            surfaceView.setZOrderOnTop(false);
            return bitmap;
        }else{
            view.draw(canvas);
            return bitmap;
        }
    }

}
