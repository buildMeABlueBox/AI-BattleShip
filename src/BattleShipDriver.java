/**
 * Created by AJ on 12/23/14.
 */
import java.util.Scanner;
public class BattleShipDriver {
    public static void main(String[] args){
        int choice;
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

        choice = getChoice("Player1");
        initialize(p1, shipContainer, choice==2);

        makeSpace();

        choice = getChoice("Player2");
        initialize(p2, shipContainer, choice==2);

        makeSpace();

        do{
            fireQuestion("Player1", "Player2");
            do {
                hit = p2.fireUpon(false);
                giveResult(hit);
                if (p2.lost()) break;
            }while(hit=='H');

            if(p2.lost()){
                System.out.println("\n Player2 loses.");
                break;
            }

            fireQuestion("Player2", "Player1");
            do {
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
            System.out.println("You hit a ship!\nEnter another coordinate to fire on: (Example: 00)");
        }
    }

    private static void initialize(Player player, Ship[] shipContainer, boolean computer){
        player.placeShips(shipContainer, computer);
    }
    private static void fireQuestion(String player1, String player2){
        System.out.println("It's " + player1 + "'s turn to fire on " + player2 +"'s board. Enter a Coordinate:(Example: 00): ");
    }
    private static void askShipPlacement(String player){
        System.out.println(player + ":\n Enter 1 to place the ships yourself. \n" +
                " Enter 2 to place them randomly.");
    }
    private static void makeSpace(){
        for(int i = 0; i<15; i++) {
            System.out.println();
        }
    }
    private static int getChoice(String player){
        Scanner sc;
        String input;
        int choice = 0, count = 0;
        boolean moveOn = true;
        do{
            moveOn = true;
            if(count == 0){
                askShipPlacement(player);
                count++;
            }
            else System.out.println("Please only enter 1 or 2.");
            sc = new Scanner(System.in);
            input = sc.nextLine();
            try{
                choice = Integer.parseInt(input);
                if(choice != 1 && choice != 2) moveOn = false;
            }catch(NumberFormatException e){
                moveOn = false;
            }
            System.out.println();
        }while(!moveOn);
        return choice;
    }
}