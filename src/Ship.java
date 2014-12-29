/**
 * Created by AJ on 12/23/14.
 */
public class Ship {
    private char shipChar;
    private int shipLength;

    public Ship(char shipChar, int shipLength){
        //Use of encapsulation.
        this.shipChar = setShipChar(shipChar);
        this.shipLength = setShipLength(shipLength);
    }

    public Ship(){

    }
    public char setShipChar(char c){
        shipChar = c;
        return shipChar;
    }
    public char getShipChar(Ship ship){
        return ship.shipChar;
    }
    public int setShipLength(int length){
        if(length > 0){
            shipLength = length;
        }
        return shipLength;
    }
    public int getShipLength(Ship ship){
        return ship.shipLength;
    }

}

