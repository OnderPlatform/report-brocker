// @GENERATOR:play-routes-compiler
// @SOURCE:C:/work/report-brocker/conf/routes
// @DATE:Mon Dec 17 01:18:26 CET 2018

import play.api.routing.JavaScriptReverseRoute


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:16
package tech.onder.reports.controllers.javascript {

  // @LINE:16
  class ReverseReportController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:18
    def prices: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.reports.controllers.ReportController.prices",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/reports/price"})
        }
      """
    )
  
    // @LINE:17
    def lastConsumption: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.reports.controllers.ReportController.lastConsumption",
      """
        function(offset0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/reports/consumption/last" + _qS([(offset0 == null ? null : (""" + implicitly[play.api.mvc.QueryStringBindable[Integer]].javascriptUnbind + """)("offset", offset0))])})
        }
      """
    )
  
    // @LINE:19
    def meters: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.reports.controllers.ReportController.meters",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/reports/meters"})
        }
      """
    )
  
    // @LINE:20
    def ws: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.reports.controllers.ReportController.ws",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "ws"})
        }
      """
    )
  
    // @LINE:16
    def consumption: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.reports.controllers.ReportController.consumption",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/reports/consumption"})
        }
      """
    )
  
  }


}
