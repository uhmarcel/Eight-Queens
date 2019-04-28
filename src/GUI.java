
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.*;



public class GUI extends JFrame implements ActionListener {
    
    private Queens queens;
    private TitlePanel titlePanel;
    private BoardPanel boardPanel;
    private OptionBar optionBar; 

    
    public GUI() {
        
        super("Eight Queens Recursion Algorithm");
        this.setSize(370,446);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        
        queens = new Queens();
        titlePanel = new TitlePanel();
        boardPanel = new BoardPanel();
        optionBar = new OptionBar();
                
        this.add(optionBar, BorderLayout.NORTH);
        this.add(boardPanel, BorderLayout.CENTER);
        this.add(titlePanel, BorderLayout.SOUTH);

        this.setIconImage(queens.queenIcon.getImage());
        this.setVisible(true);  
    }
    
    
    public void actionPerformed(ActionEvent ae) {
        boardPanel.clearBoard();
        if (((JButton)ae.getSource()).getText().equals("Reset"))
            queens.reset();
        
        boolean status = queens.solveNext();

        titlePanel.update(status);
        boardPanel.update();
        optionBar.update();
    }
    
    
    private class TitlePanel extends JPanel {

        private JPanel messagePanel;
        private JTextArea messageText;
        private JPanel generatePanel;
        private JButton generateButton;
        private JButton stepsButton;
        
        public TitlePanel() {
            super();
            this.setLayout(new GridBagLayout());
            messagePanel = new JPanel();
            messagePanel.setLayout(new BorderLayout());
            messagePanel.setBorder(BorderFactory.createEtchedBorder());
            messageText = new JTextArea(" The eight queens puzzle is the problem of placing\n"
                    + " eight chess queens on an 8×8 chessboard so that \n"
                    + " no two queens threaten each other.");
            messageText.setFont(new Font(null,Font.ITALIC,11));
            messageText.setBackground(null);
            messageText.setEditable(false);
            messagePanel.add(messageText, BorderLayout.CENTER);
            
            generatePanel = new JPanel();
            generatePanel.setLayout(new GridLayout(2,1));
            generatePanel.setBorder(BorderFactory.createEtchedBorder());
            generateButton = new JButton("Generate");
            generateButton.addActionListener(GUI.this);
            stepsButton = new JButton("Steps:");
            stepsButton.setFont(new Font(null,0,11));
            stepsButton.setEnabled(false);
            generatePanel.add(generateButton);
            generatePanel.add(stepsButton);
            
            this.add(messagePanel,setConstraints(0,0,3,0,GridBagConstraints.BOTH,0));
            this.add(generatePanel,setConstraints(3,0,1,0,GridBagConstraints.BOTH,0));           
        }
        
        public void update(boolean hasNextSolution) {
            if (!hasNextSolution) 
                generateButton.setText("Reset");
            else
                generateButton.setText("Generate");
            stepsButton.setText("Steps: " + queens.getSteps());
        }
        
        public GridBagConstraints setConstraints(int gx, int gy, int gw, int gh, int fill, int i) {
            GridBagConstraints out = new GridBagConstraints();
            out.fill = fill;
            out.gridx = gx;
            out.gridy = gy;
            out.gridwidth = gw;
            out.gridheight = gh;
            out.weightx = 0.5;
            out.insets = new Insets(i,i,i,i);
            return out;
        }    
    }

    
    private class BoardPanel extends JPanel {

        private JButton[][] board;
        
        public BoardPanel() {
            super();
            this.setLayout(new GridLayout(8,8));
            board = new JButton[8][8];
            boolean tilecolor = true;
            for (int j=0; j<8; j++) {
                tilecolor = !tilecolor;
                for (int i=0; i<8; i++) {
                        board[j][i] = new JButton();
                        if (tilecolor)
                            board[j][i].setBackground(Color.darkGray);
                        this.add(board[j][i]);
                        tilecolor = !tilecolor;
                }
            }
        }
        
        private void clearBoard() {
            boolean tilecolor = true;
            for (int j=0; j<8; j++) {
                tilecolor = !tilecolor;
                for (int i=0; i<8; i++) {
                    board[j][i].setIcon(null);
                    if (tilecolor)
                        board[j][i].setBackground(Color.darkGray);
                    else 
                        board[j][i].setBackground(null);
                    tilecolor = !tilecolor;
                }
            }
        }
        
        public void update() {
            for (Queens.Queen q : queens.getQueens()) {
                board[q.y][q.x].setIcon(queens.queenIcon);
                if (queens.getMode() == Queens.FUNDAMENTAL)
                    board[q.y][q.x].setBackground(new Color(240,160,60));
                else
                    board[q.y][q.x].setBackground(new Color(240,80,80));
            }
        }
       
    }
    
    private class OptionBar extends JMenuBar implements ItemListener {
        
        private JMenu optionsMenu;
        private JMenu modeMenu;
        private JMenu solutionDisplay;
        private JRadioButtonMenuItem fundItem;
        private JRadioButtonMenuItem defItem;
        private JMenuItem resetItem;
        private JMenuItem exitItem;
        
        public OptionBar() {
            super();
            optionsMenu = new JMenu("Options");
            optionsMenu.setFont(new Font(null,1,11));
            resetItem = new JMenuItem("Reset");
            exitItem = new JMenuItem("Exit");
            
            modeMenu = new JMenu("Mode");
            modeMenu.setFont(new Font(null,1,11));
            fundItem = new JRadioButtonMenuItem("Fundemantal solutions");
            defItem = new JRadioButtonMenuItem("All solutions");
            
            ButtonGroup mode = new ButtonGroup();
            fundItem.addItemListener(this);
            defItem.setSelected(true);
            mode.add(fundItem);
            mode.add(defItem);
            
            resetItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    queens.reset();
                    boardPanel.clearBoard();
                    titlePanel.update(true);
                    boardPanel.update();
                    optionBar.update();
                }});
            
            exitItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    System.exit(0);
                }});
            
            optionsMenu.add(resetItem);
            optionsMenu.addSeparator();
            optionsMenu.add(exitItem);
            
            modeMenu.add(fundItem);
            modeMenu.add(defItem);
            
            solutionDisplay = new JMenu("");
            solutionDisplay.setFont(new Font(null,0,11));
            solutionDisplay.setEnabled(false);
            
            this.add(optionsMenu);
            this.add(modeMenu);
            this.add(Box.createHorizontalGlue());
            this.add(solutionDisplay);
            
            update();
                    
        }
        
        public void update() {
            if (queens.getMode() == queens.FUNDAMENTAL)
                solutionDisplay.setText("Fundamental " + queens.getSolutionNumber());
            else
                solutionDisplay.setText("Solution " + queens.getSolutionNumber());                    
        }

        public void itemStateChanged(ItemEvent ie) {
            if (ie.getStateChange() == ItemEvent.SELECTED)
                queens.setMode(queens.FUNDAMENTAL);
            else
                queens.setMode(queens.DEFAULT);
            queens.reset();
            boardPanel.clearBoard();
            titlePanel.update(true);
            boardPanel.update();
            optionBar.update();
        }
    }
   
}
