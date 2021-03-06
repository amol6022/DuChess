package com.example.amol.duchess;

import java.util.ArrayList;

/**
 * Created by Amol on 8/10/2018.
 */

public class Rook extends Piece {

    public boolean hasMoved=false;

    Rook(Game game, String type, int colour){
        this.game=game;
        this.type=type;
        this.colour=colour;
    }

    protected ArrayList<String> getPossibleMoves(){
        ArrayList<String> list=new ArrayList<>();

        for(char c1='1';c1<='8';c1++){
            for(char c2='a';c2<='h';c2++){
                String id=Character.toString(c2)+Character.toString(c1);
                if(game.SquareToPieceMap.get(id)==null && validMove(game.initialPos,id)){
                    list.add(id);
                }else if(game.SquareToPieceMap.get(id)!=null && game.SquareToPieceMap.get(id).colour!=game.SquareToPieceMap.get(game.initialPos).colour &&
                !game.SquareToPieceMap.get(id).type.equals("king") && validMove(game.initialPos,id)){
                    list.add(id);
                }
            }
        }

        return list;
    }

    private boolean validMove(String source, String target){
        char startRow=source.charAt(1);
        char endRow=target.charAt(1);
        char startCol=source.charAt(0);
        char endCol=target.charAt(0);

        if(startRow==endRow){
            if(startCol>endCol){
                char temp=startCol;
                startCol=endCol;
                endCol=temp;
            }

            for(char c=++startCol;c<endCol;c++){
                String temp= Character.toString(c)+Character.toString(startRow);
                if(game.SquareToPieceMap.get(temp)!=null)
                    return false;
            }
            return true;
        }

        else if(startCol==endCol){
            if(startRow>endRow) {
                char temp = startRow;
                startRow = endRow;
                endRow = temp;
            }

            for(char c=++startRow;c<endRow;c++){
                String temp= Character.toString(startCol)+Character.toString(c);
                if(game.SquareToPieceMap.get(temp)!=null)
                    return false;
            }

            return true;
        }

        else {
            return false;
        }
    }

    protected boolean moveIsSpecial(String source,String target) {
        return false;
    }

    protected boolean performSpecialMove(String source,String target){
        return false;
    }

    protected void undoSpecialMove(String initialPos,String finalPos){

    }

    protected boolean canCaptureKing(String source, String target) {
        if (game.SquareToPieceMap.get(target) != null && game.SquareToPieceMap.get(target).colour != game.SquareToPieceMap.get(source).colour
                && validMove(source, target)) {
            return true;
        }
        return false;
    }

}
