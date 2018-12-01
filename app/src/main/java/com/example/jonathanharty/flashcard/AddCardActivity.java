package com.example.jonathanharty.flashcard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        String currentQuestion = getIntent().getStringExtra("currentQuestion");
        String currentAnswer = getIntent().getStringExtra("currentAnswer");

        String answerChoice1 = getIntent().getStringExtra("answerChoice1");
        String answerChoice2 = getIntent().getStringExtra("answerChoice2");
        String answerChoice3 = getIntent().getStringExtra("answerChoice3");

        if(answerChoice1 != null && answerChoice1.equals(currentAnswer)) {
            ((EditText)findViewById(R.id.edit_incorrect_1)).setText(answerChoice2);
            ((EditText)findViewById(R.id.edit_incorrect_2)).setText(answerChoice3);
        }
        else if(answerChoice2 != null && answerChoice2.equals(currentAnswer)) {
            ((EditText)findViewById(R.id.edit_incorrect_1)).setText(answerChoice1);
            ((EditText)findViewById(R.id.edit_incorrect_2)).setText(answerChoice3);
        }
        else if(answerChoice3 != null && answerChoice3.equals(currentAnswer)) {
            ((EditText)findViewById(R.id.edit_incorrect_1)).setText(answerChoice1);
            ((EditText)findViewById(R.id.edit_incorrect_2)).setText(answerChoice2);
        }

        ((EditText)findViewById(R.id.edit_question)).setText(currentQuestion);
        ((EditText)findViewById(R.id.edit_answer)).setText(currentAnswer);


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

                String incorrect1Text = ((EditText)findViewById(R.id.edit_incorrect_1)).getText().toString();
                String incorrect2Text = ((EditText)findViewById(R.id.edit_incorrect_2)).getText().toString();

                if(questionText.equals("") || answerText.equals("") || incorrect1Text.equals("") || incorrect2Text.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Question and Answer Needed", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                }
                else {
                    Intent data = new Intent();
                    data.putExtra("question", questionText);
                    data.putExtra("answer", answerText);

                    data.putExtra("incorrect1", incorrect1Text);
                    data.putExtra("incorrect2", incorrect2Text);

                    setResult(100, data);
                    finish();
                    overridePendingTransition(R.anim.left_in, R.anim.right_out);
                }
            }
        });
    }
}
