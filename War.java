//Simulates a game of war
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class War {

	//Store the cards and information on who stores what cards
	final int[] cards = {2,2,2,2,3,3,3,3,4,4,4,4,5,5,5,5,6,6,6,6,7,7,7,7,8,8,8,8,9,9,9,9,10,
                  10,10,10,11,11,11,11,12,12,12,12,13,13,13,13,14,14,14,14};
	int[] possess = {1,2,1,2,1,2,1,2,1,2,1,2,1,2,1,2,1,2,1,2,1,2,1,2,1,2,1,2,
                  1,2,1,2,1,2,1,2,1,2,1,2,1,2,1,2,1,2,1,2,1,2,1,2};
    
	//Used for printing out characters
	String[] cardString = {"","","2","3","4","5","6","7","8","9","10","J","Q","K","A"};
	
	//GUI components
	JFrame frame = new JFrame("War");
    final DrawCanvas panel = new DrawCanvas();
    JButton push = new JButton("Play");
    JLabel countOne = new JLabel("Player one has 26 cards in their deck");
    JLabel countTwo = new JLabel("Player two has 26 cards in their deck");
    
	//Store card information, temp variables for switching, totals, and who wins
	int cardOne,cardTwo,temp1,temp2,totalOne,totalTwo,winner;
    
	//Adds GUI elements on constructor to panel
    public War() {
    	push.addActionListener(new ButtonListener());
        panel.add(countOne);
        panel.add(countTwo);
        panel.add(push);
        setUp();
    }
	
	//Set up more GUI 
	public void setUp() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setLocation(new Point(1550,800));
        frame.setPreferredSize(new Dimension(500,300));
        frame.pack();
        frame.setVisible(true);  
    }
	
	//Adds the action listener to the play button
	private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            playGame();
        }   
    }
	
    //Main gameplay method
    public void playGame() {
    	cardOne = -1;
    	cardTwo = -1;
    	winner = 0;
    	
		//Generates the two cards, and makes sure that player possess it
		do {
    		temp1 = (int) (Math.random() * 51);
    		if (possess[temp1]==1) {
    			cardOne = cards[temp1];
    		}
    	} while (cardOne == -1);
    	do {
    		temp2 = (int) (Math.random() * 51);
    		if (possess[temp2]==2) {
    			cardTwo = cards[temp2];
    		}
    	} while (cardTwo == -1);
    	
		
    	if (cardOne>cardTwo) {			//Player 1 wins, they get the card
			possess[temp2] = 1;
    		winner=1;
    	} else if (cardOne<cardTwo) {	//Player 2 wins, they get the card
    		possess[temp1]=2;
    		winner=2;
    	} else {						//Its a tie, so WAR!
    		frame.repaint();			//Redraws the screen to say WAR
    		
			//Sets a three second timer, then does the war
			Timer timer = new Timer(3000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    doWar();
                    push.setEnabled(true);
                }
            });
			
			//Timer settings
    		timer.setRepeats(false);
    		timer.start();
    		push.setEnabled(false);
    	}
    	doUpdate(winner);	//Update the board
    }
    
	//War
    public void doWar() {
    	//Use these values to check if card is valid
		cardOne=0;
    	cardTwo=0;
		
		//Sets the war cards to be possessed by 3, which represents a war state
    	possess[temp1]=3;
    	possess[temp2]=3;
		
		//War only happens if each player has more than 5 cards (war cards, 3 down, then next card)
    	if (totalOne > 5 && totalTwo > 5) {
    		
			//Generates 3 cards from each player, puts them into a war state
			for (int i=0; i<3; i++) {
    			do {
    				temp1 = (int) (Math.random() * 51 + 1);
    				if (possess[temp1]==1) {
    					possess[temp1] = 3;
    					cardOne=1;
    				}
    			} while (cardOne == 0);
    			do {
    				temp2 = (int) (Math.random() * 51 + 1);
    				if (possess[temp2]==2) {
    					possess[temp2] = 3;
    					cardTwo=2;
    				}
    			} while (cardTwo == 0);
    		}
			
    		playGame();	//Plays a normal round, winner gets war cards
    	} else {
    		winner=3;	//Represents tie state, everyone keeps their cards
    	}
    }
    
	//Updates totals, and gives cards won in war to winner
    public void doUpdate(int player) {
		//Gives possession of war cards to winner
		for (int i=0; i<51; i++) {
    		if (possess[i] == 3) {
    			possess[i] = player;
    		}
    	}
		
		//Calculate totals for both players and sets the displays
		totalOne = 0;
    	for (int i=0; i<51; i++) {
    		if (possess[i] ==1) {
    			totalOne++;
    		}
    	}
    	totalTwo = 52 - totalOne;
    	countOne.setText("Player one has " + totalOne + " cards in their deck");
        countTwo.setText("Player two has " + totalTwo + " cards in their deck");
    	
		checkWin();	//Check for wins
    }
    
    //Redraw method
    @SuppressWarnings("serial")
	class DrawCanvas extends JPanel {
        @Override
        public void paintComponent(Graphics g) {  // invoke via repaint()
            g.drawString(cardString[cardOne],180,100);
            g.drawString(cardString[cardTwo],280,100);
            if (winner ==0) {
                g.drawString("WAR!", 215, 150);
            } else if (winner==3){
            	g.drawString("DRAW", 215, 150);
            } else {
                g.drawString("Player " + winner + " won", 195, 150);
            }
        }
    }
    
	//Check win
    public void checkWin() {
        if (totalOne==52) {
            countOne.setText("Player One Wins!");
            countTwo.setText("");
            push.setEnabled(false);
            push.setVisible(false);
        } else if (totalTwo==52) {
            countOne.setText("");
            countTwo.setText("Player Two Wins!");
            push.setEnabled(false);
            push.setVisible(false);
        }
        frame.repaint();
    }
	
	//Main driver method
	public static void main(String[] args) {
		new War();
	}
}
