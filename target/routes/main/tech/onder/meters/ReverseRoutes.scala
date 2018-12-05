// @GENERATOR:play-routes-compiler
// @SOURCE:C:/work/report-brocker/conf/routes
// @DATE:Wed Dec 05 05:20:03 CET 2018

import play.api.mvc.Call


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:11
package tech.onder.meters {

  // @LINE:11
  class ReverseCommonController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:11
    def meters(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/meters")
    }
  
    // @LINE:13
    def addMeter(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "api/meters")
    }
  
    // @LINE:15
    def meterRelations(): Call = {
    
      () match {
      
        // @LINE:15
        case ()  =>
          
          Call("GET", _prefix + { _defaultPrefix } + "api/meters/relations")
      
      }
    
    }
  
  }


}
