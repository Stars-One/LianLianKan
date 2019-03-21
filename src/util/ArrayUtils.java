package util;

import java.util.Random;

/**
 * @author StarsOne
 * @date Create in  2019-3-20 0020 20:25:52
 * @description
 */
public class ArrayUtils {
    
    private static void  swap(int[] a, int i, int j){
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    /**
     * 随机打乱数组
     * 会改变原本的数组
     * @param array   需要打乱的数组
     * @return 已经打乱的数组
     */
    public static int[]  random(int[] array) {
        Random rand = new Random();
        int length = array.length;
        for ( int i = length; i > 0; i-- ){
            int randInd = rand.nextInt(i);
            swap(array, randInd, i - 1);
        }
        return array;
    }

}
