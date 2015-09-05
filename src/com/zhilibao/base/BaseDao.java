package com.zhilibao.base;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.Assert;

/**
 * ClassName:  BaseDao. <br/>
 * Description:  公共DAO <br/>
 */
@SuppressWarnings("unchecked")
public abstract class BaseDao<T extends Serializable> extends BaseSqlSessionDaoSupport{

	protected Log logger = LogFactory.getLog(getClass());

	/**
	 * 子类直接覆盖，用于设置sqlSessionTemplate<br>
	 * 尽量直接设定sessionTemplate，防止配置被覆盖<br>
	 */
	public abstract void setSqlSessionTemplate(SqlSessionFactory sqlSessionFactory);


	public int save(final String statement,final List<T> list){
		Assert.notNull(statement, "mapper statement 不能为空");
		logger.debug("method[save], params[statement:{"+statement+"} ");
		return getSqlSession().insert(statement,list);
	}
	/**
	 * 保存实体 <br/>
	 * @param statement mapper中statement的id
	 * @param entity    mapper中statement的参数 entity
	 * @return 影响的行数
	 */ 
	public int save(final String statement,final T entity){
		Assert.notNull(statement, "mapper statement 不能为空");
		Assert.notNull(entity, "entity不能为空");
		logger.debug("method[save], params[statement:{"+statement+"} ,entity: {"+entity+"}]");
		return getSqlSession().insert(statement, entity);
	}
	/**
	 * 删除实体 <br/>
	 * @param statement mapper中statement的id
	 * @param entity	mapper中statement的参数 entity
	 * @return 影响的行数
	 */
	public int delete(final String statement,final T entity){
		Assert.notNull(statement, "mapper statement 不能为空");
		Assert.notNull(entity, "entity不能为空");
		logger.debug("method[delete],params[statement: {"+statement+"} ,entity:{"+entity+"}]");
		return getSqlSession().delete(statement, entity);
	}
	/**
	 * 根据id删除实体 <br/>
	 * @param statement mapper中statement的id
	 * @param id        mapper中statement的参数实体id
	 * @return 影响的行数
	 */
	public int delete(final String statement,final Number id){
		Assert.notNull(statement, "mapper statement 不能为空");
		Assert.notNull(id, "id不能为空");
		logger.debug("method[delete],params[statement:{"+statement+"} ,entity: {"+id+"}]");
		return getSqlSession().delete(statement,id);
	}
	/**
	 * 修改实体 <br/>
	 * @param statement mapper中statement的id
	 * @return 影响的行数
	 */
	public int update(final String statement){
		Assert.notNull(statement, "mapper statement 不能为空");
		return this.getSqlSession().update(statement);
	}
	/**
	 * 修改实体 <br/>
	 * @param statement mapper中statement的id
	 * @param entity	mapper中statement的参数entity
	 * @return 影响的行数
	 */
	public int update(final String statement,final T entity){
		Assert.notNull(statement, "mapper statement 不能为空");
		Assert.notNull(entity, "entity不能为空");
		logger.debug("method[update], params[statement:{"+statement+"} ,entity: {"+entity+"}]");
		return this.getSqlSession().update(statement, entity);
	}
	/**
	 * 修改实体 <br/>
	 * @param statement mapper中statement的id
	 * @param parameters mapper中statement的参数map
	 * @return
	 */
	public int update(final String statement,final Map<String,Object> parameters){
		Assert.notNull(statement, "mapper statement 不能为空");
		logger.debug("method[update], params[statement:{"+statement+"} ,parameterMap: {"+parameters+"}]");
		return this.getSqlSession().update(statement, parameters);
	}
	/**
	 * 根据id获取实体 <br/>
	 * @param statement mapper中statement的id
	 * @param id	    mapper中statement的参数实体id
	 * @return
	 */
	public T get(final String statement,final Number id) {
		logger.debug("method[get], params[statement:{"+statement+"} ,id: {"+id+"}]");
		return (T) getSqlSession().selectOne(statement, id);
	}
	public <Vo extends Serializable> Vo getCount(final String statement,final Number id){
		return  (Vo) getSqlSession().selectOne(statement, id);
	}
	/**
	 * 根据条件查询唯一实体 <br/>
	 * @param statement mapper中statement的id
	 * @param entity  mapper中statement的参数entity
	 * @return
	 */
	public T getUnique(final String statement,final T entity){
		logger.debug("method[getUnique], params[statement:{"+statement+"} ,entity: {"+entity+"}]");
		return (T) getSqlSession().selectOne(statement, entity);
	}
	/**
	 * 根据条件查询唯一实体  <br/>
	 * @param statement	mapper中statement的id
	 * @param parameters	mapper中statement的参数entity
	 * @return
	 */
	public T getUnique(final String statement,final Map<String,Object> parameters){
		logger.debug("method[getUnique], params[statement:{"+statement+"} ,parameterMap: {"+parameters+"}]");
		return (T) getSqlSession().selectOne(statement, parameters);
	}
	/**
	 * 查询实体 <br/>
	 * @param statement  mapper中statement的id
	 * @return
	 */
	public List<T> getList(final String statement){
		logger.debug("method[getList], params[statement:{"+statement+"} ]");
		return this.getSqlSession().selectList(statement);
	}
	/**
	 * 根据条件查询 <br/>
	 * @param statement mapper中statement的id
	 * @param entity	mapper中statement的参数entity
	 * @return
	 */
	public List<T> getList(final String statement,final T entity){
		logger.debug("method[getList], params[statement:{"+statement+"} ,entity: {"+entity+"}]");
		return this.getSqlSession().selectList(statement,entity);
	}
	/**
	 * 根据条件查询 <br/>
	 * @param statement mapper中statement的id
	 * @param parameters mapper中statement的参数map
	 * @return
	 */
	public List<T> getList(final String statement,final Map<String,Object> parameters){
		logger.debug("method[getList], params[statement:{"+statement+"} ,parameterMap: {"+parameters+"}]");
		return this.getSqlSession().selectList(statement,parameters);
	}

	
	/**
	 * 根据条件统计数量 <br/>
	 * @param statement
	 * @param parameters
	 * @return
	 */
	public int count(final String statement,final Map<String,Object> parameters){
		logger.debug("method[findPage], params[statement:{"+statement+"} ,parameterMap:{"+parameters+"}]");
		try {
			int count = ((Number) this.getSqlSession().selectOne(statement, parameters)).intValue();
			return count;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return 0;
		}
	}
	/**
	 * 根据条件统计数量 <br/>
	 * @param statement
	 * @param parameters
	 * @return
	 */
	public Number statis(final String statement,final Map<String,Object> parameters){
		logger.debug("method[findPage], params[statement:{"+statement+"} ,parameterMap:{"+parameters+"}]");
		return (Number) this.getSqlSession().selectOne(statement, parameters);
	}
}

