package com.px.utils;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件处理
 * @author Jason
 *
 */
public final class FileUtil {

	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
	
	/**
	 * 文件上传
	 * @param file 上传的文件
	 * @param path 绝对路径
	 * @return 
	 */
	public static boolean upload(MultipartFile file, String path){
		if(file == null)
			return false;
		
		if(file.isEmpty())
			return false;
		
		File exists = new File(path);
		
		if(!exists.exists())
			exists.mkdirs();
		
		/*if(!path.endsWith(File.separator))
			path = path.concat(File.separator);
		String fullPath = path.concat(file.getOriginalFilename());*/
		try {
			file.transferTo(new File(path));
			if(logger.isDebugEnabled())
				logger.debug("文件上传成功: {}", file.getOriginalFilename());
			
			return true;
		} catch (IllegalStateException | IOException e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}
	
	public static boolean isExists(String filePath){
		if(StringUtils.isEmpty(filePath))
			return false;
		
		File file = new File(filePath);
		return file.exists() && file.isFile();
	}
}
