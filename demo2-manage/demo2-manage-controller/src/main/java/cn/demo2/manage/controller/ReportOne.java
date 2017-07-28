package cn.demo2.manage.controller;



import cn.demo2.manage.pojo.Item;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * POI HSSFWorkbook生成报表
 * Created by admin on 2017/7/26.
 */
public class ReportOne {
    public static void PoiWriteExcel(List<Item> itemList, String fullPath, OutputStream os){

        try {
            int sheetNum = 1;  //工作簿sheet表
            int bodyRowCount =1; //正文内容行号
            int currentRowCount =1; //当前的行号
            int perPageNum = 1000;  //每个工作簿显示1000条数据
            //os = new BufferedOutputStream(response.getOutputStream());  //输出流
            HSSFWorkbook workbook = new HSSFWorkbook();  //创建excel
            HSSFSheet sheet = workbook.createSheet("item"+sheetNum); //创建一个工作簿
            setSheetColumn(sheet);  //设置工作簿列宽
            HSSFRow row = null; //创建一行
            HSSFCell cell = null;  //每个单元格

            HSSFCellStyle titleCellStyle = createTitleCellStyle(workbook);
            writeTitleContent(sheet,titleCellStyle);  //写入标题

            //第二行开始写入数据
            HSSFCellStyle bodyCellStyle = createBodyCellStyle(workbook);
            HSSFCellStyle dateBodyCellStyle = createDateBodyCellStyle(workbook);
            for (int i = 0; i < itemList.size(); i++) {
                //正文内容
                row = sheet.createRow(bodyRowCount);
                //第二行开始写正文内容
                cell = row.createCell((short)0);
                cell.setCellStyle(bodyCellStyle);
                cell.setCellValue(currentRowCount);
                cell = row.createCell((short)1);
                cell.setCellStyle(bodyCellStyle);
                cell.setCellValue(itemList.get(i).getId());
                cell = row.createCell((short)2);
                cell.setCellStyle(bodyCellStyle);
                cell.setCellValue(itemList.get(i).getTitle());
                cell = row.createCell((short)3);
                cell.setCellStyle(bodyCellStyle);
                cell.setCellValue(itemList.get(i).getCid());
                cell = row.createCell((short)4);
                cell.setCellStyle(bodyCellStyle);
                cell.setCellValue(itemList.get(i).getSellPoint());
                cell = row.createCell((short)5);
                cell.setCellStyle(bodyCellStyle);
                cell.setCellValue(itemList.get(i).getPrice());
                cell = row.createCell((short)6);
                cell.setCellStyle(bodyCellStyle);
                cell.setCellValue(itemList.get(i).getNum());
                cell = row.createCell((short)7);
                cell.setCellStyle(bodyCellStyle);
                cell.setCellValue(itemList.get(i).getBarcode());
                cell = row.createCell((short)8);
                cell.setCellStyle(bodyCellStyle);
                cell.setCellValue(itemList.get(i).getStatus());
                cell = row.createCell((short)9);
                cell.setCellStyle(dateBodyCellStyle);
                cell.setCellValue(itemList.get(i).getCreated());
                cell = row.createCell((short)10);
                cell.setCellStyle(dateBodyCellStyle);
                cell.setCellValue(itemList.get(i).getUpdated());

                if (currentRowCount % perPageNum ==0){
                    sheet = null;
                    sheetNum++;
                    sheet = workbook.createSheet("item"+sheetNum);
                    setSheetColumn(sheet);
                    bodyRowCount = 0;
                    writeTitleContent(sheet,titleCellStyle);
                }
                bodyRowCount++;
                currentRowCount++;
            }
            workbook.write(os);
            os.flush();

            //在本地磁盘写入一份数据
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fullPath));
            workbook.write(bos);
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置正文单元样式
     * @param workbook
     * @return
     */
    public static HSSFCellStyle createBodyCellStyle(HSSFWorkbook workbook){
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short)8);
        font.setFontName(HSSFFont.FONT_ARIAL);

        cellStyle.setFont(font);
        cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        return cellStyle;
    }


    /**
     * 设置正文时间样式
     * @param workbook
     * @return
     */
    public static HSSFCellStyle createDateBodyCellStyle(HSSFWorkbook workbook){
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 8);
        font.setFontName(HSSFFont.FONT_ARIAL);
        cellStyle.setFont(font);
        cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        HSSFDataFormat format = workbook.createDataFormat();
        cellStyle.setDataFormat(format.getFormat("yyyy-MM-dd"));
        return cellStyle;
    }

    /**
     * 设置标题单元样式
     * @param workbook
     * @return
     */
    public static HSSFCellStyle createTitleCellStyle(HSSFWorkbook workbook){
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 10);
        font.setFontName("黑体");  //设置标题字体
        font.setBold(true);

        cellStyle.setFont(font);
        cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);   //设置列标题样式
        //cellStyle.setFillBackgroundColor(HSSFColor.SKY_BLUE.index);
        //cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        //cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//水平布局：居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); //水平布局：居中
        cellStyle.setWrapText(true);
        return cellStyle;
    }

    /**
     * 写入标题行
     * @param sheet
     * @param cellStyle
     */
    public static void writeTitleContent(HSSFSheet sheet, HSSFCellStyle cellStyle){
        HSSFRow row = null;
        HSSFCell cell = null;
        //标题
        row = sheet.createRow(0);
        //第一行写入标题
        cell = row.createCell((short)0);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("序号");
        cell = row.createCell((short)1);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("商品ID");
        cell = row.createCell((short)2);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("商品标题");
        cell = row.createCell((short)3);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("叶子类目");
        cell = row.createCell((short)4);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("卖点");
        cell = row.createCell((short)5);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("价格");
        cell = row.createCell((short)6);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("库存数量");
        cell = row.createCell((short)7);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("条形码");
        cell = row.createCell((short)8);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("状态");
        cell = row.createCell((short)9);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("创建日期");
        cell = row.createCell((short)10);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("更新日期");
    }


    /**
     * 设置列宽
     * @param sheet
     */
    public static void setSheetColumn(HSSFSheet sheet){
        sheet.setColumnWidth(1,13*256);
        sheet.setColumnWidth(2,38*256);
        sheet.setColumnWidth(3,6*256);
        sheet.setColumnWidth(4,25*256);
        sheet.setColumnWidth(9,13*256);
        sheet.setColumnWidth(10,13*256);

    }
}
