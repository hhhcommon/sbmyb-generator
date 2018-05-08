/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: 玖富时代</p>
 * @author likaiwen
 * @version 1.0
 */
package com.jfcf.platform.generate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import com.jfcf.utils.StringUtils;


public class CheckModelPackage {
	
	/**
	 * 一个将实体包下包名、java的NAMESPACE和xml的NAMESPACE统一的小程序
	 * 这个程序又问题，但可以用，减少大部分的工作量
	 * 如果修改的字符串比原有字符串段，用空格占位覆盖原字符串
	 * 如果修改的字符串比原有的字符串长，会出现问题，覆盖到后面的字符，待完善
	 * @author likaiwen
	 * 
	 * */
	@SuppressWarnings("resource")
	public static void main(String[] args) throws FileNotFoundException {
		String root = System.getProperty("user.dir");
		//model实体绝对路径
		String packagePath = root + "\\src\\main\\java\\com\\doraemoney\\model";
		CheckModelPackage cmp = new CheckModelPackage();
		List<File> fileList = new ArrayList<File>();
		//递归遍历model包下面的所有文件，不包含文件夹
		fileList = cmp.traverseFolder(packagePath, fileList);
		//.java文件集
		List<File> javaFile = new ArrayList<File>();
		//.xml文件集
		List<File> xmlFile = new ArrayList<File>();
		for(int i = 0; i < fileList.size(); i++) {
			if(fileList.get(i).getName().endsWith(".java")) {
				javaFile.add(fileList.get(i));
			}
			if(fileList.get(i).getName().endsWith(".xml")) {
				xmlFile.add(fileList.get(i));
			}
		}
		String[] s1, s2;
		int count = 0;
		File java;
		File xml;
		for (int i = 0; i < javaFile.size(); i++) {
			for (int j = 0; j < xmlFile.size(); j++) {
				java = javaFile.get(i);
				RandomAccessFile javaWriter = new RandomAccessFile(java, "rw");
				xml = xmlFile.get(j);
				RandomAccessFile xmlWriter = new RandomAccessFile(xml, "rw");
				//xxx.java文件分割为字符数组
				s1 = StringUtils.split(java.getName(), ".");
				//xxx.sql.xml文件分割为字符数组
				s2 = StringUtils.split(xml.getName(), ".");
				if(s1[0].equalsIgnoreCase(s2[0])) {
					//文件计数器
					count++;
					try {
						//.java文件第一行，取出当前Java文件的包路径
						String _1stLine = javaWriter.readLine();
						String packgaeName1 = StringUtils.split(_1stLine, " ")[1];
						String packgaeName = StringUtils.substringBefore(packgaeName1, ";");
						System.out.println("包：" + packgaeName);
						String str;
						//遍历java文件的每一行
						while((str = javaWriter.readLine()) != null) {
							//
							if(StringUtils.contains(str, "NAMESPACE") && 
								StringUtils.contains(str, "private") && 
								StringUtils.contains(str, "static")) {
								String writeStr = "private static final String NAMESPACE=\"" + packgaeName 
										+ "." + StringUtils.toLowerCaseFirstOne(s1[0]) + ".\";";
								//如果替换的字符串长度比原有字符串段，则用空格覆盖
								if(writeStr.length() < str.length()) {
									//忽略tab键长度
									int len = str.length() - writeStr.length() - 4;
									for(int ii = 0; ii < len; ii++) {
										writeStr += " ";
									}
								}
								//如果替换的字符串比原来的长，
								if(writeStr.length() > str.length()) {
									//TODO
								}
								System.out.println("原有：" + str);
								System.out.println("写入：    " + writeStr);
								
								//javaWriter读取改行候文件指针位置
								long tagPoint = javaWriter.getFilePointer();
								//javaWriter写入的起始位置，这里的实际位置需要微调，通过 +n 调整
								javaWriter.seek(tagPoint - str.length()+2);
								byte[] b = writeStr.getBytes();
								//覆盖写入
								javaWriter.write(b);
								break;
							}
						}
						//遍历xml文件每一行
						while((str = xmlWriter.readLine()) != null) {
							if(StringUtils.contains(str, "<sql-statement") && StringUtils.contains(str, "namespace")) {
								String writeStr = "<sql-statement namespace=\"" + packgaeName 
										+ "." + StringUtils.toLowerCaseFirstOne(s1[0]) + "\">";
								if(writeStr.length() < str.length()) {
									int len = str.length() - writeStr.length();
									for(int ii = 0; ii < len; ii++) {
										writeStr += " ";
									}
								}
								System.out.println("原有：" + str);
								System.out.println("写入：" + writeStr);
								System.out.println();
								long tagPoint = xmlWriter.getFilePointer();
								xmlWriter.seek(tagPoint - str.length()-1);
								byte[] b = writeStr.getBytes();
								xmlWriter.write(b);
								break;
							}
						}
						
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							javaWriter.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}		
		System.out.println(count);
	}
	
	public List<File> traverseFolder(String path, List<File> fileList) {
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length > 0) {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        traverseFolder(file2.getAbsolutePath(), fileList);
                    } else {
                        fileList.add(new File(file2.getAbsolutePath()));
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
        return fileList;
    }

}
