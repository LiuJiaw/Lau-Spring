package me.lau.springframework.context.support;

import me.lau.springframework.beans.exception.BeansException;
import me.lau.springframework.beans.factory.config.AutowireCapableBeanFactory;
import me.lau.springframework.beans.factory.config.BeanDefinition;
import me.lau.springframework.beans.factory.config.BeanPostProcessor;
import me.lau.springframework.beans.support.InstantiationStrategy;
import me.lau.springframework.beans.support.SimpleInstantiationStrategy;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory
        implements AutowireCapableBeanFactory {

    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException {
        Object bean =
    }

    private Object resolveBeforeInstantiation(String beanName, BeanDefinition beanDefinition) {

    }

    private Object applyBeanPostProcessorsBeforeInstantiation(Class beanClass, String beanName) {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if(beanPostProcessor instanceof )
        }
    }
}
