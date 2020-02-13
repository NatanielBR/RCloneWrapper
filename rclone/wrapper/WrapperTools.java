/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rclone.wrapper;

import java.lang.reflect.Array;

/**
 *
 * @author neoold
 */
public class WrapperTools {
    /**
     * Metodo que generico que adiciona um valor no inicio de um Array.
     *
     * @param <T> Uma classe generica
     * @param arr Um array generico.
     * @param value Um valor generico.
     * @return Um novo array sendo adicionado o valor generico no seu inico.
     */
    public static <T> T[] arrayFirtAppend(T[] arr, T value) {
        @SuppressWarnings("unchecked")
		T[] arr2 = (T[]) Array.newInstance(value.getClass(), arr.length + 1);
        System.arraycopy(arr, 0, arr2, 1, arr2.length - 1);
        arr2[0] = value;
        return arr2;
    }
    public static<T> T[] arrayFirtAppend(T[] arr, T... values){
        T[] nArr = null;
        for (T value : values){
            nArr = arrayFirtAppend(nArr == null ? arr : nArr, value);
        }
        return nArr;
    }
}
