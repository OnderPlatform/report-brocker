// @GENERATOR:play-routes-compiler
// @SOURCE:C:/work/report-brocker/conf/routes
// @DATE:Wed Nov 21 20:50:36 CET 2018


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}