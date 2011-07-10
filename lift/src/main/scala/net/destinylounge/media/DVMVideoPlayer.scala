package net.destinylounge.media

/**
 * Created by IntelliJ IDEA.
 * User: markhalldev
 * Date: 7/8/11
 * Time: 2:23 AM
 * To change this template use File | Settings | File Templates.
 */

object DVMVideoPlayer extends DVMConfiguration {


  def selectFile (filename : String) {
    sendMessage(filename, "SE", true)
  }

  def selectFile (fileNumber : Int) {
    // fileNumber between 0-99999.

    sendMessage(fileNumber, 0, 99999, 0, "SE")
  }

  def play (loop : Boolean) {
    if (loop)
      sendMessage("LP")
    else
      sendMessage("PL")
  }

  def play (filename : String, loop : Boolean) {
    var commandBytes = "PL"
    if (loop)
      commandBytes = "LP"
    sendMessage(filename, commandBytes, true)
  }

  def play (fileNumber : Int, loop : Boolean) {
    // fileNumber between 0-99999.

    var commandBytes = "PL"
    if (loop)
      commandBytes = "LP"

    sendMessage(fileNumber, 0, 99999, 0, commandBytes)
  }

  def playNext (filename : String, loop : Boolean) {
    var commandBytes = "PN"
    if (loop)
      commandBytes = "LN"
    sendMessage(filename, commandBytes, true)
  }

  def playNext (fileNumber : Int, loop : Boolean) {
    // fileNumber between 0-99999.

    var commandBytes = "PN"
    if (loop)
      commandBytes = "LN"

    sendMessage(fileNumber, 0, 99999, 0, commandBytes)
  }

  def still () {
    sendMessage("ST")
  }

  def pause () {
    sendMessage("PA")
  }

  def stop () {
    sendMessage("RJ")
  }


  def setVolume (n : Int) {
    sendMessage(n, 0, 100, 100, "%AD")
  }

  def muteAudio (state : Boolean) {
    // state true is Mute
    // state false is Unmute
    // value is 0 (Mute) or 1 (Unmute)
    var value = 1
    if (state)
      value = 0

    sendMessage(value, "AD")
  }

  def muteVideo (state : Boolean) {
    // state true is Mute
    // state false is Unmute
    // value is 0 (Mute) or 1 (Unmute)
    var value = 1
    if (state)
      value = 0

    sendMessage(value, "VD")
  }

  def activeModeRequest () {
    sendMessage("?P")
    // Need to get results back and return them MEH?    Todo
  }

  def chapterRequest () {
    sendMessage("?C")
    // Need to get results back and return them MEH?    Todo
  }

  def frameRequest () {
    sendMessage("?F")
    // Need to get results back and return them MEH?    Todo
  }

}