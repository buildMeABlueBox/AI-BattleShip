/**
 * Created by AJ on 12/23/14.
 */
import java.util.Scanner;
public class Player {
    CoordinateBoard coordinateBoard = new CoordinateBoard();
    CoordinateBoard placedShipsBoard = new CoordinateBoard();
    VisitedBoard visited = new VisitedBoard();

    public Player(){

    }
    /**
     * This player is being fired upon. Given integer coordinates, updates board(s) accordingly.
     * Return - character 'M' if it was a miss, 'H' if it was a hit.
     */
    public char fireUpon(boolean computer){
        boolean moveOn;
        int xCord;
        int yCord;
        do {
            moveOn = true;
            Coordinate cord = computer? getCoordinate(true) : getCoordinate(false);

            xCord = cord.getX();
            yCord = cord.getY();
            //player has already hit this target.
            if (visited.visitedArr[xCord][yCord]){
                System.out.println("You've already chosen this coordinate before. Choose another one.");
                moveOn = false;
            }
        }while(!moveOn);
        
        //update visited/coordinate boards. For coordinate board: M for miss, H for Hit.
        coordinateBoard.charArr[xCord][yCord] = (placedShipsBoard.charArr[xCord][yCord] == '~')? 'M' : 'H';
        visited.visitedArr[xCord][yCord] = true;
        printCoordinateBoard();
        return coordinateBoard.charArr[xCord][yCord];
    }


    /**
     * Places all the ships for this player or computer.
     */
    public void placeShips(Ship[] shipContainer, boolean computer){
        Coordinate cord1, cord2;
        boolean enoughSpace, overlaps, isValid, rowsTheSame;
        printCoordinateBoard();

        for(Ship ship : shipContainer){
//Getting starting coordinates and ending coordinates.
            if(!computer){
                do {
                    askShipQuestion(ship);

                    cord1 = getCoordinate(false);

                    System.out.println("Enter an ending coordinate keeping in mind the ship length. Remember, " +
                            "you cannot have ships overlapping.");
                    cord2 = getCoordinate(false);

                    //if both x coordinates (rows) are the same, then check the distance between x coordinates
                    if (sameNum(cord1.getX(), cord2.getX())) {
                        enoughSpace = checkSpace(ship, cord1.getY(), cord2.getY());
                        overlaps = checkOverlap(cord1, cord2);
                        isValid = placeable(enoughSpace, overlaps);
                    }
                    //if both y coordinates (cols) are the same, then check the distance between y coordinates
                    else if (sameNum(cord1.getY(), cord2.getY())) {
                        enoughSpace = checkSpace(ship, cord1.getX(), cord2.getX());
                        overlaps = checkOverlap(cord1, cord2);
                        isValid = placeable(enoughSpace, overlaps);
                    } else {
                        isValid = false;
                        System.out.println("Invalid Coordinates.");
                    }

                }while(!isValid);
            }

            else{
                System.out.println("Computer is placing ships...");
                do{
                    cord1 = getCoordinate(true);
                    cord2 = getCoordinate(true);
                    if(sameNum(cord1.getX(), cord2.getX())){
                        enoughSpace = checkSpace(ship, cord1.getY(), cord2.getY());
                        overlaps = checkOverlap(cord1, cord2);
                        isValid = placeable(enoughSpace, overlaps);
                    }
                    else{
                        enoughSpace = checkSpace(ship, cord1.getX(), cord2.getX());
                        overlaps = checkOverlap(cord1, cord2);
                        isValid =  placeable(enoughSpace, overlaps);
                    }
                }while(!isValid);
            }
            rowsTheSame = sameNum(cord1.getX(), cord2.getX());
            placeTheShip(rowsTheSame, cord1, cord2, ship);
            printPlaceShipsBoard();
        }
    }

    //Coordinate x or y value is greater than another Coordinate, this method places the ship taking into account their values.
    private void placeTheShip(boolean rowsTheSame, Coordinate cord1, Coordinate cord2, Ship ship){
        int cord1XY, cord2XY, max, min, oppCord;
        //rows same
        if(rowsTheSame) {
            cord1XY = cord1.getY();
            cord2XY = cord2.getY();
            max = Math.max(cord1XY, cord2XY);
            min = Math.min(cord1XY, cord2XY);
            oppCord = cord1.getX();
            for(int j = min; j<= max; j++){
                placedShipsBoard.charArr[oppCord][j] = ship.getShipChar(ship);
            }
        }
        //cols same
        else{
            cord1XY = cord1.getX();
            cord2XY = cord2.getX();
            max = Math.max(cord1XY, cord2XY);
            min = Math.min(cord1XY, cord2XY);
            oppCord = cord1.getY();
            for (int j = min; j <= max; j++) {
                placedShipsBoard.charArr[j][oppCord] = ship.getShipChar(ship);
            }
        }
    }

    /**
     * Prints the board after each respective ship is placed.
     */
    private void printPlaceShipsBoard() {
        int rowNum = 0, colNum = -1;
        for (int row = 0; row < coordinateBoard.charArr[0].length;row++) {
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
            for(int col = 0; col<coordinateBoard.charArr[0].length; col++){
                //Printing the nums on the side of the board
                if(col == 0) System.out.print(rowNum++);
                //printing placedShipsBoard values as '~' if no ship there, else printing the symbol of the ship.
                System.out.print(placedShipsBoard.charArr[row][col]);
            }
        }
        System.out.println("\n");
    }

    private boolean placeable(boolean enoughSpace, boolean overlaps){
        return enoughSpace && !overlaps;
    }
    private boolean sameNum(int cord1, int cord2){
        return (cord1 == cord2);
    }

    /**
     * @param cord1 - Starting Coordinate
     * @param cord2 - Ending Coordinate
     * @return true if there is a char of any ship between the selected coordinates
     */
    private boolean checkOverlap(Coordinate cord1, Coordinate cord2){
        boolean val = false;
        int xCord1 = cord1.getX(), yCord1 = cord1.getY(), xCord2 = cord2.getX(), yCord2 = cord2.getY();
        //rows same
        if(sameNum(xCord1, xCord2)){
            if(yCord1 < yCord2) {
                for (int i = yCord1; i <= yCord2; i++) {
                    if (placedShipsBoard.charArr[xCord1][i] != '~') {
                        val = true;
                        System.out.println("There is a ship placed between the selected coordinates.");
                        break;
                    }
                }
            }
            else{
                for (int i = yCord2; i <= yCord1; i++) {
                    if (placedShipsBoard.charArr[xCord1][i] != '~') {
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
                    if (placedShipsBoard.charArr[i][yCord1] != '~') {
                        val = true;
                        System.out.println("There is a ship placed between the selected coordinates.");
                        break;
                    }
                }
            }
            else{
                for (int i = xCord2; i <= xCord1; i++) {
                    if (placedShipsBoard.charArr[i][yCord1] != '~') {
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
     * @param ship - The ship that is being taken into account
     * @param a - Starting coordinate value
     * @param b - Ending coordinate value
     * @return boolean value if there is enough space between the two integer values.
     */
    private boolean checkSpace(Ship ship, int a, int b){
        if(a==b) return false;
        int space = (a>b)? (a-b) + 1 : (b-a) + 1;
        boolean val = (ship.getShipLength(ship)==space);
        if(!val) System.out.println("Invalid Coordinates. Please take ship length into account.\n");
        return val;
    }

    /**
     * Returns true if this player has lost
     *
     * @return true if this player has lost, false otherwise
     */
    public boolean lost(){
        int count = 0;
        for(int row = 0; row < coordinateBoard.charArr[0].length; row++){
            for(int col = 0; col < coordinateBoard.charArr[0].length; col++){
               if(coordinateBoard.charArr[row][col] == 'H') count++;
            }
        }
        return (count == 17);
    }

    /**
     * Prints this player's coordinate Board.
     *
     */
    private void printCoordinateBoard(){
        char symbol;
        int rowNum = 0, colNum = -1;
        for (int row = 0; row < coordinateBoard.charArr[0].length;row++) {
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
            for(int col = 0; col<coordinateBoard.charArr[0].length; col++){
               //Printing the nums on the side of the board
                if(col == 0) System.out.print(rowNum++);
                //printing coordinateBoard vals as '~' if ship isn't hit, else printing the symbol of the ship.
                symbol = !visited.visitedArr[row][col]? '~' : coordinateBoard.charArr[row][col];
                System.out.print(symbol);
            }
        }
        System.out.println("\n");
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
    private Coordinate getCoordinate(boolean computer) {
        Scanner sc;
        String input, xCordIn, yCordIn;
        int xCord, yCord, count = 0;
        boolean moveOn;
        Coordinate cord;
//Getting coordinates from a player
        if(!computer) {
            do {
                moveOn = true;
                if (count > 0) System.out.println("Enter the coordinates again.");
                sc = new Scanner(System.in);
                input = sc.nextLine();
                count++;
                //Check for length being 2.
                if (input.length() != 2) {
                    System.out.println("Coordinates should only be numbers. There should be exactly 2. (example: 00)\n");
                    moveOn = false;
                    continue;
                }
                //checking if it's parsable
                try {
                    Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    moveOn = false;
                }
            } while (!moveOn);

            xCordIn = input.substring(0,1);
            yCordIn = input.substring(1);
            cord = new Coordinate(Integer.parseInt(xCordIn), Integer.parseInt(yCordIn));
        }
//Getting coordinates from a computer
        else{
            xCord = randomNum();
            yCord = randomNum();
            cord = new Coordinate(xCord, yCord);
        }
        return cord;
    }
    private int randomNum(){
        return (int) Math.floor(Math.random()*10);
    }

}