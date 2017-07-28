package cn.demo2.manage.controller;

import cn.demo2.manage.pojo.ItemCat;
import cn.demo2.manage.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by admin on 2017/7/25.
 */
@Controller
@RequestMapping("/item/cat")
public class ItemCatController {
    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping("/list")
    @ResponseBody
    public List<ItemCat> findItemCatList(@RequestParam(value = "id",defaultValue = "0")Long parentId){
        return itemCatService.findItemCatList(parentId);
    }
}
