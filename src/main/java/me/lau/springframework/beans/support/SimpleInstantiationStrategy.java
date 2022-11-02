package me.lau.springframework.beans.support;

import me.lau.springframework.beans.exception.BeansException;
import me.lau.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

public class SimpleInstantiationStrategy implements InstantiationStrategy{

    @Override
    public Object instantiate(BeanDefinition beanDefinition) throws BeansException {
        Class beanClass = beanDefinition.getBeanClass();

        try {
            Constructor constructor = beanClass.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            throw new BeansException(String.format("Failed to instantiate [%s] ", beanClass.getName()));
        }
    }
}
