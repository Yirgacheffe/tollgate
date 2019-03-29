//: tables: YesNoColumnType.scala
package tables

import slick.jdbc.MySQLProfile.api._


/**
  * Database column type related to data type 'Boolean'
  *
  * @version 1.0 $ 2019-03-27 15:36 $
  */
sealed abstract class YesNoBoolean( val abbr: Char )


object YesNoBoolean {

  case object Yes extends YesNoBoolean( 'Y' )
  case object No  extends YesNoBoolean( 'N' )


  implicit val columnType = MappedColumnType.base[YesNoBoolean, Char]( YesNoBoolean.toChar, YesNoBoolean.fromChar )

  def toChar( o: YesNoBoolean ): Char   = o match {

    case Yes => 'Y'
    case No  => 'N'

  }

  def fromChar( v: Char ): YesNoBoolean = v match {

    case 'Y' => Yes
    case 'N' => No
    case _   => sys.error( "Error: system only support 'Y' and 'N'" )

  }


} //:~
