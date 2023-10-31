package com.sword.mylibrary.leetcode;

import java.util.Objects;

/**
 * <a href="https://leetcode.cn/problems/add-two-numbers/description/">两数相加</a>
 */
public class AddTwoNumbers {
    public static void main(String[] args) {
        AddTwoNumbers addTwoNumbers = new AddTwoNumbers();
        ListNode l1 = buildListNodeData(new int[]{2, 4, 3});
        printListNode(l1);
        ListNode l2 = buildListNodeData(new int[]{5, 6, 4});
        printListNode(l2);
        ListNode result = addTwoNumbers.addTwoNumbers(l1, l2);
        printListNode(result);

    }


    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        if (l1 == null) return l2;
        if (l2 == null) return l1;

        ListNode l1Current = l1;
        ListNode l2Current = l2;

        ListNode head = null;
        ListNode tail = null;
        int sum;
        int carry = 0;
        do {
            sum = l1Current == null ? l2Current.val : l2Current == null ? l1Current.val : l1Current.val + l2Current.val;
            sum += carry;
            carry = sum < 10 ? 0 : sum / 10;

            //第一个节点
            if (head == null) {
                head = tail = new ListNode(sum % 10);
            } else { // 非第一个节点
                tail.next = new ListNode(sum % 10);
                tail = tail.next;
            }

            if (l1Current != null) {
                l1Current = l1Current.next;
            }
            if (l2Current != null) {
                l2Current = l2Current.next;
            }
        } while (l1Current != null || l2Current != null);

        if (carry > 0) {
            tail.next = new ListNode(carry);
        }

        return head;
    }

    private static ListNode buildListNodeData(int[] datas) {
        ListNode firstNode = new ListNode(datas[0]);
        firstNode.next = new ListNode();
        ListNode currentNode = firstNode.next;
        for (int i=1; i<datas.length; i++) {
            currentNode.val = datas[i];
            if (i < datas.length - 1) {
                currentNode.next = new ListNode();
                currentNode = currentNode.next;
            }
        }
        return firstNode;
    }

    private static void printListNode(ListNode listNode) {
        ListNode current = listNode;
        StringBuilder printString = new StringBuilder("[");
        while (current != null) {
            if (current.next != null) {
                printString.append(current.val).append(", ");
            } else {
                printString.append(current.val).append("]");
            }
            current = current.next;
        }
        System.out.println(printString);
    }



    static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
}
