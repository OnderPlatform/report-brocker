// @GENERATOR:play-routes-compiler
// @SOURCE:C:/work/report-brocker/conf/routes
// @DATE:Mon Dec 17 01:18:26 CET 2018

import play.api.mvc.Call


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:16
package tech.onder.reports.controllers {

  // @LINE:16
  class ReverseReportController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:18
    def prices(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/reports/price")
    }
  
    // @LINE:17
    def lastConsumption(offset:Integer = null): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/reports/consumption/last" + play.core.routing.queryString(List(if(offset == null) None else Some(implicitly[play.api.mvc.QueryStringBindable[Integer]].unbind("offset", offset)))))
    }
  
    // @LINE:19
    def meters(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/reports/meters")
    }
  
    // @LINE:20
    def ws(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "ws")
    }
  
    // @LINE:16
    def consumption(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/reports/consumption")
    }
  
  }


}
