package nu.danielsundberg.montyproblem.show;

import nu.danielsundberg.montyproblem.show.box.Box;
import nu.danielsundberg.montyproblem.show.box.EmptyBoxImpl;
import nu.danielsundberg.montyproblem.show.box.WinningBoxImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Abstrakt klass för att representera alla typerr av gameshows med lådor
 */
public abstract class AbstractGameShowImpl implements GameShow {

    /**
     * Konstanter för felmeddelanden.
     */
    public static final String FAR_INTE_VALJA_SLUMPVIS_ANNAN_LADA_MED_MINDRE_LADOR_AN_2 =
            "Cant select a random box when the amount of boxes in the show is lower than 2.";
    public static final String GAMESHOW_IS_RIGGED_INGA_VINNANDE_LADOR =
            "Gameshow is rigged! No winning boxes.";
    public static final String KAN_INTE_TA_BORT_SA_MANGA_LADOR_FRAN_SHOWEN =
            "Cant remove that many boxes from show.";

    /**
     * Lådor som showen innehåller.
     * Default ett tomt set med lådor.
     */
    private Set<Box> boxes = new HashSet<Box>();

    /**
     * Skapa en ny låda
     * @param containingPrize anger om lådan skall innehålla vinst eller ej
     * @return ny låda
     */
    final Box createBox(boolean containingPrize) {
        //
        // Om boxen skall innehålla priset skapa en WinningBox,
        // annars skapa en tom lådda.
        //
        return containingPrize ?
                new WinningBoxImpl() :
                new EmptyBoxImpl();
    }

    /**
     * Välj en slumpvis låda från showens kvarvarande lådor.
     * @return vald låda
     */
    public final Box selectRandomBox() {
        //
        // Välj random nummer baserad på antal befintliga lådor
        // och returnera därefter lådan på den platsen i en array
        // baserad på vårt skapade set.
        //
        int randomSelectedIndex = new Random().nextInt(this.boxes.size());
        Box[] theBoxes = this.boxes.toArray(new Box[0]);
        return theBoxes[randomSelectedIndex];
    }

    /**
     * Välj en slumpvis låda från showens kvarvarande lådor utan vinst.
     * @return vald låda
     */
    final Box selectRandomBoxWithoutPrize() {
        //
        // Skapa set med enbart icke vinnande lådor
        //
        Set<Box> boxesWithoutPrize = new HashSet<Box>();
        for(Box boxToCheck : boxes) {
            if(!boxToCheck.hasPrize()) {
                boxesWithoutPrize.add(boxToCheck);
            }
        }
        //
        // Välj ett slumpvis valt nummer baserat på antal icke vinnande lådor
        // och returnera därefter lådan på den platsen i en array baserad på
        // vårt skapade set.
        //
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
    public final void removeRandomBoxesExcept(int numberOfBoxesToRemove, Box...boxesNotToRemove) {
        //
        // Validera att vi inte försöker ta bort för många lådor
        //
        if(numberOfBoxesToRemove >= boxes.size()+boxesNotToRemove.length) {
            throw new IllegalArgumentException(KAN_INTE_TA_BORT_SA_MANGA_LADOR_FRAN_SHOWEN);
        } else {
            //
            // Skapa ett set med lådor vi vill ta bort
            //
            Set<Box> boxesToRemove = new HashSet<Box>();
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
    public final Box selectRandomBoxExcept(Box...boxesNotToChoose) {
        //
        // Kontrollera att showen innehåller lådan och att det finns fler än 1
        //
        if(boxes.size() > 1 &&
                boxes.containsAll(Arrays.asList(boxesNotToChoose))){
            Box aRandomBox = selectRandomBox();
            //
            // Om vald låda finns i listan att inte välja, gör nytt försök
            // annars returnera vald låda.
            //
            return Arrays.asList(boxesNotToChoose).contains(aRandomBox) ?
                    selectRandomBoxExcept(boxesNotToChoose) : aRandomBox;
        } else {
            throw new IllegalArgumentException(FAR_INTE_VALJA_SLUMPVIS_ANNAN_LADA_MED_MINDRE_LADOR_AN_2);
        }
    }

    /**
     * Returnera showens lådor.
     * @return boxes i showen
     */
    public final Set<Box> getBoxes() {
        return this.boxes;
    }

    /**
     * Returnera box med prize
     * @return vinnande lådan
     */
    public final Box getBoxWithPrize() {
        //
        // Snurra igenom alla lådor och returnera den med vinsten.
        //
        for(Box boxToCheck : boxes) {
            if(boxToCheck.hasPrize()) {
                return boxToCheck;
            }
        }
        throw new IllegalStateException(GAMESHOW_IS_RIGGED_INGA_VINNANDE_LADOR);
    }

    /**
     * Addera given låda till showen
     * @param boxToAdd låda att lägga till
     */
    public void addBox(Box boxToAdd) {
        //
        // Addera angiven låda till vårt set med lådor.
        //
        this.boxes.add(boxToAdd);
    }


}
