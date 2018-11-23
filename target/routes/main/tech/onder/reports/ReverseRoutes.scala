// @GENERATOR:play-routes-compiler
// @SOURCE:C:/work/report-brocker/conf/routes
// @DATE:Fri Nov 23 04:54:37 CET 2018

import play.api.mvc.Call


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:9
package tech.onder.reports {

  // @LINE:9
  class ReverseReportController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:13
    def meters(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "reports/meters")
    }
  
    // @LINE:9
    def consumption(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "reports/consumption")
    }
  
    // @LINE:11
    def prices(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "reports/price")
    }
  
    // @LINE:15
    def ws(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "ws")
    }
  
  }


}
