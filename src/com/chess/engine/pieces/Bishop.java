package com.chess.engine.pieces;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtilities;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Bishop extends Piece {

    // List of valid moves based on a board with tiles from 0-63 for Bishop
    private final static int[] POTENTIAL_MOVE_VECTORS = {-9, -7, 7, 9};

    public Bishop(int piecePosition, Color pieceColor) {
        super(piecePosition, pieceColor, PieceType.BISHOP);
    }


    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final int potentialCoordinate : POTENTIAL_MOVE_VECTORS) {
            int potentialDestinationCoordinate = this.piecePosition;
            while (BoardUtilities.isValidTileCoordinate(potentialDestinationCoordinate)) {
                if (isFirstColumnExclusion(potentialDestinationCoordinate, potentialCoordinate) ||
                        isEighthColumnExclusion(potentialDestinationCoordinate, potentialCoordinate)) {
                    break;
                }
                potentialDestinationCoordinate += potentialCoordinate;
                if (BoardUtilities.isValidTileCoordinate(potentialDestinationCoordinate)) {
                    final Tile potentialDestinationTile = board.getTile(potentialDestinationCoordinate);
                    if (!potentialDestinationTile.isTileOccupied()) {
                        // If the tile is not occupied, add it to legal moves
                        legalMoves.add(new Move.MajorMove(board, this, potentialDestinationCoordinate));
                    } else {
                        // If the tile is occupied, check to see if it's the same color, if no then it's a valid move
                        final Piece pieceAtDestination = potentialDestinationTile.getPiece();
                        final Color destinationPieceColor = pieceAtDestination.getPieceColor();
                        if (this.pieceColor != destinationPieceColor) {
                            legalMoves.add(new Move.AttackMove(board, this, potentialDestinationCoordinate, pieceAtDestination));
                        }
                        break;
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Bishop movePiece(Move move) {
        return new Bishop(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor());
    }


    @Override
    public String toString() {
        return PieceType.BISHOP.toString();
    }

    // Sets up the column exceptions for when the move option doesn't work

    private static boolean isFirstColumnExclusion(final int currentPosition, final int positionOption) {
        return BoardUtilities.FIRST_COLUMN[currentPosition] && (positionOption == -9 || positionOption == 7);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int positionOption) {
        return BoardUtilities.EIGHTH_COLUMN[currentPosition] && (positionOption == 9 || positionOption == -7);
    }
}
