package sjsh.com.graph;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import sjsh.com.util.LoadingDialog;
import sjsh.com.R;

public class FragmentGraph extends Fragment {

    private View view;

    private PieChart pieChart;
    int[] colorArray = new int[] { Color.parseColor("#E6415A"), Color.parseColor("#5587E6")};

    int win = 0;
    int lose = 0;
    int division = 0;
    RecyclerView recyclerView;

    GraphRecyclerAdapter adapter;

    boolean isError = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_graph, container, false);

        recyclerView = view.findViewById(R.id.recyclerView2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new GraphRecyclerAdapter();
        recyclerView.setAdapter(adapter);

        getGraph();

        return view;
    }

    private ArrayList<PieEntry> data(){
        ArrayList<PieEntry> dataValue= new ArrayList<>();

        dataValue.add(new PieEntry(lose*division, ""));
        dataValue.add(new PieEntry(win*division, ""));
        return dataValue;
    }

    private void getGraph() {
        Graph AsyncTask = new Graph();
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

    private class Graph extends AsyncTask<Void, Void, Void> {

        Document doc;

        TextView tv_entirely;
        String entirely;

        TextView rankOf7days;

        TextView tv_kill;
        String kill = "0";

        TextView tv_death;
        String death = "0";

        TextView tv_assist;
        String assist = "0";

        TextView tv_avgKDA;
        String avgKDA= "0.0";

        TextView tv_avgPercent;
        String avgPercent= "0%";

        String WorL = "";

        ArrayList<Integer> win_percent = new ArrayList<>();
        ArrayList<String> win_cnt = new ArrayList<>();
        ArrayList<String> lose_cnt = new ArrayList<>();

        ArrayList<String> champion_url = new ArrayList<>();

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String data = getArguments().getString("data");
                doc = Jsoup.parse(data);

                final Element Entirely_infoE = doc.selectFirst("table.GameAverageStats");
                if(Entirely_infoE == null)
                    return null;

                win = Integer.parseInt(Entirely_infoE.child(0).child(0).child(0).child(0).child(1).text());
                lose = Integer.parseInt(Entirely_infoE.child(0).child(0).child(0).child(0).child(2).text());
                division = 100/(win+lose);

                kill = Entirely_infoE.child(0).child(1).child(1).child(0).child(0).text();
                death = Entirely_infoE.child(0).child(1).child(1).child(0).child(1).text();
                assist = Entirely_infoE.child(0).child(1).child(1).child(0).child(2).text();

                avgKDA = "( " + Entirely_infoE.child(0).child(1).child(1).child(1).child(0).text().split(":")[0] + " )";
                avgPercent = "킬관여 " + Entirely_infoE.child(0).child(1).child(1).child(1).child(1).child(0).text();


                final Elements contentE = doc.select("div.ChampionWinRatioBox");

                for(Element content : contentE){
                    String champion = content.child(1).child(0).attr("title")
                            .replace(" ", "")
                            .replace("'","");

                    //챔피언이 누누일 경우는 특히함
                    if(champion.contains("&"))
                        champion = "Nunu";

                    champion_url.add("https://opgg-static.akamaized.net/images/lol/champion/" + champion + ".png?image=q_auto,w_46&v=1591083841");
                    win_percent.add(Integer.parseInt(content.child(2).text().replace("%","")));
                    if(content.child(3).child(0).child(0).childrenSize() == 2){
                        WorL = content.child(3).child(0).child(0).child(1).text();
                        if(WorL.substring(WorL.length()-1).equals("W")){
                            win_cnt.add(WorL);
                            lose_cnt.add("0L");
                        }
                        else{
                            win_cnt.add("0W");
                            lose_cnt.add(WorL);
                        }

                    }
                    else {
                        win_cnt.add(content.child(3).child(0).child(0).child(1).text());
                        lose_cnt.add(content.child(3).child(0).child(0).child(3).text());
                    }
                }


                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < champion_url.size(); i++) {
                            GraphData data = new GraphData();

                            data.setWin_percent(win_percent.get(i));
                            data.setWin_cnt(win_cnt.get(i));
                            data.setLose_cnt(lose_cnt.get(i));

                            data.setChampion_url(champion_url.get(i));

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
            super.onPostExecute(aVoid);

            checkError();

            tv_entirely = view.findViewById(R.id.tv_Entirely);
            entirely = win + lose + "전 " + win + "승 " + lose + "패";
            tv_entirely.setText(entirely);

            pieChart = view.findViewById(R.id.pieChart);
            PieDataSet pieDataSet = new PieDataSet(data(),"");
            pieDataSet.setColors(colorArray);
            PieData pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);
            pieChart.getDescription().setEnabled(false);
            pieChart.getLegend().setEnabled(false);
            pieChart.setCenterText(win*5 + "%");
            pieChart.getData().setDrawValues(false);
            pieChart.invalidate();

            tv_kill = view.findViewById(R.id.tv_kill);
            tv_death = view.findViewById(R.id.tv_death);
            tv_assist = view.findViewById(R.id.tv_assist);

            tv_kill.setText(kill);
            tv_death.setText(death);
            tv_assist.setText(assist);

            tv_avgKDA = view.findViewById(R.id.tv_avgKDA);
            tv_avgPercent = view.findViewById(R.id.tv_avgPercent);

            tv_avgKDA.setText(avgKDA);
            tv_avgPercent.setText(avgPercent);

            rankOf7days = view.findViewById(R.id.rankOf7days);
            if(champion_url.size() != 0){
                rankOf7days.setText("최근 7일간 랭크 승률");
                rankOf7days.setEnabled(true);
            }

            else
                rankOf7days.setText("랭크 플레이 기록 없음");
        }
    }
}
