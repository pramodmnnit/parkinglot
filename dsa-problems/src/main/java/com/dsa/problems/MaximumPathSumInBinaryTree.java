package com.dsa.problems;

import java.util.Objects;

public class MaximumPathSumInBinaryTree {

  static int maxSum = Integer.MIN_VALUE;

  public static int maxPathSum(TreeNode root) {
    int sum = maxPathSumUtil(root);
    return Math.max(maxSum, sum);
  }

  public static int maxPathSumUtil(TreeNode root) {
    if(Objects.isNull(root)){
      return 0;
    }
    int leftSum = maxPathSumUtil(root.left);
    int rightSum = maxPathSumUtil(root.right);
    int maxSumAtMostOneChild = Math.max(Math.max(leftSum, rightSum) + root.data, root.data);
    int maxSumSoFar = Math.max(maxSumAtMostOneChild, root.data + leftSum + rightSum);
    maxSum = Math.max(maxSum, maxSumSoFar);
    return maxSumAtMostOneChild;
  }
  public static void main(String[] args){
    TreeNode root = new TreeNode(10);
    root.left = new TreeNode(2);
    root.right = new TreeNode(10);
    root.left.left = new TreeNode(20);
    root.left.right = new TreeNode(1);
    root.right.right = new TreeNode(-25);
    root.right.right.left = new TreeNode(3);
    root.right.right.right = new TreeNode(4);
    System.out.println("Maximum path sum:" + maxPathSum(root));
  }

}
