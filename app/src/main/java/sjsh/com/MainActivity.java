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
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6713667729006874/8545645874"); // //ca-app-pub-3940256099942544/1033173712
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){
            /*@Override
            public void onAdFailedToLoad(int i) {
                String errorMsg = "?";
                loadingDialog.startLoadingDialog();
                loadingDialog.onCancelable();
                loadingDialog.offProgressBar();
                if(AdRequest.ERROR_CODE_INTERNAL_ERROR == i){
                    errorMsg = "Something happened internally; for instance, an invalid response was received from the ad server.";
                }else if(AdRequest.ERROR_CODE_INVALID_REQUEST == i){
                    errorMsg = "The ad request was invalid; for instance, the ad unit ID was incorrect.";
                }else if(AdRequest.ERROR_CODE_NETWORK_ERROR  == i){
                    errorMsg = "The ad request was unsuccessful due to network connectivity.";
                }else if(AdRequest.ERROR_CODE_NO_FILL == i){
                    errorMsg = "The ad request was successful, but no ad was returned due to lack of ad inventory.";
                }else{
                    errorMsg = "오류...";
                }
                loadingDialog.setMessage(errorMsg);
                loadingDialog.onButtonForStay();
                super.onAdFailedToLoad(i);
            }*/

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
                url = firstUrl + et_nickname.getText().toString().replace("\n", "");
                Intent intent = new Intent(getApplicationContext(), SummonerActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
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