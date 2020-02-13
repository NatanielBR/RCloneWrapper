package rclone.models.remotes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import rclone.wrapper.RCloneException;
import rclone.wrapper.RCloneWrapper;

/**
 * Classe que representa um remoto, entretando essa classe funciona corretamente
 * no Drive. Esse é o remoto de teste. A estrutura dos pacotes e dos remotos
 * deve ser parecido com essa. No futuro essa classe deve se tornar abstrada.
 * @author Nrold
 */
public class Remote {
    private RCloneWrapper rclone;
    private String name;
    /**
     * Classe que representa um remoto do google drive.
     * @param rclone O Wrapper do rclone.
     * @param name O nome do remoto.
     */
    public Remote(RCloneWrapper rclone, String name) {
        this.rclone = rclone;
        this.name = name;
    }
    /**
     * Obtem o nome do remoto.
     * @return 
     */
    public String getRemoteName() {
        return name;
    }
    
    /**
     * Equivalente à rclone about remote:
     * 
     * @return all lines of output of command.
     */
    public String[] about() {
        try{
            return rclone.readProcess(false, "about", getRemoteName()).toArray(new String[0]);
        }catch(RCloneException err){
            System.err.println(String.format("\n", err.getSaidaDoRclone()));
        }catch (Exception err) {
            err.printStackTrace();
        }
        return null;
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
            folder = String.format("%s%s",File.separator, path
                    .substring(0, path.length() - 1));
        } else {
            folder = "";
        }
        var list = new ArrayList<FileRemote>();
        try {
            var lines = rclone.readProcess(false, "lsf",
            		String.format("%s%s", getRemoteName(), folder), "-F", "pst", "-s", ";;;");
            	lines.stream().map(a -> a.split(";;;")).map(a -> {
                boolean isDirectory = a[0].endsWith("/");
                return new FileRemote(isDirectory, a[0],
                		String.format("%s%s%s",folder,File.separator,a[0]), a[2],
                		Long.parseLong(a[1]));
            }).forEach(list::add);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return list;
    }
}
