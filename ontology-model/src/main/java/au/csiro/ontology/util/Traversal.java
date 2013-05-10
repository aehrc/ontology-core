
package au.csiro.ontology.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import au.csiro.ontology.IOntology;
import au.csiro.ontology.Node;

public interface Traversal {
    
    /**
     * A level-order traversal where the level is the min distance from the 
     * root.
     * <p>
     * <b>Will</b> visit descendants of {@code start} that have paths to the 
     * root not also containing {@code start}.
     */
    final public static Traversal BFS_MIN = new AbstractTraversal() {
        public <T extends Comparable<T>> void accept(Node<T> start, 
                Visitor<T>... visitors) {
            final Set<Node<T>> done = new HashSet<Node<T>>();
            final LinkedList<Node<T>> queue = new LinkedList<Node<T>>();
            queue.add(start);
            
            while (!queue.isEmpty()) {
                Node<T> node = queue.poll();
                if (!done.contains(node)) {     // don't visit more than once
                    for (Visitor<T> v: visitors) {
                        v.visit(node);
                    }
                    done.add(node);
                    queue.addAll(node.getChildren());
                }
            }
        }
    };
    
    /**
     * A level-order traversal where the level is the max distance from the root.
     * <p>
     * <b>Will not</b> visit descendants of {@code start} that have paths to the root not also containing {@code start}.
     */
    final public static Traversal BFS_MAX = new AbstractTraversal() {
        
        public <T extends Comparable<T>> void accept(Node<T> start, Visitor<T>... visitors) {
            final Set<Node<T>> done = new HashSet<Node<T>>();
            final LinkedList<Node<T>> queue = new LinkedList<Node<T>>();
            queue.add(start);
            
            while (!queue.isEmpty()) {
                Node<T> node = queue.poll();
                if (!done.contains(node)) {     // don't visit more than once
                    for (Visitor<T> v: visitors) {
                        v.visit(node);
                    }
                    done.add(node);
                    for (Node<T> child: node.getChildren()) {
                        if (done.containsAll(child.getParents())) {     // check all parents are done first
                            queue.add(child);
                        }
                    }
                }
            }
        }
    };

    public <T extends Comparable<T>> void accept(IOntology<T> ont, Visitor<T>... visitors);
    public <T extends Comparable<T>> void accept(Node<T> node, Visitor<T>... visitors);
    
    static interface Visitor<T extends Comparable<T>> {
        void visit(Node<T> node);
    }

    static class Stats {
        /**
         * Traverses the ontology to compute min and max path length to root for each node.
         * 
         * @param ont
         * @return
         */
        @SuppressWarnings("unchecked")
        public static <T extends Comparable<T>> Map<Node<T>, Object> computeStats(final IOntology<T> ont) {
            final Map<Node<T>, Object> result = new HashMap<Node<T>, Object>();
            
            final Map<Node<T>, Integer> minLevel = new HashMap<Node<T>, Integer>();
            final Map<Node<T>, Integer> maxLevel = new HashMap<Node<T>, Integer>();
            
            final Visitor<T> v = new Visitor<T>() {
                public void visit(final Node<T> node) {
                    int min;
                    int max;
                    
                    if (ont.getTopNode().equals(node)) {
                        min = max = 0;
                    } else {
                        min = Integer.MAX_VALUE;
                        max = Integer.MIN_VALUE;
                        for (Node<T> parent: node.getParents()) {
                            min = Math.min(min, minLevel.get(parent) + 1);
                            max = Math.max(max, maxLevel.get(parent) + 1);
                        }
                    }
                    minLevel.put(node, min);
                    maxLevel.put(node, max);
                    
                    result.put(node, new int[] {min, max});
                }
            };

            BFS_MAX.accept(ont, v);
            
            return result;
        }
    }
    
}

abstract class AbstractTraversal implements Traversal {
    
    public <T extends Comparable<T>> void accept(IOntology<T> ont, Visitor<T>... visitors) {
        accept(ont.getTopNode(), visitors);
    }
    
}
