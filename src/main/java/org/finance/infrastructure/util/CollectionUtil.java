package org.finance.infrastructure.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author jiangbangfa
 */
public class CollectionUtil {

    public static <KEY, NODE> List<NODE> transformTree(List<NODE> list, Function<NODE, KEY> keyFunction,
                                                       Function<NODE, KEY> parentKeyFunction, Function<NODE, List<NODE>> childrenFunction,
                                                       BiConsumer<NODE, List<NODE>> setChildrenConsumer) {
        Map<KEY, NODE> map = new LinkedHashMap<>();
        list.forEach(node -> {
            map.put(keyFunction.apply(node), node);
        });
        List<NODE> treeList = new ArrayList<>();
        list.forEach(node -> {
            NODE parentNode = map.get(parentKeyFunction.apply(node));
            if (parentNode == null) {
                treeList.add(node);
                return;
            }
            List<NODE> children = childrenFunction.apply(parentNode);
            if (children == null) {
                children = new ArrayList<>();
                setChildrenConsumer.accept(parentNode, children);
            }
            children.add(node);
        });
        return treeList;
    }

    public static <KEY, NODE> List<NODE> orderByTree(List<NODE> list, Function<NODE, KEY> keyFunction,
                                                     Function<NODE, KEY> parentKeyFunction,
                                                     Function<NODE, List<NODE>> childrenFunction,
                                                     BiConsumer<NODE, List<NODE>> setChildrenConsumer) {
        List<NODE> tree = transformTree(list, keyFunction, parentKeyFunction, childrenFunction, setChildrenConsumer);
        List<NODE> result = new ArrayList<>();
        flatTree(tree, result, childrenFunction);
        return result;
    }

    public static <NODE> void flatTree(List<NODE> list, List<NODE> result, Function<NODE, List<NODE>> childrenFunction) {
        if (list == null) {
            return;
        }
        list.stream().sorted(Comparator.comparing(Object::toString))
            .forEach(node -> {
                result.add(node);
                List<NODE> children = childrenFunction.apply(node);
                flatTree(children, result, childrenFunction);
            });
    }

    /**
     * 集合去重
     *
     * @return
     */
    public static <E> List<E> distinct(Collection<E> collection) {
        return distinct(collection, tmp -> tmp);
    }

    /**
     * 按照指定集合元素的内容去重
     *
     * @param collection
     * @param uidFunc    (集合元素) -> 元素的内容
     * @return
     */
    public static <E, PK> List<E> distinct(Collection<E> collection, Function<E, PK> uidFunc) {
        Objects.requireNonNull(collection);
        Set<PK> set = new LinkedHashSet<>();
        return collection.stream().filter(ele -> {
            PK pk = uidFunc.apply(ele);
            if (set.contains(pk)) {
                return false;
            }
            set.add(pk);
            return true;
        }).collect(Collectors.toList());
    }

    /**
     * 将集合转换成Map 1:1
     *
     * @return
     */
    public static <K, E> Map<K, E> toMap(Collection<E> list, Function<E, K> keyFunc) {
        Objects.requireNonNull(list);
        Map<K, E> map = new LinkedHashMap<>();
        list.forEach(tmp -> map.put(keyFunc.apply(tmp), tmp));
        return map;
    }

    /**
     * 将集合转换成Map 1:n
     *
     * @param keyFunc
     * @return
     */
    public static <K, E> Map<K, Collection<E>> toMapGroup(Collection<E> list, Function<E, K> keyFunc) {
        Objects.requireNonNull(list);
        Objects.requireNonNull(keyFunc);
        Map<K, Collection<E>> map = new LinkedHashMap<>();
        list.forEach(ele -> {
            K key = keyFunc.apply(ele);
            Collection<E> collection = map.computeIfAbsent(key, k -> new ArrayList<>());
            collection.add(ele);
        });
        return map;
    }

    /**
     * 提取target集合不在source集合中的元素
     *
     * @param sourceKeyFunc source元素的标识
     * @param targetKeyFunc target元素的标识
     * @param <K>           元素的标识
     * @return
     */
    public static <S, T, K> Collection<T> extractNotInSource(Collection<S> source, Function<S, K> sourceKeyFunc,
                                                             Collection<T> target, Function<T, K> targetKeyFunc) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);
        Collection<T> addCollections = new ArrayList<>();
        target.forEach(targetEle -> {
            boolean matchFlag = source.stream().anyMatch(sourceEle ->
                    Objects.equals(
                            sourceKeyFunc == null ? sourceEle : sourceKeyFunc.apply(sourceEle),
                            targetKeyFunc == null ? targetEle : targetKeyFunc.apply(targetEle)
                    )
            );
            if (!matchFlag) {
                addCollections.add(targetEle);
            }
        });
        return addCollections;
    }

    /**
     * 获取source和target集合不相交的元素
     *
     * @param keyFunc 集合元素的标识
     * @param <K>     元素的标识
     * @return
     */
    public static <E, K> AddOrDelCollection<E> extractOfUnIntersection(Collection<E> source, Collection<E> target,
                                                                       Function<E, K> keyFunc) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);
        Collection<E> addCollections = extractNotInSource(source, target, keyFunc);
        Collection<E> delCollections = extractNotInSource(target, source, keyFunc);
        return new AddOrDelCollection<>(addCollections, delCollections);
    }

    /**
     * 获取source和target集合不相交的元素
     * 元素标识 = E元素
     *
     * @return
     */
    public static <E> AddOrDelCollection<E> extractOfUnIntersection(Collection<E> source, Collection<E> target) {
        return extractOfUnIntersection(source, target, null);
    }

    /**
     * 提取target集合不在source集合中的元素
     *
     * @param <K> target和source的元素的标识
     * @return
     */
    public static <E, K> Collection<E> extractNotInSource(Collection<E> source, Collection<E> target, Function<E, K> keyFunc) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);
        return extractNotInSource(source, keyFunc, target, keyFunc);
    }

    /**
     * 提取target集合不在source集合中的元素
     * 元素标识 = E元素
     *
     * @return
     */
    public static <E> Collection<E> extractNotInSource(Collection<E> source, Collection<E> target) {
        return extractNotInSource(source, target, null);
    }

    public static class AddOrDelCollection<E> {
        private final Collection<E> addCollections;
        private final Collection<E> delCollections;

        public AddOrDelCollection(Collection<E> addCollections, Collection<E> delCollections) {
            this.addCollections = addCollections;
            this.delCollections = delCollections;
        }

        public void apply(BiConsumer<Collection<E>, Collection<E>> consumer) {
            consumer.accept(this.addCollections, this.delCollections);
        }
    }

}
