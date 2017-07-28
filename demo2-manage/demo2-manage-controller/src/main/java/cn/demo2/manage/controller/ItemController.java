package cn.demo2.manage.controller;

import cn.demo2.common.vo.EasyUIResult;
import cn.demo2.common.vo.SysResult;
import cn.demo2.manage.pojo.Item;
import cn.demo2.manage.pojo.ItemDesc;
import cn.demo2.manage.service.ItemService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/7/25.
 */
@Controller
@RequestMapping("/item")
public class ItemController {
    //引入日志文件  架构师可能将日志对象进行包装  静态调用
    //private static final Logger logger = Logger.getLogger(ItemController.class);
    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private ItemService itemService;

    @RequestMapping("/query")
    @ResponseBody
    public EasyUIResult findItemList(int page, int rows) {
        return itemService.findItemList(page, rows);
    }

    //根据商品分类id查询商品分类名称
    @RequestMapping("/queryItemName")
    public void quertItemName(Long itemCatId, HttpServletResponse response) {
        String name = itemService.findItemCatName(itemCatId);
        response.setContentType(("text/html;charset=utf-8"));

        try {
            response.getWriter().write(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //新增商品信息
    @RequestMapping("/save")
    @ResponseBody
    public SysResult saveItem(Item item, String desc) {
        try {
            itemService.saveItem(item, desc);
            return SysResult.build(200, "商品新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("~~~~~~~~~~~商品新增失败");
            return SysResult.build(201, "商品新增失败!请联系管理员");
        }

    }

    //修改商品信息
    @RequestMapping("/update")
    @ResponseBody
    public SysResult updateItem(Item item, String desc) {
        try {
            itemService.updateItem(item, desc);
            return SysResult.build(200, "商品修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("~~~~~~~~~~~商品修改失败");
            return SysResult.build(201, "商品修改失败!请联系管理员");
        }
    }

    //批量删除商品
    @RequestMapping("delete")
    @ResponseBody
    public SysResult deleteItems(Long[] ids) {
        try {
            itemService.deleteItems(ids);
            return SysResult.build(200, "商品删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("~~~~~~~~~~~商品删除失败");
            return SysResult.build(201, "商品删除失败!请联系管理员");
        }
    }

    //批量下架商品
    @RequestMapping("/instock")
    @ResponseBody
    public SysResult instockItems(Long[] ids) {
        try {
            itemService.instockItems(ids);
            return SysResult.build(200, "商品下架成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("~~~~~~~~~~~商品下架失败");
            return SysResult.build(201, "商品下架失败!请联系管理员");
        }
    }

    //批量上架商品
    @RequestMapping("/reshelf")
    @ResponseBody
    public SysResult reshelfItems(Long[] ids) {
        try {
            itemService.reshelfItems(ids);
            return SysResult.build(200, "商品上架成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("~~~~~~~~~~~商品上架失败");
            return SysResult.build(201, "商品上架失败!请联系管理员");
        }

    }

    //查询商品描述信息
    @RequestMapping("/desc/{itemId}")
    @ResponseBody
    public SysResult findItemDesc(@PathVariable Long itemId ){
        try {
            ItemDesc itemDesc = itemService.findItemDesc(itemId);
            return SysResult.oK(itemDesc);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("~~~~~~~~~~~查询商品描述失败"+e.getMessage());
            return SysResult.build(201, "查询商品描述失败!请联系管理员");
        }
    }

    //下载全部商品
    @RequestMapping("/download")
    //@ResponseBody
    public void download(HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.reset();// 清空response

        List<Item> itemList = itemService.findItemListForDownload();

        //System.out.println("~~~~~~~~~~~~~~~~"+itemList);

        //不需要统计总数
        //Integer count = itemService.findCount();

        //String fileLocation1 = request.getServletContext().getRealPath("/WEB-INF/report/");

        String fileLocation = request.getRealPath("/WEB-INF/report/");
        System.out.println("~~~~~~~~~~~~~~~~"+fileLocation);

        String fileName = "item_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".xlsx";

        System.out.println("~~~~~~~~~~~~~~~~"+fileName);

        /**
         * request.getServletContext().getRealPath("/WEB-INF/report/");
         * .getServletContext()这个方法在本例中失效，只好用下面的拼接，才能存到指定路径
         */
        String fullPath = fileLocation+"\\"+fileName;

        //response.setContentType("application/force-download");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);

        //Report.downloadReport(itemList,count,fullPath,response.getOutputStream());
        //ReportOne.PoiWriteExcel(itemList,fullPath,response.getOutputStream());
        ReportXSSF.PoiWriteExcel(itemList,fullPath,response.getOutputStream());


    }


}
