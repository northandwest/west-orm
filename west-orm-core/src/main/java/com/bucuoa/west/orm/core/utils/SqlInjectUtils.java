package com.bucuoa.west.orm.core.utils;

public class SqlInjectUtils {
	public static String filterSql(String str)
    {
          return str.replaceAll(".*([';]+|(--)+).*", " ");
    }
}
