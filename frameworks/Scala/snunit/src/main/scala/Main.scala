import snunit._
import upickle.default._

case class Message(message: String)

object Message {
  implicit val messageRW: ReadWriter[Message] = macroRW[Message]
}

object Main {
  def main(args: Array[String]): Unit = {
    val server = SyncServerBuilder
      .build(req =>
        if (req.method == Method.GET && req.path == "/plaintext")
          req.send(
            statusCode = StatusCode.OK,
            content = "Hello, World!",
            headers = Seq("Content-Type" -> "text/plain")
          )
        else if (req.method == Method.GET && req.path == "/json")
          req.send(
            statusCode = StatusCode.OK,
            content = write(Message("Hello, World!")),
            headers = Seq("Content-Type" -> "application/json")
          )
        else
          req.send(
            statusCode = StatusCode.NotFound,
            content = "Not found",
            headers = Seq("Content-Type" -> "text/plain")
          )
      )

    server.listen()
  }
}
