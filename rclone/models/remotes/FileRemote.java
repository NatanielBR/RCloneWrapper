package rclone.models.remotes;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Classe que representa um arquivo ou diretorio.
 *
 * @author Nrold
 */
public class FileRemote {

    private boolean isDirectory;
    private String name;
    private String path;
    private Date lastModification;
    private long size;

    public FileRemote(boolean isDirectory, String name, String path, String lastModification, long size) {
        this.isDirectory = isDirectory;
        this.name = name;
        try {
            this.lastModification = DateFormat.getInstance().parse(lastModification);
        } catch (ParseException ex) {
            this.lastModification = null;
        }
        if (isDirectory) {
            path = path.substring(1);
        }
        this.path = path;
        this.size = size;
    }

    /**
     * Verificador se o arquivo remoto é um diretorio
     *
     * @return true caso seja um diretorio e false caso seja um arquivo.
     */
    public boolean isDirectory() {
        return isDirectory;
    }

    /**
     * Obtem o nome do arquivo.
     *
     * @return O nome do arquivo
     */
    public String getName() {
        return name;
    }

    /**
     * Obtem o caminho do arquivo, mas sem o nome do remoto.
     *
     * @return O caminho em do arquivo.
     */
    public String getPath() {
        return path;
    }

    /**
     * Obtem a ultima data de modificação.
     *
     * @return A ultima data de modificação ou null caso não foi informado.
     */
    public Date getLastModification() {
        return lastModification;
    }

    /**
     * Obtem o tamanho do arquivo em bytes.
     *
     * @return Um numero representando o tamanho do arquivo em bytes.
     */
    public long getSize() {
        return size;
    }

}
