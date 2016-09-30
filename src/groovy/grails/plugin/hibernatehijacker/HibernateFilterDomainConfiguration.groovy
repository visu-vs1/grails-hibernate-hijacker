package grails.plugin.hibernatehijacker

import grails.plugin.hibernatehijacker.hibernate.HibernateConfigPostProcessor
import org.codehaus.groovy.grails.orm.hibernate.cfg.GrailsAnnotationConfiguration
import org.hibernate.MappingException
import org.hibernate.cfg.Configuration
import grails.util.Holders

/**
 * Add Filter to Domain Classes.
 * We are manually setting this class as default db config file. If any other plugin tries to set a different class then that won't work.
 *
 * @author Sandeep Poonia
 */
class HibernateFilterDomainConfiguration extends GrailsAnnotationConfiguration {

    private boolean configLocked

    @Override
    protected void secondPassCompile() throws MappingException {
        super.secondPassCompile()
        if (!configLocked) {
            doConfigPostProcessing(this)
            configLocked = true
        }
    }

    private void doConfigPostProcessing(Configuration configuration) {
        def applicationContext = Holders.grailsApplication.mainContext 
        Collection<HibernateConfigPostProcessor> processors = applicationContext.getBeansOfType(HibernateConfigPostProcessor.class).values();
        for (HibernateConfigPostProcessor processor : processors) {
            processor.doPostProcessing(configuration);
        }
    }
}
