package me.lau.springframework.context.support;

import me.lau.springframework.beans.exception.BeansException;
import me.lau.springframework.beans.factory.FactoryBean;
import me.lau.springframework.beans.factory.config.BeanDefinition;
import me.lau.springframework.beans.factory.config.BeanPostProcessor;
import me.lau.springframework.beans.factory.config.ConfigurableBeanFactory;
import me.lau.springframework.core.convert.ConversionService;
import me.lau.springframework.utils.StringValueResolver;

import java.util.*;

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory {

    private final Map<String, Object> factoryBeanObjectCache = new HashMap<>();

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    private final List<StringValueResolver> embeddedValueResolvers = new ArrayList<>();

    @Override
    public Object getBean(String name) throws BeansException {
        Object sharedInstance = getSingleton(name);
        if(Objects.nonNull(sharedInstance)) {
            return getObjectForBeanInstance(sharedInstance, name);
        }

        BeanDefinition beanDefinition = getBeanDefinition(name);
        Object beanInstance = createBean(name, beanDefinition);
        return getObjectForBeanInstance(beanInstance, name);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return requiredType.cast(getBean(name));
    }

    /*@Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return null;
    }*/

    @Override
    public boolean containsBean(String name) {
        return containsBeanDefinition(name);
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        beanPostProcessors.remove(beanPostProcessor);
        beanPostProcessors.add(beanPostProcessor);
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

   /* @Override
    public void destorySingletons() {

    }*/

    @Override
    public void addEmbeddedValueResolver(StringValueResolver stringValueResolver) {
        this.embeddedValueResolvers.add(stringValueResolver);
    }

    @Override
    public String resolveEmbeddedValue(String value) {
        String result = value;
        for (StringValueResolver stringValueResolver : this.embeddedValueResolvers) {
            result = stringValueResolver.resolveStringValue(value);
        }
        return result;
    }

    @Override
    public void setConversionService(ConversionService conversionService) {

    }

    @Override
    public ConversionService getConversionService() {
        return null;
    }

    protected Object getObjectForBeanInstance(Object beanInstance, String beanName) throws BeansException {
        if(beanInstance instanceof FactoryBean) {
            FactoryBean factoryBean = (FactoryBean) beanInstance;
            if(factoryBean.isSingleton()) {
                if(factoryBeanObjectCache.containsKey(beanName)) {
                    return factoryBeanObjectCache.get(beanName);
                } else {
                    factoryBeanObjectCache.put(beanName, factoryBean.getObject());
                    return factoryBeanObjectCache.get(beanName);
                }
            } else {
                return factoryBean.getObject();
            }
        }
        return beanInstance;
    }

    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException;

    protected abstract boolean containsBeanDefinition(String beanName);
}
