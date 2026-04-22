package example

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object UsingOfSparkPostgres {

  def main(args: Array[String]): Unit = {
    val config =
      new SparkConf()
        .setMaster("local[*]")
        .setAppName("SparkPostgres")
    val spark = SparkSession
      .builder()
      .config(config)
      .getOrCreate()

    val df = spark.read
      .format("jdbc")
      .option("url", "jdbc:postgresql:eumira-atm-db")
      .option("user", "postgres")
      .option("password", "password")
      .option("query", "select atm_id, name from atm")
      .load()

    df.show()
    df.printSchema()
    spark.stop()
    spark.close()
  }

}
