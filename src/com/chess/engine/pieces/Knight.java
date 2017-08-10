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

public class Knight extends Piece {

    // List of valid moves based on a board with tiles from 0-63 for Knight
    private final static int[] POTENTIAL_MOVES = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(int piecePosition, Color pieceColor) {
        super(piecePosition, pieceColor, PieceType.KNIGHT);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        // For loop to check if each item in Potential Moves is a valid move

        for (final int currentOption : POTENTIAL_MOVES) {

            // Checks piece position and adds or subtracts from the current item in potential moves
            int potentialDestinationCoordinate = this.piecePosition + currentOption;


            if (BoardUtilities.isValidTileCoordinate(potentialDestinationCoordinate)) {

                // If it's a valid tile (not out of bounds), get the tile and store it

                if (isFirstColumnExclusion(this.piecePosition, currentOption) ||
                        isSecondColumnExclusion(this.piecePosition, currentOption) ||
                        isSeventhColumnExclusion(this.piecePosition, currentOption) ||
                        isEighthColumnExclusion(this.piecePosition, currentOption)) {
                    continue;
                }
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
    public Knight movePiece(Move move) {
        return new Knight(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor());
    }

    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }

    // Sets up the column exceptions for when the move option doesn't work

    private static boolean isFirstColumnExclusion(final int currentPosition, final int positionOption) {
        return BoardUtilities.FIRST_COLUMN[currentPosition] && (positionOption == -17 || positionOption == -10 ||
                positionOption == 6 || positionOption == 15);
    }

    private static boolean isSecondColumnExclusion(final int currentPosition, final int positionOption) {
        return BoardUtilities.SECOND_COLUMN[currentPosition] && (positionOption == -10 || positionOption == 6);
    }

    private static boolean isSeventhColumnExclusion(final int currentPosition, final int positionOption) {
        return BoardUtilities.SEVENTH_COLUMN[currentPosition] && (positionOption == 10 || positionOption == -6);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int positionOption) {
        return BoardUtilities.EIGHTH_COLUMN[currentPosition] && ((positionOption == 17) || positionOption == 10 ||
                positionOption == -6 || positionOption == -15);
    }
}
