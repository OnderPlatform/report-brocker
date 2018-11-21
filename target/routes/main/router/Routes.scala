// @GENERATOR:play-routes-compiler
// @SOURCE:C:/work/report-brocker/conf/routes
// @DATE:Thu Nov 22 00:06:44 CET 2018

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._

import play.api.mvc._

import _root_.controllers.Assets.Asset
import _root_.play.libs.F

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:2
  CollectorController_2: tech.onder.consumer.CollectorController,
  // @LINE:4
  CommonController_0: tech.onder.core.CommonController,
  // @LINE:9
  ReportController_1: tech.onder.reports.ReportController,
  val prefix: String
) extends GeneratedRouter {

   @javax.inject.Inject()
   def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:2
    CollectorController_2: tech.onder.consumer.CollectorController,
    // @LINE:4
    CommonController_0: tech.onder.core.CommonController,
    // @LINE:9
    ReportController_1: tech.onder.reports.ReportController
  ) = this(errorHandler, CollectorController_2, CommonController_0, ReportController_1, "/")

  def withPrefix(prefix: String): Routes = {
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, CollectorController_2, CommonController_0, ReportController_1, prefix)
  }

  private[this] val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/meters/""" + "$" + """id<[^/]+>""", """tech.onder.consumer.CollectorController.push(id:String)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """meters""", """tech.onder.core.CommonController.meters()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """meters/relations""", """tech.onder.core.CommonController.meterRelations()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """reports/consumption""", """tech.onder.reports.ReportController.consumption()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """reports/price""", """tech.onder.reports.ReportController.prices()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """ws""", """tech.onder.reports.ReportController.ws()"""),
    Nil
  ).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
    case l => s ++ l.asInstanceOf[List[(String,String,String)]]
  }}


  // @LINE:2
  private[this] lazy val tech_onder_consumer_CollectorController_push0_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/meters/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val tech_onder_consumer_CollectorController_push0_invoker = createInvoker(
    CollectorController_2.push(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.consumer.CollectorController",
      "push",
      Seq(classOf[String]),
      "POST",
      this.prefix + """api/meters/""" + "$" + """id<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:4
  private[this] lazy val tech_onder_core_CommonController_meters1_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("meters")))
  )
  private[this] lazy val tech_onder_core_CommonController_meters1_invoker = createInvoker(
    CommonController_0.meters(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.core.CommonController",
      "meters",
      Nil,
      "GET",
      this.prefix + """meters""",
      """""",
      Seq()
    )
  )

  // @LINE:6
  private[this] lazy val tech_onder_core_CommonController_meterRelations2_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("meters/relations")))
  )
  private[this] lazy val tech_onder_core_CommonController_meterRelations2_invoker = createInvoker(
    CommonController_0.meterRelations(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.core.CommonController",
      "meterRelations",
      Nil,
      "GET",
      this.prefix + """meters/relations""",
      """""",
      Seq()
    )
  )

  // @LINE:9
  private[this] lazy val tech_onder_reports_ReportController_consumption3_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("reports/consumption")))
  )
  private[this] lazy val tech_onder_reports_ReportController_consumption3_invoker = createInvoker(
    ReportController_1.consumption(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.reports.ReportController",
      "consumption",
      Nil,
      "GET",
      this.prefix + """reports/consumption""",
      """""",
      Seq()
    )
  )

  // @LINE:11
  private[this] lazy val tech_onder_reports_ReportController_prices4_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("reports/price")))
  )
  private[this] lazy val tech_onder_reports_ReportController_prices4_invoker = createInvoker(
    ReportController_1.prices(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.reports.ReportController",
      "prices",
      Nil,
      "GET",
      this.prefix + """reports/price""",
      """""",
      Seq()
    )
  )

  // @LINE:13
  private[this] lazy val tech_onder_reports_ReportController_ws5_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("ws")))
  )
  private[this] lazy val tech_onder_reports_ReportController_ws5_invoker = createInvoker(
    ReportController_1.ws(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.reports.ReportController",
      "ws",
      Nil,
      "GET",
      this.prefix + """ws""",
      """""",
      Seq()
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:2
    case tech_onder_consumer_CollectorController_push0_route(params@_) =>
      call(params.fromPath[String]("id", None)) { (id) =>
        tech_onder_consumer_CollectorController_push0_invoker.call(CollectorController_2.push(id))
      }
  
    // @LINE:4
    case tech_onder_core_CommonController_meters1_route(params@_) =>
      call { 
        tech_onder_core_CommonController_meters1_invoker.call(CommonController_0.meters())
      }
  
    // @LINE:6
    case tech_onder_core_CommonController_meterRelations2_route(params@_) =>
      call { 
        tech_onder_core_CommonController_meterRelations2_invoker.call(CommonController_0.meterRelations())
      }
  
    // @LINE:9
    case tech_onder_reports_ReportController_consumption3_route(params@_) =>
      call { 
        tech_onder_reports_ReportController_consumption3_invoker.call(ReportController_1.consumption())
      }
  
    // @LINE:11
    case tech_onder_reports_ReportController_prices4_route(params@_) =>
      call { 
        tech_onder_reports_ReportController_prices4_invoker.call(ReportController_1.prices())
      }
  
    // @LINE:13
    case tech_onder_reports_ReportController_ws5_route(params@_) =>
      call { 
        tech_onder_reports_ReportController_ws5_invoker.call(ReportController_1.ws())
      }
  }
}
