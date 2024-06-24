package com.niks.flashylights;

import java.util.HashMap;

public class PractiseActivity {

    public static void main(String[] args) {
        int[] arr = {1, 3, 0, 0, 2, 3, 1, 4, 5, 1, 6};
        countUniqueOccurrences(arr);
    }

    public static void countUniqueOccurrences(int[] arr) {
        HashMap<Integer, Integer> map = new HashMap<>();

        for (int num : arr) {
            if (map.containsKey(num)) {
                map.put(num, map.get(num) + 1);
            } else {
                map.put(num, 1);
            }
        }

        // Print occurrences of each unique number
        for (int key : map.keySet()) {
            System.out.println(key + " occurs " + map.get(key) + " times");
        }
    }
}