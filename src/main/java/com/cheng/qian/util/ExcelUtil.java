package com.cheng.qian.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * ExcelUtils导出数据
 * 
 * @author 
 * 
 */
public class ExcelUtil {

    private static final int MAX_ROWS = 60000;

    public static void main(String[] args) {
        List<List<String>> data = new ArrayList<List<String>>();
        List<String> a1 = new ArrayList<String>();
        List<String> a2 = new ArrayList<String>();
        List<String> a3 = new ArrayList<String>();
        List<String> a4 = new ArrayList<String>();

        a1.add("ID");
        a1.add("名称");
        a2.add("1");
        a2.add("test1");
        a3.add("2");
        a3.add("test2");
        a4.add("3");
        a4.add("xulixin");

        data.add(a2);
        data.add(a3);
        data.add(a4);
        try {
            exportDataToExcel(a1, data, new FileOutputStream("/Users/yanhechao/Documents/test.xls"),
                "test.xls", "test", "c,测试", null, null, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出数据至Excel文件
     * 
     * @param colsNames
     *            列名
     * @param data
     *            数据源
     * @param OutputStream
     *            输出流
     * @param filename
     *            excel文件名称
     * @param sheetname
     *            excel 工作区名称
     * @param title
     *            数据内容标题
     * @param foot
     *            数据内容底部
     * @param colmerge
     *            需要合并的列
     * @param condition
     *            指定以红色显示的内容
     * 
     * @author spring
     */
    public static void exportDataToExcel(List<String> colsNames, List<List<String>> data,
                                         OutputStream os, String filename, String sheetname,
                                         String title, String foot, String colmerge,
                                         String condition) {

        if (filename == null)
            filename = "Unidentified.xls";
        if (sheetname == null || sheetname.equals("")) {
            sheetname = "Sheet";
        }
        int width = 0;// excel总列合并宽度

        // 解析condition对应的列、值

        String[] conditions = new String[0];
        if (condition != null) {
            conditions = condition.split(",");
        }

        WritableWorkbook workbook = null;
        try {
            workbook = Workbook.createWorkbook(os);
            if (data == null || data.size() < 1) {// 没有数据时
                WritableSheet sheet = workbook.createSheet(sheetname, 0);

                // 设置字体样式
                WritableFont font = new WritableFont(WritableFont.ARIAL, 24);
                // 设置一般样式
                WritableCellFormat cf = new WritableCellFormat(font);
                cf.setAlignment(Alignment.CENTRE);// 设置居中
                cf.setVerticalAlignment(VerticalAlignment.CENTRE);// 设置垂直居中
                cf.setBorder(Border.ALL, BorderLineStyle.THIN);// 设置边框线

                int j = 0;// 行号标识
                width = colsNames.size();
                // 设置标题
                if (title != null && j == 0) {
                    font.setBoldStyle(WritableFont.BOLD);
                    Label header = new Label(0, j, title, cf);
                    sheet.mergeCells(0, j, width - 1, j);// 合并单元格
                    sheet.addCell(header);
                    j++;
                }

                // 设置列名
                WritableFont colsFont = new WritableFont(WritableFont.ARIAL);
                WritableCellFormat colscf = new WritableCellFormat(colsFont);
                colscf.setAlignment(Alignment.CENTRE);// 设置居中
                colscf.setVerticalAlignment(VerticalAlignment.CENTRE);// 设置垂直居中
                colscf.setBorder(Border.ALL, BorderLineStyle.THIN);// 设置边框线
                for (int i = 0; i < colsNames.size(); i++) {
                    // 设置指定内容的颜色 condition (e.g. key=value)
                    for (String string : conditions) {
                        int index = string.indexOf("=");
                        int column = Integer.parseInt(string.substring(0, index));
                        String value = string.substring(index + 1);
                        if (column == i && colsNames.get(i).equals(value)) {
                            colsFont.setColour(Colour.RED);
                            break;
                        }
                    }

                    // 合并指定值相同的列
                    mergeColumns(sheet, j, colsNames, i, colmerge);

                    Label cell = new Label(i, j, colsNames.get(i), colscf);
                    sheet.addCell(cell);
                    sheet.setColumnView(i, colsNames.get(i).getBytes().length);
                }
                j++;

                // 设置底部
                if (foot != null) {
                    Label footer = new Label(0, j, foot, cf);
                    sheet.mergeCells(0, j, width - 1, j);// 合并单元格
                    sheet.addCell(footer);
                }
            } else {
                if (data.size() > MAX_ROWS) {
                    int index = 1;
                    for (int i = 0; i < data.size(); i += MAX_ROWS) {
                        int row_to = data.size() - i > MAX_ROWS ? i + MAX_ROWS : data.size();
                        writeSheet(colsNames, data.subList(i, row_to), sheetname + index, title,
                            foot, colmerge, width, conditions, workbook);
                        index++;
                    }
                } else {
                    writeSheet(colsNames, data, sheetname, title, foot, colmerge, width, conditions,
                        workbook);
                }
            }

            workbook.write();
        } catch (WriteException e) {
        } catch (IOException e) {
        } finally {
            try {
                workbook.close();
            } catch (WriteException e) {
            } catch (IOException e) {
            }
        }

    }

    public static String toUtf8String(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = Character.toString(c).getBytes("utf-8");
                } catch (Exception ex) {
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0)
                        k += 256;
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }

    private static void writeSheet(List<String> colsNames, List<List<String>> data,
                                   String sheetname, String title, String foot, String colmerge,
                                   int width, String[] conditions,
                                   WritableWorkbook workbook) throws WriteException,
                                                              RowsExceededException {
        WritableSheet sheet = workbook.createSheet(sheetname, 0);

        // 设置字体样式
        WritableFont font = new WritableFont(WritableFont.ARIAL, 24);
        // 设置一般样式
        WritableCellFormat cf = new WritableCellFormat(font);
        cf.setAlignment(Alignment.CENTRE);// 设置居中
        cf.setVerticalAlignment(VerticalAlignment.CENTRE);// 设置垂直居中
        cf.setBorder(Border.ALL, BorderLineStyle.THIN);// 设置边框线

        int j = 0;// 行号标识
        width = colsNames.size();
        // 设置标题
        if (title != null && j == 0) {
            font.setBoldStyle(WritableFont.BOLD);
            Label header = new Label(0, j, title, cf);
            sheet.mergeCells(0, j, width - 1, j);// 合并单元格
            sheet.addCell(header);
            j++;
        }

        // 设置列名
        WritableFont colsFont = new WritableFont(WritableFont.ARIAL, 20);
        WritableCellFormat colscf = new WritableCellFormat(colsFont);
        colscf.setAlignment(Alignment.CENTRE);// 设置居中
        colscf.setVerticalAlignment(VerticalAlignment.CENTRE);// 设置垂直居中
        colscf.setBorder(Border.ALL, BorderLineStyle.THIN);// 设置边框线
        for (int i = 0; i < colsNames.size(); i++) {
            // 设置指定内容的颜色 condition (e.g. key=value)
            for (String string : conditions) {
                int index = string.indexOf("=");
                int column = Integer.parseInt(string.substring(0, index));
                String value = string.substring(index + 1);
                if (column == i && colsNames.get(i).equals(value)) {
                    colsFont.setColour(Colour.RED);
                    break;
                }
            }

            // 合并指定值相同的列
            mergeColumns(sheet, j, colsNames, i, colmerge);

            Label cell = new Label(i, j, colsNames.get(i), colscf);
            sheet.addCell(cell);
            sheet.setColumnView(i, colsNames.get(i).getBytes().length);
        }
        j++;

        WritableFont bodyFont = new WritableFont(WritableFont.ARIAL);
        WritableCellFormat bodycf = new WritableCellFormat(bodyFont);
        bodycf.setAlignment(Alignment.CENTRE);// 设置居中
        bodycf.setVerticalAlignment(VerticalAlignment.CENTRE);// 设置垂直居中
        bodycf.setBorder(Border.ALL, BorderLineStyle.THIN);// 设置边框线
        for (List<String> cells : data) {
            for (int i = 0; i < cells.size(); i++) {
                // 设置指定内容的颜色 condition (e.g. key=value)
                for (String string : conditions) {
                    int index = string.indexOf("=");
                    int column = Integer.parseInt(string.substring(0, index));
                    String value = string.substring(index + 1);
                    if (column == i && cells.get(i).equals(value)) {
                        bodyFont.setColour(Colour.RED);
                        break;
                    }
                }

                // 合并指定值相同的列
                mergeColumns(sheet, j, cells, i, colmerge);

                Label cell = new Label(i, j, cells.get(i), bodycf);
                sheet.addCell(cell);
            }
            j++;
        }

        // 设置底部
        if (foot != null) {
            Label footer = new Label(0, j, foot, cf);
            sheet.mergeCells(0, j, width - 1, j);// 合并单元格
            sheet.addCell(footer);
        }
    }

    /***************************************** private method ********************************************************/
    /*
     * merge cells which contains same values in column by given column number
     * 
     * @param sheet
     * 
     * @param j row number
     * 
     * @param cells cells of row
     * 
     * @param i column number
     * 
     * @param colsmerge merged column
     * 
     * @throws WriteException
     * 
     * @throws RowsExceededException
     */
    private static void mergeColumns(WritableSheet sheet, int j, List<String> cells, int i,
                                     String colsmerge) throws WriteException,
                                                       RowsExceededException {

        if (colsmerge == null)
            return;

        String[] cols = colsmerge.split(",");
        for (String col : cols) {
            if (col != null && Integer.parseInt(col) == i) {
                int mergeColl = Integer.parseInt(col);
                String preCellValue = sheet.getCell(mergeColl, j - 1).getContents();
                if (cells.get(i).equals(preCellValue)) {
                    sheet.mergeCells(mergeColl, j - 1, mergeColl, j);
                    continue;
                }
            }
        }
    }
}
