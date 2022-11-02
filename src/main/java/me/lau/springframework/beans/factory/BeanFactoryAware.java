package me.lau.springframework.beans.factory;

import me.lau.springframework.beans.exception.BeansException;

public interface BeanFactoryAware extends Aware {

    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
