package com.bucuoa.west.orm.app.extend.spring;

/**
 * 为提高用户体验，单表操作的基础操作和分库分表的参数是不一样的，个性化处理
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bucuoa.west.orm.app.common.Expression;
import com.bucuoa.west.orm.app.common.OrderBy;
import com.bucuoa.west.orm.app.common.WPage;
import com.bucuoa.west.orm.core.ExecuteManager;
import com.bucuoa.west.orm.core.base.IdObject;
import com.bucuoa.west.orm.core.converter.ClassObjectConverter;
import com.bucuoa.west.orm.core.mapping.SQLFactory;
import com.bucuoa.west.orm.core.utils.AnnoationUtil;

public  class BaseDao<T, PK> {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected String getTablePKField(Class<T> classz) {
		return AnnoationUtil.getId(classz);
	}

	protected IdObject getIdInfo(Class<T> classz) {
		return AnnoationUtil.getIdObject(classz);
	}

	protected String getTablePKSetMethod(Class<T> classz) {
		String id = AnnoationUtil.getId(classz);
		id = ClassObjectConverter.fieldToClazzSetMethod(id);
		return id;
	}
	
	@SuppressWarnings("unchecked")
	public  T saveEntity(T entity,ExecuteManager<T, PK> excetueManager,Class<T> classz) throws Exception {
		String insertSql = SQLFactory.insertSql(entity);

		String setPK = getTablePKSetMethod(classz);

		long start = System.currentTimeMillis();

		IdObject idInfo = getIdInfo(classz);

		if (idInfo != null && idInfo.isAutoIncrement()) {
			PK id = (PK) excetueManager.executeUpdateSql(insertSql);

			Class<?> clazz = entity.getClass();
			clazz.getMethod(setPK, id.getClass()).invoke(entity, id);
		} else {
			boolean success = excetueManager.executeUpdateSqlNoID(insertSql);
		}
		long end = System.currentTimeMillis();

		logger.trace("执行sql:{} use:{}ms", insertSql, (end - start));
		return entity;
	}

	public  void updateEntity(T entity,ExecuteManager<T, PK> excetueManager) throws Exception {
		long start = System.currentTimeMillis();
		String updateSql = SQLFactory.updateSql(entity);

		excetueManager.executeUpdateSql(updateSql);

		long end = System.currentTimeMillis();
		
		logger.trace("执行sql:{} use:{}ms", updateSql, (end - start));

	}
	
	
	public boolean updateEntityIn(T t,Long[] ids,ExecuteManager<T, PK> excetueManager) throws Exception
	{
		String updateSql = SQLFactory.updateSql(t,ids);
		
		excetueManager.executeUpdateSql(updateSql);
		return true;
	}

	public int getEntityCount(T t,ExecuteManager<T, PK> excetueManager) throws Exception {

		String sql = SQLFactory.selectCountSql(t, null);

		if (sql == null) {
			throw new IllegalArgumentException("The sql statement is null.");
		}

		int queryCount = 0;
		long start = System.currentTimeMillis();

		queryCount = excetueManager.queryCount(sql);

		long end = System.currentTimeMillis();

		logger.trace("执行sql:{} use:{}ms", sql, (end - start));

		return queryCount;
	}

	public int getEntityCount(List<Expression> wheres,ExecuteManager<T, PK> excetueManager,Class<T> classz) throws Exception {

		String selectCountSql = SQLFactory.selectCountSql(classz.newInstance(), wheres);
		int queryCount = 0;
		long start = System.currentTimeMillis();

		queryCount = excetueManager.queryCount(selectCountSql);

		long end = System.currentTimeMillis();

		logger.trace("执行sql:{} use:{}ms", selectCountSql, (end - start));

		return queryCount;
	}

	public boolean deleteEntityBy(T t,ExecuteManager<T, PK> excetueManager) throws Exception {

		String selectSql = 	SQLFactory.deleteSql(t);
		long start = System.currentTimeMillis();

		excetueManager.deleteBy(selectSql);

		long end = System.currentTimeMillis();

		logger.trace("执行sql:{} use:{}ms", selectSql, (end - start));

		return true;
	}

	public T findEntityById(PK id,ExecuteManager<T, PK> excetueManager,Class<T> classz) throws Exception {

		List<Expression> wheres = new ArrayList<Expression>();
		String tablePKField = getTablePKField(classz);
		Expression expression = new Expression(tablePKField, id);
		wheres.add(expression);

		String setPK = getTablePKSetMethod(classz);

		T newInstance = classz.newInstance();
		classz.getMethod(setPK, id.getClass()).invoke(newInstance, id);

		String selectSql = SQLFactory.selectSql(newInstance, wheres);

		long start = System.currentTimeMillis();

		T queryOne = excetueManager.queryOne(selectSql, classz);

		long end = System.currentTimeMillis();

		logger.trace("执行sql : {} use:{}ms", selectSql, (end - start));
		return queryOne;
	}

	@SuppressWarnings({ "unchecked" })
	public <T> List<T> findListBy(T t,ExecuteManager<T, PK> excetueManager,Class<T> classz) throws Exception {

		Class<T> classz2 = (Class<T>) classz;

		List<T> queryList = (List<T>) findListBy(t, classz2,excetueManager);

		return queryList;
	}

	public <T> List<T> findListBy(T t, Class<T> clazz,ExecuteManager<T, PK> excetueManager) throws Exception {

		List<Expression> wheres = new ArrayList<Expression>();

		Map<String, Object> conditions = SQLFactory.getNotNullFields(t);

		Set<Entry<String, Object>> entrySet = conditions.entrySet();

		for (Entry<String, Object> entry : entrySet) {
			Expression expression = new Expression(entry.getKey().toString(), entry.getValue());
			wheres.add(expression);
		}

		String selectSql = SQLFactory.selectSql(t, wheres);

		long start = System.currentTimeMillis();

		List<T> queryList = (List<T>) excetueManager.queryList(selectSql, clazz);

		long end = System.currentTimeMillis();

		logger.trace("执行sql : {} use:{}ms", selectSql, (end - start));
		return queryList;
	}

	public boolean deleteEntityById(PK id,ExecuteManager<T, PK> excetueManager,Class<T> classz) throws Exception {
		T newInstance = classz.newInstance();

		String setPK = getTablePKSetMethod(classz);
		classz.getMethod(setPK, id.getClass()).invoke(newInstance, id);

		String deleteSql = SQLFactory.deleteSql(newInstance);// .deleteSqlBy(classz,
																// condition);
		long start = System.currentTimeMillis();

		excetueManager.deleteBy(deleteSql);
		long end = System.currentTimeMillis();

		logger.trace("执行sql : " + deleteSql + "use:" + (end - start) + "ms");
		return true;
	}

	public boolean deleteEntityByCondition(List<Expression> condition,ExecuteManager<T, PK> excetueManager,Class<T> classz) throws Exception {

		String deleteSql = SQLFactory.deleteSqlBy(classz, condition);
		long start = System.currentTimeMillis();

		excetueManager.deleteBy(deleteSql);
		long end = System.currentTimeMillis();

		logger.trace("执行sql : " + deleteSql + "use:" + (end - start) + "ms");
		return true;
	}

	public List<T> findEntityList(List<Expression> where, OrderBy orderBy, WPage page,ExecuteManager<T, PK> excetueManager,Class<T> classz) {
		return findEntityList(null, where, orderBy, page,excetueManager,classz);
	}

	public List<T> findEntityList(List<Expression> where, WPage page,ExecuteManager<T, PK> excetueManager,Class<T> classz) {
		return findEntityList(null, where, null, page,excetueManager,classz);
	}

	public List<T> findEntityList(String[] column, List<Expression> wheres, OrderBy orderBy, WPage page,ExecuteManager<T, PK> excetueManager,Class<T> classz) {

		List<T> queryList = null;
		try {
			T newInstance = classz.newInstance();
			String selectSql = SQLFactory.selectSql(newInstance, wheres, orderBy, page);
			long start = System.currentTimeMillis();

			queryList = excetueManager.queryList(selectSql, classz);
			long end = System.currentTimeMillis();

			logger.trace("执行sql : " + selectSql + "use:" + (end - start) + "ms");
		} catch (Exception e) {
		}

		if (queryList == null) {
			queryList = new ArrayList<T>();
		}

		return queryList;
	}

	public List<T> findEntityList(OrderBy orderBy, WPage page,ExecuteManager<T, PK> excetueManager,Class<T> classz) {
		List<T> queryList = null;
		try {
			String selectSql = SQLFactory.selectSql(classz, orderBy, page);
			long start = System.currentTimeMillis();

			queryList = excetueManager.queryList(selectSql, classz);
			long end = System.currentTimeMillis();

			logger.trace("执行sql : " + selectSql + "use:" + (end - start) + "ms");
		} catch (Exception e) {
		}

		if (queryList == null) {
			queryList = new ArrayList<T>();
		}

		return queryList;
	}

	public int queryCount(String sql,ExecuteManager<T, PK> excetueManager) {
		long start = System.currentTimeMillis();

		int count = excetueManager.queryCount(sql);

		long end = System.currentTimeMillis();

		logger.trace("执行sql : " + sql + "use:" + (end - start) + "ms");
		return count;
	}

	public List<T> queryListBean(Class<T> clazz, String sql,ExecuteManager<T, PK> excetueManager) {
		List<T> list = null;
		long start = System.currentTimeMillis();

		list = (List<T>) excetueManager.queryList(sql, clazz);
		long end = System.currentTimeMillis();

		logger.trace("执行sql : " + sql + "use:" + (end - start) + "ms");
		return list;
	}
	
	public List<Map<String, Object>> queryListMap(String sql,ExecuteManager<T, PK> excetueManager) {
		List<Map<String, Object>> queryListMap = null;
		long start = System.currentTimeMillis();

		queryListMap = excetueManager.queryListMap(sql);

		long end = System.currentTimeMillis();

		logger.trace("执行sql : " + sql + "use:" + (end - start) + "ms");
		return queryListMap;
	}



}
