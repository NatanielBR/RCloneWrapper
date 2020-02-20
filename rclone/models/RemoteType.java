package rclone.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import rclone.models.remotes.Remote;
import rclone.wrapper.RCloneWrapper;
import rclone.wrapper.WrapperTools;

/**
 * Classe pai que representa um remoto abstrato.
 *
 * @author neoold
 */
public class RemoteType extends Remote {

    protected RCloneWrapper wrapper;
    protected LinkedBlockingDeque<String> lines;
    protected ConfigParametros[] obrigatorios;
    protected HashMap<ConfigParametros, String> parametros;

    private Process processRClone;

    /**
     * Construtor para criar o RemoteType.
     *
     * @param wrapper Wrapper que sera usado para contruir o Remoto.
     * @param obrigatorios Parametros obrigatorios para ser verificado, pode ser
     * null entretando o metodo verficarObrigatorio não terá efeito.
     * @param parametros Parametros que será usado para construir o Remoto.
     */
    public RemoteType(RCloneWrapper wrapper, ConfigParametros[] obrigatorios,
            HashMap<ConfigParametros, String> parametros) {
        super(wrapper, parametros.get(ConfigParametros.NOME),
                parametros.get(ConfigParametros.TIPO));
        this.wrapper = wrapper;
        this.lines = new LinkedBlockingDeque<>();
        this.obrigatorios = obrigatorios;
        this.parametros = parametros;
    }

    /**
     * Metodo que irá criar uma instancia do RClone com o alguns parametros ja
     * definidos, até o momento o seu equivalente é: "rclone config create".
     *
     * @param onUpdate Uma ação que será executada sempre que houver atualização
     * no texto que o rclone mandar.
     */
    public void create(Consumer<String> onUpdate) {
        var args = mapParaArray(parametros);
        args = WrapperTools.arrayFirtAppend(args, new String[]{"config", "create"});
        try {
            processRClone = wrapper.getRcloneRuntime(args);
            readInput(processRClone);
            while (processRClone.isAlive()) {
                StringBuilder bu = new StringBuilder();
                String c = lines.take();
                do {
                    bu.append(c).append(System.lineSeparator());
                } while ((c = lines.poll(500, TimeUnit.MILLISECONDS)) != null);
                onUpdate.accept(bu.toString());
                Thread.sleep(500);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
        }

    }
    /**
     * Metodo para finalizar o processo RClone.
     * Será chamado o metodo Process.destroy.
     * @see Process
     */
    public void finalizarProcesso() {
        processRClone.destroy();
    }

    /**
     * Metodo para apagar um remoto. É equivalente à: rclone config delete
     * remote
     *
     * @param wrapper O wrapper para executar o comando.
     * @param remote O remoto, não deve ser uma String para evitar problemas.
     */
    public static void delete(RCloneWrapper wrapper, Remote remote) {
        try {
            var name = remote.getRemoteName();
            name = name.substring(0, name.length());
            wrapper.getRcloneRuntime("config", "delete", name).waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo para verificar se os valores obrigatorios foi informado.
     *
     * @return true se, e somente se, todos foram informados e false caso
     * contrario.
     */
    public boolean verificarObrigatorio() {
        var keys = parametros.keySet();
        // verifica se a lista de chaves é menor que o obrigatorio
        if (keys.size() < obrigatorios.length) {
            return false;
        }
        for (var ob : obrigatorios) {
            boolean cond = keys.stream().anyMatch(a -> a.equals(ob));
            // verifica se o obrigatorio foi "setado"
            // caso não, não cumpriu os obrigatorios.
            if (cond == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * Metodo para transformar um HashMap um Array compativel com o Process.
     *
     * @param parametros Uma dicionario de parametros e valores.
     * @see Process
     * @return Um array de String compativel com o Process.
     */
    private String[] mapParaArray(HashMap<ConfigParametros, String> parametros) {
        List<String> kv = new ArrayList<>();
        kv.add(parametros.remove(ConfigParametros.NOME));
        kv.add(parametros.remove(ConfigParametros.TIPO));
        parametros.forEach((a, b) -> {
            kv.add(a.texto);
            kv.add(b);
        });
        return kv.toArray(new String[0]);
    }

    /**
     * Obter a lista de chaves obrigatorias.
     *
     * @return Lista de parametros obrigatorios. Essa lista é relativa para cada
     * Remoto. Veja a classe do remoto ou a documentação do Rclone.
     */
    public ConfigParametros[] getObrigatorios() {
        return obrigatorios;
    }

    /**
     * Metodo que cria uma thread para ler o texto do processo. Para evitar
     * block de IO, se cria um Thread para esse proposito.
     *
     * @param proc Um processo para se poder ler seu texto.
     */
    private void readInput(Process proc) {
        var tf = new Thread(() -> {
            try (Scanner ent = new Scanner(proc.getInputStream())) {
                while (ent.hasNextLine()) {
                    lines.add(ent.nextLine());
                }
            } catch (Exception e) {
            }
            try (Scanner ent = new Scanner(proc.getErrorStream())) {
                while (ent.hasNextLine()) {
                    lines.add(ent.nextLine());
                }
            } catch (Exception e) {
            }

        });
        tf.start();
    }
}
