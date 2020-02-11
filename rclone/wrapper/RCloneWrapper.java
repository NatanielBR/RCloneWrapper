/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rclone.wrapper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import rclone.models.drive.Remote;

/**
 * Classe que encapsula o Rclone. Alguns metodos do Rclone, pode ser acessado
 * atraves de outras classes como Remote.
 * @author Nrold
 */
public class RCloneWrapper {

    private final String rclonePath;

    public RCloneWrapper(String rclonePath) {
		this.rclonePath = rclonePath;
	}

	/**
     * Metodo que irá criar um processo do rclone com os argumentos passado como
     * parametro.
     *
     * @param args Argumentos para o Rclone
     * @return Um processo
     * @throws IOException
     */
    private Process getRcloneRuntime(String... args) throws IOException {
        return Runtime.getRuntime().exec(arrayFirtAppend(
        		args, new File(rclonePath).getAbsolutePath()));
    }

    /**
     * Metodo que generico que adiciona um valor no inicio de um Array.
     *
     * @param <T> Uma classe generica
     * @param arr Um array generico.
     * @param value Um valor generico.
     * @return Um novo array sendo adicionado o valor generico no seu inico.
     */
    private <T> T[] arrayFirtAppend(T[] arr, T value) {
        @SuppressWarnings("unchecked")
		T[] arr2 = (T[]) Array.newInstance(value.getClass(), arr.length + 1);
        System.arraycopy(arr, 0, arr2, 1, arr2.length - 1);
        arr2[0] = value;
        return arr2;
    }

    /**
     * Metodo que irá criar um processo Rclone com os argumentos fornecidos como
     * parametro no metodo. A saida sera lida e atraves do parametro booleano
     * será decidido se irá ser listado por token (utilizando Scanner.next) ou
     * listando por linha (utilizando Scanner.nextLine)
     *
     * @param byToken Ler por token
     * @param args Parametros que será passado para o Rclone
     * @return Lista com a saida do comando, sendo separado por linha ou por
     * token.
     * @throws IOException
     */
    public List<String> readProcess(boolean byToken, String... args) 
    		throws IOException, RCloneException {
        var list = new ArrayList<String>();
        Process proc = getRcloneRuntime(args);
        Scanner ent = new Scanner(proc.getInputStream(), "UTF-8");
        if (byToken) {
            while (ent.hasNext()) {
                list.add(ent.next());
            }
        } else {
            while (ent.hasNextLine()) {
                list.add(ent.nextLine());
            }
        }
        ent.close();
        // Para identificar um erro, percebi que o Rclone mostra uma data
        // Contando que não ocorra na virada do dia, deve funcionar bem.
        if (list.size() > 0 && list.get(0).startsWith(
        		new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime()))
        		){
            throw new RCloneException("Erro no RClone",list);
        }
        return list;
    }

    /**
     * Obtem a versão do Rclone.
     *
     * @return Uma String que contem a versão do Rclone, exemplo: 1.50.3 ou null caso
     * ocorra um erro.
     */
    public String version() {
        try {
            String version = null;
            for (var s : readProcess(true, "--version")) {
                if (s.startsWith("v")) {
                    version = s;
                    break;
                }
            }
            if (version == null) {
                return null;
            }
            return version.substring(1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Equivalente a "rclone listremotes --long"
     *
     * @return Uma lista de Remotos.
     */
    public List<Remote> listRemotes() {
        List<Remote> remotes = new ArrayList<>();
        try {
            var list = readProcess(false, "listremotes", "--long");
            for (int i = 0; i < list.size(); i++) {
                String[] s = list.get(i).split(" ");
                if (s[1].equals("drive")) {
                    remotes.add(new rclone.models.drive.Remote(this, s[0]));
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return remotes;
    }

    /**
     * Obtem um remoto com base em seu nome.
     *
     * @param remote Remoto a ser requerido.
     * @return Uma classe remoto, caso não exista irá retornar null.
     */
    public Remote getRemoteByName(String remote) {
        if (!remote.endsWith(":")) {
            remote = remote.concat(":");
        }
        final String remoteF = remote;
        return listRemotes().stream().filter(a -> a.getRemoteName().equals(remoteF)).findFirst().orElse(null);
    }

    /**
     * Metodo que verifica se o remoto se enconta configurado no rclone
     *
     * @param remote remoto a ser verificado
     * @return resultado da verificação, true caso exista e false caso não.
     */
    public boolean containsRemote(String remote) {
        return getRemoteByName(remote) != null;
    }

}
