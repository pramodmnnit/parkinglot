package com.dsa.problems;

import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.TreeMap;

class TreeNodeWithDistance {
  int data;
  int distance;
  TreeNodeWithDistance left;
  TreeNodeWithDistance right;

  public TreeNodeWithDistance(int data) {
    this.data = data;
    this.left = this.right = null;
  }

}

public class BinaryTreeBottomView {

  public static void main(String[] args) {
    TreeNodeWithDistance root = new TreeNodeWithDistance(20);
    root.left = new TreeNodeWithDistance(8);
    root.right = new TreeNodeWithDistance(22);
    root.left.left = new TreeNodeWithDistance(5);
    root.left.right = new TreeNodeWithDistance(3);
    root.left.right.left = new TreeNodeWithDistance(10);
    root.right.left = new TreeNodeWithDistance(4);
    root.right.left.right = new TreeNodeWithDistance(14);
    root.right.right = new TreeNodeWithDistance(25);
    printBottomView(root);
  }

  private static void printBottomView(TreeNodeWithDistance root) {
    if(Objects.isNull(root)){
      return;
    }
    Map<Integer, Integer> distanceToDataMap = new TreeMap<>();
    Queue<TreeNodeWithDistance> queue = new LinkedList<>();
    root.distance = 0;
    queue.add(root);
    while (!queue.isEmpty()){
      TreeNodeWithDistance node = queue.remove();
      distanceToDataMap.put(node.distance, node.data);
      if(Objects.nonNull(node.left)){
        node.left.distance = node.distance -1;
        queue.add(node.left);
      }
      if(Objects.nonNull(node.right)){
        node.right.distance = node.distance + 1;
        queue.add(node.right);
      }
    }
    System.out.println("Bottom view of the tree");
    for( Map.Entry<Integer, Integer> each : distanceToDataMap.entrySet()){
      System.out.print(each.getValue() + " ");
    }
    System.out.println();
  }


}

