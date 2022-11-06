package me.lau.springframework.context.support;

import me.lau.springframework.beans.PropertyValue;
import me.lau.springframework.beans.PropertyValues;
import me.lau.springframework.beans.exception.BeansException;
import me.lau.springframework.beans.factory.ObjectFactory;
import me.lau.springframework.beans.factory.config.AutowireCapableBeanFactory;
import me.lau.springframework.beans.factory.config.BeanDefinition;
import me.lau.springframework.beans.factory.config.BeanPostProcessor;
import me.lau.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import me.lau.springframework.beans.support.InstantiationStrategy;
import me.lau.springframework.beans.support.SimpleInstantiationStrategy;

import java.util.Objects;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory
        implements AutowireCapableBeanFactory {

    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException {
        Object bean = resolveBeforeInstantiation(beanName, beanDefinition);
        if(Objects.nonNull(bean)) {
            return bean;
        }

    }

    private Object resolveBeforeInstantiation(String beanName, BeanDefinition beanDefinition) {
        Object bean = applyBeanPostProcessorsBeforeInstantiation(beanDefinition.getBeanClass(), beanName);
        if(Objects.nonNull(bean)) {
            bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        }
        return bean;
    }

    private Object applyBeanPostProcessorsBeforeInstantiation(Class beanClass, String beanName) {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if(beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                Object result = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessBeforeInstantiation(beanClass, beanName);
                if(Objects.nonNull(result)) {
                    return result;
                }
            }
        }
        return null;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {

        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object currentResult = processor.postProcessAfterInitialization(result, beanName);
            // why: 这里为何为null则返回？而不是跳过？
            if(Objects.isNull(currentResult)) {
                return result;
            }
            result = currentResult;
        }
        return result;
    }

    protected Object doCreateBean(String beanName, BeanDefinition beanDefinition) {
        Object result = null;
        result = createBeanInstance(beanDefinition);

        // 循环依赖，设置第三级缓存
        if(beanDefinition.isSingleton()) {
            Object finalBean = result;
            addSingletonFactory(beanName, new ObjectFactory<Object>() {
                @Override
                public Object getObject() throws BeansException {
                    return getEarlyBeanReference(beanName, finalBean);
                }
            });
        }

        // 实例化后处理
        if(!applyBeanPostProcessorAfterInstantiation(beanName, result)){
            return result;
        }

        // 初始化之前，允许InstatiationAwarePostProcessor享有更改属性值的特权
        applyBeanPostProcesorsBeforeApplayingPropertyValues(beanName, beanName, beanDefinition);
        // 为Bean填充属性


    }

    private boolean applyBeanPostProcessorAfterInstantiation(String beanName, Object bean) {
        boolean continueWithPropertyPopulation = true;
        for (BeanPostProcessor bpp : getBeanPostProcessors()) {
            if(bpp instanceof InstantiationAwareBeanPostProcessor) {
                // why: 为何break？什么情况会return false？
                if(!((InstantiationAwareBeanPostProcessor) bpp).postProcessAfterInstantiation(bean, beanName)) {
                    continueWithPropertyPopulation = false;
                    break;
                }
            }
        }
        return continueWithPropertyPopulation;
    }

    private Object getEarlyBeanReference(String beanName, Object bean) {
        Object result = bean;
        for (BeanPostProcessor bpp : getBeanPostProcessors()) {
            if(bpp instanceof InstantiationAwareBeanPostProcessor) {
                result = ((InstantiationAwareBeanPostProcessor) bpp).getEarlyBeanReference(bean, beanName);
                if(Objects.isNull(result)) {
                    return result;
                }
            }
        }
        return result;
    }

    private Object createBeanInstance(BeanDefinition beanDefinition) {
        return getInstantiationStrategy().instantiate(beanDefinition);
    }

    private InstantiationStrategy getInstantiationStrategy() {
        return this.instantiationStrategy;
    }

    private void applyBeanPostProcesorsBeforeApplayingPropertyValues(String beanName, Object bean,
                                                                     BeanDefinition beanDefinition) {
        for (BeanPostProcessor bpp : getBeanPostProcessors()) {
            if(bpp instanceof InstantiationAwareBeanPostProcessor) {
                PropertyValues pvs = ((InstantiationAwareBeanPostProcessor) bpp)
                        .postProcessPropertyValues(beanDefinition.getPropertyValues(), bean, beanName);
                if(Objects.nonNull(pvs)) {
                    for (PropertyValue pv : pvs.getPropertyValues()) {
                        beanDefinition.getPropertyValues().addPropertyValue(pv);
                    }
                }
            }
        }
    }


}
