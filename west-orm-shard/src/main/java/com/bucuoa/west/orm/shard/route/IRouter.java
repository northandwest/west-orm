package com.bucuoa.west.orm.shard.route;

public interface IRouter {

	String route();

	int getDbNums();

	String getTableName();
}
