package me.lau.springframework.context.support;

import me.lau.springframework.beans.factory.DisposableBean;
import me.lau.springframework.beans.factory.ObjectFactory;
import me.lau.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    private Map<String, Object> singletonObjects = new HashMap<>();

    private Map<String, Object> earlySingletonObjects = new HashMap<>();

    private Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>();

    private Map<String, DisposableBean> disposableBeanMap = new HashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        Object singletonObject = singletonObjects.get(beanName);
        if(Objects.isNull(singletonObject)) {
            singletonObject = earlySingletonObjects.get(beanName);
            if (Objects.isNull(singletonObject)) {
                ObjectFactory<?> singletonFactory = singletonFactories.get(beanName);
                if(Objects.nonNull(singletonFactory)) {
                    singletonObject = singletonFactory.getObject();
                    earlySingletonObjects.put(beanName, singletonObject);
                }
            }
        }
        return singletonObject;
    }

    @Override
    public void addSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject);
        earlySingletonObjects.remove(beanName);
        singletonFactories.remove(beanName);
    }

    public void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
        singletonFactories.put(beanName, singletonFactory);
    }

    public void registerDisposableBean(String beanName, DisposableBean bean) {
        this.disposableBeanMap.put(beanName, bean);
    }

    public void destroySingletons() {
        disposableBeanMap.values().forEach(DisposableBean::destroy);
    }
}
