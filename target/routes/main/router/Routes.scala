// @GENERATOR:play-routes-compiler
// @SOURCE:C:/work/report-brocker/conf/routes
// @DATE:Mon Dec 17 01:18:26 CET 2018

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._

import play.api.mvc._

import _root_.controllers.Assets.Asset
import _root_.play.libs.F

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:2
  CollectorController_3: tech.onder.collector.controllers.CollectorController,
  // @LINE:6
  QueueController_0: tech.onder.queue.controllers.QueueController,
  // @LINE:10
  MeterController_2: tech.onder.meters.controllers.MeterController,
  // @LINE:16
  ReportController_1: tech.onder.reports.controllers.ReportController,
  val prefix: String
) extends GeneratedRouter {

   @javax.inject.Inject()
   def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:2
    CollectorController_3: tech.onder.collector.controllers.CollectorController,
    // @LINE:6
    QueueController_0: tech.onder.queue.controllers.QueueController,
    // @LINE:10
    MeterController_2: tech.onder.meters.controllers.MeterController,
    // @LINE:16
    ReportController_1: tech.onder.reports.controllers.ReportController
  ) = this(errorHandler, CollectorController_3, QueueController_0, MeterController_2, ReportController_1, "/")

  def withPrefix(prefix: String): Routes = {
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, CollectorController_3, QueueController_0, MeterController_2, ReportController_1, prefix)
  }

  private[this] val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/meters/""" + "$" + """id<[^/]+>""", """tech.onder.collector.controllers.CollectorController.push(id:String)"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/meters/fromlog""", """tech.onder.collector.controllers.CollectorController.pushList()"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/queue/backup""", """tech.onder.queue.controllers.QueueController.backup()"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/queue/restore""", """tech.onder.queue.controllers.QueueController.restore()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/meters""", """tech.onder.meters.controllers.MeterController.list()"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/meters""", """tech.onder.meters.controllers.MeterController.add()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/meters/relations""", """tech.onder.meters.controllers.MeterController.meterRelations()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/reports/consumption""", """tech.onder.reports.controllers.ReportController.consumption()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/reports/consumption/last""", """tech.onder.reports.controllers.ReportController.lastConsumption(offset:Integer ?= null)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/reports/price""", """tech.onder.reports.controllers.ReportController.prices()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/reports/meters""", """tech.onder.reports.controllers.ReportController.meters()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """ws""", """tech.onder.reports.controllers.ReportController.ws()"""),
    Nil
  ).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
    case l => s ++ l.asInstanceOf[List[(String,String,String)]]
  }}


  // @LINE:2
  private[this] lazy val tech_onder_collector_controllers_CollectorController_push0_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/meters/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val tech_onder_collector_controllers_CollectorController_push0_invoker = createInvoker(
    CollectorController_3.push(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.collector.controllers.CollectorController",
      "push",
      Seq(classOf[String]),
      "POST",
      this.prefix + """api/meters/""" + "$" + """id<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:3
  private[this] lazy val tech_onder_collector_controllers_CollectorController_pushList1_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/meters/fromlog")))
  )
  private[this] lazy val tech_onder_collector_controllers_CollectorController_pushList1_invoker = createInvoker(
    CollectorController_3.pushList(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.collector.controllers.CollectorController",
      "pushList",
      Nil,
      "POST",
      this.prefix + """api/meters/fromlog""",
      """""",
      Seq()
    )
  )

  // @LINE:6
  private[this] lazy val tech_onder_queue_controllers_QueueController_backup2_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/queue/backup")))
  )
  private[this] lazy val tech_onder_queue_controllers_QueueController_backup2_invoker = createInvoker(
    QueueController_0.backup(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.queue.controllers.QueueController",
      "backup",
      Nil,
      "POST",
      this.prefix + """api/queue/backup""",
      """""",
      Seq()
    )
  )

  // @LINE:7
  private[this] lazy val tech_onder_queue_controllers_QueueController_restore3_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/queue/restore")))
  )
  private[this] lazy val tech_onder_queue_controllers_QueueController_restore3_invoker = createInvoker(
    QueueController_0.restore(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.queue.controllers.QueueController",
      "restore",
      Nil,
      "POST",
      this.prefix + """api/queue/restore""",
      """""",
      Seq()
    )
  )

  // @LINE:10
  private[this] lazy val tech_onder_meters_controllers_MeterController_list4_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/meters")))
  )
  private[this] lazy val tech_onder_meters_controllers_MeterController_list4_invoker = createInvoker(
    MeterController_2.list(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.meters.controllers.MeterController",
      "list",
      Nil,
      "GET",
      this.prefix + """api/meters""",
      """""",
      Seq()
    )
  )

  // @LINE:11
  private[this] lazy val tech_onder_meters_controllers_MeterController_add5_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/meters")))
  )
  private[this] lazy val tech_onder_meters_controllers_MeterController_add5_invoker = createInvoker(
    MeterController_2.add(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.meters.controllers.MeterController",
      "add",
      Nil,
      "POST",
      this.prefix + """api/meters""",
      """""",
      Seq()
    )
  )

  // @LINE:12
  private[this] lazy val tech_onder_meters_controllers_MeterController_meterRelations6_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/meters/relations")))
  )
  private[this] lazy val tech_onder_meters_controllers_MeterController_meterRelations6_invoker = createInvoker(
    MeterController_2.meterRelations(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.meters.controllers.MeterController",
      "meterRelations",
      Nil,
      "GET",
      this.prefix + """api/meters/relations""",
      """""",
      Seq()
    )
  )

  // @LINE:16
  private[this] lazy val tech_onder_reports_controllers_ReportController_consumption7_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/reports/consumption")))
  )
  private[this] lazy val tech_onder_reports_controllers_ReportController_consumption7_invoker = createInvoker(
    ReportController_1.consumption(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.reports.controllers.ReportController",
      "consumption",
      Nil,
      "GET",
      this.prefix + """api/reports/consumption""",
      """""",
      Seq()
    )
  )

  // @LINE:17
  private[this] lazy val tech_onder_reports_controllers_ReportController_lastConsumption8_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/reports/consumption/last")))
  )
  private[this] lazy val tech_onder_reports_controllers_ReportController_lastConsumption8_invoker = createInvoker(
    ReportController_1.lastConsumption(fakeValue[Integer]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.reports.controllers.ReportController",
      "lastConsumption",
      Seq(classOf[Integer]),
      "GET",
      this.prefix + """api/reports/consumption/last""",
      """""",
      Seq()
    )
  )

  // @LINE:18
  private[this] lazy val tech_onder_reports_controllers_ReportController_prices9_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/reports/price")))
  )
  private[this] lazy val tech_onder_reports_controllers_ReportController_prices9_invoker = createInvoker(
    ReportController_1.prices(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.reports.controllers.ReportController",
      "prices",
      Nil,
      "GET",
      this.prefix + """api/reports/price""",
      """""",
      Seq()
    )
  )

  // @LINE:19
  private[this] lazy val tech_onder_reports_controllers_ReportController_meters10_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/reports/meters")))
  )
  private[this] lazy val tech_onder_reports_controllers_ReportController_meters10_invoker = createInvoker(
    ReportController_1.meters(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.reports.controllers.ReportController",
      "meters",
      Nil,
      "GET",
      this.prefix + """api/reports/meters""",
      """""",
      Seq()
    )
  )

  // @LINE:20
  private[this] lazy val tech_onder_reports_controllers_ReportController_ws11_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("ws")))
  )
  private[this] lazy val tech_onder_reports_controllers_ReportController_ws11_invoker = createInvoker(
    ReportController_1.ws(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.reports.controllers.ReportController",
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
    case tech_onder_collector_controllers_CollectorController_push0_route(params@_) =>
      call(params.fromPath[String]("id", None)) { (id) =>
        tech_onder_collector_controllers_CollectorController_push0_invoker.call(CollectorController_3.push(id))
      }
  
    // @LINE:3
    case tech_onder_collector_controllers_CollectorController_pushList1_route(params@_) =>
      call { 
        tech_onder_collector_controllers_CollectorController_pushList1_invoker.call(CollectorController_3.pushList())
      }
  
    // @LINE:6
    case tech_onder_queue_controllers_QueueController_backup2_route(params@_) =>
      call { 
        tech_onder_queue_controllers_QueueController_backup2_invoker.call(QueueController_0.backup())
      }
  
    // @LINE:7
    case tech_onder_queue_controllers_QueueController_restore3_route(params@_) =>
      call { 
        tech_onder_queue_controllers_QueueController_restore3_invoker.call(QueueController_0.restore())
      }
  
    // @LINE:10
    case tech_onder_meters_controllers_MeterController_list4_route(params@_) =>
      call { 
        tech_onder_meters_controllers_MeterController_list4_invoker.call(MeterController_2.list())
      }
  
    // @LINE:11
    case tech_onder_meters_controllers_MeterController_add5_route(params@_) =>
      call { 
        tech_onder_meters_controllers_MeterController_add5_invoker.call(MeterController_2.add())
      }
  
    // @LINE:12
    case tech_onder_meters_controllers_MeterController_meterRelations6_route(params@_) =>
      call { 
        tech_onder_meters_controllers_MeterController_meterRelations6_invoker.call(MeterController_2.meterRelations())
      }
  
    // @LINE:16
    case tech_onder_reports_controllers_ReportController_consumption7_route(params@_) =>
      call { 
        tech_onder_reports_controllers_ReportController_consumption7_invoker.call(ReportController_1.consumption())
      }
  
    // @LINE:17
    case tech_onder_reports_controllers_ReportController_lastConsumption8_route(params@_) =>
      call(params.fromQuery[Integer]("offset", Some(null))) { (offset) =>
        tech_onder_reports_controllers_ReportController_lastConsumption8_invoker.call(ReportController_1.lastConsumption(offset))
      }
  
    // @LINE:18
    case tech_onder_reports_controllers_ReportController_prices9_route(params@_) =>
      call { 
        tech_onder_reports_controllers_ReportController_prices9_invoker.call(ReportController_1.prices())
      }
  
    // @LINE:19
    case tech_onder_reports_controllers_ReportController_meters10_route(params@_) =>
      call { 
        tech_onder_reports_controllers_ReportController_meters10_invoker.call(ReportController_1.meters())
      }
  
    // @LINE:20
    case tech_onder_reports_controllers_ReportController_ws11_route(params@_) =>
      call { 
        tech_onder_reports_controllers_ReportController_ws11_invoker.call(ReportController_1.ws())
      }
  }
}
