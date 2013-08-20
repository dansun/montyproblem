package nu.danielsundberg.montyproblem.show.box;

/**
 * Representerar en låda som kan ha eller vara utan vinst
 */
public interface Box {

    /**
     * Indikerar om lådan har vinsten eller inte
     * @return true om lådan innehåller vinsten
     */
    boolean hasPrize();

}
