package rclone.models.mega;

import java.util.HashMap;
import rclone.models.Builder;
import rclone.models.ConfigParametros;
import rclone.models.ObrigatorioException;
import rclone.models.RemoteType;
import rclone.wrapper.RCloneWrapper;

/**
 * Builder do Mega.
 *
 * @author neoold
 */
public class MegaBuilder extends Builder {

    private HashMap<ConfigParametros, String> parametros;
    private RCloneWrapper wrapper;

    public MegaBuilder(RCloneWrapper wrapper) {
        parametros = new HashMap<>();
        this.wrapper = wrapper;
    }

    @Override
    public ConfigParametros[] parametrosBuild() {
        return new ConfigParametros[]{
            ConfigParametros.USER,
            ConfigParametros.PASSWORD
        };
    }

    @Override
    public ConfigParametros[] parametrosObrigadorios() {
        return new ConfigParametros[]{ConfigParametros.NOME,
            ConfigParametros.TIPO,
            ConfigParametros.USER,
            ConfigParametros.PASSWORD};
    }

    @Override
    public RemoteType build() throws ObrigatorioException {
        parametros.put(ConfigParametros.TIPO, type());
        var mega = new RemoteType(wrapper, parametrosObrigadorios(),
                parametros);
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

    @Override
    public String type() {
        return "mega";
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
