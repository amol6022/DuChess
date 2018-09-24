package com.example.amol.duchess;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static ImageButton buttons[];
    private boolean firstClick = true;
    private AlertDialog upgradeAlert;
    public Game game;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttons = new ImageButton[64];
        game = new Game(this);

        fetchButtons();
        nameButtons();
        setListeners();
    }

    private void fetchButtons() {
        int i = 0;
        for (char c1 = '1'; c1 <= '8'; c1++) {
            for (char c2 = 'a'; c2 <= 'h'; c2++) {
                String s = Character.toString(c2) + Character.toString(c1);
                int id = getResources().getIdentifier(s, "id", getPackageName());
                buttons[i] = (ImageButton) findViewById(id);
                buttons[i].setTag(s);
                i++;
            }
        }
    }

    private void nameButtons() {
        int i = 0;
        for (char c1 = '1'; c1 <= '8'; c1++) {
            for (char c2 = 'a'; c2 <= 'h'; c2++) {
                String s = Character.toString(c2) + Character.toString(c1);
                buttons[i].setTag(s);
                i++;
            }
        }
    }

    private void setListeners() {
        for (int i = 0; i < 64; i++) {
            buttons[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(firstClick){
                        if(game.getPossibleMoves(v.getTag().toString())) {
                            showPossibilities();
                            firstClick = false;
                        }
                    }else{
                        game.performActualMove(v.getTag().toString());
                        firstClick=true;
                    }
                }
            });
        }
    }

    private void showPossibilities(){

    }

    public void pawnUpgradeDialog(int pawnColour){
        View dialogView;
        System.out.println("pawnupgradedialog");
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        if(pawnColour==1) {
            dialogView = inflater.inflate(R.layout.whitealert, null);
        }else{
            dialogView = inflater.inflate(R.layout.blackalert, null);
        }

        dialogBuilder.setView(dialogView);

        upgradeAlert=dialogBuilder.create();
        upgradeAlert.show();
    }

    public void getChoice(View v){
        game.upgradePawnTo(v.getTag().toString());
        upgradeAlert.dismiss();
    }

    public static void makeToast(MainActivity activity, String message){
        Toast.makeText(activity,message,Toast.LENGTH_LONG).show();
    }

    public void displayBoard(HashMap<String,Piece> boardMap, MainActivity activity) {
        int i = 0;
        for (char c1 = '1'; c1 <= '8'; c1++) {
            for (char c2 = 'a'; c2 <= 'h'; c2++) {
                String square = Character.toString(c2) + Character.toString(c1);

                if (boardMap.get(square) == null) {
                    if ((c1 + c2) % 2 == 0) {
                        buttons[i].setBackground(activity.getDrawable(R.drawable.square1));
                    } else {
                        buttons[i].setBackground(activity.getDrawable(R.drawable.square2));
                    }
                } else {
                    String pieceColour,squareColour, image;
                    if ((c1 + c2) % 2 == 0) {
                        squareColour = "dark";
                    } else {
                        squareColour = "light";
                    }

                    if(boardMap.get(square).colour==1) pieceColour="white";
                    else pieceColour="black";

                    image = pieceColour + boardMap.get(square).type + squareColour;

                    int id = activity.getResources().getIdentifier(image, "drawable", activity.getPackageName());

                    buttons[i].setBackground(activity.getDrawable(id));
                }

                i++;
            }

        }

        for(i=0;i<64;i++){
            if(game.currentTurn==1) {
                buttons[i].setRotation(180);
            }else {
                buttons[i].setRotation(360);
            }
        }

    }

}
