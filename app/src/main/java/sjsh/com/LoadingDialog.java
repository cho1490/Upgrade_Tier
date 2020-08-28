package sjsh.com;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoadingDialog {

    private Activity activity;

    private AlertDialog alertDialog;

    public LoadingDialog(Activity activity){
        this.activity = activity;
    }

    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog, null));
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
    }

    public void dismissDialog(){
        alertDialog.dismiss();
    }

    public void offProgressBar(){
        ProgressBar progressBar = alertDialog.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }

    public void setMessage(String message){
        TextView textView = alertDialog.findViewById(R.id.textForMessage);
        textView.setText(message);
    }

    public void onCancelable(){
        alertDialog.setCancelable(true);
    }

    public void onButtonForFinish(){
        Button button = alertDialog.findViewById(R.id.btn_dialogOk);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                activity.finish();
            }
        });
    }

    public void onButtonForStay(){
        Button button = alertDialog.findViewById(R.id.btn_dialogOk);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });
    }

}
