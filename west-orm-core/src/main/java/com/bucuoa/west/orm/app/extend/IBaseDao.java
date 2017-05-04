package com.bucuoa.west.orm.app.extend;

import java.io.Serializable;
import java.util.List;

public interface IBaseDao<T, PK> {

	T saveEntity(T entity) throws Exception;

	void updateEntity(T entity) throws Exception;

	List<T> findListBy(T t) throws Exception;

	boolean deleteEntityBy(T t) throws Exception;
	
	boolean updateEntityIn(T t,Long[] ids) throws Exception;


}
