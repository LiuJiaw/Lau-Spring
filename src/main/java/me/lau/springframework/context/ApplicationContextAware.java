package me.lau.springframework.context;

import me.lau.springframework.beans.exception.BeansException;
import me.lau.springframework.beans.factory.Aware;

public interface ApplicationContextAware extends Aware {

    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
