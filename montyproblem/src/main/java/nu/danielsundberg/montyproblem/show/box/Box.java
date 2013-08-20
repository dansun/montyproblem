package nu.danielsundberg.montyproblem.show.box;

/**
 * Representerar en låda som kan ha eller vara utan pengar
 */
public interface Box {

    /**
     * Indikerar om lådan har pengar eller inte
     * @return true om lådan innehåller pengar
     */
    boolean hasPrize();

}
