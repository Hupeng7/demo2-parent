package cn.demo2.manage.mapper;

import cn.demo2.common.mapper.SysMapper;
import cn.demo2.manage.pojo.Item;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by admin on 2017/7/24.
 */
//@Component
public interface ItemMapper extends SysMapper<Item> {
    List<Item> findItemList();

    String findItemCatName(Long itemCatId);
}
