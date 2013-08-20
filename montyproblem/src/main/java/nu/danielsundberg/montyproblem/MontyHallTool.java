package nu.danielsundberg.montyproblem;

import nu.danielsundberg.montyproblem.show.Box;
import nu.danielsundberg.montyproblem.show.GameShow;
import nu.danielsundberg.montyproblem.show.MontyHallGameShow;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Tool för att undersöka MonyHall-fenomenet, kan användas som tool eller köras från jar.
 */
public final class MontyHallTool {

    public static final BigInteger DEFAULT_NUMBER_OF_TRIES = BigInteger.valueOf(50000);
    public static final BigInteger DEFAULT_NUMBER_OF_BOXES = BigInteger.valueOf(3);
    public static final BigInteger DEFAULT_NUMBER_TO_REMOVE = BigInteger.ONE;
    public static final BigInteger PROCENT = BigInteger.valueOf(100);

    /**
     * Göm undan defaultkonstruktorn för statisk klass
     */
    private MontyHallTool() {}

    /**
     * Skapa en ny show
     * @param numberOfBoxesInShow representerar antalet lådor i showen
     * @return a new gameshow
     */
    public static GameShow generateGameShow(int numberOfBoxesInShow) {
        return new MontyHallGameShow(numberOfBoxesInShow);
    }

    /**
     * Mainmetod för att köra från commandline.
     * @param arguments de argument som anges från commandline
     */
    public static void main(String arguments[]) {
        //
        // Skriv ut logo och initial info.
        //
        System.out.println(
                "nu.danielsundberg     .-.                        .-.   .-.                 \n"+
                "---------------------.' `.-----------------------: :---: :-----------------\n"+
                ",-.,-.,-. .--. ,-.,-.`. .'.-..-..---. .--.  .--. : `-. : :   .--. ,-.,-.,-.\n"+
                ": ,. ,. :' .; :: ,. : : : : :; :: .; `: ..'' .; :' .; :: :_ ' '_.': ,. ,. :\n"+
                ":_;:_;:_;`.__.':_;:_; :_; `._. ;: ._.':_;  `.__.'`.__.'`.__;`.__.':_;:_;:_;\n"+
                "---------------------------.-. :: :-------------------------0.0.1-SNAPSHOT-\n"+
                "                           `._.':_;                                        ");
        //
        // Initiera simulering
        //
        BigInteger numberOfTries;
        BigInteger numberOfBoxes;
        BigInteger numberOfBoxesToRemove;
        if(arguments != null && arguments.length == 3) {
            numberOfTries = BigInteger.valueOf(Integer.parseInt(arguments[0]));
            if(numberOfTries.intValue() <= 0) {
                throw new IllegalArgumentException("Number of tries must be larger than 0");
            }
            numberOfBoxes = BigInteger.valueOf(Integer.parseInt(arguments[1]));
            numberOfBoxesToRemove = BigInteger.valueOf(Integer.parseInt(arguments[2]));
            if(numberOfBoxesToRemove.intValue() >= numberOfBoxes.intValue() - 1) {
                throw new IllegalArgumentException("Number to remove must be less than number of boxes - 1");
            }
        } else {
            System.out.println("Usage: [numberOfSimulations] [numberOfBoxes] [numberOfBoxesToRemove]");
            System.out.println("Will use default values for this run.");
            numberOfTries = DEFAULT_NUMBER_OF_TRIES;
            numberOfBoxes = DEFAULT_NUMBER_OF_BOXES;
            numberOfBoxesToRemove = DEFAULT_NUMBER_TO_REMOVE;
        }
        System.out.println(
                "Running " + numberOfTries +
                " simulations with " + numberOfBoxes +
                " initial boxes and removing " + numberOfBoxesToRemove +
                " before choice.");


        //
        // Försök angiven antal gånger utan byte av låda
        //
        float winWithoutChange = 0;
        for(int i = 0; i < numberOfTries.intValue(); i++) {
            //
            // Skapa en ny gameshow med angivet antal lådor
            //
            GameShow gameShow = MontyHallTool.generateGameShow(numberOfBoxes.intValue());
            //
            // Välj en slumpmässigt vald låda
            //
            Box boxThatPlayerSelectsInitially = gameShow.selectRandomBox();
            //
            // Ta bort angivet antal lådor (som inte innehåller priset)
            //
            gameShow.removeRandomBoxesExcept(
                    numberOfBoxesToRemove.intValue(),
                    new Box[]{boxThatPlayerSelectsInitially,
                            gameShow.getBoxWithPrize()});
            //
            // Verifiera om lådan innehåller priset
            //
            if(boxThatPlayerSelectsInitially.hasPrize()) {
                winWithoutChange++;
            }
        }

        //
        // Försök angivet antal gånger med byte av låda
        //
        float winWithChange = 0;
        for(int i = 0; i < numberOfTries.intValue(); i++) {
            //
            // Skapa en ny gameshow med angivet antal lådor
            //
            GameShow gameShow = MontyHallTool.generateGameShow(numberOfBoxes.intValue());
            //
            // Välj en låda slumpmässigt
            //
            Box boxThatPlayerSelectsInitially = gameShow.selectRandomBox();
            //
            // Ta bort angivet antal lådor (som inte innehåller priset eller är spelarens val)
            //
            gameShow.removeRandomBoxesExcept(
                    numberOfBoxesToRemove.intValue(),
                    new Box[]{boxThatPlayerSelectsInitially,
                            gameShow.getBoxWithPrize()});
            //
            // Byt låda (men inte till den spelare redan valt)
            //
            Box boxThatPlayerChangesTo = gameShow.selectRandomBoxExcept(boxThatPlayerSelectsInitially);
            //
            // Verifiera om lådan innehåller priset
            //
            if(boxThatPlayerChangesTo.hasPrize()) {
                winWithChange++;
            }
        }

        //
        // Skriv ut lite rolig statistik och slutsatsen
        //
        float withChangeAsPercent = (winWithChange/numberOfTries.intValue()) * PROCENT.intValue();
        float withoutChangeAsPercent = (winWithoutChange / numberOfTries.intValue()) * PROCENT.intValue();
        System.out.println(
                "Wins by switching box: " + BigDecimal.valueOf(winWithChange).stripTrailingZeros() +
                " wins out of " + numberOfTries +
                " (" + BigDecimal.valueOf(withChangeAsPercent).setScale(1, RoundingMode.HALF_UP) + "%)");
        System.out.println(
                "Wins by staying true: " +  BigDecimal.valueOf(winWithoutChange).stripTrailingZeros() +
                " wins out of " + numberOfTries +
                " (" + BigDecimal.valueOf(withoutChangeAsPercent).setScale(1, RoundingMode.HALF_UP) + "%)");
        System.out.println("The simultation seems to recommend " +
                (withChangeAsPercent > withoutChangeAsPercent ?
                        "that you change your selection." :
                        "that you keep your original choice."));
        System.out.println("--------------------------------------------------------------------------");

    }

}
