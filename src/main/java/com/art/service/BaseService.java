package com.art.service;

import com.art.mapper.BaseMapper;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 基类的service
 *
 * @Author: never
 * @Date: 2019/4/25 下午10:32
 */
public interface BaseService<M extends BaseMapper<T>, T, ID> {


    /**
     * 添加实体
     *
     * @param t
     * @return
     */
    boolean add(T t);

    /**
     * 更新实体
     *
     * @param t
     * @return
     */
    boolean update(T t);

    /**
     * 根据ID删除
     *
     * @param id
     * @return
     */
    boolean deleteById(ID id);

    /**
     * 根据ID删除
     *
     * @param t
     * @return
     */
    boolean deleteEntity(T t);

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    T getById(ID id);

    /**
     * 根据ID查询
     *
     * @param t
     * @return
     */
    T getEntity(T t);

    /**
     * 查询所有
     *
     * @return
     */
    List<T> findAll();

    /**
     * 模糊匹配查询
     *
     * @param t
     * @return
     */
    List<T> seleteVague(T t);

    /**
     * 查询总数
     *
     * @return
     */
    long countAll();

    /**
     * 精确匹配查询
     *
     * @param t
     * @return
     */
    List<T> seleteAccuracy(T t);

    /**
     * 模糊匹配,查询总数
     *
     * @param t
     * @return
     */
    long countVague(T t);

    /**
     * 精确匹配,查询总数
     *
     * @param t
     * @return
     */
    long countAccuracy(T t);

    /**
     * 删除所有的实体
     *
     * @return
     */
    long deleteAll();

    /**
     * 模糊匹配删除实体
     *
     * @return
     */
    @Transactional
    long deleteVague();

    /**
     * 精确匹配删除实体
     *
     * @return
     */
    long deleteAccuracy();

    /**
     * 模糊匹配分页查询
     *
     * @param t
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<T> pageVague(T t, int pageNum, int pageSize);

    /**
     * 精确匹配分页查询
     *
     * @param t
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<T> pageAccuracy(T t, int pageNum, int pageSize);

    /**
     * 通过ID，反射创建实体
     *
     * @param id
     * @return
     */
    T assembly(ID id);

    /**
     * 反射创建实体
     *
     * @return
     */
    T assembly();

    /**
     * 获取带有@MyId的field
     *
     * @param obj
     * @return
     */
    Field getIdField(ID obj);

    /*
     * 获取泛型的class
     *
     * @see
     * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    void afterPropertiesSet() throws Exception;

}
