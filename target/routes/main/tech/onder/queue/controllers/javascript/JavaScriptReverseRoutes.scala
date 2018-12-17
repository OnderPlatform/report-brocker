// @GENERATOR:play-routes-compiler
// @SOURCE:C:/work/report-brocker/conf/routes
// @DATE:Mon Dec 17 01:18:26 CET 2018

import play.api.routing.JavaScriptReverseRoute


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:6
package tech.onder.queue.controllers.javascript {

  // @LINE:6
  class ReverseQueueController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:7
    def restore: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.queue.controllers.QueueController.restore",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "api/queue/restore"})
        }
      """
    )
  
    // @LINE:6
    def backup: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.queue.controllers.QueueController.backup",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "api/queue/backup"})
        }
      """
    )
  
  }


}
