/**
 * Created by qianqian on 04/01/2018.
 */
public class Searching {

    public static int binSearchR(int[] a, int start, int end, int key) {
        if (start <= end) {     // Important: =, if not have "=", if we want to search the first item in a sorted array, it will not find it.
            int mid = (start + end) / 2;
            if (key < a[mid])
                return binSearchR(a, start, mid - 1, key);
            else if (key > a[mid])
                return binSearchR(a, mid + 1, end, key);
            else
                return mid;
        }
        return -1;
    }

    public static int binSearch(int a[], int key) {
        int start = 0;
        int end = a.length -1;

        while(start <= end) {
            int mid = (start + end)/2;
            if (key == a[mid]) {
                return mid;
            }
            else if (key < a[mid]) {
                end = mid -1;
            }
            else {
                start = mid + 1;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println("********** Binary Search in Recursive method **********");
        int[] a = {4, 2, 1, 6, 3, 6, 0, -5, 1, 1};
        Sorting.mergeSort(a, 0, a.length - 1);
        System.out.print("Sorted array : ");
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }
        System.out.println();

        int ret = binSearchR(a, 0, a.length -1, 6);
        int ret2 = binSearchR(a, 0, a.length -1, 9);
        System.out.println("Key index : " +  ret);
        System.out.println("Key index : " +  ret2);

        int b[] = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        System.out.print("Sorted array : ");
        for (int i = 0; i < b.length; i++) {
            System.out.print(b[i] + " ");
        }
        System.out.println();
        int ret3 =  binSearchR(b, 0, b.length -1, 1);
        System.out.println("Key index : " +  ret3);

        System.out.println("********** Binary Search in non-Recursive method **********");
        int c[] = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        System.out.print("Sorted array : ");
        for (int i = 0; i < c.length; i++) {
            System.out.print(c[i] + " ");
        }
        System.out.println();
        int ret4 =  binSearch(c, 1);
        System.out.println("Key index : " +  ret4);
    }
}
