package hello.mdc

import java.util.concurrent.TimeUnit

import akka.dispatch._
import com.typesafe.config.Config
import org.slf4j.MDC

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.{Duration, FiniteDuration}

/**
  * Configurator for a context propagating dispatcher.
  */
class ContextPropagatingDispatcherConfigurator(config: Config, prerequisites: DispatcherPrerequisites)
  extends MessageDispatcherConfigurator(config, prerequisites) {

  private val instance = new ContextPropagatingDispatcher(
    this,
    config.getString("id"),
    config.getInt("throughput"),
    FiniteDuration(config.getDuration("throughput-deadline-time", TimeUnit.NANOSECONDS), TimeUnit.NANOSECONDS),
    configureExecutor(),
    FiniteDuration(config.getDuration("shutdown-timeout", TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS))

  override def dispatcher(): MessageDispatcher = instance
}

/**
  * A context propagating dispatcher.
  *
  * This dispatcher propagates the current request context if it's set when it's executed.
  */
class ContextPropagatingDispatcher(_configurator: MessageDispatcherConfigurator,
                                   id: String,
                                   throughput: Int,
                                   throughputDeadlineTime: Duration,
                                   executorServiceFactoryProvider: ExecutorServiceFactoryProvider,
                                   shutdownTimeout: FiniteDuration) extends Dispatcher(
  _configurator, id, throughput, throughputDeadlineTime, executorServiceFactoryProvider, shutdownTimeout
) { self =>

  override def prepare(): ExecutionContext = new ExecutionContext {
    def execute(r: Runnable) = self.execute(new Runnable {

      private val mdcContextMap = MDC.getCopyOfContextMap

      def run() = {
        val oldMdcContextMap = MDC.getCopyOfContextMap
        setContextMap(mdcContextMap)
        try {
          r.run()
        } finally {
          setContextMap(oldMdcContextMap)
        }
      }
    })

    private[this] def setContextMap(context: java.util.Map[String, String]): Unit = {
      if (context == null) {
        MDC.clear()
      } else {
        MDC.setContextMap(context)
      }
    }

    def reportFailure(t: Throwable) = self.reportFailure(t)
  }
}