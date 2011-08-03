package code.model

/**
 * (c) mindsteps BV 
 *
 * User: Hans Speijer
 * Date: 21-7-11
 * Time: 7:28
 * 
 */

class MediaTrigger(_duration: Int) {
  var duration = _duration
  def durationStr = duration.toString
}

case class MovieTrigger(_duration: Int, _name: String) extends MediaTrigger(_duration){
  var name = _name
}