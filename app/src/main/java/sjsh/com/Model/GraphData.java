package sjsh.com.Model;

public class GraphData {

    int win_percent;
    String win_cnt;
    String lose_cnt;

    String champion_url;

    public int getWin_percent() {
        return win_percent;
    }

    public void setWin_percent(int win_percent) {
        this.win_percent = win_percent;
    }

    public String getWin_cnt() {
        return win_cnt;
    }

    public void setWin_cnt(String win_cnt) {
        this.win_cnt = win_cnt;
    }

    public String getLose_cnt() {
        return lose_cnt;
    }

    public void setLose_cnt(String lose_cnt) {
        this.lose_cnt = lose_cnt;
    }

    public String getChampion_url() {
        return champion_url;
    }

    public void setChampion_url(String champion_url) {
        this.champion_url = champion_url;
    }
}