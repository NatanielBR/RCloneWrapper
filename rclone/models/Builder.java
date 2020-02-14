/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rclone.models;

/**
 *
 * @author neoold
 */
public abstract class Builder {
    /**
     * Metodo para construir um remoto, tambem irá verificar as exigencias
     * antes de "entregar" a classe.
     *
     * @param <T> Uma classe que herda de RemoteType.
     * Após ser criado o remoteType, é nescessario criar o remoto
     * usando o create.
     * @return Um remoto pronto para ser criado.
     * @throws ObrigatorioException Caso algum valor obrigatorio não seja
     * informado.
     */
    public abstract<T extends RemoteType> T build() throws ObrigatorioException;
    /**
     * Nome do remoto, Obrigatorio.
     *
     * @param <T> O Builder de forma mais especifico.
     * @param name O nome do remoto, sem o ":".
     * @return O Builder.
     */
    public abstract<T extends Builder> T name(String name);
    /**
     * Tipo do remoto, bye.
     *
     * @param <T> O Builder de forma mais especifico.
     * @return O Builder
     */
    public abstract<T extends Builder> T type();
    /**
     * Metodo para listar os parametros nescessarios para
     * esse remoto. A lista é relativa a exclusivamente esse
     * remoto.
     * @return Uma lista com os parametros.
     */
    public abstract ConfigParametros[] parametrosBuild();
}
