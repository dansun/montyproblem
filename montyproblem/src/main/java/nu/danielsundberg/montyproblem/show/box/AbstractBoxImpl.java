package nu.danielsundberg.montyproblem.show.box;

/**
 * Abstrakt klass som representerar alla lådor
 */
public abstract class AbstractBoxImpl implements Box {

    /**
     * Värden för lådor, om den är vinnande eller ej.
     */
    public static final Boolean PRIZE = Boolean.TRUE;
    public static final Boolean NO_PRIZE = Boolean.FALSE;

    /**
     * Lådans innehåll.
     */
    private boolean containsPrize;

    /**
     * Default construktor
     * @param containsPrize om lådan innehåller vinsten
     */
    public AbstractBoxImpl(Boolean containsPrize) {
        this.containsPrize = containsPrize;
    }

    /**
     * Representerar om lådan innehåller vinst eller ej.
     * @return true om lådan innehåller vinsten.
     */
    public final boolean hasPrize() {
        return containsPrize;
    }

}
