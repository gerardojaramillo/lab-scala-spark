/**
 * UsingOfSparkRead.scala
 * @author
 *   Gerardo Jaramillo
 */

package lab

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.DataTypes.DateType
import org.apache.spark.sql.types.DataTypes.StringType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType

object UsingOfSparkRead {

  def read() = {
    val config = new SparkConf()
      .setAppName("Unknow")
      .setMaster("local[*]")
    val sc: SparkContext = new SparkContext(config)
    val rdd = sc.parallelize(Seq.range(0, 99))
    rdd.map(n => n + 1).collect().foreach(println)
  }

  def readJson(path: String): Unit = {
    val spark =
      SparkSession
        .builder()
        .appName("")
        .master("local[*]")
        .getOrCreate()
    val schema = new StructType()
      .add(StructField("", StringType, false))
      .add(StructField("", StringType, false))
      .add(StructField("", StringType, false))
    val df = spark.read
      .format("json")
      .schema(schema)
      .option("", "")
      .load(path)
    spark.stop()
    spark.close()
  }

  def main(args: Array[String]): Unit = {

    read()

    /**
     * require(args.length == 1, "Usage: UsingOfSparkRead <path_to_csv>") val
     * path = args(0)
     *
     * val spark = SparkSession .builder() .appName("UsingOfSparkRead")
     * .master("local[*]") .getOrCreate()
     *
     * val schema = new StructType() .add(StructField("name", StringType, true))
     * .add(StructField("paternal", StringType, true))
     * .add(StructField("maternal", StringType, true))
     * .add(StructField("birthday", DateType, true)) .add(StructField("gender",
     * StringType, true)) .add(StructField("country", StringType, true))
     *
     * val df = spark.read .format("csv") .schema(schema) .option("header",
     * "true") .option("dateFormat", "yyyy-MM-dd") .load(s"file://${path}")
     *
     * import org.apache.spark.sql.functions._ val genderGroup =
     * df.groupBy(col("gender")) .agg(count("gender").as("counter"))
     * .sort(col("gender")) genderGroup.show()
     *
     * spark.close
     */

  }

}
