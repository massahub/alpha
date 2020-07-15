package com.massa.alpha.util;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFileUtil {
    private static final Logger logger = LoggerFactory.getLogger(ZipFileUtil.class);

    // zip으로 묶을 대상 파일이 위치한 경로
    static String filePath = "/Users/hsy9005/Documents/";

    // zip으로 묶어서 서버에 저장 될 경로+파일명
    static String zipPath = "/Users/hsy9005/Documents/mobilepark.zip";

    // 다운받을 파일명을 지정하지 않았을 경우 default name
    static String defaultDownloadFileName = "download.zip";

    /*   public static void createZipFile() throws Exception{
           String path = "/Users/hsy9005/Documents";

           FileOutputStream zipFileOutputStream = new FileOutputStream(path + "/files.zip");
           ZipOutputStream zipOutputStream = new ZipOutputStream(zipFileOutputStream);

           ZipEntry zipEntry = new ZipEntry("test.txt");
           zipOutputStream.putNextEntry(zipEntry);
           zipOutputStream.write("모바일파크\n파일 압축 테스트".getBytes(StandardCharsets.UTF_8));

           ZipEntry zipEntry2 = new ZipEntry("test2.txt");
           zipOutputStream.putNextEntry(zipEntry2);
           zipOutputStream.write("두번째파일 \n 모바일파크 파일 압축 테스트".getBytes(StandardCharsets.UTF_8));

           zipOutputStream.close();
           zipFileOutputStream.close();

       }*/

    /**
     * Controller단에서 호출 되는 method
     * filename 전달 여부에 따라 호출하는 method가 달라진다.
     *
     * @param response
     * @param sourceFiles
     * @throws Exception
     */
    public static void downloadZip(HttpServletResponse response, List<String> sourceFiles) throws Exception{
        createZipFile(response, sourceFiles, null);
    }

    public static void downloadZip(HttpServletResponse response, List<String> sourceFiles, String fileName) throws Exception{
        createZipFile(response, sourceFiles, fileName);
    }

    public void downloadZip(List<String> fileUrl, String zipFile) {

        createZip(fileUrl,zipFile);
    }

    /**
     * filename을 전달 받았을 경우 호출 되는 method
     *
     * @param response
     * @param sourceFiles
     * @param fileName
     * @throws Exception
     */
    public static void createZipFile(HttpServletResponse response, List<String> sourceFiles, String fileName) throws Exception{
        try{
            FileOutputStream zipFileOutputStream = new FileOutputStream(zipPath);
            ZipOutputStream zipOutputStream = new ZipOutputStream(zipFileOutputStream);

            for(int i=0; i < sourceFiles.size(); i++){
                String sourceFile = filePath+sourceFiles.get(i);

                ZipEntry zipEntry = new ZipEntry(new File(sourceFile).getName());
                zipOutputStream.putNextEntry(zipEntry);

                FileInputStream fin = new FileInputStream(sourceFile);
                byte[] buffer = new byte[1024];
                int length;

                while((length = fin.read(buffer)) > 0){
                    zipOutputStream.write(buffer, 0, length);
                }

                zipOutputStream.closeEntry();
                fin.close();
            }

            zipOutputStream.close();

            if(StringUtils.isBlank(fileName)){
                fileName = defaultDownloadFileName;
            }

            response.setContentType("application/zip");
            if(fileName.indexOf(".zip") == -1){
                fileName = fileName+".zip";
            }
            response.addHeader("Content-Disposition", "attachment; filename="+fileName);

            FileInputStream fis=new FileInputStream(zipPath);
            BufferedInputStream bis=new BufferedInputStream(fis);
            ServletOutputStream so=response.getOutputStream();
            BufferedOutputStream bos=new BufferedOutputStream(so);

            byte[] data=new byte[2048];
            int input=0;

            while((input=bis.read(data))!=-1){
                bos.write(data,0,input);
                bos.flush();
            }

            if(bos!=null) bos.close();
            if(bis!=null) bis.close();
            if(so!=null) so.close();
            if(fis!=null) fis.close();


        } catch(IOException ioe){ }
    }

    private void createZip(List<String> fileUrl, String zipFile) { // 20200427 수정 hjs


        FileInputStream fileInputStream = null;
        ZipOutputStream zipOutputStream = null;

        try {
            // ZipOutputStream을 FileOutputStream 으로 감쌈
            zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));

            for (String fileUrlSize : fileUrl) {

                ZipEntry zipEntry = new ZipEntry(new File(fileUrlSize).getName());
                zipOutputStream.putNextEntry(zipEntry);

                if (!fileUrlSize.equals("/Users/gksjs3468/Downloads/fileupload/null")) {

                    fileInputStream = new FileInputStream(fileUrlSize);
                    byte[] buffer = new byte[1024];
                    int length;

                    while ((length = fileInputStream.read(buffer)) > 0) {
                        zipOutputStream.write(buffer, 0, length);
                    }
                    zipOutputStream.closeEntry();
                    fileInputStream.close();
                }
            }
            zipOutputStream.close();

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {

            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            if (zipOutputStream != null) {
                try {
                    zipOutputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }


    }
}
