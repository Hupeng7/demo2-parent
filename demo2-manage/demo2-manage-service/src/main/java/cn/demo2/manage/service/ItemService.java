package cn.demo2.manage.service;

import cn.demo2.common.service.BaseService;
import cn.demo2.common.vo.EasyUIResult;
import cn.demo2.manage.mapper.ItemDescMapper;
import cn.demo2.manage.mapper.ItemMapper;
import cn.demo2.manage.pojo.Item;
import cn.demo2.manage.pojo.ItemDesc;
import com.github.pagehelper.PageHelper;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/7/24.
 */
@Service
public class ItemService extends BaseService<Item> {
    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemDescMapper itemDescMapper;


    /**
     * 查询所有商品列表
     *
     * @param page
     * @param rows
     * @return
     */
    public EasyUIResult findItemList(int page, int rows) {
        PageHelper.startPage(page, rows);

        List<Item> itemList = itemMapper.findItemList();

        PageInfo<Item> info = new PageInfo<Item>(itemList);

        return new EasyUIResult(info.getTotal(), info.getList());
    }

    //根据商品分类id查询商品分类名称
    public String findItemCatName(Long itemCatId) {
        return itemMapper.findItemCatName(itemCatId);
    }

    //新增商品信息
    public void saveItem(Item item, String desc) {
        //插入商品信息
        item.setCreated(new Date());
        item.setUpdated(item.getCreated());

        //动态插入
        itemMapper.insertSelective(item);

        //插入商品描述信息
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(item.getCreated());
        itemDesc.setUpdated(item.getUpdated());
        itemDescMapper.insert(itemDesc);

    }

    public void updateItem(Item item, String desc) {
        //修改商品信息
        item.setUpdated(new Date());
        itemMapper.updateByPrimaryKeySelective(item);

        //修改商品描述信息
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        itemDesc.setUpdated(item.getUpdated());
        itemDescMapper.updateByPrimaryKeySelective(itemDesc);

    }

    public void deleteItems(Long[] ids) {
        //删除商品信息
        itemMapper.deleteByIDS(ids);
        //删除商品描述信息
        itemDescMapper.deleteByIDS(ids);
    }

    public void instockItems(Long[] ids) {
        Date date = new Date();
        for (Long id : ids
                ) {
            Item item = itemMapper.selectByPrimaryKey(id);
            item.setUpdated(date);
            item.setStatus(2);
            itemMapper.updateByPrimaryKeySelective(item);
        }
    }

    public void reshelfItems(Long[] ids) {
        Date date = new Date();
        for (Long id : ids
                ) {
            Item item = itemMapper.selectByPrimaryKey(id);
            item.setUpdated(date);
            item.setStatus(1);
            itemMapper.updateByPrimaryKeySelective(item);
        }

    }

    public ItemDesc findItemDesc(Long itemId) {
        return itemDescMapper.selectByPrimaryKey(itemId);

    }

    public List<Item> findItemListForDownload() {

        return itemMapper.findItemList();

    }

    public Integer findCount() {
        return itemMapper.findCount();
    }
}
