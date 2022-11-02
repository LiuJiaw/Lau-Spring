package me.lau.springframework.beans.factory.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.TypeUtil;
import me.lau.springframework.beans.PropertyValues;
import me.lau.springframework.beans.exception.BeansException;
import me.lau.springframework.beans.factory.BeanFactory;
import me.lau.springframework.beans.factory.BeanFactoryAware;
import me.lau.springframework.beans.factory.ConfigurableListableBeanFactory;
import me.lau.springframework.beans.factory.annotation.Autowired;
import me.lau.springframework.beans.factory.annotation.Value;
import me.lau.springframework.core.convert.ConversionService;

import java.lang.reflect.Field;
import java.util.Objects;

public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {

    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return false;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();

        // 处理@Value注解
        for (Field field : fields) {
            Value valueAnnotation = field.getAnnotation(Value.class);
            if(Objects.nonNull(valueAnnotation)) {
                Object value = valueAnnotation.value();
                value = beanFactory.resolveEmbeddedValue(String.valueOf(value));

                Class<?> sourceType = value.getClass();
                Class<?> targetType = field.getType();
                ConversionService conversionService = beanFactory.getConversionService();
                if(Objects.nonNull(conversionService) && conversionService.canConvert(sourceType, targetType)) {
                    value = conversionService.convert(value, targetType);
                }

                BeanUtil.setFieldValue(bean, field.getName(), value);
            }
        }

        // 处理@Autowired注解
        for (Field field : fields) {
            Autowired autowiredAnnotation = field.getAnnotation(Autowired.class);
            if(Objects.nonNull(autowiredAnnotation)) {
                Class<?> fieldType = field.getType();

            }
        }
    }

    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
        return InstantiationAwareBeanPostProcessor.super.getEarlyBeanReference(bean, beanName);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }
}
