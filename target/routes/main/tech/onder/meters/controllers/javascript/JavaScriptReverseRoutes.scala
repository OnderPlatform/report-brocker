// @GENERATOR:play-routes-compiler
// @SOURCE:C:/work/report-brocker/conf/routes
// @DATE:Mon Dec 17 01:18:26 CET 2018

import play.api.routing.JavaScriptReverseRoute


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:10
package tech.onder.meters.controllers.javascript {

  // @LINE:10
  class ReverseMeterController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:10
    def list: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.meters.controllers.MeterController.list",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/meters"})
        }
      """
    )
  
    // @LINE:12
    def meterRelations: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.meters.controllers.MeterController.meterRelations",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/meters/relations"})
        }
      """
    )
  
    // @LINE:11
    def add: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.meters.controllers.MeterController.add",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "api/meters"})
        }
      """
    )
  
  }


}
