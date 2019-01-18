package com.fendo.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.fendo.entity.User;

/**
 * dao层基类，实现增，删，改，查等常用功能。
 * 
 * @param <T>
 */
@Repository
public class BaseDao<T> {
	
	
    @Resource
    private SessionFactory sessionFactory;

	private Session currentSession() {
		return sessionFactory.getCurrentSession();
	}
    
    /**
     * 保存数据
     * 
     * @return void 返回类型
     */
    @Transactional
    public void save(T t) {
        sessionFactory.getCurrentSession().save(t);
    }

    /**
     * 删除数据
     * 
     * @return void 返回类型
     */
    @Transactional
    public void delete(Serializable id, Class<T> clazz) {
        T t = get(id, clazz);
        if (t != null)
            sessionFactory.getCurrentSession().delete(t);
        else
            new RuntimeException("你要删除的数据不存在").printStackTrace();
        ;
    }

    /**
     * 更新数据
     * 
     * @return void 返回类型
     */
    @Transactional
    public void update(T t) {
        sessionFactory.getCurrentSession().update(t);
    }

    /**
     * 根据ID查找数据
     * 
     * @return T 返回类型
     */
    @SuppressWarnings("unchecked")
    public T get(Serializable id, Class<T> clazz) {
        return (T) sessionFactory.getCurrentSession().get(clazz, id);
    }

    /**
     * 查找所有数据
     * 
     * @return List<T> 返回类型
     */
    @SuppressWarnings("unchecked")
    public List<T> getAll(Class<T> clazz) {
        return sessionFactory.getCurrentSession().createQuery(" from "+clazz.getSimpleName()).list();
    }
}