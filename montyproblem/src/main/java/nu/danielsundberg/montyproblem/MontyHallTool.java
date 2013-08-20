package nu.danielsundberg.montyproblem;

import nu.danielsundberg.montyproblem.show.box.Box;
import nu.danielsundberg.montyproblem.show.GameShow;
import nu.danielsundberg.montyproblem.show.MontyHallGameShow;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Tool för att undersöka MonyHall-fenomenet, kan användas som tool eller köras från jar.
 */
public final class MontyHallTool {

    /**
     * Defaultvärden för vår show
     */
    public static final BigInteger DEFAULT_NUMBER_OF_TRIES = BigInteger.valueOf(50000);
    public static final BigInteger DEFAULT_NUMBER_OF_BOXES = BigInteger.valueOf(3);
    public static final BigInteger DEFAULT_NUMBER_TO_REMOVE = BigInteger.ONE;
    public static final BigInteger PROCENT = BigInteger.valueOf(100);

    /**
     * Konstanta felmeddelanden
     */
    public static final String ANTAL_SIMULERINGAR_MASTE_VARA_FLER_AN_NOLL =
            "Number of tries must be larger than 0";
    public static final String ANTAL_BORTTAGNINGAR_MASTE_VARA_FARRE_AN_ANTAL_LADOR_MINUS_ETT =
            "Number to remove must be less than number of boxes - 1";

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
     * Initiera parametrar och skriv ut lite spacig info.
     * @param arguments argument till klassen
     * @return parametrar att använda för simulering
     */
    private static GameShowParameters initiateGameShowParametersAndPrintWelcome(String[] arguments) {
        //
        // Skriv ut EPISK ASCII-logo och initial info.
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
        GameShowParameters gameShowParameters;
        if(arguments != null && arguments.length == 3) {
            gameShowParameters = new GameShowParameters(arguments);
        } else {
            System.out.println("Usage: [numberOfSimulations] [numberOfBoxes] [numberOfBoxesToRemove]");
            System.out.println("Will use default values for this run.");
            gameShowParameters =
                    new GameShowParameters(
                        DEFAULT_NUMBER_OF_TRIES,
                        DEFAULT_NUMBER_OF_BOXES,
                        DEFAULT_NUMBER_TO_REMOVE);
        }
        System.out.println(
                "Running " + gameShowParameters.getNumberOfTries() +
                " simulations with " + gameShowParameters.getNumberOfBoxes() +
                " initial boxes and removing " + gameShowParameters.getNumberOfBoxesToRemove() +
                " before choice.");
        return gameShowParameters;
    }

    /**
     * Simulera ett antal spel där spelaren inte byter låda efter att monty tagit bort en/flera
     * @param gameShowParameters parametrar för simulering
     * @return antal spelare som vunnit genom att inte byta låda
     */
    private static BigDecimal simulateGameWherePlayerKeepsBox(GameShowParameters gameShowParameters) {
        float winWithoutChange = 0;
        for(int i = 0; i < gameShowParameters.getNumberOfTries().intValue(); i++) {
            //
            // Skapa en ny gameshow med angivet antal lådor
            //
            GameShow gameShow = MontyHallTool.generateGameShow(gameShowParameters.getNumberOfBoxes().intValue());
            //
            // Välj en slumpmässigt vald låda
            //
            Box boxThatPlayerSelectsInitially = gameShow.selectRandomBox();
            //
            // Ta bort angivet antal lådor (som inte innehåller priset)
            //
            gameShow.removeRandomBoxesExcept(
                    gameShowParameters.getNumberOfBoxesToRemove().intValue(),
                    boxThatPlayerSelectsInitially,
                    gameShow.getBoxWithPrize());
            //
            // Verifiera om lådan innehåller priset
            //
            if(boxThatPlayerSelectsInitially.hasPrize()) {
                winWithoutChange++;
            }
        }
        return BigDecimal.valueOf(winWithoutChange);
    }

    /**
     * Simulera ett spel där spelaren byter låda efter monty har tagit bort en/flera
     * @param gameShowParameters parametrar för simulering
     * @return antal spelare som vunnit genom att byta låda
     */
    private static BigDecimal simulateGameWherePlayerSwitchesBoxes(GameShowParameters gameShowParameters) {
        float winWithChange = 0;
        for(int i = 0; i <  gameShowParameters.getNumberOfTries().intValue(); i++) {
            //
            // Skapa en ny gameshow med angivet antal lådor
            //
            GameShow gameShow = MontyHallTool.generateGameShow( gameShowParameters.getNumberOfBoxes().intValue());
            //
            // Välj en låda slumpmässigt
            //
            Box boxThatPlayerSelectsInitially = gameShow.selectRandomBox();
            //
            // Ta bort angivet antal lådor (som inte innehåller priset eller är spelarens val)
            //
            gameShow.removeRandomBoxesExcept(
                    gameShowParameters.getNumberOfBoxesToRemove().intValue(),
                    boxThatPlayerSelectsInitially,
                    gameShow.getBoxWithPrize());
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
        return BigDecimal.valueOf(winWithChange);
    }

    /**
     * Rapportera resultat
     * @param numberOfTries
     * @param winWithoutChange
     * @param winWithChange
     */
    private static void outputResult(BigInteger numberOfTries, BigDecimal winWithoutChange, BigDecimal winWithChange) {
        //
        // Skriv ut lite rolig statistik och slutsatsen
        //
        BigDecimal withChangeAsPercent =
                BigDecimal.valueOf(winWithChange.floatValue()/numberOfTries.intValue() * PROCENT.intValue());
        BigDecimal withoutChangeAsPercent =
                BigDecimal.valueOf(winWithoutChange.floatValue()/ numberOfTries.intValue() * PROCENT.intValue());
        System.out.println(
                "Wins by switching box: " + winWithChange.stripTrailingZeros() +
                " wins out of " + numberOfTries +
                " (" + withChangeAsPercent.setScale(1, RoundingMode.HALF_UP) + "%)");
        System.out.println(
                "Wins by staying true: " + winWithoutChange.stripTrailingZeros() +
                " wins out of " + numberOfTries +
                " (" + withoutChangeAsPercent.setScale(1, RoundingMode.HALF_UP) + "%)");
        System.out.println("The simultation seems to recommend " +
                (withChangeAsPercent.compareTo(withoutChangeAsPercent) > 0 ?
                        "that you change your selection." :
                        "that you keep your original choice."));
        System.out.println("--------------------------------------------------------------------------");
    }

    /**
     * Mainmetod för att köra från commandline.
     * @param arguments de argument som anges från commandline
     */
    public static void main(String arguments[]) {
        //
        // Initiera gameshowens parametrar och skriv ut ett välkomnande meddelande
        //
        GameShowParameters gameShowParameters =
                initiateGameShowParametersAndPrintWelcome(arguments);
        //
        // Försök angiven antal gånger utan byte av låda
        //
        BigDecimal winWithoutChange =
                simulateGameWherePlayerKeepsBox(gameShowParameters);
        //
        // Försök angivet antal gånger med byte av låda
        //
        BigDecimal winWithChange =
                simulateGameWherePlayerSwitchesBoxes(gameShowParameters);
        //
        // Rapportera hur det gick på lämpligt sätt.
        //
        outputResult(gameShowParameters.getNumberOfTries(), winWithoutChange, winWithChange);
    }

    /**
     * Privat klass för att hålla parametrar som skall konfigurera simuleringarna
     */
    private static class GameShowParameters {

        /**
         * Variabler för simuleringar
         */
        private BigInteger numberOfTries;
        private BigInteger numberOfBoxes;
        private BigInteger numberOfBoxesToRemove;

        /**
         * Konstruktor som tar commandline-argument
         * @param arguments alla argument passade till klassen på commandline
         */
        public GameShowParameters(String... arguments) {
            //
            // Tolka angivna parametrar och gör snabb validering
            //
            numberOfTries = BigInteger.valueOf(Integer.parseInt(arguments[0]));
            if(numberOfTries.intValue() <= 0) {
                throw new IllegalArgumentException(ANTAL_SIMULERINGAR_MASTE_VARA_FLER_AN_NOLL);
            }
            numberOfBoxes = BigInteger.valueOf(Integer.parseInt(arguments[1]));
            numberOfBoxesToRemove = BigInteger.valueOf(Integer.parseInt(arguments[2]));
            if(numberOfBoxesToRemove.intValue() >= numberOfBoxes.intValue() - 1) {
                throw new IllegalArgumentException(ANTAL_BORTTAGNINGAR_MASTE_VARA_FARRE_AN_ANTAL_LADOR_MINUS_ETT);
            }
        }

        /**
         * Konstruktor som tar exakta parametrar
         * @param numberOfTries antal simuleringar
         * @param numberOfBoxes antal lådor i varje simulering
         * @param numberOfBoxesToRemove antal lådor att ta bort innan spelaren får välja
         */
        public GameShowParameters( BigInteger numberOfTries,
                                   BigInteger numberOfBoxes,
                                   BigInteger numberOfBoxesToRemove ) {
            this.numberOfBoxes = numberOfBoxes;
            this.numberOfBoxesToRemove = numberOfBoxesToRemove;
            this.numberOfTries = numberOfTries;
        }

        /**
         * Hämtar antal försök/simuleringar
         * @return antal simuleringar
         */
        public BigInteger getNumberOfTries() {
            return numberOfTries;
        }

        /**
         * Hämtar antal lådor i simulering
         * @return antal lådor
         */
        public BigInteger getNumberOfBoxes() {
            return numberOfBoxes;
        }

        /**
         * Hämtar antal lådor att ta bort innan valet ges till spelaren
         * @return antal lådor att ta bort
         */
        public BigInteger getNumberOfBoxesToRemove() {
            return numberOfBoxesToRemove;
        }

    }

}
