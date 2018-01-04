package org.learn.algorithm;

/**
 * Created by qianqian on 04/01/2018.
 */
public class Sorting {

    public static void insertSort(int[] a) {
        int i,j;
        for(i = 1; i < a.length; i++) {
            int key = a[i];
            for(j = i-1; (j >= 0) && (a[j] > key); j--){
                a[j+1] = a[j];
            }
            a[j+1] = key;
        }
    }

    public static void selectSort(int[] a) {
        for(int i = 0; i < a.length; i++) {
            int index = i;
            for (int j = i + 1; j < a.length; j++) {
                if (a[j] < a[index]) {
                    index = j;
                }
            }
            if (index != i) {
                int temp = a[index];
                a[index] = a[i];
                a[i] = temp;
            }
        }
    }

    public static void merge(int[] a, int s1, int e1, int s2, int e2) {
        int i, j;
        i = s1;
        j = s2;

        int k = 0;
        int[] tmp = new int[e2 - s1 + 1];

        while ((i <= e1) && (j <= e2)) {
            if (a[i] <= a[j]) {
                tmp[k++] = a[i++];
            }
            else {
                tmp[k++] = a[j++];
            }
        }
        while (i <= e1)
            tmp[k++] = a[i++];
        while (j <= e2)
            tmp[k++] = a[j++];

        // move tmp array to original array
        k = s1;
        for (int m = 0; m < tmp.length; m++) {
            a[k++] = tmp[m];
        }
    }

    public static void mergeSort(int[] a, int start, int end) {
        int mid = (start + end) / 2;
        if (start < end) {
            mergeSort(a, start, mid);
            mergeSort(a, mid + 1, end);
            merge(a, start, mid, mid + 1, end);
        }
    }

    public static int partition(int[] a, int start, int end) {
        int pivot = a[start];
        int i = start;
        for (int j = (start + 1); j<= end; j++) {
            if (a[j] <= pivot) {
                i = i + 1;
                int temp = a[j];
                a[j] = a[i];
                a[i] = temp;
            }
        }
        int temp = a[i];
        a[i] = pivot;
        a[start] = temp;
        return i;
    }

    public static void quickSort(int[] a, int start, int end) {
        if (start < end) {
            int i = partition(a, start, end);
            quickSort(a, start, i - 1);
            quickSort(a, i + 1, end);
        }

    }

    public static int randomPartition(int[] a, int start, int end) {
        int random = (int)(Math.random() * (end - start + 1)) + start;
        // exchange the pivot (the first elem in array with this random index number in this array)
        int temp = a[start];
        a[start] = a[random];
        a[random] = temp;
        return partition(a, start, end);
    }

    public static void randomQuickSort(int[] a, int start, int end) {
        if (start < end) {
            int i = randomPartition(a, start, end);
            randomQuickSort(a, start, i - 1);
            randomQuickSort(a, i + 1, end);
        }
    }

    public static void heapAdjust(int[] a, int parent, int length) {
        int temp = a[parent];
        int child = 2*parent + 1; // left child

        while (child < length) {
            // if has right child and right child is larger than left child --> select this right child
            if ((child + 1) < length && a[child] < a[child + 1]) {
                child++;
            }

            // if parent is already larger than child --> just break
            if (temp >= a[child])
                break;

            a[parent] = a[child];
            parent = child;
            child = 2*child + 1;
        }
        a[parent] = temp;
    }

    public static void heapSort(int[] a) {
        // initially create heap
        for(int i = a.length/2 - 1; i >= 0; i--) {
            heapAdjust(a, i, a.length - 1);
        }

        // loop n-1 times to sort
        for(int i = a.length - 1; i > 0; i--) {
            // swap the first and the last element
            int temp = a[i];
            a[i] = a[0];
            a[0] = temp;

            heapAdjust(a, 0, i);
            System.out.format("No. %d: \t", a.length - i);
            for (int j = 0; j < a.length; j++) {
                System.out.print(a[j] + " ");
            }
            System.out.println();
        }

    }

    public static void bubbleSort(int[] a) {

    }


    public static void main(String[] args) {
        int[] a = {4, 2, 9, 6, 3, 7, 0, -5, 1, 1};
//        insertSort(a);
//        selectSort(a);
//        mergeSort(a, 0, a.length -1);
//        quickSort(a, 0, a.length - 1);
//        randomQuickSort(a, 0, a.length -1);
        heapSort(a);

        for(int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }
    }
}