package JobSorter;

import Entity.Job;

import java.util.List;

public class JobSorter {
    private static List<Job> quickSort(List<Job> list, int lowerIndex, int higherIndex) {
        int i = lowerIndex;
        int j = higherIndex;
        // Calcola il numero pivot, prendendo come pivot il numero centrale
        Job pivot_job = list.get(lowerIndex + (higherIndex - lowerIndex) / 2);
        int pivot = pivot_job.getDueDate();
        // Divide in due array
        while (i <= j) {
            while (list.get(i).getDueDate() < pivot) {
                i++;
            }
            while (list.get(j).getDueDate() > pivot) {
                j--;
            }
            if (i <= j) {
                list = switchElement(list, i, j);
                i++;
                j--;
            }
        }
        // Chiama quickSort() in modo ricorsivo
        if (lowerIndex < j)
            quickSort(list, lowerIndex, j);
        if (i < higherIndex)
            quickSort(list, i, higherIndex);
        return list;
    }

    private static List<Job> switchElement(List<Job> list, int i, int j) {
        Job temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
        return list;
    }
}