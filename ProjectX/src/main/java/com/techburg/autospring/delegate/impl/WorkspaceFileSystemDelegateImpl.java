package com.techburg.autospring.delegate.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.web.multipart.MultipartFile;

import com.techburg.autospring.delegate.abstr.IWorkspaceFileSystemDelegate;
import com.techburg.autospring.model.business.Workspace;
import com.techburg.autospring.util.FileUtil;

public class WorkspaceFileSystemDelegateImpl implements IWorkspaceFileSystemDelegate {

	//Implement
	public void saveWorkspaceBuildScriptContent(Workspace workspace, String buildScriptContent) throws Exception {
		String scriptFilePath = workspace.getScriptFilePath();

		//Avoid script run error due to \r character
		String content = buildScriptContent.replace("\r\n", "\n");

		FileUtil fileUtil = new FileUtil();
		fileUtil.storeContentToFile(content, scriptFilePath);
	}

	//Implement
	public String getWorkspaceScriptContent(Workspace workspace) {
		FileUtil fileUtil = new FileUtil();
		try {
			return fileUtil.getStringFromFile(workspace.getScriptFilePath());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	//Implement
	public void extractUploadedZipFileToWorkspace(Workspace workspace, MultipartFile uploadedZipFile) throws Exception {
		String outDirectory = workspace.getDirectoryPath();

		//Refer from http://developer.android.com/reference/java/util/zip/ZipInputStream.html
		ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(uploadedZipFile.getInputStream()));
		try {
			ZipEntry zipEntry;
			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
				String filename = zipEntry.getName();
				
				String outFilePath = outDirectory + File.separator + filename;
				File outputFile = new File(outFilePath);

				//Create new file/folder corresponding to zip entry
				if(zipEntry.isDirectory()) {
					if(!outputFile.mkdir()) {
						throw new IOException("Can't extract sub-folder");
					}
				}
				else {
					//Otherwise just copy file content
					OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

					byte[] buffer = new byte[1024];
					int count;
					while ((count = zipInputStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, count);
					}

					outputStream.close();
				}
			}
		} 
		finally {
			zipInputStream.close();
		}
	}
	
	//Implement
	public void eraseWorkspace(Workspace workspace) throws Exception {
		FileUtil fileUtil = new FileUtil();
		fileUtil.removeAtPath(workspace.getDirectoryPath());
	}
}
