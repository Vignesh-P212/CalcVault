package com.example.calcvault;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private TextView tvInput;
    private String fullExpression = "";
    private boolean isNewInput = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvInput = findViewById(R.id.tvInput);
    }

    public void onNumberClick(View view) {
        if (isNewInput) {
            fullExpression = "";
            isNewInput = false;
        }
        fullExpression += ((Button) view).getText().toString();
        tvInput.setText(fullExpression);
    }

    public void onOperatorClick(View view) {
        String operator = ((Button) view).getText().toString();
        if (!fullExpression.isEmpty() && !isOperator(fullExpression.charAt(fullExpression.length() - 1))) {
            fullExpression += " " + operator + " ";
            tvInput.setText(fullExpression);
            isNewInput = false;
        }
    }

    public void onEqualClick(View view) {
        if (!fullExpression.isEmpty()) {
            try {
                // Replace special symbols before evaluation
                String formattedExpression = fullExpression.replace("×", "*").replace("÷", "/");
                double result = evaluateExpression(formattedExpression);
                tvInput.setText(fullExpression + " = " + formatResult(result));
                fullExpression = formatResult(result);
                isNewInput = true;
            } catch (Exception e) {
                tvInput.setText("Error");
                fullExpression = "";
                isNewInput = true;
            }
        }
    }

    public void onClearClick(View view) {
        fullExpression = "";
        tvInput.setText("0");
        isNewInput = true;
    }

    public void onDecimalClick(View view) {
        if (!fullExpression.isEmpty() && !fullExpression.endsWith(".")) {
            fullExpression += ".";
            tvInput.setText(fullExpression);
        }
    }

    public void onToggleSignClick(View view) {
        if (!fullExpression.isEmpty()) {
            try {
                double value = Double.parseDouble(fullExpression);
                value *= -1;
                fullExpression = String.valueOf(value);
                tvInput.setText(fullExpression);
            } catch (Exception ignored) {
            }
        }
    }

    public void onPercentageClick(View view) {
        if (!fullExpression.isEmpty()) {
            try {
                double value = Double.parseDouble(fullExpression) / 100;
                fullExpression = String.valueOf(value);
                tvInput.setText(fullExpression);
            } catch (Exception ignored) {
            }
        }
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private double evaluateExpression(String expression) {
        String[] tokens = expression.split(" ");
        return evaluatePostfix(infixToPostfix(tokens));
    }

    private String[] infixToPostfix(String[] infix) {
        Stack<String> operators = new Stack<>();
        Stack<String> output = new Stack<>();

        for (String token : infix) {
            if (isNumeric(token)) {
                output.push(token);
            } else if (isOperator(token.charAt(0))) {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(token)) {
                    output.push(operators.pop());
                }
                operators.push(token);
            }
        }

        while (!operators.isEmpty()) {
            output.push(operators.pop());
        }

        return output.toArray(new String[0]);
    }

    private double evaluatePostfix(String[] postfix) {
        Stack<Double> stack = new Stack<>();

        for (String token : postfix) {
            if (isNumeric(token)) {
                stack.push(Double.parseDouble(token));
            } else {
                double b = stack.pop();
                double a = stack.pop();
                stack.push(applyOperator(a, b, token));
            }
        }

        return stack.pop();
    }

    private double applyOperator(double a, double b, String operator) {
        switch (operator) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": return a / b;
        }
        throw new IllegalArgumentException("Invalid operator: " + operator);
    }

    private int precedence(String operator) {
        if (operator.equals("+") || operator.equals("-")) return 1;
        if (operator.equals("*") || operator.equals("/")) return 2;
        return -1;
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    private String formatResult(double result) {
        if (result == (int) result) {
            return String.valueOf((int) result);
        } else {
            return String.valueOf(result);
        }
    }
}
