
// Emilio Ovalles-Misterman && Crystal Paudyal
// CS 201 Final Project

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class ConnectFourGame extends Applet implements ActionListener {
	
	private static final long serialVersionUID = 1L; 
	
	//instance variables
    ConnectFourCanvas c;
    Button leftY,leftR,lifeY,lifeR, newGame;
    Label leftYLabel, leftRLabel, lifeYLabel, lifeRLabel;
    int leftYValue, leftRValue, lifeYValue, lifeRValue;
    
    Label turnLabel = new Label("Red's turn");
    Label blockerLabel = new Label("",(int) LEFT_ALIGNMENT);
                
    public void init () {
    	// initializes display
    	
      newGame = new Button("New Game");
      newGame.setBackground(Color.gray);
      newGame.addActionListener(this);
      setFont(new Font("TimesRoman", Font.BOLD, 20));
      leftY = new Button("Blocker");
      leftY.setBackground(Color.yellow);
      leftY.addActionListener(this);
      
      leftR = new Button("Blocker");
      leftR.setBackground(Color.red);
      leftR.addActionListener(this);
      
      leftYValue = 2;
      leftRValue = 2;
      lifeYValue = 0;
      lifeRValue = 0;
      
      leftYLabel = new Label(Integer.toString(leftYValue));
      leftRLabel = new Label(Integer.toString(leftRValue));
      lifeYLabel = new Label(Integer.toString(lifeYValue),(int) RIGHT_ALIGNMENT);
      lifeRLabel = new Label(Integer.toString(lifeRValue),(int) RIGHT_ALIGNMENT);
      
      setLayout(new BorderLayout());
      add("North",makeTopPanel());
      c = new ConnectFourCanvas(this);
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
      titlePanel.setBackground(Color.magenta);
      titlePanel.add(new Label("Connect Four"));
      
      return titlePanel;
    }
  
  	public Panel makeBlockerPanel() {
  		  
    
	  Panel leftR = new Panel();
      leftR.setLayout(new GridLayout(1,3));
      leftR.add(this.leftR);    // add blockerButton here
      leftR.add(new Label(" "));
      leftR.add(leftRLabel);
      
      Panel lifeR = new Panel();
      lifeR.setLayout(new GridLayout(1,1));
      lifeR.add(lifeRLabel);
      
      Panel leftY = new Panel();
      leftY.setLayout(new GridLayout(1,3));
      leftY.add(this.leftY);    // add blockerButton here
      leftY.add(new Label(" "));
      leftY.add(leftYLabel);
      
      Panel lifeY = new Panel();
      lifeY.setLayout(new GridLayout(1,1));
      lifeY.add(lifeYLabel);
      
      Panel blockerPanel = new Panel();
	  blockerPanel.setLayout(new GridLayout(3, 2));
	  blockerPanel.setBackground(Color.cyan);
	  blockerPanel.add(new Label("Blockers Left", (int) RIGHT_ALIGNMENT));
	  blockerPanel.add(new Label("Blocker Life",(int) RIGHT_ALIGNMENT));
	  blockerPanel.add(leftR);
	  blockerPanel.add(lifeR);
	  
	  blockerPanel.add(leftY);
	  blockerPanel.add(lifeY);
      
      
      return blockerPanel;
      
    }
  
  	public Panel makeTurnPanel() {
  	  Panel turnPanel = new Panel();
      turnPanel.setLayout(new GridLayout(1,3));
      turnPanel.setBackground(Color.cyan);
      turnPanel.add(turnLabel);
      turnPanel.add(blockerLabel);
      turnPanel.add(newGame);
      return turnPanel;
    }
  	
	
  	public void start() {
        c.start();
    }

    public void stop() {
        c.stop();
    }
    
    public void actionPerformed(ActionEvent e) 
    {    	
        if (e.getSource() == newGame)
        {   
        	// reset values for new game
        	leftYValue = 2;
            leftRValue = 2;
            lifeYValue = 0;
            lifeRValue = 0;
            
            leftYLabel.setText(Integer.toString(leftYValue));
            leftRLabel.setText(Integer.toString(leftRValue));
            
            lifeYLabel.setForeground(Color.black);
            lifeYLabel.setText(Integer.toString(lifeYValue));
            
            lifeRLabel.setForeground(Color.black);
            lifeRLabel.setText(Integer.toString(lifeRValue));
            
            turnLabel.setText("Red's turn");
            blockerLabel.setText("");
            
            c.resetGame();
        }
        
        else if (!c.winnerCanvas)  // if the game's not over
        { 
        
        	// if yellow's blocker button is pressed
	        if (e.getSource() == leftY)
	        {       	        
	        	if (!c.turn)  // if Yellow's turn
	        	{
		        	// if there's blocker left and is not already in use
		        	if(!c.blockerY && leftYValue > 0)
		        	{
		        		c.blockerPiece = true;
		        		//subtract blocker selected
		        		leftYValue -= 1;
			        	leftYLabel.setText(Integer.toString(leftYValue));
			        	blockerLabel.setText("Blocker On!");
			        	//initialize blocker life, highlight number
			        	lifeYValue = 2;
			        	lifeYLabel.setForeground(Color.yellow);
			        	lifeYLabel.setText(Integer.toString(lifeYValue));
			        	lifeYValue = 3;
			        	c.blockerOn();
		        	}
		        	
		        	//if blocker is selected, but haven't been dropped, toggle blocker    
			    	else if (c.blockerY && lifeYValue == 3)
			    	{
			    		c.blockerPiece = false;
			    		//add value, toggle
			    		leftYValue += 1;
			    		leftYLabel.setText(Integer.toString(leftYValue));
			    		blockerLabel.setText(" ");
			    		
			    		//reset blocker life to zero
			        	lifeYValue = 0;
			        	lifeYLabel.setForeground(Color.black);
			        	lifeYLabel.setText(Integer.toString(lifeYValue));
			        	
			    		c.blockerOn();			
			    	}
			    	else if (c.blockerY && lifeYValue < 3)
			    	{
			        	blockerLabel.setText("In Play!");
			    	}
		    	
			    }
	        	
	        	else     // if not Yellow's turn
	        	{
	        		blockerLabel.setText("Wrong Team!");
	        	}
	        }
	        //if Red's blocker button is pressed
	        else if (e.getSource() == leftR)
	        {
	        	if (c.turn)  // if Red's turn
	        	{
		        	// if there's blocker left and is not already in use
		        	if(!c.blockerR && leftRValue > 0)
		        	{
		        		c.blockerPiece = true;
		        		//subtract value, blocker selected
		        		leftRValue -= 1;
			        	leftRLabel.setText(Integer.toString(leftRValue));
			        	blockerLabel.setText("Blocker On");
			        	//initialize blocker life, highlight number
			        	lifeRValue = 2;
			        	lifeRLabel.setForeground(Color.red);
			        	lifeRLabel.setText(Integer.toString(lifeRValue));
			        	lifeRValue = 3;
			        	
		        		c.blockerOn();
		        	}
		        	
		        	//if blocker is selected, but haven't been dropped, toggle blocker   
			    	else if (c.blockerR && lifeRValue == 3)
			    	{
			    		c.blockerPiece = false;
			    		// add value, toggle
			    		leftRValue += 1;
			    		leftRLabel.setText(Integer.toString(leftRValue));
			    		blockerLabel.setText(" ");
			    		// reset blocker life
			    		lifeRValue = 0;
			        	lifeRLabel.setForeground(Color.black);
			        	lifeRLabel.setText(Integer.toString(lifeRValue));
			        	
			    		c.blockerOn();
				
			    	}
		    	
			    	else if (c.blockerR && lifeRValue < 3)
			    	{
			    		// error message, only one blocker can be in play per team
			        	blockerLabel.setText("In Play!");
			    	}
			    }
	        	
	        	else     // if not Red's turn, error message
	        	{
	        		blockerLabel.setText("Wrong Team!");
	        	}
	        	
	        }        
        }	
    }
}	   