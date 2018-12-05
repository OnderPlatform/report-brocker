// @GENERATOR:play-routes-compiler
// @SOURCE:C:/work/report-brocker/conf/routes
// @DATE:Wed Dec 05 04:45:59 CET 2018

import play.api.mvc.Call


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:7
package tech.onder.queue {

  // @LINE:7
  class ReverseQueueController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:8
    def restore(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "queue/restore")
    }
  
    // @LINE:7
    def backup(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "queue/backup")
    }
  
  }


}
