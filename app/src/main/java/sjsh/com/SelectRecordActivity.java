package sjsh.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

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

        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        recyclerView = findViewById(R.id.select_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecordRecyclerAdapter(dm.widthPixels);
        recyclerView.setAdapter(adapter);
        Intent intent = getIntent();
        adapter.setListData((ArrayList<RecordData>)intent.getSerializableExtra("data"));
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
                Intent intent = new Intent(this, ScreenshotRecordActivity.class);
                intent.putExtra("data",adapter.getSelectedListData());
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}