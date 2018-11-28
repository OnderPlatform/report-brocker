// @GENERATOR:play-routes-compiler
// @SOURCE:C:/work/report-brocker/conf/routes
// @DATE:Wed Nov 28 19:19:03 CET 2018

package tech.onder.consumer;

import router.RoutesPrefix;

public class routes {
  
  public static final tech.onder.consumer.ReverseCollectorController CollectorController = new tech.onder.consumer.ReverseCollectorController(RoutesPrefix.byNamePrefix());

  public static class javascript {
    
    public static final tech.onder.consumer.javascript.ReverseCollectorController CollectorController = new tech.onder.consumer.javascript.ReverseCollectorController(RoutesPrefix.byNamePrefix());
  }

}
