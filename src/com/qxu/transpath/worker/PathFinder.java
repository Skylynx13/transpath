package com.qxu.transpath.worker;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	
	public static void main (String[] args) {
		PathFinder pf1 = new PathFinder();
		ArrayList<String> fl = pf1.getFileNameList("qtest");
		ArrayList<File> fl1 = pf1.getFileList("qtest");
		List<String> fl2 = Arrays.asList("abc", "def", "ghi", "tdir000", "tdir001");
		fl.addAll(fl2);
//		for (String str: fl) {
//		System.out.println(str);
//	}
		for (File tfl: fl1) {
			String dInd = tfl.isDirectory()?">>>":"";
			System.out.println(tfl.getPath() + dInd);
		}
		System.out.println("=======================================");
		for (File tfl: fl1) {
			if (tfl.isFile()) {
				System.out.println(tfl.getName() + "|" + tfl.getParent().replaceAll("\\\\", "/"));
			}
		}
	}
}
