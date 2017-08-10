package com.chess.engine.board;

import com.chess.engine.Color;
import com.chess.engine.pieces.*;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.*;

public class Board {

    private final List<Tile> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;

    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;

    private Board(Builder builder) {
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard, Color.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, Color.BLACK);

        final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);

        this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);

    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < BoardUtilities.NUM_TILES; i++) {
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", tileText));
            if ((i + 1) % BoardUtilities.NUM_TILES_PER_ROW == 0) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    public Player whitePlayer() {
        return this.whitePlayer;
    }

    public Player currentPlayer() {
        return this.currentPlayer;
    }

    public Player blackPlayer() {
        return this.blackPlayer;
    }

    public Collection<Piece> getBlackPieces() {
        return this.blackPieces;
    }

    public Collection<Piece> getWhitePieces() {
        return this.whitePieces;
    }

    private Collection<Move> calculateLegalMoves(Collection<Piece> pieces) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final Piece piece : pieces) {
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return ImmutableList.copyOf(legalMoves);
    }

    private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Color color) {
        final List<Piece> activePieces = new ArrayList<>();
        for (final Tile tile : gameBoard) {
            if (tile.isTileOccupied()) {
                final Piece piece = tile.getPiece();
                if (piece.getPieceColor() == color) {
                    activePieces.add(piece);
                }
            }
        }
        return ImmutableList.copyOf(activePieces);
    }

    public Tile getTile(final int tileCoordinate) {
        return gameBoard.get(tileCoordinate);
    }

    // Populates a list of tiles numbered 0-63 to represent the game board
    private static List<Tile> createGameBoard(final Builder builder) {
        final Tile[] tiles = new Tile[BoardUtilities.NUM_TILES];
        for (int i = 0; i < BoardUtilities.NUM_TILES; i++) {
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }

    public static Board createStandardBoard() {
        final Builder builder = new Builder();

        // Black pieces
        builder.setPiece(new Rook(0, Color.BLACK));
        builder.setPiece(new Knight(1, Color.BLACK));
        builder.setPiece(new Bishop(2, Color.BLACK));
        builder.setPiece(new Queen(3, Color.BLACK));
        builder.setPiece(new King(4, Color.BLACK));
        builder.setPiece(new Bishop(5, Color.BLACK));
        builder.setPiece(new Knight(6, Color.BLACK));
        builder.setPiece(new Rook(7, Color.BLACK));
        builder.setPiece(new Pawn(8, Color.BLACK));
        builder.setPiece(new Pawn(9, Color.BLACK));
        builder.setPiece(new Pawn(10, Color.BLACK));
        builder.setPiece(new Pawn(11, Color.BLACK));
        builder.setPiece(new Pawn(12, Color.BLACK));
        builder.setPiece(new Pawn(13, Color.BLACK));
        builder.setPiece(new Pawn(14, Color.BLACK));
        builder.setPiece(new Pawn(15, Color.BLACK));

        // White pieces
        builder.setPiece(new Rook(63, Color.WHITE));
        builder.setPiece(new Knight(62, Color.WHITE));
        builder.setPiece(new Bishop(61, Color.WHITE));
        builder.setPiece(new Queen(60, Color.WHITE));
        builder.setPiece(new King(59, Color.WHITE));
        builder.setPiece(new Bishop(58, Color.WHITE));
        builder.setPiece(new Knight(57, Color.WHITE));
        builder.setPiece(new Rook(56, Color.WHITE));
        builder.setPiece(new Pawn(55, Color.WHITE));
        builder.setPiece(new Pawn(54, Color.WHITE));
        builder.setPiece(new Pawn(53, Color.WHITE));
        builder.setPiece(new Pawn(52, Color.WHITE));
        builder.setPiece(new Pawn(51, Color.WHITE));
        builder.setPiece(new Pawn(50, Color.WHITE));
        builder.setPiece(new Pawn(49, Color.WHITE));
        builder.setPiece(new Pawn(48, Color.WHITE));

        // White moves first
        builder.setMoveMaker(Color.WHITE);

        return builder.build();
    }

    public Iterable<Move> getAllLegalMoves() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(), this.blackPlayer.getLegalMoves()));
    }

    public static class Builder {

        Map<Integer, Piece> boardConfig;
        Color nextMoveMaker;

        public Builder() {
            this.boardConfig = new HashMap<>();
        }

        public Builder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }

        public Builder setMoveMaker(final Color color) {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public Board build() {
            return new Board(this);
        }

    }
}
