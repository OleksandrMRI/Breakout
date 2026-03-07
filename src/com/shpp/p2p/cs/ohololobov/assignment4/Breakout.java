package com.shpp.p2p.cs.ohololobov.assignment4;

import acm.graphics.*;
import acm.util.RandomGenerator;
import com.shpp.cs.a.graphics.WindowProgram;


import java.awt.*;
import java.awt.event.MouseEvent;


/**
 * This class is a copies of classic game Breakout, when player must reflect ball
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
    private static final int PADDLE_HEIGHT = 10;

    /**
     * Offset of the paddle up from the bottom
     */
    private static final int PADDLE_Y_OFFSET = 30;

    /**
     * Number of bricks per row
     */
    private static final int NBRICKS_PER_ROW = 10;

    /**
     * Number of rows of bricks
     */
    private static final int NBRICK_ROWS = 10;

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

    //velocity in the x and y direction
    private double vy = -3.0, vx;

    //  x and y coordinates of baseline of animation notification in the center of canvas
    private double xBaseLine;
    private double yBaseLine;

    //number of bricks, that decrease by one, everytime one brick removed
    private int counter = NBRICK_ROWS * NBRICKS_PER_ROW;

    //rectangle paddle
    private GRect paddle;

    //circle start ball, that moves synchronize with paddle, and removes in moment,
    // when creating independent moving ball1
    private GOval ball;

    // ball1 moves independent of paddle
    private GOval ball1;

    // labels of life1, life2, life3
    private GOval life1;
    private GOval life2;
    private GOval life3;

    //object from which the ball bounces
    private GObject collider;

    //label which show counter in window
    private GLabel counterBrick;

    // variable of velocity acceleration of ball
    private int dv = 5;

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
          This method draws ball of bricks.
         */
        drawWall();
        /*
           this method create balls of lives counter
         */
        createLivesBalls();
        pause(100);
        /*
          method create and show animation of greeting labels, and short instruction for begin game.
         */
        showGreetingInstruction();
    }

    /**
     * this method create 3 balls of lives counter in lower left angle of game window using the cycle;
     */
    private void createLivesBalls() {
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                life1 = createLifeCircle(i);
                add(life1);
            } else if (i == 1) {
                life2 = createLifeCircle(i);
                add(life2);
            } else {
                life3 = createLifeCircle(i);
                add(life3);
            }
        }
    }

    /**
     * this method draws one ball life circle according to numbers of life.
     *
     * @param i - displacement coefficient of x-start point of ball life circle.
     * @return - circle life
     */
    private GOval createLifeCircle(int i) {
        GOval life = new GOval(DIAMETER_BALL * i, getHeight() - DIAMETER_BALL, DIAMETER_BALL, DIAMETER_BALL);
        life.setColor(Color.RED);
        life.setFilled(true);
        return life;
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
        GLabel win = drawLabel(string, xWinLine, yWinLine, font, color);
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
        GLabel lost = drawLabel(string, xLossLine, yLossLine, font, color);
        add(lost, xBaseLine, yBaseLine);
    }

    /**
     * this method consist group of methods and cycles which draw movable objects in game,
     * drives all processes and calculates all events in game.
     */
    private void playGame() {
        //for cycle to create turns of game
        for (int i = 0; i < NTURNS; i++) {
            /*
               GRect paddle variable appropriates return value from getGRect method, which transmit parameters
               x and y start positions, sizes and colors of edge and filling paddle
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
              circle start ball variable (that moves synchronize with paddle, and removes in moment,
               when creating independent moving ball1) appropriates method getBall that return circle,
               and transmit parameters of x and y start positions, sizes and color of filling
             */
            ball = getBall(getWidth() / 2.0 - BALL_RADIUS,
                    getHeight() - PADDLE_Y_OFFSET - BALL_RADIUS * 2.0 - 5);

            /*
              method that activate pause of program, until player click let mouse button.
             */
            waitForClick();

            /*
              method adds ball
             */
            add(ball);

            /*
              method accepts as parameter variable i  from cycle and remove 1 life circle
              according to value variable i
             */
            removeLife(i);

            /*
              method accepts as parameter variable i  from cycle and shows animation of labels
              with instructions for creating ball and number of using ball.
             */
            showBallRunInstruction(i);

            /*
              method start the game(launches the creating and movement ball1, remove ball),
              when player move mouse left
             */
            startRound();

            /*
              circle start ball variable (that moves synchronize with paddle, and removes in moment,
               when creating independent moving ball1) appropriates method getBall that return circle,
               and transmit parameters of x and y start positions, sizes and color of filling ball1
               x and y start position is identical with x and y position of ball
             */
            ball1 = getBall(ball.getX(),
                    ball.getY());

            /*
              method adds ball1
             */
            add(ball1);

            /*
              method removes ball
             */
            remove(ball);

            pause(1);

            /*
              method include group of methods for starting independent flying,
              controlling of collision and reflection ball1,
              removing bricks and counting of remaining bricks.
             */
            moveOneRound();

            /*
              method removes ball1 when it falls lower then paddle
             */
            remove(ball1);

            /*
              method removes paddle
             */
            remove(paddle);

            // operator for finishing game when player win
            if (counter == 0) {
                return;
            }
            /*
              method shows label with number of loosing ball
             */
            showLoseBall(i);
            // operator include method which show label with instruction for starting next turn
            // if any attempts left
            if (i < 2) {
                /*
                  method which show label with instruction for starting next turn
                 */
                showInstruction();
            }
        }
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
     * method which show  label with number of loosing balls.
     */
    private void showLoseBall(int i) {
        //variables of x and y start points
        double xLoseLine = getWidth() / 2.0;
        double yLoseLine = getHeight() / 2.0;
        // variable String of number of loosing ball
        String loseBall = "";
        if (i == 0) {
            loseBall = "ONE";
        } else if (i == 1) {
            loseBall = "TWO";
        } else if (i == 2) {
            loseBall = "ALL";
        }
        // showing string
        String string = "You lose " + loseBall + " ball(s)";
        //font variable
        String font = "SansSerif-40";
        //color variable
        Color color = Color.GREEN;
        GLabel lose = drawLabel(string, xLoseLine, yLoseLine, font, color);
        add(lose, xBaseLine, yBaseLine);
        pause(1000);
        remove(lose);
    }

    /**
     * method remove one GOval from indicator of lives
     *
     * @param i - variable for definition of live circle.
     */
    private void removeLife(int i) {
        if (i == 0) {
            remove(life3);
        } else if (i == 1) {
            remove(life2);
        } else if (i == 2) {
            remove(life1);
        }
    }

    /**
     * method create animation label with instruction to run ball and write number of runnable ball
     * Labels is appearing on the border of  lower and middle third of game window
     *
     * @param i - - variable for counting balls
     */
    private void showBallRunInstruction(int i) {
        //variables of x and y start points
        double xRunLine = getWidth() / 2.0;
        double yRunLane = getHeight() * 2 / 3.0;
        // variable String of number of running ball
        String numberBall = "";
        if (i == 0) {
            numberBall = "FIRST";
        } else if (i == 1) {
            numberBall = "SECOND";
        } else if (i == 2) {
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
        drawChangedLabel(firstString, secondString, thirdString, xRunLine, yRunLane, font, color);
    }

    /**
     * method create animation label with greeting instruction
     * Labels is appearing in center of game window
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
                label = drawLabel(one, xBase, yBase, font, color);
            } else if (i == 1) {
                label = drawLabel(two, xBase, yBase, font, color);
            } else {
                label = drawLabel(three, xBase, yBase, font, color);
            }
            add(label, xBaseLine, yBaseLine);
            pause(900);
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
    private GLabel drawLabel(String string, double xBase, double yBase, String font, Color color) {
        GLabel creatingLabel = new GLabel(string);
        creatingLabel.setFont(font);
        creatingLabel.setColor(color);

        xBaseLine = xBase - creatingLabel.getWidth() / 2;
        yBaseLine = yBase - creatingLabel.getAscent();
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
     */
    private void moveOneRound() {
        //create random vx value
        RandomGenerator rgen = RandomGenerator.getInstance();
        vx = rgen.nextDouble(1.0, 3.0);
        if (rgen.nextBoolean(0.5))
            vx = -vx;

        // cycle that includes logic of movement, reflections of ball? removing and counting bricks
        while (ball1.getY() < getHeight()) {
            //start movement of ball
            ball1.move(vx, vy);

            pause(8 - dv / DECREASING_COEFFICIENT);
            /*
              method that describes logic of reflection of ball in different conditions
             */
            reversV();

            /*
              method counts and removes brick
             */
            countAndRemove();
            // operator, when label counterBrick not equals null remove it, for next creating new counterBrick
            if (counterBrick != null) {
                remove(counterBrick);
            }
            /*
              method creates label counterBrick
             */
            createCounterBrick();
            // condition when player win, game is finish.
            if (counter == 0) {
                return;
            }
        }
    }

    /**
     * method creates and shows label counterBrick
     */
    private void createCounterBrick() {
        counterBrick = new GLabel("left " + counter + " brick(s)");
        counterBrick.setColor(Color.black);
        counterBrick.setFont("SanSerif-20");
        double xBaseLineCounter = getWidth() - counterBrick.getWidth();
        double yBaseLineCounter = getHeight() - counterBrick.getDescent();

        add(counterBrick, xBaseLineCounter, yBaseLineCounter);
    }

    /**
     * in this method if collider equal brick this brick removes and counter decreasing in one
     */
    private void countAndRemove() {
        if (collider != paddle && collider != counterBrick &&
                collider != life1 &&
                collider != life2 &&
                collider != null) {
            remove(collider);

            if (collider != paddle && collider != counterBrick) {
                counter--;
                dv++;
            }
        }
    }

    /**
     * method create logic of reflection of ball in different condition
     */
    private void reversV() {
        //Array of 8 check points of ball
        // Numbering of points in array is clockwise and begin from upper left point:
        //         0-upper left Point, 1-upper midl point,
        //         2-upper right point, 3-right midl point,
        //         4-lower right point, 5-lower midl point,
        //         6-lower left point, 7-left midl point;
        //
        GObject[] ballPoints = {
                (getElementAt(ball1.getX(), ball1.getY())),
                (getElementAt(ball1.getX() + BALL_RADIUS, ball1.getY() - COMPENSATION)),
                (getElementAt(ball1.getX() + BALL_RADIUS * 2.0, ball1.getY())),
                (getElementAt(ball1.getX() + BALL_RADIUS * 2.0 + COMPENSATION,
                        ball1.getY() + BALL_RADIUS)),
                (getElementAt(ball1.getX() + BALL_RADIUS * 2.0,
                        ball1.getY() + BALL_RADIUS * 2.0)),
                (getElementAt(ball1.getX() + BALL_RADIUS,
                        ball1.getY() + BALL_RADIUS * 2.0 + COMPENSATION)),
                (getElementAt(ball1.getX(), ball1.getY() + BALL_RADIUS * 2.0)),
                (getElementAt(ball1.getX() - COMPENSATION, ball1.getY() + BALL_RADIUS))
        };

        // appropriation variable collider with return value of method getCollidingObject, which accepts as parameter
        //GObject [] ballPoints
        collider = getCollidingObject(ballPoints);
        // definition logic of ball reflection
        if (collider != counterBrick && collider != life1 &&
                collider != life2 || collider == null) {
            if (isReversX(ballPoints)) {
                vx = -vx;
            }
            if (isReversY(ballPoints)) {
                vy = -vy;
            }
            if (isReversXY(ballPoints)) {
                vx = -vx;
                vy = -vy;
            }
        }
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
     * @param ballPoints - array GObject [] ballPoints
     * @return - boolean resXY
     */
    private boolean isReversXY(GObject[] ballPoints) {
        boolean resXY = false;

        if (ballPoints[0] != null && vx < 0 && vy < 0) {
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
     * @param ballPoints - array GObject [] ballPoints
     * @return - boolean resX
     */
    private boolean isReversX(GObject[] ballPoints) {
        boolean resX = false;
        if (ball1.getX() >= getWidth() - BALL_RADIUS * 2.0 && vx > 0 || ball1.getX() <= 0 && vx < 0) {
            resX = true;
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
     * @param ballPoints - array GObject [] ballPoints
     * @return - boolean resY
     */
    private boolean isReversY(GObject[] ballPoints) {
        boolean resY = false;
        if (ball1.getY() < 0 && vy < 0) {
            resY = true;
        } else if (ballPoints[1] != null && vy <= 1) {
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
     * @return - oval
     */
    private GOval getBall(double ballStartX, double ballStartY) {
        GOval oval = new GOval(ballStartX,
                ballStartY,
                DIAMETER_BALL,
                DIAMETER_BALL);
        oval.setColor(Color.gray);
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
        //variable of x start point coordinate of paddle depends on mouse pointer, before ball1 create
        double newBallX;

        if (e.getX() >= paddle.getWidth() / 2.0 && e.getX() <= getWidth() - paddle.getWidth() / 2.0) {
            //calculating  x start points coordinates of paddle and ball depends on mouse pointer
            newPaddleX = e.getX() - paddle.getWidth() / 2;
            newBallX = e.getX() - BALL_RADIUS;

            // binding x start points coordinates of paddle and ball  to x coordinate of mouse pointer
            paddle.setLocation(newPaddleX, getHeight() - PADDLE_Y_OFFSET);
            ball.setLocation(newBallX, getHeight() - PADDLE_Y_OFFSET - BALL_RADIUS * 2.0 - 5);

            // edge case when mouse pointer quickly leaves boundaries of canvas left
        } else if (e.getX() < paddle.getWidth() / 2.0) {
            paddle.setLocation(0, getHeight() - PADDLE_Y_OFFSET);
            ball.setLocation(paddle.getWidth() / 2 - BALL_RADIUS, getHeight() - PADDLE_Y_OFFSET - BALL_RADIUS * 2.0 - 5);
            // edge case when mouse pointer quickly leaves boundaries of canvas right
        } else if (e.getX() > getWidth() - paddle.getWidth() / 2.0) {
            paddle.setLocation(getWidth() - paddle.getWidth(),
                    getHeight() - PADDLE_Y_OFFSET);
            ball.setLocation(getWidth() - paddle.getWidth() / 2.0 - BALL_RADIUS, getHeight() - PADDLE_Y_OFFSET - BALL_RADIUS * 2.0 - 5);
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
                Color color = colorOfRow(row);
                drawBrick(startPointX, startPointY, row, col, color);
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
    private Color colorOfRow(int row) {
        Color rowColor = null;
        if (row <= 1) {
            rowColor = Color.RED;
        } else if (row <= 3) {
            rowColor = Color.ORANGE;
        } else if (row <= 5) {
            rowColor = Color.YELLOW;
        } else if (row <= 7) {
            rowColor = Color.GREEN;
        } else if (row <= 9) {
            rowColor = Color.CYAN;
        }
        return rowColor;
    }


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



