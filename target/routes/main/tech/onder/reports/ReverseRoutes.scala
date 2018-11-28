// @GENERATOR:play-routes-compiler
// @SOURCE:C:/work/report-brocker/conf/routes
// @DATE:Wed Nov 28 18:30:04 CET 2018

import play.api.mvc.Call


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:11
package tech.onder.reports {

  // @LINE:11
  class ReverseReportController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:15
    def meters(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "reports/meters")
    }
  
    // @LINE:11
    def consumption(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "reports/consumption")
    }
  
    // @LINE:13
    def prices(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "reports/price")
    }
  
    // @LINE:19
    def ws(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "ws")
    }
  
  }


}
