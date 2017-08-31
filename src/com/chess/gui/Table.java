package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtilities;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Table {

    private JFrame gameFrame;
    private BoardPanel boardPanel;
    private Board chessBoard;
    private Color lightTileColor = Color.decode("#FFCE9E");
    private Color darkTileColor = Color.decode("#D18B47");

    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
    private static String pieceIconPath = "art/pieces/";

    public Table() {
        this.gameFrame = new JFrame("Java Chess");
        this.gameFrame.setLayout(new BorderLayout());

        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);

        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.chessBoard = Board.createStandardBoard();
        this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setVisible(true);
    }

    private JMenuBar createTableMenuBar() {
        JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");

        final JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(e -> System.out.println("open pgn file"));
        fileMenu.add(openPGN);

        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitMenuItem);

        return fileMenu;
    }

    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;

        BoardPanel() {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();

            for (int i = 0; i < BoardUtilities.NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }

            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }
    }

    private class TilePanel extends JPanel {

        private int tileId;

        TilePanel(BoardPanel boardPanel, int tileId) {
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePieceIcon(chessBoard);
            validate();
        }

        private void assignTilePieceIcon(Board board) {
            this.removeAll();

            if (board.getTile(this.tileId).isTileOccupied()) {
                try {
                    BufferedImage image =
                            ImageIO.read(new File(pieceIconPath + board.getTile(this.tileId).getPiece().getPieceColor().toString().substring(0, 1) +
                            board.getTile(this.tileId).getPiece().toString() + ".gif"));
                    this.add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void assignTileColor() {
            if (BoardUtilities.EIGHTH_ROW[this.tileId] ||
                    BoardUtilities.SIXTH_ROW[this.tileId] ||
                    BoardUtilities.FOURTH_ROW[this.tileId] ||
                    BoardUtilities.SECOND_ROW[this.tileId]) {
                if (this.tileId % 2 == 0) {
                    setBackground(lightTileColor);
                } else {
                    setBackground(darkTileColor);
                }
            } else if (BoardUtilities.SEVENTH_ROW[this.tileId] ||
                    BoardUtilities.FIFTH_ROW[this.tileId] ||
                    BoardUtilities.THIRD_ROW[this.tileId] ||
                    BoardUtilities.FIRST_ROW[this.tileId]) {
                if (this.tileId % 2 != 0) {
                    setBackground(lightTileColor);
                } else {
                    setBackground(darkTileColor);
                }
            }
        }

    }

}
