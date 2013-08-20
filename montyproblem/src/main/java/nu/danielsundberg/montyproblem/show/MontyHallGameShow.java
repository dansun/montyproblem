package nu.danielsundberg.montyproblem.show;

import nu.danielsundberg.montyproblem.show.box.Box;

import java.util.Random;

/**
 * Representation av en show av Monty Hall-modell.
 */
public class MontyHallGameShow extends AbstractGameShowImpl implements GameShow {

    public static final int DEFAULT_ANTAL_LADOR = 3;

    /**
     * Default konstruktor för montys show
     */
    public MontyHallGameShow() {
        this(DEFAULT_ANTAL_LADOR);
    }

    /**
     * Parameterstyrd konstruktor för givet antal lådor i vår show
     * @param numberOfBoxes representerar antal lådor i showen.
     */
    public MontyHallGameShow(int numberOfBoxes) {
        //
        // Generera en show med angivet antal lådor och enbart en vinst.
        //
        Boolean gameshowHasABoxWithPrize = Boolean.FALSE;
        for(int i = 0; i < numberOfBoxes; i++) {
            boolean isLastBox = (i == numberOfBoxes-1);
            Box theNewBox;
            //
            // Om showen redan har en vinst, genererar bara nya tomma lådor
            //
            if(gameshowHasABoxWithPrize) {
                theNewBox = createBox(Boolean.FALSE);
            } else {
                //
                // Genererar en slumpmässig låda.
                // Om vi är på sista lådan och inte har någon vinst tvingar
                // vi fram en vinnande låda.
                //
                theNewBox = (isLastBox && ! gameshowHasABoxWithPrize) ?
                                this.createBox(Boolean.TRUE) :
                                this.createBox(new Random().nextBoolean());
                //
                // Kontrollera om showen har en vinnande låda
                //
                gameshowHasABoxWithPrize =
                        theNewBox.hasPrize() ?
                            Boolean.TRUE :
                            Boolean.FALSE;
            }
            this.addBox(theNewBox);
        }
    }

}
