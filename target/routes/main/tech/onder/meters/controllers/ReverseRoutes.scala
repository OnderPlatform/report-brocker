// @GENERATOR:play-routes-compiler
// @SOURCE:C:/work/report-brocker/conf/routes
// @DATE:Mon Dec 17 01:18:26 CET 2018

import play.api.mvc.Call


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:10
package tech.onder.meters.controllers {

  // @LINE:10
  class ReverseMeterController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:10
    def list(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/meters")
    }
  
    // @LINE:12
    def meterRelations(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/meters/relations")
    }
  
    // @LINE:11
    def add(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "api/meters")
    }
  
  }


}
