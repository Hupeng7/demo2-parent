package cn.demo2.manage.service;

import cn.demo2.common.service.BaseService;
import cn.demo2.manage.mapper.ItemCatMapper;
import cn.demo2.manage.pojo.ItemCat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by admin on 2017/7/24.
 */
@Service
public class ItemCatService extends BaseService<ItemCat> {
    @Autowired
    private ItemCatMapper itemCatMapper;


    public List<ItemCat> findItemCatList(Long parentId) {
        /**
         * 如果传入的是对象  那么查询时就会根据对象的属性添加where 条件
         * select * from tb_item_cat where id = ?
         */
        ItemCat itemCat = new ItemCat();
        itemCat.setParentId(parentId);

        return itemCatMapper.select(itemCat);
    }
}
