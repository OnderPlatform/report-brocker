// @GENERATOR:play-routes-compiler
// @SOURCE:C:/work/report-brocker/conf/routes
// @DATE:Wed Nov 28 19:19:03 CET 2018

import play.api.routing.JavaScriptReverseRoute


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:11
package tech.onder.reports.javascript {

  // @LINE:11
  class ReverseReportController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:15
    def meters: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.reports.ReportController.meters",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "reports/meters"})
        }
      """
    )
  
    // @LINE:11
    def consumption: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.reports.ReportController.consumption",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "reports/consumption"})
        }
      """
    )
  
    // @LINE:13
    def prices: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.reports.ReportController.prices",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "reports/price"})
        }
      """
    )
  
    // @LINE:19
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
