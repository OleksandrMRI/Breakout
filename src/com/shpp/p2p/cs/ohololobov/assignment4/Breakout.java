package com.shpp.p2p.cs.ohololobov.assignment4;

import acm.graphics.*;
import acm.util.RandomGenerator;
import com.shpp.cs.a.graphics.WindowProgram;


import java.awt.*;
import java.awt.event.MouseEvent;


public class Breakout extends WindowProgram {
    /**
     * Width and height of application window in pixels
     */
    public static final int APPLICATION_WIDTH = 400;
    public static final int APPLICATION_HEIGHT = 700;

    /**
     * Dimensions of game board (usually the same)
     */
    private static final int WIDTH = APPLICATION_WIDTH;
    private static final int HEIGHT = APPLICATION_HEIGHT;

    /**
     * Dimensions of the paddle
     */
    private static final int PADDLE_WIDTH = 90;
    private static final int PADDLE_HEIGHT = 150;

    /**
     * Offset of the paddle up from the bottom
     */
    private static final int PADDLE_Y_OFFSET = 170;

    /**
     * Number of bricks per row
     */
    private static final int NBRICKS_PER_ROW = 10;

    /**
     * Number of rows of bricks
     */
    private static final int NBRICK_ROWS = 15;

    /**
     * Separation between bricks
     */
    private static final int BRICK_SEP = 4;

    /**
     * Width of a brick
     */
    private static final int BRICK_WIDTH =
            (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

    /**
     * Height of a brick
     */
    private static final int BRICK_HEIGHT = 8;

    /**
     * Radius of the ball in pixels
     */
    private static final int BALL_RADIUS = 10;

    private static final int DIAMETER_BALL = BALL_RADIUS * 2;

    /**
     * Offset of the top brick row from the top
     */
    private static final int BRICK_Y_OFFSET = 70;

    /**
     * Number of turns
     */
    private static final int NTURNS = 3;

    /**
     * decreasing coefficient of velocity acceleration
     */
    private static final double DECREASING_COEFFICIENT = 20;

    //Variables of class:

    //Color of paddle
    private static final Color BROWN = new Color(255, 222, 173);

    //pixel compensation, for odd points of ball, without this compensation
    // ball incorrect interacts with objects.
    private static final double COMPENSATION = 2;


    //  x and y coordinates of baseline of animation notification in the center of canvas
    private double xBaseLine;
    private double yBaseLine;

    //rectangle paddle
    private GRect paddle;

    //number of bricks, that decrease by one, everytime one brick removed
    private int counter = NBRICK_ROWS * NBRICKS_PER_ROW;

    //variable of counter label in canvas
    private GLabel counterBrick = null;

    // variable of acceleration velocity of ball, by decreasing pause between ball movements
    int dv = 5;



    /**
     * This method consist group of methods that create copies of classic game Breakout, when player must reflect ball
     * by paddle (movable rectangle platform) in bottom of game window
     * and crushed wall of bricks in upper side of game window brick by brick.
     * Player has 3 attempts to remove all bricks.
     * When ball moves, it meets an obstacle it is reflected according to the laws of physics.
     * When ball reflects from brick, that brick removed.
     * When paddle not catches a ball, and ball touches bottom edge of game window, player lose 1 attempt.
     * Game is over when user crushed all bricks, or lost all attempts.
     * In this variant of game ball starts fly from paddle up
     * with start velocity by y direction, and random velocity by x direction.
     */
    public void run() {
        /*
          in this method in game window drawing start elements:
          wall ob bricks, lives circles and shows first greeting labels.
         */
        drawStartElements();

        /*
          this method consist group of methods which draw movable objects in game,
          drives all processes and calculates all events in game.
         */
        playGame();

        /*
          method shows result of game with labels in game window(win or loss).
         */
        showResultGame();
    }

    /**
     * in this method in game window drawing start elements:
     * wall ob bricks, lives circles and shows first greeting labels.
     */
    private void drawStartElements() {
        /*
          this method draws ball of bricks.
         */
        drawWall();

        pause(1000);
        /*
          method create and show animation of greeting labels? and short instruction for begin game.
         */
        showGreetingInstruction();
    }


    /**
     * method shows result ("win" or "game over")
     */
    private void showResultGame() {
        if (counter == 0) {
            //show win label
            showWinNotification();
        } else {
            //show lost label
            showLostNotification();
        }
    }

    /**
     * method shows label with "You WIN:)" notification in the center of game window
     */
    private void showWinNotification() {
        //variables of x and y start points
        double xWinLine = getWidth() / 2.0;
        double yWinLine = getHeight() / 2.0;
        // showing string
        String string = "You WIN:)";
        //font variable
        String font = "SansSerif-40";
        //color variable
        Color color = Color.GREEN;
        GLabel win = getLabel(string, xWinLine, yWinLine, font, color);
        add(win, xBaseLine, yBaseLine);
    }

    /**
     * method shows label with "GAME OVER:(" notification in the center of game window
     */
    private void showLostNotification() {
        //variables of x and y start points
        double xLossLine = getWidth() / 2.0;
        double yLossLine = getHeight() / 2.0;
        // showing string
        String string = "GAME OVER:(";
        //font variable
        String font = "SansSerif-40";
        //color variable
        Color color = Color.RED;
        GLabel lost = getLabel(string, xLossLine, yLossLine, font, color);
        add(lost, xBaseLine, yBaseLine);
    }

    /**
     * this method consist group of methods and cycles which draw movable objects in game,
     * drives all processes and calculates all events in game.
     */
    private void playGame() {
        // creating array with GOvals ball life
        GOval[] lifeBalls = lifeBallsArray();
        // add GOvals in array to canvas
        for (GOval lifeBall : lifeBalls) {
            add(lifeBall);
        }


        //for cycle to create turns of game
        for (int turn = 0; turn < NTURNS; turn++) {
            /*
               GRect paddle variable appropriates return value from getGRect method, which transmit parameters
               x and y start positions
             */
            paddle = getGRect((getWidth() - PADDLE_WIDTH) / 2.0, getHeight() - PADDLE_Y_OFFSET);

            /*
              add paddle;
             */
            add(paddle);

            /*
              method for listing mouse events;
             */
            addMouseListeners();

            /*
              method that activate pause of program? until player click let mouse button.
             */
            waitForClick();

            /*
              circle  ball variable  appropriates method getBall that return circle,
              and transmit parameters of x and y start positions
             */
            GOval ball = getBall(getWidth() / 2.0 - BALL_RADIUS,
                    getHeight() / 2.0 - BALL_RADIUS, Color.DARK_GRAY);

            /*
              method adds ball
             */
            add(ball);

            /*
              method accepts as parameter variable turn from cycle and lifeBalls, removes 1 life circle
              according to value variable turn
             */
            removeLife(turn, lifeBalls);

            /*
              method accepts as parameter variable turn  from cycle and shows animation of labels
              with instructions for creating ball and number of using ball.
             */
            showBallRunInstruction(turn);

            /*
              method start the game, launches the ball, when player move mouse left
             */
            startRound();

            pause(10);

            /*
              method include group of methods for starting independent flying,
              controlling of collision and reflection ball,
              removing bricks and counting of remaining bricks.
             */
            playOneTurn(ball, lifeBalls);

            /*
              method removes ball when it falls lower then paddle
             */
            remove(ball);

            /*
              method removes paddle in the end of turn
             */
            remove(paddle);

            // operator for finishing game when player win
            if (counter == 0) {
                return;
            }

            /*
              method shows label with number of loosing ball
             */
            showLoseBall(turn);

            // operator include method which show label with instruction for starting next turn
            // if any attempts left
            if (turn < 2) {
                /*
                  method which show label with instruction for starting next turn
                 */
                showInstruction();
            }
        }
    }

    /**
     * This method fills array with GOvals (indicators of ball lives)
     *
     * @return - filling array
     */
    private GOval[] lifeBallsArray() {
        GOval[] ovals = new GOval[3];
        for (int life = 0; life < NTURNS; life++) {
            /*
             this cycle creates and add balls of lives
            */
            Color lifeColor = Color.RED;
            if (life == 0) {
                ovals[0] = getBall(DIAMETER_BALL * life, getHeight() - DIAMETER_BALL, lifeColor);

            } else if (life == 1) {
                ovals[1] = getBall(DIAMETER_BALL * life, getHeight() - DIAMETER_BALL, lifeColor);

            } else {
                ovals[2] = getBall(DIAMETER_BALL * life, getHeight() - DIAMETER_BALL, lifeColor);
            }
        }
        return ovals;
    }

    /**
     * method which show animation label with instruction for starting next turn
     */
    private void showInstruction() {
        //variables of x and y start points
        double xGreetingLine = getWidth() / 2.0;
        double yGreetingLane = getHeight() / 2.0;
        // showing strings
        String one = "";
        String two = "press left mouse button";
        String three = "to create ball";
        //font variable
        String font = "SansSerif-35";
        //color variable
        Color color = Color.DARK_GRAY;
        drawChangedLabel(one, two, three, xGreetingLine, yGreetingLane, font, color);
    }

    /**
     * method which show label with number of loosing balls.
     *
     * @param turn - number of turn
     */
    private void showLoseBall(int turn) {
        //variables of x and y start points
        double xLoseLine = getWidth() / 2.0;
        double yLoseLine = getHeight() / 2.0;
        // variable String of number of loosing ball
        String loseBall = "";
        if (turn == 0) {
            loseBall = "ONE";
        } else if (turn == 1) {
            loseBall = "TWO";
        } else if (turn == 2) {
            loseBall = "ALL";
        }
        // showing string
        String string = "You lose " + loseBall + " ball(s)";
        //font variable
        String font = "SansSerif-40";
        //color variable
        Color color = Color.GREEN;
        GLabel lose = getLabel(string, xLoseLine, yLoseLine, font, color);
        add(lose, xBaseLine, yBaseLine);
        pause(1000);
        remove(lose);
    }

    /**
     * method remove one GOval from indicator of lives
     *
     * @param turn - number of turn (variable for definition of live circle)
     */
    private void removeLife(int turn, GOval[] lifeBalls) {
        if (turn == 0) {
            remove(lifeBalls[2]);
        } else if (turn == 1) {
            remove(lifeBalls[1]);
        } else if (turn == 2) {
            remove(lifeBalls[0]);
        }
    }

    /**
     * method create animation label with instruction to run ball and write number of runnable ball
     * Labels is appears on the border of  lower and middle third of game window
     *
     * @param turn - number of turn (variable for counting balls)
     */
    private void showBallRunInstruction(int turn) {
        //variables of x and y start points
        double xRunLine = getWidth() / 2.0;
        double yRunLane = getHeight() * 2 / 3.0;
        // variable String of number of running ball
        String numberBall = "";
        if (turn == 0) {
            numberBall = "FIRST";
        } else if (turn == 1) {
            numberBall = "SECOND";
        } else if (turn == 2) {
            numberBall = "LAST";
        }
        // showing strings
        String firstString = "To run " + numberBall + " ball";
        String secondString = "MOVE";
        String thirdString = "LEFT";
        //font variable
        String font = "SansSerif-40";
        //color variable
        Color color = Color.CYAN;
        // method create animation labels
        drawChangedLabel(firstString, secondString, thirdString, xRunLine, yRunLane, font, color);
    }

    /**
     * method create animation label with greeting instruction
     * Labels is appears in center of game window
     */
    private void showGreetingInstruction() {
        //variables of x and y start points
        double xGreetingLine = getWidth() / 2.0;
        double yGreetingLane = getHeight() / 2.0;
        // showing strings
        String stringOne = "Yor have 3 balls";
        String stringTwo = "press left mouse button";
        String stringThree = "to create ball";
        //font variable
        String font = "SansSerif-35";
        //color variable
        Color color = Color.DARK_GRAY;
        // method create animation labels
        drawChangedLabel(stringOne, stringTwo, stringThree, xGreetingLine, yGreetingLane, font, color);
    }

    /**
     * method create tree independent  variables label
     *
     * @param one   - first string
     * @param two   - second string
     * @param three - third string
     * @param xBase - x baseline point
     * @param yBase - y baseline point
     * @param font  - font
     * @param color - color of label
     */
    private void drawChangedLabel(String one,
                                  String two,
                                  String three,
                                  double xBase,
                                  double yBase,
                                  String font,
                                  Color color) {

        for (int i = 0; i < 3; i++) {
            //common animation labels that change each other  in central part window.
            GLabel label;
            if (i == 0) {
                //getLabel - method create and return one label
                label = getLabel(one, xBase, yBase, font, color);
            } else if (i == 1) {
                label = getLabel(two, xBase, yBase, font, color);
            } else {
                label = getLabel(three, xBase, yBase, font, color);
            }
            add(label, xBaseLine, yBaseLine);
            pause(1000);
            remove(label);
        }
    }

    /**
     * Method draws one label
     *
     * @param string - string for showing
     * @param xBase  - x baseline point
     * @param yBase  - y baseline point
     * @param font   - font
     * @param color  - color
     * @return - creatingLabel GLabel
     */
    private GLabel getLabel(String string, double xBase, double yBase, String font, Color color) {
        GLabel creatingLabel = new GLabel(string);
        creatingLabel.setFont(font);
        creatingLabel.setColor(color);

        xBaseLine = xBase - creatingLabel.getWidth() / 2;
        yBaseLine = yBase - creatingLabel.getDescent();
        return creatingLabel;
    }

    /**
     * after implementation of this method ball start flying.
     * Method creates a pause in game until player move mouse left
     */
    private void startRound() {
        boolean wait = true;
        while (wait) {
            double x = paddle.getX();
            pause(0.1);
            double y = paddle.getX();
            if (x > y) {
                wait = false;
            }
        }
    }

    /**
     * method calculates condition for flying ball from start position,
     * and launches ball, drive the reflection of ball and removing of bricks
     *
     * @param ball      - GOval ball
     * @param lifeBalls - array of GOvals of counter of lives GOval[] lifeBalls
     */
    private void playOneTurn(GOval ball, GOval[] lifeBalls) {
        //create random vx value
        RandomGenerator rgen = RandomGenerator.getInstance();
        //random velocity at x direction
        double vx = rgen.nextDouble(1.0, 3.0);
        if (rgen.nextBoolean(0.5))
            vx = -vx;

        //velocity at y direction
        double vy = 3.0;

        // cycle that includes logic of movement, reflections of ball, removing and counting bricks
        while (ball.getY() < getHeight()) {
            // assignment of movement of ball with velocity vx and vy;
            ball.move(vx, vy);
            //pause between steps of ball decreases depends on velocity acceleration of ball and
            //coefficient of velocity acceleration
            pause(40 - dv / DECREASING_COEFFICIENT);

            //when counterBrick is not equal null, remove label for repainting it with new value
            if (counterBrick != null) {
                remove(counterBrick);
            }
            // assignment of return value of getLabel() to counterBrick;
            counterBrick = getLabel("left " + counter + " brick(s)",
                    getWidth()/2.0,
                    getHeight(),
                    "SanSerif-20",
                    Color.BLACK);
            /*
             method creates label counterBrick
            */
            add(counterBrick, xBaseLine, yBaseLine);

            //Initialization of array with eight check points of ball
            GObject[] ballPoints = getgObjects(ball);

            // Initialization of object from which the ball bounces
            // appropriation variable collider with return value of method getCollidingObject,
            // which accepts as parameter GObject [] ballPoints
            GObject collider = getCollidingObject(ballPoints);

            /*
              methods reversX() and reversY create logic of reflection of ball in different conditions
              accepts parameters collider, ballPoints, vx, vy, ball, lifeBalls, counterBrick
              and return new values of vx and vy
             */
            vx = reversX(collider, ballPoints, vx, vy, ball, lifeBalls, counterBrick);
            vy = reversY(collider, ballPoints, vx, vy, ball, lifeBalls, counterBrick);

            /*
              method isRemove return boolean, if value equal true, it means that collider equal brick,
              and this collider removed
              isRemove accepts parameters object collider, ovals lifeBalls and label counterBrick
             */
            if (isRemove(collider, lifeBalls, counterBrick)) {
                remove(collider);
                //when one collider remove value of counter of bricks decreases by one
                --counter;
                //value of velocity acceleration increases by one.
                dv++;
            }

            // condition when player win, game is finish.
            if (counter == 0) {
                return;
            }
        }
    }

    /**
     * in this method checking if collider equal brick, counter decreasing in one
     *
     * @param collider     - object Collider
     * @param lifeBalls    - array of GOvals of counter of lives GOval[] lifeBalls
     * @param counterBrick - GLabel which shows how many brick are left
     * @return - boolean isRemove = true (if collider == brick)
     */
    private boolean isRemove(GObject collider, GOval[] lifeBalls, GLabel counterBrick) {
        //checking variable
        boolean isRemove = false;
        if (collider != paddle && collider != counterBrick &&
                collider != lifeBalls[0] &&
                collider != lifeBalls[1] &&
                collider != null) {
            isRemove = true;
        }
        return isRemove;
    }

    /**
     * method create logic of x reflection of ball in different condition
     *
     * @param collider     - object collider
     * @param ballPoints   - GObject[] ballPoints - array with ball check points
     * @param vx           - x velocity
     * @param vy           - y velocity
     * @param ball         - GOval ball
     * @param lifeBalls    - array of GOvals of counter of lives GOval[] lifeBalls
     * @param counterBrick - GLabel which shows how many brick are left
     * @return - double resultX
     */
    private double reversX(GObject collider,
                           GObject[] ballPoints,
                           double vx,
                           double vy,
                           GOval ball,
                           GOval[] lifeBalls,
                           GLabel counterBrick) {
        double resultX = vx;
        // definition logic of ball reflection
        if (collider != counterBrick && collider != lifeBalls[0] &&
                collider != lifeBalls[1] || collider == null) {
            if (isReversX(ballPoints, vx, ball)) {
                resultX = -vx;
            } else if (isReversXY(ballPoints, vx, vy)) {
                resultX = -vx;
            }
        }
        return resultX;
    }

    /**
     * method create logic of y reflection of ball in different condition
     *
     * @param collider     - object collider
     * @param ballPoints   - GObject[] ballPoints - array with ball check points
     * @param vx           - x velocity
     * @param vy           - y velocity
     * @param ball         - GOval ball
     * @param lifeBalls    - array of GOvals of counter of lives GOval[] lifeBalls
     * @param counterBrick - GLabel which shows how many brick are left
     * @return - double resultX
     */
    private double reversY(GObject collider,
                           GObject[] ballPoints,
                           double vx,
                           double vy,
                           GOval ball,
                           GOval[] lifeBalls,
                           GLabel counterBrick) {
        double resultY = vy;
        // definition logic of ball reflection
        if (collider != counterBrick && collider != lifeBalls[0] &&
                collider != lifeBalls[1] || collider == null) {
            if (isReversY(ballPoints, vy, ball)) {
                resultY = -vy;
            }
            if (isReversXY(ballPoints, vx, vy)) {
                resultY = -vy;
            }
        }
        return resultY;
    }

    private GObject[] getgObjects(GOval ball) {
        //Array of 8 check points of ball
        // Numbering of points in array is clockwise and begin from upper left point:
        //         0-upper left Point, 1-upper midl point,
        //         2-upper right point, 3-right midl point,
        //         4-lower right point, 5-lower midl point,
        //         6-lower left point, 7-left midl point;

        return new GObject[]{
                (getElementAt(ball.getX(), ball.getY())),
                (getElementAt(ball.getX() + BALL_RADIUS, ball.getY() - COMPENSATION)),
                (getElementAt(ball.getX() + BALL_RADIUS * 2.0, ball.getY())),
                (getElementAt(ball.getX() + BALL_RADIUS * 2.0 + COMPENSATION,
                        ball.getY() + BALL_RADIUS)),
                (getElementAt(ball.getX() + BALL_RADIUS * 2.0,
                        ball.getY() + BALL_RADIUS * 2.0)),
                (getElementAt(ball.getX() + BALL_RADIUS,
                        ball.getY() + BALL_RADIUS * 2.0 + COMPENSATION)),
                (getElementAt(ball.getX(), ball.getY() + BALL_RADIUS * 2.0)),
                (getElementAt(ball.getX() - COMPENSATION, ball.getY() + BALL_RADIUS))
        };
    }

    /**
     * creating  colliding depends on contacts ball with another objects in check points.
     *
     * @param ballPoints - array GObject [] ballPoints
     * @return - selectedObject (contact object).
     */
    private GObject getCollidingObject(GObject[] ballPoints) {
        GObject selectedObject = null;
        for (GObject ballPoint : ballPoints) {
            if (ballPoint != null) {
                selectedObject = ballPoint;
            }
        }
        return selectedObject;
    }

    /**
     * method describe condition when in reflection revers sign of vx and vy value.
     *
     * @param ballPoints - array of ball check points GObject [] ballPoints
     * @param vx         - x velocity of ball
     * @param vy         - y velocity of ball
     * @return - boolean resXY
     */
    private boolean isReversXY(GObject[] ballPoints, double vx, double vy) {
        boolean resXY = false;

        if (ballPoints[3] != null && ballPoints[3] == paddle&&vy>0) {
            resXY = false;
        } else if (ballPoints[7] != null && ballPoints[7] == paddle&&vy>0) {
            resXY = false;
        } else if (ballPoints[0] != null && vx < 0 && vy < 0) {
            resXY = true;
        } else if (ballPoints[2] != null && vx > 0 && vy < 0) {
            resXY = true;
        } else if (ballPoints[4] != null && vx > 0 && vy > 0) {
            resXY = true;
        } else if (ballPoints[6] != null && vx < 0 && vy > 0) {
            resXY = true;
        }
        return resXY;
    }

    /**
     * method describe condition when in reflection revers sign of vx value.
     *
     * @param ballPoints - array of ball check points GObject [] ballPoints
     * @param vx         - x velocity of ball
     * @param ball       - - ball GOval
     * @return - boolean resX
     */
    private boolean isReversX(GObject[] ballPoints, double vx, GOval ball) {
        boolean resX = false;

        if (ball.getX() >= getWidth() - BALL_RADIUS * 2.0 && vx > 0 || ball.getX() <= 0 && vx < 0) {
            resX = true;
        } else if (ballPoints[7] != null && ballPoints[7] == paddle&&vx>0) {
            resX = false;
        } else if (ballPoints[3] != null && ballPoints[3] == paddle&vx<0) {
            resX = false;
        } else if (ballPoints[3] != null && vx >= 0) {
            resX = true;
        } else if (ballPoints[7] != null && vx <= 0) {
            resX = true;
        }
        return resX;
    }

    /**
     * method describe condition when in reflection revers sign of vy value.
     *
     * @param ballPoints - array of ball check points GObject [] ballPoints
     * @param vy         - y velocity of ball
     * @param ball       - ball GOval
     * @return - boolean resY
     */
    private boolean isReversY(GObject[] ballPoints, double vy, GOval ball) {
        boolean resY = false;
        if (ball.getY() < 0&& vy < 0) {
            resY = true;
        } else if (ballPoints[3] != null && ballPoints[3]==paddle) {
            resY = false;
        } else if (ballPoints[7] != null && ballPoints[7]==paddle){
            resY = false;
        }else if (ballPoints[1] != null && vy <= 1) {
            resY = true;
        } else if (ballPoints[5] != null && vy >= 0) {
            resY = true;
        }
        return resY;
    }

    /**
     * Method create circle ball
     *
     * @param ballStartX - x start point
     * @param ballStartY - y start point
     * @param color      - color of filling
     * @return - oval
     */
    private GOval getBall(double ballStartX, double ballStartY, Color color) {
        GOval oval = new GOval(ballStartX,
                ballStartY,
                DIAMETER_BALL,
                DIAMETER_BALL);
        oval.setColor(color);
        oval.setFilled(true);
        return oval;
    }

    /**
     * method that creates listening of mouse move events
     *
     * @param e - event of mouse movement
     */
    public void mouseMoved(MouseEvent e) {
        //variable of x start point coordinate of paddle depends on mouse pointer
        double newPaddleX;
        if (e.getX() >= paddle.getWidth() / 2.0 && e.getX() <= getWidth() - paddle.getWidth() / 2.0) {

            //calculating  x start points coordinates of paddle depends on mouse pointer
            newPaddleX = e.getX() - paddle.getWidth() / 2;
            paddle.setLocation(newPaddleX, getHeight() - PADDLE_Y_OFFSET);

            // edge case when mouse pointer quickly leaves boundaries of canvas left
        } else if (e.getX() < paddle.getWidth() / 2.0) {
            paddle.setLocation(0, getHeight() - PADDLE_Y_OFFSET);
            // edge case when mouse pointer quickly leaves boundaries of canvas right
        } else if (e.getX() > getWidth() - paddle.getWidth() / 2.0) {
            paddle.setLocation(getWidth() - paddle.getWidth(),
                    getHeight() - PADDLE_Y_OFFSET);
        }
    }

    /**
     * method create GRect
     *
     * @param startX - x start point
     * @param startY - y start point
     * @return - rect(GRect)
     */
    private GRect getGRect(double startX, double startY) {

        GRect rect = new GRect(startX,
                startY,
                PADDLE_WIDTH,
                PADDLE_HEIGHT);
        rect.setColor(Color.GREEN);
        rect.setFilled(true);
        rect.setFillColor(BROWN);
        return rect;
    }

    /**
     * method accepts as variables rows and collumns
     * (in this case equal constants NUM_ROWS, NUM_COLS)
     * and build matrix row by row;
     */
    private void drawWall() {
        for (int row = 0; row < NBRICK_ROWS; row++) {
            for (int col = 0; col < NBRICKS_PER_ROW; col++) {
                // call method which build one box (item);
                // which takes the value of arguments number of row
                // and number of collumn;

                //x and y start points (calculating)
                double startPointX = (getWidth() - ((BRICK_WIDTH + BRICK_SEP) * NBRICKS_PER_ROW - BRICK_SEP)) / 2.0;
                double startPointY = (BRICK_Y_OFFSET + ((BRICK_HEIGHT + BRICK_SEP) * NBRICK_ROWS - BRICK_SEP)) / 2.0;
                Color[] color = {Color.RED,Color.ORANGE,Color.YELLOW,Color.GREEN,Color.CYAN};
                Color rowColor=color[row%10/2];

                drawBrick(startPointX, startPointY, row, col, rowColor);
                pause(5);
            }
        }
    }

    /**
     * method create color of row
     *
     * @param row - row number
     * @return - rowColor
     */
//    private Color colorOfRow(int row) {
//        Color[] color = {Color.RED,Color.ORANGE,Color.YELLOW,Color.GREEN,Color.CYAN};
//
//        return rowColor;
//    }


    // method accepts as variables number of row and number of collumn
    // and build one box, with coordinates depending on number of row and number of collumn,
    // with size equal value of constant  BOX_SIZE
    // and with space between boxes equal value of constant BOX_SPACING;

    /**
     * method builds one brick, with coordinates depending on number of row and number of collumn,
     * with sizes equal value of constant  BRICK_WIDTH and BRICK_HEIGHT;
     * and with space between boxes equal value of constant BRICK_SEP;
     *
     * @param startPointX   - x start point
     * @param startPointY   - y start point
     * @param rowNumber     - number of row
     * @param collumnNumber - number of collumn
     * @param color         - color of filling brick
     */
    private void drawBrick(double startPointX, double startPointY, int rowNumber, int collumnNumber, Color color) {


        GRect brick = new GRect(startPointX + collumnNumber * (BRICK_WIDTH + BRICK_SEP),
                startPointY + rowNumber * (BRICK_HEIGHT + BRICK_SEP),
                BRICK_WIDTH,
                BRICK_HEIGHT
        );

        brick.setColor(Color.GRAY);
        brick.setFilled(true);

        brick.setFillColor(color);

        add(brick);
    }
}
