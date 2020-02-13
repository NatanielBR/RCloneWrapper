package rclone.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import rclone.wrapper.RCloneWrapper;
import rclone.wrapper.WrapperTools;

/**
 * Classe pai que representa um remoto abstrato.
 * @author neoold
 */
public class RemoteType {

    protected RCloneWrapper wrapper;
    protected LinkedBlockingDeque<String> lines;
    protected ConfigParametros[] obrigatorios;
    protected HashMap<ConfigParametros, String> parametros;
    
    public RemoteType(RCloneWrapper wrapper, ConfigParametros[] obrigatorios, HashMap<ConfigParametros, String> parametros) {
        this.wrapper = wrapper;
        this.lines = new LinkedBlockingDeque<>();
        this.obrigatorios = obrigatorios;
        this.parametros = parametros;
    }
    /**
     * Metodo que irá criar uma instancia do RClone com o alguns parametros ja
     * definidos, até o momento o seu equivalente é: "rclone config create".
     * @param onUpdate
     * Uma ação que será executada sempre que houver atualização
     * no texto que o rclone mandar.
     * @throws ObrigatorioException
     * Caso a lista de obrigatorios não foi atentida.
     */
    public void create(Consumer<String> onUpdate) throws ObrigatorioException {
        var args = mapParaArray(parametros);
        //A ordem fica invertida, para agilizar somente inverti os valores
        //para ter a ordem correta.
        args = WrapperTools.arrayFirtAppend(args, "create", "config");
        try {
            var proc = wrapper.getRcloneRuntime(args);
            readInput(proc);
            while (proc.isAlive()) {
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
            ex.printStackTrace();
        }

    }
    /**
     * Metodo para verificar se os valores obrigatorios foi informado.
     * @return true se, e somente se, todos foram informados e false
     * caso contrario.
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
     * Metodo para transformar um HashMap um Array compativel com o
     * Process.
     * @param parametros Uma dicionario de parametros e valores.
     * @see Process
     * @return Um array de String compativel com o Process.
     */
    private String[] mapParaArray(HashMap<ConfigParametros, String> parametros) {
        var chaves = parametros.keySet();
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
     * @return 
     */
    public ConfigParametros[] getObrigatorios() {
        return obrigatorios;
    }
    /**
     * Metodo que cria uma thread para ler o texto do processo.
     * Para evitar block de IO, se cria um Thread para esse
     * proposito.
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

        });
        tf.start();
    }
}
