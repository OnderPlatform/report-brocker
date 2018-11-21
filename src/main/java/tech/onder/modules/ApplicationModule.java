package tech.onder.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.typesafe.config.Config;
import org.apache.log4j.xml.DOMConfigurator;
import play.Environment;
import play.Logger;
import tech.onder.consumer.services.ChunkReportManagementService;
import tech.onder.reports.actors.UserActor;
import tech.onder.reports.actors.UserParentActor;

import play.libs.akka.AkkaGuiceSupport;

import java.net.URL;

public class ApplicationModule extends AbstractModule implements AkkaGuiceSupport{

    /**
     * Логгер
     */
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
        URL u = getClass().getClassLoader().getResource("log4j.xml");
        DOMConfigurator.configure(appConfig.loggerConfigFile());
        bind(ChunkReportManagementService.class).toProvider(ChunkServiceProvider.class).asEagerSingleton();
        bindActor(UserParentActor.class, "userParentActor");
        bindActorFactory(UserActor.class, UserActor.Factory.class);
        LOGGER.info("Application initialization ");
    }


}
