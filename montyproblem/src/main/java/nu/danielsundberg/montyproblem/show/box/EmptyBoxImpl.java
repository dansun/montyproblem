package nu.danielsundberg.montyproblem.show.box;

/**
 * Representerar en låda som inte innehåller pengar.
 */
public class EmptyBoxImpl extends AbstractBoxImpl {

    /**
     * Default konstruktor för tom låda
     */
    public EmptyBoxImpl() {
        super(NO_PRIZE);
    }

}