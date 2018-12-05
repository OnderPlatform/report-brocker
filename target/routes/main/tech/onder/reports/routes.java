// @GENERATOR:play-routes-compiler
// @SOURCE:C:/work/report-brocker/conf/routes
// @DATE:Wed Dec 05 05:07:31 CET 2018

package tech.onder.reports;

import router.RoutesPrefix;

public class routes {
  
  public static final tech.onder.reports.ReverseReportController ReportController = new tech.onder.reports.ReverseReportController(RoutesPrefix.byNamePrefix());

  public static class javascript {
    
    public static final tech.onder.reports.javascript.ReverseReportController ReportController = new tech.onder.reports.javascript.ReverseReportController(RoutesPrefix.byNamePrefix());
  }

}
