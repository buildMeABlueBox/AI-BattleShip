/**
 * Created by AJ on 12/23/14.
 */


public class BattleShipDriver{
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

        p1.placeShips(shipContainer, BattleShipUtilities.getChoice("Player1") == 2);

        BattleShipUtilities.makeSpace();

        p2.placeShips(shipContainer, BattleShipUtilities.getChoice("Player2") == 2);

        BattleShipUtilities.makeSpace();

        do{
            BattleShipUtilities.fireQuestion("Player1", "Player2");
            do {
                hit = p2.fireUpon(false);
                BattleShipUtilities.giveResult(hit);
                if (p2.lost()) break;
            }while(hit=='H');

            if(p2.lost()){
                System.out.println("\n Player2 loses.");
                break;
            }

            BattleShipUtilities.fireQuestion("Player2", "Player1");
            do {
                hit = p1.fireUpon(false);
                BattleShipUtilities.giveResult(hit);
                if (p1.lost()) break;
            }while(hit =='H');
            if(p1.lost()){
                System.out.println("\nPlayer1 loses.");
                break;
            }

        }while(!p1.lost() || !p2.lost());
    }
}