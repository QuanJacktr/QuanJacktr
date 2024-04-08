import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MinesweeperGUI{
    private JFrame frame;
    private JButton[][] buttons;
    private final int rows = 10;
    private final int cols = 10;
    private final int numMines = 10;
    private boolean[][] mines;
    private boolean[][] revealed;
    private boolean gameOver;

    public MinesweeperGUI(){
        frame = new JFrame("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(rows, cols));
        buttons = new JButton[rows][cols];
        mines = new boolean[rows][cols];
        revealed = new boolean[rows][cols];
        gameOver = false;
        initializeBoard();
        placeMines();
        createButtons();
        frame.pack();
        frame.setVisible(true);
    }

    private void initializeBoard(){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                mines[i][j] =false;
                revealed[i][j] = false;
            }
        }
    }

    private void placeMines(){
        int minesPlaced = 0;
        while(minesPlaced < numMines){
            int row = (int) (Math.random() * rows);
            int col = (int) (Math.random() * cols);
            if(!mines[row][col]){
                mines[row][col] = true;
                minesPlaced++;
            }
        }
    }

    private void createButtons() {
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(30, 30));
                button.setFocusPainted(false);
                int row = i;
                int col = j;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e){
                        if(!gameOver && !revealed[row][col]){
                            revealCell(row, col);
                        }
                    }
                }); 
                buttons[i][j] = button;
                frame.add(button);
            }
        }
    }

    private void revealCell(int row, int col){
        if(mines[row][col]){
            buttons[row][col].setText("*");
            gameOver = true;
            revealAllMines();
        }else{
            int count = countAdjacentMines(row, col);
            buttons[row][col].setText(String.valueOf(count));
            revealed[row][col] = true;
            if(count == 0){
                revealAdjacentCells(row, col);
            }
            checkWin();
        }
    }

    private int countAdjacentMines(int row, int col){
        int count = 0;
        for(int dr = -1; dr <= 1; dr++){
            for(int dc = -1; dc <= 1; dc++){
                int r = row + dr;
                int c = col + dc;
                if(r >= 0 && r < rows && c >= 0 && c < cols && mines[r][c]){
                    count++;
                }
            }
        }
        return count;
    }

    private void revealAdjacentCells(int row, int col){
        for(int dr = -1; dr <= 1; dr++){
            for(int dc = -1; dc <= 1; dc++){
                int r = row + dr;
                int c = col + dc;
                if(r >= 0 && r < rows && c >=0 && c < cols && !mines[r][c] && !revealed[r][c]){
                    revealCell(r, c);
                }
            }
        }
    }

    private void revealAllMines(){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                if(mines[i][j]){
                    buttons[i][j].setText("*");
                }
            }
        }
    }

    private void checkWin(){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; i++){
                if(!mines[i][j] && !revealed[i][j]){
                    return;
                }
            }
        }
        JOptionPane.showMessageDialog(frame, "You win!");
        gameOver = true;
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                new MinesweeperGUI();
            }
        });
    }
}