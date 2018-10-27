package com.example.jonathanharty.flashcard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        findViewById(R.id.cancel_add_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.save_question).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String questionText = ((EditText)findViewById(R.id.edit_question)).getText().toString();
                String answerText = ((EditText)findViewById(R.id.edit_answer)).getText().toString();

                Intent data = new Intent();
                data.putExtra("question", questionText);
                data.putExtra("answer", answerText);

                setResult(100, data);
                finish();
            }
        });
    }
}
