package tech.onder.modules;

import com.google.inject.AbstractModule;
import com.typesafe.config.Config;
import org.apache.log4j.xml.DOMConfigurator;
import play.Environment;
import play.Logger;
import play.libs.akka.AkkaGuiceSupport;
import tech.onder.consumer.services.ChunkReportManagementService;
import tech.onder.reports.Publisher;
import tech.onder.reports.actors.UserActor;
import tech.onder.reports.actors.UserParentActor;

public class ApplicationModule extends AbstractModule implements AkkaGuiceSupport {

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
        DOMConfigurator.configure(appConfig.loggerConfigFile());
        bind(ChunkReportManagementService.class).toProvider(ChunkServiceProvider.class).asEagerSingleton();
        //bind(Publisher.class).toInstance(new Publisher());
        bindActor(UserParentActor.class, "userParentActor");
        bindActor(UserActor.class, "userActor");

        bindActorFactory(UserActor.class, UserActor.Factory.class);
        LOGGER.info("Application initialization ");
    }


}
