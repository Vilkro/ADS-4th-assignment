package com.example.task2;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static int SIZE = 2 * 128;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter a source file name: ");
        String sourceFilename = input.nextLine();
        System.out.print("Enter a target file name: ");
        String targetFilename = input.nextLine();

        String codedFilename = sourceFilename + ".new";
        String codesFilename = sourceFilename + ".counts";

        String codedFile = getCodedFile(codedFilename);
        int[] counts = getCounts(codesFilename);

        Tree tree = getHuffmanTree(counts);

        String[] codes = new String[SIZE];
        assignCode(tree.root, codes);

        System.out.println("Counts:" + counts + "!\n");
        System.out.println("Codes:" + codes + "!");
        System.out.println("\n\nCoded text:" + codedFile + "!\n");

        ArrayList<Character> result = decomress(tree, codedFile);

        WriteToFilePlease(targetFilename, result);
    }

    public static void WriteToFilePlease(String filename, ArrayList<Character> result) throws IOException {

        FileWriter myWriter = new FileWriter(filename);
        String res = "";
        for (int i = 0; i < result.size(); i++) {
            res += result.get(i);
        }
        myWriter.write(res);
        myWriter.close();
    }

    public static ArrayList<Character> decomress(Tree tree, String codedFile) {

        ArrayList<Character> result = new ArrayList<>();

        Main.Tree.Node node = tree.root;

        for (int i = 0; i < codedFile.length(); i++) {

            if (codedFile.charAt(i) == '0') {
                node = node.left;
            } else {
                node = node.right;
            }
            if (node.left == null && node.right == null) {
                // System.out.print(node.element);
                result.add(node.element);
                node = tree.root;
            }

        }

        return result;
    }

    public static String getCodedFile(String codeFile) throws IOException {
        FileInputStream fis = new FileInputStream(codeFile);

        String result = "";
        int r;
        while ((r = fis.read()) != -1) {
            result = result + String.format("%8s", Integer.toBinaryString(r)).replace(' ', '0');
        }
        return result;
    }

    public static int[] getCounts(String encodedFile) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(encodedFile);

        ObjectInputStream input = new ObjectInputStream(fis);

        long charlen = input.readLong();
        int[] counts = (int[]) input.readObject();
        return counts;
    }

    public static void assignCode(Tree.Node root, String[] codes) {
        if (root == null)
            return;

        if (root.left != null) {
            root.left.code = root.code + "0";
            assignCode(root.left, codes);

            root.right.code = root.code + "1";
            assignCode(root.right, codes);
        } else {
            codes[(int) root.element] = root.code;
        }
    }

    public static Tree getHuffmanTree(int[] counts) {
        Heap<Tree> heap = new Heap<Tree>();

        for (int i = 0; i < counts.length; i++) {
            if (counts[i] > 0)
                heap.add(new Tree(counts[i], (char) i));
        }

        while (heap.getSize() > 1) {
            Tree t1 = heap.remove();
            Tree t2 = heap.remove();
            heap.add(new Tree(t1, t2));
        }

        return heap.remove();
    }

    public static class Tree implements Comparable<Tree> {
        Node root;

        public Tree(Tree t1, Tree t2) {
            root = new Node();
            root.left = t1.root;
            root.right = t2.root;
            root.weight = t1.root.weight + t2.root.weight;
        }

        public Tree(int weight, char element) {
            root = new Node(weight, element);
        }

        public int compareTo(Tree o) {
            if (root.weight < o.root.weight)
                return 1;
            else if (root.weight == o.root.weight)
                return 0;
            else
                return -1;
        }

        public class Node {
            char element;
            int weight;
            Node left;
            Node right;
            String code = "";

            public Node() {
            }

            public Node(int weight, char element) {
                this.weight = weight;
                this.element = element;
            }
        }
    }

    public static class Heap<E extends Comparable<E>> {
        private java.util.ArrayList<E> list = new java.util.ArrayList<E>();

        /** Create a default heap */
        public Heap() {
        }

        /** Create a heap from an array of objects */
        public Heap(E[] objects) {
            for (int i = 0; i < objects.length; i++)
                add(objects[i]);
        }

        /** Add a new object into the heap */
        public void add(E newObject) {
            list.add(newObject); // Append to the heap
            int currentIndex = list.size() - 1; // The index of the last node

            while (currentIndex > 0) {
                int parentIndex = (currentIndex - 1) / 2;
                // Swap if the current object is greater than its parent
                if (list.get(currentIndex).compareTo(
                        list.get(parentIndex)) > 0) {
                    E temp = list.get(currentIndex);
                    list.set(currentIndex, list.get(parentIndex));
                    list.set(parentIndex, temp);
                } else
                    break; // the tree is a heap now

                currentIndex = parentIndex;
            }
        }

        /** Remove the root from the heap */
        public E remove() {
            if (list.size() == 0)
                return null;

            E removedObject = list.get(0);
            list.set(0, list.get(list.size() - 1));
            list.remove(list.size() - 1);

            int currentIndex = 0;
            while (currentIndex < list.size()) {
                int leftChildIndex = 2 * currentIndex + 1;
                int rightChildIndex = 2 * currentIndex + 2;

                // Find the maximum between two children
                if (leftChildIndex >= list.size())
                    break; // The tree is a heap
                int maxIndex = leftChildIndex;
                if (rightChildIndex < list.size()) {
                    if (list.get(maxIndex).compareTo(
                            list.get(rightChildIndex)) < 0) {
                        maxIndex = rightChildIndex;
                    }
                }

                // Swap if the current node is less than the maximum
                if (list.get(currentIndex).compareTo(
                        list.get(maxIndex)) < 0) {
                    E temp = list.get(maxIndex);
                    list.set(maxIndex, list.get(currentIndex));
                    list.set(currentIndex, temp);
                    currentIndex = maxIndex;
                } else
                    break; // The tree is a heap
            }

            return removedObject;
        }

        /** Get the number of nodes in the tree */
        public int getSize() {
            return list.size();
        }
    }

}