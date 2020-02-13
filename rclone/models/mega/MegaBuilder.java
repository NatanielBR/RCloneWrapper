package rclone.models.mega;

import java.util.HashMap;
import rclone.models.ConfigParametros;
import rclone.models.ObrigatorioException;
import rclone.wrapper.RCloneWrapper;

/**
 *
 * @author neoold
 */
public class MegaBuilder {

    private HashMap<ConfigParametros, String> parametros;
    private RCloneWrapper wrapper;

    public MegaBuilder(RCloneWrapper wrapper) {
        parametros = new HashMap<>();
        type("mega");
        this.wrapper = wrapper;
    }

    public Mega build() throws ObrigatorioException {
        var mega = new Mega(wrapper, parametros);
        if (!mega.verificarObrigatorio()) {
            throw new ObrigatorioException();
        }
        return mega;
    }

    public MegaBuilder name(String name) {
        if (name == null) {
            throw new NullPointerException();
        }
        parametros.put(ConfigParametros.NOME, name);
        return this;
    }

    private void type(String type) {
        parametros.put(ConfigParametros.TIPO, type);
    }

    public MegaBuilder user(String user) {
        if (user == null) {
            throw new NullPointerException();
        }
        parametros.put(ConfigParametros.USER, user);
        return this;
    }

    public MegaBuilder pass(String password) {
        if (password == null) {
            throw new NullPointerException();
        }
        parametros.put(ConfigParametros.PASSWORD, password);
        return this;
    }
}
