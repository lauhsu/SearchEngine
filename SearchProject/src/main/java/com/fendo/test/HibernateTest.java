package com.fendo.test;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fendo.entity.User;

public class HibernateTest {
	
	StandardServiceRegistry registry =  null;
    SessionFactory sessionFactory = null;
    Session session = null;
    Transaction transaction = null;
    
	@Before
    public void init() {

        registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        session = sessionFactory.openSession();
        //开始事务
        transaction = session.getTransaction();
        transaction.begin();
    }
	
	@Test
    public void testSaveUser() {
         User user = new User();
        user.setName("fendo");
        session.save(user);
    }

    @After 
    public void destroy(){
        transaction.commit();
        session.close();
        sessionFactory.close();
        StandardServiceRegistryBuilder.destroy(registry);
    }

}
