//: tables: YesNoColumnType.scala
package tables

import slick.jdbc.MySQLProfile.api._


/**
  * Database column type related to data type 'Boolean'
  *
  * @version 1.0 $ 2019-03-27 15:36 $
  */
sealed abstract class YesNoBoolean( v: Boolean )


object YesNoBoolean {

  case object Yes extends YesNoBoolean( true  )
  case object No  extends YesNoBoolean( false )


  implicit val columnType: BaseColumnType[YesNoBoolean] =
        MappedColumnType.base[YesNoBoolean, Boolean]( YesNoBoolean.toBool, YesNoBoolean.fromBool )


  def toBool( o: YesNoBoolean ): Boolean   = o match {

    case Yes => true
    case No  => false
    case _   => sys.error( "Error: system only support 'Yes' and 'No'" )

  }

  def fromBool( v: Boolean ): YesNoBoolean = v match {

    case true  => Yes
    case false => No
    case _   => sys.error( "Error: system only support type 'Boolean'" )

  }


} //:~
