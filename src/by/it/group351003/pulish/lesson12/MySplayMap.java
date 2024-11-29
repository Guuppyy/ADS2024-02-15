package by.it.group351003.pulish.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    by.it.group351003.pulish.lesson12.MySplayMap.Node Root;

    class Node {
        Integer key;
        String value;
        by.it.group351003.pulish.lesson12.MySplayMap.Node left, right, parent;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public String toString() {
        if (Root == null)
            return "{}";
        StringBuilder sb = new StringBuilder().append("{");
        inOrderTraversal(Root, sb);
        sb.replace(sb.length() - 2, sb.length(), "");
        sb.append("}");
        return sb.toString();
    }

    void inOrderTraversal(by.it.group351003.pulish.lesson12.MySplayMap.Node node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key + "=" + node.value + ", ");
            inOrderTraversal(node.right, sb);
        }
    }

    @Override
    public int size() {
        return size(Root);
    }

    int size(by.it.group351003.pulish.lesson12.MySplayMap.Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + size(node.left) + size(node.right);
    }

    @Override
    public boolean isEmpty() {
        return Root == null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(Root, value.toString());
    }

    boolean containsValue(by.it.group351003.pulish.lesson12.MySplayMap.Node node, String value) {
        if (node == null) {
            return false;
        }
        if (node.value.equals(value)) {
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public String get(Object key) {
        by.it.group351003.pulish.lesson12.MySplayMap.Node found = SearchKey((Integer) key, Root);
        if (found != null) {
            Root = splay(Root, found.key);
            return found.value;
        }
        return null;
    }

    by.it.group351003.pulish.lesson12.MySplayMap.Node SearchKey(Integer key, by.it.group351003.pulish.lesson12.MySplayMap.Node node) {
        if (node == null)
            return null;
        int comparison = key.compareTo(node.key);
        if (comparison == 0)
            return node;

        return SearchKey(key, comparison < 0 ? node.left : node.right);
    }

    @Override
    public String put(Integer key, String value) {
        if (Root == null) {
            Root = new by.it.group351003.pulish.lesson12.MySplayMap.Node(key, value);
            return null;
        }

        Root = splay(Root, key);
        int cmp = key.compareTo(Root.key);
        if (cmp == 0) {
            String oldValue = Root.value;
            Root.value = value;
            return oldValue;
        } else if (cmp < 0) {
            by.it.group351003.pulish.lesson12.MySplayMap.Node newNode = new by.it.group351003.pulish.lesson12.MySplayMap.Node(key, value);
            newNode.left = Root.left;
            newNode.right = Root;
            newNode.right.parent = newNode;
            Root.left = null;
            Root = newNode;
        } else {
            by.it.group351003.pulish.lesson12.MySplayMap.Node newNode = new by.it.group351003.pulish.lesson12.MySplayMap.Node(key, value);
            newNode.right = Root.right;
            newNode.left = Root;
            newNode.left.parent = newNode;
            Root.right = null;
            Root = newNode;
        }
        return null;
    }

    @Override
    public String remove(Object key) {
        if (Root == null) {
            return null;
        }

        Root = splay(Root, (Integer) key);
        int cmp = ((Integer) key).compareTo(Root.key);
        if (cmp != 0) {
            return null;
        }

        String removedValue = Root.value;

        if (Root.left == null) {
            Root = Root.right;
            if (Root != null) {
                Root.parent = null;
            }
        } else {
            by.it.group351003.pulish.lesson12.MySplayMap.Node newRoot = Root.right;
            newRoot = splay(newRoot, (Integer) key);
            newRoot.left = Root.left;
            newRoot.left.parent = newRoot;
            Root = newRoot;
        }

        return removedValue;
    }

    by.it.group351003.pulish.lesson12.MySplayMap.Node splay(by.it.group351003.pulish.lesson12.MySplayMap.Node node, Integer key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            if (node.left == null) {
                return node;
            }
            int cmp2 = key.compareTo(node.left.key);
            if (cmp2 < 0) {
                node.left.left = splay(node.left.left, key);
                node = rotateRight(node);
            } else if (cmp2 > 0) {
                node.left.right = splay(node.left.right, key);
                if (node.left.right != null) {
                    node.left = rotateLeft(node.left);
                }
            }
            if (node.left == null) {
                return node;
            } else {
                return rotateRight(node);
            }
        } else if (cmp > 0) {
            if (node.right == null) {
                return node;
            }
            int cmp2 = key.compareTo(node.right.key);
            if (cmp2 < 0) {
                node.right.left = splay(node.right.left, key);
                if (node.right.left != null) {
                    node.right = rotateRight(node.right);
                }
            } else if (cmp2 > 0) {
                node.right.right = splay(node.right.right, key);
                node = rotateLeft(node);
            }
            if (node.right == null) {
                return node;
            } else {
                return rotateLeft(node);
            }
        } else {
            return node;
        }
    }

    by.it.group351003.pulish.lesson12.MySplayMap.Node rotateRight(by.it.group351003.pulish.lesson12.MySplayMap.Node node) {
        by.it.group351003.pulish.lesson12.MySplayMap.Node leftChild = node.left;
        node.left = leftChild.right;
        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }
        leftChild.right = node;
        leftChild.parent = node.parent;
        node.parent = leftChild;
        return leftChild;
    }

    by.it.group351003.pulish.lesson12.MySplayMap.Node rotateLeft(by.it.group351003.pulish.lesson12.MySplayMap.Node node) {
        by.it.group351003.pulish.lesson12.MySplayMap.Node rightChild = node.right;
        node.right = rightChild.left;
        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }
        rightChild.left = node;
        rightChild.parent = node.parent;
        node.parent = rightChild;
        return rightChild;
    }

    @Override
    public void clear() {
        Root = clear(Root);
    }

    by.it.group351003.pulish.lesson12.MySplayMap.Node clear(by.it.group351003.pulish.lesson12.MySplayMap.Node node) {
        if (node == null)
            return null;
        node.left = clear(node.left);
        node.right = clear(node.right);
        return null;
    }

    @Override
    public Integer lowerKey(Integer key) {
        if (Root == null)
            return null;
        by.it.group351003.pulish.lesson12.MySplayMap.Node node = lowerKeyNode(key, Root);
        if (node != null) {
            return node.key;
        }
        return null;
    }

    by.it.group351003.pulish.lesson12.MySplayMap.Node lowerKeyNode(Integer key, by.it.group351003.pulish.lesson12.MySplayMap.Node node) {
        if (node == null)
            return null;
        int comparison = key.compareTo(node.key);
        if (comparison <= 0)
            return lowerKeyNode(key, node.left);
        by.it.group351003.pulish.lesson12.MySplayMap.Node rightResult = lowerKeyNode(key, node.right);
        if (rightResult != null)
            return rightResult;
        return node;
    }

    @Override
    public Integer floorKey(Integer key) {
        if (Root == null)
            return null;
        by.it.group351003.pulish.lesson12.MySplayMap.Node node = floorKeyNode(key, Root);
        if (node != null) {
            return node.key;
        }
        return null;
    }

    by.it.group351003.pulish.lesson12.MySplayMap.Node floorKeyNode(Integer key, by.it.group351003.pulish.lesson12.MySplayMap.Node node) {
        if (node == null)
            return null;
        int comparison = key.compareTo(node.key);
        if (comparison == 0)
            return node;
        if (comparison < 0)
            return floorKeyNode(key, node.left);
        by.it.group351003.pulish.lesson12.MySplayMap.Node rightResult = floorKeyNode(key, node.right);
        if (rightResult != null)
            return rightResult;
        return node;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (Root == null)
            return null;
        by.it.group351003.pulish.lesson12.MySplayMap.Node node = ceilingKeyNode(key, Root);
        if (node != null) {
            return node.key;
        }
        return null;
    }

    by.it.group351003.pulish.lesson12.MySplayMap.Node ceilingKeyNode(Integer key, by.it.group351003.pulish.lesson12.MySplayMap.Node node) {
        if (node == null)
            return null;
        int comparison = key.compareTo(node.key);
        if (comparison == 0)
            return node;
        if (comparison > 0)
            return ceilingKeyNode(key, node.right);
        by.it.group351003.pulish.lesson12.MySplayMap.Node leftResult = ceilingKeyNode(key, node.left);
        if (leftResult != null)
            return leftResult;
        return node;
    }

    @Override
    public Integer higherKey(Integer key) {
        if (Root == null)
            return null;
        by.it.group351003.pulish.lesson12.MySplayMap.Node node = higherKeyNode(key, Root);
        if (node != null) {
            return node.key;
        }
        return null;
    }

    by.it.group351003.pulish.lesson12.MySplayMap.Node higherKeyNode(Integer key, by.it.group351003.pulish.lesson12.MySplayMap.Node node) {
        if (node == null)
            return null;
        int comparison = key.compareTo(node.key);
        if (comparison >= 0)
            return higherKeyNode(key, node.right);
        by.it.group351003.pulish.lesson12.MySplayMap.Node leftResult = higherKeyNode(key, node.left);
        if (leftResult != null)
            return leftResult;
        return node;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        by.it.group351003.pulish.lesson12.MySplayMap subMap = new by.it.group351003.pulish.lesson12.MySplayMap();
        headMap(Root, toKey, subMap);
        return subMap;
    }

    void headMap(by.it.group351003.pulish.lesson12.MySplayMap.Node node, Integer toKey, by.it.group351003.pulish.lesson12.MySplayMap subMap) {
        if (node == null) {
            return;
        }

        if (node.key.compareTo(toKey) < 0) {
            subMap.put(node.key, node.value);
            headMap(node.right, toKey, subMap);
        }

        headMap(node.left, toKey, subMap);
    }


    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        by.it.group351003.pulish.lesson12.MySplayMap subMap = new by.it.group351003.pulish.lesson12.MySplayMap();
        tailMap(Root, fromKey, subMap);
        return subMap;
    }

    void tailMap(by.it.group351003.pulish.lesson12.MySplayMap.Node node, Integer fromKey, by.it.group351003.pulish.lesson12.MySplayMap subMap) {
        if (node == null) {
            return;
        }

        if (node.key.compareTo(fromKey) >= 0) {
            subMap.put(node.key, node.value);
            tailMap(node.left, fromKey, subMap);
        }

        tailMap(node.right, fromKey, subMap);
    }

    @Override
    public Integer firstKey() {
        if (Root == null)
            return null;
        by.it.group351003.pulish.lesson12.MySplayMap.Node node = Root;
        while (node.left != null) {
            node = node.left;
        }
        return node.key;
    }

    @Override
    public Integer lastKey() {
        if (Root == null)
            return null;
        by.it.group351003.pulish.lesson12.MySplayMap.Node node = Root;
        while (node.right != null) {
            node = node.right;
        }
        return node.key;
    }

    ///////////////////////////////////////////////////////////////

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
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
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

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        return null;
    }
}
