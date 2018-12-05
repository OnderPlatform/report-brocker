// @GENERATOR:play-routes-compiler
// @SOURCE:C:/work/report-brocker/conf/routes
// @DATE:Wed Dec 05 05:07:31 CET 2018

package tech.onder.queue;

import router.RoutesPrefix;

public class routes {
  
  public static final tech.onder.queue.ReverseQueueController QueueController = new tech.onder.queue.ReverseQueueController(RoutesPrefix.byNamePrefix());

  public static class javascript {
    
    public static final tech.onder.queue.javascript.ReverseQueueController QueueController = new tech.onder.queue.javascript.ReverseQueueController(RoutesPrefix.byNamePrefix());
  }

}
