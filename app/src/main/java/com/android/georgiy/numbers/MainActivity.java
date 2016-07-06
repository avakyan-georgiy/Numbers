package com.android.georgiy.numbers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public TextView nums;
    public TextView result;
    public TextView counter;
    public TextView timer;
    public EditText answer;
    public EditText ct_timer;
    public int btncase;
    public int tmp = 4000;
    public int correct_answer = 0;
    public int incorrect_answer = 0;
    public OnClickListener radioListener;
    public OnClickListener startGameListener;
    public View.OnKeyListener enterListener;
    Button startGame;
    RadioButton firstrb;
    RadioButton scndrb;
    RadioButton thirdrb;
    private Chronometer mChronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startGame = (Button) findViewById(R.id.startGame_button);
        counter = (TextView) findViewById(R.id.counter_textView);
        timer = (TextView) findViewById(R.id.timer_textView);
        result = (TextView) findViewById(R.id.result_textView);
        nums = (TextView) findViewById(R.id.nums_textView);
        answer = (EditText) findViewById(R.id.answer_editText);
        ct_timer = (EditText) findViewById(R.id.timer_editText);
        mChronometer = (Chronometer) findViewById(R.id.chronometer);

        ct_timer.setText(String.valueOf(4));
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.setFormat("%s");

        radioListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton rb = (RadioButton) v;
                switch (rb.getId()) {
                    case R.id.radioButton:
                        randFunc(1);
                        break;
                    case R.id.radioButton2:
                        randFunc(2);
                        break;
                    case R.id.radioButton3:
                        randFunc(3);
                        break;
                    default:
                        break;
                }
            }
        };

        firstrb = (RadioButton) findViewById(R.id.radioButton);
        assert firstrb != null;
        firstrb.setOnClickListener(radioListener);
        scndrb = (RadioButton) findViewById(R.id.radioButton2);
        assert scndrb != null;
        scndrb.setOnClickListener(radioListener);
        scndrb.setChecked(true);
        scndrb.performClick();

        thirdrb = (RadioButton) findViewById(R.id.radioButton3);
        assert thirdrb != null;
        thirdrb.setOnClickListener(radioListener);

        answer.requestFocus();

        enterListener = new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if ((correct_answer + incorrect_answer) < 9) {
                        writeRes();
                        ct_timer.setEnabled(false);
                        ct_timer.setFocusable(false);
                        if ((checkAnswer(null)) && (checkAnswerLength())) {
                            correct_answer++;
                            counter.setText(String.valueOf(10 - (correct_answer + incorrect_answer)));
                            return true;
                        } else if ((!checkAnswer(null)) && (checkAnswerLength())) {
                            incorrect_answer++;
                            counter.setText(String.valueOf(10 - (correct_answer + incorrect_answer)));
                            return true;
                        }
                        return true;
                    } else {
                        writeRes();
                        if (checkAnswer(null)) {
                            correct_answer++;
                            counter.setText(Html.fromHtml("<font color=#00ff00>" + String.valueOf(correct_answer) + "</font>:<font color=#ff0000>" + String.valueOf(incorrect_answer) + "</font>"));
                            Toast.makeText(getApplicationContext(), "Game finished!", Toast.LENGTH_LONG).show();
                            correct_answer = 0;
                            incorrect_answer = 0;
                            mChronometer.stop();
                            String timertext = String.valueOf(((SystemClock.elapsedRealtime()
                                    - mChronometer.getBase()) / 1000) - (tmp * 10)) + "с, t = " + tmp / 1000;
                            timer.setText(timertext);
                            mChronometer.setVisibility(View.GONE);
                            ct_timer.setEnabled(true);
                            ct_timer.setFocusableInTouchMode(true);
                            hideKeyboard();
                        } else if (!checkAnswer(null)) {
                            incorrect_answer++;
                            counter.setText(Html.fromHtml("<font color=#00ff00>" + String.valueOf(correct_answer) + "</font>:<font color=#ff0000>" + String.valueOf(incorrect_answer) + "</font>"));
                            Toast.makeText(getApplicationContext(), "Game finished!", Toast.LENGTH_LONG).show();
                            correct_answer = 0;
                            incorrect_answer = 0;
                            mChronometer.stop();
                            String timertext = String.valueOf(((SystemClock.elapsedRealtime()
                                    - mChronometer.getBase()) / 1000) - (tmp / 100)) + "с, t = " + tmp / 1000;
                            timer.setText(timertext);
                            mChronometer.setVisibility(View.GONE);
                            ct_timer.setEnabled(true);
                            ct_timer.setFocusableInTouchMode(true);
                            hideKeyboard();
                        }
                        hideKeyboard();
                        return true;
                    }
                }
                return false;
            }
        };
        answer.setOnKeyListener(enterListener);

        startGameListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                correct_answer = 0;
                incorrect_answer = 0;
                counter.setText(String.valueOf(10));
                performRbClick();
                mChronometer.setVisibility(View.VISIBLE);
                mChronometer.setBase(SystemClock.elapsedRealtime());
                mChronometer.start();
                timer.setText("");
                answer.getText().clear();
                answer.requestFocus();
            }
        };

        startGame.setOnClickListener(startGameListener);
    }

    public void performRbClick() {
        if (firstrb.isChecked()) {
            firstrb.performClick();
        } else if (scndrb.isChecked()) {
            scndrb.performClick();
        } else if (thirdrb.isChecked()) {
            thirdrb.performClick();
        } else {
            Toast.makeText(getApplicationContext(), "Режим игры не выбран", Toast.LENGTH_LONG).show();
        }
    }

    public int randFunc(int var) {
        Random r = new Random();
        int fmin;
        int fmax;
        int smin;
        int smax;
        nums.setText("");

        switch (var) {
            case 1:
                fmin = 2;
                fmax = 9;
                smin = 10;
                smax = 99;
                String text = String.valueOf(r.nextInt(fmax - fmin + 1) + fmin) + " * " + String.valueOf(r.nextInt(smax - smin + 1) + smin);
                nums.setText(text);
                btncase = 1;
                break;
            case 2:
                fmin = 11;
                fmax = 99;
                smin = fmin;
                smax = fmax;
                String text2 = String.valueOf(r.nextInt(fmax - fmin + 1) + fmin) + " * " + String.valueOf(r.nextInt(smax - smin + 1) + smin);
                nums.setText(text2);
                btncase = 2;
                break;
            case 3:
                fmin = 2;
                fmax = 9;
                smin = 101;
                smax = 999;
                String text3 = String.valueOf(r.nextInt(fmax - fmin + 1) + fmin) + " * " + String.valueOf(r.nextInt(smax - smin + 1) + smin);
                nums.setText(text3);
                btncase = 3;
                break;
            default:
                counter.setText(String.valueOf(10));
                break;
        }
        return btncase;
    }

    public void checkTimer() {
        tmp = 4000;
        if (ct_timer.getText().toString().trim().length() > 0) {
            tmp = Integer.parseInt(ct_timer.getText().toString());
            tmp = tmp * 1000;
        }
        answer.setEnabled(false);
        CountDownTimer timer = new CountDownTimer(tmp, 1000) {
            @Override
            public void onFinish() {
                result.setText("");
                answer.getText().clear();
                answer.setEnabled(true);
                if (1 == btncase) {
                    firstrb.performClick();
                } else if (2 == btncase) {
                    scndrb.performClick();
                } else if (3 == btncase) {
                    thirdrb.performClick();
                } else {
                    Toast.makeText(getApplicationContext(), "ERROR!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onTick(long millisLeft) {
                // not ticking
            }
        };
        timer.start();
    }

    public void showKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(answer, InputMethodManager.SHOW_IMPLICIT);
    }

    public void hideKeyboard() {
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromInputMethod(null, InputMethodManager.SHOW_IMPLICIT);
    }

    public boolean checkAnswer(View view) {
        if (answer.getText().length() > 0) {
            int answ = Integer.parseInt(answer.getText().toString());
            String[] separated = nums.getText().toString().split(" ");
            int fnum = Integer.parseInt(separated[0]);
            int snum = Integer.parseInt(separated[2]);
            int tnum = fnum * snum;
            return answ == tnum;
        } else {
            Toast.makeText(getApplicationContext(), "Проверьте ввод!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public boolean checkAnswerLength() {
        if (answer.getText().length() > 0) {
            return true;
        } else if (answer.getText().length() <= 0) {
            return false;
        }
        return false;
    }

    public void writeRes() {
        answer.setEnabled(false);
        answer.setFocusable(false);
        int greencolor = Color.GREEN;
        int redcolor = Color.RED;
        if (checkAnswer(null)) {
            result.setText("OК");
            result.setTextColor(greencolor);
            result.setTypeface(null, Typeface.BOLD);
            checkTimer();
            new CountDownTimer(tmp, 1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    answer.setEnabled(true);
                    answer.setFocusableInTouchMode(true);
                    answer.requestFocus();
                    showKeyboard(answer);
                }
            }.start();

        } else if (!checkAnswer(null)) {
            result.setText("Мимо");
            result.setTextColor(redcolor);
            result.setTypeface(null, Typeface.BOLD);
            checkTimer();
            new CountDownTimer(tmp, 1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    answer.setEnabled(true);
                    answer.setFocusableInTouchMode(true);
                    answer.requestFocus();
                    showKeyboard(answer);
                }
            }.start();
        }
    }
}
