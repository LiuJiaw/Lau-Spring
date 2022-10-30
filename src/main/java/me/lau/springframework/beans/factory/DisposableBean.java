package me.lau.springframework.beans.factory;

import me.lau.springframework.beans.exception.BeansException;

public interface DisposableBean {

    void destroy() throws BeansException;
}
