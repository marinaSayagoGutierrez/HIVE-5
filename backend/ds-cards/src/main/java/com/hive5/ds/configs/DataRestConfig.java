package com.hive5.ds.configs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;

@Configuration
public class DataRestConfig implements RepositoryRestConfigurer{

    private EntityManager entityManager;

    @Autowired
    public DataRestConfig(EntityManager theEntityManager){
        entityManager = theEntityManager;
    }


    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        //Exposses "id" parameter on api responses
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
        List<Class> entityClasses = new ArrayList<>();

        for(EntityType tempEntity : entities){
            entityClasses.add(tempEntity.getJavaType());
        }

        Class[] classes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(classes);
    }
}