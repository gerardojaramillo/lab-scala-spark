/**
 * UsingOfSparkSession.scala
 * @author
 *   Gerardo Jaramillo
 */

package lab

import org.apache.spark.sql.SparkSession

object UsingOfSparkSession {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("UsingOfSparkSession")
      .master("local[*]")
      .getOrCreate()
    val rdd = spark.sparkContext.parallelize(Seq("A", "B", "C"))
    rdd.foreachPartition(println)
    // rdd.foreachPartition { itr => itr.foreach(println) }
    spark.stop()
  }

}
