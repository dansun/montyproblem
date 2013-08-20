package nu.danielsundberg.montyproblem.show;

/**
 * Interface till vår gameshow
 */
public interface GameShow {

    /**
     * Välj en slumpmässig box
     * @return en av lådorna i showen.
     */
    Box selectRandomBox();

    /**
     * Hämta box med priset.
     * @return vinnande låddan
     */
    Box getBoxWithPrize();

    /**
     * Väljer en slumpmässig låda som inte
     * @param boxesNotToChoose låddor att inte returnera
     * @return vald slumpvis lådda
     */
    Box selectRandomBoxExcept(Box...boxesNotToChoose);

    /**
     * Välj bort ett givet antal lådor slumpmässigt.
     * @param numberOfBoxesToRemove representerar antalet att ta bort
     * @param boxesNotToRemove array med låddor att inte ta bort
     * @throws IllegalArgumentException
     */
    void removeRandomBoxesExcept(int numberOfBoxesToRemove, Box...boxesNotToRemove)
            throws IllegalArgumentException;

}
