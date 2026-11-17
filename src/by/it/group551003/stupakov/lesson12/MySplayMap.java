package by.it.group551003.stupakov.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        int key;
        String value;
        Node left;
        Node right;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root = null;
    private int size = 0;

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        return x;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        return x;
    }

    private Node splay(Node h, int key) {
        if (h == null) {
            return null;
        }
        if (key < h.key) {
            if (h.left == null) {
                return h;
            }
            if (key < h.left.key) {
                h.left.left = splay(h.left.left, key);
                h = rotateRight(h);
            } else if (key > h.left.key) {
                h.left.right = splay(h.left.right, key);
                if (h.left.right != null) {
                    h.left = rotateLeft(h.left);
                }
            }
            if (h.left == null) {
                return h;
            }
            return rotateRight(h);
        } else if (key > h.key) {
            if (h.right == null) {
                return h;
            }
            if (key < h.right.key) {
                h.right.left = splay(h.right.left, key);
                if (h.right.left != null) {
                    h.right = rotateRight(h.right);
                }
            } else if (key > h.right.key) {
                h.right.right = splay(h.right.right, key);
                h = rotateLeft(h);
            }
            if (h.right == null) {
                return h;
            }
            return rotateLeft(h);
        }
        return h;
    }

    private boolean splayFind(int key) {
        if (root == null) {
            return false;
        }
        root = splay(root, key);
        return root.key == key;
    }

    private Node lowerNode(int key, boolean inclusive) {
        Node current = root;
        Node result = null;
        while (current != null) {
            if (current.key < key || (inclusive && current.key == key)) {
                result = current;
                current = current.right;
            } else {
                current = current.left;
            }
        }
        return result;
    }

    private Node higherNode(int key, boolean inclusive) {
        Node current = root;
        Node result = null;
        while (current != null) {
            if (current.key > key || (inclusive && current.key == key)) {
                result = current;
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return result;
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

    private void fill(Node node, int bound, boolean head, MySplayMap result) {
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
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }
        root = splay(root, key);
        if (key < root.key) {
            Node node = new Node(key, value);
            node.left = root.left;
            node.right = root;
            root.left = null;
            root = node;
            size++;
            return null;
        }
        if (key > root.key) {
            Node node = new Node(key, value);
            node.right = root.right;
            node.left = root;
            root.right = null;
            root = node;
            size++;
            return null;
        }
        String old = root.value;
        root.value = value;
        return old;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        int k = (Integer) key;
        if (!splayFind(k)) {
            return null;
        }
        String old = root.value;
        if (root.left == null) {
            root = root.right;
        } else {
            Node right = root.right;
            root = splay(root.left, k);
            root.right = right;
        }
        size--;
        return old;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        if (!splayFind((Integer) key)) {
            return null;
        }
        return root.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }
        return splayFind((Integer) key);
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
        MySplayMap result = new MySplayMap();
        fill(root, toKey, true, result);
        return result;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap result = new MySplayMap();
        fill(root, fromKey, false, result);
        return result;
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            return null;
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
            return null;
        }
        Node current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node node = lowerNode(key, false);
        if (node == null) {
            return null;
        }
        return node.key;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node node = lowerNode(key, true);
        if (node == null) {
            return null;
        }
        return node.key;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node node = higherNode(key, true);
        if (node == null) {
            return null;
        }
        return node.key;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node node = higherNode(key, false);
        if (node == null) {
            return null;
        }
        return node.key;
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        return null;
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        return null;
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    @Override
    public Comparator<? super Integer> comparator() {
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