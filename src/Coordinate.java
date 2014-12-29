/**
 * Created by AJ on 12/23/14.
 */
public class Coordinate {
    private int x;
    private int y;
    public Coordinate(int x, int y){
        this.x = setX(x);
        this.y = setY(y);
    }
    public int setX(int num){
        if(num >= 0){
            x = num;
        }
        return x;
    }
    public int setY(int num){
        if(num >= 0){
            y = num;

        }
        return y;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public String toString(){
        return(x+"" +y+"");
    }
}
