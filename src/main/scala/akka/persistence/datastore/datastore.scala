package akka.persistence

import java.util.concurrent.Executor

import com.google.common.util.concurrent.ListenableFuture

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.Try

package object datastore {

  implicit class ListenableFutureConverter[A](val lf: ListenableFuture[A]) extends AnyVal {
    def asScala(implicit ec: ExecutionContext): Future[A] = {
      val promise = Promise[A]
      lf.addListener(new Runnable {
        def run() = promise.complete(Try(lf.get()))
      }, ec.asInstanceOf[Executor])
      promise.future
    }
  }


}
