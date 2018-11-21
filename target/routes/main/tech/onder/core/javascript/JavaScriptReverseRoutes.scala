// @GENERATOR:play-routes-compiler
// @SOURCE:C:/work/report-brocker/conf/routes
// @DATE:Wed Nov 21 20:50:36 CET 2018

import play.api.routing.JavaScriptReverseRoute


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:4
package tech.onder.core.javascript {

  // @LINE:4
  class ReverseCommonController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:4
    def meters: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.core.CommonController.meters",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "meters"})
        }
      """
    )
  
    // @LINE:6
    def meterRelations: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.core.CommonController.meterRelations",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "meters/relations"})
        }
      """
    )
  
  }


}
