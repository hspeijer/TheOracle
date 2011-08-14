package code.lib

import net.liftweb.common.{Logger, Box}
import collection.mutable.{Map, MutableList}
import java.util.HashMap
import ch.qos.logback.core.joran.conditional.ElseAction
import java.io.{PrintWriter, FileOutputStream, InputStreamReader, BufferedReader}

/**
 * (c) mindsteps BV 
 *
 * User: Hans Speijer
 * Date: 10-8-11
 * Time: 0:07
 * 
 */
abstract class Setting(var value : Any) {
  def Setting(value : Any, parent: Setting)
  def addSetting(key : String, value: Any)
}

class ListSettings extends Setting(new MutableList[Setting]()) {
  def Setting(value: Any, parent: Setting) = null
  def addSetting(key: String, value: Any) = null
}

class MapSettings extends Setting(new HashMap[String, String]()){
   var valueMap = new HashMap[String, Any]()

  def Setting(key: Any, parent: Setting) = null
  def addSetting(key: String, setting: Any) = {
    valueMap.put(key, setting)
    //println(valueMap.toString + "key:" + key)
    valueMap
  }
  override def toString() = valueMap.toString();
}

object ConfigurationManager extends Logger {
  val result = new MapSettings();

  def loadSettings(namespace : String) : MapSettings = {
    val configNameDefault = "config/" + namespace + "-default.properties"
    loadSettings(configNameDefault, result)

    val configName = "config/" + namespace + ".properties"
    loadSettings(configName, result)

    result
  }

  def loadSettings(configName : String, mergeWith: Setting) = {
    val configUrl = this.getClass.getClassLoader.getResource(configName)

    if (configUrl == null) {
      error("Could not find resource for " + configName);
    } else {
      info("Loading " + configUrl + " from classpath")

      val input : BufferedReader = new BufferedReader(new InputStreamReader(configUrl.openStream()));
      var inputLine : String = input.readLine()
      while (inputLine != null) {
        val keyValue = inputLine.split("=")
        info("Setting:" + keyValue(0) + " = " + keyValue(1))
        mergeWith.addSetting(keyValue(0), keyValue(1))
        inputLine = input.readLine()
      }
      }
  }

  def writeSettings(configName : String, path: String) = {
    val output = new FileOutputStream(path + configName);
    val writer = new PrintWriter(output)
    val iterator = result.valueMap.keySet().iterator()

    while(iterator.hasNext) {
      val key = iterator.next()
      writer.write(key + "=" + result.valueMap.get(key) + "\n")
    }

    writer.close()
    output.close()
  }

  def getSetting(key : String) : Any = {
    result.valueMap.get(key)
  }

  def querySettings(uri: Box[String], namespace: Box[String]) = {

  }
}