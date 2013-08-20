package nu.danielsundberg.montyproblem.show;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Representation av en show.
 */
public class MontyHallGameShow implements GameShow {

    public static final int DEFAULT_ANTAL_LADOR = 3;

    /**
     * Lådor som showen innehåller.
     */
    private Set<Box> boxes = new HashSet();

    /**
     * Default konstruktor för vår show
     */
    public MontyHallGameShow() {
        this(DEFAULT_ANTAL_LADOR);
    }

    /**
     * Parameterstyrd konstruktor för givet antal lådor i vår show
     * @param numberOfBoxes representerar antal lådor i showen.
     */
    public MontyHallGameShow(int numberOfBoxes) {
        Boolean gameshowHasABoxWithPrize = Boolean.FALSE;
        for(int i = 0; i < numberOfBoxes; i++) {
            Box aNewBox;
            //
            // Om showen redan har en vinst, genererar bara nya tomma lådor
            //
            if(gameshowHasABoxWithPrize) {
                aNewBox = createBox(Boolean.FALSE);
            } else {
                //
                // Genererar en slumpmässig låda med minst 1 vinst.
                //
                aNewBox =
                        (i == numberOfBoxes-1) &&
                                !gameshowHasABoxWithPrize?createBox(Boolean.TRUE):
                                createBox(new Random().nextBoolean());
                //
                // Har vi genererat en vinnande låda?
                //
                gameshowHasABoxWithPrize = aNewBox.hasPrize() ?
                        Boolean.TRUE : Boolean.FALSE;
            }
            boxes.add(aNewBox);
        }
    }

    /**
     * Skapa en ny låda
     * @param containingPrize anger om lådan skall innehålla vinst eller ej
     * @return ny låda
     */
    public Box createBox(boolean containingPrize) {
        return containingPrize ?
                new WinningBoxImpl() :
                new EmptyBoxImpl();
    }

    /**
     * Välj en slumpvis låda från showens kvarvarande lådor.
     * @return vald låda
     */
    public Box selectRandomBox() {
        int randomSelectedIndex = new Random().nextInt(this.boxes.size());
        Box[] theBoxes = this.boxes.toArray(new Box[0]);
        return theBoxes[randomSelectedIndex];
    }

    /**
     * Välj en slumpvis låda från showens kvarvarande lådor utan vinst.
     * @return vald låda
     */
    public Box selectRandomBoxWithoutPrize() {
        Set<Box> boxesWithoutPrize = new HashSet<Box>();
        for(Box boxToCheck : boxes) {
            if(!boxToCheck.hasPrize()) {
                boxesWithoutPrize.add(boxToCheck);
            }
        }
        int randomSelectedIndex = new Random().nextInt(boxesWithoutPrize.size());
        Box[] theBoxes = boxesWithoutPrize.toArray(new Box[0]);
        return theBoxes[randomSelectedIndex];
    }

    /**
     * Ta bort ett givet antal lådor från showen (som inte innehåller vinsten).
     * @param numberOfBoxesToRemove representerar antalet att ta bort
     * @param boxesNotToRemove låddor att inte ta bort
     * @throws IllegalArgumentException
     */
    public void removeRandomBoxesExcept(int numberOfBoxesToRemove, Box...boxesNotToRemove) throws IllegalArgumentException {
        if(numberOfBoxesToRemove >= boxes.size()+boxesNotToRemove.length) {
            throw new IllegalArgumentException("Cant remove that many boxes from show.");
        } else {
            Set<Box> boxesToRemove = new HashSet();
            for(int i = numberOfBoxesToRemove; i > 0; i--) {
                //
                // Välj en slumpvist utvald låda i showen (som inte är angivna lådor)
                //
                boxesToRemove.add(selectRandomBoxExcept(boxesNotToRemove));
            }
            //
            // Ta bort utvalda lådor från befintliga lådor.
            //
            this.boxes.removeAll(boxesToRemove);
        }
    }

    /**
     * Välj en av showens lådor utom angiven
     * @param boxesNotToChoose låda att inte välja
     * @return slumpvis låda
     */
    public Box selectRandomBoxExcept(Box...boxesNotToChoose) {
        //
        // Kontrollera att showen innehåller lådan och att det finns fler än 1
        //
        if(boxes.size()>1 && boxes.containsAll(Arrays.asList(boxesNotToChoose))){
            Box aRandomBox = selectRandomBox();
            return Arrays.asList(boxesNotToChoose).contains(aRandomBox) ?
                    selectRandomBoxExcept(boxesNotToChoose) : aRandomBox;
        } else {
            throw new IllegalArgumentException("Får inte välja slumpvis annan låda på show med mindre lådor än 2.");
        }
    }

    /**
     * Returnera showens lådor.
     * @return boxes i showen
     */
    public Set<Box> getBoxes() {
        return this.boxes;
    }

    /**
     * Returnera box med prize
     * @return vinnande lådan
     */
    public Box getBoxWithPrize() {
        for(Box boxToCheck : boxes) {
            if(boxToCheck.hasPrize()) {
                return boxToCheck;
            }
        }
        throw new IllegalStateException("Gameshow is rigged! No winning boxes.");
    }

}
