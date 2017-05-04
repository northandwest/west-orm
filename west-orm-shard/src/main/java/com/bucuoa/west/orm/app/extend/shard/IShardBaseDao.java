package com.bucuoa.west.orm.app.extend.shard;

import java.io.Serializable;
import java.util.List;

import com.bucuoa.west.orm.app.extend.IBaseDao;

public interface IShardBaseDao<T, PK   extends Serializable> extends IBaseDao<T, PK> {
	T findEntityById(T t) throws Exception;
	List<T> findListBy(T t) throws Exception;
}
