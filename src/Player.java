/**
 * Created by AJ on 12/23/14.
 */
public class Player {
    CoordinateBoard coordinateBoard = new CoordinateBoard();
    CoordinateBoard placedShipsBoard = new CoordinateBoard();
    VisitedBoard visited = new VisitedBoard();

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
            Coordinate cord = computer? BattleShipUtilities.getCoordinate(true) : BattleShipUtilities.getCoordinate(false);

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
                    BattleShipUtilities.askShipQuestion(ship);

                    cord1 = BattleShipUtilities.getCoordinate(false);

                    System.out.println("Enter an ending coordinate keeping in mind the ship length. Remember, " +
                            "you cannot have ships overlapping.");
                    cord2 = BattleShipUtilities.getCoordinate(false);

                    //if both x coordinates (rows) are the same, then check the distance between x coordinates
                    if (BattleShipUtilities.sameNum(cord1.getX(), cord2.getX())) {
                        enoughSpace = BattleShipUtilities.checkSpace(ship, cord1.getY(), cord2.getY());
                        overlaps = checkOverlap(cord1, cord2);
                        isValid = BattleShipUtilities.placeable(enoughSpace, overlaps);
                    }
                    //if both y coordinates (cols) are the same, then check the distance between y coordinates
                    else if (BattleShipUtilities.sameNum(cord1.getY(), cord2.getY())) {
                        enoughSpace = BattleShipUtilities.checkSpace(ship, cord1.getX(), cord2.getX());
                        overlaps = checkOverlap(cord1, cord2);
                        isValid = BattleShipUtilities.placeable(enoughSpace, overlaps);
                    } else {
                        isValid = false;
                        System.out.println("Invalid Coordinates.");
                    }

                }while(!isValid);
            }

            else{
                System.out.println("Computer is placing ships...");
                do{
                    cord1 = BattleShipUtilities.getCoordinate(true);
                    cord2 = BattleShipUtilities.getCoordinate(true);
                    if(BattleShipUtilities.sameNum(cord1.getX(), cord2.getX())){
                        enoughSpace = BattleShipUtilities.checkSpace(ship, cord1.getY(), cord2.getY());
                        overlaps = checkOverlap(cord1, cord2);
                        isValid = BattleShipUtilities.placeable(enoughSpace, overlaps);
                    }
                    else{
                        enoughSpace = BattleShipUtilities.checkSpace(ship, cord1.getX(), cord2.getX());
                        overlaps = checkOverlap(cord1, cord2);
                        isValid =  BattleShipUtilities.placeable(enoughSpace, overlaps);
                    }
                }while(!isValid);
            }
            rowsTheSame = BattleShipUtilities.sameNum(cord1.getX(), cord2.getX());
            placeTheShip(rowsTheSame, cord1, cord2, ship);
            printPlaceShipsBoard();
        }
    }
    /**
     * Prints the board after each respective ship is placed.
     */
    public void printPlaceShipsBoard() {
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
    //Coordinate x or y value is greater than another Coordinate, this method places the ship taking into account their values.
    public void placeTheShip(boolean rowsTheSame, Coordinate cord1, Coordinate cord2, Ship ship){
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
    public  void printCoordinateBoard(){
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
     * @param cord1 - Starting Coordinate
     * @param cord2 - Ending Coordinate
     * @return true if there is a char of any ship between the selected coordinates
     */
    public boolean checkOverlap(Coordinate cord1, Coordinate cord2){
        boolean val = false;
        int xCord1 = cord1.getX(), yCord1 = cord1.getY(), xCord2 = cord2.getX(), yCord2 = cord2.getY();
        //rows same
        if(BattleShipUtilities.sameNum(xCord1, xCord2)){
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
}