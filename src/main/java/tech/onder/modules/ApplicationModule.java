package tech.onder.modules;

import com.google.inject.AbstractModule;
import com.typesafe.config.Config;
import org.apache.log4j.xml.DOMConfigurator;
import play.Environment;
import play.Logger;

public class ApplicationModule extends AbstractModule {

    /** Логгер */
    private final static Logger.ALogger LOGGER = Logger.of(ApplicationModule.class);

    private Config configuration;

    private AppConfig appConfig;

    public ApplicationModule(Environment aEnvironment, Config aConfiguration) {
        this.configuration = aConfiguration;
        this.appConfig = new AppConfig(this.configuration);
        //runMode = aConfiguration.getString("run.mode", "acc");
    }

    @Override
    protected void configure() {
        System.out.println(appConfig.loggerConfigFile());
        DOMConfigurator.configure(appConfig.loggerConfigFile());

        LOGGER.info("Application initialization ");
    }
}
