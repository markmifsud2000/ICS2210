import javax.swing.plaf.InsetsUIResource;
import javax.swing.tree.AbstractLayoutCache;

public class RBTree {

    static int rotations = 0;       // Total number of rotations performed
    static int comparisons = 0;     // Total number of comparisons performed

    Node root;      // The root of the tree


    /**
     * Constructs an empty tree with no values.
     */
    public RBTree() {
        this.root = null;
    }


    /*
     * Represents a node in the tree. Each node has a key value and is coloured Red or Black.
     */
    public class Node {

        Node left;      // Left subtree
        Node right;     // Right subtree
        Node parent;    // The parent node

        Integer key;    // Value stored
        boolean isRed;  // True when the node is red, false when the node is black


        /**
         * Creates an empty node with no value stored.
         */
        public Node () {
            this.left = null;
            this.right = null;
            this.parent = null;
            this.key = null;
            this.isRed = true;
        }

        /**
         * Creates a red node with a given value.
         * @param key The Value stored.
         */
        public Node (int key) {
            this.left = null;
            this.right = null;
            this.parent = null;
            this.key = key;
            this.isRed = true;
        }


        /**
         * Recursively print out the contents of the node.
         * Prints "(Key, R/B, LeftKey, RightKey)" for each node.
         * Used for debugging purposes.
         */
        public void print(){
            System.out.print("(" + key + ", " + (isRed ? "R":"B") + ", ");

            if (left == null) {
                System.out.print("-, ");
            }
            else {
                System.out.print(left.key + ", ");
            }

            if (right == null) {
                System.out.print("-)");
            }
            else {
                System.out.print(right.key + ")");
            }

            if (parent == null) {
                System.out.println(" (root)");
            }
            else {
                System.out.println(" (" + parent.key + ")");
            }

            if (left != null) {
                left.print();
            }
            if (right != null) {
                right.print();
            }

        }


        /**
         * Calculates the height of the node.
         * @return The height of the node.
         */
        public int height() {

            int rHeight = 0;
            int lHeight = 0;

            if (left == null && right == null) {
                return 0;
            }

            if (left != null){
                lHeight = left.height();
            }

            if (right != null){
                rHeight = right.height();
            }

            return (rHeight > lHeight) ? (rHeight+1) : (lHeight+1);
        }

        /**
         * Counts the number of nodes in the tree
         * @return The number of nodes in the tree
         */
        public int countNodes() {
            int lCount = 0;
            int rCount = 0;

            if (left != null) {
                lCount += left.countNodes();
            }

            if (right != null) {
                rCount += right.countNodes();
            }

            return 1 + Math.max(lCount, rCount);
        }

    }



    /**
     * Search the tree for a given key value.
     * @param n The key value to search for.
     * @return The node with the key value or null if the value is not in the tree.
     */
    public Node search(int n) {

        Node x = root;

        while (x != null) {

            if (compare('=', n, x.key)) {
                // This is the requested node
                return x;
            }
            else if (compare('<', n, x.key)) {
                // The key is in the left subtree
                x = x.left;
            }
            else {
                // The key is in the right subtree
                x = x.right;
            }

        }

        // The key was not found
        return null;

    }


    /**
     * Insert a new key value into the tree.
     * @param n The value to be inserted.
     */
    public void insert(int n) {

        // Check if the root node has been created yet
        if (root == null) {
            //Create a black root node
            root = new Node(n);
            root.isRed = false;
            return;
        }


        // Begin insertion

        Node x = root; //Current node

        // Loop until we have found the right position
        while (x != null) {

            if (compare('<', n, x.key)) {
                //insert n in the left subtree

                if (x.left == null) {
                    //Insert n here, as the left child
                    x.left = new Node(n);
                    x.left.parent = x;

                    //If the current node is black, we can just insert n and stop
                    //If the node is red, we must perform rotations

                    if (x.isRed) {
                        //The current node is red & we know the uncle cannot also be red

                        if (x == x.parent.left) {
                            //The current node is a left child
                            //We are inserting n on the left
                            llRotation(x.parent);

                        }
                        else {
                            //The current node is a right child
                            //We are inserting n on the left
                            rlRotation(x.parent);
                        }

                        // Update node colours after rotations
                        x.isRed = false;
                        if (x.parent != null) {
                            x.parent.isRed = true;
                        }

                    }


                    x = null; // Stop Looping

                }
                else {
                    //Continue searching

                    //Check if we have 2 red children that need to be recoloured
                    checkFor2RedChildren(x);


                    //Continue in the left subtree
                    x = x.left;
                }

            }

            else {
                //insert n in the right subtree

                if (x.right == null) {
                    //Insert n as the right child
                    x.right = new Node(n);
                    x.right.parent = x;

                    //If the current node is black, we can just insert n and stop
                    //If the node is red, we must perform rotations

                    if (x.isRed)  {
                        //The current node is red & we know the uncle cannot also be red

                        if (x == x.parent.right) {
                            //The current node is a right child
                            //We are inserting n on the right
                            rrRotation(x.parent);

                        }
                        else {
                            //The current node is a left child
                            //We are inserting n on the right
                            lrRotation(x.parent);
                        }

                        //Update colours after rotations
                        x.isRed = false;
                        if (x.parent != null) {
                            x.parent.isRed = true;
                        }

                    }

                    x = null; // Stop Looping
                }
                else {
                    //Continue searching

                    //Check if we have 2 red children that need to be recoloured
                    checkFor2RedChildren(x);


                    //Continue in the right subtree
                    x = x.right;
                }

            }

        }


        //The root node must always be black
        root.isRed = false;
    }


    /**
     * When carrying out a top-down insertion, check if the next 2 children are both red.
     * If both children are red, change their colours and perform any necessary rotations.
     * @param x The current node.
     */
    private void checkFor2RedChildren(Node x) {

        if (x.left != null && x.right != null) {
            if (x.left.isRed && x.right.isRed) {
                //Both children are red, swap their colours
                x.isRed = true;
                x.left.isRed = false;
                x.right.isRed = false;

                //Check for red-red violations
                if ((x.parent != null) && (x.parent.isRed)) {
                    //We have a violation, perform the necessary rotation

                    if (x.parent == x.parent.parent.left) {
                        // The parent is left of the grandparent

                        if (x == x.parent.left) {
                            // The current node is also left
                            llRotation(x.parent.parent);
                        }
                        else {
                            // The current node is right
                            lrRotation(x.parent.parent);
                        }

                    }
                    else {
                        // The parent is right of the grandparent

                        if (x == x.parent.left) {
                            // The current node is left
                            rlRotation(x.parent.parent);
                        }
                        else {
                            // The current node is also right
                            rrRotation(x.parent.parent);
                        }

                    }

                }

            }

        }


    }


    /**
     * Delete a node from the tree.
     * @param n The key value of the node to be deleted.
     */
    public void delete(int n) {

        //check the root node
        if (root == null) {
            return;
        }


        Node x = root; //Current node being checked


        while (true) {

            if (compare('=', x.key, n)) {
                //This is the node to be deleted

                Node y = x;
                boolean yColour = y.isRed;

                Node z = null;
                Node p = null;  //Parent node, will be passed to deleteFixup

                if  (x.left == null) {
                    //Replace x with the right child
                    z = x.right;
                    transplant(x, x.right);
                    p = x.parent;
                }

                else if (x.right == null) {
                    //Replace x with the left child
                    z = x.left;
                    transplant(x, x.left);
                    p = x.parent;
                }

                else {
                    //There are two non null-children
                    //Find the inorder successor of x;
                    y  = x.right;

                    while (y.left != null) {
                        y = y.left;
                    }

                    yColour = y.isRed;
                    z = y.right;
                    p = y;

                    if (y.parent == x && z != null) {
                        z.parent = y;
                    }
                    else {
                        transplant(y, y.right);
                        y.right = x.right;
                        if (y.right != null) {
                            y.right.parent = y;
                        }
                    }

                    transplant(x, y);
                    y.left = x.left;
                    y.left.parent = y;
                    y.isRed = x.isRed;

                }

                if (!yColour) {
                    deleteFixup(z, p);
                }

                break;

            }

            else if (compare('<', x.key, n)) {
                //Search in the right subtree
                if (x.right == null) {
                    //There is no right subtree
                    return;
                }
                else {
                    //Continue in the right subtree
                    x = x.right;
                }

            }

            else {
                //Search in the left subtree
                if (x.left == null) {
                    //There is no left subtree
                    return;
                }
                else {
                    //Continue in the left subtree
                    x = x.left;
                }
            }

        }

    }


    /**
     * Replace node X with node Y.
     * @param x The node to be replaced.
     * @param y The new node.
     */
    private void transplant (Node x, Node y) {

        if (x.parent == null) {
            //x is the root
            root = y;
        }
        else if  (x == x.parent.left) {
            //x is the left child
            x.parent.left = y;
        }
        else {
            //x is the right child
            x.parent.right = y;
        }

        if (y != null) {
            y.parent = x.parent;
        }
    }


    /**
     * After a node has been deleted, some RB tree properties may have been violated.
     * @param x We will start fixing the tree from node x
     * @param p The parent of x
     */
    private void deleteFixup(Node x, Node p) {

        while (x != root && (x == null || !x.isRed)) {

            Node w; //Sibling node

            if (x == p.left) {

                w = p.right;

                //Case 1: x has a red sibling
                if (w!= null && w.isRed) {
                    w.isRed = false;
                    p.isRed = true;
                    rrRotation(p);
                    w = p.right;
                }

                //Case 2: x's sibling is black, and the sibling has 2 black children
                if (w!= null && (w.left==null || !w.left.isRed) && (w.left==null || !w.right.isRed)) {
                    w.isRed = true;
                    x = p;
                }

                //Case 3: x's sibling is black, and the sibling's left child is red and right child is black
                else if (w!= null && (w.right==null || !w.right.isRed)){
                    w.left.isRed = false;
                    w.isRed = true;
                    llRotation(w);
                    w = p.right;
                }

                //Case 4: x's sibling is black, and the sibling's right child is red
                else if (w!=null) {
                    w.isRed = p.isRed;
                    p.isRed = false;
                    w.right.isRed = false;
                    rrRotation(p);
                    x = root;
                }

                //Case 5: x does not have a sibling
                else {
                    break;
                }

            }

            else {

                w = p.left;

                //Case 1: x has a red sibling
                if (w!= null && w.isRed) {
                    w.isRed = false;
                    p.isRed = true;
                    llRotation(p);
                    w = p.left;
                }

                //Case 2: x's sibling is black, and the sibling has 2 black children
                if (w!= null && (w.left==null || !w.left.isRed) && (w.right==null || !w.right.isRed)) {
                    w.isRed = true;
                    x = p;
                }

                //Case 3: x's sibling is black, and the sibling's right child is red and left child is black
                else if (w!= null && (w.left==null || !w.left.isRed)){
                    w.right.isRed = false;
                    w.isRed = true;
                    rrRotation(w);
                    w = p.left;
                }

                //Case 4: x's sibling is black, and the sibling's left child is red
                else if (w!= null){
                    w.isRed = p.isRed;
                    p.isRed = false;
                    w.left.isRed = false;
                    llRotation(p);
                    x = root;
                }

                //Case 5: x does not have a sibling
                else {
                    break;
                }


            }

        }

        if (x != null) {
            x.isRed = false;
        }
    }






    //Rotations

    /**
     * Perform a left single rotation
     */
    private void llRotation(Node x) {

        rotations++;

        Node tmp = x.left; //Store the node that will be rotated temporarily

        x.left = tmp.right; //Move the child to its new position

        if (tmp.right != null) {
            //If the child exists, update its parent
            tmp.right.parent = x;
        }

        tmp.parent = x.parent;  //Set the new parent for the rotated node

        if (x.parent == null) {
            //If x was the root, then tmp is now the new root
            this.root = tmp;
        }
        else if (x == x.parent.left) {
            //x was the left child, tmp replaces it
            x.parent.left = tmp;
        }
        else {
            //x was the right child, tmp replaces it
            x.parent.right = tmp;
        }

        //Put x in its new position
        tmp.right = x;
        x.parent = tmp;

    }

    /**
     * Perform a left double rotation
     */
    private void lrRotation(Node x) {
        rrRotation(x.left);
        llRotation(x);
    }

    /**
     * Perform a right single rotation
     */
    private void rrRotation(Node x) {

        rotations++;

        Node tmp = x.right; //Store the node that will be rotated temporarily

        x.right = tmp.left; //Move the child to its new position

        if (tmp.left != null) {
            //If the child exists, update its parent
            tmp.left.parent = x;
        }

        tmp.parent = x.parent;  //Set the new parent for the rotated node

        if (x.parent == null) {
            //If x was the root, then tmp is now the new root
            this.root = tmp;
        }
        else if (x == x.parent.left) {
            //x was the left child, tmp replaces it
            x.parent.left = tmp;
        }
        else {
            //x was the right child, tmp replaces it
            x.parent.right = tmp;
        }

        //Put x in its new position
        tmp.left = x;
        x.parent = tmp;

    }

    /**
     * Perform a right double rotation
     */
    private void rlRotation(Node x) {
        llRotation(x.right);
        rrRotation(x);
    }


    /**
     * Get the height of the tree.
     * @return The height of the tree.
     */
    public int height() {
        return root.height();
    }

    /**
     * Counts the number of nodes in the tree.
     * @return The number of nodes in the tree.
     */
    public int countNodes() {
        return root.countNodes();
    }



    /**
     * Used instead of <, >, and == operators, counting the number of times it was called.
     * @return boolean value
     */
    private boolean compare(char op, int n1, int n2) {
        this.comparisons++;

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
     * Prints "(key, R/B, left.key, right.key)" for each node.
     * Used for debugging purposes.
     */
    public void print(){
        if (root != null) {
            root.print();
        }
    }

}
