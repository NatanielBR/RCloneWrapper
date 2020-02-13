package rclone.models.drive;

import java.util.HashMap;
import rclone.models.ConfigParametros;
import rclone.models.RemoteType;
import rclone.wrapper.RCloneWrapper;

/**
 *
 * @author neoold
 */
public class Drive extends RemoteType {

    public Drive(RCloneWrapper wrapper, HashMap<ConfigParametros, String> parametros) {
        super(wrapper, new ConfigParametros[]{ConfigParametros.NOME, ConfigParametros.TIPO}, parametros);
    }
}
