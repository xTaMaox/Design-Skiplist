class Skiplist {

    static class Node {
        int val;
        int count; // to handle duplicates
        int h; // highest level: [0,32]
        Node[] next;
        public Node(int a, int b) {
            val = a; h = b; count = 1;
            next = new Node[33];
        }
    }

    Node sentinel = new Node(Integer.MIN_VALUE, 32);
    int topLevel = 0;
    Node[] stack = new Node[33];  // to keep track of search path
    Random rand = new Random();

    public Skiplist() {
    }

    // find the predecessor
    private Node findPred(int num) {
        Node cur = sentinel;
        for (int r = topLevel; r >= 0; r--) {
            while (cur.next[r] != null && cur.next[r].val < num)  cur = cur.next[r];
            stack[r] = cur;
        }
        return cur;
    }

    public boolean search(int target) {
        Node pred = findPred(target);
        return pred.next[0] != null && pred.next[0].val == target;
    }

    public void add(int num) {
        Node pred = findPred(num);
        if (pred.next[0] != null && pred.next[0].val == num) {
            pred.next[0].count++;
            return;
        }
        Node w = new Node(num, pickHeight());
        while (topLevel < w.h) stack[++topLevel] = sentinel;
        for (int i = 0; i <= w.h; i++) {
            w.next[i] = stack[i].next[i];
            stack[i].next[i] = w;
        }
    }

    public boolean erase(int num) {
        Node pred = findPred(num);
        if (pred.next[0] == null || pred.next[0].val != num) return false;
        boolean noNeedToRemove = --pred.next[0].count != 0;
        if (noNeedToRemove) return true;
        for (int r = topLevel; r >= 0; r--) {
            Node cur = stack[r];
            if (cur.next[r] != null && cur.next[r].val == num) cur.next[r] = cur.next[r].next[r];
            if (cur == sentinel && cur.next[r] == null) topLevel--;
        }
        return true;
    }

    // number of trailing 0s of a random Integer: [0, 32]
    private int pickHeight() {
        return Integer.numberOfTrailingZeros(rand.nextInt());
    }
}
