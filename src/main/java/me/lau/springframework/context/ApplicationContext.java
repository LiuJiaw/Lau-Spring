package me.lau.springframework.context;

import me.lau.springframework.beans.factory.HierarchicalBeanFactory;
import me.lau.springframework.beans.factory.ListableBeanFactory;
import me.lau.springframework.core.io.ResourceLoader;

public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader {
}
