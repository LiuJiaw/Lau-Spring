package me.lau.springframework.beans.factory;

import me.lau.springframework.beans.exception.BeansException;
import me.lau.springframework.beans.factory.config.AutowireCapableBeanFactory;
import me.lau.springframework.beans.factory.config.BeanDefinition;
import me.lau.springframework.beans.factory.config.BeanPostProcessor;
import me.lau.springframework.beans.factory.config.ConfigurableBeanFactory;

public interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {

    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    void preInstantiateSingletons() throws BeansException;

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}
