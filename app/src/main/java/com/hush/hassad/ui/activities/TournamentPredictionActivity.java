package com.hush.hassad.ui.activities;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.hush.hassad.R;
import com.hush.hassad.controller.competition.results.TournamentResult;
import com.hush.hassad.controller.player.User;
import com.hush.hassad.controller.predictions.TournamentPrediction;
import com.hush.hassad.ui.adapters.FriendsAdapter;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class TournamentPredictionActivity extends AppCompatActivity {

	private Spinner spinner_gold, spinner_silver, spinner_bronze;
	private Button btnSubmit;
	private String [] countries;
	private TypedArray flags;// = {R.drawable.manchester_united,R.drawable.chelsea,R.drawable.profile};
	ArrayList<User> users;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tournament_prediction);

		spinner_gold = (Spinner) findViewById(R.id.spinner_gold);
		spinner_silver = (Spinner) findViewById(R.id.spinner_silver);
		spinner_bronze = (Spinner) findViewById(R.id.spinner_bronze);
		btnSubmit = (Button) findViewById(R.id.btnSubmitTournamentPred);

		countries = getResources().getStringArray(R.array.countries);
		flags = getResources().obtainTypedArray(R.array.flags);

		SpinnerAdapter spinnerAdapter = new com.hush.hassad.ui.adapters.SpinnerAdapter(this,R.layout.custom_spinner_items,countries,flags);

		spinner_gold.setAdapter(spinnerAdapter);
		spinner_silver.setAdapter(spinnerAdapter);
		spinner_bronze.setAdapter(spinnerAdapter);

		btnSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String spinnerGoldText = spinner_gold.getSelectedItem().toString();
				String spinnerSilverText = spinner_silver.getSelectedItem().toString();
				String spinnerBronzeText = spinner_bronze.getSelectedItem().toString();
				Log.d("TeamsOnClick", "onClick: " + spinnerGoldText + " " + spinnerSilverText + " " + spinnerBronzeText );
				//TODO check if same teams then intent to main activity

				Intent intent = new Intent(getApplicationContext(), MainActivity.class);

				intent.putExtra("prediction", true);
				intent.putExtra("gold", spinnerGoldText);
				intent.putExtra("silver", spinnerSilverText);
				intent.putExtra("bronze", spinnerBronzeText);

				startActivity(intent);

				finish();
			}
		});
	}
}
