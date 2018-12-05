// @GENERATOR:play-routes-compiler
// @SOURCE:C:/work/report-brocker/conf/routes
// @DATE:Wed Dec 05 02:01:15 CET 2018

import play.api.routing.JavaScriptReverseRoute


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:7
package tech.onder.queue.javascript {

  // @LINE:7
  class ReverseQueueController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:8
    def restore: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.queue.QueueController.restore",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "queue/restore"})
        }
      """
    )
  
    // @LINE:7
    def backup: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "tech.onder.queue.QueueController.backup",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "queue/backup"})
        }
      """
    )
  
  }


}
