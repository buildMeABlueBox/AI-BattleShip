/**
 * Created by AJ on 12/23/14.
 */
import java.util.Scanner;
public class Player {
    private char[][] coordinateBoard = new char[10][10];
    private char[][] placedShipsBoard = new char[10][10];
    private boolean[][] visited = new boolean[10][10];

    public Player(){

    }
    /**
     * This player is being fired upon. Given integer coordinates, updates board(s) accordingly.
     * Return - character 'M' if it was a miss, 'H' if it was a hit.
     */
    public char fireUpon(boolean computer){
        boolean moveOn = true;
        int xCord;
        int yCord;
        do {
            Coordinate cord = computer == true? getCoordinate(true) : getCoordinate(false);

            xCord = cord.getX();
            yCord = cord.getY();
            //player has already hit this target.
            if (visited[xCord][yCord]){
                System.out.println("You've already chosen this coordinate before. Choose another one.");
                moveOn = false;
            }
        }while(!moveOn);
        
        //update visited/coordinate boards. For coordinate board: M for miss, H for Hit.
        coordinateBoard[xCord][yCord] = (placedShipsBoard[xCord][yCord] == '~')? 'M' : 'H';
        visited[xCord][yCord] = true;
        printCoordinateBoard();
        return coordinateBoard[xCord][yCord];
    }


    /**
     * Places all the ships for this player or computer.
     */
    public void placeShips(Ship[] shipContainer, boolean computer){
        boolean moveOn = true;
        boolean overlaps = false;
        Coordinate cord1;
        Coordinate cord2;
        fillShipBoard(placedShipsBoard);

        for(Ship ship : shipContainer){
            //Getting starting coordinates and ending coordinates.
            if(computer != true){
                do {
                    askShipQuestion(ship);

                    cord1 = getCoordinate(false);

                    System.out.println("Enter an ending coordinate keeping in mind the ship length. Remember, " +
                            "you cannot have ships overlapping.");
                    cord2 = getCoordinate(false);

                    //if both x coordinates (rows) are the same, then check the distance between x coordinates
                    if (cord1.getX() == cord2.getX()) {
                        moveOn = checkSpace(ship, cord1.getY(), cord2.getY());
                        overlaps = checkOverlap(cord1.getX(), cord1.getY(), cord2.getX(), cord2.getY());
                    }
                    //if both y coordinates (cols) are the same, then check the distance between y coordinates
                    else if (cord1.getY() == cord2.getY()) {
                        moveOn = checkSpace(ship, cord1.getX(), cord2.getX());
                        overlaps = checkOverlap(cord1.getX(), cord1.getY(), cord2.getX(), cord2.getY());
                    } else {
                        moveOn = false;
                        System.out.println("Invalid Coordinates.");
                    }

                }while(!moveOn || overlaps);
            }

            else{
                System.out.println("Computer is placing ships...");
                do{
                    cord1 = getCoordinate(true);
                    cord2 = getCoordinate(true);
                    if(cord1.getX() == cord2.getX()){
                        moveOn = checkSpace(ship, cord1.getY(), cord2.getY());
                        overlaps = checkOverlap(cord1.getX(), cord1.getY(), cord2.getX(), cord2.getY());
                    }
                    else{
                        moveOn = checkSpace(ship, cord1.getX(), cord2.getX());
                        overlaps = checkOverlap(cord1.getX(), cord1.getY(), cord2.getX(), cord2.getY());
                    }
                }while(overlaps || !moveOn);
            }

            //place ship in placedShipBoard considering cord1 and cord2.
            // if rows are the same.
            if(cord1.getX() == cord2.getX()){
                //check which way coordinates are oriented and place ship taking that into account.
                if(cord1.getY() < cord2.getY()) {
                    for (int j = cord1.getY(); j <= cord2.getY(); j++) {
                        placedShipsBoard[cord1.getX()][j] = ship.getShipChar(ship);
                    }
                }
                else{
                    for(int j = cord2.getY(); j<= cord1.getY(); j++){
                        placedShipsBoard[cord1.getX()][j] = ship.getShipChar(ship);
                    }
                }
            }
            //if cols are the same
            else{
                //check which way coordinates are oriented and place ship taking that into account.
                if(cord1.getX() < cord2.getX()) {
                    for (int j = cord1.getX(); j <= cord2.getX(); j++) {
                        placedShipsBoard[j][cord1.getY()] = ship.getShipChar(ship);
                    }
                }
                else{
                    for(int j = cord2.getX(); j<= cord1.getX(); j++) {
                        placedShipsBoard[j][cord1.getY()] = ship.getShipChar(ship);
                    }
                }
            }
            printPlaceShipsBoard();
        }
    }

    /**
     * Prints the board after each respective ship is placed.
     */
    private void printPlaceShipsBoard() {
        int rowNum = 0; int colNum = -1;
        for (int row = 0; row < coordinateBoard[0].length;row++) {
            if(row!=0) System.out.println();
            else{
                // Printing the numbers on top of the board
                for(int i = 0; i<=10; i++){
                    if(colNum == -1){
                        System.out.print(" ");
                        colNum++;
                        continue;
                    }
                    System.out.print(colNum++);
                }
                System.out.println();
            }
            for(int col = 0; col<coordinateBoard[0].length; col++){
                //Printing the nums on the side of the board
                if(col == 0) System.out.print(rowNum++);
                //printing placedShipsBoard vals as '~' if no ship there, else printing the symbol of the ship.
                System.out.print(placedShipsBoard[row][col]);
            }
        }
        System.out.println("\n");
    }

    /**
     *
     * @param xCord1 - x Coordinate of cord1
     * @param yCord1 - y Coordinate of cord1
     * @param xCord2 - x Coordinate of cord2
     * @param yCord2 - y Coordinate of cord2
     * @return true if there is a char of a ship between the selected coordinates
     */
    private boolean checkOverlap(int xCord1, int yCord1, int xCord2, int yCord2){
        boolean val = false;
        //rows same
        if(xCord1 == xCord2){
            if(yCord1 < yCord2) {
                for (int i = yCord1; i <= yCord2; i++) {
                    if (placedShipsBoard[xCord1][i] != '~') {
                        val = true;
                        System.out.println("There is a ship placed between the selected coordinates.");
                        break;
                    }
                }
            }
            else{
                for (int i = yCord2; i <= yCord1; i++) {
                    if (placedShipsBoard[xCord1][i] != '~') {
                        val = true;
                        System.out.println("There is a ship placed between the selected coordinates.");
                        break;
                    }
                }
            }
        }
        //cols same
        else{
            if(xCord1 < xCord2) {
                for (int i = xCord1; i <= xCord2; i++) {
                    if (placedShipsBoard[i][yCord1] != '~') {
                        val = true;
                        System.out.println("There is a ship placed between the selected coordinates.");
                        break;
                    }
                }
            }
            else{
                for (int i = xCord2; i <= xCord1; i++) {
                    if (placedShipsBoard[i][yCord1] != '~') {
                        val = true;
                        System.out.println("There is a ship placed between the selected coordinates.");
                        break;
                    }
                }
            }
        }
        return val;
    }

    /**
     * Checks to see if there space with the chosen coordinates is equal to the length of the ship.
     * @param ship
     * @param a
     * @param b
     * @return boolean value if there is enough space between the two integer values.
     */
    private boolean checkSpace(Ship ship, int a, int b){
        if(a==b) return false;
        int space = (a>b)? (a-b) + 1 : (b-a) + 1;
        boolean val = (ship.getShipLength(ship)==space)? true : false;
        if(val == false) System.out.println("Invalid Coordinates. Please take ship length into account.\n");
        return val;
    }

    /**
     * Returns true if this player has lost
     *
     * @return true if this player has lost, false otherwise
     */
    public boolean lost(){
        int count = 0;
        for(int row = 0; row < visited[0].length; row++){
            for(int col = 0; col < visited[0].length; col++){
               if(visited[row][col] == true) count++;
            }
        }
        boolean val = (count == 17)? true : false;
        return val;
    }

    /**
     * Prints this player's coordinate Board.
     *
     */
    public void printCoordinateBoard(){
        char symbol;
        int rowNum = 0;
        int colNum = -1;
        for (int row = 0; row < coordinateBoard[0].length;row++) {
           if(row!=0) System.out.println();
            else{
               // Printing the numbers on top of the board
                for(int i = 0; i<=10; i++){
                    if(colNum == -1){
                        System.out.print(" ");
                        colNum++;
                        continue;
                    }
                    System.out.print(colNum++);
                }
               System.out.println();
            }
            for(int col = 0; col<coordinateBoard[0].length; col++){
               //Printing the nums on the side of the board
                if(col == 0) System.out.print(rowNum++);
                //printing coordinateBoard vals as '~' if ship isn't hit, else printing the symbol of the ship.
                symbol = visited[row][col]!= true? '~' : coordinateBoard[row][col];
                System.out.print(symbol);
            }
        }
        System.out.println("\n");
    }

    public void setHitsArray(){
        for(int row = 0; row < visited[0].length; row++){
            for(int col = 0; col < visited[0].length; col++){
                visited[row][col] = false;
            }
        }
    }

    /**
     * initializes coordinateBoard to '~' (only used for coordinate board for a player)
      */
    public void setCoordinateBoard(){
        for(int row = 0; row < visited[0].length; row++){
            for(int col = 0; col < visited[0].length; col++){
                coordinateBoard[row][col] = '~';
            }
        }
    }

    /**
     * initializes placedShipsBoard to '~'.
     *
     * @param placedShipsBoard - ship placement board
     */
    private void fillShipBoard(char[][] placedShipsBoard){
        for(int row = 0; row < visited[0].length; row++){
            for(int col = 0; col < visited[0].length; col++){
                placedShipsBoard[row][col] = '~';
            }
        }
    }

    /**
     * Asks question on placing the ship based on the ship and it's length.
     * @param ship - ship type
     */
    private void askShipQuestion(Ship ship){
        System.out.println("You are about to place ship " + ship.getShipChar(ship) + ", It's " + ship.getShipLength(ship)
        +" units long. Enter a starting coordinate where you wish to place your ship: (example: 00)");
    }

    /**
     *
     * @return Coordinate value the player has typed in.
     */
    public Coordinate getCoordinate(boolean computer) {
        String input;
        String xCordIn; String yCordIn;
        int xCord; int yCord;
        int count = 0;
        boolean moveOn;
        Coordinate cord;
//Getting coordinates from a player
        if(computer != true) {
            do {
                moveOn = true;
                if (count > 0) System.out.println("Enter the coordinates again.");
                Scanner sc = new Scanner(System.in);
                input = sc.nextLine();
                count++;
                //Check for length being 2.
                if (input.length() != 2) {
                    System.out.println("Coordinates should only be numbers. There should be exactly 2. (example: 00)\n");
                    moveOn = false;
                    continue;
                }
                try {
                    Integer.parseInt(input);
                } catch (NumberFormatException e) {   //checking if it's parsable
                    moveOn = false;
                }
            } while (!moveOn);

            xCordIn = input.substring(0,1);
            yCordIn = input.substring(1);
            cord = new Coordinate(Integer.parseInt(xCordIn), Integer.parseInt(yCordIn));
        }
//Getting coordinates from a computer
        else{
            xCord = getRandomNum();
            yCord = getRandomNum();
            cord = new Coordinate(xCord, yCord);
        }
        return cord;
    }
    public int getRandomNum(){
        return (int) Math.floor(Math.random()*10);
    }
}