// @GENERATOR:play-routes-compiler
// @SOURCE:C:/work/report-brocker/conf/routes
// @DATE:Mon Nov 26 05:03:16 CET 2018

import play.api.routing.JavaScriptReverseRoute


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:9
package tech.onder.reports.javascript {

  // @LINE:9
  class ReverseReportController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:13
    def meters: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.reports.ReportController.meters",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "reports/meters"})
        }
      """
    )
  
    // @LINE:9
    def consumption: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.reports.ReportController.consumption",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "reports/consumption"})
        }
      """
    )
  
    // @LINE:11
    def prices: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.reports.ReportController.prices",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "reports/price"})
        }
      """
    )
  
    // @LINE:15
    def ws: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.reports.ReportController.ws",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "ws"})
        }
      """
    )
  
  }


}
