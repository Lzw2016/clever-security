package org.clever.security.embed.test.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.clever.security.embed.utils.ListSortUtils;
import org.junit.Test;
import org.springframework.core.Ordered;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 19:47 <br/>
 */
@Slf4j
public class ListSortUtilsTest {

    @Data
    public static final class Item implements Ordered {

        private final int order;

        public Item(int order) {
            this.order = order;
        }

        @Override
        public int getOrder() {
            return order;
        }
    }

    @Test
    public void t01() {
        List<Item> list = new ArrayList<>();
        list.add(new Item(-1));
        list.add(new Item(0));
        list.add(new Item(3));
        list.add(new Item(7));
        list.add(new Item(-13));
        list.add(new Item(100));
        list.add(new Item(56));

        log.info("--> {}", ListSortUtils.sort(list));
    }
}
