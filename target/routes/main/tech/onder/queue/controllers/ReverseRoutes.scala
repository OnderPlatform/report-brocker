// @GENERATOR:play-routes-compiler
// @SOURCE:C:/work/report-brocker/conf/routes
// @DATE:Mon Dec 17 01:18:26 CET 2018

import play.api.mvc.Call


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:6
package tech.onder.queue.controllers {

  // @LINE:6
  class ReverseQueueController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:7
    def restore(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "api/queue/restore")
    }
  
    // @LINE:6
    def backup(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "api/queue/backup")
    }
  
  }


}
