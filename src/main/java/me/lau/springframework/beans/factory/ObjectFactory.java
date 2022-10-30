package me.lau.springframework.beans.factory;

import me.lau.springframework.beans.exception.BeansException;

public interface ObjectFactory<T> {

    T getObject() throws BeansException;
}
