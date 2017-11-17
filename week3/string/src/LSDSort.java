import java.util.Random;

public class LSDSort {

    static final int R = 26;

    static void sort(char[] a) {
        int[] count = new int[R+1];
        for (char x : a) {
            int key = x - 'a';
            count[key + 1]++;
        }
        for (int i = 1; i < R; i++) {

            count[i] += count[i - 1];
        }
        char[] aux = new char[a.length];
        for (int i = 0; i < a.length; i++) {
            int key = a[i] - 'a';
            aux[count[key]++] = a[i];
        }
        for (int i = 0; i < a.length; i++) {
            a[i] = aux[i];
        }
    }

    public static void main(String[] args) {
        char a[] = new char[10];
        Random r = new Random();
        for(int i = 0; i < a.length; i++)
        {
            a[i] = (char)('a' + r.nextInt(R));
        }
        sort(a);
        for(char x : a) {
            System.out.println(x);
        }
    }
}
