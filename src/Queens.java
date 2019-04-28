
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.ImageIcon;


public class Queens {
    
    private Solution queens;
    private Set<Solution> previousSolutions;

    private int steps; 
    private int nextSolution;
    private int solution;
    
    private boolean mode;
    public final ImageIcon queenIcon;
    
    public static final boolean FUNDAMENTAL = true;
    public static final boolean DEFAULT = false;
    
    public Queens() {
        queens = new Solution();
        previousSolutions = new HashSet<>();
        queenIcon = loadImageIcon("queen.png");
        mode = false;
        steps = 0;
        nextSolution = 1;
        solution = 1;
    }

    
    public boolean recursiveSolve(int x, int y) {
        steps++;
        
        if (y>7) {
            if (mode == FUNDAMENTAL) {
                if (isSolutionFundamental()) {
                    solution--;
                    if (solution == 0) {
                        addSolutionAndEquivalences();
                        return true;
                    }
                }
                else 
                    return false;
            }
            else {
                solution--;
                return solution == 0;
            }
        }
        if (x>7)
            return false;
        
        if (isPositionValid(x,y)) {
            queens.add(y, new Queen(x,y));
            if (recursiveSolve(0,y+1) == false){
                queens.remove(y);
                return recursiveSolve(x+1, y);
            }
            else {
            return true;
            }
        }
        else {
            return recursiveSolve(x+1, y);
        }
    }
    
    
    private boolean isPositionValid(int x, int y) {
        for (Queen current : queens) {
            if (current.x == x)
                return false;
            
            int xdif = Math.abs(x - current.x);
            int ydif = Math.abs(y - current.y);
            
            if (current.y + xdif == y || current.x + ydif == x) 
                return false;
        }
        return true;
    }
    
    private boolean isSolutionFundamental() {
        return !previousSolutions.contains(queens);
    }
    
    private void addSolutionAndEquivalences() {
        previousSolutions.add(queens);
        previousSolutions.add(rotate(queens));
        previousSolutions.add(rotate(rotate(queens)));
        previousSolutions.add(rotate(rotate(rotate(queens))));
        
        Solution reflection = reflect(queens);
        
        previousSolutions.add(reflection);
        previousSolutions.add(rotate(reflection));
        previousSolutions.add(rotate(rotate(reflection)));
        previousSolutions.add(rotate(rotate(rotate(reflection))));
    }
    
    
    private Solution rotate(Solution solution) {
        Queen[] array = new Queen[8];
        for (Queen q : solution) {
            int absX = q.x-4;
            int absY = q.y-4;
            array[(-absX)+3] = new Queen(absY+4, (-absX)+3);
        }
        return new Solution(Arrays.asList(array));
    }
    
    private Solution reflect(Solution solution) {
        ArrayList<Queen> output = new ArrayList<>();
        Queen[] array = new Queen[8];
        for (Queen q : solution) {
            int absX = q.x-4;
            int absY = q.y-4;
            array[absY+4] = new Queen((-absX)+3, absY+4);
        }
        return new Solution(Arrays.asList(array));
    }
    
    public void reset() {
        queens.clear();
        previousSolutions.clear();
        steps = 0;
        solution = 1;
        nextSolution = 1;
    }
    
    
    private void clear() {
        queens.clear();
        steps = 0;
        solution = nextSolution;
    }
    
    
    public boolean solveNext() {
        clear();
        recursiveSolve(0,0);
        if (queens.isEmpty()) 
            return false;
        nextSolution++;
        return true;
    }
    
    
    public ArrayList<Queen> getQueens() {
        return queens;
    }
    
    public void setMode(boolean mode) {
        this.mode = mode;
    }
    
    public boolean getMode() {
        return mode;
    }
    
    public int getSteps() {
        return steps;
    }
    
    public int getSolutionNumber() {
        return nextSolution-1;
    }
    
    private ImageIcon loadImageIcon(String path) {   
        return new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/"+path)));
    }
    
    public static class Queen {
        public final int x;
        public final int y;
      
        public Queen(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public int hashCode() {
            return 31*x + y;
        }
        public String toString() {
            return "["+x+","+y+"]";
        }
        public boolean equals(Object o) {
            if (o instanceof Queen)
                return ((Queen)o).x==x && ((Queen)o).y == y;
            return false;
        }
    }
    
    public static class Solution extends ArrayList<Queen> {
        public Solution() {
            super(8);
        }
        public Solution(List<Queen> a) {
            super(a);                   
        }
        public boolean equals(Object other) {
            if (!(other instanceof Solution)) {
                return false;
            }
            for (Queen q : this) { 
                if (!((Solution)other).contains(q))
                    return false;
            }
            return true;
        }
    }

    
}
