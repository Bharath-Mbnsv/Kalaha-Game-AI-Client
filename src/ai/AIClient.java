package ai;

import ai.Global;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import kalaha.*;

/**
 * This is the main class for your Kalaha AI bot. Currently
 * it only makes a random, valid move each turn.
 *
 * 
 */
public class AIClient implements Runnable
{
    private int player;
    private JTextArea text;

    private PrintWriter out;
    private BufferedReader in;
    private Thread thr;
    private Socket socket;
    private boolean running;
    private boolean connected;

    /**
     * Creates a new client.
     */
    public AIClient()
    {
	player = -1;
        connected = false;

        //This is some necessary client stuff. You don't need
        //to change anything here.
        initGUI();

        try
        {
            addText("Connecting to localhost:" + KalahaMain.port);
            socket = new Socket("localhost", KalahaMain.port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            addText("Done");
            connected = true;
        }
        catch (Exception ex)
        {
            addText("Unable to connect to server");
            return;
        }
    }

    /**
     * Starts the client thread.
     */
    public void start()
    {
        //Don't change this
        if (connected)
        {
            thr = new Thread(this);
            thr.start();
        }
    }

    /**
     * Creates the GUI.
     */
    private void initGUI()
    {
        //Client GUI stuff. You don't need to change this.
        JFrame frame = new JFrame("My AI Client");
        frame.setLocation(Global.getClientXpos(), 445);
        frame.setSize(new Dimension(420,250));
        frame.getContentPane().setLayout(new FlowLayout());

        text = new JTextArea();
        JScrollPane pane = new JScrollPane(text);
        pane.setPreferredSize(new Dimension(400, 210));

        frame.getContentPane().add(pane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }

    /**
     * Adds a text string to the GUI textarea.
     *
     * @param txt The text to add
     */
    public void addText(String txt)
    {
        //Don't change this
        text.append(txt + "\n");
        text.setCaretPosition(text.getDocument().getLength());
    }

    /**
     * Thread for server communication. Checks when it is this
     * client's turn to make a move.
     */
    public void run()
    {
        String reply;
        running = true;

        try
        {
            while (running)
            {
                //Checks which player you are. No need to change this.
                if (player == -1)
                {
                    out.println(Commands.HELLO);
                    reply = in.readLine();

                    String tokens[] = reply.split(" ");
                    player = Integer.parseInt(tokens[1]);

                    addText("I am player " + player);
                }

                //Check if game has ended. No need to change this.
                out.println(Commands.WINNER);
                reply = in.readLine();
                if(reply.equals("1") || reply.equals("2") )
                {
                    int w = Integer.parseInt(reply);
                    if (w == player)
                    {
                        addText("I won!");
                    }
                    else
                    {
                        addText("I lost...");
                    }
                    running = false;
                }
                if(reply.equals("0"))
                {
                    addText("Even game!");
                    running = false;
                }

                //Check if it is my turn. If so, do a move
                out.println(Commands.NEXT_PLAYER);
                reply = in.readLine();
                if (!reply.equals(Errors.GAME_NOT_FULL) && running)
                {
                    int nextPlayer = Integer.parseInt(reply);

                    if(nextPlayer == player)
                    {
                        out.println(Commands.BOARD);
                        String currentBoardStr = in.readLine();
                        boolean validMove = false;
                        while (!validMove)
                        {
                            long startT = System.currentTimeMillis();
                            //This is the call to the function for making a move.
                            //You only need to change the contents in the getMove()
                            //function.
                            GameState currentBoard = new GameState(currentBoardStr);
                            int cMove = getMove(currentBoard);

                            //Timer stuff
                            long tot = System.currentTimeMillis() - startT;
                            double e = (double)tot / (double)1000;

                            out.println(Commands.MOVE + " " + cMove + " " + player);
                            reply = in.readLine();
                            if (!reply.startsWith("ERROR"))
                            {
                                validMove = true;
                                addText("Made move " + cMove + " in " + e + " secs");
                            }
                        }
                    }
                }

                //Wait
                Thread.sleep(100);
            }
	}
        catch (Exception ex)
        {
            running = false;
        }

        try
        {
            socket.close();
            addText("Disconnected from server");
        }
        catch (Exception ex)
        {
            addText("Error closing connection: " + ex.getMessage());
        }
    }

    /**
     * This is the method that makes a move each time it is your turn.
     * Here you need to change the call to the random method to your
     * Minimax search.
     *
     * @param currentBoard The current board state
     * @return Move to make (1-6)
     */
     public int getMove(GameState currentBoard)//this function is used to get the current Board state
     {
   {
       int move;
       move = DeepeningSearch(currentBoard);

       return move;
   }
   }
   public int DeepeningSearch(GameState gamest)// THis funcrion is used to deepen the search for the best possible move 
   {
       int bestcurrentMove = 0;
       int bestcurrentState = 9999999; //Arbritrarily high negative value
       int state;
       
     
       int gamerone = player;
       int gamertwo = player;
       
       int depth = 1;

       for(int i=0; i < 6 ; i++)
       {
           if(player == 1) {
                   gamerone = player;
               }
               else {
                   gamertwo = player;
               }
           long limit = System.currentTimeMillis() + (5000 / 6);
           while (true) {
               long now = System.currentTimeMillis();

                if (limit <= now) {
                   break;
               }
               
               if (gamest.moveIsPossible(i))
               {
                   GameState gamestate= (GameState) gamest.clone();
                   gamestate.makeMove(i);

                   //state=minMax(gamestate, depth,gamerone, 9999999, 9999999);
                   
                   state=minMax(gamestate, depth,gamertwo, 9999999, 9999999);

                   if(state>bestcurrentState)
                   {
                       bestcurrentState=state;
                       bestcurrentMove=i;
                   }
               }
               
           }
           depth++;
       }

       return bestcurrentMove;
   }
   
//Algoritm Implementation::::
	/* * 
	 * function minimax(gameinBoard,depth,player,alpha,beta)
	 * 
	 * if depth==0 or game over { return the static score }
	 * 
	 * if player is maximazing { maxValue= -Infinity
	 * 
	 * for each child of position
	 * eval=minimax(gameinBoard,depth-1,maximizingPlayer,alpha,beta)
	 * maxValue=max(maxValue,eval) alpha=max(alpha,eval)
	 * 
	 * if beta<=alpha break return maxValue }
	 * else {minValue= +Infinity for each child of position
	 * eval=minimax(GameInBoard,depth-1,player,alpha,beta)
	 * minValue=min(minValue,eval) beta=min(beta,eval)
	 * 
	 * if beta<=alpha break
	 * 
	 * return minValue }
	*/

   public int minMax(GameState minBoard, int depth,int player, int alpha, int beta)
   {
       int bestcurrentState = 0;
       int state;
       int gamerone = player;
       int gamertwo = player;
       if(player == 1) {
                   gamerone = player;
               }
               else {
                   gamertwo = player;
               }
       if(depth<=0)
           return getUtility(minBoard,player);

       for(int i = 0;i<6;i++)
       {
           if(minBoard.moveIsPossible(i)) {
           GameState gamestate= (GameState) minBoard.clone();
           gamestate.makeMove(i);

           if(player == gamertwo) {
               bestcurrentState = 9999999;
               state=minMax(gamestate,--depth,gamertwo, alpha, beta);
               bestcurrentState = Math.min(bestcurrentState, state);
               beta = Math.min(beta, bestcurrentState);
               if(beta <= alpha) {
                 break;
               }
           }

           else {
               bestcurrentState = -9999999;
              state=minMax(gamestate,--depth,gamerone, alpha, beta);
              bestcurrentState = Math.max(bestcurrentState, state);
              alpha = Math.max(alpha, bestcurrentState);
              if(beta <= alpha) {
                break;
              }
           }
          }
       }

       return bestcurrentState;
   }
 
   public int getUtility(GameState gamest,int player) {
       if(player == 1) {
           return (gamest.getScore(1) - gamest.getScore(2));
       }
       else {
           return (gamest.getScore(2) - gamest.getScore(1));
       }
   }
   
 }
