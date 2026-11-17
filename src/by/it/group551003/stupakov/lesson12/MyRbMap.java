package by.it.group551003.stupakov.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {

    private static class Node {
        int key;
        String value;
        boolean red;
        Node left;
        Node right;

        Node(int key, String value, boolean red) {
            this.key = key;
            this.value = value;
            this.red = red;
        }
    }

    private Node root = null;
    private int size = 0;
    private String oldValue = null;

    private boolean isRed(Node node) {
        if (node == null) {
            return false;
        }
        return node.red;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.red = h.red;
        h.red = true;
        return x;
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.red = h.red;
        h.red = true;
        return x;
    }

    private void flipColors(Node h) {
        h.red = !h.red;
        h.left.red = !h.left.red;
        h.right.red = !h.right.red;
    }

    private Node balance(Node h) {
        if (isRed(h.right) && !isRed(h.left)) {
            h = rotateLeft(h);
        }
        if (isRed(h.left) && isRed(h.left.left)) {
            h = rotateRight(h);
        }
        if (isRed(h.left) && isRed(h.right)) {
            flipColors(h);
        }
        return h;
    }

    private Node moveRedLeft(Node h) {
        flipColors(h);
        if (isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    private Node moveRedRight(Node h) {
        flipColors(h);
        if (isRed(h.left.left)) {
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    private Node insert(Node h, int key, String value) {
        if (h == null) {
            size++;
            return new Node(key, value, true);
        }
        if (key < h.key) {
            h.left = insert(h.left, key, value);
        } else if (key > h.key) {
            h.right = insert(h.right, key, value);
        } else {
            oldValue = h.value;
            h.value = value;
        }
        return balance(h);
    }

    private Node deleteMin(Node h) {
        if (h.left == null) {
            return null;
        }
        if (!isRed(h.left) && !isRed(h.left.left)) {
            h = moveRedLeft(h);
        }
        h.left = deleteMin(h.left);
        return balance(h);
    }

    private Node delete(Node h, int key) {
        if (key < h.key) {
            if (!isRed(h.left) && !isRed(h.left.left)) {
                h = moveRedLeft(h);
            }
            h.left = delete(h.left, key);
        } else {
            if (isRed(h.left)) {
                h = rotateRight(h);
            }
            if (key == h.key && h.right == null) {
                return null;
            }
            if (!isRed(h.right) && !isRed(h.right.left)) {
                h = moveRedRight(h);
            }
            if (key == h.key) {
                Node min = h.right;
                while (min.left != null) {
                    min = min.left;
                }
                h.key = min.key;
                h.value = min.value;
                h.right = deleteMin(h.right);
            } else {
                h.right = delete(h.right, key);
            }
        }
        return balance(h);
    }

    private Node find(int key) {
        Node current = root;
        while (current != null) {
            if (key < current.key) {
                current = current.left;
            } else if (key > current.key) {
                current = current.right;
            } else {
                return current;
            }
        }
        return null;
    }

    private void print(Node node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        print(node.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(node.key).append("=").append(node.value);
        print(node.right, sb);
    }

    private boolean hasValue(Node node, Object value) {
        if (node == null) {
            return false;
        }
        if (hasValue(node.left, value)) {
            return true;
        }
        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }
        return hasValue(node.right, value);
    }

    private void fill(Node node, int bound, boolean head, MyRbMap result) {
        if (node == null) {
            return;
        }
        fill(node.left, bound, head, result);
        if (head ? node.key < bound : node.key >= bound) {
            result.put(node.key, node.value);
        }
        fill(node.right, bound, head, result);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        print(root, sb);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String put(Integer key, String value) {
        oldValue = null;
        root = insert(root, key, value);
        root.red = false;
        return oldValue;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Node node = find((Integer) key);
        if (node == null) {
            return null;
        }
        String result = node.value;
        if (!isRed(root.left) && !isRed(root.right)) {
            root.red = true;
        }
        root = delete(root, (Integer) key);
        if (root != null) {
            root.red = false;
        }
        size--;
        return result;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Node node = find((Integer) key);
        if (node == null) {
            return null;
        }
        return node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }
        return find((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return hasValue(root, value);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        fill(root, toKey, true, result);
        return result;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        fill(root, fromKey, false, result);
        return result;
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.key;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
    }

    @Override
    public Set<Integer> keySet() {
        return null;
    }

    @Override
    public Collection<String> values() {
        return null;
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return null;
    }
}