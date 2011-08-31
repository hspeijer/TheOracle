package code.model

import java.io.{FileInputStream, File}
import net.liftweb.common.Logger
import collection.SortedMap
import io.Source

case class MediaContainer(path : String, var files : SortedMap[String, MediaFile]) extends Logger {

  def this(path: String) {
    this(path, SortedMap[String, MediaFile]())
  }

  scanFolder()

  def file : File = new File(path)

  def addFile(file : MediaFile) {
    files += (file.name.substring(0, file.name.indexOf(".")) -> file)
  }

  def scanFolder() {
    var folder = new File(path)
    debug("scanning " + path)
    folder.listFiles().foreach(f  => addFile(new MediaFile(f.getName(), this)))
    debug("Files:" + files)
  }

  def getFile(name : String) : MediaFile = {
    files(name)
  }
}

case class MediaFile(name: String, var tags: List[String], var duration:Int) {
  var parent: MediaContainer = null

  def file : File = new File(parent.file.getAbsolutePath + "/" + name)

  def this(name: String, pparent: MediaContainer) {
    this(name, List[String](), 0)
    parent = pparent
  }

//  def (name : String, duration : Long) = {
//    this.duration = duration
//  }

  def inputStream = new FileInputStream(file)

  def size = file.length()

  override def toString() = name + ":" + duration
//  var file = _
}