package com.qxu.transpath.oldschool;

import com.qxu.transpath.utils.TransConst;

@SuppressWarnings("unused")
public class FilePath extends BasePath {
	private String archiveName;
    private int archiveYear;

	private String blockName;
	private int blockMonth;
	private int blockDate;
	private int blockNumber;
	
	public String getArchiveName() {
		return archiveName;
	}
	public void setArchiveName(String archiveName) {
		this.archiveName = archiveName;
	}
	public String getBlockName() {
		return blockName;
	}
	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}
	
	public void setFullPath() {
		super.setFullPath(archiveName + TransConst.LEVEL_LINKER + blockName);
	}
	
	@Override
	public String getFullPath() {
		return archiveName + TransConst.LEVEL_LINKER + blockName;
	}
}