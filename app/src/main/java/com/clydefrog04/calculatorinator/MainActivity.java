//https://stackoverflow.com/questions/26893796/how-to-set-emoji-by-unicode-in-a-textview
//https://en.wikipedia.org/wiki/Emoji#Unicode_blocks
package com.clydefrog04.calculatorinator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        //init vars
        answerDisplay = findViewById(R.id.answerDisplay);
        numbers = new ArrayList<>();
        operators = new ArrayList<>();

        answerDisplay.setText(answer);
        answerDisplay.setTextIsSelectable(false);
        answerDisplay.setFocusableInTouchMode(false);
    }

    public String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    private boolean canAddDot() {
        int lastPlus = answer.lastIndexOf("+");
        int lastminus = answer.lastIndexOf("-");
        int lastmult = answer.lastIndexOf("*");
        int lastDiv = answer.lastIndexOf("/");
        int lastplusMin = Math.max(lastPlus, lastminus);
        int lastMultDiv = Math.max(lastmult, lastDiv);
        int checkIndex = Math.max(lastplusMin, lastMultDiv);
        int lastDot = answer.lastIndexOf(".");
        return lastDot < checkIndex;
    }

    public void digitBtnHandler(View view) {
        Button btn = (Button) view;
        String appendText = btn.getText().toString();
        Log.i("info", appendText);
        Log.i("info", "btn id:" + view.getId());
        Log.i("info", String.valueOf(view.getWidth()));

        try {
            switch (view.getId()) {
                case R.id.period:
                    if (!answer.contains(".")) {
                        answer += appendText;
                    } else if (canAddDot()) {
                        answer += ".";
                    }
                    break;
                case R.id.clear:
                    answer = "";
                    break;
                case R.id.solveBtn:
                    if (isLastCharOperator()) break;
                    solveProblem(answer.replaceAll(",", ""));
                    break;
                case R.id.minusBtn://minus handled differently to allow negative input
                    if(!isLastCharOperator()){
                        answer += appendText;
                    }
                    break;
                case R.id.plusBtn:
                case R.id.multiplyBtn:
                case R.id.divideBtn:
                    if (isLastCharOperator() || answer.length() == 0) {
                        break;
                    }
                    //intentional fallthrough
                default:
                    Log.i("debug", "default called");
                    answer += appendText;
                    break;
            }
            answerDisplay.setText(answer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isLastCharOperator() {
        if (answer.length() <= 0)//this check is necessary since the lastChar expects there to be some length
            return false;
        char lastChar = answer.charAt(answer.length() - 1);
        return (lastChar == '+' ||
                lastChar == '-' ||
                lastChar == '*' ||
                lastChar == '/');
    }

    private void solveProblem(String equation) {
//        Log.i("info", R.string.)
        String pattern = "((?<=[/*\\-+])|(?=[/*\\-+]))";
        ArrayList<String> equationParts = new ArrayList<>(Arrays.asList(equation.split(pattern)));

        //first check if first input was negative, and handle accordingly
        if(equationParts.get(0).equals("-")){
            double newFirstIndex = Double.parseDouble(equationParts.get(1)) * -1;
            equationParts.remove(0);
            equationParts.set(0, String.valueOf(newFirstIndex));
        }

        if (equationParts.size() <= 2)
            return;//prevents a crash if the equation is something like 5+
        while (equationParts.size() > 1) {
            if (equationParts.contains("*")) {
                int index = equationParts.indexOf("*");//todo break these into one function call
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