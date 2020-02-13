package rclone.models.mega;

import java.util.HashMap;
import rclone.models.ConfigParametros;
import rclone.models.ObrigatorioException;
import rclone.wrapper.RCloneWrapper;

/**
 * Builder do Mega.
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
    /**
     * Metodo para construir o Mega, tambem irá verificar as exigencias antes
     * de "entregar" a classe.
     *
     * @return Um remoto pronto para ser criado.
     * @throws ObrigatorioException Caso algum valor obrigatorio não seja
     * informado.
     */
    public Mega build() throws ObrigatorioException {
        var mega = new Mega(wrapper, parametros);
        if (!mega.verificarObrigatorio()) {
            throw new ObrigatorioException();
        }
        return mega;
    }
    /**
     * Nome do remoto, Obrigatorio.
     * @param name
     * @return 
     */
    public MegaBuilder name(String name) {
        if (name == null) {
            throw new NullPointerException();
        }
        parametros.put(ConfigParametros.NOME, name);
        return this;
    }
    /**
     * Tipo do remoto, bye.
     * @param type 
     */
    private void type(String type) {
        parametros.put(ConfigParametros.TIPO, type);
    }
    /**
     * Email do Mega, Obrigatorio.
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
