package com.example.amol.duchess;

import java.util.ArrayList;

/**
 * Created by Amol on 8/10/2018.
 */

public class Pawn extends Piece {
     boolean potentialVictimOfEnPassant=false;
     private Piece enPassantVictim;

    Pawn(Game game, String type, int colour){
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
                    ///System.out.println("hi pawn inside if");
                    list.add(id);
                }else if((game.SquareToPieceMap.get(id)!=null && game.SquareToPieceMap.get(id).colour != game.SquareToPieceMap.get(game.initialPos).colour && !game.SquareToPieceMap.get(id).type.equals("king")
                && validCapture(game.initialPos,id)) || (game.SquareToPieceMap.get(id)==null && validCapture(game.initialPos,id))){
                    //System.out.println("valid capture");
                    list.add(id);
                }
            }
        }

        return list;
    }

    private boolean validMove(String source,String target){
        char startRow = source.charAt(1);
        char endRow = target.charAt(1);
        char startCol = source.charAt(0);
        char endCol = target.charAt(0);

        if(this.colour==1){
            if(startCol == endCol && endRow == startRow + 1)
                return true;

            else if(startCol == endCol && endRow == startRow + 2 && startRow=='2'){
                String temp=Character.toString(game.initialPos.charAt(0))+Character.toString('3');

                if(game.SquareToPieceMap.get(temp)!=null)
                    return false;

                return true;
            }else{
                return false;
            }

        }else{

            if(startCol == endCol && endRow == startRow - 1)
                return true;

            else if(startCol == endCol && endRow == startRow - 2 && startRow=='7'){
                String temp=Character.toString(game.initialPos.charAt(0))+Character.toString('6');

                if(game.SquareToPieceMap.get(temp)!=null)
                    return false;

                return true;
            }else{
                return false;
            }

        }
    }

    protected boolean validCapture(String source, String target){

        char startRow=source.charAt(1);
        char endRow=target.charAt(1);
        char startCol=source.charAt(0);
        char endCol=target.charAt(0);

        if(game.SquareToPieceMap.get(target)!=null && game.SquareToPieceMap.get(target).colour!=this.colour) {

            if (this.colour==1) {
                if ((endCol == startCol + 1 || endCol == startCol - 1) && endRow == startRow + 1) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if ((endCol == startCol + 1 || endCol == startCol - 1) && endRow == startRow - 1) {
                    return true;
                } else {
                    return false;
                }
            }

        }else if(game.SquareToPieceMap.get(target)==null && moveIsSpecial(source,target)){
            return true;
        }else{
            return false;
        }
    }

    protected boolean canCaptureKing(String source, String target){
        if (validCapture(source,target)){
            return true;
        }
        return false;
    }

    protected boolean moveIsSpecial(String source,String target){
        char startRow=source.charAt(1);
        char endRow=target.charAt(1);
        char startCol=source.charAt(0);
        char endCol=target.charAt(0);
        
        if (this.colour==1) {
            if ((endCol == startCol + 1 || endCol == startCol - 1) && endRow == startRow + 1) {
                String s= Character.toString(endCol)+Character.toString(startRow);
                if(game.SquareToPieceMap.get(s)!=null && game.SquareToPieceMap.get(s).type.equals("pawn") && game.SquareToPieceMap.get(s).colour==-1){
                    Pawn pawn=(Pawn)game.SquareToPieceMap.get(s);
                    if(pawn.potentialVictimOfEnPassant){
                        return true;
                    }
                }
            }
            return false;

        } else {
            if ((endCol == startCol + 1 || endCol == startCol - 1) && endRow == startRow - 1) {
                String s= Character.toString(endCol)+Character.toString(startRow);
                if(game.SquareToPieceMap.get(s)!=null && game.SquareToPieceMap.get(s).type.equals("pawn") && game.SquareToPieceMap.get(s).colour==1){
                    Pawn pawn=(Pawn)game.SquareToPieceMap.get(s);
                    if(pawn.potentialVictimOfEnPassant){
                        return true;
                    }
                }
            }
            return false;
        }
    }

    protected boolean performSpecialMove(String source,String target){
        char startRow=source.charAt(1);
        char endCol=target.charAt(0);

        String victimSquare=Character.toString(endCol)+Character.toString(startRow);

        enPassantVictim=game.SquareToPieceMap.get(victimSquare);

        Piece killerPiece=game.SquareToPieceMap.get(source);

        game.SquareToPieceMap.put(source,null);
        game.SquareToPieceMap.put(victimSquare,null);
        game.SquareToPieceMap.put(target,killerPiece);

        game.PieceToSquareMap.put(killerPiece,target);
        game.PieceToSquareMap.put(enPassantVictim,null);

        return true;

    }

    protected void undoSpecialMove(String initialPos,String finalPos){
        char startRow=initialPos.charAt(1);
        char endCol=finalPos.charAt(0);

        String victimSquare=Character.toString(endCol)+Character.toString(startRow);

        Piece killerPiece=game.SquareToPieceMap.get(finalPos);

        game.SquareToPieceMap.put(finalPos,null);
        game.SquareToPieceMap.put(victimSquare,enPassantVictim);
        game.SquareToPieceMap.put(initialPos,killerPiece);

        game.PieceToSquareMap.put(killerPiece,initialPos);
        game.PieceToSquareMap.put(enPassantVictim,victimSquare);
    }

}
