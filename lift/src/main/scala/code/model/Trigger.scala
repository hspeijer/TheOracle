package code.model

/**
 * (c) mindsteps BV 
 *
 * User: Hans Speijer
 * Date: 21-7-11
 * Time: 7:28
 * 
 */

class MediaTrigger(duration: Int) {
  var durationVar = duration
  def durationStr = duration.toString
}

case class MovieTrigger(duration: Int, name: String) extends MediaTrigger(duration){
  var nameVar = name
}

case class StoneSequenceTrigger(name: String, duration: Int) extends MediaTrigger(duration)