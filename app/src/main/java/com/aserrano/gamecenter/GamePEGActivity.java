package com.aserrano.gamecenter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class GamePEGActivity extends AppCompatActivity {

    private static final String TAG = "PEG_Solitaire";
    private int fields = 7;
    private final int SELECTED = 3;
    private final int GONE = 2;
    private final int PEG = 1;
    private final int INVISIBLE = 0;
    private int[][] map = {
            {2, 2, 1, 1, 1, 2, 2},
            {2, 2, 1, 1, 1, 2, 2},
            {1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 0, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1},
            {2, 2, 1, 1, 1, 2, 2},
            {2, 2, 1, 1, 1, 2, 2}
    };

    private final int[][] aux = new int[fields][fields];

    private boolean clicked;

    private int oldI, oldJ;

    private int pegs;
    private int totalPegs;

    private boolean gameOver;

    private final ImageButton[][] tablero = new ImageButton[fields][fields];

    private TextView scoreText;
    private int scorePEG;
    private int scoreAuxPEG;

    private TextView numPegs;
    private TextView timerUp;
    private Timer timer;
    private TimerTask timerTask;
    private double time = 0.0;

    private String username;

    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peg_game);

        initTablero();

        Bundle extras = getIntent().getExtras();

        username = extras.getString("user");

        db = new Database(this);

    }

    public void initTablero() {

        scoreText = (TextView) findViewById(R.id.scorePeg);
        numPegs = (TextView) findViewById(R.id.numPegs);

        timerUp = (TextView) findViewById(R.id.countUpTimer);
        timer = new Timer();
        startTimer();

        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[0].length; j++) {

                String ImageButtonID = "button_" + i + "_" + j;

                int resID = getResources().getIdentifier(ImageButtonID, "id", getPackageName());

                tablero[i][j] = (ImageButton) findViewById(resID);
            }
        }

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == PEG){
                    totalPegs++;
                }
            }
        }
        numPegs.setText(String.valueOf(totalPegs));
    }

    private void startTimer() {

        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        timerUp.setText(getTimerText());
                        //scoreText.setText(Double.toString(time));
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    private String getTimerText() {

        int rounded = (int) Math.round(time);

        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = ((rounded % 86400) / 3600);

        return formatTime(seconds, minutes, hours);
    }

    @SuppressLint("DefaultLocale")
    private String formatTime(int seconds, int minutes, int hours) {

        return String.format("%02d", hours) + " : " + String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
    }

    public void detectarCoordenada(View view) {

        int i = Character.getNumericValue(view.getTag().toString().charAt(1));
        int j = Character.getNumericValue(view.getTag().toString().charAt(2));


        if (!clicked && map[i][j] == PEG) {
            tablero[i][j].setBackgroundResource(R.drawable.button_peg_clicked);
            oldI = i;
            oldJ = j;
            map[oldI][oldJ] = SELECTED;
            clicked = true;
            //System.out.println("Has clickado: " + oldI + "" + oldJ);

        } else if (clicked && map[i][j] == map[oldI][oldJ]) {
            tablero[i][j].setBackgroundResource(R.drawable.button_peg);
            map[i][j] = PEG;
            clicked = false;
            //System.out.println("Deseleccionar");

        } else if (clicked && map[i][j] == PEG && map[oldI][oldJ] == SELECTED) {

            tablero[oldI][oldJ].setBackgroundResource(R.drawable.button_peg);
            map[oldI][oldJ] = PEG;
            tablero[i][j].setBackgroundResource(R.drawable.button_peg_clicked);
            map[i][j] = SELECTED;
            oldI = i;
            oldJ = j;
            //System.out.println("Cambiar la selección del PEG");

        }else if (clicked && map[i][j] == INVISIBLE) {

            movingRightLeft(i, j, oldI, oldJ);
            movingUpDown(i, j, oldI, oldJ);
            //checkIsWin(pegs);
            System.out.println(pegs);
            if(checkIsWin(pegs) == 1){
                System.out.println("* * * * * * * * * * *");
                Toast.makeText(getApplicationContext(), "**** IS WIN ****", Toast.LENGTH_SHORT).show();
                db.updateScorePEG(username, scorePEG);
                Intent intent = new Intent(GamePEGActivity.this, WinnerPEGActivity.class);
                intent.putExtra("user", username);
                startActivity(intent);
            }else if(gameOver()){
                System.out.println("******************");
                Intent intent = new Intent(GamePEGActivity.this, GameOverPEGActivity.class);
                intent.putExtra("user", username);
                db.updateScorePEG(username, scorePEG);
                startActivity(intent);
            }
        }
    }

    public void movingRightLeft(int i, int j, int oldI, int oldJ) {

        if (i == oldI) {
            if ((oldJ - j) == 2 || (oldJ - j) == -2) {
                //System.out.println("Has comido una pelota");
                if ((oldJ - j) == -2) { // RIGHT / DERECHA
                    copyAux();
                    tablero[i][j].setBackgroundResource(R.drawable.button_peg);
                    map[i][j] = PEG;
                    tablero[oldI][oldJ].setBackgroundResource(R.drawable.button_invisible);
                    map[oldI][oldJ] = INVISIBLE;
                    tablero[i][j - 1].setBackgroundResource(R.drawable.button_invisible);
                    map[i][j - 1] = INVISIBLE;
                    clicked = false;
                    updatePeg();

                } else if ((oldJ - j) == 2) { // LEFT/ IZQUIERDA
                    copyAux();
                    tablero[i][j].setBackgroundResource(R.drawable.button_peg);
                    map[i][j] = PEG;
                    tablero[oldI][oldJ].setBackgroundResource(R.drawable.button_invisible);
                    map[oldI][oldJ] = INVISIBLE;
                    tablero[i][j + 1].setBackgroundResource(R.drawable.button_invisible);
                    map[i][j + 1] = INVISIBLE;
                    clicked = false;
                    updatePeg();
                }
            }
        }else{

        }
    }

    public void movingUpDown(int i, int j, int oldI, int oldJ) {

        if (j == oldJ) {
            if ((oldI - i == 2) || (oldI - i) == -2) {
                //System.out.println("Has comido una pelota");
                if ((oldI - i) == 2) { // UP
                    copyAux();
                    tablero[i][j].setBackgroundResource(R.drawable.button_peg);
                    map[i][j] = PEG;
                    tablero[oldI][oldJ].setBackgroundResource(R.drawable.button_invisible);
                    map[oldI][oldJ] = INVISIBLE;
                    tablero[i + 1][j].setBackgroundResource(R.drawable.button_invisible);
                    map[i + 1][j] = INVISIBLE;
                    clicked = false;
                    updatePeg();
                } else if ((oldI - i) == -2) { // DOWN
                    copyAux();
                    tablero[i][j].setBackgroundResource(R.drawable.button_peg);
                    map[i][j] = PEG;
                    tablero[oldI][oldJ].setBackgroundResource(R.drawable.button_invisible);
                    map[oldI][oldJ] = INVISIBLE;
                    tablero[i - 1][j].setBackgroundResource(R.drawable.button_invisible);
                    map[i - 1][j] = INVISIBLE;
                    clicked = false;
                    updatePeg();
                }
            }
        }else{
        }
    }

    private void updatePeg() {
        totalPegs--;
        numPegs.setText(String.valueOf(totalPegs));

        if(((int) time) > 60){
            scorePEG += 29;
        }else if (((int) time) > 50){
            scorePEG += 37;
        }else if (((int) time) > 40){
            scorePEG += 43;
        }else if (((int) time) > 30){
            scorePEG += 57;
        }else if (((int) time) > 20){
            scorePEG += 76;
        }else if (((int) time) > 10) {
            scorePEG += 99;
        }else if (((int) time) > 5){
            scorePEG += 110;
        }else{
            scorePEG += 120;
        }

        scoreText.setText(String.valueOf(scorePEG));
    }

    public void resetTablero(View view) {
        System.out.println("Reseteando el tablero");
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[0].length; j++) {
                tablero[i][j].setBackgroundResource(R.drawable.button_peg);
                map[i][j] = PEG;
            }
        }
        tablero[3][3].setBackgroundResource(R.drawable.button_invisible);
        map[3][3] = INVISIBLE;

        map[0][0] = GONE;
        map[0][1] = GONE;
        map[1][0] = GONE;
        map[1][1] = GONE;
        map[0][5] = GONE;
        map[0][6] = GONE;
        map[1][5] = GONE;
        map[1][6] = GONE;
        map[5][0] = GONE;
        map[5][1] = GONE;
        map[6][0] = GONE;
        map[6][1] = GONE;
        map[5][5] = GONE;
        map[5][6] = GONE;
        map[6][5] = GONE;
        map[6][6] = GONE;
        pegs = 0;
        clicked = false;

        totalPegs = 0;
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[0].length; j++) {

                if (map[i][j] == PEG) {
                    totalPegs++;
                }
                numPegs.setText(String.valueOf(totalPegs));
            }
        }

        timerTask.cancel();
        time = 0.0;
        timerUp.setText(formatTime(0, 0, 0));
        startTimer();

        scorePEG = 0;
        scoreText.setText(String.valueOf(scorePEG));
    }

    public void goToMenu(View view) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(GamePEGActivity.this);

        alertBuilder.setTitle("EXIT");
        alertBuilder.setMessage("Are you sure you want to go out?");
        alertBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(GamePEGActivity.this, MenuActivity.class);
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

    public void copyAux() {

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                aux[i][j] = map[i][j];
            }
        }
        scoreAuxPEG = scorePEG;
    }

    public void undoMovement(View view) {
        System.out.println("UNDO MOVEMENT");

        for (int i = 0; i < aux.length; i++) {
            for (int j = 0; j < aux[0].length; j++) {
                map[i][j] = aux[i][j];

                if (map[i][j] == 1 || map[i][j] == 3) {
                    tablero[i][j].setBackgroundResource(R.drawable.button_peg);
                    map[i][j] = 1;
                } else if (map[i][j] == 0) {
                    tablero[i][j].setBackgroundResource(R.drawable.button_invisible);
                }
            }
        }
        totalPegs++;
        numPegs.setText(String.valueOf(totalPegs));
        clicked = false;
        scoreText.setText(String.valueOf(scoreAuxPEG));
    }

    private int checkIsWin(int numpeg) {

        numpeg = 0;
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[0].length; j++) {

                if (map[i][j] == PEG) {
                    numpeg++;
                }
            }
        }
        System.out.println("\nTotalPEGS = " + numpeg + "\n");

        return numpeg;
    }

    public boolean gameOver() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {

                if (map[i][j] == PEG) {
                    if (i != 6 && i != 0) {
                        if (map[i - 1][j] == PEG) {
                            if (map[i + 1][j] == INVISIBLE) {
                                System.out.println("Puede seguir jugando 1");
                                gameOver = false;
                                return gameOver;
                            }
                        }
                    }
                    if (i != 6 && i != 0) {
                        if (map[i + 1][j] == PEG) {
                            if (map[i - 1][j] == INVISIBLE) {
                                System.out.println("Puede seguir jugando 2");
                                gameOver = false;
                                return gameOver;
                            }
                        }
                    }
                    if (j != 6 && j != 0) {
                        if (map[i][j - 1] == PEG) {
                            if (map[i][j + 1] == INVISIBLE) {
                                System.out.println("Puede seguir jugando 3");
                                gameOver = false;
                                return gameOver;
                            }
                        }
                    }
                    if (j != 6 && j != 0) {
                        if (map[i][j + 1] == PEG) {
                            if (map[i][j - 1] == INVISIBLE) {
                                System.out.println("Puede seguir jugando 4");
                                gameOver = false;
                                return gameOver;
                            }
                        }
                    }
                }
            }
        }
        gameOver = true;
        System.out.println(gameOver);
        return gameOver;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(GamePEGActivity.this);

        alertBuilder.setTitle("EXIT");
        alertBuilder.setMessage("Are you sure you want to go out?");
        alertBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(GamePEGActivity.this, MenuActivity.class);
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