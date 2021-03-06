package rclone.models.drive;

import java.util.HashMap;
import rclone.models.Builder;
import rclone.models.ConfigParametros;
import rclone.models.ObrigatorioException;
import rclone.models.RemoteType;
import rclone.wrapper.RCloneWrapper;

/**
 * Builder para a criação do Drive.
 *
 * @author neoold
 */
public class DriveBuilder extends Builder {

    private HashMap<ConfigParametros, String> parametros;
    private RCloneWrapper wrapper;

    public DriveBuilder(RCloneWrapper wrapper) {
        parametros = new HashMap<>();
        this.wrapper = wrapper;
    }

    @Override
    public ConfigParametros[] parametrosBuild() {
        return new ConfigParametros[]{
            ConfigParametros.DRIVE_CLIENT_ID,
            ConfigParametros.DRIVE_CLIENT_SECRET,
            ConfigParametros.DRIVE_ROOT_FOLDER,
            ConfigParametros.DRIVE_SCOPE
        };
    }

    @Override
    public RemoteType build() throws ObrigatorioException {
        parametros.put(ConfigParametros.TIPO, type());
        var drive = new RemoteType(wrapper, parametrosObrigadorios(), parametros);
        if (!drive.verificarObrigatorio()) {
            throw new ObrigatorioException();
        }
        return drive;
    }

    @Override
    public DriveBuilder name(String name) {
        if (name == null) {
            throw new NullPointerException();
        }
        parametros.put(ConfigParametros.NOME, name);
        return this;
    }

    @Override
    public String type() {
        return "drive";
    }

    /**
     * O client id, Opcional.
     *
     * @param id o id
     * @return o builder.
     */
    public DriveBuilder clientId(String id) {
        if (id == null) {
            return this;
        }
        parametros.put(ConfigParametros.DRIVE_CLIENT_ID, id);
        return this;
    }

    /**
     * O client secret, Opcional.
     *
     * @param secret
     * @return o builder.
     */
    public DriveBuilder clientSecret(String secret) {
        if (secret == null) {
            return this;
        }
        parametros.put(ConfigParametros.DRIVE_CLIENT_SECRET, secret);
        return this;
    }

    /**
     * O escopo do drive, Opcional.
     *
     * @param scope
     * @return
     */
    public DriveBuilder scope(Escopo scope) {
        if (scope == null) {
            return this;
        }
        parametros.put(ConfigParametros.DRIVE_SCOPE, scope.texto);
        return this;
    }

    /**
     * O root folder do drive, Opcional.
     *
     * @param root
     * @return
     */
    public DriveBuilder rootFolder(String root) {
        if (root == null) {
            return this;
        }
        parametros.put(ConfigParametros.DRIVE_ROOT_FOLDER, root);
        return this;
    }

    @Override
    public ConfigParametros[] parametrosObrigadorios() {
        return new ConfigParametros[]{
            ConfigParametros.NOME,
            ConfigParametros.TIPO};
    }

    /**
     * Tipos de escopo, documentação do RClone.
     */
    public enum Escopo {
        /**
         * Full access all files, excluding Application Data Folder.
         */
        DRIVE("drive"),
        /**
         * Read-only access to file metadata and file contents.
         */
        DRIVE_READ_ONLY("drive.readonly"),
        /**
         * Access to files created by rclone only. These are visible in the
         * drive website. File authorization is revoked when the user
         * deauthorizes the app.
         */
        DRIVE_FILE("drive.file"),
        /**
         * Allows read and write access to the Application Data folder. This is
         * not visible in the drive website.
         */
        DRIVE_APPFOLDER("drive.appfolder"),
        /**
         * Allows read-only access to file metadata but does not allow any
         * access to read or download file content.
         */
        DRIVE_METADATA_READ_ONLY("drive.metadata.readonly");
        public final String texto;

        private Escopo(String texto) {
            this.texto = texto;
        }

    }
}
