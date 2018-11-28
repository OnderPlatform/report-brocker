// @GENERATOR:play-routes-compiler
// @SOURCE:C:/work/report-brocker/conf/routes
// @DATE:Wed Nov 28 19:19:03 CET 2018

import play.api.routing.JavaScriptReverseRoute


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:4
package tech.onder.meters.javascript {

  // @LINE:4
  class ReverseCommonController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:4
    def meters: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.meters.CommonController.meters",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "meters"})
        }
      """
    )
  
    // @LINE:6
    def addMeter: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.meters.CommonController.addMeter",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "meters"})
        }
      """
    )
  
    // @LINE:8
    def meterRelations: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.meters.CommonController.meterRelations",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "meters/relations"})
        }
      """
    )
  
  }


}
