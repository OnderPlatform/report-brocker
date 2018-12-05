// @GENERATOR:play-routes-compiler
// @SOURCE:C:/work/report-brocker/conf/routes
// @DATE:Wed Dec 05 02:01:15 CET 2018

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._

import play.api.mvc._

import _root_.controllers.Assets.Asset
import _root_.play.libs.F

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:2
  CollectorController_3: tech.onder.consumer.CollectorController,
  // @LINE:7
  QueueController_0: tech.onder.queue.QueueController,
  // @LINE:11
  CommonController_1: tech.onder.meters.CommonController,
  // @LINE:18
  ReportController_2: tech.onder.reports.ReportController,
  val prefix: String
) extends GeneratedRouter {

   @javax.inject.Inject()
   def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:2
    CollectorController_3: tech.onder.consumer.CollectorController,
    // @LINE:7
    QueueController_0: tech.onder.queue.QueueController,
    // @LINE:11
    CommonController_1: tech.onder.meters.CommonController,
    // @LINE:18
    ReportController_2: tech.onder.reports.ReportController
  ) = this(errorHandler, CollectorController_3, QueueController_0, CommonController_1, ReportController_2, "/")

  def withPrefix(prefix: String): Routes = {
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, CollectorController_3, QueueController_0, CommonController_1, ReportController_2, prefix)
  }

  private[this] val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/meters/""" + "$" + """id<[^/]+>""", """tech.onder.consumer.CollectorController.push(id:String)"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/meters""", """tech.onder.consumer.CollectorController.pushList()"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """queue/backup""", """tech.onder.queue.QueueController.backup()"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """queue/restore""", """tech.onder.queue.QueueController.restore()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/meters""", """tech.onder.meters.CommonController.meters()"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/meters""", """tech.onder.meters.CommonController.addMeter()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/meters/relations""", """tech.onder.meters.CommonController.meterRelations()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/reports/consumption""", """tech.onder.reports.ReportController.consumption()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/reports/consumption/last""", """tech.onder.reports.ReportController.lastConsumption(shift:Integer ?= null)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/reports/price""", """tech.onder.reports.ReportController.prices()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/reports/meters""", """tech.onder.reports.ReportController.meters()"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/queue/back""", """tech.onder.meters.CommonController.meterRelations()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/ws""", """tech.onder.reports.ReportController.ws()"""),
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
    CollectorController_3.push(fakeValue[String]),
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
  private[this] lazy val tech_onder_consumer_CollectorController_pushList1_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/meters")))
  )
  private[this] lazy val tech_onder_consumer_CollectorController_pushList1_invoker = createInvoker(
    CollectorController_3.pushList(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.consumer.CollectorController",
      "pushList",
      Nil,
      "POST",
      this.prefix + """api/meters""",
      """""",
      Seq()
    )
  )

  // @LINE:7
  private[this] lazy val tech_onder_queue_QueueController_backup2_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("queue/backup")))
  )
  private[this] lazy val tech_onder_queue_QueueController_backup2_invoker = createInvoker(
    QueueController_0.backup(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.queue.QueueController",
      "backup",
      Nil,
      "POST",
      this.prefix + """queue/backup""",
      """""",
      Seq()
    )
  )

  // @LINE:8
  private[this] lazy val tech_onder_queue_QueueController_restore3_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("queue/restore")))
  )
  private[this] lazy val tech_onder_queue_QueueController_restore3_invoker = createInvoker(
    QueueController_0.restore(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.queue.QueueController",
      "restore",
      Nil,
      "POST",
      this.prefix + """queue/restore""",
      """""",
      Seq()
    )
  )

  // @LINE:11
  private[this] lazy val tech_onder_meters_CommonController_meters4_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/meters")))
  )
  private[this] lazy val tech_onder_meters_CommonController_meters4_invoker = createInvoker(
    CommonController_1.meters(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.meters.CommonController",
      "meters",
      Nil,
      "GET",
      this.prefix + """api/meters""",
      """""",
      Seq()
    )
  )

  // @LINE:13
  private[this] lazy val tech_onder_meters_CommonController_addMeter5_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/meters")))
  )
  private[this] lazy val tech_onder_meters_CommonController_addMeter5_invoker = createInvoker(
    CommonController_1.addMeter(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.meters.CommonController",
      "addMeter",
      Nil,
      "POST",
      this.prefix + """api/meters""",
      """""",
      Seq()
    )
  )

  // @LINE:15
  private[this] lazy val tech_onder_meters_CommonController_meterRelations6_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/meters/relations")))
  )
  private[this] lazy val tech_onder_meters_CommonController_meterRelations6_invoker = createInvoker(
    CommonController_1.meterRelations(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.meters.CommonController",
      "meterRelations",
      Nil,
      "GET",
      this.prefix + """api/meters/relations""",
      """""",
      Seq()
    )
  )

  // @LINE:18
  private[this] lazy val tech_onder_reports_ReportController_consumption7_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/reports/consumption")))
  )
  private[this] lazy val tech_onder_reports_ReportController_consumption7_invoker = createInvoker(
    ReportController_2.consumption(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.reports.ReportController",
      "consumption",
      Nil,
      "GET",
      this.prefix + """api/reports/consumption""",
      """""",
      Seq()
    )
  )

  // @LINE:20
  private[this] lazy val tech_onder_reports_ReportController_lastConsumption8_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/reports/consumption/last")))
  )
  private[this] lazy val tech_onder_reports_ReportController_lastConsumption8_invoker = createInvoker(
    ReportController_2.lastConsumption(fakeValue[Integer]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.reports.ReportController",
      "lastConsumption",
      Seq(classOf[Integer]),
      "GET",
      this.prefix + """api/reports/consumption/last""",
      """""",
      Seq()
    )
  )

  // @LINE:22
  private[this] lazy val tech_onder_reports_ReportController_prices9_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/reports/price")))
  )
  private[this] lazy val tech_onder_reports_ReportController_prices9_invoker = createInvoker(
    ReportController_2.prices(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.reports.ReportController",
      "prices",
      Nil,
      "GET",
      this.prefix + """api/reports/price""",
      """""",
      Seq()
    )
  )

  // @LINE:24
  private[this] lazy val tech_onder_reports_ReportController_meters10_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/reports/meters")))
  )
  private[this] lazy val tech_onder_reports_ReportController_meters10_invoker = createInvoker(
    ReportController_2.meters(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.reports.ReportController",
      "meters",
      Nil,
      "GET",
      this.prefix + """api/reports/meters""",
      """""",
      Seq()
    )
  )

  // @LINE:27
  private[this] lazy val tech_onder_meters_CommonController_meterRelations11_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/queue/back")))
  )
  private[this] lazy val tech_onder_meters_CommonController_meterRelations11_invoker = createInvoker(
    CommonController_1.meterRelations(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.meters.CommonController",
      "meterRelations",
      Nil,
      "POST",
      this.prefix + """api/queue/back""",
      """""",
      Seq()
    )
  )

  // @LINE:28
  private[this] lazy val tech_onder_reports_ReportController_ws12_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/ws")))
  )
  private[this] lazy val tech_onder_reports_ReportController_ws12_invoker = createInvoker(
    ReportController_2.ws(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "tech.onder.reports.ReportController",
      "ws",
      Nil,
      "GET",
      this.prefix + """api/ws""",
      """""",
      Seq()
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:2
    case tech_onder_consumer_CollectorController_push0_route(params@_) =>
      call(params.fromPath[String]("id", None)) { (id) =>
        tech_onder_consumer_CollectorController_push0_invoker.call(CollectorController_3.push(id))
      }
  
    // @LINE:4
    case tech_onder_consumer_CollectorController_pushList1_route(params@_) =>
      call { 
        tech_onder_consumer_CollectorController_pushList1_invoker.call(CollectorController_3.pushList())
      }
  
    // @LINE:7
    case tech_onder_queue_QueueController_backup2_route(params@_) =>
      call { 
        tech_onder_queue_QueueController_backup2_invoker.call(QueueController_0.backup())
      }
  
    // @LINE:8
    case tech_onder_queue_QueueController_restore3_route(params@_) =>
      call { 
        tech_onder_queue_QueueController_restore3_invoker.call(QueueController_0.restore())
      }
  
    // @LINE:11
    case tech_onder_meters_CommonController_meters4_route(params@_) =>
      call { 
        tech_onder_meters_CommonController_meters4_invoker.call(CommonController_1.meters())
      }
  
    // @LINE:13
    case tech_onder_meters_CommonController_addMeter5_route(params@_) =>
      call { 
        tech_onder_meters_CommonController_addMeter5_invoker.call(CommonController_1.addMeter())
      }
  
    // @LINE:15
    case tech_onder_meters_CommonController_meterRelations6_route(params@_) =>
      call { 
        tech_onder_meters_CommonController_meterRelations6_invoker.call(CommonController_1.meterRelations())
      }
  
    // @LINE:18
    case tech_onder_reports_ReportController_consumption7_route(params@_) =>
      call { 
        tech_onder_reports_ReportController_consumption7_invoker.call(ReportController_2.consumption())
      }
  
    // @LINE:20
    case tech_onder_reports_ReportController_lastConsumption8_route(params@_) =>
      call(params.fromQuery[Integer]("shift", Some(null))) { (shift) =>
        tech_onder_reports_ReportController_lastConsumption8_invoker.call(ReportController_2.lastConsumption(shift))
      }
  
    // @LINE:22
    case tech_onder_reports_ReportController_prices9_route(params@_) =>
      call { 
        tech_onder_reports_ReportController_prices9_invoker.call(ReportController_2.prices())
      }
  
    // @LINE:24
    case tech_onder_reports_ReportController_meters10_route(params@_) =>
      call { 
        tech_onder_reports_ReportController_meters10_invoker.call(ReportController_2.meters())
      }
  
    // @LINE:27
    case tech_onder_meters_CommonController_meterRelations11_route(params@_) =>
      call { 
        tech_onder_meters_CommonController_meterRelations11_invoker.call(CommonController_1.meterRelations())
      }
  
    // @LINE:28
    case tech_onder_reports_ReportController_ws12_route(params@_) =>
      call { 
        tech_onder_reports_ReportController_ws12_invoker.call(ReportController_2.ws())
      }
  }
}
