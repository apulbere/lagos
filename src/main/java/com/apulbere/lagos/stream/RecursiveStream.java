package com.apulbere.lagos.stream;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RecursiveStream {

    public List<Node> findAllWithCode(List<Node> nodes, String code) {
        return nodes.stream()
                .flatMap(this::toStream)
                .filter(n -> code.equals(n.getCode()))
                .collect(Collectors.toList());
    }

    private Stream<Node> toStream(Node node) {
        if(node != null) {
            return Stream.concat(Stream.of(node), node.getChildren().stream().flatMap(this::toStream));
        }
        return Stream.empty();
    }


    static class Node {
        private int id;
        private String code;
        private List<Node> children;

        public Node(int id, String code, List<Node> children) {
            this.id = id;
            this.code = code;
            this.children = children;
        }

        public Node(int id, String code) {
            this.id = id;
            this.code = code;
            this.children = List.of();
        }

        public List<Node> getChildren() {
            return children;
        }

        public String getCode() {
            return code;
        }
    }
}
