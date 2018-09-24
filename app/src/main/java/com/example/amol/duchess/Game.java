package com.example.amol.duchess;

import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Amol on 8/9/2018.
 */

public class Game{
    int currentTurn=1;
    Piece allPieces[];
    String initialPos=null;
    private MainActivity activity;
    HashMap<String,Piece> SquareToPieceMap;
    HashMap<Piece,String> PieceToSquareMap;
    private ArrayList<String> currentPossibilities=null;
    private ArrayList <HashMap<String,Piece>> gameRecord;

    Game(MainActivity activity){
        this.activity=activity;

        initializeAllPieces();
        initializeMaps();

        gameRecord=new ArrayList<>();
        addCopy();
    }

    private void addCopy(){
        HashMap<String,Piece> tempMap=new HashMap<>();

        for(char c1='1';c1<='8';c1++){
            for(char c2='a';c2<='h';c2++){
                String id=Character.toString(c2)+Character.toString(c1);
                tempMap.put(id,SquareToPieceMap.get(id));
            }
        }

        gameRecord.add(tempMap);

    }

    private void initializeAllPieces(){
        allPieces=new Piece[32];

        allPieces[0]=new Rook(this,"rook",1);
        allPieces[7]=new Rook(this,"rook",1);

        allPieces[1]=new Knight(this,"knight",1);
        allPieces[6]=new Knight(this,"knight",1);

        allPieces[2]=new Bishop(this,"bishop",1);
        allPieces[5]=new Bishop(this,"bishop",1);

        allPieces[3]=new Queen(this,"queen",1);
        allPieces[4]=new King(this,"king",1);

        for(int i=8;i<16;i++){
            allPieces[i]=new Pawn(this,"pawn",1);
        }

        for(int i=16;i<24;i++){
            allPieces[i]=new Pawn(this,"pawn",-1);
        }

        allPieces[24]=new Rook(this,"rook",-1);
        allPieces[31]=new Rook(this,"rook",-1);

        allPieces[25]=new Knight(this,"knight",-1);
        allPieces[30]=new Knight(this,"knight",-1);

        allPieces[26]=new Bishop(this,"bishop",-1);
        allPieces[29]=new Bishop(this,"bishop",-1);

        allPieces[27]=new Queen(this,"queen",-1);
        allPieces[28]=new King(this,"king",-1);
    }

    private void initializeMaps(){

        SquareToPieceMap=new HashMap<>();
        PieceToSquareMap=new HashMap<>();

        int i=0;

        for(char c1='1';c1<='2';c1++){
            for(char c2='a';c2<='h';c2++){
                String id=Character.toString(c2)+Character.toString(c1);
                SquareToPieceMap.put(id,allPieces[i]);
                PieceToSquareMap.put(allPieces[i],id);
                i++;
            }
        }

        for(char c1='3';c1<='6';c1++){
            for(char c2='a';c2<='h';c2++){
                String id= Character.toString(c2)+Character.toString(c1);
                SquareToPieceMap.put(id,null);
            }
        }

        for(char c1='7';c1<='8';c1++){
            for(char c2='a';c2<='h';c2++){
                String id=Character.toString(c2)+Character.toString(c1);
                SquareToPieceMap.put(id,allPieces[i]);
                PieceToSquareMap.put(allPieces[i],id);
                i++;
            }
        }


    }

    private boolean turnIsOrdered(String square){
        if(currentTurn==SquareToPieceMap.get(square).colour)
            return true;

        return false;
    }

    boolean getPossibleMoves(String square){
        if(SquareToPieceMap.get(square)!=null) {
            if(turnIsOrdered(square)) {
                initialPos = square;
                currentPossibilities = SquareToPieceMap.get(square).getPossibleMoves();
                discardInvalidPossibilities();
                return true;
            }else{
                MainActivity.makeToast(activity,"Opponent's Move");
                return false;
            }
        }else{
            MainActivity.makeToast(activity,"Select a piece to move!");
            return false;
        }
    }

    private void discardInvalidPossibilities(){
        Piece chosenPiece=SquareToPieceMap.get(initialPos);
        String finalPos;

        for(int i=0;i<currentPossibilities.size();i++){
            finalPos=currentPossibilities.get(i);

            if(SquareToPieceMap.get(finalPos)==null){

                if(chosenPiece.moveIsSpecial(initialPos,finalPos)){
                    boolean success=chosenPiece.performSpecialMove(initialPos,finalPos);

                    if(checkForCheck(currentTurn)){
                        currentPossibilities.remove(i);
                        i--;
                    }

                    if(success) chosenPiece.undoSpecialMove(initialPos,finalPos);
                }else{
                    move(initialPos,finalPos);

                    if(checkForCheck(currentTurn)){
                        currentPossibilities.remove(i);
                        i--;
                    }

                    undoMove(finalPos,initialPos);
                }

            }else{

                if(chosenPiece.moveIsSpecial(initialPos,finalPos)){
                   boolean success=chosenPiece.performSpecialMove(initialPos,finalPos);

                    if(checkForCheck(currentTurn)){
                        currentPossibilities.remove(i);
                        i--;
                    }

                    if(success) chosenPiece.undoSpecialMove(initialPos,finalPos);
                }else{
                    Piece capturedPiece=capture(initialPos,finalPos);

                    if(checkForCheck(currentTurn)){
                        currentPossibilities.remove(i);
                        i--;
                    }

                    undoCapture(capturedPiece,initialPos,finalPos);
                }

            }
        }
    }

    public boolean checkForCheck(int kingColour) {
        String kingPosition;
        int start, end;

        if (kingColour == 1) {
            kingPosition = PieceToSquareMap.get(allPieces[4]);
            start = 16;
            end = 32;
        } else {
            kingPosition = PieceToSquareMap.get(allPieces[28]);
            start = 0;
            end = 16;
        }

        for (int i = start; i < end; i++) {
            if (PieceToSquareMap.get(allPieces[i])!=null && allPieces[i].canCaptureKing(PieceToSquareMap.get(allPieces[i]), kingPosition)) {
                return true;
            }
        }

        return false;
    }

    public void move(String source, String target){
        Piece piece=SquareToPieceMap.get(source);

        SquareToPieceMap.put(source,null);
        SquareToPieceMap.put(target,piece);

        PieceToSquareMap.put(piece,target);
    }

    public void undoMove(String source, String target){
        move(source,target);
    }

    private Piece capture(String source, String target){
        Piece killerPiece=SquareToPieceMap.get(source);
        Piece victimPiece=SquareToPieceMap.get(target);

        SquareToPieceMap.put(source,null);
        SquareToPieceMap.put(target,killerPiece);

        PieceToSquareMap.put(killerPiece,target);
        PieceToSquareMap.put(victimPiece,null);

        return victimPiece;
    }

    private void undoCapture(Piece capturedPiece,String initialPos,String finalPos){
        Piece killerPiece=SquareToPieceMap.get(finalPos);

        SquareToPieceMap.put(finalPos,capturedPiece);
        SquareToPieceMap.put(initialPos,killerPiece);

        PieceToSquareMap.put(killerPiece,initialPos);
        PieceToSquareMap.put(capturedPiece,finalPos);
    }

    private boolean moveAllowed(String chosenSquare){
        for(int i=0;i<currentPossibilities.size();i++){
            if(currentPossibilities.get(i).equals(chosenSquare)){
                return true;
            }
        }
        return false;
    }

    void performActualMove(String chosenSquare){
        if(chosenSquare.equals(initialPos)){
            initialPos=null;
            currentPossibilities=null;
            MainActivity.makeToast(activity,"Choose another square to move!");
            return;
        }

        if(moveAllowed(chosenSquare)){
            Piece chosenPiece=SquareToPieceMap.get(initialPos);

            if(SquareToPieceMap.get(chosenSquare)==null){

                if(chosenPiece.moveIsSpecial(initialPos,chosenSquare)){
                    chosenPiece.performSpecialMove(initialPos,chosenSquare);
                }else{
                    move(initialPos,chosenSquare);
                }

            }else{

                if(chosenPiece.moveIsSpecial(initialPos,chosenSquare)){
                    chosenPiece.performSpecialMove(initialPos,chosenSquare);
                }else {
                    capture(initialPos, chosenSquare);
                }

            }

            if(chosenPiece.type.equals("king") || chosenPiece.type.equals("rook") || chosenPiece.type.equals("pawn")) {
                specialNeeds(chosenPiece, initialPos, chosenSquare);
            }

            disableOpponentsEnPassantVictim(-1*currentTurn);

            activity.displayBoard(SquareToPieceMap,activity);

            currentTurn=-1*currentTurn;
            initialPos=null;
            currentPossibilities=null;

            if(checkForCheck(currentTurn) && checkForMate(currentTurn)){
                MainActivity.makeToast(activity,"Checkmate!");
                activity.finish();
            }else if(!checkForCheck(currentTurn) && checkForMate(currentTurn)){
                MainActivity.makeToast(activity,"Draw by Stalemate!");
                activity.finish();
            }else if(checkForCheck(currentTurn) && !checkForMate(currentTurn)){
                MainActivity.makeToast(activity,"Check!");
            }

            addCopy();

            if(checkForDraw()){
                MainActivity.makeToast(activity,"Game Drawn!");
                activity.finish();
            }                                   //undo//offer draw//quit game//add activities//online//chess engine.
            currentPossibilities=null;

        }else{
            initialPos=null;
            currentPossibilities=null;
            MainActivity.makeToast(activity,"Invalid Move!");
        }
    }

    private boolean checkForDraw(){
        /*if(gameRecord.size()==50){
            MainActivity.makeToast(activity,"No Pawn Moves or Captures for 50 moves!");
            return true;
        }*/

        int count=0;
        for(int i=0;i<gameRecord.size()-1;i++){
            if(count==2){
                MainActivity.makeToast(activity,"Identical Board Position repeated thrice!");
                return true;
            }

            if(gameRecord.get(gameRecord.size()-1).equals(gameRecord.get(i))){
                count++;
            }
        }

        return false;
    }

    private void specialNeeds(Piece chosenPiece,String source,String target){
        if(chosenPiece.type.equals("king")){
            King king=(King)chosenPiece;
            if(!king.hasMoved){
                king.hasMoved=true;
            }
        }else if(chosenPiece.type.equals("rook")){
            Rook rook=(Rook)chosenPiece;
            if(!rook.hasMoved){
                rook.hasMoved=true;
            }
        }else if(chosenPiece.type.equals("pawn")){
            Pawn pawn=(Pawn)chosenPiece;

            char startRow=source.charAt(1);
            char endRow=target.charAt(1);

            if((endRow-startRow)==(2*chosenPiece.colour)){
                pawn.potentialVictimOfEnPassant=true;
            }

            if(chosenPiece.colour==1 && target.contains("8")){
                activity.pawnUpgradeDialog(chosenPiece.colour);
            }else if(chosenPiece.colour==-1 && target.contains("1")){
                activity.pawnUpgradeDialog(chosenPiece.colour);
            }

        }

    }

    private void disableOpponentsEnPassantVictim(int opponentColour){
        int start,end;

        if(opponentColour==1){
            start=8;
            end=16;
        }else{
            start=16;
            end=24;
        }

        for(int i=start;i<end;i++){
            if(allPieces[i].type.equals("pawn")){
                Pawn pawn=(Pawn)allPieces[i];
                if(pawn.potentialVictimOfEnPassant){
                    pawn.potentialVictimOfEnPassant=false;
                    break;
                }
            }
        }
    }

    private boolean checkForMate(int opponentColour){
        int start, end;

        if (opponentColour == 1) {
            start = 0;
            end = 16;
        } else {
            start = 16;
            end = 32;
        }

        for (int i = start; i < end; i++) {
            if(PieceToSquareMap.get(allPieces[i])!=null)
                getPossibleMoves(PieceToSquareMap.get(allPieces[i]));

            if (currentPossibilities.size() != 0){
                initialPos=null;
                currentPossibilities=null;
                return false;
            }
        }
        initialPos=null;
        currentPossibilities=null;
        return true;
    }

    void upgradePawnTo(String pieceType){
        Piece piece=null;

        if(pieceType.contains("white")){
            if(pieceType.contains("queen")){
                piece=new Queen(this,"queen",1);
            }else if(pieceType.contains("rook")){
                piece=new Queen(this,"rook",1);
            }else if(pieceType.contains("knight")){
                piece=new Queen(this,"knight",1);
            }else if(pieceType.contains("bishop")){
                piece=new Queen(this,"bishop",1);
            }
        }else if(pieceType.contains("black")){
            if(pieceType.contains("queen")){
                piece=new Queen(this,"queen",-1);
            }else if(pieceType.contains("rook")){
                piece=new Queen(this,"rook",-1);
            }else if(pieceType.contains("knight")){
                piece=new Queen(this,"knight",-1);
            }else if(pieceType.contains("bishop")){
                piece=new Queen(this,"bishop",-1);
            }
        }

        for(int i=8;i<24;i++){
            Pawn pawn=(Pawn)allPieces[i];
            if(pawn.colour==currentTurn && PieceToSquareMap.get(pawn)!=null && (PieceToSquareMap.get(pawn).contains("1") || PieceToSquareMap.get(pawn).contains("8"))){
                SquareToPieceMap.put(PieceToSquareMap.get(pawn),piece);
                PieceToSquareMap.put(piece,PieceToSquareMap.get(pawn));
                PieceToSquareMap.put(pawn,null);
                break;
            }
        }
    }

}