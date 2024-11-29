package by.it.group351003.pulish.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    by.it.group351003.pulish.lesson12.MyRbMap.Node Root;

    enum Color {
        RED, BLACK
    }

    class Node {
        Integer key;
        String value;
        by.it.group351003.pulish.lesson12.MyRbMap.Node parent, left, right;
        by.it.group351003.pulish.lesson12.MyRbMap.Color color;
        public Node(by.it.group351003.pulish.lesson12.MyRbMap.Color color, Integer key, String value) {
            this.color = color;
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

    private void inOrderTraversal(by.it.group351003.pulish.lesson12.MyRbMap.Node node, StringBuilder sb) {
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

    int size(by.it.group351003.pulish.lesson12.MyRbMap.Node node) {
        if (node == null) {
            return 0;
        }
        return size(node.left) + size(node.right) + 1;
    }

    @Override
    public boolean isEmpty() {
        return Root == null;
    }

    @Override
    public boolean containsKey(Object key) {
        return SearchKey((Integer) key, Root) != null;
    }

    by.it.group351003.pulish.lesson12.MyRbMap.Node SearchKey(Integer key, by.it.group351003.pulish.lesson12.MyRbMap.Node node) {
        if (node == null)
            return null;
        int comparison = key.compareTo(node.key);
        if (comparison == 0)
            return node;

        return SearchKey(key, comparison < 0 ? node.left : node.right);
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(Root, value);
    }

    boolean containsValue(by.it.group351003.pulish.lesson12.MyRbMap.Node node, Object value) {
        if (node == null) {
            return false;
        }
        if (value.equals(node.value)) {
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public String get(Object key) {
        by.it.group351003.pulish.lesson12.MyRbMap.Node node = SearchKey((Integer) key, Root);
        return (node != null) ? node.value : null;
    }

    @Override
    public String put(Integer key, String value) {
        if (Root == null) {
            Root = new by.it.group351003.pulish.lesson12.MyRbMap.Node(by.it.group351003.pulish.lesson12.MyRbMap.Color.BLACK, key, value);
        } else {
            by.it.group351003.pulish.lesson12.MyRbMap.Node newNode = new by.it.group351003.pulish.lesson12.MyRbMap.Node(by.it.group351003.pulish.lesson12.MyRbMap.Color.RED, key, value);
            by.it.group351003.pulish.lesson12.MyRbMap.Node current = Root;
            while (true) {
                newNode.parent = current;
                if (key.compareTo(current.key) < 0) {
                    if (current.left == null) {
                        current.left = newNode;
                        break;
                    } else {
                        current = current.left;
                    }
                } else if (key.compareTo(current.key) > 0) {
                    if (current.right == null) {
                        current.right = newNode;
                        break;
                    } else {
                        current = current.right;
                    }
                } else {
                    String oldValue = current.value;
                    current.value = value;
                    return oldValue;
                }
            }

            balanceAfterInsert(newNode);
        }
        return null;
    }

    void balanceAfterInsert(by.it.group351003.pulish.lesson12.MyRbMap.Node node) {
        while (node != Root && node.color == by.it.group351003.pulish.lesson12.MyRbMap.Color.RED && node.parent.color == by.it.group351003.pulish.lesson12.MyRbMap.Color.RED) {
            by.it.group351003.pulish.lesson12.MyRbMap.Node parent = node.parent;
            by.it.group351003.pulish.lesson12.MyRbMap.Node grandparent = parent.parent;

            if (parent == grandparent.left) {
                by.it.group351003.pulish.lesson12.MyRbMap.Node uncle = grandparent.right;
                if (uncle != null && uncle.color == by.it.group351003.pulish.lesson12.MyRbMap.Color.RED) {
                    parent.color = by.it.group351003.pulish.lesson12.MyRbMap.Color.BLACK;
                    uncle.color = by.it.group351003.pulish.lesson12.MyRbMap.Color.BLACK;
                    grandparent.color = by.it.group351003.pulish.lesson12.MyRbMap.Color.RED;
                    node = grandparent;
                } else {
                    if (node == parent.right) {
                        node = parent;
                        RotateLeft(node);
                    }
                    parent.color = by.it.group351003.pulish.lesson12.MyRbMap.Color.BLACK;
                    grandparent.color = by.it.group351003.pulish.lesson12.MyRbMap.Color.RED;
                    RotateRight(grandparent);
                }
            } else {
                by.it.group351003.pulish.lesson12.MyRbMap.Node uncle = grandparent.left;
                if (uncle != null && uncle.color == by.it.group351003.pulish.lesson12.MyRbMap.Color.RED) {
                    parent.color = by.it.group351003.pulish.lesson12.MyRbMap.Color.BLACK;
                    uncle.color = by.it.group351003.pulish.lesson12.MyRbMap.Color.BLACK;
                    grandparent.color = by.it.group351003.pulish.lesson12.MyRbMap.Color.RED;
                    node = grandparent;
                } else {
                    if (node == parent.left) {
                        node = parent;
                        RotateRight(node);
                    }
                    parent.color = by.it.group351003.pulish.lesson12.MyRbMap.Color.BLACK;
                    grandparent.color = by.it.group351003.pulish.lesson12.MyRbMap.Color.RED;
                    RotateLeft(grandparent);
                }
            }
        }

        Root.color = by.it.group351003.pulish.lesson12.MyRbMap.Color.BLACK;
    }

    by.it.group351003.pulish.lesson12.MyRbMap.Node getSuccessor(by.it.group351003.pulish.lesson12.MyRbMap.Node node) {
        by.it.group351003.pulish.lesson12.MyRbMap.Node successor = null;
        by.it.group351003.pulish.lesson12.MyRbMap.Node current = node.right;

        while (current != null) {
            successor = current;
            current = current.left;
        }

        return successor;
    }

    void deleteNode(by.it.group351003.pulish.lesson12.MyRbMap.Node node) {
        by.it.group351003.pulish.lesson12.MyRbMap.Node replacement;
        if (node.left != null && node.right != null) {
            by.it.group351003.pulish.lesson12.MyRbMap.Node successor = getSuccessor(node);
            node.key = successor.key;
            node.value = successor.value;
            node = successor;
        }

        replacement = (node.left != null) ? node.left : node.right;

        if (replacement != null) {
            replacement.parent = node.parent;
            if (node.parent == null) {
                Root = replacement;
            } else if (node == node.parent.left) {
                node.parent.left = replacement;
            } else {
                node.parent.right = replacement;
            }

            if (node.color == by.it.group351003.pulish.lesson12.MyRbMap.Color.BLACK) {
                balanceDeletion(replacement);
            }
        } else if (node.parent == null) {
            Root = null;
        } else {
            if (node.color == by.it.group351003.pulish.lesson12.MyRbMap.Color.BLACK) {
                balanceDeletion(node);
            }

            if (node.parent != null) {
                if (node == node.parent.left) {
                    node.parent.left = null;
                } else if (node == node.parent.right) {
                    node.parent.right = null;
                }
                node.parent = null;
            }
        }
    }


    by.it.group351003.pulish.lesson12.MyRbMap.Color getColor(by.it.group351003.pulish.lesson12.MyRbMap.Node node) {
        return (node == null) ? by.it.group351003.pulish.lesson12.MyRbMap.Color.BLACK : node.color;
    }

    void setColor(by.it.group351003.pulish.lesson12.MyRbMap.Node node, by.it.group351003.pulish.lesson12.MyRbMap.Color color) {
        if (node != null) {
            node.color = color;
        }
    }

    void RotateLeft(by.it.group351003.pulish.lesson12.MyRbMap.Node node)
    {
        by.it.group351003.pulish.lesson12.MyRbMap.Node right = node.right;
        node.right = right.left;
        if (right.left != null)
            right.left.parent = node;
        right.parent = node.parent;
        if (node.parent == null)
            Root = right;
        else if (node == node.parent.left)
            node.parent.left = right;
        else
            node.parent.right = right;
        right.left = node;
        node.parent = right;
    }

    void RotateRight(by.it.group351003.pulish.lesson12.MyRbMap.Node node)
    {
        by.it.group351003.pulish.lesson12.MyRbMap.Node left = node.left;
        node.left = left.right;
        if (left.right != null)
            left.right.parent = node;
        left.parent = node.parent;
        if (node.parent == null)
            Root = left;
        else if (node == node.parent.right)
            node.parent.right = left;
        else
            node.parent.left = left;
        left.right = node;
        node.parent = left;
    }

    void balanceDeletion(by.it.group351003.pulish.lesson12.MyRbMap.Node node) {
        while (node != Root && getColor(node) == by.it.group351003.pulish.lesson12.MyRbMap.Color.BLACK) {
            if (node == node.parent.left) {
                by.it.group351003.pulish.lesson12.MyRbMap.Node sibling = node.parent.right;
                if (getColor(sibling) == by.it.group351003.pulish.lesson12.MyRbMap.Color.RED) {
                    setColor(sibling, by.it.group351003.pulish.lesson12.MyRbMap.Color.BLACK);
                    setColor(node.parent, by.it.group351003.pulish.lesson12.MyRbMap.Color.RED);
                    RotateLeft(node.parent);
                    sibling = node.parent.right;
                }
                if (getColor(sibling.left) == by.it.group351003.pulish.lesson12.MyRbMap.Color.BLACK && getColor(sibling.right) == by.it.group351003.pulish.lesson12.MyRbMap.Color.BLACK) {
                    setColor(sibling, by.it.group351003.pulish.lesson12.MyRbMap.Color.RED);
                    node = node.parent;
                } else {
                    if (getColor(sibling.right) == by.it.group351003.pulish.lesson12.MyRbMap.Color.BLACK) {
                        setColor(sibling.left, by.it.group351003.pulish.lesson12.MyRbMap.Color.BLACK);
                        setColor(sibling, by.it.group351003.pulish.lesson12.MyRbMap.Color.RED);
                        RotateRight(sibling);
                        sibling = node.parent.right;
                    }
                    setColor(sibling, getColor(node.parent));
                    setColor(node.parent, by.it.group351003.pulish.lesson12.MyRbMap.Color.BLACK);
                    setColor(sibling.right, by.it.group351003.pulish.lesson12.MyRbMap.Color.BLACK);
                    RotateLeft(node.parent);
                    node = Root;
                }
            } else {
                by.it.group351003.pulish.lesson12.MyRbMap.Node sibling = node.parent.left;
                if (getColor(sibling) == by.it.group351003.pulish.lesson12.MyRbMap.Color.RED) {
                    setColor(sibling, by.it.group351003.pulish.lesson12.MyRbMap.Color.BLACK);
                    setColor(node.parent, by.it.group351003.pulish.lesson12.MyRbMap.Color.RED);
                    RotateRight(node.parent);
                    sibling = node.parent.left;
                }
                if (getColor(sibling.right) == by.it.group351003.pulish.lesson12.MyRbMap.Color.BLACK && getColor(sibling.left) == by.it.group351003.pulish.lesson12.MyRbMap.Color.BLACK) {
                    setColor(sibling, by.it.group351003.pulish.lesson12.MyRbMap.Color.RED);
                    node = node.parent;
                } else {
                    if (getColor(sibling.left) == by.it.group351003.pulish.lesson12.MyRbMap.Color.BLACK) {
                        setColor(sibling.right, by.it.group351003.pulish.lesson12.MyRbMap.Color.BLACK);
                        setColor(sibling, by.it.group351003.pulish.lesson12.MyRbMap.Color.RED);
                        RotateLeft(sibling);
                        sibling = node.parent.left;
                    }
                    setColor(sibling, getColor(node.parent));
                    setColor(node.parent, by.it.group351003.pulish.lesson12.MyRbMap.Color.BLACK);
                    setColor(sibling.left, by.it.group351003.pulish.lesson12.MyRbMap.Color.BLACK);
                    RotateRight(node.parent);
                    node = Root;
                }
            }
        }

        setColor(node, by.it.group351003.pulish.lesson12.MyRbMap.Color.BLACK);
    }

    @Override
    public String remove(Object key) {
        by.it.group351003.pulish.lesson12.MyRbMap.Node node = SearchKey((Integer) key, Root);
        if (node != null) {
            String oldValue = node.value;
            deleteNode(node);
            return oldValue;
        }
        return null;
    }

    @Override
    public void clear() {
        Root = clear(Root);
    }

    by.it.group351003.pulish.lesson12.MyRbMap.Node clear(by.it.group351003.pulish.lesson12.MyRbMap.Node node) {
        if (node == null)
            return null;
        node.left = clear(node.left);
        node.right = clear(node.right);
        return null;
    }

    @Override
    public Integer firstKey() {
        by.it.group351003.pulish.lesson12.MyRbMap.Node first = firstNode(Root);
        return (first != null) ? first.key : null;
    }

    by.it.group351003.pulish.lesson12.MyRbMap.Node firstNode(by.it.group351003.pulish.lesson12.MyRbMap.Node node) {
        while (node != null && node.left != null) {
            node = node.left;
        }
        return node;
    }

    @Override
    public Integer lastKey() {
        by.it.group351003.pulish.lesson12.MyRbMap.Node last = lastNode(Root);
        return (last != null) ? last.key : null;
    }

    by.it.group351003.pulish.lesson12.MyRbMap.Node lastNode(by.it.group351003.pulish.lesson12.MyRbMap.Node node) {
        while (node != null && node.right != null) {
            node = node.right;
        }
        return node;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        by.it.group351003.pulish.lesson12.MyRbMap subMap = new by.it.group351003.pulish.lesson12.MyRbMap();
        headMap(Root, toKey, subMap);
        return subMap;
    }

    void headMap(by.it.group351003.pulish.lesson12.MyRbMap.Node node, Integer toKey, by.it.group351003.pulish.lesson12.MyRbMap subMap) {
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
        by.it.group351003.pulish.lesson12.MyRbMap subMap = new by.it.group351003.pulish.lesson12.MyRbMap();
        tailMap(Root, fromKey, subMap);
        return subMap;
    }

    void tailMap(by.it.group351003.pulish.lesson12.MyRbMap.Node node, Integer fromKey, by.it.group351003.pulish.lesson12.MyRbMap subMap) {
        if (node == null) {
            return;
        }

        if (node.key.compareTo(fromKey) >= 0) {
            subMap.put(node.key, node.value);
            tailMap(node.left, fromKey, subMap);
        }

        tailMap(node.right, fromKey, subMap);
    }


    ///////////////////////

    @Override
    public Set<Integer> keySet() {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

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
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }
}
