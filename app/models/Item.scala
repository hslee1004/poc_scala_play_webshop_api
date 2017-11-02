package models

import play.api.libs.json.Json

case class Item(
                 id: String,
                 name: String,
                 price: Int,
                 qty: Int
               ) {

  def toXml = <Item>
    <ItemId>{id}</ItemId>
    <ItemName>{name}</ItemName>
    <Price>{price}</Price>
    <Quantity>1</Quantity>
  </Item>
}

object Item {
  implicit val itemFormat = Json.format[Item]
}
