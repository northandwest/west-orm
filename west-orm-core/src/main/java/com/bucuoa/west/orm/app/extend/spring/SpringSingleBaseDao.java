package com.bucuoa.west.orm.app.extend.spring;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bucuoa.west.orm.app.common.Expression;
import com.bucuoa.west.orm.app.common.OrderBy;
import com.bucuoa.west.orm.app.common.WPage;
import com.bucuoa.west.orm.core.ExecuteManager;
import com.bucuoa.west.orm.core.base.IdObject;
import com.bucuoa.west.orm.core.converter.ClassObjectConverter;
import com.bucuoa.west.orm.core.utils.AnnoationUtil;

/**
 * west和应用的接口类 项目只需要继承本类就可以实现west提供的功能 用户也可以自己实现本类 只需要注入ExcetueManager即可
 * 
 * @author jake
 */
@Component
public class SpringSingleBaseDao<T, PK> extends BaseDao<T,PK> {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	protected ExecuteManager<T, PK> excetueManager;

	@SuppressWarnings("unchecked")
	protected SpringSingleBaseDao() {
		classz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	private Class<T> classz;
	private Class<T> getClassz() {
		return classz;
	}
	
	protected String getTablePKField() {
		return AnnoationUtil.getId(getClassz());
	}

	protected IdObject getIdInfo() {
		return AnnoationUtil.getIdObject(getClassz());
	}

	protected String getTablePKSetMethod() {
		String id = AnnoationUtil.getId(getClassz());
		id = ClassObjectConverter.fieldToClazzSetMethod(id);
		return id;
	}
	
	@SuppressWarnings("unchecked")
	public T saveEntity(T entity) throws Exception {
	
		return super.saveEntity(entity, excetueManager, classz);
	}

	public void updateEntity(T entity) throws Exception {
		
		super.updateEntity(entity, excetueManager);
	}
	
	
	public boolean updateEntityIn(T t,Long[] ids) throws Exception
	{
		return	super.updateEntityIn(t, ids, excetueManager);
	}


	public Connection getConnection()
	{
		return excetueManager.getSessionFactory().getSession().getConnection();
	}

	public int getEntityCount(T t) throws Exception {
		
		return	 super.getEntityCount(t, excetueManager);
	}

	public int getEntityCount(List<Expression> wheres) throws Exception {

		return super.getEntityCount(wheres, excetueManager,classz);
	}

	public boolean deleteEntityBy(T t) throws Exception {

		return super.deleteEntityBy(t, excetueManager);
	}

	public T findEntityById(PK id) throws Exception {

		return super.findEntityById(id, excetueManager, classz);
	}

	@SuppressWarnings({ "unchecked" })
	public  List<T> findListBy(T t) throws Exception {

		return super.findListBy(t, excetueManager, classz);
	}

	public  List<T> findListBy(T t, Class<T> clazz) throws Exception {

		return super.findListBy(t, clazz, excetueManager);
	}

	public boolean deleteEntityById(PK id) throws Exception {

		return super.deleteEntityById(id, excetueManager, classz);
	}

	public boolean deleteEntityByCondition(List<Expression> condition) throws Exception {

		return super.deleteEntityByCondition(condition, excetueManager, classz);
	}

	public List<T> findEntityList(List<Expression> where, OrderBy orderBy, WPage page) {
		return super.findEntityList(where, page, excetueManager, classz);
	}

	public List<T> findEntityList(List<Expression> where, WPage page) {
		return findEntityList(null, where, null, page);
	}

	public List<T> findEntityList(String[] column, List<Expression> wheres, OrderBy orderBy, WPage page) {



		return super.findEntityList(column, wheres, orderBy, page, excetueManager, classz);
	}

	public List<T> findEntityList(OrderBy orderBy, WPage page) {
	

		return super.findEntityList(orderBy, page, excetueManager, classz);
	}

	public int queryCount(String sql) {
	
		return super.queryCount(sql, excetueManager);
	}

	public List<T> queryListBean(Class<T> clazz, String sql) {
	
		return super.queryListBean(clazz, sql, excetueManager);
	}
	
	public List<Map<String, Object>> queryListMap(String sql) {

		return super.queryListMap(sql, excetueManager);
	}


}
