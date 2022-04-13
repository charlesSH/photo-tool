package com.example;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

import static java.nio.file.StandardCopyOption.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws Exception {
        System.out.println( "Hello World!" );
        String path = "D:\\Download"; // 要遍历的路径
        File downloadDir = new File(path); // 获取其file对象
        File[] files = downloadDir.listFiles(); // 遍历path下的文件和目录，放在File数组中
        for (File file : files) { // 遍历File[]数组
            if (!file.isDirectory()) {
                System.out.println("------------------------");
                System.out.println(file); // 若非目录(即文件)，则打印
                Metadata metadata;
                try {
                    Boolean jumpFlag = false;
                    metadata = ImageMetadataReader.readMetadata(file);
                    for (Directory directory : metadata.getDirectories()) {
                        for (Tag tag : directory.getTags()) {
                            String suffix = file.getName().substring(file.getName().lastIndexOf("."));
                            if(suffix.contains("MOV")){
                                System.out.format("[%s] - %s = %s", directory.getName(), tag.getTagName(), tag.getDescription());
                                System.out.println();
                            }
                            if(tag.getTagName().contains("Date/Time")){
                                System.out.format("[%s] - %s = %s", directory.getName(), tag.getTagName(), tag.getDescription());
                                System.out.println();
                                String newName = tag.getDescription().replace(":", "").replace(" ", "");

                                File targetFile = new File("D:\\Output\\" + newName + suffix);
                                Files.copy(file.toPath(), targetFile.toPath(), REPLACE_EXISTING);
                                jumpFlag = true;
                                break;
                            }
                        }
                        if(jumpFlag){
                            break;
                        }
                        if (directory.hasErrors()) {
                            for (String error : directory.getErrors()) {
                                System.err.format("ERROR: %s", error);
                            }
                        }
                    }
                    if(!jumpFlag){
                        System.out.println("!!!!!!!!!!!!!!!!!!!!");
                    }
                } catch (ImageProcessingException | IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
    }
}
