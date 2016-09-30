package grails.plugin.hibernatehijacker.spring;

import grails.plugin.hibernatehijacker.hibernate.WrappedSessionFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.core.Ordered;
import org.hibernate.SessionFactory;
import java.util.Set;

/**
 * Replaces the default ConfigurableLocalSessionFactoryBean with WrappedSessionFactoryBean.
 * It will also make sure that our replacement is wired with the required dependencies.
 *
 * @author Kim A. Betti <kim.betti@gmail.com>
 */
public class SessionFactoryPostProcessor implements BeanFactoryPostProcessor, Ordered {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinition beanDef = beanFactory.getBeanDefinition("sessionFactory");
        beanDef.setBeanClassName(WrappedSessionFactoryBean.class.getName());
        setBeanProperties(beanFactory, beanDef);

        /*beanDef = beanFactory.getBeanDefinition("sessionFactory_bonita");
        beanDef.setBeanClassName(WrappedSessionFactoryBean.class.getName());
        setBeanProperties(beanFactory, beanDef);*/
        /*Object[] sessionFactoryBeanNames = beanFactory.getBeansOfType(SessionFactory.class).keySet().toArray();
        for(int i=0; i<sessionFactoryBeanNames.length; i++) {
            String sessionFactoryBeanName = (String)sessionFactoryBeanNames[i];
            System.out.println("sessionFactoryBeanName: " + sessionFactoryBeanName);
            BeanDefinition beanDef = beanFactory.getBeanDefinition(sessionFactoryBeanName);
            beanDef.setBeanClassName(WrappedSessionFactoryBean.class.getName());
            setBeanProperties(beanFactory, beanDef);
        }*/
    }

    private void setBeanProperties(ConfigurableListableBeanFactory beanFactory, BeanDefinition beanDef) {
        MutablePropertyValues properties = beanDef.getPropertyValues();
        setHibernateProxyFactoryReference(properties);
    }

    private void setHibernateProxyFactoryReference(MutablePropertyValues properties) {
        RuntimeBeanReference reference = new RuntimeBeanReference("sessionFactoryProxyFactory");
        properties.add("sessionFactoryProxyFactory", reference);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}