/**
 * SparkWatermark.scala
 * @author
 *   Gerardo Jaramillo (https://gerardojaramillo.dev)
 */

package example

import org.apache.spark.SparkConf
import org.apache.spark.sql.Encoders
import org.apache.spark.sql.Encoders.tuple
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.functions.window
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.streaming.Trigger
import org.apache.spark.sql.types.DataTypes
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType

import java.sql.Timestamp

object SparkWatermark {

  val consumerOptions = Map(
    "kafka.bootstrap.servers" -> "localhost:9092",
    "subscribe" -> "stream-person")

  val schema =
    new StructType()
      .add(StructField("field1", DataTypes.StringType, false))
      .add(StructField("field2", DataTypes.StringType, false))
      .add(StructField("field3", DataTypes.StringType, false))

  def main(args: Array[String]): Unit = {
    val config =
      new SparkConf()
        .setAppName("SparkWatermark")
        .setMaster("local[*]")
    val spark = SparkSession.builder().config(config).getOrCreate()
    spark.sparkContext.setLogLevel("ERROR")

    import org.apache.spark.sql.functions._
    val streamDF = spark.readStream
      .format("kafka")
      .options(consumerOptions)
      .load()
      .selectExpr(
        "CAST(key AS STRING) AS key",
        "CAST(value AS STRING) AS value",
        "CAST(timestamp AS TIMESTAMP) as timestamp"
      )
      .as[(String, String, Timestamp)](
        tuple(Encoders.STRING, Encoders.STRING, Encoders.TIMESTAMP))
      .select(from_json(col("value"), schema).as("json"))
      .selectExpr("json.*")

    import scala.concurrent.duration._
    streamDF.writeStream
      .format("console")
      .outputMode(OutputMode.Append())
      .trigger(Trigger.ProcessingTime(3.seconds))
      .option("truncate", false)
      .start()
      .awaitTermination()
  }

}
