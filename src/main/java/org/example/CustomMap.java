package org.example;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomMap<K, V> extends AbstractMap<K,V> implements Map<K,V>, Cloneable, Serializable {
    private class MapNode<K, V> implements Map.Entry<K, V>{
        final K key;
        V value;
        MapNode<K, V> nextNode;
        public MapNode(K key, V value){
            this.key = key;
            this.value = value;
        }
        public K getKey(){ return key; }
        public V getValue(){ return value; }
        @Override
        public V setValue(V value) {
            var oldValue = this.value;
            this.value = value;
            return oldValue;
        }

    }
    private int capacity = 100;
    private int size;
    private MapNode<K, V>[] nodes;

    public CustomMap(){
        nodes = new MapNode[capacity];
        //size = new AtomicInteger();
    }
    public CustomMap(int copacity) throws NegativeArraySizeException {
        this.capacity = copacity;
        nodes = new MapNode[copacity];
        //size = new AtomicInteger();
    }
    private int customHash(K key){
        return key.hashCode() % capacity;
    }
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if(key == null){
            return false;
        }
        var node = nodes[customHash((K)key)];
        while(node != null){
            if(node.key.equals((K)key)){
                return true;
            }else{
                node = node.nextNode;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        if(value == null){
            return false;
        }
        for(var node : nodes){
            while(node != null){
                if(value.equals(node.value)){
                    return true;
                }else{
                    node = node.nextNode;
                }
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        if(key == null){
            return null;
        }
        var currentNode = nodes[customHash((K) key)];
        while(currentNode != null) {
            if (currentNode.key.equals(key)) {
                return currentNode.value;
            }else{
                currentNode = currentNode.nextNode;
            }
        }
        return null;
    }

    @Override
    public synchronized V put(K key, V value) {
        if(key == null || value == null){
            return null;
        }
        var index = customHash(key);
        if(nodes[index] == null){
            nodes[index] = new MapNode<>(key, value);
            if(size++ == capacity * 2){
                resize();
            }
        }else{
            var currentNode = nodes[index];
            while(currentNode != null){
                if(currentNode.key.equals(key)){
                    return currentNode.setValue(value);
                }else{
                    if(currentNode.nextNode != null){
                        currentNode = currentNode.nextNode;
                    }else{
                        currentNode.nextNode = new MapNode(key, value);
                        if(size++ == capacity * 2){
                            resize();
                        }
                    }
                }
            }
        }
        return null;
    }
    private void resize(){
        var oldArray = nodes;
        capacity *= 1000;
        nodes = new MapNode[capacity];
        for (var node : oldArray) {
            while(node != null){
                put(node.key, node.value);
                node = node.nextNode;
            }
        }
    }

    @Override
    public synchronized V remove(Object key) {
        if(key == null){
            return null;
        }
        var index = customHash((K)key);
        var currentNode = nodes[index];
        if(currentNode == null){
            return null;
        }
        if(currentNode.key.equals((K)key)){
            if(currentNode.nextNode != null){
                var oldNode = nodes[index];
                nodes[index] = oldNode.nextNode;
                oldNode.nextNode = null;
                size--;
                return oldNode.value;
            }else{
                var value = nodes[index].value;
                nodes[index] = null;
                size--;
                return value;
            }
        }
        while(currentNode != null){
            if(currentNode.nextNode != null){
                if(currentNode.nextNode.key.equals((K)key)){
                    var oldNode = currentNode.nextNode;
                    currentNode.nextNode = oldNode.nextNode;
                    oldNode.nextNode = null;
                    return (V)oldNode.value;
                }else{
                    currentNode = currentNode.nextNode;
                }
            }else{
                return null;
            }
        }
        return null;
    }

    @Override
    public synchronized void putAll(Map<? extends K, ? extends V> m) {
        for(var node : m.entrySet()) {
            put(node.getKey(), node.getValue());
        }
    }

    @Override
    public void clear() {
        capacity = 100;
        size = 0;
        nodes = new MapNode[capacity];
    }

    @Override
    public Set<K> keySet() {
        Set<K> set = new LinkedHashSet<>();
        for(var node : nodes){
            while(node != null){
                set.add(node.key);
                node = node.nextNode;
            }
        }
        return set;
    }

    @Override
    public Collection<V> values() {
        Collection<V> values = new ArrayList<>();
        for(var node : nodes){
            while(node != null){
                values.add(node.value);
                node = node.nextNode;
            }
        }
        return values;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K,V>> set = new LinkedHashSet<>();
        for(var node : nodes){
            while(node != null){
                set.add(node);
                node = node.nextNode;
            }
        }
        return set;
    }
}
