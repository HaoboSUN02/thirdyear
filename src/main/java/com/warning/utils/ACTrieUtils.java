package com.warning.utils;

import java.util.*;

/**
 * 多模字符串匹配算法
 * 原理
 * <p>
 * 在一般的情况下，针对一个文本进行关键词匹配，在匹配的过程中要与每个关键词一一进行计算。也就是说，每与一个关键词进行匹配，都要重新从文档的开始到结束进行扫描。AC自动机的思想是，在开始时先通过词表，对以下三种情况进行缓存：
 * <p>
 * 按照字符转移成功进行跳转（success表）
 * <p>
 * 按照字符转移失败进行跳转（fail表）
 * <p>
 * 匹配成功输出表（output表）
 * <p>
 * 因此在匹配的过程中，无需从新从文档的开始进行匹配，而是通过缓存直接进行跳转，从而实现近似于线性的时间复杂度。
 * <p>
 * 构建
 * <p>
 * 构建的过程分三个步骤，分别对success表，fail表，output表进行构建。其中output表在构建sucess和fail表进行都进行了补充。fail表是一对一的，output表是一对多的。
 * <p>
 * 按照字符转移成功进行跳转（success表）
 * <p>
 * sucess表实际就是一棵trie树，构建的方式和trie树是一样的，这里就不赘述。
 * <p>
 * 按照字符转移失败进行跳转（fail表）
 * <p>
 * 设这个节点上的字母为C，沿着他父亲的失败指针走，直到走到一个节点，他的儿子中也有字母为C的节点。然后把当前节点的失败指针指向那个字母也为C的儿子。如果一直走到了root都没找到，那就把失败指针指向root。使用广度优先搜索BFS，层次遍历节点来处理，每一个节点的失败路径。
 * <p>
 * 匹配成功输出表（output表）
 * <p>
 * 匹配
 * <p>
 * 举例说明，按顺序先后添加关键词he，she，,his，hers。在匹配ushers过程中。先构建三个表，如下图，实线是sucess表，虚线是fail表，结点后的单词是ourput表。
 */
public class ACTrieUtils {
    private Boolean failureStatesConstructed = false;
    //是否建立了failure表
    private Node root;

    //根结点
    public ACTrieUtils() {
        this.root = new Node(true);
    }

    /**
     * 添加一个模式串
     *
     * @param keyword
     */
    public void addKeyword(String keyword) {
        if (keyword == null || keyword.length() == 0) {
            return;
        }
        Node currentState = this.root;
        for (Character character : keyword.toCharArray()) {
            currentState = currentState.insert(character);
        }
        currentState.addEmit(keyword);
    }

    /**
     * 模式匹配
     *
     * @param text 待匹配的文本
     * @return 匹配到的模式串
     */
    public Collection<Emit> parseText(String text) {
        checkForConstructedFailureStates();
        Node currentState = this.root;
        List<Emit> collectedEmits = new ArrayList<>();
        for (int position = 0; position < text.length(); position++) {
            Character character = text.charAt(position);
            currentState = currentState.nextState(character);
            Collection<String> emits = currentState.emit();
            if (emits == null || emits.isEmpty()) {
                continue;
            }
            for (String emit : emits) {
                collectedEmits.add(new Emit(position - emit.length() + 1, position, emit));
            }
        }
        return collectedEmits;
    }

    /**
     * 检查是否建立了failure表
     */
    private void checkForConstructedFailureStates() {
        if (!this.failureStatesConstructed) {
            constructFailureStates();
        }
    }

    /**
     * 建立failure表
     */
    private void constructFailureStates() {
        Queue<Node> queue = new LinkedList<>();
        // 第一步，将深度为1的节点的failure设为根节点
        //特殊处理：第二层要特殊处理，将这层中的节点的失败路径直接指向父节点(也就是根节点)。
        for (Node depthOneState : this.root.children()) {
            depthOneState.setFailure(this.root);
            queue.add(depthOneState);
        }
        this.failureStatesConstructed = true;
        // 第二步，为深度 > 1 的节点建立failure表，这是一个bfs 广度优先遍历
        /**
         * 构造失败指针的过程概括起来就一句话：设这个节点上的字母为C，沿着他父亲的失败指针走，直到走到一个节点，他的儿子中也有字母为C的节点。
         * 然后把当前节点的失败指针指向那个字母也为C的儿子。如果一直走到了root都没找到，那就把失败指针指向root。
         * 使用广度优先搜索BFS，层次遍历节点来处理，每一个节点的失败路径。　　
         */
        while (!queue.isEmpty()) {
            Node parentNode = queue.poll();
            for (Character transition : parentNode.getTransitions()) {
                Node childNode = parentNode.find(transition);
                queue.add(childNode);
                Node failNode = parentNode.getFailure().nextState(transition);
                childNode.setFailure(failNode);
                childNode.addEmit(failNode.emit());
            }
        }
    }

    private static class Node {
        private Map<Character, Node> map;
        private List<String> emits;
        //输出
        private Node failure;
        //失败中转
        private Boolean isRoot = false;

        //是否为根结点
        public Node() {
            map = new HashMap<>();
            emits = new ArrayList<>();
        }

        public Node(Boolean isRoot) {
            this();
            this.isRoot = isRoot;
        }

        public Node insert(Character character) {
            Node node = this.map.get(character);
            if (node == null) {
                node = new Node();
                map.put(character, node);
            }
            return node;
        }

        public void addEmit(String keyword) {
            emits.add(keyword);
        }

        public void addEmit(Collection<String> keywords) {
            emits.addAll(keywords);
        }

        /**
         * success跳转
         *
         * @param character
         * @return
         */
        public Node find(Character character) {
            return map.get(character);
        }

        /**
         * 跳转到下一个状态
         *
         * @param transition 接受字符
         * @return 跳转结果
         */
        private Node nextState(Character transition) {
            Node state = this.find(transition);
            // 先按success跳转
            if (state != null) {
                return state;
            }
            //如果跳转到根结点还是失败，则返回根结点
            if (this.isRoot) {
                return this;
            }
            // 跳转失败的话，按failure跳转
            return this.failure.nextState(transition);
        }

        public Collection<Node> children() {
            return this.map.values();
        }

        public void setFailure(Node node) {
            failure = node;
        }

        public Node getFailure() {
            return failure;
        }

        public Set<Character> getTransitions() {
            return map.keySet();
        }

        public Collection<String> emit() {
            return this.emits == null ? Collections.<String>emptyList() : this.emits;
        }
    }

    /**
     * 模式串匹配结果对象
     */
    public static class Emit {
        private final String keyword;
        //匹配到的模式串
        private final int start;
        private final int end;

        /**
         * 构造一个模式串匹配结果
         *
         * @param start   起点
         * @param end     重点
         * @param keyword 模式串
         */
        public Emit(final int start, final int end, final String keyword) {
            this.start = start;
            this.end = end;
            this.keyword = keyword;
        }

        /**
         * 获取对应的模式串
         *
         * @return 模式串
         */
        public String getKeyword() {
            return keyword;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        @Override
        public String toString() {
            return "Emit{" +
                    "keyword='" + keyword + '\'' +
                    ", start=" + start +
                    ", end=" + end +
                    '}';
        }
    }

    public static void main(String[] args) {
        ACTrieUtils trie = new ACTrieUtils();
        trie.addKeyword("hers");
        trie.addKeyword("his");
        trie.addKeyword("she");
        trie.addKeyword("he");
        Collection<Emit> emits = trie.parseText("ushers");
        for (Emit emit : emits) {
            System.out.println(emit.start + " " + emit.end + "\t" + emit.getKeyword());
        }
    }
}