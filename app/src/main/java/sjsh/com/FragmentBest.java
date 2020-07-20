package sjsh.com;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FragmentBest extends Fragment {

    private View view;

    FrameLayout adContainerView;
    AdView adView;

    boolean isBestChampion = false;
    boolean isError = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_best, container, false);

        //애드몹
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        adContainerView = view.findViewById(R.id.ad_view_container);
        adView = new AdView(getContext());
        adView.setAdUnitId("ca-app-pub-6713667729006874/8545645874"); // //ca-app-pub-3940256099942544/6300978111
        adContainerView.addView(adView);
        loadBanner();

        getBest();

        return view;
    }

    private void loadBanner() {
        List<String> testDevices = new ArrayList<>();
        testDevices.add(AdRequest.DEVICE_ID_EMULATOR);

        RequestConfiguration requestConfiguration
                = new RequestConfiguration.Builder()
                .setTestDeviceIds(testDevices)
                .build();
        MobileAds.setRequestConfiguration(requestConfiguration);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.loadAd(new AdRequest.Builder().build());
    }

    private void getBest() {
        Best AsyncTask = new Best();
        AsyncTask.execute();
    }

    void checkError(){
        if(isError){
            getFragmentManager().beginTransaction().remove(this).commit();
            LoadingDialog loadingDialog = new LoadingDialog(getActivity());
            loadingDialog.startLoadingDialog();
            loadingDialog.onCancelable();
            loadingDialog.offProgressBar();
            loadingDialog.setMessage("죄송합니다. 오류입니다 :(\n 반복될 경우 메인화면 하단에 이메일로 문의 주세요!!");
            loadingDialog.onButtonForStay();
        }
    }

    private class Best extends AsyncTask<Void, Void, Void>{

        Document doc;

        ImageView bestChampion;
        String bestChampion_url;

        TextView avgScore;
        String avgScoreText;

        TextView avgKDA;
        String avgKDAText;

        TextView avgLevel;
        String avgLevelText;

        TextView avgPercent;
        String avgPercentText;

        TextView avgMinion;
        String avgMinionText;

        TextView avgPinkWard;
        String avgPinkWardText;

        TextView textInput;

        int bestIndex = 20;

        double sumKill = 0;
        double sumDeath = 0;
        double sumAssist = 0;
        double sumKDA = 0;

        int sumLevel = 0;
        int sumMinion = 0;
        int sumPercent = 0;

        double sumPinkWard = 0;

        ArrayList<String> champion = new ArrayList<>();
        ArrayList<Integer> matchCount = new ArrayList<>();
        ArrayList<Integer> score = new ArrayList<>();

        boolean isChampion(String championName){
            for (String name : champion) {
                if(name.equals(championName))
                    return true;
            }
            return false;
        }

        int getChampionIndex(String championName) {
            int cnt = 0;
            for (String name : champion) {
                if (name.equals(championName)) {
                    return cnt;
                }
                cnt++;
            }
            return cnt;
        }

        int getScore(int k, int d, int a, int kda_p, int pw ,String bg) {
            int aScore = 0; // kda
            double bScore = 0; // k_p
            int cScore = 0; // pw
            int addPoint = 0; // mvp , ace
            int result = 0; // last

            if (d == 0)
                aScore = 40;
            else
                aScore = 4*((k+a)/d);

            if (aScore > 40)
                aScore = 40;


            if (kda_p>=100)
                bScore = 70;
            else
                bScore = kda_p/1.42;


            if (pw>=5)
                cScore = 40;
            else
                cScore = pw * 8;

            if (cScore > 40)
                cScore = 40;


            if (bg.equals("MVP"))
                addPoint = 20;
            else if(bg.equals("ACE"))
                addPoint = 15;


            if (aScore + bScore + cScore + addPoint >= 100)
                result = 100;
            else
                result = aScore + (int)bScore + cScore + addPoint;

            return result;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String data = getArguments().getString("data");
                doc = Jsoup.parse(data);

                final Elements infoE = doc.select("div.GameItemList div.GameItemWrap div.GameItem");

                for (Element info : infoE) {
                    if (!info.child(0).child(0).child(0).text().equals("Ranked Solo"))
                        continue;

                    String championName = info.child(0).child(1).child(3).child(0).text();
                    int kill = Integer.parseInt(info.child(0).child(2).child(0).child(0).text());
                    int death = Integer.parseInt(info.child(0).child(2).child(0).child(1).text());
                    int assist = Integer.parseInt(info.child(0).child(2).child(0).child(2).text());
                    int kda_percent = Integer.parseInt(info.child(0).child(3).child(2).text().split(" ")[1].replace("%", ""));
                    int pinkWard = 0;
                    if (info.child(0).child(4).childrenSize() == 2){
                        pinkWard = Integer.parseInt(info.child(0).child(4).child(1).child(1).text());
                    }

                    String badge = "";

                    if (!isChampion(championName)) {
                        champion.add(info.child(0).child(1).child(3).child(0).text());
                        if(info.child(0).child(2).childrenSize()>2) {
                            int size = info.child(0).child(2).childrenSize();
                            badge = info.child(0).child(2).child(size-1).child(0).text();
                        }
                        score.add(getScore(kill, death, assist, kda_percent, pinkWard, badge));
                        matchCount.add(1);
                    }
                    else {
                        int currentIndex = getChampionIndex(championName);
                        score.set(currentIndex, (score.get(currentIndex) + getScore(kill, death, assist, kda_percent, pinkWard, badge))/2);
                        matchCount.set(currentIndex, matchCount.get(currentIndex)+1);
                    }
                }

                int bestScore = 0;
                for (int index=0; index<champion.size(); index++){
                    if (bestScore < score.get(index)) {
                        if(matchCount.get(index)>=2) {
                            bestScore = score.get(index);
                            bestIndex = index;
                        }
                    }
                }

                if(bestIndex != 20) {
                    isBestChampion = true;
                    bestChampion_url = "https://opgg-static.akamaized.net/images/lol/champion/" + champion.get(bestIndex)
                            .replace(" ", "")
                            .replace("'", "") + ".png?image=q_auto,w_46&v=1591083841";

                    avgScoreText = score.get(bestIndex) + " 점";

                    for(Element info : infoE){
                        if (info.child(0).child(1).child(3).child(0).text().equals(champion.get(bestIndex))) {
                            sumKill += Integer.parseInt(info.child(0).child(2).child(0).child(0).text());
                            sumDeath += Integer.parseInt(info.child(0).child(2).child(0).child(1).text());
                            sumAssist += Integer.parseInt(info.child(0).child(2).child(0).child(2).text());

                            sumLevel += Integer.parseInt(info.child(0).child(3).child(0).text().replace("Level",""));
                            sumMinion += Integer.parseInt(info.child(0).child(3).child(1).child(0).text().split(" ")[0]);
                            sumPercent += Integer.parseInt(info.child(0).child(3).child(2).text().split(" ")[1].replace("%",""));
                            if(info.child(0).child(4).childrenSize() == 2)
                                sumPinkWard += Integer.parseInt(info.child(0).child(4).child(1).child(1).text());
                        }
                    }

                    if(sumDeath == 0)
                        avgKDAText = "Perfect!";
                    else
                        avgKDAText = Math.round(((sumKill+sumAssist)/sumDeath)*10)/10.0 + "";
                    avgLevelText = sumLevel/matchCount.get(bestIndex) + "";
                    avgMinionText = sumMinion/matchCount.get(bestIndex) + " CS";
                    avgPercentText = sumPercent/matchCount.get(bestIndex) + "%";
                    avgPinkWardText = Math.round(sumPinkWard/matchCount.get(bestIndex)*10)/10.0 + " 개";
                }
            } catch (Exception e) {
                e.printStackTrace();
                isError = true;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            checkError();

            if(isBestChampion) {
                bestChampion = view.findViewById(R.id.bestChampion);
                bestChampion.setBackgroundResource(0);
                Glide.with(getActivity()).load(bestChampion_url).into(bestChampion);

                textInput = view.findViewById(R.id.textInput);
                String bestComment = matchCount.get(bestIndex) + "판 플레이 평균";
                textInput.setText(bestComment);

                avgScore = view.findViewById(R.id.avgScore);
                avgKDA = view.findViewById(R.id.avgKDA);
                avgLevel = view.findViewById(R.id.avgLevel);
                avgPercent = view.findViewById(R.id.avgPercent);
                avgMinion = view.findViewById(R.id.avgMinion);
                avgPinkWard = view.findViewById(R.id.avgPinkWard);

                avgScore.setText(avgScoreText);
                avgKDA.setText(avgKDAText);
                avgLevel.setText(avgLevelText);
                avgMinion.setText(avgMinionText);
                avgPercent.setText(avgPercentText);
                avgPinkWard.setText(avgPinkWardText);
            }
            else{
                ConstraintLayout block2 = view.findViewById(R.id.block2);
                ConstraintLayout block3 = view.findViewById(R.id.block3);
                ConstraintLayout block4 = view.findViewById(R.id.block4);

                block2.setVisibility(View.GONE);
                block3.setVisibility(View.GONE);
                block4.setVisibility(View.GONE);

                textInput = view.findViewById(R.id.textInput);
                textInput.setText("\n베스트 챔피언이 없습니다.\n( 랭크게임 2판 이상 플레이한 챔피언 )");
            }
            super.onPostExecute(aVoid);
        }
    }
}
