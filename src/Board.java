/**
 * Created by AJ on 1/2/15.
 */
public class Board<T>{
    private T [][] values;

    public Board() {

    }
    public Board(Class<T> clazz, T defaultVal) throws InstantiationException, IllegalAccessException {
       //Find out how to make T[][] = new T[10][10]
        Class<T> cl = (Class<T>) clazz.getClass();
        T type = createContents(cl);

    }

    void setBoardValues(T[][] values, T defaultVal) {
        for(int i = 0; i<values[0].length; i++){
            for(int j = 0; j<values[0].length; j++){
                values[i][j] = defaultVal;
            }
        }
    }


    T createContents(Class<T> board) throws IllegalAccessException, InstantiationException {
        return board.newInstance();
    }
}
class CoordinateBoard extends Board{
    char[][] charArr;
    public CoordinateBoard() {
        charArr = new char[10][10];
        setBoardValues(charArr, '~');
    }

    void setBoardValues(char[][] charArr, char defaultVal){
        for(int i = 0; i<charArr[0].length; i++){
            for(int j = 0; j<charArr[0].length; j++){
                charArr[i][j] = defaultVal;
            }
        }
    }
}

class VisitedBoard extends Board{
    boolean[][] visitedArr;
    public VisitedBoard(){
        visitedArr = new boolean[10][10];
        setBoardValues(visitedArr, false);
    }

    void setBoardValues(boolean[][] boolArr, boolean defaultVal){
        for(int i = 0; i<boolArr[0].length; i++){
            for(int j = 0; j<boolArr[0].length; j++){
                boolArr[i][j] = defaultVal;
            }
        }
    }
}
