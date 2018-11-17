package com.example.jonathanharty.flashcard;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amitshekhar.DebugDB;

import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    boolean isShowingAnswers = false;
    boolean isShowingQuestionSide = true;
    boolean emptyState = true;
    List<Flashcard> allFlashcards;
    int currentCardDisplayedIndex = 0;

    FlashcardDatabase flashcardDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashcards = flashcardDatabase.getAllCards();

        if(allFlashcards != null && allFlashcards.size() > 0) {
            ((TextView)findViewById(R.id.flashcardQuestion)).setText(allFlashcards.get(0).getQuestion());
            ((TextView)findViewById(R.id.flashcardAnswer)).setText(allFlashcards.get(0).getAnswer());

            int correctPosition = getRandNum(1, 3);

            if(correctPosition == 1) {
                ((TextView)findViewById(R.id.choice1)).setText(allFlashcards.get(0).getAnswer());
                ((TextView)findViewById(R.id.choice2)).setText(allFlashcards.get(0).getWrongAnswer1());
                ((TextView)findViewById(R.id.choice3)).setText(allFlashcards.get(0).getWrongAnswer2());
            }
            else if(correctPosition == 2) {
                ((TextView)findViewById(R.id.choice1)).setText(allFlashcards.get(0).getWrongAnswer1());
                ((TextView)findViewById(R.id.choice2)).setText(allFlashcards.get(0).getAnswer());
                ((TextView)findViewById(R.id.choice3)).setText(allFlashcards.get(0).getWrongAnswer2());
            }
            else {
                ((TextView)findViewById(R.id.choice1)).setText(allFlashcards.get(0).getWrongAnswer1());
                ((TextView)findViewById(R.id.choice2)).setText(allFlashcards.get(0).getWrongAnswer2());
                ((TextView)findViewById(R.id.choice3)).setText(allFlashcards.get(0).getAnswer());
            }
        }
        else {
            displayEmpty();
            emptyState = true;
        }

        findViewById(R.id.flashcardQuestion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.flashcardQuestion).setVisibility(View.INVISIBLE);
                findViewById(R.id.flashcardAnswer).setVisibility(View.VISIBLE);

                isShowingQuestionSide = false;
            }
        });

        findViewById(R.id.flashcardAnswer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.flashcardQuestion).setVisibility(View.VISIBLE);
                findViewById(R.id.flashcardAnswer).setVisibility(View.INVISIBLE);

                isShowingQuestionSide = true;
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
                    ((ImageView)findViewById(R.id.toggle_choices_visibility)).setImageResource(R.drawable.view_answers_symbol);
                }
                else {
                    isShowingAnswers = true;

                    findViewById(R.id.choice1).setVisibility(View.VISIBLE);
                    findViewById(R.id.choice2).setVisibility(View.VISIBLE);
                    findViewById(R.id.choice3).setVisibility(View.VISIBLE);
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

        findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);

                intent.putExtra("currentQuestion", ((TextView)findViewById(R.id.flashcardQuestion)).getText());
                intent.putExtra("currentAnswer", ((TextView)findViewById(R.id.flashcardAnswer)).getText());

                intent.putExtra("answerChoice1", ((TextView)findViewById(R.id.choice1)).getText());
                intent.putExtra("answerChoice2", ((TextView)findViewById(R.id.choice2)).getText());
                intent.putExtra("answerChoice3", ((TextView)findViewById(R.id.choice3)).getText());


                MainActivity.this.startActivityForResult(intent, 110);
            }
        });

        findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allFlashcards.size() > 0) {
                    if(!isShowingQuestionSide) {
                        findViewById(R.id.flashcardQuestion).setVisibility(View.VISIBLE);
                        findViewById(R.id.flashcardAnswer).setVisibility(View.INVISIBLE);
                    }

                    currentCardDisplayedIndex = getRandNum(0, allFlashcards.size() - 1);

                    if(currentCardDisplayedIndex > allFlashcards.size() - 1) {
                        currentCardDisplayedIndex = 0;
                    }

                    displayNext();
                    emptyState = false;
                }
            }
        });

        findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allFlashcards.size() > 0) {
                    flashcardDatabase.deleteCard(((TextView)findViewById(R.id.flashcardQuestion)).getText().toString());
                    allFlashcards.remove(currentCardDisplayedIndex);

                    if(allFlashcards.size() > 0) {
                        currentCardDisplayedIndex = getRandNum(0, allFlashcards.size() - 1);

                        displayNext();
                        emptyState = false;
                    }
                    else {
                        emptyState = true;
                        displayEmpty();
                    }

                }
                else {
                    emptyState = true;
                    displayEmpty();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100 && resultCode == 100) {
            String question = data.getExtras().getString("question");
            String answer = data.getExtras().getString("answer");

            String incorrect_1 = data.getExtras().getString("incorrect1");
            String incorrect_2 = data.getExtras().getString("incorrect2");

            flashcardDatabase.insertCard(new Flashcard(question, answer, incorrect_1, incorrect_2));
            allFlashcards = flashcardDatabase.getAllCards();

            ((TextView)findViewById(R.id.flashcardQuestion)).setText(question);
            ((TextView)findViewById(R.id.flashcardAnswer)).setText(answer);

            int correctPosition = getRandNum(1, 3);

            if(correctPosition == 1) {
                ((TextView)findViewById(R.id.choice1)).setText(answer);
                ((TextView)findViewById(R.id.choice2)).setText(incorrect_1);
                ((TextView)findViewById(R.id.choice3)).setText(incorrect_2);
            }
            else if(correctPosition == 2) {
                ((TextView)findViewById(R.id.choice1)).setText(incorrect_1);
                ((TextView)findViewById(R.id.choice2)).setText(answer);
                ((TextView)findViewById(R.id.choice3)).setText(incorrect_2);
            }
            else {
                ((TextView)findViewById(R.id.choice1)).setText(incorrect_1);
                ((TextView)findViewById(R.id.choice2)).setText(incorrect_2);
                ((TextView)findViewById(R.id.choice3)).setText(answer);
            }

            Snackbar.make(findViewById(R.id.flashcardQuestion), "Question Added", Snackbar.LENGTH_SHORT).show();

            if(emptyState) {
                findViewById(R.id.empty).setVisibility(View.INVISIBLE);
                findViewById(R.id.toggle_choices_visibility).setVisibility(View.VISIBLE);
                if(isShowingAnswers) {
                    findViewById(R.id.choice1).setVisibility(View.VISIBLE);
                    findViewById(R.id.choice2).setVisibility(View.VISIBLE);
                    findViewById(R.id.choice3).setVisibility(View.VISIBLE);
                    ((ImageView) findViewById(R.id.toggle_choices_visibility)).setImageResource(R.drawable.hide_answers_symbol);
                }
                else {
                    ((ImageView) findViewById(R.id.toggle_choices_visibility)).setImageResource(R.drawable.view_answers_symbol);
                }
                findViewById(R.id.flashcardQuestion).setVisibility(View.VISIBLE);
                findViewById(R.id.edit_button).setVisibility(View.VISIBLE);
                findViewById(R.id.next_button).setVisibility(View.VISIBLE);
                findViewById(R.id.delete_button).setVisibility(View.VISIBLE);
                displayNext();
            }
        }
        else if(requestCode == 110 && resultCode == 100) {

            String question = data.getExtras().getString("question");
            String answer = data.getExtras().getString("answer");

            String incorrect_1 = data.getExtras().getString("incorrect1");
            String incorrect_2 = data.getExtras().getString("incorrect2");

            ((TextView)findViewById(R.id.flashcardQuestion)).setText(question);
            ((TextView)findViewById(R.id.flashcardAnswer)).setText(answer);

            int correctPosition = getRandNum(1, 3);

            if(correctPosition == 1) {
                ((TextView)findViewById(R.id.choice1)).setText(answer);
                ((TextView)findViewById(R.id.choice2)).setText(incorrect_1);
                ((TextView)findViewById(R.id.choice3)).setText(incorrect_2);
            }
            else if(correctPosition == 2) {
                ((TextView)findViewById(R.id.choice1)).setText(incorrect_1);
                ((TextView)findViewById(R.id.choice2)).setText(answer);
                ((TextView)findViewById(R.id.choice3)).setText(incorrect_2);
            }
            else {
                ((TextView)findViewById(R.id.choice1)).setText(incorrect_1);
                ((TextView)findViewById(R.id.choice2)).setText(incorrect_2);
                ((TextView)findViewById(R.id.choice3)).setText(answer);
            }

            Flashcard curr = allFlashcards.get(currentCardDisplayedIndex);
            curr.setQuestion(question);
            curr.setAnswer(answer);
            curr.setWrongAnswer1(incorrect_1);
            curr.setWrongAnswer2(incorrect_2);

            flashcardDatabase.updateCard(curr);

            Snackbar.make(findViewById(R.id.flashcardQuestion), "Question Updated", Snackbar.LENGTH_SHORT).show();
        }
    }

    public int getRandNum(int minNum, int maxNum) {
        Random rand = new Random();
        return rand.nextInt((maxNum - minNum) + 1) + minNum;
    }

    public void displayEmpty() {
        findViewById(R.id.empty).setVisibility(View.VISIBLE);
        findViewById(R.id.choice1).setVisibility(View.INVISIBLE);
        findViewById(R.id.choice2).setVisibility(View.INVISIBLE);
        findViewById(R.id.choice3).setVisibility(View.INVISIBLE);
        findViewById(R.id.flashcardQuestion).setVisibility(View.INVISIBLE);
        findViewById(R.id.flashcardAnswer).setVisibility(View.INVISIBLE);
        findViewById(R.id.edit_button).setVisibility(View.INVISIBLE);
        findViewById(R.id.toggle_choices_visibility).setVisibility(View.INVISIBLE);
        findViewById(R.id.next_button).setVisibility(View.INVISIBLE);
        findViewById(R.id.delete_button).setVisibility(View.INVISIBLE);
    }

    public void displayNext() {
        ((TextView)findViewById(R.id.flashcardQuestion)).setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());
        ((TextView)findViewById(R.id.flashcardAnswer)).setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());

        int correctPosition = getRandNum(1, 3);

        if(correctPosition == 1) {
            ((TextView)findViewById(R.id.choice1)).setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());
            ((TextView)findViewById(R.id.choice2)).setText(allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer1());
            ((TextView)findViewById(R.id.choice3)).setText(allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer2());
        }
        else if(correctPosition == 2) {
            ((TextView)findViewById(R.id.choice1)).setText(allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer1());
            ((TextView)findViewById(R.id.choice2)).setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());
            ((TextView)findViewById(R.id.choice3)).setText(allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer2());
        }
        else {
            ((TextView)findViewById(R.id.choice1)).setText(allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer1());
            ((TextView)findViewById(R.id.choice2)).setText(allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer2());
            ((TextView)findViewById(R.id.choice3)).setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());
        }
    }
}
