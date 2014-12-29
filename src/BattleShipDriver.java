/**
 * Created by AJ on 12/23/14.
 */
public class BattleShipDriver {
    public static void main(String[] args){
        char hit;
        Ship aircraft = new Ship('A',5);
        Ship battleship = new Ship('B',4);
        Ship cruiser = new Ship('C',3);
        Ship patrolBoat = new Ship('P',2);
        Ship submarine = new Ship('S',3);

        Ship[] shipContainer = new Ship[5];
        shipContainer[0] = aircraft;
        shipContainer[1] = battleship;
        shipContainer[2] = cruiser;
        shipContainer[3] = patrolBoat;
        shipContainer[4] = submarine;

        Player p1 = new Player();
        Player p2 = new Player();

        System.out.println("Player1: \n");
        initialize(p1, shipContainer, true);

        makeSpace();

        System.out.println("Player2: \n");
        initialize(p2, shipContainer, true);

        makeSpace();

        do{
            do {
                askQuestion("p1", "p2");
                hit = p2.fireUpon(false);
                giveResult(hit);
                if (p2.lost()) break;
            }while(hit=='H');

            if(p2.lost()){
                System.out.println("\n Player2 loses.");
                break;
            }

            do {
                askQuestion("p2", "p1");
                hit = p1.fireUpon(false);
                giveResult(hit);
                if (p1.lost()) break;
            }while(hit =='H');
            if(p1.lost()){
                System.out.println("\nPlayer1 loses.");
                break;
            }

        }while(!p1.lost() || !p2.lost());
    }

    private static void giveResult(char hit) {
        if(hit == 'M'){
            System.out.println("You Missed!");
        }
        else{
            System.out.println("You hit a ship!");
        }
    }

    private static void initialize(Player player, Ship[] shipContainer, boolean computer){
        player.printCoordinateBoard();
        player.setCoordinateBoard();
        player.setHitsArray();
        player.placeShips(shipContainer, computer);
    }
    private static void askQuestion(String player1, String player2){
        System.out.println("It's " + player1 + "'s turn to fire on " + player2 +"'s board. Enter a Coordinate:(Example: 00): ");
    }
    private static void makeSpace(){
        for(int i = 0; i<15; i++) {
            System.out.println();
        }
    }
}