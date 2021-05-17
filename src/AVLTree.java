import java.lang.Math;

public class AVLTree {

    static int rotations = 0;   // The total number of rotations performed
    static int comparisons = 0; // The total number of comparisons performed

    int height;     // The height of the tree/subtree

    AVLTree left;   // The left subtree
    AVLTree right;  // The right subtree
    Integer key;    // The key value of the node


    /**
     * Creates an empty node with no key value.
     */
    public AVLTree() {
        this.left = null;
        this.right = null;
        this.key = null;

        this.height = -1;
    }

    /**
     * Creates an initialised node with a given key value.
     * @param key The key value of the node.
     */
    public AVLTree(int key) {
        this.left = new AVLTree();
        this.right = new AVLTree();
        this.key = key;

        this.height = 0;
    }


    //Binary search Tree implementation


    /**
     * Search the tree for a given key value.
     * @param n The key value to search for.
     * @return The node with the key value or null if the value is not in the tree.
     */
    public AVLTree search(int n) {

        if (key == null) {
            // If this node has no value,
            // then the entire tree has been searched and the value was not found
            return null;
        }

        else if(compare('=', n, key)) {
            // This is the requested node
            return this;
        }

        else if (compare('<', n, key)) {
            // The key is in the left subtree
            return left.search(n);
        }

        else {
            // The key is in the right subtree
            return right.search(n);
        }

    }


    /**
     * Insert a new key value into the tree.
     * @param n The value to be inserted.
     */
    public void insert(int n) {

        if (key == null) {
            //This node has no value, add the key here
            this.key = n;
            this.height = 0;
            this.left = new AVLTree();
            this.right = new AVLTree();
        }

        else if (compare('<', n, key)){
            //Add the key to left subtree
            left.insert(n);
        }

        else {
            //Add the key to right subtree
            right.insert(n);
        }

        //Balance the tree
        balance();
    }


    /**
     * Remove a key from the tree.
     * @param n The key to be removed.
     */
    public void delete(int n) {

        if (key == null) {
            //n is not in the tree
            return;
        }

        if (compare('=', key, n)) {
            //This is the key to be removed

            if (left.key != null) {
                //Replace it with a key from the left subtree
                int max = left.findMax();
                this.key = max;
                left.delete(max);
            }

            else if (right.key != null) {
                //Replace it with a key from the right subtree
                int min = right.findMin();
                this.key = min;
                right.delete(min);
            }

            else {
                //This is a leaf node, just remove it
                this.key = null;
                this.height = -1;
                this.left = null;
                this.right = null;
            }

        }

        else if (compare('<', key, n)) {
            // The required key is in the right subtree
            right.delete(n);
        }

        else {
            // The required key is in the left subtree
            left.delete(n);
        }

        // Balance the tree
        balance();
    }

    /**
     * Find the largest key value in the tree.
     */
    public int findMax() {

        // Find the rightmost value
        if (right.key != null) {
            return right.findMax();
        }
        else {
            return key;
        }

    }

    /**
     * Find the least value in the tree.
     */
    public int findMin() {

        // Find the leftmost value.
        if (left.key != null) {
            return left.findMin();
        }
        else {
            return key;
        }

    }


    //AVL Tree Methods

    /**
     * Carries out any rotations necessary to balance the tree.
     */
    private void balance() {

        // Update the height of the node
        adjustHeight();

        // Find the difference in heights of the subtrees
        int bf = balanceFactor();

        // If the balance factor is greater than 1, we need to balance the tree
        if (compare('>', Math.abs(bf), 1)) {

            if (compare('>', bf, 0)) {
                // The left subtree is taller

                // Check whether we need an inner or outer rotation
                if (compare('>', left.balanceFactor(), 0)) {
                    llRotation();
                }
                else {
                    lrRotation();
                }

            }
            else {
                // The right subtree is taller

                // Check whether we need an inner or outer rotation
                if (compare('<', right.balanceFactor(), 0)) {
                    rrRotation();
                }
                else {
                    rlRotation();
                }

            }

        }

    }

    /**
     * Check whether a tree is balanced.
     * @return The difference in the heights of the subtrees. Return 0 if it is a terminal node.
     */
    public int balanceFactor() {
        if (key == null) { //This node has no value, it is a terminal node
            return 0;
        }
        else {
            return left.height - right.height;
        }
    }

    /**
     * Update the height value of the tree after an insertion
     * @return The height of the tree.
     */
    public int adjustHeight() {

        if (key == null) { //This is a terminal node
            this.height = -1;
        }

        else { // Increment the height
            this.height = Math.max(left.height, right.height) + 1;
        }

        return this.height;
    }

    //Rotations

    /**
     * Perform a left single rotation
     */
    public void llRotation() {

        rotations++;

        // Rotate the nodes
        AVLTree tmpTree = right;
        right = left;
        left = right.left;
        right.left = right.right;
        right.right = tmpTree;

        // Swap the key values
        int tmpInt = key;
        key = right.key;
        right.key = tmpInt;

        // Update the heights of the rotated nodes
        right.adjustHeight();
        adjustHeight();
    }

    /**
     * Perform a left double rotation
     */
    public void lrRotation() {
        left.rrRotation();
        llRotation();
    }

    /**
     * Perform a right single rotation
     */
    public void rrRotation() {

        rotations++;

        // Rotate the nodes
        AVLTree tmpTree = left;
        left = right;
        right = left.right;
        left.right = left.left;
        left.left = tmpTree;

        // Swap the key values
        int tmpInt = key;
        key = left.key;
        left.key = tmpInt;

        // Update the heights of the rotated nodes
        left.adjustHeight();
        adjustHeight();

    }

    /**
     * Perform a right double rotation
     */
    public void rlRotation() {
        right.llRotation();
        rrRotation();
    }


    /**
     * Counts the number of nodes in the tree.
     * @return The number of nodes in the tree.
     */
    public int countNodes() {
        if (key == null) {
            return 0;
        }
        else {
            return left.countNodes() + right.countNodes() + 1;
        }
    }



    /**
     * Used instead of <, >, and == operators, counting the number of times it was called.
     * @return boolean value
     */
    private static boolean compare(char op, int n1, int n2) {
        comparisons++;

        switch (op) {
            case '<':
                if (n1 < n2) {
                    return true;
                }
                break;

            case '>':
                if (n1 > n2) {
                    return true;
                }
                break;

            case '=':
                if (n1 == n2) {
                    return true;
                }
        }

        return false;
    }


    /**
     * Reset comparisons and rotations counters to zero.
     */
    public static void resetCounters(){
        rotations = 0;
        comparisons = 0;
    }


    /**
     * Print out the contents of the tree.
     * Prints "(key, left.key, right.key)" for each node.
     * Used for debugging purposes.
     */
    public void print(){
        System.out.print("(" + key + ", ");

        if (left.key == null) {
            System.out.print("-, ");
        }
        else {
            System.out.print(left.key + ", ");
        }

        if (right.key == null) {
            System.out.println("-)");
        }
        else {
            System.out.println(right.key + ")");
        }

        if (left.key != null) {
            left.print();
        }
        if (right.key != null) {
            right.print();
        }

    }

}
