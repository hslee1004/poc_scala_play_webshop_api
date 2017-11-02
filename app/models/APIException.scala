package models

case class APIException(errDetail: String)  extends Exception(errDetail)