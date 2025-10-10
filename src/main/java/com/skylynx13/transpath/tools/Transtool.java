package com.skylynx13.transpath.tools;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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

    private static void sortTextLines() throws IOException {
        // 1. 从 toSort.txt 读取所有行
        List<String> lines = new ArrayList<>();
        File inFile = new File("/opt/app/transpath/conf/rename.list");
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(Files.newInputStream(inFile.toPath()), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }

        // 2. 排序（默认字典序，可自定义比较器）
        Collections.sort(lines);                       // 字典序
        // Collections.sort(lines, String.CASE_INSENSITIVE_ORDER); // 忽略大小写
        // lines.sort(Comparator.comparingInt(String::length));   // 按长度升序

        // 3. 写入 sorted.txt
        File outFile = new File("/opt/app/transpath/conf/rename.list.st");
        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(Files.newOutputStream(outFile.toPath()), StandardCharsets.UTF_8))) {
            for (String l : lines) {
                pw.println(l);
            }
        }

        System.out.println("排序完成！结果已写入 " + outFile.getAbsolutePath());
    }

    public static void main(String[] args) throws IOException {
        sortTextLines();
    }
}
