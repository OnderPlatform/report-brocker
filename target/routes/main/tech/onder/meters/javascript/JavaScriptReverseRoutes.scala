// @GENERATOR:play-routes-compiler
// @SOURCE:C:/work/report-brocker/conf/routes
// @DATE:Wed Dec 05 05:20:03 CET 2018

import play.api.routing.JavaScriptReverseRoute


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:11
package tech.onder.meters.javascript {

  // @LINE:11
  class ReverseCommonController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:11
    def meters: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.meters.CommonController.meters",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/meters"})
        }
      """
    )
  
    // @LINE:13
    def addMeter: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.meters.CommonController.addMeter",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "api/meters"})
        }
      """
    )
  
    // @LINE:15
    def meterRelations: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.meters.CommonController.meterRelations",
      """
        function() {
        
          if (true) {
            return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/meters/relations"})
          }
        
        }
      """
    )
  
  }


}
