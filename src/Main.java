import java.util.*;
import java.lang.Math;

public class Main {

    public static void main(String[] args) {

        RBTree rb = new RBTree();
        AVLTree avl = new AVLTree();


        //Create sets
        int n = (int) Math.round((Math.random()*2000)+1000);
        int m = (int) Math.round((Math.random()*500)+500);
        int k = (int) Math.round((Math.random()*1000)+1000);

        Set X = makeRandSet(n, -3000, 3000);
        Set Y = makeRandSet(m, -3000, 3000);
        Set Z = makeRandSet(k, -3000, 3000);

        System.out.println("Set X contains " + X.size() + " elements");
        System.out.println("Set Y contains " + Y.size() + " elements");
        System.out.println();
        System.out.println("Sets X and Y have " + compareSet(X, Y) + " elements in common");
        System.out.println();

        RBTree.resetCounters();
        AVLTree.resetCounters();


        //Insert Elements

        for (Object x: X) {
            avl.insert((int) x);
            rb.insert((int) x);
        }

        System.out.println("Insertions:");
        System.out.printf("AVL: %d tot. rotations req., height is %d, #nodes is %d, #comparisons is %d\n", AVLTree.rotations, avl.height, avl.countNodes(), AVLTree.comparisons);
        System.out.printf("RBT: %d tot. rotations req., height is %d, #nodes is %d, #comparisons is %d\n", RBTree.rotations, rb.height(), rb.countNodes(), RBTree.comparisons);
        System.out.println();

        RBTree.resetCounters();
        AVLTree.resetCounters();


        //Delete

        for (Object y: Y) {
            avl.delete((int) y);
            rb.delete((int) y);
        }

        System.out.println("Deletions:");
        System.out.printf("AVL: %d tot. rotations req., height is %d, #nodes is %d, #comparisons is %d\n", AVLTree.rotations, avl.height, avl.countNodes(), AVLTree.comparisons);
        System.out.printf("RBT: %d tot. rotations req., height is %d, #nodes is %d, #comparisons is %d\n", RBTree.rotations, rb.height(), rb.countNodes(), RBTree.comparisons);
        System.out.println();

        RBTree.resetCounters();
        AVLTree.resetCounters();


        //Search

        for (Object z: Z) {
            avl.search((int) z);
            rb.search((int) z);
        }

        System.out.println("Search:");
        System.out.println("k is " + k);
        System.out.printf("AVL: %d tot. comparisons required\n", AVLTree.comparisons);
        System.out.printf("RBT: %d tot. comparisons required\n", RBTree.comparisons);
        System.out.println();

    }


    /**
     * Returns a set of randomly generated integers.
     * @param size The size of the set created.
     * @param min The lower-bound of the set.
     * @param max The upper-bound of the set.
     * @return The Set of random numbers.
     */
    public static Set makeRandSet(int size, int min, int max){

        Set<Integer> X = new HashSet<Integer>();


        // Loop until we have filled the set
        // Since we are using sets, numbers will only be added if they do not already exist
        // So the size will only increase when a new number is generated
        while (X.size() < size) {
            int r = (int) Math.floor( Math.random()*(max-min+1) + min);
            X.add(r);
        }

        return X;
    }


    /**
     * Counts the number of common elements in two sets
     * @param X The larger set.
     * @param Y The smaller set.
     * @return The number of common elements
     */
    public static int compareSet(Set X, Set Y) {
        int counter = 0;

        for (Object y : Y) {
            if (X.contains(y)) {
                counter++;
            }
        }

        return counter;
    }


}
