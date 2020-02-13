package rclone.models;

/**
 *
 * @author neoold
 */
public class ObrigatorioException extends Exception{

    public ObrigatorioException() {
        super("É nescessario que os valores obrigatorios sejam informados.");
    }
    
}
