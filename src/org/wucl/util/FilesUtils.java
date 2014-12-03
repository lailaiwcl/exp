package org.wucl.util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class FilesUtils {
	/**
	 * 删除指定目录中的所有内容
	 * 
	 * @param dir
	 *            将要删除的目录
	 * @return true 删除成功 false 删除失败
	 */

	private static boolean doDelete(File dir) {
		File[] files = dir.listFiles();
		for (File file : files) {
			// 如果是文件
			if (file.isFile()) {
				boolean b = file.delete();
				// 如果删除失败，则返回false
				if (!b)
					return b;
			} else {// 如果是目录
				doDelete(file);
				boolean b = file.delete();
				if (!b)
					return b;

			}
		}
		// 删除成功返回true
		return true;
	}

	/**
	 * 
	 * @param dir
	 *            将要删除的目录
	 * @return <code>true</code>删除成功 <code> false</code>删除失败
	 */
	public static boolean deleteDir(File dir) {
		// 判断文件格式合法性
		if (dir == null || !dir.isDirectory() || !dir.exists()) {
			// 抛出文件不合法异常
			throw new IllegalArgumentException("目录格式不合法");
		} else {
			return doDelete(dir);
		}
	}

	/**
	 * 
	 * @param pathname
	 *            目录的字符串路径
	 * @return <code>true</code>删除成功 <code> false</code>删除失败
	 */
	public static boolean deleteDir(String pathname) {
		File dir = new File(pathname);
		// 判断文件格式合法性
		if (dir == null || !dir.isDirectory() || !dir.exists()) {
			// 抛出文件不合法异常
			throw new IllegalArgumentException("目录格式不合法");
		} else {
			return doDelete(dir);
		}
	}

	/**
	 * 按条件查找指定目录以及其子目录下的所有文件
	 * 
	 * @param dir
	 *            将要查找的目录
	 * @param ff
	 *            查找的条件
	 * @return 符合条件的文件名
	 */
	private static void doFind(List<File> fileList, File dir, FileFilter ff) {
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile() && ff.accept(file)) {
				fileList.add(file);
			} else if (file.isDirectory()) {
				doFind(fileList, file, ff);
			}
		}
	}

	/**
	 * 
	 * @param dir
	 *            文件目录路径
	 * @param ff
	 *            文件过滤器
	 * @return An array of abstract pathnames denoting the files
	 */
	public static File[] findFiles(File dir, FileFilter ff) {
		ArrayList<File> fileList = new ArrayList<File>();
		doFind(fileList, dir, ff);
		return fileList.toArray(new File[fileList.size()]);

	}

	/**
	 * 查找指定目录下的制定后缀的文件名（递归）
	 * 
	 * @param dir
	 *            我们目录
	 * @param stuffix
	 *            文件后缀名
	 * @return 符合用户要求的文件
	 */

	public static File[] findFiles(File dir, final String stuffix) {
		return findFiles(dir, new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				if (pathname.isFile() && pathname.getName().endsWith(stuffix))
					return true;
				return false;
			}

		});

	}
	/**
	 * 查找指定目录下的制定后缀的文件名(非递归)
	 * 
	 * @param dir 要查找的目录或文件
	 *
	 * @param stuffix 文件后缀名
	 *
	 * @return 符合用户要求的文件
	 */
	public static File[] findFiles2(File dir , final String stuffix){
		ArrayList<File> fileList = new ArrayList<File>();
		if (dir.isDirectory()) {
			Stack<File> filestack = new Stack<File>();
			filestack.push(dir);
			while (!filestack.isEmpty()) {
				File f = filestack.pop();
				File[] ff = f.listFiles();
				for (int i = 0; i < ff.length; i++) {
					if (ff[i].isFile() && new FileFilter(){
						@Override
						public boolean accept(File pathname) {
							if (pathname.isFile() && pathname.getName().endsWith(stuffix))
								return true;
							return false;
						}						
					}.accept(ff[i])) {
						fileList.add(ff[i]);
					} else if(ff[i].isDirectory()){
						filestack.push(ff[i]);
					}
				}
			}
			return fileList.toArray(new File[fileList.size()]);
		}else{
			return new File[]{dir};
		}
	}

}
