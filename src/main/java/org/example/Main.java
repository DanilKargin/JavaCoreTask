package org.example;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Map<Integer, String> map = new CustomMap<>();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                map.put(1, "-_-");
            }
        };
        Thread th = new Thread(task);
        th.start();

        Thread.currentThread().sleep(1000);
        System.out.println(map.size());
    }
}