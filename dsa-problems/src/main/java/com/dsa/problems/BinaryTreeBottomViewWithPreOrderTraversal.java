package com.dsa.problems;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class BinaryTreeBottomViewWithPreOrderTraversal {

  public static void main(String [] args){
    TreeNode root = buildTree();
    printTreeBottomView(root);
  }

  private static TreeNode buildTree() {
    TreeNode root = new TreeNode(20);
    root.left = new TreeNode(8);
    root.right = new TreeNode(22);
    root.left.left = new TreeNode(5);
    root.left.right = new TreeNode(3);
    root.left.right.left = new TreeNode(10);
    root.right.left = new TreeNode(4);
    root.right.left.right = new TreeNode(14);
    root.right.right = new TreeNode(25);
    return root;
  }

  private static void printTreeBottomView(TreeNode root) {
    if(Objects.isNull(root)){
      return;
    }
    Map<Integer, Pair<Integer, Integer>> distanceToNodePairMap = new TreeMap<>();
    printTreeBottomViewUtil(root, 0, 0, distanceToNodePairMap);
    for(Map.Entry<Integer, Pair<Integer, Integer>> each : distanceToNodePairMap.entrySet()){
      System.out.print(each.getValue().first + " ");
    }
    System.out.println();
  }

  private static void printTreeBottomViewUtil(TreeNode root, int distance, int height,
      Map<Integer, Pair<Integer, Integer>> distanceToNodePairMap) {
    if(Objects.isNull(root)){
      return;
    }
    if(!distanceToNodePairMap.containsKey(distance)){
      distanceToNodePairMap.put(distance, new Pair<>(root.data, height));
    } else {
      Pair<Integer, Integer> pair = distanceToNodePairMap.get(distance);
      if(pair.second <= height){
        pair.second = height;
        pair.first = root.data;
        distanceToNodePairMap.put(distance, pair);
      }
    }
    printTreeBottomViewUtil(root.left, distance -1 , height +1, distanceToNodePairMap);
    printTreeBottomViewUtil(root.right, distance +1 , height +1, distanceToNodePairMap);
  }
}
