package sjsh.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SelectRecordActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecordRecyclerAdapter adapter;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_record);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        textView = findViewById(R.id.title);
        textView.setText("전적 선택하기");

        recyclerView = findViewById(R.id.select_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecordRecyclerAdapter();
        recyclerView.setAdapter(adapter);

        ArrayList<RecordData> listData = new ArrayList<>();
        ExtendedDataHolder extras = ExtendedDataHolder.getInstance();
        if (extras.hasExtra("list")) {
            listData = (ArrayList<RecordData>)extras.getExtra("list");
        }

        adapter.setListData(listData);
        adapter.notifyDataSetChanged();
        adapter.setScreenShot(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.screenshot_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.screenshot_save:
                checkSelfPermission();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void checkSelfPermission(){
       String temp = " ";

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }

        if (TextUtils.isEmpty(temp) == false){
            // 권한 요청
            ActivityCompat.requestPermissions(this, temp.trim().split(" "), 1);
        }else {
            // 모두 허용 상태
            Toast.makeText(this, "권한 허용", Toast.LENGTH_SHORT).show();
        }

    }

    public void getScreenshot(){
        ExtendedDataHolder extras = ExtendedDataHolder.getInstance();
        extras.clear();
        extras.putExtra("list", adapter.getSelectedListData());
        Intent intent = new Intent(this, ScreenshotRecordActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 권한 허용한 경우
        if (requestCode == 1){
            getScreenshot();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}