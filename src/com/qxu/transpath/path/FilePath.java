package com.qxu.transpath.path;

import com.qxu.transpath.utils.TranspathConstants;

public class FilePath extends BasePath {
	private static final String prefixArchive = "A";
	private static final String prefixBlock = "B";
	
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
		super.setFullPath(archiveName + TranspathConstants.LEVEL_LINKER + blockName);
	}
	
	@Override
	public String getFullPath() {
		return archiveName + TranspathConstants.LEVEL_LINKER + blockName;
	}
}
