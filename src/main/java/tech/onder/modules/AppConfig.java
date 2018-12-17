package tech.onder.modules;

import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;

public class AppConfig {
    
    private Logger logger = LoggerFactory.getLogger(AppConfig.class);
    
    private Config configuration;
    
    @Inject
    public AppConfig(Config configuration) {
        this.configuration = configuration;
    }
    
    public String loggerConfigFile() {
        return Optional.ofNullable(configuration.getString("logger.config")).orElse("log4j.def.xml");
    }
    
    public Optional<String> initMetersDataFile() {
        return Optional.ofNullable(configuration.getString("meters.initData.file"));
    }
    
}
