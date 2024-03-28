package com.kenn.book.utils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: 集合工具类
 * date: 2021/12/27 11:36
 *
 * @author 18305
 */
public final class CollectionUtils {

    private CollectionUtils() {
    }

    /**
     * @param data:      要分组的集合(M)
     * @param pieceSize: 每组的大小(N)
     * @return java.util.List<java.util.List < T>>
     * @Description M个元素的集合每N个分成一组
     * @author 18305
     * @Datetime 2021/12/27 10:36
     */
    public static <T> List<List<T>> groupOnPieceSize(Collection<T> data, int pieceSize) {
        if (pieceSize <= 0) {
            throw new IllegalArgumentException("参数错误");
        }
        if (StringUtils.isEmpty(data)) {
            return new ArrayList<>(0);
        }
        List<List<T>> resultList = new ArrayList<>(data.size());
        for (int i = 0; i < data.size(); i += pieceSize) {
            resultList.add(data.stream().skip(i).limit(pieceSize).collect(Collectors.toList()));
        }
        return resultList;
    }

}
