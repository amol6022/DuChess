package com.example.amol.duchess;

import java.util.ArrayList;

/**
 * Created by Amol on 8/10/2018.
 */

public class King extends Piece {
    public boolean hasMoved=false;

    King(Game game, String type, int colour){
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
                }else if(game.SquareToPieceMap.get(id)!=null && game.SquareToPieceMap.get(id).colour!=game.SquareToPieceMap.get(game.initialPos).colour
                && !game.SquareToPieceMap.get(id).type.equals("king") && validMove(game.initialPos,id)){
                    list.add(id);
                }
            }
        }

        return list;
    }

    private boolean validMove(String source,String target){
        char startRow=source.charAt(1);
        char endRow=target.charAt(1);
        char startCol=source.charAt(0);
        char endCol=target.charAt(0);

        if(endRow==startRow && (endCol==startCol+1 || endCol==startCol-1)){
            return true;
        }else if(endRow==startRow+1 && (endCol==startCol || endCol==startCol+1 || endCol==startCol-1)){
            return true;
        }else if(endRow==startRow-1 && (endCol==startCol || endCol==startCol+1 || endCol==startCol-1)){
            return true;
        }else if(moveIsSpecial(source,target)){
            return true;
        }
        return false;

    }

    protected boolean moveIsSpecial(String source,String target){
        char startRow=source.charAt(1);
        char endRow=target.charAt(1);
        char startCol=source.charAt(0);
        char endCol=target.charAt(0);


        if(this.colour==1 && endRow==startRow && endCol==startCol+2){
            King king=(King)game.allPieces[4];Rook rook=(Rook)game.allPieces[7];
            if(!king.hasMoved && !rook.hasMoved){
                return true;
            }
        }else if(this.colour==1 && endRow==startRow && endCol==startCol-2){
            King king=(King)game.allPieces[4];Rook rook=(Rook)game.allPieces[0];
            if(!king.hasMoved && !rook.hasMoved){
                return true;
            }
        }else if(this.colour==-1 && endRow==startRow && endCol==startCol+2){
            King king=(King)game.allPieces[28];Rook rook=(Rook)game.allPieces[31];
            if(!king.hasMoved && !rook.hasMoved){
                return true;
            }
        }else if(this.colour==-1 && endRow==startRow && endCol==startCol-2){
            King king=(King)game.allPieces[28];Rook rook=(Rook)game.allPieces[24];
            if(!king.hasMoved && !rook.hasMoved){
                return true;
            }
        }
        return false;
    }

    protected boolean performSpecialMove(String source,String target){
        if(game.checkForCheck(game.currentTurn)){
            return false;
        }

        char startCol=source.charAt(0);
        char endCol=target.charAt(0);
        char row=source.charAt(1);

        char rookFinalCol=(char)((startCol+endCol)/2);

        String tempPos=Character.toString(rookFinalCol)+Character.toString(row);

        game.move(source,tempPos);

        if(game.checkForCheck(game.currentTurn)){
            game.undoMove(tempPos,source);
            return false;
        }

        game.move(tempPos,target);

        String rookInitialPos=null;

        if(rookFinalCol-3=='a'){
            rookInitialPos=Character.toString('a')+Character.toString(row);
        }else if(rookFinalCol+2=='h'){
            rookInitialPos=Character.toString('h')+Character.toString(row);
        }
        System.out.println("Castling, rookInit "+rookInitialPos+" rookfinal "+tempPos);
        game.move(rookInitialPos,tempPos);
        System.out.println("Castling type of piece"+game.SquareToPieceMap.get(tempPos).type+" on "+tempPos);
        return true;
    }

    protected void undoSpecialMove(String initialPos,String finalPos){
        char startCol=initialPos.charAt(0);
        char endCol=finalPos.charAt(0);
        char row=initialPos.charAt(1);

        char rookFinalCol=(char)((startCol+endCol)/2);

        String tempPos=Character.toString(rookFinalCol)+Character.toString(row);

        game.move(finalPos,initialPos);

        String rookInitialPos=null;
        if(rookFinalCol-3=='a'){
            rookInitialPos=Character.toString('a')+Character.toString(row);
        }else if(rookFinalCol+2=='h'){
            rookInitialPos=Character.toString('h')+Character.toString(row);
        }

        game.move(tempPos,rookInitialPos);
    }

    protected boolean canCaptureKing(String source, String target){
        if (game.SquareToPieceMap.get(target) != null && game.SquareToPieceMap.get(target).colour != game.SquareToPieceMap.get(source).colour
                && validMove(source, target)){
            return true;
        }
        return false;
    }

}
