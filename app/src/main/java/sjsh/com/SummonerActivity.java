package sjsh.com;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class SummonerActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    FragmentRecord fragmentRecord;
    FragmentGraph fragmentGraph;
    FragmentBest fragmentBest;

    TextView textView;

    String url;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summoner);

        Toolbar toolbar = findViewById(R.id.toolBar);
        //toolbar.setBackgroundColor(Color.parseColor("#FFE6DFDF"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        textView = findViewById(R.id.title);

        Intent intent = getIntent();
        url = intent.getStringExtra("url");

        getConnect();

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.summoner_record:
                        setFrag(0);
                        break;
                    case R.id.summoner_graph:
                        setFrag(1);
                        break;
                    case R.id.summoner_best:
                        setFrag(2);
                        break;
                }
                return true;
            }
        });

        fragmentRecord = new FragmentRecord();
        fragmentGraph = new FragmentGraph();
        fragmentBest = new FragmentBest();
    }

    private void setFrag(int n) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (n) {
            case 0:
                fragmentTransaction.replace(R.id.frameLayout, fragmentRecord);
                fragmentTransaction.commit();
                textView.setText("대전 기록");
                break;
            case 1:
                fragmentTransaction.replace(R.id.frameLayout, fragmentGraph);
                fragmentTransaction.commit();
                textView.setText("그래프 정보");
                break;
            case 2:
                fragmentTransaction.replace(R.id.frameLayout, fragmentBest);
                fragmentTransaction.commit();
                textView.setText("챔피언 추천");
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void getConnect() {
        Connect AsyncTask = new Connect();
        AsyncTask.execute();
    }

    private class Connect extends AsyncTask<Void, Void, Void> {

        LoadingDialog loadingDialog;

        String message = " 분 석 중 . . . ( 2초 가량 소요 ) ";
        Document doc;

        @Override
        protected void onPreExecute() {
            loadingDialog = new LoadingDialog(SummonerActivity.this);
            loadingDialog.startLoadingDialog();
            loadingDialog.setMessage(message);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(message.equals("success")) {
                Bundle bundle = new Bundle();
                bundle.putString("data", doc.toString());
                fragmentRecord.setArguments(bundle);
                fragmentGraph.setArguments(bundle);
                fragmentBest.setArguments(bundle);
                loadingDialog.dismissDialog();
                setFrag(0);
            }
            else{
                loadingDialog.onCancelable();
                loadingDialog.offProgressBar();
                loadingDialog.setMessage(message);
                loadingDialog.onButtonForFinish();
            }
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                doc = Jsoup.connect(url).get();
            } catch (Exception e){
                System.out.println("csh SummonerActivity Error : " + e.toString());
            }

            Element isSummoner = doc.selectFirst("div.SummonerLayout");
            if(isSummoner != null){
                message = "success";
            }
            else {
                Element isServerCheck = doc.selectFirst("div.SummonerNotFoundLayout");
                if(isServerCheck != null){
                    message = "등록되지 않은 소환사입니다.";
                }
                else{
                    message = "서버 점검중입니다.";
                }
            }

            return null;
        }
    }

}