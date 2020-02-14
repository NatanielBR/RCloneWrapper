package rclone.wrapper;

import java.util.List;

/**
 * Exception relacionado ao rclone.
 *
 * @author Nrold
 */
@SuppressWarnings("serial")
public class RCloneException extends Exception {

    private List<String> rOut;

    public RCloneException(String message, List<String> rOut) {
        super(message);
        this.rOut = rOut;
    }

    /**
     * Saida do comando feito ao rclone.
     *
     * @return Uma lista de linhas com a saida do comando.
     */
    public List<String> getSaidaDoRclone() {
        return rOut;
    }

}
