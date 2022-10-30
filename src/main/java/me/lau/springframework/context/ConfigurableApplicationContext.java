package me.lau.springframework.context;

import me.lau.springframework.beans.exception.BeansException;
import me.lau.springframework.beans.factory.BeanFactory;

public interface ConfigurableApplicationContext extends ApplicationContext {

    void refresh() throws BeansException;

    void close();

    void registerShutdownHook();

    void setParentBeanFactory(BeanFactory parentBeanFactory);

}
