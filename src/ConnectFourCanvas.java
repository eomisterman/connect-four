import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

public class ConnectFourCanvas extends Canvas implements MouseListener, Runnable  
{
	
	private static final long serialVersionUID = 1L; 
	
	// instance variables 
	
	// for double buffering
    Image offscreen;
    Dimension offscreensize;
    Graphics g2;
        
    int row = 6;
  	int col = 7;
    String[][] board = new String[row][col];    // represents the game board
    
    // stores the winningPieces: true = corresponding piece in 'board' is a winning piece, 
	  //                           false = not
    boolean[][] winningPieces = new boolean[row][col];
    Vector<Integer> flaggedCol = new Vector<Integer>();  // Colums that have blocks
    boolean[][] aboveBlocker = new boolean[row][col];    // Pieces above blocker   
  	boolean fallingAnimation;	// animation after blocker disappears
  	boolean fallingRed;       // if the falling piece after blocker diapperas is Red
  	int dropAt;               // where the piece falls after blocker disappears
    
    int size = 80;            // size of an 'imaginary' box that contains a piece
    int border = 20;
    int currentR, currentC;   // Row and Column where the falling piece is landing
    String acceptable;        // acceptable piece while checking if the user wins
       
    // for blocker life
    int redBlockerRow;
    int redBlockerCol;
    int yellowBlockerRow;
    int yellowBlockerCol;
    
    boolean blockerPiece;			// check if blocker is in use
    boolean blockerY = false; // check which blocker in use (also to keep track of 
    boolean blockerR = false; // when the blocker diappears)
    boolean turn = true;      // Player turn: true = red , false = yellow
    
    boolean winnerCanvas = false;  // if winner is decided
    ConnectFourGame parent;
    
    // for animation
    Thread myThread;
    double position = 0.0;
    int startX, startY, endY;
    boolean animate = false;
    long startTime = 0;
    
    //constructor    
    public ConnectFourCanvas(ConnectFourGame cf)
    {
    	parent = cf;
    }    
   
 	  // updates the canvas, called by paint() 
    public synchronized void update(Graphics g) 
    {    		
	    Dimension d = getSize();
	
	    // initially (or when size changes) create new image
	    if ((offscreen == null)
	    		|| (d.width != offscreensize.width)
	    		|| (d.height != offscreensize.height)) 
	    {
	    	offscreen = createImage(d.width, d.height);
	        offscreensize = d;
	        g2 = offscreen.getGraphics();
	        g2.setFont(getFont());
	    }
	    
	    // erase old contents:
	    g2.setColor(getBackground());
	    g2.fillRect(0, 0, d.width, d.height);    	
	    g2.setColor(Color.blue);
	    g2.fillRect(border, border, size*col, size*row);		
	    
	    parent.turnLabel.setForeground(Color.black);
		    	
		for(int r = 0; r < row; r++)
		{	    
		    for (int c = 0; c < col; c++) 
		    {
		    	drawBoardPiece(r,c);		    				
		    }	    		
		}

		if (animate)    // for the dropping piece
		{ 	    
			drawAnimationPiece();	        
		}
	    
		
		
	    if (winnerCanvas)   // if a winner is decided
	    {	    	
	    	if (!animate)    // wait until the animation is over
	    	{
	    		drawWinnerCanvas();		    	
	    	}
	    }    	
	    	    
       	g.drawImage(offscreen, 0, 0, null);     
    }
    
    public void paint(Graphics g) 
    {
      //updates board
        update(g);
    }
    
    public void setPieceColor(int r, int c)
    {
      //draws board pieces depending on the game piece, white if empty
    	
    	if (board[r][c] == "Blocked")
		{
			g2.setColor(Color.black);
		}
		
		else if(board[r][c] == "Red")
		{
			g2.setColor(Color.red);
		}
		else if (board[r][c] == "Yellow")
		{
			g2.setColor(Color.yellow);
		}
    	else 
		{
			g2.setColor(Color.white);
		}
    }
    
    public void drawBoardPiece(int r, int c)
    {    	
      //draws game pieces and winning pieces
    	int y = r * size + border;
    	int x = c * size + border;	
    	
    	if (animate && r==currentR && c == currentC)
    	{
    		g2.setColor(Color.white);
    		g2.fillOval(x+10, y+10, size - 20, size - 20);
    	}
    	else if (animate)
    	{
    		setPieceColor(r,c);
    		g2.fillOval(x+10, y+10, size - 20, size - 20);
    	}
    	else
    	{		    				
    		if (winningPieces[r][c])
    		{
    			g2.setColor(Color.green);
	    		g2.fillOval(x+5, y+5, size - 10, size - 10);
    		}		    			
    		setPieceColor(r,c);
    		g2.fillOval(x+10, y+10, size - 20, size - 20);
    	}
    }
    
    public void drawAnimationPiece()
    {
      //draws animation
    	int currentY = startY + (int)(position*(endY - startY));
    	if (fallingAnimation)
    	{
    		if (fallingRed)
    		{
    		g2.setColor(Color.red);
    		}
    		else
    		{
    			g2.setColor(Color.yellow);
    		}
    	}
    	else
    	{
            setPieceColor(currentR, currentC);
    	}
        g2.fillOval(startX + 10, currentY + 10, size - 20, size - 20);
    }
    
    public void drawWinnerCanvas()
    {
      //displays winner
    	if (board[currentR][currentC] == "Red")
    	{
    		parent.turnLabel.setForeground(Color.red);
    		parent.turnLabel.setText("Red Wins");
    		
    	}
    	else if (board[currentR][currentC] == "Yellow")
    	{
    		parent.turnLabel.setForeground(Color.yellow);
    		parent.turnLabel.setText("Yellow Wins");
    		
    	}		
    }
  
    
    
    public void start() 
    {
      //for animation
        myThread = new Thread(this);
        myThread.start();
    }

    public void stop() 
    {
      //for animation
        myThread = null;
    }

    public void blockerOn()
    {
      //turns blocker on depending on turn. used with blocker life
    	
    	if (turn)
    	{
    		blockerR = !blockerR;
    	}
    	else
    	{
    		blockerY = !blockerY;
    	}
    	
    }
    
    // handle mouse events
    public void mousePressed(MouseEvent event) 
    {
	   if (!winnerCanvas && !animate)
	   {
    		Point p = event.getPoint();
	
	        // check if clicked in box area	
	        int x = p.x - border;
	        int y = p.y - border;
	        int colNum = x/size;
	        int rowNum = y/size;
	        
	        
	        if (x >= 0 && x < (col*size) &&
	        	y >= 0 && y < (row*size))
	        {
	        	
	        	int r = pieceDrop(colNum);
	        	
	        	// if blocker is selected and the player presses a 
	        	// space that already has a piece
	        	if (blockerPiece && board[rowNum][colNum] != null)
	        	{ 
	        		return;
	        	}
	        	
		        if (r>=0)
		        {		        	
		        	startX = colNum*size + border;
		        	startY = border;	        	        	
		        	
		        	animate = true;
		            startTime = System.currentTimeMillis();   
		            
		            if (flaggedCol.contains(colNum))
		            {
		            	aboveBlocker[r][colNum] = true;
		            }
		            
			        if(!blockerPiece)
			        {
			        	currentC = colNum;
			            currentR = r;
			        	endY = r * size + border;
			        	
                // assigning r/y piece to data structure
			        	if(turn)
				        {
				        	board[r][colNum] = "Red";				        		
				        }
				        else
				        {
				        	board[r][colNum] = "Yellow";
				        }				        	
			        }				        
			        else   // if blocker
			        {
			        	currentC = colNum;
				        currentR = rowNum;
				        
				        flaggedCol.add(colNum);
				        
					    endY = rowNum*size+border;					        	
					    board[rowNum][colNum] = "Blocked";
					    parent.blockerLabel.setText("");
					    
					    
					    if (turn)
					    {
                // records where redblocker is
					    	redBlockerCol = currentC;
					    	redBlockerRow = currentR;					    	
					    }
					    else 
					    {
                // records where yellow blocker is
					    	yellowBlockerCol = currentC;
					    	yellowBlockerRow = currentR;	
					    }
			        		        	
		        	}	        
			        if (turn)
			        {
			        	acceptable = "Red";
			        }
			        else
			        {
			        	acceptable = "Yellow";
			        }
			        
              //test for winner
			        if( diagonalTestTopBottom(r, colNum) || verticalTest(r, colNum) ||
			        		diagonalTestBottomTop(r, colNum) || horizTest(r,colNum) )
			        {			        
			        	winnerCanvas = true;			        	        
			        }
			        else
			        {
                //pieces to highlight when player wins
			        winningPieces = new boolean[row][col];			        		
			        }		        
			        

		        	// handles blocker life	        		
			        if(turn && blockerR){
			        	
		        		parent.lifeRValue -= 1;
			        	

		        		parent.lifeRLabel.setForeground(Color.red);
		        		parent.lifeRLabel.setText(Integer.toString(parent.lifeRValue));
		        	}
              //handles blocker life
		        	if(!turn && blockerY){
		        		parent.lifeYValue -= 1;
		        		parent.lifeYLabel.setForeground(Color.yellow);
		        		parent.lifeYLabel.setText(Integer.toString(parent.lifeYValue));
		        	}
			        //changes turn
			        turn = !turn;
			        parent.blockerLabel.setText("");
			    
			        if (turn)
			        {			        	
			        	parent.turnLabel.setText("Red's Turn");
			        }
			        else 
			        {
			        	parent.turnLabel.setText("Yellow's Turn");
			        }	
		        }
	        }
	   }
    }
    
    public int pieceDrop(int colNum) 
    {    	
      // handles the lowest space a normal playing piece can drop
    	for(int i = 0; i <= 5; i++)
		{    		
    		if (board[i][colNum] == "Blocked")
    		{
    			
    			
    			return i-1;
    		}
    		else if(board[i][colNum] == "Red" || 
    				board[i][colNum] == "Yellow")
			{
				return i-1;
			}
		}
        return 5;
    }
    
    public void fallingAnimation(int i, int blockedCol)
	{	
      //handles piece dropping animation
				String move = board[i][blockedCol];
				board[i][blockedCol] = null;
				
				int checker = 0;
				for (int j = i; j < row; j++)
				{
					if (board[j][blockedCol] != null)
					{						
						endY= (j - 1) * size + border;
						dropAt = j - 1;
						checker = 1;
						break;
					}
				}
				if (checker == 0){
					endY = 5 * size + border;
					dropAt = 5;
				}
				
				int y = dropAt;
				
				currentC = blockedCol;
				currentR = y;
				board[y][blockedCol] = move;
				
				startX = blockedCol * size + border;
				startY = i * size + border;
				
				animate = true;
				startTime = System.currentTimeMillis();	
				    			
    			acceptable = move;

    			if(diagonalTestTopBottom(y, blockedCol) || verticalTest(y, blockedCol) ||
			        		diagonalTestBottomTop(y, blockedCol) || horizTest(y, blockedCol) )
			    {			        
			        winnerCanvas = true;			        	        
			    }
			    else
			    {
			        winningPieces = new boolean[row][col];			        		
			    }  			
			}

    public void run()
    {
      // for animation, handles blocker animations and expiration
    	while(true)
    	{
    		if (animate)
    		{				
    			double t = (System.currentTimeMillis() - startTime) / 1000.0;
    			if (t < 1)
    			{
    				position = t;
    			}	
    			else
    			{
    				animate = false;
    				
    				if (blockerPiece)
    				{
    					blockerPiece = false;
    				}
    				
    				
    				if( blockerR && parent.lifeRValue == 0)
    				{
	        			// erase blocker, change blocker life back to 0
	        			board[redBlockerRow][redBlockerCol] = null;	        			
	        			parent.lifeRLabel.setForeground(Color.black);
	        			parent.lifeRLabel.setText(Integer.toString(parent.lifeRValue));
	        			blockerR = false;
	        			int blockedCol = flaggedCol.firstElement();
	        			fallingAnimation = true;
	        			for (int i = row - 1; i >= 0; i--)
	        			{		        				
	        				if (aboveBlocker[i][blockedCol])
	        				{	        					
	        					fallingAnimation(i, blockedCol);	        					
	        				}
	        			}
	        			fallingAnimation = false;
	        			flaggedCol.remove(0);
	        			
	        			
	        		}
	        		if (blockerY && parent.lifeYValue == 0) {
	        			// erase blocker, change blocker life back to 0
	        			board[yellowBlockerRow][yellowBlockerCol] = null;
	        			parent.lifeYLabel.setForeground(Color.black);
	        			parent.lifeYLabel.setText(Integer.toString(parent.lifeYValue));
	        			blockerY = false;
	        			int blockedCol = flaggedCol.firstElement();
	        			fallingAnimation = true;
	        			for (int i = row - 1; i >= 0; i--)
	        			{	        				
	        				if (aboveBlocker[i][blockedCol])
	        				{
	        					fallingAnimation(i, blockedCol);
	        				}
	        			}
	        			fallingAnimation = false;
	        			flaggedCol.remove(0);

	        		}    				
    			}
    			repaint();
    		}
    		try 
    		{
                int fps = 60; // frame rate (frames per second)
                Thread.sleep(1000 / fps); // wait time in milliseconds
            } 
    		catch (InterruptedException e) { }

    	}
    }
    
    // need these also because we implement a MouseListener
    public void mouseReleased(MouseEvent event) { }
    public void mouseClicked(MouseEvent event) { }
    public void mouseEntered(MouseEvent event) { }
    public void mouseExited(MouseEvent event) { }
    
    public boolean diagonalTestTopBottom(int r, int c)
    {
            // Diagonal test of four piece sequence, boolean, player wins if true
    	if (diagonalTestTopBottomUp(r,c) + 
    			diagonalTestTopBottomDown(r,c) - 1 >= 4)
    	{
    		return true;
    	}
    	else
    	{
    		winningPieces = new boolean[row][col];
    		return false;
    	}
    }
    
    public int diagonalTestTopBottomUp(int r, int c)
    {    		
            // Diagonal test of four piece sequence
    	if (r < 0 || c < 0)
    	{
    		return 0;
    	}
    	else if (board[r][c] != acceptable)
    	{
    		return 0;	
    	}
    	else
    	{
    		winningPieces[r][c] = true;
    		return 1 + diagonalTestTopBottomUp(r-1, c-1);
    	}

    }
    
    public int diagonalTestTopBottomDown(int r, int c)
    {   		
            // Diagonal test of four piece sequence
    	if (r > 5 || c > 6)
    	{
    		return 0;
    	}
    	else if (board[r][c] != acceptable){
    		return 0;	
    	}
    	else
    	{
    		winningPieces[r][c] = true;
    		return 1 + diagonalTestTopBottomDown(r+1, c+1);
    	}
    }

    public boolean diagonalTestBottomTop(int r, int c)
    {
            // Diagonal test of four piece sequence
    	if (diagonalTestBottomTopUp(r,c) + 
    			diagonalTestBottomTopDown(r,c) - 1 >= 4)
    	{
    		return true;
    	}
    	else
    	{
    		winningPieces = new boolean[row][col];
    		return false;
    	}
    }
    
    public int diagonalTestBottomTopUp(int r, int c)
    {
            // Diagonal test of four piece sequence
    	if (r < 0 || c > 6)
    	{
    		return 0;
    	}
    	else if (board[r][c] != acceptable)
    	{
    		return 0;	
    	}
    	else
    	{
    		winningPieces[r][c] = true;
    		return 1 + diagonalTestBottomTopUp(r-1, c+1);
    	}
    }
    
    public int diagonalTestBottomTopDown(int r, int c)
    {   		
      // Diagonal test of four piece sequence
    	if (r > 5 || c < 0)
    	{
    		return 0;
    	}
    	else if (board[r][c] != acceptable)
    	{
    		return 0;	
    	}
    	else
    	{
    		winningPieces[r][c] = true;
    		return 1 + diagonalTestBottomTopDown(r+1, c-1);
    	}
    }
   
    
    public boolean verticalTest(int r, int c)
    {
      // boolean, true if vertical four piece sequence
    	if(verticalTestDown(r, c) >= 4)
    	{
    		return true;
    	}
    	else
    	{
    		winningPieces = new boolean[row][col];
    		return false;
    	}
    }

    
    public int verticalTestDown(int r, int c)
    {
      // Vertical test, only goes down
    	if (r > 5)
    	{
    		return 0;
    	}    		
    	else if (board[r][c] != acceptable)
    	{
    		return 0;
    	}    		
	    else
	    {
    		winningPieces[r][c] = true;
	    	return 1 + verticalTestDown(r+1, c);
	    }
	}
    
    public boolean horizTest(int row, int col)  
    {    	
      // combines left and right test to check 4 piece sequence
 		if((horizTestRight(row,col) +
 		horizTestLeft(row,col)) - 1 >=4)
 		{
 			return true;
 		}
 		else
 		{
    		winningPieces = new boolean[row][col];
 			return false;
 		}
    }
    
    public int horizTestRight(int r, int c)
    {    
      // tests horizontal pieces to right of parameters
		if(c > 6)
		{
			return 0;
		}
		else if(board[r][c] != acceptable)
		{		
			return 0;				
		}
    	else
    	{
    		winningPieces[r][c] = true;
    		return 1 + horizTestRight(r, c+1);
    	}

    }
    
    public int horizTestLeft(int r, int c)
    {    
      // tests horizontal pieces to right of parameters
    	if(c < 0)
    	{
			return 0;
		}
		else if(board[r][c] != acceptable)
		{				
			return 0;				
		}
    	else
    	{
    		winningPieces[r][c] = true;
    		return 1 + horizTestLeft(r, c-1);
    	}
    	
    }
    
    public void resetGame()
    {
      // resets game variables
    	board = new String[row][col];
        winningPieces = new boolean[row][col];
        blockerR = false;
        blockerY = false;
        turn = true;
        winnerCanvas = false;
        animate = false;
        flaggedCol = new Vector<Integer>();
        aboveBlocker = new boolean[row][col];
        fallingAnimation = false;
        repaint();
    }
}

