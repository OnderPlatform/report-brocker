package tech.onder.modules;

import akka.actor.ActorRef;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.typesafe.config.Config;
import org.apache.log4j.xml.DOMConfigurator;
import play.Environment;
import play.Logger;
import play.libs.Json;
import play.libs.akka.AkkaGuiceSupport;
import tech.onder.collector.IConsumptionStorage;
import tech.onder.meters.models.dto.MeterInputDTO;
import tech.onder.meters.repositories.MeterRepo;
import tech.onder.meters.services.MeterService;
import tech.onder.meters.services.converters.MeterConverter;
import tech.onder.queue.IRestorableStorage;
import tech.onder.queue.actors.ForceUpdate;
import tech.onder.queue.conf.ConsumptionQueueConf;
import tech.onder.queue.manager.ConsumptionQueuesManager;
import tech.onder.reports.IReportDataSupplier;
import tech.onder.reports.actors.IWebsocketQueuePublisher;
import tech.onder.reports.actors.UserActor;

import javax.inject.Named;
import java.io.InputStream;
import java.util.Arrays;

public class ApplicationModule extends AbstractModule implements AkkaGuiceSupport {
    
    /**
     * Логгер
     */
    private final static Logger.ALogger LOGGER = Logger.of(ApplicationModule.class);
    
    private Config configuration;
    
    private AppConfig appConfig;
    
    private Environment environment;
    
    public ApplicationModule(Environment aEnvironment, Config aConfiguration) {
        this.configuration = aConfiguration;
        this.environment = aEnvironment;
        this.appConfig = new AppConfig(this.configuration);
        
    }
    
    @Override
    protected void configure() {
        
        DOMConfigurator.configure(appConfig.loggerConfigFile());

        bindActor(UserActor.class, "userActor");
        bindActor(ForceUpdate.class, "queueCleanActor");
        bind(IRestorableStorage.class).to(ConsumptionQueuesManager.class);//Instance(manager);
        bind(IConsumptionStorage.class).to(ConsumptionQueuesManager.class);
        bind(IWebsocketQueuePublisher.class).to(ConsumptionQueuesManager.class);
        bind(IReportDataSupplier.class).to(ConsumptionQueuesManager.class);

        LOGGER.info("Application initialization ");
    }
    
    
//    private ConsumptionQueuesManager getQueueManager(@Named("queueCleanActor") ActorRef updateActor) {
//        ConsumptionQueueConf conf = new ConsumptionQueueConf(this.configuration);
//        ConsumptionQueuesManager manager = new ConsumptionQueuesManager(conf, updateActor, environment.);
//        return manager;
//    }
    
    @Provides
    private MeterService provideMeterService(MeterConverter aMeterConverter, MeterRepo aMeterRepo) {
        MeterService meterService = new MeterService(aMeterConverter, aMeterRepo);
        appConfig.initMetersDataFile().ifPresent(
                (f) -> {
                    InputStream inputDataStream = this.environment.resourceAsStream(f);
                    JsonNode node = Json.parse(inputDataStream);
                    MeterInputDTO[] meters = Json.fromJson(node, MeterInputDTO[].class);
                    meterService.addList(Arrays.asList(meters));
                }
        );
        return meterService;
    }
    
}
