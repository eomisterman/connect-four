/*
// Emilio Ovalles-Misterman && Crystal Paudyal
// CS 201 Final Project

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class backup extends Applet implements ActionListener {

    ConnectFourCanvas c;
    //Button setButton, clearButton, toggleButton;
    
    public void init () {
    
      setFont(new Font("TimesRoman", Font.BOLD, 28));
      
      
      
      setLayout(new BorderLayout());
      add("North",makeTopPanel());
      c = new ConnectFourCanvas();
      c.setBackground(Color.orange);
      add("Center", c);
      c.addMouseListener(c);
      
    }
  
  	public Panel makeTopPanel() {
      Panel topPanel = new Panel();
      topPanel.setLayout(new BorderLayout());
      topPanel.add("North", makeTitlePanel());
      topPanel.add("Center", makeBlockerPanel());
      topPanel.add("South", makeTurnPanel());
                
      return topPanel;
    }
  
  	public Panel makeTitlePanel() {
  	  Panel titlePanel = new Panel();
      titlePanel.setLayout(new FlowLayout());
      titlePanel.setBackground(Color.green);
      titlePanel.add(new Label("Connect Four"));
      
      return titlePanel;
    }
  
  	public Panel makeBlockerPanel() {
  		  
    
	  Panel leftR = new Panel();
      leftR.setLayout(new GridLayout(1,2));
      leftR.add(new Label("Red"));    // add blockerButton here
      leftR.add(new Label("2"));
      
      Panel lifeR = new Panel();
      lifeR.setLayout(new GridLayout(1,2));
      lifeR.add(new Label("Red"));    // add blocker button here
      lifeR.add(new Label("2"));
      
      Panel leftY = new Panel();
      leftY.setLayout(new GridLayout(1,2));
      leftY.setBackground(Color.red);
      leftY.add(new Label("Yellow"));  // add blockerButton here
      leftY.add(new Label("2"));
      
      Panel lifeY = new Panel();
      lifeY.setLayout(new GridLayout(1,2));
      lifeY.setBackground(Color.magenta);
      lifeY.add(new Label("Yellow"));	// add blockerButton here
      lifeY.add(new Label("1"));
      
      Panel blockerPanel = new Panel();
	  blockerPanel.setLayout(new GridLayout(3, 2));
	  blockerPanel.setBackground(Color.cyan);
	  blockerPanel.add(new Label("Blockers Left"));
	  blockerPanel.add(new Label("Blocker Life"));
	  blockerPanel.add(lifeY);
	  blockerPanel.add(leftY);
	  
	  blockerPanel.add(leftR);
	  blockerPanel.add(lifeR);
      
      
      return blockerPanel;
      
    }
  
  	public Panel makeTurnPanel() {
  	  Panel turnPanel = new Panel();
      turnPanel.setLayout(new FlowLayout());
      turnPanel.setBackground(Color.cyan);
      turnPanel.add(new Label("Turn: "));
      turnPanel.add(new Label("Yellow"));	// add player turn img here // should be bool
      
      return turnPanel;
    }

    
    public void actionPerformed(ActionEvent e) {
        
    }
}


@SuppressWarnings("serial")

class backupCanvas extends Canvas implements MouseListener  {

    // instance variables representing the game go here
    int row = 6;
  	int col = 7;
    String[][] grid = new String[row][col];
    int size = 80;
    int border = 20;
    
    boolean blocker = false;
    boolean turn = true;
    boolean red = true;
    boolean yellow = false;
    
    // draw the boxes
    public void paint(Graphics g) {
    	for(int r = 0; r < row; r++){
    		int y = border;
    		y = r * size + border;
	        for (int c = 0; c < col; c++) {
	            int x = c * size + border;
	            g.setColor(Color.blue);
	            g.fillRect(x, y, size, size);
	            if(grid[r][c] == null)
	            {
	            	g.setColor(Color.white);
	            }
	            else if(grid[r][c] == "Red")
	            {
	            	g.setColor(Color.red);
	            }
	            else
	            {
	            	g.setColor(Color.yellow);
	            }
	        	g.fillOval(x+10, y+10, 60, 60);
	            
	            
	        }
    	}
    	
    }

    // handle mouse events
    public void mousePressed(MouseEvent event) {
        Point p = event.getPoint();

        // check if clicked in box area

        int x = p.x - border;
        int y = p.y - border;
        int colNum = x/size;
        int rowNum = y/size;
        
        if (x >= 0 && x < (col*size) &&
        	y >= 0 && y < (row*size)){
	        if(!blocker){
	        	if(pieceDrop(colNum) >= 0){
		        	if(turn) {
		        		grid[pieceDrop(colNum)][colNum] = "Red";
		        		
		        	}
		        	else{
		        		grid[pieceDrop(colNum)][colNum] = "Yellow";
		        	}
		        	System.out.println(grid[pieceDrop(colNum)][colNum]);
	        	}
	        	
	        }
	        else{
	        	if(grid[rowNum][colNum] != null){
	        		if(turn)
	        			grid[rowNum][colNum] = "Red";
	        		else
	        			grid[rowNum][colNum] = "Yellow";
	        	}
	        }
        }
        repaint();
//        if(!verticalTest(rowNum, colNum))
//        	System.out.println(verticalTestDown(rowNum,colNum)
//        			+ verticalTestUp(rowNum,colNum));
        if(horizTest(rowNum, colNum))
        	System.out.println(horizTestLeft(rowNum, colNum) +
        			horizTestRight(rowNum, colNum));
        turn = !turn;
    }
    
    public int pieceDrop(int column) {
    	// finds the lowest possible row to drop piece within column
    	
    	for(int i = 0; i <= 5; i++)
		{
			if(grid[i][column] != null)
			{
				return i-1;
			}
		}
        return 5;
    }


    
    public boolean horizTest(int row, int col){
    	
 		if(horizTestRight(row,col) +
 		horizTestLeft(row,col) >=4){
 			return true;
 		}
 		else{
 			return false;
 		}
    }
    
    public int horizTestRight(int row, int col){
    	//Runs horizontal test right
    	
    	// Red's turn
    	if(turn){
    		if(grid[row][col] == "Red"){
    			if(col+1 < 7){
    				return 1 + horizTestRight(row, col+1);
    			}
    			// Outside bounds of grid
    			else{
    				return 0;
    			}
    		}
    		else{
    			return 0;
    		}
    	}
    	else{
    		if(grid[row][col] == "Yellow"){
    			if(col+1 < 7){
    				return 1 + horizTestRight(row, col+1);
    			}
    			// Outside bounds of grid
    			else{
    				return 0;
    			}
    		}
    		else{
    			return 0;
    		}
    	}
    }
    
    public int horizTestLeft(int row, int col){
    	//Runs horizontal test left
    	
    	// Red's turn
    	if(turn){
    		if(grid[row][col] == "Red"){
    			if(col-1 >= 0){
    				return 1 + horizTestLeft(row, col-1);
    			}
    			// Outside bounds of grid
    			else{
    				return 0;
    			}
    		}
    		else{
    			return 0;
    		}
    	}
    	else{
    		if(grid[row][col] == "Yellow"){
    			if(col-1 >= 0){
    				return 1 + horizTestLeft(row, col-1);
    			}
    			// Outside bounds of grid
    			else{
    				return 0;
    			}
    		}
    		else{
    			return 0;
    		}
    	}
    }
    
    
    
    public boolean verticalTest(int row, int col){
    	// Runs up and down vertical tests to complete the vertical test
    	if((verticalTestUp(row, col) + 
    		verticalTestDown(row, col)) >= 4){
    		return true;
    	}
    	else{
    		return false;
    	}
    }
    
    public int verticalTestUp(int row, int col) {
    	// Function tests for a four-piece sequence vertically
    	
    	// Red's turn
    	if(turn){
    		if(grid[row][col] == "Red"){
    			if(row-1 >= 0){
    				return 1 + verticalTestUp(row-1, col);
    			}
    			// Outside bounds of grid
    			else{
    				return 0;
    			}
    		}
    		else{
    			return 0;
    		}
    	}
    	// Yellow's turn
    	else{
    		if(grid[row][col] == "Yellow"){
    			if(row-1 >= 0){
    				return 1 + verticalTestUp(row-1, col);
    			}
    			// Outside bounds of grid
    			else{
    				return 0;
    			}
    		}
    		else{
    			return 0;
    		}
    	}
    }
    
    public int verticalTestDown(int row, int col) {
    	// Function tests for a four-piece sequence vertically
    	
    	// Red's turn
    	if(turn){
    		if(grid[row][col] == "Red"){
    			if(row+1 <= 5){
    				return 1 + verticalTestDown(row+1, col);
    			}
    			// Outside bounds of grid
    			else{
    				return 0;
    			}
    		}
    		else{
    			return 0;
    		}
    	}
    	// Yellow's turn
    	else{
    		if(grid[row][col] == "Yellow"){
    			if(row+1 <= 5){
    				return 1 + verticalTestDown(row+1, col);
    			}
    			// Outside bounds of grid
    			else{
    				return 0;
    			}
    		}
    		else{
    			return 0;
    		}
    	}
    }
    
    // need these also because we implement a MouseListener
    public void mouseReleased(MouseEvent event) { }
    public void mouseClicked(MouseEvent event) { }
    public void mouseEntered(MouseEvent event) { }
    public void mouseExited(MouseEvent event) { }
}
*/