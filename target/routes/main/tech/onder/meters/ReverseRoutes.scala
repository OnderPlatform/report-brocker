// @GENERATOR:play-routes-compiler
// @SOURCE:C:/work/report-brocker/conf/routes
// @DATE:Mon Nov 26 00:39:00 CET 2018

import play.api.mvc.Call


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:4
package tech.onder.meters {

  // @LINE:4
  class ReverseCommonController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:4
    def meters(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "meters")
    }
  
    // @LINE:6
    def meterRelations(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "meters/relations")
    }
  
  }


}
