package me.lau.springframework.beans.support;

import me.lau.springframework.beans.exception.BeansException;
import me.lau.springframework.beans.factory.config.BeanDefinition;

public interface InstantiationStrategy {

    Object instantiate(BeanDefinition beanDefinition) throws BeansException;
}
