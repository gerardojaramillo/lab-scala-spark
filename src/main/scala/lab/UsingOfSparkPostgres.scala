/**
 * UsingOfSparkPostgres.scala
 * @author
 *   Gerardo Jaramillo
 */

package lab

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object UsingOfSparkPostgres {

  def main(args: Array[String]): Unit = {
    val config =
      new SparkConf()
        .setAppName("UsingOfSparkPostgres")
        .setMaster("local[*]")
    val spark = SparkSession
      .builder()
      .config(config)
      .getOrCreate()

    spark.sparkContext.setJobDescription("UsingOfSparkPostgres")

    val df = spark.read
      .format("jdbc")
      .option("url", "jdbc:postgresql:eumira-atm-db")
      .option("user", "postgres")
      .option("password", "password")
      .option("dbtable", "(select atm_id, name from atm) as subquery")
      .option("partitionColumn", "atm_id")
      .option("numPartitions", 10)
      .option("lowerBound", "1")
      .option("upperBound", "100000")
      .option("fetchsize", "100000")
      .load()

    df.printSchema()
    df.show()
    spark.stop()
    spark.close()
  }

}
