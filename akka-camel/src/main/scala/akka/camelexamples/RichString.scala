package akka.camelexamples

import java.io.FileWriter

object RichString{
  implicit def toRichString(s:String) : RichString = new RichString(s)
}


class RichString(s: String){
  def saveAs(fileName: String) = write(fileName, s)
  def >>(fileName: String) = this.saveAs(fileName)
  def <<(content: String) = write(s, content)

  private[this] def write(fileName: String, content: String) {
    val f = new FileWriter(fileName)
    f.write(content)
    f.close()
  }
}



