package by.it.group551003.stupakov.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        E value;
        Node<E> next;

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E>[] table = (Node<E>[]) new Node[16];
    private int size = 0;

    private int indexFor(Object o, int length) {
        if (o == null) {
            return 0;
        }
        return (o.hashCode() & 0x7fffffff) % length;
    }

    private boolean same(Object a, Object b) {
        if (a == null) {
            return b == null;
        }
        return a.equals(b);
    }

    private void ensureCapacity() {
        if (size < table.length * 3 / 4) {
            return;
        }
        Node<E>[] newTable = (Node<E>[]) new Node[table.length * 2];
        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            while (current != null) {
                Node<E> next = current.next;
                int index = indexFor(current.value, newTable.length);
                current.next = newTable[index];
                newTable[index] = current;
                current = next;
            }
        }
        table = newTable;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (int i = 0; i < table.length; i++) {
            Node<E> current = table[i];
            while (current != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(current.value);
                first = false;
                current = current.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        table = (Node<E>[]) new Node[16];
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E element) {
        if (contains(element)) {
            return false;
        }
        ensureCapacity();
        int index = indexFor(element, table.length);
        table[index] = new Node<>(element, table[index]);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexFor(o, table.length);
        Node<E> prev = null;
        Node<E> current = table[index];
        while (current != null) {
            if (same(current.value, o)) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        Node<E> current = table[indexFor(o, table.length)];
        while (current != null) {
            if (same(current.value, o)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }
}