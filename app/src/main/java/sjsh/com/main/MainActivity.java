package sjsh.com.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import sjsh.com.util.LoadingDialog;
import sjsh.com.R;


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
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                }
                else{
                    dialogControl("닉네임을 입력해주세요.");
                }
            }
        });


        lookAd = findViewById(R.id.lookAd);
        lookAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdRequest adRequest = new AdRequest.Builder().build(); //ca-app-pub-6713667729006874/4863026514
                InterstitialAd.load(getApplicationContext(), "ca-app-pub-6713667729006874/4863026514", adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        // Handle the error
                        Log.i("csh", loadAdError.getMessage() + " :(");
                        mInterstitialAd = null;
                    }
                });

                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                    dialogControl("감사합니다 :)");
                }
                else {
                    dialogControl("시청 가능한 광고가 없습니다 :(");
                }
            }
        });

        payment = findViewById(R.id.payment);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogControl("마음만 받을게요 :)\n감사합니다.");
            }
        });
    }

    private void dialogControl(String msg) {
        loadingDialog.startLoadingDialog();
        loadingDialog.onCancelable();
        loadingDialog.offProgressBar();
        loadingDialog.setMessage(msg);
        loadingDialog.onButtonForStay();
    }

}