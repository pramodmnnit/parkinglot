package com.dsa.problems;

import java.util.Objects;

public class LevelOrderTraversalWithRecursion {

  public static void printLevelOrder(TreeNode root){
    System.out.println("Level order traversal");
    int height = getHeight(root);
    for(int level= 1; level<= height ; level++){
      printNodesAtLevel(root, level);
    }
  }

  private static void printNodesAtLevel(TreeNode root, int level) {
    if(Objects.isNull(root)){
      return;
    }
    if(level == 1){
      System.out.print(root.data + " ");
    }
    printNodesAtLevel(root.left, level-1);
    printNodesAtLevel(root.right, level-1);
  }

 public static int getHeight(TreeNode root) {
    if(Objects.isNull(root)){
      return 0;
    }
    int leftHeight = getHeight(root.left);
    int rightHeight = getHeight(root.right);
    if(leftHeight > rightHeight){
      return leftHeight + 1;
    } else {
      return rightHeight + 1;
    }

  }

  public static void main(String[] args){
    TreeNode root = new TreeNode(1);
    root.left = new TreeNode(2);
    root.right = new TreeNode(3);
    root.left.left = new TreeNode(4);
    root.left.right  = new TreeNode(5);
    printLevelOrder(root);
  }
}
