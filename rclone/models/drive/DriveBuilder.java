/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rclone.models.drive;

import java.util.HashMap;
import rclone.models.ConfigParametros;
import rclone.models.ObrigatorioException;
import rclone.wrapper.RCloneWrapper;

/**
 *
 * @author neoold
 */
public class DriveBuilder {

    private HashMap<ConfigParametros, String> parametros;
    private RCloneWrapper wrapper;

    public DriveBuilder(RCloneWrapper wrapper) {
        parametros = new HashMap<>();
        type("drive");
        this.wrapper = wrapper;
    }

    public Drive build() throws ObrigatorioException {

        var drive = new Drive(wrapper, parametros);
        if (!drive.verificarObrigatorio()) {
            throw new ObrigatorioException();
        }
        return drive;
    }

    public DriveBuilder name(String name) {
        if (name == null) {
            throw new NullPointerException();
        }
        parametros.put(ConfigParametros.NOME, name);
        return this;
    }

    private void type(String type) {
        parametros.put(ConfigParametros.TIPO, type);
    }

    public DriveBuilder clientId(String id) {
        if (id == null) {
            return this;
        }
        parametros.put(ConfigParametros.DRIVE_CLIENT_ID, id);
        return this;
    }

    public DriveBuilder clientSecret(String secret) {
        if (secret == null) {
            return this;
        }
        parametros.put(ConfigParametros.DRIVE_CLIENT_SECRET, secret);
        return this;
    }

    public DriveBuilder scope(String scope) {
        if (scope == null) {
            return this;
        }
        parametros.put(ConfigParametros.DRIVE_SCOPE, scope);
        return this;
    }

    public DriveBuilder rootFolder(String root) {
        if (root == null) {
            return this;
        }
        parametros.put(ConfigParametros.DRIVE_ROOT_FOLDER, root);
        return this;
    }
}
