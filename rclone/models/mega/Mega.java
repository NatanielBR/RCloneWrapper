package rclone.models.mega;

import java.util.HashMap;
import rclone.models.ConfigParametros;
import rclone.models.RemoteType;
import rclone.wrapper.RCloneWrapper;

/**
 *
 * @author neoold
 */
public class Mega extends RemoteType{
    
    public Mega(RCloneWrapper wrapper, HashMap<ConfigParametros, String> parametros) {
        super(wrapper, new ConfigParametros[]
        {ConfigParametros.NOME, ConfigParametros.TIPO,ConfigParametros.USER,ConfigParametros.PASSWORD},
                parametros);
    }
    
}
