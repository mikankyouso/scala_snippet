package http

import com.ning.http.client.ProxyServer

object Client {
  def main(args: Array[String]) {
    import dispatch._
    import Defaults._

    val req = url("http://localhost/")
    val body = Http(req OK as.String)
    body.foreach(println)
    body.onComplete(_ => Http.shutdown)

  }

}