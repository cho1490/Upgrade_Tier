package sjsh.com;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {

    String url = "";
    String firstUrl = "https://www.op.gg/summoner/userName=";
    EditText et_nickname;
    Button btn_search;
    Button lookAd;
    Button payment;

    private InterstitialAd mInterstitialAd;

    LoadingDialog loadingDialog = new LoadingDialog(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        mInterstitialAd = new InterstitialAd(MainActivity.this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6713667729006874/4863026514");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int i) {
                String errorMsg = "";
                loadingDialog.startLoadingDialog();
                loadingDialog.onCancelable();
                loadingDialog.offProgressBar();
                if(AdRequest.ERROR_CODE_INTERNAL_ERROR == i){
                    errorMsg = "서버에 연결 할 수 없습니다 :(";
                }else if(AdRequest.ERROR_CODE_INVALID_REQUEST == i){
                    errorMsg = "올바르지 않은 광고 입니다 :(";
                }else if(AdRequest.ERROR_CODE_NETWORK_ERROR  == i){
                    errorMsg = "네트워크에 연결할 수 없습니다 :(";
                }else if(AdRequest.ERROR_CODE_NO_FILL == i){
                    errorMsg = "시청 가능한 광고가 없습니다 :(";
                }else{
                    errorMsg = "오류 :(";
                }
                loadingDialog.setMessage(errorMsg);
                loadingDialog.onButtonForStay();
                super.onAdFailedToLoad(i);
            }
            @Override
            public void onAdClosed() {
                loadingDialog.startLoadingDialog();
                loadingDialog.onCancelable();
                loadingDialog.offProgressBar();
                loadingDialog.setMessage("감사합니다. :)");
                loadingDialog.onButtonForStay();
                super.onAdClosed();
            }
        });


        btn_search = findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_nickname = findViewById(R.id.et_nickname);
                if(!et_nickname.getText().toString().equals("")){
                    url = firstUrl + et_nickname.getText().toString().replace("\n", "");
                    Intent intent = new Intent(getApplicationContext(), SummonerActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
                else{
                    loadingDialog.startLoadingDialog();
                    loadingDialog.onCancelable();
                    loadingDialog.offProgressBar();
                    loadingDialog.setMessage("닉네임을 입력해주세요.");
                    loadingDialog.onButtonForStay();
                }
            }
        });

        lookAd = findViewById(R.id.lookAd);
        lookAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    System.out.println("csh");
                }
            }
        });

        payment = findViewById(R.id.payment);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                loadingDialog.onCancelable();
                loadingDialog.offProgressBar();
                loadingDialog.setMessage("마음만 받을게요 :)\n감사합니다.");
                loadingDialog.onButtonForStay();
            }
        });
    }
}