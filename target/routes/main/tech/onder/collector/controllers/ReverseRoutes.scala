// @GENERATOR:play-routes-compiler
// @SOURCE:C:/work/report-brocker/conf/routes
// @DATE:Mon Dec 17 01:18:26 CET 2018

import play.api.mvc.Call


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:2
package tech.onder.collector.controllers {

  // @LINE:2
  class ReverseCollectorController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:3
    def pushList(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "api/meters/fromlog")
    }
  
    // @LINE:2
    def push(id:String): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "api/meters/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("id", id)))
    }
  
  }


}
