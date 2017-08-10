package com.chess.engine.pieces;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtilities;
import com.chess.engine.board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {

    // List of valid moves based on a board with tiles from 0-63 for Pawn
    private final static int[] POTENTIAL_MOVES = {7, 8, 9, 16};

    public Pawn(final int piecePosition, final Color pieceColor) {
        super(piecePosition, pieceColor, PieceType.PAWN);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final int potentialCoordinate : POTENTIAL_MOVES) {

            final int potentialDestinationCoordinate = this.piecePosition + (this.pieceColor.getDirection() * potentialCoordinate);

            if (!BoardUtilities.isValidTileCoordinate(potentialDestinationCoordinate)) {
                continue;
            }

            if (potentialCoordinate == 8 && !board.getTile(potentialDestinationCoordinate).isTileOccupied()) {
                legalMoves.add(new Move.MajorMove(board, this, potentialDestinationCoordinate));
            } else if (potentialCoordinate == 16 && this.isFirstMove() &&
                    (BoardUtilities.SECOND_ROW[this.piecePosition] && this.pieceColor.isBlack()) ||
                    (BoardUtilities.SEVENTH_ROW[this.piecePosition] && this.pieceColor.isWhite())) {
                final int behindPotentialDestinationCoordinate = this.piecePosition + (this.pieceColor.getDirection() * 8);
                if (!board.getTile(behindPotentialDestinationCoordinate).isTileOccupied() &&
                        !board.getTile(potentialDestinationCoordinate).isTileOccupied()) {
                    legalMoves.add(new Move.MajorMove(board, this, potentialDestinationCoordinate));
                }
            } else if (potentialCoordinate == 7 &&
                    !((BoardUtilities.EIGHTH_COLUMN[this.piecePosition] && this.pieceColor.isWhite() ||
                            (BoardUtilities.FIRST_COLUMN[this.piecePosition] && this.pieceColor.isBlack())))) {
                if (board.getTile(potentialDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnPoint = board.getTile(potentialDestinationCoordinate).getPiece();
                    if (this.pieceColor != pieceOnPoint.getPieceColor()) {
                        // add an attack move
                        legalMoves.add(new Move.MajorMove(board, this, potentialDestinationCoordinate));
                    }
                }

            } else if (potentialCoordinate == 9 &&
                    !((BoardUtilities.EIGHTH_COLUMN[this.piecePosition] && this.pieceColor.isBlack() ||
                            (BoardUtilities.FIRST_COLUMN[this.piecePosition] && this.pieceColor.isWhite())))) {
                if (board.getTile(potentialDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnPoint = board.getTile(potentialDestinationCoordinate).getPiece();
                    if (this.pieceColor != pieceOnPoint.getPieceColor()) {
                        // add an attack move
                        legalMoves.add(new Move.MajorMove(board, this, potentialDestinationCoordinate));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor());
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }
}
