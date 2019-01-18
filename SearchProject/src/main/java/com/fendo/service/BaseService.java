package com.fendo.service;

import java.io.Serializable;
import java.util.List;

import javax.transaction.Transactional;

/**
 * Service层基类，定义通用的增，删，改，查功能接口。
 * 
 * @param <T>
 */
public interface BaseService<T> {
    /**
     * 保存数据
     * 
     * @return void 返回类型
     */
    
    public void save(T t);

    /**
     * 删除数据
     * @return void 返回类型
     */
    
    public void delete(Serializable id);

    /**
     * 更新数据
     * 
     * @return void 返回类型
     */
    public void update(T t);

    /**
     * 根据ID获取数据
     * 
     * @return T 返回类型
     */
    public T get(Serializable id);

    /**
     * 获取所有的数据
     * 
     * @return List<T> 返回类型
     */
    public List<T> getAll();
}
