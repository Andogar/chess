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

public class King extends Piece {

    private final static int[] POTENTIAL_MOVES = {-9, -8, -7, -1, 1, 7, 8, 9};

    public King(int piecePosition, Color pieceColor) {
        super(piecePosition, pieceColor, PieceType.KING);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {

        final List<Move> legalMoves = new ArrayList<>();
        for (final int potentialCoordinate : POTENTIAL_MOVES) {
            if (isFirstColumnExclusion(this.piecePosition, potentialCoordinate) ||
                    isEighthColumnExclusion(this.piecePosition, potentialCoordinate)) {
                continue;
            }
            final int potentialDestinationCoordinate = this.piecePosition + potentialCoordinate;
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
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public King movePiece(Move move) {
        return new King(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor());
    }

    @Override
    public String toString() {
        return PieceType.BISHOP.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int positionOption) {
        return BoardUtilities.FIRST_COLUMN[currentPosition] && (positionOption == -9 || positionOption == -1 ||
                positionOption == 7);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int positionOption) {
        return BoardUtilities.SECOND_COLUMN[currentPosition] && (positionOption == 9 || positionOption == 1 ||
                positionOption == -7);
    }
}
