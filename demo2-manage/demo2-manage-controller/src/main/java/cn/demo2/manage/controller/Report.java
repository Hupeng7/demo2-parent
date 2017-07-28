package cn.demo2.manage.controller;

import cn.demo2.manage.pojo.Item;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.List;

/**
 * Created by admin on 2017/7/26.
 */
public class Report {
    public static void downloadReport(List<Item> itemList, Integer count, String fullPath, OutputStream os) throws IOException {
        int perPageNum = 1000;
        int sheetNum = 1;
        Workbook wb = new XSSFWorkbook();
        CreationHelper creationHelper = wb.getCreationHelper();

        Sheet sheet = wb.createSheet("item"+sheetNum);

        String[] titleArray = new String[]{"商品ID","商品标题","叶子类目","卖点","价格","库存数量","条形码","状态","创建日期","更新日期"};

        Row row = sheet.createRow((short)0);
        Font font = wb.createFont();
        //font.setColor(XSSFFont.COLOR_RED); //字体颜色
        font.setFontHeightInPoints((short) 10);
        font.setFontName("黑体");
        font.setBold(true);
        //font.setItalic(true); //是否使用斜体

        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THICK); //下边框
        cellStyle.setBorderLeft(BorderStyle.THICK);   //左边框
        cellStyle.setBorderTop(BorderStyle.THICK);    //上边框
        cellStyle.setBorderRight(BorderStyle.THICK);  //右边框
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);// 设置背景色
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//水平布局：居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); //水平布局：居中
        cellStyle.setWrapText(true);

        for (int i = 0; i < titleArray.length; i++) {
            Cell rr = row.createCell(i);
            rr.setCellValue(creationHelper.createRichTextString(titleArray[i]));
            rr.setCellStyle(cellStyle);
        }

        int currentRowNum = 1;
        font = wb.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setFontName("宋体");
        font.setBold(false);

        cellStyle = wb.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(true);

        for (int i = 0; i < itemList.size(); i++) {
            Item item = itemList.get(i);
            Cell cc = null;
            Row mergerRow = sheet.createRow((short)currentRowNum++);
            cc = mergerRow.createCell(0);
            cc.setCellValue(item.getId());
            cc.setCellStyle(cellStyle);
            cc = mergerRow.createCell(1);
            cc.setCellValue(item.getTitle());
            cc.setCellStyle(cellStyle);
            cc = mergerRow.createCell(2);
            cc.setCellValue(item.getCid());
            cc.setCellStyle(cellStyle);
            cc = mergerRow.createCell(3);
            cc.setCellValue(item.getSellPoint());
            cc.setCellStyle(cellStyle);
            cc = mergerRow.createCell(4);
            cc.setCellValue(item.getPrice());
            cc.setCellStyle(cellStyle);
            cc = mergerRow.createCell(5);
            cc.setCellValue(item.getNum());
            cc.setCellStyle(cellStyle);
            cc = mergerRow.createCell(6);
            cc.setCellValue(item.getBarcode());
            cc.setCellStyle(cellStyle);
            cc = mergerRow.createCell(7);
            cc.setCellValue(item.getStatus());
            cc.setCellStyle(cellStyle);
            cc = mergerRow.createCell(8);
            cc.setCellValue(item.getCreated());
            cc.setCellStyle(cellStyle);
            cc = mergerRow.createCell(9);
            cc.setCellValue(item.getUpdated());
            cc.setCellStyle(cellStyle);

            if (currentRowNum % perPageNum ==0){
                sheet = null;
                sheetNum++;
                sheet = wb.createSheet("item"+sheetNum);
                for (int j = 0; j < titleArray.length; j++) {
                    Cell rr = row.createCell(j);
                    rr.setCellValue(creationHelper.createRichTextString(titleArray[j]));
                    rr.setCellStyle(cellStyle);
                }
            }
            currentRowNum++;
        }
        sheet.setColumnWidth(0,13*256);
        sheet.setColumnWidth(1,38*256);
        sheet.setColumnWidth(2,6*256);
        sheet.setColumnWidth(3,25*256);

        wb.write(os);

        //在本地磁盘写入一份数据
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fullPath));
        wb.write(bos);
        bos.close();
    }
}
