// @GENERATOR:play-routes-compiler
// @SOURCE:C:/work/report-brocker/conf/routes
// @DATE:Wed Nov 21 20:50:36 CET 2018

package tech.onder.consumer;

import router.RoutesPrefix;

public class routes {
  
  public static final tech.onder.consumer.ReverseCollectorController CollectorController = new tech.onder.consumer.ReverseCollectorController(RoutesPrefix.byNamePrefix());

  public static class javascript {
    
    public static final tech.onder.consumer.javascript.ReverseCollectorController CollectorController = new tech.onder.consumer.javascript.ReverseCollectorController(RoutesPrefix.byNamePrefix());
  }

}