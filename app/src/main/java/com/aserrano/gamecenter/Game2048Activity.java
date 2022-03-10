package com.aserrano.gamecenter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Game2048Activity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private static final String TAG = "2048";
    private float x1, x2, y1, y2;
    private static final int MIN_DISTANCE = 250;
    private int fields = 4;
    private boolean gameOver = false;
    private boolean canSwipe;

    private TextView tScore;
    private int score;
    private int auxScore;

    private GestureDetector gestureDetector;

    private TextView[][] tablero;

    private String[][] aux = new String[fields][fields];

    private Database db;

    private String username;

    public int getScore() {
        return score;
    }

    public int getAuxScore() {
        return auxScore;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2048_game);

        this.gestureDetector = new GestureDetector(Game2048Activity.this, this);
        this.tablero = new TextView[fields][fields];
        tScore = (TextView) findViewById(R.id.score2048);
        db = new Database(this);

        initTablero();
        randomSwipe();
        changeColor();
        copyAux();
        Bundle extras = getIntent().getExtras();

        username = extras.getString("user");

    }

    public void initTablero() {

        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[0].length; j++) {
                String TextViewID = "text_" + i + "_" + j;

                int resID = getResources().getIdentifier(TextViewID, "id", getPackageName());

                tablero[i][j] = findViewById(resID);
            }
        }

    }

    public void randomSwipe() {
        if (comprobarZero()) {
            Random random = new Random();
            int randomFila = random.nextInt(4);
            int randomColumna = random.nextInt(4);


            while (!tablero[randomFila][randomColumna].getText().equals("")) {

                randomFila = random.nextInt(4);
                randomColumna = random.nextInt(4);
            }

            tablero[randomFila][randomColumna].setText("2");

            int random4 = random.nextInt(11);

            if (random4 > 9) {
                tablero[randomFila][randomColumna].setText("4");
            }
        }
    }

    public boolean comprobarZero() {

        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[0].length; j++) {

                if (tablero[i][j].getText().equals("")) {
                    return true;
                }
            }
        }
        return false;
    }

    public void resetTablero(View view) {

        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[0].length; j++) {

                tablero[i][j].setText("");

            }
        }
        randomSwipe();
        changeColor();
        score = 0;
        updateScore();
    }

    public void copyAux() {

        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[0].length; j++) {
                aux[i][j] = String.valueOf(tablero[i][j].getText());
            }
        }

        auxScore = getScore();

    }

    public void undoMovement(View view) {

        System.out.println("UNDO MOVEMENT");

        for (int i = 0; i < aux.length; i++) {
            for (int j = 0; j < aux[0].length; j++) {
                tablero[i][j].setText(aux[i][j]);
            }
        }
        changeColor();
        tScore.setText(String.valueOf(this.getAuxScore()));
        score = getAuxScore();

    }

    public void goToMenu(View view) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(Game2048Activity.this);

        alertBuilder.setTitle("EXIT");
        alertBuilder.setMessage("Are you sure you want to go out?");
        alertBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Game2048Activity.this, MenuActivity.class);
                intent.putExtra("user", username);
                startActivity(intent);
            }
        });
        alertBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertBuilder.show();
    }

    public void updateScore() {

        tScore.setText(String.valueOf(this.getScore()));
    }

    public void swipeRight(int fila) {

        for (int z = 0; z < 4; z++) {

            for (int j = 3; j > 0; j--) {

                // si text_0_3 es 0
                if (tablero[fila][j].getText().equals("")) {

                    // pillamos el numero de la casilla text_0_2
                    String aux = (String) tablero[fila][j - 1].getText();

                    // lo escribimos en la casilla 0_3
                    tablero[fila][j].setText(aux);

                    //borramos el numero de la casilla 0_2
                    tablero[fila][j - 1].setText("");

                }
            }
        }
    }

    public void sumaRight(int fila) {

        for (int j = 3; j > 0; j--) {

            if (tablero[fila][j].getText().equals(tablero[fila][j - 1].getText())) {
                if ((tablero[fila][j].getText()).equals("") || (tablero[fila][j - 1].getText().equals(""))) {
                    System.out.println("No se puede sumar");
                } else {

                    String aux = (String) tablero[fila][j].getText();

                    int suma = Integer.parseInt(aux) * 2;

                    String resultado = Integer.toString(suma);

                    this.score += suma;

                    updateScore();

                    tablero[fila][j].setText(resultado);

                    tablero[fila][j - 1].setText("");

                }
            }

            canSwipe = true;

        }
        canSwipe = false;

    }

    public void swipeLeft(int fila) {

        for (int z = 0; z < 4; z++) {

            for (int j = 0; j < 3; j++) {

                if (tablero[fila][j].getText().equals("")) {

                    String aux = (String) tablero[fila][j + 1].getText();

                    tablero[fila][j].setText(aux);

                    tablero[fila][j + 1].setText("");

                }
            }
        }
    }

    public void sumaLeft(int fila) {

        for (int j = 0; j < 3; j++) {

            if (tablero[fila][j].getText().equals(tablero[fila][j + 1].getText())) {

                if ((tablero[fila][j].getText()).equals("") || (tablero[fila][j + 1].getText().equals(""))) {
                    System.out.println("No se puede sumar");
                } else {

                    String aux = (String) tablero[fila][j].getText();

                    int suma = Integer.parseInt(aux) * 2;

                    String resultado = Integer.toString(suma);

                    this.score += suma;

                    updateScore();

                    tablero[fila][j].setText(resultado);

                    tablero[fila][j + 1].setText("");
                }
            }

            canSwipe = true;
        }

        canSwipe = false;
    }

    public void swipeTop(int columna) {

        for (int z = 0; z < 4; z++) {

            for (int i = 0; i < 3; i++) {

                if (tablero[i][columna].getText().equals("")) {

                    String aux = (String) tablero[i + 1][columna].getText();

                    tablero[i][columna].setText(aux);

                    tablero[i + 1][columna].setText("");
                }
            }
        }
    }

    public void sumaTop(int columna) {

        for (int i = 0; i < 3; i++) {

            if (tablero[i][columna].getText().equals(tablero[i + 1][columna].getText())) {
                if ((tablero[i][columna].getText()).equals("") || (tablero[i+1][columna].getText().equals(""))) {
                    System.out.println("No se puede sumar");
                }else {

                    String aux = (String) tablero[i][columna].getText();

                    int suma = Integer.parseInt(aux) * 2;

                    String resultado = Integer.toString(suma);

                    this.score += suma;

                    updateScore();

                    tablero[i][columna].setText(resultado);

                    tablero[i + 1][columna].setText("");
                }
            }

            canSwipe = true;
        }

        canSwipe = false;
    }

    public void swipeBottom(int columna) {

        for (int z = 0; z < 4; z++) {

            for (int i = 3; i > 0; i--) {

                if (tablero[i][columna].getText().equals("")) {

                    String aux = (String) tablero[i - 1][columna].getText();

                    tablero[i][columna].setText(aux);

                    tablero[i - 1][columna].setText("");
                }
            }
        }
    }

    public void sumaBottom(int columna) {

        for (int i = 3; i > 0; i--) {

            if (tablero[i][columna].getText().equals(tablero[i - 1][columna].getText())) {
                if ((tablero[i][columna].getText()).equals("") || (tablero[i - 1][columna].getText().equals(""))) {
                    System.out.println("No se puede sumar");
                } else {

                    String aux = (String) tablero[i][columna].getText();

                    int suma = Integer.parseInt(aux) * 2;

                    String resultado = Integer.toString(suma);

                    this.score += suma;

                    updateScore();

                    tablero[i][columna].setText(resultado);

                    tablero[i - 1][columna].setText("");
                }
            }

            canSwipe = false;
        }

        canSwipe = true;
    }

    public void swipeAndSumRight() {
        if (canSwipeRight()) {
            copyAux();
            for (int i = 0; i < 4; i++) {
                swipeRight(i);
                sumaRight(i);
                swipeRight(i);
            }
            randomSwipe();
        }
    }

    public void swipeAndSumLeft() {
        if (canSwipeLeft()) {
            copyAux();
            for (int i = 0; i < 4; i++) {
                swipeLeft(i);
                sumaLeft(i);
                swipeLeft(i);
            }
            randomSwipe();
        }
    }

    public void swipeAndSumTop() {
        if (canSwipeTop()) {
            copyAux();
            for (int i = 0; i < 4; i++) {
                swipeTop(i);
                sumaTop(i);
                swipeTop(i);
            }
            randomSwipe();
        }
    }

    public void swipeAndSumBottom() {
        if (canSwipeBottom()) {
            copyAux();
            for (int i = 0; i < 4; i++) {
                swipeBottom(i);
                sumaBottom(i);
                swipeBottom(i);
            }
            randomSwipe();
        }
    }

    public void changeColor() {

        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[0].length; j++) {

                if (tablero[i][j].getText().equals("")) {
                    tablero[i][j].setBackground(getResources().getDrawable(R.color.textViewColor));

                } else if (tablero[i][j].getText().equals("2")) {
                    tablero[i][j].setBackground(getResources().getDrawable(R.color.color_2));

                } else if (tablero[i][j].getText().equals("4")) {
                    tablero[i][j].setBackground(getResources().getDrawable(R.color.color_4));

                } else if (tablero[i][j].getText().equals("8")) {
                    tablero[i][j].setBackground(getResources().getDrawable(R.color.color_8));

                } else if (tablero[i][j].getText().equals("16")) {
                    tablero[i][j].setBackground(getResources().getDrawable(R.color.color_16));

                } else if (tablero[i][j].getText().equals("32")) {
                    tablero[i][j].setBackground(getResources().getDrawable(R.color.color_32));

                } else if (tablero[i][j].getText().equals("64")) {
                    tablero[i][j].setBackground(getResources().getDrawable(R.color.color_64));

                } else if (tablero[i][j].getText().equals("128")) {
                    tablero[i][j].setBackground(getResources().getDrawable(R.color.color_128));

                } else if (tablero[i][j].getText().equals("256")) {
                    tablero[i][j].setBackground(getResources().getDrawable(R.color.color_256));

                } else if (tablero[i][j].getText().equals("512")) {
                    tablero[i][j].setBackground(getResources().getDrawable(R.color.color_512));

                } else if (tablero[i][j].getText().equals("1024")) {
                    tablero[i][j].setBackground(getResources().getDrawable(R.color.color_1024));

                } else if (tablero[i][j].getText().equals("2048")) {
                    tablero[i][j].setBackground(getResources().getDrawable(R.color.color_2048));

                } else if (tablero[i][j].getText().equals("4096")) {
                    tablero[i][j].setBackground(getResources().getDrawable(R.color.color_4096));

                } else if (tablero[i][j].getText().equals("8192")) {
                    tablero[i][j].setBackground(getResources().getDrawable(R.color.color_8192));
                }
            }
        }
    }

    public void checkGameOver() {

        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[0].length; j++) {

                if (canSwipeTop() || canSwipeRight() || canSwipeBottom() || canSwipeLeft()) {
                    System.out.println("Puedes seguir jugando");

                } else if (!canSwipeTop() && !canSwipeRight() && !canSwipeBottom() && !canSwipeLeft()) {
                    System.out.println("Has perdido");
                    db.updateScore2048(username, score);
                    Intent intent = new Intent(Game2048Activity.this, GameOverScreen.class);
                    intent.putExtra("user", username);
                    startActivity(intent);
                }

            }
        }
    }

    public void checkWin() {
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[0].length; j++) {
                if (tablero[i][j].getText() == "2048") {
                    System.out.println("HAS GANADOO");
                    db.updateScore2048(username, score);
                    Intent intent = new Intent(Game2048Activity.this, Winner2048Activity.class);
                    intent.putExtra("user", username);
                    startActivity(intent);
                }
            }
        }
    }

    public boolean canSwipeTop() {

        for (int columna = 0; columna < tablero.length; columna++) {

            for (int i = 0; i < tablero[0].length - 1; i++) {

                if (tablero[i][columna].getText().equals("") && !(tablero[i + 1][columna].getText().equals(""))) {
                    return true;
                } else if (tablero[i][columna].getText().equals(tablero[i + 1][columna].getText()) &&
                        !(tablero[i][columna].getText().equals(""))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canSwipeBottom() {

        for (int columna = 0; columna < tablero.length; columna++) {

            for (int i = 3; i > 0; i--) {

                if (tablero[i][columna].getText().equals("") && !(tablero[i - 1][columna].getText().equals(""))) {
                    return true;
                } else if (tablero[i][columna].getText().equals(tablero[i - 1][columna].getText()) &&
                        !(tablero[i][columna].getText().equals(""))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canSwipeRight() {

        for (int fila = 0; fila < tablero.length; fila++) {

            for (int j = 3; j > 0; j--) {

                // si text_0_3 es 0
                if (tablero[fila][j].getText().equals("") && !(tablero[fila][j - 1].getText().equals(""))) {
                    return true;

                } else if (tablero[fila][j].getText().equals(tablero[fila][j - 1].getText()) &&
                        !(tablero[fila][j].getText().equals(""))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canSwipeLeft() {
        for (int fila = 0; fila < tablero.length; fila++) {

            for (int j = 0; j < 3; j++) {

                if (tablero[fila][j].getText().equals("") && !(tablero[fila][j + 1].getText().equals(""))) {

                    return true;
                } else if (tablero[fila][j].getText().equals(tablero[fila][j + 1].getText()) &&
                        !(tablero[fila][j].getText().equals(""))) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        gestureDetector.onTouchEvent(event);

        switch (event.getAction()) {
            // starting to swipe time gesture
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            // ending time swipe gesture
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();

                // getting value for horizontal swipe
                float valueX = x2 - x1;

                // getting value for vertical swipe
                float valueY = y2 - y1;

                if (Math.abs(valueX) > MIN_DISTANCE) {
                    // detect left to right swipe
                    if (x2 > x1) {
                        swipeAndSumRight();
                        comprobarZero();
                        changeColor();
                        checkWin();
                        checkGameOver();
                        //Log.d(TAG, "Right swipe");
                    } else {
                        // detect right to left swipe
                        swipeAndSumLeft();
                        comprobarZero();
                        changeColor();
                        checkWin();
                        checkGameOver();
                        //Log.d(TAG, "Left swipe");
                    }
                } else if (Math.abs(valueY) > MIN_DISTANCE) {
                    //detect top to bottom swipe
                    if (y2 > y1) {
                        swipeAndSumBottom();
                        comprobarZero();
                        changeColor();
                        checkWin();
                        checkGameOver();
                        //Log.d(TAG, "Bottom swipe");
                    } else {
                        swipeAndSumTop();
                        comprobarZero();
                        changeColor();
                        checkWin();
                        checkGameOver();
                        //Log.d(TAG, "Up swipe");
                    }
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(Game2048Activity.this);

        alertBuilder.setTitle("EXIT");
        alertBuilder.setMessage("Are you sure you want to go out?");
        alertBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Game2048Activity.this, MenuActivity.class);
                intent.putExtra("user", username);
                startActivity(intent);
            }
        });
        alertBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertBuilder.show();
    }
}