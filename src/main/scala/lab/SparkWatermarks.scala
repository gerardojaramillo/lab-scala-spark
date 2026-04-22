/**
 * SparkWatermarks.scala
 * @author
 *   Gerardo Jaramillo (https://gerardojaramillo.dev)
 */

package lab

import org.apache.spark.SparkConf

object SparkWatermarks {

  def main(args: Array[String]): Unit = {
    val config: SparkConf =
      new SparkConf()
      .setAppName("SparkWatermarks")
      .setMaster("local[*]")
  }

}
