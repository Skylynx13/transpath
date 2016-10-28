package com.qxu.transpath.worker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.qxu.transpath.utils.TransLog;

 /**
 * ClassName: PathFinder <br/>
 * Description: TODO <br/>
 * Date: 2014-3-29 下午8:42:20 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */
public class PathFinder {
	public ArrayList<String> getFileNameList(String pFilePath) {
		
		ArrayList<String> fileNameList = new ArrayList<String>();

		File dirRoot = new File(pFilePath);
		File[] fileInRoot = dirRoot.listFiles();

		for (int iFile = 0; iFile<fileInRoot.length; iFile++){
			fileNameList.add(fileInRoot[iFile].getPath());
			if (fileInRoot[iFile].isDirectory()) {
				fileNameList.addAll(this.getFileNameList(fileInRoot[iFile].getPath()));
			}
		}

		return fileNameList;
	}
	
	public ArrayList<File> getFileList(String pFilePath) {
		
		ArrayList<File> fileList = new ArrayList<File>();

		File dirRoot = new File(pFilePath);
		File[] dirFileList = dirRoot.listFiles();

		for (int iFile = 0; iFile<dirFileList.length; iFile++){
			File aFile = dirFileList[iFile];
			fileList.add(aFile);
			if (aFile.isDirectory()) {
				fileList.addAll(this.getFileList(aFile.getPath()));
			}
		}

		return fileList;
	}

	public int writeToFile(String fileName, String listRoot) {
	    PrintWriter out = null;
        try {
            out = new PrintWriter(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        ArrayList<File> fileList = this.getFileList(listRoot);
        for (File tfl: fileList) {
            if (tfl.isFile()) {
                String path = tfl.getParent();
                String root = listRoot.replaceAll("\\\\", "\\\\\\\\");
                path = path.replaceAll(root, "").replaceAll("\\\\", "/");
                out.println(tfl.getName() + "|" + path);
            }
        }
        out.close();
        return 0;
    }
	
	public void testOutput() {
        ArrayList<String> fl = this.getFileNameList("qtest");
        fl = this.getFileNameList("F:\\Book\\TFLib\\A2013");
        ArrayList<File> fl1 = this.getFileList("qtest");
        fl1 = this.getFileList("F:\\Book\\TFLib\\A2013");
        List<String> fl2 = Arrays.asList("abc", "def", "ghi", "tdir000", "tdir001");
        fl.addAll(fl2);
//      for (String str: fl) {
//      TransLog.getLogger().info(str);
//  }
        for (File tfl: fl1) {
            String dInd = tfl.isDirectory()?">>>":"";
            TransLog.getLogger().info(tfl.getPath() + dInd);
        }
        TransLog.getLogger().info("=======================================");
        for (File tfl: fl1) {
            if (tfl.isFile()) {
                TransLog.getLogger().info(tfl.getName() + "|" + tfl.getParent().replaceAll("\\\\", "/"));
            }
        }
	}
	
	public void testOutFile() {
        String fn1 = "resource/pflist.txt";
        String lr1 = "D:\\Book\\TFLib";
        //String lr1 = "D:\\temp";
        long strtMillis = System.currentTimeMillis();
        TransLog.getLogger().info("Path(" + lr1 + ") write to File(" + fn1 + ").");
	    this.writeToFile(fn1, lr1);
	    TransLog.getLogger().info("File written in " + (System.currentTimeMillis()-strtMillis) + "ms.");
	}
	
	public void testBackslash() {
        String str1 = "F:\\Book\\TFLib\\A2014";
        String str2 = "F:\\Book\\TFLib\\A2014\\asdf\\asdfasd\\asdfasdf";
        str1 = str1.replaceAll("\\\\", "\\\\\\\\");
        TransLog.getLogger().info(str1);
        str2 = str2.replaceAll(str1, "");
        TransLog.getLogger().info(str2);
        str2 = str2.replaceAll("\\\\", "/");
        TransLog.getLogger().info(str2);	    
	}
	
	public static void main (String[] args) {
		PathFinder pf1 = new PathFinder();
		//pf1.testOutput();
		pf1.testOutFile();
        TransLog.getLogger().info("Done.");
	}
}
