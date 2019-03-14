package models

import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ Future, ExecutionContext }

import slick.jdbc.JdbcProfile
import play.api.db.slick.DatabaseConfigProvider


@Singleton
class ClaimRepository @Inject() ( dbConfigProvider: DatabaseConfigProvider ) (implicit ec: ExecutionContext ) {


  private val dbConfig = dbConfigProvider.get[JdbcProfile]



} //:~
