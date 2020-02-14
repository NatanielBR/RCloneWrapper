package rclone.models.mega;

import java.util.HashMap;
import rclone.models.Builder;
import rclone.models.ConfigParametros;
import rclone.models.ObrigatorioException;
import rclone.wrapper.RCloneWrapper;

/**
 * Builder do Mega.
 *
 * @author neoold
 */
public class MegaBuilder extends Builder{

    private HashMap<ConfigParametros, String> parametros;
    private RCloneWrapper wrapper;

    public MegaBuilder(RCloneWrapper wrapper) {
        super();
        parametros = new HashMap<>();
        this.wrapper = wrapper;
    }
    
    @Override
    public ConfigParametros[] parametrosBuild(){
        return new ConfigParametros[]{
            ConfigParametros.USER,
            ConfigParametros.PASSWORD
        };
    }
    
    @Override
    public Mega build() throws ObrigatorioException {
        type();
        var mega = new Mega(wrapper, parametros);
        if (!mega.verificarObrigatorio()) {
            throw new ObrigatorioException();
        }
        return mega;
    }

    
    @Override
    public MegaBuilder name(String name) {
        if (name == null) {
            throw new NullPointerException();
        }
        parametros.put(ConfigParametros.NOME, name);
        return this;
    }

    
    public MegaBuilder type() {
        parametros.put(ConfigParametros.TIPO, "mega");
        return this;
    }

    /**
     * Email do Mega, Obrigatorio.
     *
     * @param user
     * @return
     */
    public MegaBuilder user(String user) {
        if (user == null) {
            throw new NullPointerException();
        }
        parametros.put(ConfigParametros.USER, user);
        return this;
    }

    /**
     * Senha do mega, Obrigatorio.
     *
     * @param password
     * @return
     */
    public MegaBuilder pass(String password) {
        if (password == null) {
            throw new NullPointerException();
        }
        parametros.put(ConfigParametros.PASSWORD, password);
        return this;
    }
}
