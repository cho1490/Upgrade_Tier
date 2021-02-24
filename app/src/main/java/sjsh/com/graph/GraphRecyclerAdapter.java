package sjsh.com.graph;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import sjsh.com.R;

public class GraphRecyclerAdapter extends RecyclerView.Adapter<GraphRecyclerAdapter.ItemViewHolder> {

    private ArrayList<GraphData> listData = new ArrayList<>();


    @NonNull
    @Override
    public GraphRecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bestrecyclerview_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    public void addItem(GraphData data) {
        listData.add(data);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView win_percent, win_cnt, lose_cnt;
        private ImageView championUrl;

        String win_percent_txt;
        String win_txt;
        String lose_txt;

        ConstraintLayout.LayoutParams win_layoutParams =
                new ConstraintLayout.LayoutParams(
                        0, ConstraintLayout.LayoutParams.WRAP_CONTENT
                );

        ConstraintLayout.LayoutParams lose_layoutParams =
                new ConstraintLayout.LayoutParams(
                        0, ConstraintLayout.LayoutParams.WRAP_CONTENT
                );

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            win_percent = itemView.findViewById(R.id.tv_win_percent);
            win_cnt = itemView.findViewById(R.id.tv_win_cnt);
            lose_cnt = itemView.findViewById(R.id.tv_lose_cnt);

            championUrl = itemView.findViewById(R.id.iv_champion2);


            win_layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            win_layoutParams.endToStart = R.id.tv_lose_cnt;
            win_layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;

            lose_layoutParams.startToEnd = R.id.tv_win_cnt;
            lose_layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            lose_layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;

        }

        void onBind(GraphData data) {
            win_percent_txt = data.getWin_percent() + "%";
            win_percent.setText(win_percent_txt);

            win_layoutParams.horizontalWeight = data.getWin_percent();
            win_cnt.setLayoutParams(win_layoutParams);
            if(!data.getWin_cnt().equals("0W")){
                win_txt ="   " + data.getWin_cnt();
                win_cnt.setText(win_txt);
            }


            lose_layoutParams.horizontalWeight = 100-data.getWin_percent();
            lose_cnt.setLayoutParams(lose_layoutParams);
            if(!data.getLose_cnt().equals("0L")){
                lose_txt = data.getLose_cnt() + "   ";
                lose_cnt.setText(lose_txt);
            }

            Glide.with(itemView.getContext()).load(data.getChampion_url()).into(championUrl);
        }

    }
}
