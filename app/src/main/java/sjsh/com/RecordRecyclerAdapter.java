package sjsh.com;

import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecordRecyclerAdapter extends RecyclerView.Adapter<RecordRecyclerAdapter.ItemViewHolder> {

    private ArrayList<RecordData> listData = new ArrayList<>();

    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);
    boolean isScreenShot = false;

    public RecordRecyclerAdapter(){

    }

    @NonNull
    @Override
    public RecordRecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recordrecyclerview_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));

        if ( mSelectedItems.get(position, false) ){
            holder.firstBackground.setBackgroundColor(Color.parseColor("#858585"));
        } else {
            holder.firstBackground.setBackgroundColor(Color.WHITE);
        }
    }

    void setScreenShot(boolean screenShot){ isScreenShot = screenShot; }

    void addItem(RecordData data) {
        listData.add(data);
    }

    ArrayList<RecordData> getListData(){
        return listData;
    }

    ArrayList<RecordData> getSelectedListData(){
        ArrayList<RecordData> selectedListData = new ArrayList<>();
        for(int i = 0;i<listData.size();i++){
            if(mSelectedItems.get(i, false)){
                selectedListData.add(listData.get(i));
            }
        }
        return selectedListData;
    }

    void setListData(ArrayList<RecordData> listData){
        this.listData = listData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return  listData == null ? 0 : listData.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView tv_result, tv_time, tv_kill, tv_death, tv_assist, tv_percent, tv_game_mode, tv_game_ago, tv_badge;
        private ImageView iv_champion, iv_spell1, iv_spell2, iv_rune1, iv_rune2, iv_item1, iv_item2, iv_item3, iv_item4, iv_item5, iv_item6, iv_item7;
        private LinearLayout ll_background, firstBackground ;


        public ItemViewHolder(@NonNull final View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            firstBackground = itemView.findViewById(R.id.first_background);
            ll_background = itemView.findViewById(R.id.ll_background);

            tv_result = itemView.findViewById(R.id.tv_result);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_kill = itemView.findViewById(R.id.tv_kill);
            tv_death = itemView.findViewById(R.id.tv_death);
            tv_assist = itemView.findViewById(R.id.tv_assist);
            tv_percent = itemView.findViewById(R.id.tv_percent);
            tv_game_mode = itemView.findViewById(R.id.tv_game_mode);
            tv_game_ago = itemView.findViewById(R.id.tv_game_ago);
            tv_badge = itemView.findViewById(R.id.tv_badge);

            iv_champion = itemView.findViewById(R.id.iv_champion);
            iv_spell1 = itemView.findViewById(R.id.iv_spell1);
            iv_spell2 = itemView.findViewById(R.id.iv_spell2);
            iv_rune1 = itemView.findViewById(R.id.iv_rune1);
            iv_rune2 = itemView.findViewById(R.id.iv_rune2);
            iv_item1 = itemView.findViewById(R.id.iv_item1);
            iv_item2 = itemView.findViewById(R.id.iv_item2);
            iv_item3 = itemView.findViewById(R.id.iv_item3);
            iv_item4 = itemView.findViewById(R.id.iv_item4);
            iv_item5 = itemView.findViewById(R.id.iv_item5);
            iv_item6 = itemView.findViewById(R.id.iv_item6);
            iv_item7 = itemView.findViewById(R.id.iv_item7);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isScreenShot){
                        int position = getAdapterPosition();
                        if ( mSelectedItems.get(position, false) ){
                            mSelectedItems.put(position, false);
                            firstBackground.setBackgroundColor(Color.WHITE);
                        } else {
                            mSelectedItems.put(position, true);
                            firstBackground.setBackgroundColor(Color.parseColor("#999999"));
                        }
                    }
                }
            });

        }

        void onBind(RecordData data) {
            tv_result.setText(data.getResult());
            if (data.getResult().equals("승")) {
                ll_background.setBackgroundColor(Color.parseColor("#5587E6"));
            } else if (data.getResult().equals("패")) {
                ll_background.setBackgroundColor(Color.parseColor("#E6415A"));
            } else if (data.getResult().equals("리")) {
                ll_background.setBackgroundColor(Color.parseColor("#B6B6B6"));
            }

            tv_time.setText(data.getTime());
            tv_kill.setText(data.getKill());
            tv_death.setText(data.getDeath());
            tv_assist.setText(data.getAssist());
            tv_percent.setText(data.getKda_percent());
            tv_game_mode.setText(data.getGame_mode());
            tv_game_ago.setText(data.getGame_ago());

            tv_badge.setText(data.getBadge());
            if(data.getBadge().equals("")){
                tv_badge.setVisibility(View.INVISIBLE);
            }else if(data.getBadge().equals("MVP") || data.getBadge().equals("ACE")){
                tv_badge.setTextColor(Color.parseColor("#5587E6"));
            }else{
                tv_badge.setTextColor(Color.parseColor("#F44336"));
            }

            Glide.with(itemView.getContext()).load(data.getChampion_url()).into(iv_champion);
            Glide.with(itemView.getContext()).load(data.getSpell1_url()).into(iv_spell1);
            Glide.with(itemView.getContext()).load(data.getSpell2_url()).into(iv_spell2);
            Glide.with(itemView.getContext()).load(data.getRune1_url()).into(iv_rune1);
            Glide.with(itemView.getContext()).load(data.getRune2_url()).into(iv_rune2);
            Glide.with(itemView.getContext()).load(data.getItem1_url()).into(iv_item1);
            Glide.with(itemView.getContext()).load(data.getItem2_url()).into(iv_item2);
            Glide.with(itemView.getContext()).load(data.getItem3_url()).into(iv_item3);
            Glide.with(itemView.getContext()).load(data.getItem4_url()).into(iv_item4);
            Glide.with(itemView.getContext()).load(data.getItem5_url()).into(iv_item5);
            Glide.with(itemView.getContext()).load(data.getItem6_url()).into(iv_item6);
            Glide.with(itemView.getContext()).load(data.getItem7_url()).into(iv_item7);
        }

    }
}
