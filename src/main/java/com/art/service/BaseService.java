package com.art.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.art.annotation.MyId;
import com.art.mapper.BaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 基类的service
 *
 * @Author: never
 * @Date: 2019/04/22 10:32
 *
 */
public abstract class BaseService<M extends BaseMapper<T>, T> implements InitializingBean {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	protected M mapper;

	// 具体操作的实体类
	private Class<T> clazz;

	/**
	 * 添加实体
	 * 
	 * @param t
	 * @return
	 */
	@Transactional
	public boolean add(T t) { return mapper.insert(t) > 0;
	}

	/**
	 * 更新实体
	 * 
	 * @param t
	 * @return
	 */
	@Transactional
	public boolean update(T t) {
		return mapper.update(t) > 0;
	}

	/**
	 * 根据ID删除
	 * 
	 * @param id
	 * @return
	 */
	@Transactional
	public boolean deleteById(Object id) {
		T t = assembly(id);
		return mapper.deleteById(t) > 0;
	}

	/**
	 * 根据ID删除
	 * 
	 * @param t
	 * @return
	 */
	@Transactional
	public boolean deleteEntity(T t) {
		return mapper.deleteById(t) > 0;
	}

	/**
	 * 根据ID查询
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public T getById(Object id) {
		T t = assembly(id);
		return mapper.getById(t);
	}

	/**
	 * 根据ID查询
	 * 
	 * @param t
	 * @return
	 */
	@Transactional(readOnly = true)
	public T getEntity(T t) {
		return mapper.getById(t);
	}

	/**
	 * 查询所有
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<T> findAll() {
		T t = assembly();
		return mapper.findAll(t);
	}

	/**
	 * 模糊匹配查询
	 * 
	 * @param t
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<T> seleteVague(T t) {
		return mapper.seleteVague(t);
	}

	/**
	 * 查询总数
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public long countAll() {
		T t = assembly();
		return mapper.countAll(t);
	}

	/**
	 * 精确匹配查询
	 * 
	 * @param t
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<T> seleteAccuracy(T t) {
		return mapper.seleteAccuracy(t);
	}

	/**
	 * 模糊匹配,查询总数
	 * 
	 * @param t
	 * @return
	 */
	@Transactional(readOnly = true)
	public long countVague(T t) {
		return mapper.countVague(t);
	}

	/**
	 * 精确匹配,查询总数
	 * 
	 * @param t
	 * @return
	 */
	@Transactional(readOnly = true)
	public long countAccuracy(T t) {
		return mapper.countAccuracy(t);
	}

	/**
	 * 删除所有的实体
	 * 
	 * @return
	 */
	@Transactional
	public long deleteAll() {
		T t = assembly();
		return mapper.deleteAll(t);
	}

	/**
	 * 模糊匹配删除实体
	 * 
	 * @return
	 */
	@Transactional
	public long deleteVague() {
		T t = assembly();
		return mapper.deleteVague(t);
	}

	/**
	 * 精确匹配删除实体
	 * 
	 * @return
	 */
	@Transactional
	public long deleteAccuracy() {
		T t = assembly();
		return mapper.deleteAccuracy(t);
	}

	/**
	 * 模糊匹配分页查询
	 * 
	 * @param t
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@Transactional(readOnly = true)
	public PageInfo<T> pageVague(T t, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<T> list = mapper.seleteVague(t);
		return new PageInfo<>(list);
	}

	/**
	 * 精确匹配分页查询
	 * 
	 * @param t
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@Transactional(readOnly = true)
	public PageInfo<T> pageAccuracy(T t, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<T> list = mapper.seleteAccuracy(t);
		return new PageInfo<>(list);
	}

	/**
	 * 通过ID，反射创建实体
	 * 
	 * @param id
	 * @return
	 */
	private T assembly(Object id) {
		try {
			T t = clazz.newInstance();
			Field field = getIdField(t);
			field.set(t, id);
			return t;
		} catch (Exception e) {
			logger.error("assembly entity with id error", e);
			return null;
		}
	}

	/**
	 * 反射创建实体
	 * 
	 * @return
	 */
	private T assembly() {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			logger.error("assembly entity without id error", e);
			return null;
		}
	}

	/**
	 * 获取带有@MyId的field
	 * 
	 * @param obj
	 * @return
	 */
	private Field getIdField(Object obj) {
		Class<?> clazz = obj.getClass();
		Field idField = null;
		while (true) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				MyId id = field.getAnnotation(MyId.class);
				if (id != null) {
					idField = field;
					break;
				}
			}
			if (idField != null || clazz.getSuperclass() == Object.class)
				break;
			clazz = clazz.getSuperclass();
		}
		if (idField != null)
			idField.setAccessible(true);
		return idField;
	}

	/*
	 * 获取泛型的class
	 * 
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void afterPropertiesSet() throws Exception {
		Type type = this.getClass().getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType) type;
		clazz = Class.class.cast(pt.getActualTypeArguments()[1]);
		Class<?> interfaces = Class.class.cast(pt.getActualTypeArguments()[0]);
		logger.info("\nthe {} service's entity is {}", getClass().getName(), clazz.getName());
		logger.info("\nthe {} service's mapper is {}", getClass().getName(), interfaces.getName());
	}

}
