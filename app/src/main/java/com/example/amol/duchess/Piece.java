package com.example.amol.duchess;

import java.util.ArrayList;

/**
 * Created by Amol on 8/9/2018.
 */

abstract class Piece{
    String type;
    int colour;
    Game game;

    protected abstract ArrayList<String> getPossibleMoves();

    protected abstract boolean moveIsSpecial(String source,String target);

    protected abstract boolean performSpecialMove(String source,String target);

    protected abstract void undoSpecialMove(String initialPos,String finalPos);

    protected abstract boolean canCaptureKing(String source, String target);

}
