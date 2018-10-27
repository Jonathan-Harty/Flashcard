package com.example.jonathanharty.flashcard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    boolean isShowingAnswers = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.flashcardQuestion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.flashcardQuestion).setVisibility(View.INVISIBLE);
                findViewById(R.id.flashcardAnswer).setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.flashcardAnswer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.flashcardQuestion).setVisibility(View.VISIBLE);
                findViewById(R.id.flashcardAnswer).setVisibility(View.INVISIBLE);
            }
        });


        findViewById(R.id.toggle_choices_visibility).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isShowingAnswers) {
                    isShowingAnswers = false;

                    findViewById(R.id.choice1).setVisibility(View.INVISIBLE);
                    findViewById(R.id.choice2).setVisibility(View.INVISIBLE);
                    findViewById(R.id.choice3).setVisibility(View.INVISIBLE);
                    findViewById(R.id.choice4).setVisibility(View.INVISIBLE);
                    ((ImageView)findViewById(R.id.toggle_choices_visibility)).setImageResource(R.drawable.view_answers_symbol);
                }
                else {
                    isShowingAnswers = true;

                    findViewById(R.id.choice1).setVisibility(View.VISIBLE);
                    findViewById(R.id.choice2).setVisibility(View.VISIBLE);
                    findViewById(R.id.choice3).setVisibility(View.VISIBLE);
                    findViewById(R.id.choice4).setVisibility(View.VISIBLE);
                    ((ImageView) findViewById(R.id.toggle_choices_visibility)).setImageResource(R.drawable.hide_answers_symbol);
                }
            }
        });

        findViewById(R.id.add_question).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                MainActivity.this.startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100) {
            String question = data.getExtras().getString("question");
            String answer = data.getExtras().getString("answer");

            ((TextView)findViewById(R.id.flashcardQuestion)).setText(question);
            ((TextView)findViewById(R.id.flashcardAnswer)).setText(answer);
        }
    }
}
