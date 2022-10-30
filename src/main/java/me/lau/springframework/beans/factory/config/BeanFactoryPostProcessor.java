package me.lau.springframework.beans.factory.config;

import me.lau.springframework.beans.exception.BeansException;
import me.lau.springframework.beans.factory.ConfigurableListableBeanFactory;

public interface BeanFactoryPostProcessor {

    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;
}
