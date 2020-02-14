package rclone.models.remotes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rclone.models.RemoteType;
import rclone.wrapper.RCloneException;
import rclone.wrapper.RCloneWrapper;

/**
 * Classe que representa um remoto. Todos os comandos relacionado ao remoto,
 * como lista arquivos, copiar, mover ou melhor exemplificado: rclone * remote:,
 * irá vir para aqui com exceção do delete. Esse metodo se encontra aqui como
 * forma mais agil de realizar essa ação.
 *
 * @author Nrold
 */
public class Remote {

    private RCloneWrapper rclone;
    private String name;
    private String tipo;

    /**
     * Classe que representa um remoto do google drive.
     *
     * @param rclone O Wrapper do rclone.
     * @param name O nome do remoto.
     */
    public Remote(RCloneWrapper rclone, String name, String tipo) {
        this.rclone = rclone;
        this.name = name;
        this.tipo = tipo;
    }

    /**
     * Obtem o nome do remoto.
     *
     * @return
     */
    public String getRemoteName() {
        return name;
    }

    public String getType() {
        return tipo;
    }

    /**
     * Equivalente à rclone about remote:
     *
     * @return all lines of output of command.
     */
    public String[] about() {
        try {
            return rclone.readProcess(false, "about", getRemoteName()).toArray(new String[0]);
        } catch (RCloneException err) {
            System.err.println(String.format("\n", err.getSaidaDoRclone()));
        } catch (Exception err) {
            err.printStackTrace();
        }
        return null;
    }

    /**
     * Metodo para deletar o remoto. Esse metodo é o mesmo que
     * RemoteType.delete(wrapper,remote);
     */
    public void delete() {
        RemoteType.delete(rclone, this);
    }

    /**
     * Equivalente a 'rclone lsf remote: -F psth -s \'
     *
     * @return Uma lista de arquivos remotos.
     */
    public List<FileRemote> listFiles() {
        try {
            return listFiles(null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Equivalente a 'rclone lsf remote:diretorio -F psth -s \'
     *
     * @param directory Diretorio remoto, é usado o FileRemote.isDirectory para
     * verificar se é um diretorio.
     * @return Uma lista de arquivos e diretorios remotodos dentro da pasta
     * especificada.
     * @throws Exception Caso algum erro aconteça
     */
    public List<FileRemote> listFiles(FileRemote directory) throws Exception {
        String folder;
        if (directory != null) {
            if (!directory.isDirectory()) {
                throw new Exception("So e possivel listar arquivos em um diretorio");
            }
            var path = directory.getPath();
            folder = String.format("%s%s", File.separator, path.substring(0, path.length() - 1));
        } else {
            folder = "";
        }
        var list = new ArrayList<FileRemote>();
        try {
            var lines = rclone.readProcess(false, "lsf", String.format("%s:%s", getRemoteName(), folder), "-F", "pst",
                    "-s", ";;;");
            lines.stream().map(a -> a.split(";;;")).map(a -> {
                boolean isDirectory = a[0].endsWith("/");
                return new FileRemote(isDirectory, a[0], String.format("%s%s%s", folder, File.separator, a[0]), a[2],
                        Long.parseLong(a[1]));
            }).forEach(list::add);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return list;
    }
}
