package com.helun.menu.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.opencsv.CSVWriter;

public class CSVUtil {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(CSVUtil.class);

    public static boolean exportCsv(List<String[]> dataList, String filePath,Boolean over) throws Exception {
        boolean isSuccess = false;

        CSVWriter writer = null;

        OutputStreamWriter out = null;

        try {

            String exprtDir = filePath.substring(0, filePath.lastIndexOf("export") + "export".length());
            File dir = new File(exprtDir);
            if (!dir.exists()) {
                boolean ret = dir.mkdir();
                if (!ret) {
                    LOG.warn("Fail to make directory 'export' by automatic,Please create it in your web root directory.");
                }
            }

            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }

            out = new OutputStreamWriter(new FileOutputStream(filePath,!over), Charset.forName("UTF-8"));
             //CSVWriter.DEFAULT_SEPARATOR
            writer = new CSVWriter(out, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);


            for(String[] nextLine : dataList) {
            	writer.writeNext(nextLine);
            	writer.flush();
            }

            isSuccess = true;

        } catch (Exception e) {

            System.out.println("生成CSV出错..." + e);

            throw e;

        } finally {

            if (null != writer) {

                writer.close();

            }

            if (null != out) {

                try {

                    out.close();

                } catch (IOException e) {

                    System.out.println("exportCsv close Exception: " + e);

                    throw e;
                }
            }
        }

        return isSuccess;
    }
    
    
    public static boolean exportCsv(List<String[]> dataList, String filePath) throws Exception {
    	return exportCsv(dataList, filePath, false);
    }
}
