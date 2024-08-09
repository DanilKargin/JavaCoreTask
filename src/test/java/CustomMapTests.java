import org.example.CustomMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
@DisplayName("Testing custom map")
public class CustomMapTests  {

    @Test
    @DisplayName("Adding new object and get method")
    public void addNewObject(){
        Map<Integer, String> map = new CustomMap<>();
        Integer key = 1;
        String value = "Test value";
        map.put(key, value);
        Assertions.assertEquals(map.get(key), value);
        Assertions.assertEquals(map.size(), 1);
    }
    @Test
    @DisplayName("Remove object from map")
    public void removeObject(){
        Map<Integer, String> map = new CustomMap<>();
        Integer key = 1;
        String value = "Test value";
        map.put(key, value);
        Assertions.assertEquals(map.remove(key), value);
        Assertions.assertEquals(map.size(), 0);
    }

    @Test
    @DisplayName("Check addition of data from multiple threads")
    public void addFromMultipleStreams() throws InterruptedException {
        Map<Integer, String> map = new CustomMap<>();
        int iterationNumber = 10000;
        Runnable task1 = new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < iterationNumber; i++){
                    map.put(i, "Test value -" + i);
                }
            }
        };
        Runnable task2 = new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < iterationNumber; i++){
                    map.put(i, "Test value -" + i);
                }
            }
        };
        Runnable task3 = new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < iterationNumber; i++){
                    map.put(i, "Test value -" + i);
                }
            }
        };
        Thread th1 = new Thread(task1);
        Thread th2 = new Thread(task2);
        Thread th3 = new Thread(task3);

        th1.start();
        th2.start();
        th3.start();
        Thread.currentThread().sleep(1000);
        Assertions.assertEquals(map.size(), iterationNumber);
    }

    @Test
    @DisplayName("Remove object from multiple threads")
    public void removeFrom() throws InterruptedException {
        Map<Integer, String> map = new CustomMap<>();
        map.put(1, "Test value");
        final String[] results = new String[3];
        int numberNotNullObj = 0;
        Runnable task1 = new Runnable() {
            @Override
            public void run() {
                results[0] = map.remove(1);
            }
        };
        Runnable task2 = new Runnable() {
            @Override
            public void run() {
                results[1] = map.remove(1);
            }
        };
        Runnable task3 = new Runnable() {
            @Override
            public void run() {
                results[2] = map.remove(1);
            }
        };

        Thread th1 = new Thread(task1);
        Thread th2 = new Thread(task2);
        Thread th3 = new Thread(task3);

        th1.start();
        th2.start();
        th3.start();

        Thread.currentThread().sleep(100);
        for(var item : results){
            if(item != null){
                numberNotNullObj++;
            }
        }
        Assertions.assertEquals(numberNotNullObj, 1);
    }
    @Test
    @DisplayName("Edit value in map")
    public void editValue(){
        Map<Integer, String> map = new CustomMap<>();
        Integer key = 1;
        String firstValue = "First value";
        String secondValue = "Second value";

        var firstResult = map.put(key, firstValue);
        var secondResult = map.put(key, secondValue);
        Assertions.assertEquals(secondResult, firstValue);
    }

    @Test
    @DisplayName("Clear method")
    public void checkClearMap(){
        Map<Integer, String> map = new CustomMap<>();
        for(int i = 0; i < 100000; i++){
            map.put(i, "Test value - " + i);
        }
        map.clear();
        var result = map.get(1);
        Assertions.assertEquals(result, null);
        Assertions.assertEquals(map.size(), 0);
    }
}
