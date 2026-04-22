/**
 * UsingOfSparkListener.scala
 * @author
 *   Gerardo Jaramillo
 */

package lab

import org.apache.spark.scheduler.SparkListener
import org.apache.spark.scheduler.SparkListenerInterface
import org.apache.spark.sql.SparkSession

object UsingOfSparkListener {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("UsingOfSparkListener")
      .master("local[*]")
      .getOrCreate()

    /** Spark Listener */

  }

}
