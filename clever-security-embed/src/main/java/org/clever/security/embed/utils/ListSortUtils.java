package org.clever.security.embed.utils;

import org.springframework.core.Ordered;

import java.util.Collections;
import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 19:42 <br/>
 */
public class ListSortUtils {

    /**
     * 集合排序(Order值从小到大排序)
     */
    public static <T extends Ordered> List<T> sort(List<T> list) {
        if (list == null) {
            return Collections.emptyList();
        }
        if (list.isEmpty()) {
            return list;
        }
        list.sort((o1, o2) -> {
            int res = o1.getOrder() - o2.getOrder();
            return Integer.compare(res, 0);
        });
        return list;
    }
}
