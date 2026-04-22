/**
 * SparkAgg.scala
 * @author
 *   Gerardo Jaramillo
 */

package example

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.TimestampType
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.expressions.Window

object SparkAgg {

  val schema = new StructType()
    .add("name", StringType, false)
    .add("lastname", StringType, false)
    .add("birthday", TimestampType, false)
    .add("gender", StringType, false)
    .add("cntryCode", StringType, false)

  val path =
    getClass()
      .getResource("/people.csv")
      .getPath()

  def main(args: Array[String]): Unit = {

    val t = System.currentTimeMillis

    val config = new SparkConf()
      .setAppName("SparkAgg")
      .setMaster("local[*]")
    val spark = SparkSession
      .builder()
      .config(config)
      .getOrCreate()
    spark.sparkContext.setLogLevel(Level.ERROR.toString)
    val df = spark.read
      .format("csv")
      .schema(schema)
      .option("header", "true")
      .load(path)
      .repartition(8)
      .cache()

    println(s"Partitions: ${df.rdd.getNumPartitions}")

    import org.apache.spark.sql.functions._
    val groupedByCountry = df
      .groupBy(col("cntryCode"))
      .agg(count(col("*")).as("counter"))
      .withColumn(
        "percentage",
        round(
          col("counter") / sum(col("counter")).over(Window.partitionBy()) * 100,
          2
        ))
    // groupedByCountry.show()

    val groupedByGender =
      df.groupBy(col("gender"))
        .agg(count("*").as("counter"))
        .withColumn(
          "percentage",
          round(
            col("counter") / sum(col("counter")).over(
              Window.partitionBy()) * 100,
            2
          )
        )

    // groupedByGender.show()

    val ageDF = df.withColumn(
      "age",
      floor(months_between(current_date(), col("birthday")) / 12))

    ageDF.show()

    val avgAge = ageDF.agg(
      min(col("age")).alias("min"),
      max(col("age")).alias("max"),
      round(avg(col("age")), 2).alias("avg")
    )
    avgAge.show()

    spark.stop()
    spark.close()

    println(s"${(System.currentTimeMillis() - t)}".toDouble / 1000)

  }

}
