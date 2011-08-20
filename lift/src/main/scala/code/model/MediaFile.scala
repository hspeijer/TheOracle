package code.model

import java.io.{FileInputStream, File}
import java.util.HashMap
import net.liftweb.common.Logger

case class MediaContainer(path : String) extends Logger {

  var files = new HashMap[String, MediaFile]()

  scanFolder()

  def file : File = new File(path)

  def addFile(file : MediaFile) {
    files.put(file.name, file)
  }

  def scanFolder() {
    var folder = new File(path)
    debug("scanning " + path)
    folder.listFiles().foreach(f  => addFile(new MediaFile(f.getName(), this)))
    debug("Files:" + files)
  }

  def metaData : String = {
    var tmp = new File(path + ".meta")

    tmp.getAbsolutePath
  }

  def getFile(name : String) : MediaFile = {
    files.get(name)
  }
}

case class MediaFile(name: String) {
  val tags : List[String] = List("*")
  var parent : MediaContainer = null
  var duration : Long = 0

  def file : File = new File(parent.file.getAbsolutePath + name)

  def this(name: String, pparent: MediaContainer) {
    this(name)
    parent = pparent
  }

  def MediaFile(name : String, duration : Long) = {
    this.duration = duration
  }

  def inputStream = new FileInputStream(file)

  def size = file.length()

  override def toString() = name + ":" + duration
//  var file = _
}