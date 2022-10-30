package me.lau.springframework.beans.factory;

import me.lau.springframework.beans.PropertyValue;
import me.lau.springframework.beans.PropertyValues;
import me.lau.springframework.beans.exception.BeansException;
import me.lau.springframework.beans.factory.config.BeanDefinition;
import me.lau.springframework.beans.factory.config.BeanFactoryPostProcessor;
import me.lau.springframework.core.io.DefaultResourceLoader;
import me.lau.springframework.core.io.Resource;
import me.lau.springframework.utils.StringValueResolver;

import java.io.IOException;
import java.util.Properties;

public class PropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {

    private static final String PLACEHOLDER_PREFIX = "$(";

    private static final String PLACEHOLDER_SUFFIX = ")";

    private static final String PROPERTY_LOCATION = "classpath:application.properties";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        Properties properties = loadProperties();

        processProperties(beanFactory, properties);

        StringValueResolver stringValueResolver = new PlaceholderStringValueResolver(properties);
        beanFactory.addEmbeddedValueResolver(stringValueResolver);
    }

    private Properties loadProperties() {
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource(PROPERTY_LOCATION);
        Properties properties = new Properties();
        try {
            properties.load(resource.getInputStream());
        } catch (IOException ex) {
            throw new BeansException("Can not load properties", ex);
        }
        return properties;
    }

    private void processProperties(ConfigurableListableBeanFactory beanFactory,
                                   Properties properties)
            throws BeansException {
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for (String beanName : beanDefinitionNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            resolvePropertyValues(beanDefinition, properties);
        }
    }

    private void resolvePropertyValues(BeanDefinition beanDefinition, Properties properties) {
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        for (PropertyValue pv : propertyValues.getPropertyValues()) {
            Object value = pv.getValue();
            if(value instanceof String) {
                value = resolvePlaceHolder((String) value, properties);
                propertyValues.addPropertyValue(new PropertyValue(pv.getName(), value));
            }
        }
    }

    private String resolvePlaceHolder(String value, Properties properties) {
        String result = null;
        int leftIndex = value.indexOf(PLACEHOLDER_PREFIX);
        int rightIndex = value.indexOf(PLACEHOLDER_SUFFIX);
        if(leftIndex>=0 && rightIndex>=0 && leftIndex<rightIndex) {
            String replacedVal = value.substring(leftIndex+2, rightIndex);
            String targetVal = properties.getProperty(replacedVal);
            result = value.replaceFirst(replacedVal, targetVal);
        }
        return result;
    }

    private class PlaceholderStringValueResolver implements StringValueResolver {

        private final Properties properties;

        public PlaceholderStringValueResolver(Properties properties) {
            this.properties = properties;
        }

        public String resolveStringValue(String value) throws BeansException {
            return PropertyPlaceholderConfigurer.this.resolvePlaceHolder(value, properties);
        }
    }
}
