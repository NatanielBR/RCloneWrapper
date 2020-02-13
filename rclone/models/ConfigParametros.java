package rclone.models;

/**
 *
 * @author neoold
 */
public enum ConfigParametros {
    /**
     * Nome do remoto. Obrigatorio.
     */
    NOME(null),
    /**
     * Tipo do remoto. Obrigatorio.
     */
    TIPO(null),
    /**
     * ClientId, relacionado ao Drive. Opcional.
     */
    DRIVE_CLIENT_ID("client_id"),
    /**
     * ClientSecret, relacionado ao Drive. Opcional.
     */
    DRIVE_CLIENT_SECRET("client_secret"),
    /**
     * Escopo, relacionado ao Drive. Opcional.
     */
    DRIVE_SCOPE("scope"),
    /**
     * RootFolder, relacionado ao Drive. Opcional.
     */
    DRIVE_ROOT_FOLDER("root_folder"),
    /**
     * Usuario ou email. Obrigatorio.
     */
    USER("user"),
    /**
     * Senha. Obrigatorio.
     */
    PASSWORD("pass");

    public final String texto;

    private ConfigParametros(String t) {
        this.texto = t;
    }

}
