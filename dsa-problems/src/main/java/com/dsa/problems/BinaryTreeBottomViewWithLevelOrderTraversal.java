package com.dsa.problems;

import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.TreeMap;

public class BinaryTreeBottomViewWithLevelOrderTraversal {


  public static void main(String[] args) {
    TreeNode root = new TreeNode(20);
    root.left = new TreeNode(8);
    root.right = new TreeNode(22);
    root.left.left = new TreeNode(5);
    root.left.right = new TreeNode(3);
    root.left.right.left = new TreeNode(10);
    root.right.left = new TreeNode(4);
    root.right.left.right = new TreeNode(14);
    root.right.right = new TreeNode(25);
    printBottomView(root);
  }

  private static void printBottomView(TreeNode root) {
    if(Objects.isNull(root)){
      return;
    }
    int leftMost = Integer.MAX_VALUE;
    Map<Integer, Integer> distanceToDataMap = new TreeMap<>();
    Queue<Pair<TreeNode, Integer>> queue = new LinkedList<>();
    queue.add(new Pair<>(root, 0));
    while (!queue.isEmpty()){
      Pair<TreeNode, Integer> nodePair = queue.remove();
      distanceToDataMap.put(nodePair.second, nodePair.first.data);
      leftMost = Math.min(leftMost, nodePair.second);
      if(Objects.nonNull(nodePair.first.left)){
        queue.add(new Pair<>(nodePair.first.left, nodePair.second -1));
      }
      if(Objects.nonNull(nodePair.first.right)){
        queue.add(new Pair<>(nodePair.first.right, nodePair.second + 1));
      }
    }
    System.out.println("Bottom view of the tree with level order traversal");
    while(distanceToDataMap.containsKey(leftMost)){
      System.out.print(distanceToDataMap.get(leftMost++)+ " ");
      }
    System.out.println();
  }

}
