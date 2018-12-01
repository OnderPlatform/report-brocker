// @GENERATOR:play-routes-compiler
// @SOURCE:C:/work/report-brocker/conf/routes
// @DATE:Sat Dec 01 04:05:03 CET 2018

import play.api.routing.JavaScriptReverseRoute


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:18
package tech.onder.reports.javascript {

  // @LINE:18
  class ReverseReportController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:22
    def prices: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.reports.ReportController.prices",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/reports/price"})
        }
      """
    )
  
    // @LINE:20
    def lastConsumption: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.reports.ReportController.lastConsumption",
      """
        function(shift0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/reports/consumption/last" + _qS([(shift0 == null ? null : (""" + implicitly[play.api.mvc.QueryStringBindable[Integer]].javascriptUnbind + """)("shift", shift0))])})
        }
      """
    )
  
    // @LINE:24
    def meters: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.reports.ReportController.meters",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/reports/meters"})
        }
      """
    )
  
    // @LINE:28
    def ws: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.reports.ReportController.ws",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/ws"})
        }
      """
    )
  
    // @LINE:18
    def consumption: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.reports.ReportController.consumption",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/reports/consumption"})
        }
      """
    )
  
  }


}
