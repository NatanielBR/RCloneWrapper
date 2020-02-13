package rclone.models;

/**
 * Exception caso algum valor obrigatorio não esteja informado.
 * Note que os valores obrigatorios são relativos e cada
 * remoto posui uma propria lista de exigencias.
 * @author neoold
 */
@SuppressWarnings("serial")
public class ObrigatorioException extends Exception{

    public ObrigatorioException() {
        super("É nescessario que os valores obrigatorios sejam informados.");
    }
    
}
