package rclone.models;

/**
 *
 * @author neoold
 */
public enum ConfigParametros {

    NOME(null),
    TIPO(null),
    DRIVE_CLIENT_ID("client_id"),
    DRIVE_CLIENT_SECRET("client_secret"),
    DRIVE_SCOPE("scope"),
    DRIVE_ROOT_FOLDER("root_folder"),
    USER("user"),
    PASSWORD("pass");

    public final String texto;

    private ConfigParametros(String t) {
        this.texto = t;
    }

}
