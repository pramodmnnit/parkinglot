package com.dsa.problems;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class LevelOrderTraversalWithQueue {

  public static void printLevelOrder(TreeNode root){
    System.out.println("Level order traversal");
    Queue<TreeNode> queue = new LinkedList<>();
    queue.add(root);
    while (!queue.isEmpty()){
      TreeNode node = queue.remove();
      System.out.print(node.data + " ");
      if( Objects.nonNull(node.left)){
        queue.add(node.left);
        }
      if(Objects.nonNull(node.right)){
        queue.add(node.right);
      }
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
