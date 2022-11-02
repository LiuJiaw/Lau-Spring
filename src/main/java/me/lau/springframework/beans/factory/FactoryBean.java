package me.lau.springframework.beans.factory;

import me.lau.springframework.beans.exception.BeansException;

public interface FactoryBean<T> {

    T getObject() throws BeansException;

    boolean isSingleton();
}
