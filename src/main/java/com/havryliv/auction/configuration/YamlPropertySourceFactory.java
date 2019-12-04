package com.havryliv.auction.configuration;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.Properties;

public class YamlPropertySourceFactory implements PropertySourceFactory {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public PropertySource<?> createPropertySource(@Nullable String name, EncodedResource resource) {
        Properties effectiveProperties = new Properties();

        // First, load all properties from the default resource
        Resource defaultResource = resource.getResource();
        if (defaultResource.exists()) {
            log.info("Activating default configurations: {}.", resource.getResource().getFilename());
            effectiveProperties.putAll(this.loadYamlIntoProperties(defaultResource));

        }

        // Next, if profile-specific properties exist, merge them in and override the default values
        Resource profileResource = this.findResourceBasedOnActiveProfiles(resource);
        if (profileResource == null)
            Assert.isTrue(defaultResource.exists(), String.format("The YAML file %s does not exist.", defaultResource.getFilename()));
        else {
            log.info("Activating profile-specific configurations: {}.", profileResource.getFilename());
            effectiveProperties.putAll(this.loadYamlIntoProperties(profileResource));

        }

        return new PropertiesPropertySource((StringUtils.isNotBlank(name)) ? name : resource.getResource().getFilename(), effectiveProperties);
    }

    private Properties loadYamlIntoProperties(Resource resource) {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource);
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    private Resource findResourceBasedOnActiveProfiles(EncodedResource resource) {
        String   resourceName   = resource.getResource().getFilename();
        String[] activeProfiles = this.getActiveRuntimeProfiles();

        for (String activeProfile : activeProfiles) {
            // Assuming the format of the YAML file's name is "<RESOURCE_NAME>_<PROFILE_NAME>.yml"
            String resourceNameRelativeToActiveProfile = resourceName.replaceFirst("\\.yml", String.format("_%s.yml", activeProfile));

            /*
             * If a resource was indeed created for this active profile, return it.
             * We're making an implicit assumption here that there will NEVER be more
             * than ONE profile-specific resource given a number of active profiles.
             *
             * For example, if the application was started with two active profiles
             * "dev" and "microservice", there can ONLY be either "resource_dev" or
             * "resource_microservice", not both.
             *
             * In other words, we can only categorize configurations in ONE dimension.
             * Otherwise, we need extra logic to decide which configurations should
             * override the other (i.e. which one is of higher priority).
             *
             */
            Resource potentialResource = new ClassPathResource(resourceNameRelativeToActiveProfile);
            if (potentialResource.exists())
                return potentialResource;
        }

        return null;
    }

    /**
     * Return the set of profiles explicitly made active for this environment. Profiles
     * are used for creating logical groupings of bean definitions to be registered
     * conditionally, for example based on deployment environment. Profiles can be
     * activated adding the command line argument -Dspring.profiles.active upon starting
     * the application (e.g. java -jar -Dspring.profiles.active=prod myapp.jar).
     *
     */
    private String[] getActiveRuntimeProfiles() {
        String profileConfig = System.getProperty("spring.profiles.active");
        return StringUtils.isBlank(profileConfig) ? new String[] {} : profileConfig.split(",");
    }
}
