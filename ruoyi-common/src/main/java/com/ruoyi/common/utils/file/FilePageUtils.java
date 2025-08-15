package com.ruoyi.common.utils.file;

import com.itextpdf.text.pdf.PdfReader;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

/**
 * 文件页数工具类
 *
 */
public class FilePageUtils {

    /**
     * 计算文件的页数
     *
     * @param fileInputStream
     * @param suffix
     * @return
     */
    public static Integer countPage(InputStream fileInputStream, String suffix) {
        if ("pdf".equals(suffix)) {
            return countPdfPage(fileInputStream);
        } else if ("xls".equals(suffix) || "xlsx".equals(suffix)) {
            return countExcelPage(fileInputStream, suffix);
        }
        return null;
    }

    /**
     * 计算PDF格式文档的页数
     *
     * @param fileInputStream
     * @return
     */
    private static Integer countPdfPage(InputStream fileInputStream) {
        Integer pageCount = 0;
        PdfReader reader = null;
        try {
            reader = new PdfReader(fileInputStream);
            pageCount = reader.getNumberOfPages();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            reader.close();
        }
        return pageCount;
    }


    /**
     * 计算EXCEL格式文档的页数
     *
     * @param inputStream
     * @param suffix
     * @return
     */
    private static Integer countExcelPage(InputStream inputStream, String suffix) {
        Integer totalPageCount = 0;
        Workbook workbook = null;

        try {
            if ("xls".equals(suffix)) {
                workbook = new HSSFWorkbook(inputStream);
            } else if ("xlsx".equals(suffix)) {
                workbook = new XSSFWorkbook(inputStream);
            } else {
                throw new IllegalArgumentException("Unsupported file type: " + suffix);
            }

            int sheets = workbook.getNumberOfSheets();
            for (int i = 0; i < sheets; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                int rows = sheet.getLastRowNum() + 1; // 获取行数，包括空行

                // 这里的每页行数是一个估算值，需要根据实际情况调整
                // 假设每页可以打印50行（这个值需要根据页面设置、字体大小等调整）
                int rowsPerPage = 100;

                // 计算当前工作表的页数（向上取整）
                int sheetPageCount = (rows + rowsPerPage - 1) / rowsPerPage;

                // 累加到总页数
                totalPageCount += sheetPageCount;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    // 忽略关闭Workbook时的异常
                }
            }
        }

        return totalPageCount;
    }
}