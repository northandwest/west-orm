package com.bucuoa.west.orm.core.clazzinfo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

//com.bucuoa.west.orm.core.clazzinfo.ShardTableInfo
public class TableinfoFactory {

	public static  <T> ITableInfo create(T t)
	{
		SingleTableInfo single = new SingleTableInfo(t);
		if(single.isSingleTable())
		{
			return single;
		}else
		{
			Class<?> obj = null;
			try {
				obj = Class.forName("com.bucuoa.west.orm.core.clazzinfo.ShardTableInfo");
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} 
			
			 Constructor constructor = null;
			try {
				constructor = obj.getConstructor(Object.class);
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} 
			
			try {
				return (ITableInfo) constructor.newInstance(t);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			
			return null;
		}
	}

}
