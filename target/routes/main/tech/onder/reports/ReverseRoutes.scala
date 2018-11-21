// @GENERATOR:play-routes-compiler
// @SOURCE:C:/work/report-brocker/conf/routes
// @DATE:Thu Nov 22 00:06:44 CET 2018

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

  
    // @LINE:9
    def consumption(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "reports/consumption")
    }
  
    // @LINE:11
    def prices(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "reports/price")
    }
  
    // @LINE:13
    def ws(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "ws")
    }
  
  }


}
