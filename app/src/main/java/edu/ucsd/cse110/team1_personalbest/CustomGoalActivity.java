package edu.ucsd.cse110.team1_personalbest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import edu.ucsd.cse110.team1_personalbest.Activities.MainActivity;
import edu.ucsd.cse110.team1_personalbest.Firebase.Database;

public class CustomGoalActivity extends AppCompatActivity {

    private Database db;
    private String FILENAME = "steps.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_goal);

        Button btnCancelCustomGoal = (Button) findViewById(R.id.buttonCancelCustomGoal);
        btnCancelCustomGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        Button btnAcceptCustomGoal = (Button) findViewById(R.id.buttonAcceptCustomGoal);
        btnAcceptCustomGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCustomGoal(v);
            }
        });

        db = new Database(getApplicationContext());
    }

    public void saveCustomGoal(View view){

        EditText newGoal = (EditText) findViewById(R.id.customGoal);

        SharedPreferences sharedPreferences = getSharedPreferences("new_goal", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("newGoal", newGoal.getText().toString());

        editor.apply();
        startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

}
