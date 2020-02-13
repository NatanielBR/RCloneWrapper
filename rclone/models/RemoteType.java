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
 *
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

    public void create(Consumer<String> onUpdate) throws ObrigatorioException {
        var args = mapParaArray(parametros);
        args = WrapperTools.arrayFirtAppend(args, "create", "config");
        System.out.println(String.join(" ", args));
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

    private String[] mapParaArray(HashMap<ConfigParametros, String> parametros) {
        var chaves = parametros.keySet();
        List<String> kv = new ArrayList<>();
        kv.add(parametros.remove(ConfigParametros.NOME));
        kv.add(parametros.remove(ConfigParametros.TIPO));
        parametros.forEach((a, b) -> {
//            b = b.contains(" ")? String.format("\"%s\"", b):b;
            kv.add(a.texto);
            kv.add(b);
        });
//        chaves.stream().map(a->parametros.get(a))
//                .map(a-> a.contains(" ")? String.format("\"%s\"", a):a)
//                .forEach(kv::add);
        return kv.toArray(new String[0]);
    }

    private boolean isPar(int valor) {
        return valor % 2 == 0;
    }

    public ConfigParametros[] getObrigatorios() {
        return obrigatorios;
    }

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
