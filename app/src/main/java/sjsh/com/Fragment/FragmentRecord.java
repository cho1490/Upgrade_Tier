package sjsh.com.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import sjsh.com.ExtendedDataHolder;
import sjsh.com.LoadingDialog;
import sjsh.com.R;
import sjsh.com.Model.RecordData;
import sjsh.com.RecordRecyclerAdapter;
import sjsh.com.Activity.SelectRecordActivity;

public class FragmentRecord extends Fragment {

    private View view;

    LinearLayout top_block;

    Button btn_recordScreenshot;

    RecyclerView recyclerView;
    RecordRecyclerAdapter adapter;

    ImageView iv_icon;
    String icon_url;

    ImageView iv_soloRank_tier;
    String soloRank_tier_url;

    TextView tv_soloRank_tier;
    String soloRank_tier_text;

    ImageView iv_freeRank_tier;
    String freeRank_tier_url;

    TextView tv_freeRank_tier;
    String freeRank_tier_text;

    TextView tv_nickname;
    String nickName;

    TextView tv_level;
    String level;

    boolean isError = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_record, container, false);

        DisplayMetrics dm;
        top_block = view.findViewById(R.id.top_block);
        //가로 세로 비율 맞추기
        dm = getActivity().getResources().getDisplayMetrics();
        top_block.getLayoutParams().height = dm.widthPixels/6;

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecordRecyclerAdapter();
        recyclerView.setAdapter(adapter);

        getRecord();

        btn_recordScreenshot = view.findViewById(R.id.btn_recordScreenshot);
        btn_recordScreenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExtendedDataHolder extras = ExtendedDataHolder.getInstance();
                extras.putExtra("list", adapter.getListData());
                Intent intent = new Intent(getActivity(), SelectRecordActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void getRecord() {
        Record AsyncTask = new Record();
        AsyncTask.execute();
    }

    void checkError(){
        if(isError){
            getFragmentManager().beginTransaction().remove(this).commit();
            LoadingDialog loadingDialog = new LoadingDialog(getActivity());
            loadingDialog.startLoadingDialog();
            loadingDialog.onCancelable();
            loadingDialog.offProgressBar();
            loadingDialog.setMessage("죄송합니다. 오류입니다 :(\n반복될 경우 메인화면 하단\n이메일로 문의 주세요!!");
            loadingDialog.onButtonForStay();
        }
    }

    private class Record extends AsyncTask<Void, Void, Void> {

        Document doc;

        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> time = new ArrayList<>();
        ArrayList<String> kill = new ArrayList<>();
        ArrayList<String> death = new ArrayList<>();
        ArrayList<String> assist = new ArrayList<>();
        ArrayList<String> kda_percent = new ArrayList<>();
        ArrayList<String> game_mode = new ArrayList<>();
        ArrayList<String> game_ago = new ArrayList<>();
        ArrayList<String> badge = new ArrayList<>();

        ArrayList<String> champion_url = new ArrayList<>();
        ArrayList<String> spell_url = new ArrayList<>();
        ArrayList<String> rune_url = new ArrayList<>();
        ArrayList<String> item_url = new ArrayList<>();

        private String getGame_mode(String game_mode){
            String gameName = "?";

            if(game_mode.equals("Normal"))
                gameName = "일반";
            else if(game_mode.equals("Ranked Solo"))
                gameName = "솔로 랭크";
            else if(game_mode.equals("Flex 5:5 Rank"))
                gameName = "자유 5:5 랭크";
            else if(game_mode.equals("ARAM"))
                gameName = "무작위 총력전";
            else if(game_mode.equals("URF"))
                gameName = "우르프";
            else if(game_mode.equals("Bot"))
                gameName = "봇전";
            else if(game_mode.equals("Special Mode")){
                gameName = "돌격! 넥서스";
            }
            /*
            else{
                System.out.println("csh : " + game_mode);
            }*/

            return gameName;
        }

        private String getGame_ago(String game_ago){
            String ret = "?";

            Date date;
            Calendar c = Calendar.getInstance();

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = dateFormat.parse(game_ago);

                long now = c.getTimeInMillis();
                long dateM = date.getTime();
                long gap = now - dateM;

                //        초       분   시
                //        1000    60  60
                gap = (long) (gap / 1000);
                long hour = gap / 3600;
                gap = gap % 3600;
                long min = gap / 60;
                long sec = gap % 60;

                if(hour > 720){
                    ret = hour / 720 + "달 전";
                }else if (hour > 24) {
                    ret = hour / 24 + "일 전";
                } else if (hour > 0) {
                    ret = hour + "시간 전";
                } else if (min > 0) {
                    ret = min + "분 전";
                } else if (sec > 0) {
                    ret = sec + "초 전";
                } else {
                    ret = new SimpleDateFormat("HH:mm").format(date);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            return ret;
        }

        private String getBadge(String Badge){
            String badgeName = "?";

            //System.out.println("csh : "  + Badge);

            if(Badge.equals("Double Kill"))
                badgeName = "더블킬";
            else if(Badge.equals("Triple Kill"))
                badgeName = "트리플킬";
            else if(Badge.equals("Quadra Kill"))
                badgeName = "쿼드라킬";
            else if(Badge.equals("Penta Kill"))
                badgeName = "펜타킬";
            else
                badgeName = Badge;

            return badgeName;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            iv_icon = view.findViewById(R.id.iv_icon);
            iv_soloRank_tier = view.findViewById(R.id.soloRank_tier);
            iv_freeRank_tier = view.findViewById(R.id.freeRank_tier);
            tv_soloRank_tier = view.findViewById(R.id.tv_SoloRank_tier);
            tv_freeRank_tier = view.findViewById(R.id.tv_freeRank_tier);
            tv_level = view.findViewById(R.id.tv_level);
            tv_nickname = view.findViewById(R.id.tv_nickname);


            //한번에 출력
            Glide.with(getActivity()).load(icon_url).into(iv_icon);
            Glide.with(getActivity()).load(soloRank_tier_url).into(iv_soloRank_tier);
            Glide.with(getActivity()).load(freeRank_tier_url).into(iv_freeRank_tier);
            tv_soloRank_tier.setText(soloRank_tier_text);
            tv_freeRank_tier.setText(freeRank_tier_text);

            tv_level.setText(level);

            if(nickName.length() >= 14){
                tv_nickname.setTextSize(12);
            }else if(nickName.length() >= 12){
                tv_nickname.setTextSize(14);
            }else if(nickName.length() >= 10){
                tv_nickname.setTextSize(16);
            }
            tv_nickname.setText(nickName);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String data = getArguments().getString("data");
                doc = Jsoup.parse(data);

                final Element profile_info = doc.selectFirst("div.ProfileIcon");
                final Element summonerName = doc.selectFirst("div.Information");
                final Element soloRank_tierE = doc.selectFirst("div.SummonerRatingMedium");
                final Element freeRank_tierE = doc.selectFirst("div.sub-tier");

                icon_url = "https:" + profile_info.child(profile_info.childrenSize()-2).attr("src");

                soloRank_tier_url = "https:" + soloRank_tierE.child(0).child(0).attr("src");
                freeRank_tier_url = "https:" + freeRank_tierE.child(0).attr("src");
                soloRank_tier_text = soloRank_tierE.child(1).child(1).text();
                freeRank_tier_text = freeRank_tierE.child(1).child(1).text();


                level = profile_info.child(profile_info.childrenSize()-1).text();
                nickName = summonerName.child(summonerName.childrenSize()-3).text();

                //솔랭 티어 자유랭 티어 가져오기
                publishProgress(); // icon_url, level 불러오기

                final Elements game_infoE = doc.select("div.GameStats");
                final Elements kdaE = doc.select("div.KDA div.KDA");
                final Elements kda_percentE = doc.select("div.CKRate");
                final Elements game_status_urlE = doc.select("div.GameSettingInfo");
                final Elements item_urlE = doc.select("div.itemList div.item");

                for (Element game_info : game_infoE) {
                    game_mode.add(getGame_mode(game_info.child(0).text()));
                    game_ago.add(getGame_ago(game_info.child(1).child(0).text()));

                    if (game_info.child(3).text().equals("Victory"))
                        result.add("승");
                    else if (game_info.child(3).text().equals("Defeat"))
                        result.add("패");
                    else if (game_info.child(3).text().equals("Remake"))
                        result.add("리");



                    time.add(game_info.child(4).text().split(" ")[0].replace("m", "분"));
                }

                for (Element kda : kdaE) {
                    kill.add(kda.child(0).text());
                    death.add(kda.child(1).text());
                    assist.add(kda.child(2).text());
                    if(kda.parent().childrenSize() > 2)
                        badge.add(getBadge(kda.parent().child(kda.parent().childrenSize()-1).child(0).text()));
                    else
                        badge.add("");

                }

                for (Element percent : kda_percentE) {
                    kda_percent.add("킬관여 " + percent.text().split(" ")[1]);
                }


                for (Element game_status_url : game_status_urlE) {
                    champion_url.add("https:" + game_status_url.child(0).child(0).child(0).attr("src"));
                    spell_url.add("https:" + game_status_url.child(1).child(0).child(0).attr("src"));
                    spell_url.add("https:" + game_status_url.child(1).child(1).child(0).attr("src"));
                    rune_url.add("https:" + game_status_url.child(2).child(0).child(0).attr("src"));
                    rune_url.add("https:" + game_status_url.child(2).child(1).child(0).attr("src"));
                }

                for (Element item : item_urlE) {
                    if (item.child(0).hasAttr("src"))
                        item_url.add("https:" + item.child(0).attr("src"));
                    else
                        item_url.add("");
                }


                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < game_mode.size(); i++) {
                            RecordData data = new RecordData();
                            data.setGame_mode(game_mode.get(i));
                            data.setGame_ago(game_ago.get(i));
                            data.setResult(result.get(i));
                            data.setTime(time.get(i));
                            data.setKill(kill.get(i));
                            data.setDeath(death.get(i));
                            data.setAssist(assist.get(i));
                            data.setKda_percent(kda_percent.get(i));
                            data.setBadge(badge.get(i));

                            data.setChampion_url(champion_url.get(i));

                            data.setSpell1_url(spell_url.get(i * 2));
                            data.setSpell2_url(spell_url.get(i * 2 + 1));

                            data.setRune1_url(rune_url.get(i * 2));
                            data.setRune2_url(rune_url.get(i * 2 + 1));

                            data.setItem1_url(item_url.get(i * 7));
                            data.setItem2_url(item_url.get(i * 7 + 1));
                            data.setItem3_url(item_url.get(i * 7 + 2));
                            data.setItem7_url(item_url.get(i * 7 + 3));
                            data.setItem4_url(item_url.get(i * 7 + 4));
                            data.setItem5_url(item_url.get(i * 7 + 5));
                            data.setItem6_url(item_url.get(i * 7 + 6));

                            adapter.addItem(data);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                isError = true;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            checkError();
            super.onPostExecute(aVoid);
        }
    }
}
