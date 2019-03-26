package error

import Severity._

case class ErrorResponse( message: String, code: String,  severity: Severity,
                          attribute: Option[String],
                          extraInfo: Option[String] )

trait Errors {

  val invalidParameterError = ErrorResponse( "", "", Severity.WARN, None, None )

}
