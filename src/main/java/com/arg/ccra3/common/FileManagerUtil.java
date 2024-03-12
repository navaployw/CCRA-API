package com.arg.ccra3.common;

import java.io.File;

public class FileManagerUtil {
    private FileManagerUtil(){}
    public static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) 
            for (File file : allContents) 
                deleteDirectory(file);
            
        return directoryToBeDeleted.delete();
    }
    public static File createDirIfNotExist(String dirPath){
        File dir = new File(dirPath);
        if(!dir.exists())
            dir.mkdirs();
        return dir;
    }
}
