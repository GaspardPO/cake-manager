package com.waracle.cakemgr;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class PersistenceService {

    private SessionFactory sessionFactory = buildSessionFactory();

    private SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration().configure(PersistenceService.class.getResource("/hibernate.cfg.xml"));
            StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();
            serviceRegistryBuilder.applySettings(configuration.getProperties());
            ServiceRegistry serviceRegistry = serviceRegistryBuilder.build();
            return configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    void shutdown() {
        sessionFactory.close();
    }

    void persist(List<CakeEntity> cakesEntities) {
        Session session = sessionFactory.openSession();
        cakesEntities.forEach(cake -> persist(cake, session));
        session.close();
    }

    void persist(CakeEntity newCake) {
        Session session = sessionFactory.openSession();
        persist(newCake, session);
        session.close();
    }

    private void persist(CakeEntity cake, Session session) {
        session.beginTransaction();
        session.persist(cake);
        System.out.println("persist cake entity");
        session.getTransaction().commit();
    }

    List<CakeEntity> getAllCakes() {
        Session session = sessionFactory.openSession();
        List<CakeEntity> list = session.createCriteria(CakeEntity.class).list();
        session.close();
        return list;
    }
}