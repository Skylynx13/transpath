package com.skylynx13.transpath.tools;

import java.util.*;

public class Transtool {
    public static void analyzeString () {
        String input = "Cullen - The-Milk in the Mild mountains57234 What's ain't a Milk-cow (13456) (random labels) (random labels).abc";

        // 检查括号是否匹配
        List<Integer> mismatchedPositions = checkParentheses(input);
        if (!mismatchedPositions.isEmpty()) {
            System.out.println("括号不匹配，有问题的括号位置：");
            for (int pos : mismatchedPositions) {
                System.out.println("位置：" + pos);
            }
        } else {
            System.out.println("括号匹配正常。");

            // 提取括号内外的内容
            List<String> outsideParentheses = new ArrayList<>();
            List<String> insideParentheses = new ArrayList<>();

            extractPhrases(input, outsideParentheses, insideParentheses);

            // 去除常见词
            Set<String> commonWords = new HashSet<>(Arrays.asList(
                    "the", "a", "an", "in", "on", "at", "to", "for", "with", "and", "or", "but", "if", "of", "is", "it", "this", "that"
            ));

            // 去除重复项和常见词
            outsideParentheses.removeIf(commonWords::contains);
            insideParentheses.removeIf(commonWords::contains);

            // 去除重复项
            outsideParentheses = new ArrayList<>(new LinkedHashSet<>(outsideParentheses));
            insideParentheses = new ArrayList<>(new LinkedHashSet<>(insideParentheses));

            // 输出结果
            System.out.println("括号外的内容（去重、去常见词）：");
            outsideParentheses.forEach(System.out::println);

            System.out.println("括号内的内容（去重、去常见词）：");
            insideParentheses.forEach(System.out::println);
        }
    }

    // 检查括号是否匹配
    private static List<Integer> checkParentheses(String input) {
        List<Integer> mismatchedPositions = new ArrayList<>();
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '(') {
                stack.push(i);
            } else if (c == ')') {
                if (stack.isEmpty()) {
                    mismatchedPositions.add(i);
                } else {
                    stack.pop();
                }
            }
        }

        if (!stack.isEmpty()) {
            mismatchedPositions.addAll(stack);
        }

        return mismatchedPositions;
    }

    // 提取括号内外的内容
    private static void extractPhrases(String input, List<String> outsideParentheses, List<String> insideParentheses) {
        StringBuilder currentPhrase = new StringBuilder();
        boolean inside = false;
        StringBuilder insidePhrase = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c == '(') {
                if (currentPhrase.length() > 0) {
                    outsideParentheses.add(currentPhrase.toString().trim());
                    currentPhrase.setLength(0);
                }
                inside = true;
            } else if (c == ')') {
                insidePhrase.append(c);
                insideParentheses.add(insidePhrase.toString().trim());
                insidePhrase.setLength(0);
                inside = false;
            } else if (inside) {
                insidePhrase.append(c);
            } else if (c == ' ' || c == '-') {
                if (currentPhrase.length() > 0) {
                    outsideParentheses.add(currentPhrase.toString().trim());
                    currentPhrase.setLength(0);
                }
            } else {
                currentPhrase.append(c);
            }
        }

        if (currentPhrase.length() > 0) {
            outsideParentheses.add(currentPhrase.toString().trim());
        }
    }
    public static void main(String[] args) {
        analyzeString();
    }
}
