package com.jfcf.utils;

import java.util.List;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * @author ducongcong
 * @date 2016年2月27日
 */
public class CollectionUtil {
	/**
	 * 
	 *<br/>方法描述: 依照指定属性集合排序
	 *@param collection  需要排序的集合
	 *@param sortCol 排序字段
	 *@param isAsc true 升序 false 降序
	 *@return  List
	 */
	public static  <T> List<T> sortCollection(List<T> collection, String sortCol,boolean isAsc) {
		for (int i = 0; i < collection.size(); i++) {
			for (int j = i + 1; j < collection.size(); j++) {
				BeanWrapper bwi = new BeanWrapperImpl(collection.get(i));
				BeanWrapper bwj = new BeanWrapperImpl(collection.get(j));
				int leftI = (Integer) bwi.getPropertyValue(sortCol);
				int leftJ = (Integer) bwj.getPropertyValue(sortCol);
				if (isAsc) {
					if (leftI > leftJ) {
						T obj = collection.get(j);
						collection.set(j, collection.get(i));
						collection.set(i, obj);
					}
				} else {
					if (leftI < leftJ) {
						T obj = collection.get(j);
						collection.set(j, collection.get(i));
						collection.set(i, obj);
					}
				}
			}
		}
		return collection;
	}
}
