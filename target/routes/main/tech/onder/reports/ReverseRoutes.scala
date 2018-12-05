// @GENERATOR:play-routes-compiler
// @SOURCE:C:/work/report-brocker/conf/routes
// @DATE:Wed Dec 05 05:20:03 CET 2018

import play.api.mvc.Call


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:18
package tech.onder.reports {

  // @LINE:18
  class ReverseReportController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:22
    def prices(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/reports/price")
    }
  
    // @LINE:20
    def lastConsumption(shift:Integer = null): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/reports/consumption/last" + play.core.routing.queryString(List(if(shift == null) None else Some(implicitly[play.api.mvc.QueryStringBindable[Integer]].unbind("shift", shift)))))
    }
  
    // @LINE:24
    def meters(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/reports/meters")
    }
  
    // @LINE:28
    def ws(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/ws")
    }
  
    // @LINE:18
    def consumption(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/reports/consumption")
    }
  
  }


}
