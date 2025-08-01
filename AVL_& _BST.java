import java.util.*;
import static java.lang.Math.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
 
class BSTNode {
    int data;
    int h;
    BSTNode left, right;
    public BSTNode(int x){
        this.data = x; 
        this.h = 1;
        this.left = this.right = null;
    }
}
 
class Solution {
    // 1. where to insert   2. what to insert
    // returning new root
    BSTNode insertNode(BSTNode root, int x){
        if(root == null)
            return new BSTNode(x);
        if(x < root.data){
            root.left = insertNode(root.left, x);
        } else {
            root.right = insertNode(root.right, x);
        }
        updateHeight(root);
        if(getBF(root) == +2){ // LEFT HEAVY CASES
        // LL / LR
            if(getBF(root.left)==1) //LL
            {
                root=rightRotate(root);
            }
            else if(getBF(root.left)==-1) //LR
            {
                root.left=leftRotate(root.left);
                root=rightRotate(root);
            }
        } else if(getBF(root) == -2){ // RIGHT HEAVY CASE
            if(getBF(root.right) == -1){ // RR
                // left rotate of root
                root = leftRotate(root);
            } else { // RL
                root.right=rightRotate(root.right);
                root=leftRotate(root);
            }
        }
        return root;
    }
    BSTNode leftRotate(BSTNode root){
        BSTNode newRoot = root.right;
        BSTNode t1 = root.left, t2 = root.right.left;
        // root.left = t1
        root.right = t2;
        updateHeight(root);
        newRoot.left = root;
        updateHeight(newRoot);
        return newRoot;
    }

    BSTNode rightRotate(BSTNode root)
    {
        BSTNode newRoot=root.left;
        BSTNode t1=root.right;
        BSTNode t2=root.left.right;
        root.left=t2;
        updateHeight(root);
        newRoot.right=root;
        updateHeight(newRoot);
        return newRoot;
    }
 
    int getBF(BSTNode root){
    	if(root==null)
    	return 0;
        return (root.left==null ? 0 : root.left.h) -  
                    (root.right==null ? 0: root.right.h);
    }
    void updateHeight(BSTNode root){
    	if(root!=null)
        root.h = 1 + max(root.left==null ? 0 : root.left.h, 
                        root.right==null ? 0: root.right.h);
    }
    boolean searchNode(BSTNode root,int val)
    {
    	if(root==null)
    	return false;
    	if(root.data==val)
    	return true;
    	if(val<root.data)
    	{
    	    return searchNode(root.left,val);
    	}
    	else
    	{
    	   return searchNode(root.right,val);
    	}
    }
    
    BSTNode deleteNode(BSTNode root, int val) 
    {
    if (root == null) return null;

    if (val < root.data) 
    {
        root.left = deleteNode(root.left, val);
    } 
    else if (val > root.data) 
    {
        root.right = deleteNode(root.right, val);
    } else {
        // Node found
        if (root.left == null || root.right == null) 
        {
            root = (root.left != null) ? root.left : root.right;
        } 
        else {
            // Find inorder successor
            BSTNode temp = findMin(root.right);
            root.data = temp.data;
            root.right = deleteNode(root.right, temp.data);
        }
    }

    // If the tree had only one node
    if (root == null) return null;

    // Update height
    updateHeight(root);

    // Balance the tree (AVL logic)
    int balanceFactor = getBF(root);

    // Left heavy
    if (balanceFactor > 1) {
        if (getBF(root.left) >= 0) {
            return rightRotate(root);  // LL
        } else {
            root.left = leftRotate(root.left);  // LR
            return rightRotate(root);
        }
    }

    // Right heavy
    if (balanceFactor < -1) {
        if (getBF(root.right) <= 0) {
            return leftRotate(root);  // RR
        } else {
            root.right = rightRotate(root.right);  // RL
            return leftRotate(root);
        }
    }

    return root;  // Balanced node
}

    BSTNode findMin(BSTNode root) {
        while (root.left != null) root = root.left;
        return root;
    }

    BSTNode balance(BSTNode root) {
        updateHeight(root);

        int balanceFactor = getBF(root);

        // Left-heavy cases
        if (balanceFactor > 1) {
            if (getBF(root.left) >= 0) {
                return rightRotate(root);  // LL case
            } else {
                root.left = leftRotate(root.left);  // LR case
                return rightRotate(root);
            }
        }

        // Right-heavy cases
        if (balanceFactor < -1) {
            if (getBF(root.right) <= 0) {
                return leftRotate(root);  // RR case
            } else {
                root.right = rightRotate(root.right);  // RL case
                return leftRotate(root);
            }
        }

        return root;  // Already balanced
    }
}
 
class Main {
    static Scanner sc = new Scanner(System.in);
    static Solution sol = new Solution();
    public static void main(String[] args) {
        int val;
        BSTNode root = null; // declare an empty tree
        TreePrinter<BSTNode> printer = new TreePrinter<>(n->""+n.data, n->n.left, n->n.right);
        //while(sc.hasNextInt()){
            //root = sol.insertNode(root, sc.nextInt());
            //printer.printTree(root);
        //}
        while(true)
        {
        	System.out.println("\nChoose an operation :  1-Insert   2-Search   3-Delete   4-Print   5-Exit");
        	System.out.print("Enter your choice = ");
        	int ch=sc.nextInt();
        	switch(ch)
        	{
        		case 1:
        			System.out.print("Enter value to Insert = ");
        			val=sc.nextInt();
        			root=sol.insertNode(root,val);
        			System.out.println();
        			printer.printTree(root);
        			break;
        		case 2:
        		    System.out.print("Enter value to Search = ");
        		    val=sc.nextInt();
        		    boolean ans=sol.searchNode(root,val);
        		    if(ans)
        		    {
        		        System.out.println();
        		        System.out.println(val+" - Node is FOUND");
        		    }
        		    else
        		    {
        		        System.out.println();
        		        System.out.println(val+" - Node is NOT FOUND");
        		    }
        		    break;
        		case 3:
                    System.out.print("Enter value to Delete = ");
                    val = sc.nextInt();

                    if (!sol.searchNode(root, val)) 
                    {
                        System.out.println("\nNode not found in the tree.");
                    } 
                    else 
                    {
                        root = sol.deleteNode(root, val);
                        if (root == null) 
                        {
                            System.out.println("\nNode deleted. The tree is now empty.");
                        } 
                        else
                        {
                            System.out.println("\nNode deleted successfully.\n");
                            printer.printTree(root);
                        }
                    }
                    break;
        		case 4:
        			System.out.println("\n***** AVL TREE *****\n");
        			printer.printTree(root);
        			break;
        		case 5:
        		    System.out.println("\n----- The End -----\n");
        		    return;
        		default:
        		    System.out.println("Invalid choice, try again....");
        	}
        }
    }
}
 
 
class TreePrinter<T> {
 
    private Function<T, String> getLabel;
    private Function<T, T> getLeft;
    private Function<T, T> getRight;
 
    private PrintStream outStream = System.out;
 
    private boolean squareBranches = false;
    private boolean lrAgnostic = false;
    private int hspace = 2;
    private int tspace = 1;
 
    public TreePrinter(Function<T, String> getLabel, Function<T, T> getLeft, Function<T, T> getRight) {
        this.getLabel = getLabel;
        this.getLeft = getLeft;
        this.getRight = getRight;
    }
 
    public void setPrintStream(PrintStream outStream) {
        this.outStream = outStream;
    }
 
    public void setSquareBranches(boolean squareBranches) { this.squareBranches = squareBranches; }
 
    public void setLrAgnostic(boolean lrAgnostic) { this.lrAgnostic = lrAgnostic; }
 
    public void setHspace(int hspace) { this.hspace = hspace; }
 
    public void setTspace(int tspace) { this.hspace = tspace; }
 
    /*
        Prints ascii representation of binary tree.
        Parameter hspace is minimum number of spaces between adjacent node labels.
        Parameter squareBranches, when set to true, results in branches being printed with ASCII box
        drawing characters.
     */
    public void printTree(T root) {
        List<TreeLine> treeLines = buildTreeLines(root);
        printTreeLines(treeLines);
    }
 
    /*
        Prints ascii representations of multiple trees across page.
        Parameter hspace is minimum number of spaces between adjacent node labels in a tree.
        Parameter tspace is horizontal distance between trees, as well as number of blank lines
        between rows of trees.
        Parameter lineWidth is maximum width of output
        Parameter squareBranches, when set to true, results in branches being printed with ASCII box
        drawing characters.
     */
    public void printTrees(List<T> trees, int lineWidth) {
        List<List<TreeLine>> allTreeLines = new ArrayList<>();
        int[] treeWidths = new int[trees.size()];
        int[] minLeftOffsets = new int[trees.size()];
        int[] maxRightOffsets = new int[trees.size()];
        for (int i = 0; i < trees.size(); i++) {
            T treeNode = trees.get(i);
            List<TreeLine> treeLines = buildTreeLines(treeNode);
            allTreeLines.add(treeLines);
            minLeftOffsets[i] = minLeftOffset(treeLines);
            maxRightOffsets[i] = maxRightOffset(treeLines);
            treeWidths[i] = maxRightOffsets[i] - minLeftOffsets[i] + 1;
        }
 
        int nextTreeIndex = 0;
        while (nextTreeIndex < trees.size()) {
            // print a row of trees starting at nextTreeIndex
 
            // first figure range of trees we can print for next row
            int sumOfWidths = treeWidths[nextTreeIndex];
            int endTreeIndex = nextTreeIndex + 1;
            while (endTreeIndex < trees.size() && sumOfWidths + tspace + treeWidths[endTreeIndex] < lineWidth) {
                sumOfWidths += (tspace + treeWidths[endTreeIndex]);
                endTreeIndex++;
            }
            endTreeIndex--;
 
            // find max number of lines for tallest tree
            int maxLines = allTreeLines.stream().mapToInt(list -> list.size()).max().orElse(0);
 
            // print trees line by line
            for (int i = 0; i < maxLines; i++) {
                for (int j = nextTreeIndex; j <= endTreeIndex; j++) {
                    List<TreeLine> treeLines = allTreeLines.get(j);
                    if (i >= treeLines.size()) {
                        System.out.print(spaces(treeWidths[j]));
                    } else {
                        int leftSpaces = -(minLeftOffsets[j] - treeLines.get(i).leftOffset);
                        int rightSpaces = maxRightOffsets[j] - treeLines.get(i).rightOffset;
                        System.out.print(spaces(leftSpaces) + treeLines.get(i).line + spaces(rightSpaces));
                    }
                    if (j < endTreeIndex) System.out.print(spaces(tspace));
                }
                System.out.println();
            }
 
            for (int i = 0; i < tspace; i++) {
                System.out.println();
            }
 
            nextTreeIndex = endTreeIndex + 1;
        }
    }
 
    private void printTreeLines(List<TreeLine> treeLines) {
        if (treeLines.size() > 0) {
            int minLeftOffset = minLeftOffset(treeLines);
            int maxRightOffset = maxRightOffset(treeLines);
            for (TreeLine treeLine : treeLines) {
                int leftSpaces = -(minLeftOffset - treeLine.leftOffset);
                int rightSpaces = maxRightOffset - treeLine.rightOffset;
                outStream.println(spaces(leftSpaces) + treeLine.line + spaces(rightSpaces));
            }
        }
    }
 
    private List<TreeLine> buildTreeLines(T root) {
        if (root == null) return Collections.emptyList();
        else {
            String rootLabel = getLabel.apply(root);
            List<TreeLine> leftTreeLines = buildTreeLines(getLeft.apply(root));
            List<TreeLine> rightTreeLines = buildTreeLines(getRight.apply(root));
 
            int leftCount = leftTreeLines.size();
            int rightCount = rightTreeLines.size();
            int minCount = Math.min(leftCount, rightCount);
            int maxCount = Math.max(leftCount, rightCount);
 
            // The left and right subtree print representations have jagged edges, and we essentially we have to
            // figure out how close together we can bring the left and right roots so that the edges just meet on
            // some line.  Then we add hspace, and round up to next odd number.
            int maxRootSpacing = 0;
            for (int i = 0; i < minCount; i++) {
                int spacing = leftTreeLines.get(i).rightOffset - rightTreeLines.get(i).leftOffset;
                if (spacing > maxRootSpacing) maxRootSpacing = spacing;
            }
            int rootSpacing = maxRootSpacing + hspace;
            if (rootSpacing % 2 == 0) rootSpacing++;
            // rootSpacing is now the number of spaces between the roots of the two subtrees
 
            List<TreeLine> allTreeLines = new ArrayList<>();
 
            // strip ANSI escape codes to get length of rendered string. Fixes wrong padding when labels use ANSI escapes for colored nodes.
            String renderedRootLabel = rootLabel.replaceAll("\\e\\[[\\d;]*[^\\d;]", "");
 
            // add the root and the two branches leading to the subtrees
 
            allTreeLines.add(new TreeLine(rootLabel, -(renderedRootLabel.length() - 1) / 2, renderedRootLabel.length() / 2));
 
            // also calculate offset adjustments for left and right subtrees
            int leftTreeAdjust = 0;
            int rightTreeAdjust = 0;
 
            if (leftTreeLines.isEmpty()) {
                if (!rightTreeLines.isEmpty()) {
                    // there's a right subtree only
                    if (squareBranches) {
                        if (lrAgnostic) {
                            allTreeLines.add(new TreeLine("\u2502", 0, 0));
                        } else {
                            allTreeLines.add(new TreeLine("\u2514\u2510", 0, 1));
                            rightTreeAdjust = 1;
                        }
                    } else {
                        allTreeLines.add(new TreeLine("\\", 1, 1));
                        rightTreeAdjust = 2;
                    }
                }
            } else if (rightTreeLines.isEmpty()) {
                // there's a left subtree only
                if (squareBranches) {
                    if (lrAgnostic) {
                        allTreeLines.add(new TreeLine("\u2502", 0, 0));
                    } else {
                        allTreeLines.add(new TreeLine("\u250C\u2518", -1, 0));
                        leftTreeAdjust = -1;
                    }
                } else {
                    allTreeLines.add(new TreeLine("/", -1, -1));
                    leftTreeAdjust = -2;
                }
            } else {
                // there's a left and right subtree
                if (squareBranches) {
                    int adjust = (rootSpacing / 2) + 1;
                    String horizontal = String.join("", Collections.nCopies(rootSpacing / 2, "\u2500"));
                    String branch = "\u250C" + horizontal + "\u2534" + horizontal + "\u2510";
                    allTreeLines.add(new TreeLine(branch, -adjust, adjust));
                    rightTreeAdjust = adjust;
                    leftTreeAdjust = -adjust;
                } else {
                    if (rootSpacing == 1) {
                        allTreeLines.add(new TreeLine("/ \\", -1, 1));
                        rightTreeAdjust = 2;
                        leftTreeAdjust = -2;
                    } else {
                        for (int i = 1; i < rootSpacing; i += 2) {
                            String branches = "/" + spaces(i) + "\\";
                            allTreeLines.add(new TreeLine(branches, -((i + 1) / 2), (i + 1) / 2));
                        }
                        rightTreeAdjust = (rootSpacing / 2) + 1;
                        leftTreeAdjust = -((rootSpacing / 2) + 1);
                    }
                }
            }
 
            // now add joined lines of subtrees, with appropriate number of separating spaces, and adjusting offsets
 
            for (int i = 0; i < maxCount; i++) {
                TreeLine leftLine, rightLine;
                if (i >= leftTreeLines.size()) {
                    // nothing remaining on left subtree
                    rightLine = rightTreeLines.get(i);
                    rightLine.leftOffset += rightTreeAdjust;
                    rightLine.rightOffset += rightTreeAdjust;
                    allTreeLines.add(rightLine);
                } else if (i >= rightTreeLines.size()) {
                    // nothing remaining on right subtree
                    leftLine = leftTreeLines.get(i);
                    leftLine.leftOffset += leftTreeAdjust;
                    leftLine.rightOffset += leftTreeAdjust;
                    allTreeLines.add(leftLine);
                } else {
                    leftLine = leftTreeLines.get(i);
                    rightLine = rightTreeLines.get(i);
                    int adjustedRootSpacing = (rootSpacing == 1 ? (squareBranches ? 1 : 3) : rootSpacing);
                    TreeLine combined = new TreeLine(leftLine.line + spaces(adjustedRootSpacing - leftLine.rightOffset + rightLine.leftOffset) + rightLine.line,
                            leftLine.leftOffset + leftTreeAdjust, rightLine.rightOffset + rightTreeAdjust);
                    allTreeLines.add(combined);
                }
            }
            return allTreeLines;
        }
    }
 
    private static int minLeftOffset(List<TreeLine> treeLines) {
        return treeLines.stream().mapToInt(l -> l.leftOffset).min().orElse(0);
    }
 
    private static int maxRightOffset(List<TreeLine> treeLines) {
        return treeLines.stream().mapToInt(l -> l.rightOffset).max().orElse(0);
    }
 
    private static String spaces(int n) {
        return String.join("", Collections.nCopies(n, " "));
    }
 
    private static class TreeLine {
        String line;
        int leftOffset;
        int rightOffset;
 
        TreeLine(String line, int leftOffset, int rightOffset) {
            this.line = line;
            this.leftOffset = leftOffset;
            this.rightOffset = rightOffset;
        }
    }
}