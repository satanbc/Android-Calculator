package com.lab3.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    private EditText inputField;
    private StringBuilder expression;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputField = findViewById(R.id.inputField);
        expression = new StringBuilder();

        Button button0 = findViewById(R.id.button0);
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        Button button5 = findViewById(R.id.button5);
        Button button6 = findViewById(R.id.button6);
        Button button7 = findViewById(R.id.button7);
        Button button8 = findViewById(R.id.button8);
        Button button9 = findViewById(R.id.button9);

        Button buttonPlus = findViewById(R.id.buttonPlus);
        Button buttonMinus = findViewById(R.id.buttonMinus);
        Button buttonMultiply = findViewById(R.id.buttonMultiply);
        Button buttonDivide = findViewById(R.id.buttonDivide);
        Button buttonEqual = findViewById(R.id.buttonEqual);
        Button buttonClear = findViewById(R.id.buttonClear);

        Button buttonSquareRoot = findViewById(R.id.buttonSquareRoot);
        Button buttonPower = findViewById(R.id.buttonPower);
        Button buttonSin = findViewById(R.id.buttonSin);
        Button buttonCos = findViewById(R.id.buttonCos);
        Button buttonTan = findViewById(R.id.buttonTan);
        Button buttonLog = findViewById(R.id.buttonLog);
        Button buttonLn = findViewById(R.id.buttonLn);
        Button buttonBackspace = findViewById(R.id.buttonBackspace);
        Button buttonDot = findViewById(R.id.buttonDot);
        Button buttonPi = findViewById(R.id.buttonPi);
        Button buttonE = findViewById(R.id.buttonE);
        Button buttonNegative = findViewById(R.id.buttonNegative);

        View.OnClickListener numberListener = v -> {
            Button button = (Button) v;
            int cursorPosition = inputField.getSelectionStart();
            expression.insert(cursorPosition, button.getText());
            inputField.setText(expression.toString());
            inputField.setSelection(cursorPosition + button.getText().length());
        };

        button0.setOnClickListener(numberListener);
        button1.setOnClickListener(numberListener);
        button2.setOnClickListener(numberListener);
        button3.setOnClickListener(numberListener);
        button4.setOnClickListener(numberListener);
        button5.setOnClickListener(numberListener);
        button6.setOnClickListener(numberListener);
        button7.setOnClickListener(numberListener);
        button8.setOnClickListener(numberListener);
        button9.setOnClickListener(numberListener);

        View.OnClickListener operatorListener = v -> {
            Button button = (Button) v;
            int cursorPosition = inputField.getSelectionStart();
            String operator = " " + button.getText() + " ";
            expression.insert(cursorPosition, operator);
            inputField.setText(expression.toString());
            inputField.setSelection(cursorPosition + operator.length());
        };

        buttonPlus.setOnClickListener(operatorListener);
        buttonMinus.setOnClickListener(operatorListener);
        buttonMultiply.setOnClickListener(operatorListener);
        buttonDivide.setOnClickListener(operatorListener);
        buttonPower.setOnClickListener(operatorListener);

        buttonSquareRoot.setOnClickListener(v -> appendFunctionWithParentheses("sqrt"));
        buttonSin.setOnClickListener(v -> appendFunctionWithParentheses("sin"));
        buttonCos.setOnClickListener(v -> appendFunctionWithParentheses("cos"));
        buttonTan.setOnClickListener(v -> appendFunctionWithParentheses("tan"));
        buttonLog.setOnClickListener(v -> appendFunctionWithParentheses("log"));
        buttonLn.setOnClickListener(v -> appendFunctionWithParentheses("ln"));

        buttonDot.setOnClickListener(v -> {
            int cursorPosition = inputField.getSelectionStart();
            if (cursorPosition > 0 && !expression.toString().contains(".")) {
                expression.insert(cursorPosition, ".");
                inputField.setText(expression.toString());
                inputField.setSelection(cursorPosition + 1);
            }
        });

        buttonBackspace.setOnClickListener(v -> {
            int cursorPosition = inputField.getSelectionStart();
            if (cursorPosition > 0) {
                expression.deleteCharAt(cursorPosition - 1);
                inputField.setText(expression.toString());
                inputField.setSelection(cursorPosition - 1);
            }
        });

        buttonEqual.setOnClickListener(v -> {
            String expr = expression.toString();
            try {
                double result = evaluateExpression(expr);
                String formattedResult = (result == (long) result) ? String.valueOf((long) result) : String.valueOf(result);
                inputField.setText(formattedResult);
                expression.setLength(0);
                expression.append(formattedResult);
                inputField.setSelection(expression.length());
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Error in expression", Toast.LENGTH_SHORT).show();
            }
        });

        buttonClear.setOnClickListener(v -> {
            expression.setLength(0);
            inputField.setText("");
        });

        buttonPi.setOnClickListener(v -> {
            int cursorPosition = inputField.getSelectionStart();
            expression.insert(cursorPosition, "pi");
            inputField.setText(expression.toString());
            inputField.setSelection(cursorPosition + 2);
        });

        buttonE.setOnClickListener(v -> {
            int cursorPosition = inputField.getSelectionStart();
            expression.insert(cursorPosition, "e");
            inputField.setText(expression.toString());
            inputField.setSelection(cursorPosition + 1);
        });

        buttonNegative.setOnClickListener(v -> {
            int cursorPosition = inputField.getSelectionStart();
            if (cursorPosition > 0 && expression.charAt(cursorPosition - 1) != '-') {
                expression.insert(cursorPosition, "-");
                inputField.setText(expression.toString());
                inputField.setSelection(cursorPosition + 1);
            } else if (cursorPosition == 0) {
                expression.insert(0, "-");
                inputField.setText(expression.toString());
                inputField.setSelection(1);
            }
        });
    }

    double evaluateExpression(String expression) throws Exception {
        expression = expression.replace("pi", String.valueOf(Math.PI));
        expression = expression.replace("e", String.valueOf(Math.E));

        Expression expr = new ExpressionBuilder(expression)
                .function(new net.objecthunter.exp4j.function.Function("ln", 1) {
                    @Override
                    public double apply(double... args) {
                        return Math.log(args[0]);
                    }
                })
                .function(new net.objecthunter.exp4j.function.Function("log", 1) {
                    @Override
                    public double apply(double... args) {
                        return Math.log10(args[0]);
                    }
                })
                .build();

        return expr.evaluate();
    }

    private void appendFunctionWithParentheses(String function) {
        int cursorPosition = inputField.getSelectionStart();
        expression.insert(cursorPosition, function + "()");
        inputField.setText(expression.toString());
        inputField.post(() -> inputField.setSelection(cursorPosition + function.length() + 1));
    }
}
