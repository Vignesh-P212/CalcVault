package com.example.calcvault;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.calcvault.R;

public class MainActivity extends AppCompatActivity {

    private TextView tvInput;
    private String currentInput = "";
    private String operator = "";
    private double firstValue = 0;
    private boolean isNewInput = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvInput = findViewById(R.id.tvInput);
    }

    public void onNumberClick(View view) {
        if (isNewInput) {
            tvInput.setText("");
            isNewInput = false;
        }
        currentInput += ((Button) view).getText().toString();
        tvInput.setText(currentInput);
    }

    public void onOperatorClick(View view) {
        if (!currentInput.isEmpty()) {
            firstValue = Double.parseDouble(currentInput);
            operator = ((Button) view).getText().toString();
            isNewInput = true;
        }
    }

    public void onEqualClick(View view) {
        if (!currentInput.isEmpty()) {
            double secondValue = Double.parseDouble(currentInput);
            double result = 0;

            switch (operator) {
                case "+": result = firstValue + secondValue; break;
                case "-": result = firstValue - secondValue; break;
                case "×": result = firstValue * secondValue; break;
                case "÷": result = firstValue / secondValue; break;
            }

            tvInput.setText(String.valueOf(result));
            currentInput = String.valueOf(result);
            isNewInput = true;
        }
    }

    public void onClearClick(View view) {
        currentInput = "";
        tvInput.setText("0");
        isNewInput = true;
    }

    public void onDecimalClick(View view) {
        if (!currentInput.contains(".")) {
            currentInput += ".";
            tvInput.setText(currentInput);
        }
    }

    public void onToggleSignClick(View view) {
        if (!currentInput.isEmpty()) {
            double value = Double.parseDouble(currentInput);
            value *= -1;
            currentInput = String.valueOf(value);
            tvInput.setText(currentInput);
        }
    }

    public void onPercentageClick(View view) {
        if (!currentInput.isEmpty()) {
            double value = Double.parseDouble(currentInput);
            value /= 100;
            currentInput = String.valueOf(value);
            tvInput.setText(currentInput);
        }
    }
}
