//https://stackoverflow.com/questions/26893796/how-to-set-emoji-by-unicode-in-a-textview
//https://en.wikipedia.org/wiki/Emoji#Unicode_blocks
package com.clydefrog04.calculatorinator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    String answer = "";
    int dead = 0x1F635;
    EditText answerDisplay;
    ArrayList<String> numbers;
    ArrayList<String> operators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//todo: create landscape view

        //init vars
        answerDisplay = findViewById(R.id.answerDisplay);
        numbers = new ArrayList<>();
        operators = new ArrayList<>();

        answerDisplay.setText(answer);
        answerDisplay.setTextIsSelectable(false);
        answerDisplay.setFocusableInTouchMode(false);
    }

    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

    public void digitBtnHandler(View view) {
        Button btn = (Button) view;
        String appendText = btn.getText().toString();
        Log.i("info", appendText);
        Log.i("info", "btn id:" + view.getId());
        Log.i("info", String.valueOf(view.getWidth()));

        switch (view.getId()) {
            case R.id.period:
                if (!answer.contains(".")) {
                    answer += appendText;
                }
                break;
            case R.id.clear:
                answer = "";
                break;
            case R.id.solveBtn:
                solveProblem(answer);
                break;
            default:
                Log.i("debug", "default called");
                answer += appendText;
                break;
        }
        answerDisplay.setText(answer);
    }

    private void solveProblem(String equation){
        String pattern = "((?<=[/*\\-+])|(?=[/*\\-+]))";
        ArrayList<String> equationParts = new ArrayList<>(Arrays.asList(equation.split(pattern)));
        while (equationParts.size() > 1) {
            if (equationParts.contains("*")) {
                int index = equationParts.indexOf("*");
                double operand1 = Double.parseDouble(equationParts.get(index - 1));
                double operand2 = Double.parseDouble(equationParts.get(index + 1));
                double product = operand1 * operand2;
                equationParts.remove(index + 1);
                equationParts.set(index, String.valueOf(product));
                equationParts.remove(index - 1);
            }//end multiply
            else if (equationParts.contains("/")) {
                int index = equationParts.indexOf("/");
                double operand1 = Double.parseDouble(equationParts.get(index - 1));
                double operand2 = Double.parseDouble(equationParts.get(index + 1));
                double product = operand1 / operand2;
                equationParts.remove(index + 1);
                equationParts.set(index, String.valueOf(product));
                equationParts.remove(index - 1);
            }//end divide
            else if (equationParts.contains("+")) {
                int index = equationParts.indexOf("+");
                double operand1 = Double.parseDouble(equationParts.get(index - 1));
                double operand2 = Double.parseDouble(equationParts.get(index + 1));
                double product = operand1 + operand2;
                equationParts.remove(index + 1);
                equationParts.set(index, String.valueOf(product));
                equationParts.remove(index - 1);
            }//end plus
            else if (equationParts.contains("-")) {
                int index = equationParts.indexOf("-");
                double operand1 = Double.parseDouble(equationParts.get(index - 1));
                double operand2 = Double.parseDouble(equationParts.get(index + 1));
                double product = operand1 - operand2;
                equationParts.remove(index + 1);
                equationParts.set(index, String.valueOf(product));
                equationParts.remove(index - 1);
            }//end subtract
        }
        Log.i("equation", String.valueOf(equationParts));
//        answer = getEmojiByUnicode(dead);
        double val = Double.parseDouble(equationParts.get(0));
        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(6);
        answer = format.format(val);
        answerDisplay.setText(answer);
    }
}