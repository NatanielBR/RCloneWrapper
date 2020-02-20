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
     * Metodo para construir um remoto, tambem irá verificar as exigencias antes
     * de "entregar" a classe.
     *
     * @param <T> Uma classe que herda de RemoteType. Após ser criado o
     * remoteType, é nescessario criar o remoto usando o create.
     * @return Um remoto pronto para ser criado.
     * @throws ObrigatorioException Caso algum valor obrigatorio não seja
     * informado.
     */
    public abstract <T extends RemoteType> T build() throws ObrigatorioException;

    /**
     * Nome do remoto, Obrigatorio.
     *
     * @param <T> O Builder de forma mais especifico.
     * @param name O nome do remoto, sem o ":".
     * @return O Builder.
     */
    public abstract <T extends Builder> T name(String name);

    /**
     * Tipo do remoto.
     *
     * @return Uma String com o tipo do remoto. De forma mais especifica ele irá
     * retornar o tipo do remoto em letras minusculas.
     */
    public abstract String type();

    /**
     * Metodo para listar os parametros nescessarios para esse remoto. A lista é
     * relativa a exclusivamente esse remoto.
     *
     * @return Um array com os parametros.
     */
    public abstract ConfigParametros[] parametrosBuild();

    /**
     * Lista de Parametros obrigatorios para o funcionamento basico do remoto.
     *
     * @return Um array com esses parametros.
     */
    public abstract ConfigParametros[] parametrosObrigadorios();
}
