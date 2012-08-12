package ece.uprm.vzw.services;

import java.text.DecimalFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;
import ece.uprm.vzw.R;

public class DropCallActivity extends Activity {
	
	private Context context;
	
	private SharedPreferences preferences;
	
	private int signalStrenght;
	private final DecimalFormat oneDForm = new DecimalFormat("#.#");
	
	private final static String DC_FILE_NAME = "DropCalls_INF";
	
	private String message = "\n\nWe noticed a finished call with very low signal.\nWas it a dropped call?";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.disclaimer_layout);
		
		context = this;
		
		signalStrenght = getIntent().getIntExtra("signalStrength", -1);
		String signalStrenghtdB = oneDForm.format(1.9375*signalStrenght - 113);
		
		AlertDialog.Builder dcDialog = new AlertDialog.Builder(this);
		dcDialog.setTitle(getString(R.string.app_name));
		dcDialog.setMessage("Signal when call ended:\n" + signalStrenght + "\t(" + signalStrenghtdB + " dBm) " + message);
		dcDialog.setCancelable(true);
		dcDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						preferences = context.getSharedPreferences(DC_FILE_NAME, Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = preferences.edit();
						editor.putInt("count", preferences.getInt("count", 0) + 1);
						editor.commit();
						Toast.makeText(context, "Got it! Thanks!", Toast.LENGTH_SHORT).show();
						finish();
					}
				});
		dcDialog.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						finish();
					}
				});
		dcDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				finish();
			}
		});
		AlertDialog dialogLoadStart = dcDialog.create();
		dialogLoadStart.show();
	}

}
