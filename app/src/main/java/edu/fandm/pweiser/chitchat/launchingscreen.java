package edu.fandm.pweiser.chitchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class launchingscreen extends AppCompatActivity {

    Button easy, medium, hard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launchscreen);

        easy = (Button) findViewById(R.id.Easy) ;
        medium = (Button) findViewById(R.id.Medium) ;
        hard = (Button) findViewById(R.id.Hard) ;


    }

    public void GameLaunch(View v){
        Button b = (Button) v;
        Intent intent = new Intent(this, LaunchActivity.class);
        String difficulty = b.getText().toString();

        switch(difficulty){
            case "Easy":
                Toast.makeText(this,"PassingIntent",Toast.LENGTH_SHORT).show();
                intent.putExtra("seq", 3);
                startActivity(intent);
                break;
            case "Medium":
                Toast.makeText(this,"PassingIntent",Toast.LENGTH_SHORT).show();
                intent.putExtra("seq", 4);
                startActivity(intent);
                break;
            case "Hard":
                Toast.makeText(this,"PassingIntent",Toast.LENGTH_SHORT).show();
                intent.putExtra("seq", 5);
                startActivity(intent);
                break;
        }
    }
}
