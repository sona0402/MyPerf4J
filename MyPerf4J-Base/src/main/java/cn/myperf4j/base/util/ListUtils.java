package cn.myperf4j.base.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LinShunkang on 2020/05/16
 */
public final class ListUtils {

    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }

    public static <T> boolean isNotEmpty(List<T> list) {
        return !isEmpty(list);
    }

    public static <T> List<List<T>> partition(List<T> list, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException(size + " <= 0");
        }

        if (list == null || list.size() == 0) {
            return new ArrayList<>(0);
        }

        List<List<T>> result = new ArrayList<>(list.size() / size + 1);
        for (int fromIndex = 0; fromIndex < list.size(); ) {
            List<T> subList = getSubList(list, fromIndex, size);
            result.add(subList);
            fromIndex += size;
        }
        return result;
    }

    public static <T> List<T> getSubList(List<T> list, int fromIndex, int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException(limit + " <= 0");
        }

        int toIndex = Math.min(list.size(), fromIndex + limit);
        return list.subList(fromIndex, toIndex);
    }

}
