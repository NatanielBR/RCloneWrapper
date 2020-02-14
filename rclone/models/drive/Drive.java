package rclone.models.drive;

import java.util.HashMap;
import rclone.models.ConfigParametros;
import rclone.models.RemoteType;
import rclone.wrapper.RCloneWrapper;

/**
 * Classe que Representa o Drive. Em um futuro, essa classe terá um peso maior.
 * Ou não.
 *
 * @author neoold
 */
public class Drive extends RemoteType {

    public Drive(RCloneWrapper wrapper, HashMap<ConfigParametros, String> parametros) {
        super(wrapper, new ConfigParametros[]{ConfigParametros.NOME, ConfigParametros.TIPO}, parametros);
    }
}
