package com.nouman.calculator;

import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    List<String> expression = new ArrayList<>();
    Boolean isEvaluated = false;
    double lastResult = 0;
    Map<String, String> buttonLabelToExpression = new HashMap<>();
    Map<String, String> buttonLabelToDisplay = new HashMap<>();
    int currentCursorPosition = 0;
    int currentCursorDisplayState = 0;

    public double evalExpression(String expression) {
        double result = 0;
        try {
            // replace Ans with last result
            expression = expression.replace("Ans", String.valueOf(lastResult));

            Expression e = new ExpressionBuilder(expression).build();
            result = e.evaluate();
        } catch (Exception ex) {
            result =  Double.NaN;
        }
        lastResult = result;
        return result;
    }

    private void evaluateCurrentExpression(View view) {
        String expressionString = expressionListToEvaluatable(expression);
        Log.d("Expression", expressionString);
        double result = evalExpression(expressionString);
        TextView screen = findViewById(R.id.screen_output);
        screen.setText(String.valueOf(result));
        onButtonPressGeneral(view, true);
        isEvaluated = true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ViewGroup rootView = findViewById(R.id.main);
        addHapticFeedbackToButtons(rootView);

        buttonLabelToExpression.put("x", "*");
        buttonLabelToExpression.put("÷", "/");
        buttonLabelToExpression.put("sin", "sin(");
        buttonLabelToExpression.put("cos", "cos(");
        buttonLabelToExpression.put("tan", "tan(");
        buttonLabelToExpression.put("Log", "log(");
        buttonLabelToExpression.put("Ln", "log2(");
        buttonLabelToExpression.put("◼⁄◼", "/");
        buttonLabelToExpression.put("√◼", "sqrt(");
        buttonLabelToExpression.put("x²", "^2");
        buttonLabelToExpression.put("x▀", "^(");
        buttonLabelToExpression.put("x⁻¹", "^(-1)");


        buttonLabelToDisplay.put("sin", "sin(");
        buttonLabelToDisplay.put("cos", "cos(");
        buttonLabelToDisplay.put("tan", "tan(");
        buttonLabelToDisplay.put("Log", "log(");
        buttonLabelToDisplay.put("Ln", "ln(");
        buttonLabelToDisplay.put("◼⁄◼", "⁄");
        buttonLabelToDisplay.put("√◼", "√(");
        buttonLabelToDisplay.put("x²", "²");
        buttonLabelToDisplay.put("x▀", "^(");
        buttonLabelToDisplay.put("x⁻¹", "⁻¹");


        findViewById(R.id.button_0).setOnClickListener(this::OnBasicButtonPressed);
        findViewById(R.id.button_1).setOnClickListener(this::OnBasicButtonPressed);
        findViewById(R.id.button_2).setOnClickListener(this::OnBasicButtonPressed);
        findViewById(R.id.button_3).setOnClickListener(this::OnBasicButtonPressed);
        findViewById(R.id.button_4).setOnClickListener(this::OnBasicButtonPressed);
        findViewById(R.id.button_5).setOnClickListener(this::OnBasicButtonPressed);
        findViewById(R.id.button_6).setOnClickListener(this::OnBasicButtonPressed);
        findViewById(R.id.button_7).setOnClickListener(this::OnBasicButtonPressed);
        findViewById(R.id.button_8).setOnClickListener(this::OnBasicButtonPressed);
        findViewById(R.id.button_9).setOnClickListener(this::OnBasicButtonPressed);
        findViewById(R.id.button_dot).setOnClickListener(this::OnBasicButtonPressed);
        findViewById(R.id.button_subtract).setOnClickListener(this::OnBasicButtonPressed);
        findViewById(R.id.button_add).setOnClickListener(this::OnBasicButtonPressed);
        findViewById(R.id.button_multiply).setOnClickListener(this::OnBasicButtonPressed);
        findViewById(R.id.button_divide).setOnClickListener(this::OnBasicButtonPressed);
        findViewById(R.id.button_delete).setOnClickListener(this::onDelButtonPressed);
        findViewById(R.id.button_clear).setOnClickListener(this::onClearButtonPressed);
        findViewById(R.id.button_ans).setOnClickListener(this::OnBasicButtonPressed);
        findViewById(R.id.button_open_parenthesis).setOnClickListener(this::OnBasicButtonPressed);
        findViewById(R.id.button_close_parenthesis).setOnClickListener(this::OnBasicButtonPressed);
        findViewById(R.id.button_left_arrow).setOnClickListener(this::onArrowButtonPressed);
        findViewById(R.id.button_right_arrow).setOnClickListener(this::onArrowButtonPressed);
        findViewById(R.id.button_sin).setOnClickListener(this::OnBasicButtonPressed);
        findViewById(R.id.button_cos).setOnClickListener(this::OnBasicButtonPressed);
        findViewById(R.id.button_tan).setOnClickListener(this::OnBasicButtonPressed);
        findViewById(R.id.button_log).setOnClickListener(this::OnBasicButtonPressed);
        findViewById(R.id.button_ln).setOnClickListener(this::OnBasicButtonPressed);
        findViewById(R.id.button_fancy_divide).setOnClickListener(this::OnBasicButtonPressed);
        findViewById(R.id.button_square_root).setOnClickListener(this::OnBasicButtonPressed);
        findViewById(R.id.button_square).setOnClickListener(this::OnBasicButtonPressed);
        findViewById(R.id.button_pow).setOnClickListener(this::OnBasicButtonPressed);
        findViewById(R.id.button_inverse).setOnClickListener(this::OnBasicButtonPressed);
        findViewById(R.id.button_mode_scam).setOnClickListener(this::onModeScamButtonPressed);


        findViewById(R.id.button_evaluate).setOnClickListener(this::evaluateCurrentExpression);

        startExpressionDisplay();
    }

    private String expressionListToDisplay(List<String> expression) {
        StringBuilder labelBuilder = new StringBuilder();
        for (String eachExpression : expression) {
            String text = buttonLabelToDisplay.getOrDefault(eachExpression, eachExpression);
            labelBuilder.append(text);
        }
        return labelBuilder.toString();
    }

    private String expressionListToEvaluatable(List<String> expression) {
        StringBuilder expressionBuilder = new StringBuilder();
        for (String eachExpression : expression) {
            String text = buttonLabelToExpression.getOrDefault(eachExpression, eachExpression);
            expressionBuilder.append(text);
        }
        return expressionBuilder.toString();
    }

    private void setCursorDisplay(Boolean showCursor){
        TextView cursor_screen = findViewById(R.id.cursor_display);
        StringBuilder cursorText = new StringBuilder();
        int i = 0;
        for (String eachExpression : expression) {
            if (currentCursorPosition == i && showCursor) {
                cursorText.append("|");
            }
            String realText = buttonLabelToDisplay.getOrDefault(eachExpression, eachExpression);
            for (int j = 0; j < realText.length(); j++) {
                cursorText.append(" ");
            }
            i++;
        }
        if (currentCursorPosition == i && showCursor) {
            cursorText.append("|");
        }
        cursor_screen.setText(cursorText.toString());
    }

    private void startExpressionDisplay() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleWithFixedDelay(() -> {
            if (currentCursorDisplayState == 0) {
                setCursorDisplay(true);
                currentCursorDisplayState = 1;
            } else {
                setCursorDisplay(false);
                currentCursorDisplayState = 0;
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
    }

    private void addHapticFeedbackToButtons(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof Button) {
                child.setOnClickListener(v -> {
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                });
            } else if (child instanceof ViewGroup) {
                addHapticFeedbackToButtons((ViewGroup) child);
            }
        }
    }

    private void performHaptic(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
    }

    private void updateScreen() {
        // Update the screen with the expression
        TextView screen = findViewById(R.id.screen_input);
        screen.setText(expressionListToDisplay(expression));
        setCursorDisplay(true);
    }



    private void onButtonPressGeneral(View view, Boolean isEvaluating) {
        performHaptic(view);
        updateScreen();
        if (isEvaluated && !isEvaluating) {
            TextView screen = findViewById(R.id.screen_output);
            screen.setText("");
            isEvaluated = false;
        }
    }

    private void onModeScamButtonPressed(View view) {
        if (isEvaluated) {
            int result = (int) lastResult;
            TextView screen = findViewById(R.id.screen_output);
            screen.setText(String.valueOf(result));
            onButtonPressGeneral(view, true);
        }
    }

    private void OnBasicButtonPressed(View view) {
        Button button = (Button) view;
        expression.add(currentCursorPosition, (button.getText().toString()));
        currentCursorPosition++;
        onButtonPressGeneral(view, false);
    }
    private void onDelButtonPressed(View view) {
        if (expression.size() > 0) {
            expression.remove(currentCursorPosition - 1);
            currentCursorPosition--;
            onButtonPressGeneral(view, false);
        }
    }
    private void onClearButtonPressed(View view) {
        expression.clear();
        currentCursorPosition = 0;
        onButtonPressGeneral(view, false);
    }

    private void onArrowButtonPressed(View view) {
        // check which button was pressed
        Button button = (Button) view;
        if (button.getText().toString().equals("◀")) {
            if (currentCursorPosition > 0) {
                currentCursorPosition--;
            }
        } else {
            if (currentCursorPosition < expression.size()) {
                currentCursorPosition++;
            }
        }
        onButtonPressGeneral(view, false);
    }
}
