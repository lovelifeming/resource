//package com.zsm.bigdata.scala
//
//import java.io.{File, FileWriter}
//
//import org.apache.spark.{SparkConf, SparkContext}
//
//import scala.util.Random
//
///*
//  * 假设要对某个省的人口 (1 亿) 性别还有身高进行统计，并计算出男女人数，男性中的最高和最低身高，以及女性中的最高和最低身高。
//  * 源文件有以下格式, 三列分别是 ID，性别，身高 (cm)。
//  * @Author: zengsm.
//  * @Description:
//  * @Date:Created in 2018/3/1 9:36.
//  * @Modified By:
//  */
//class SparkPeopleInfoCalculator {
//
//  /**
//    * 统计男女的信息，返回男性总数，最低身高，最高身高，女性总数，最低身高，最高身高
//    *
//    * @param filePath
//    * @return
//    */
//  def statisticCalculator(filePath: String): Array[Long] = {
//    //创建SparkConf配置对象，并给Job取名
//    val conf = new SparkConf().setAppName("Spark Exercise:People Info(Gender & Height) Calculator")
//    //创建SparkContext容器对象
//    val sc = new SparkContext(conf)
//    //获取文件内容，设置RDD为5个
//    val dataFile = sc.textFile(filePath, 5);
//    //分别获取男性和女性性别和升高信息
//    val maleData = dataFile.filter(line => line.contains("M")).map(line =>
//      (line.split(" ")(1) + " " + line.split(" ")(2)))
//    val femaleData = dataFile.filter(line => line.contains("F")).map(line =>
//      (line.split(" ")(1) + " " + line.split(" ")(2)))
//
//    val maleHeightData = maleData.map(line => line.split(" ")(1).toInt)
//    val femaleHeightData = femaleData.map(line => line.split(" ")(1).toInt)
//
//    val lowestMale = maleHeightData.sortBy(x => x, true).first()
//    val lowestFemale = femaleHeightData.sortBy(x => x, true).first()
//    val highestMale = maleHeightData.sortBy(x => x, false).first()
//    val highestFemale = femaleHeightData.sortBy(x => x, false).first()
//    println("Number of Male People:" + maleData.count())
//    println("Number of Female People:" + femaleData.count())
//    println("Lowest Male:" + lowestMale)
//    println("Lowest Female:" + lowestFemale)
//    println("Highest Male:" + highestMale)
//    println("Highest Female:" + highestFemale)
//
//    val result= Array( maleData.count(),lowestMale,highestMale,femaleData.count(),lowestFemale,highestFemale)
//    return result;
//  }
//
//  /**
//    * 生成人口信息：ID，性别(M,F)，升高(cm)
//    *
//    * @param filePath
//    */
//  def peopleInfoGenerator(filePath: String): Unit = {
//    val writer = new FileWriter(new File(filePath));
//    val random = new Random();
//    for (i <- 1 to 100) {
//      var height = random.nextInt(220);
//      if (height < 50) {
//        height = height + 60;
//      }
//      val gender = getRandomGender()
//      if (height < 100 && gender == "M") {
//        height = height + 100;
//      }
//      if (height < 100 && gender == "F") {
//        height = height + 50;
//      }
//      writer.write(i + " " + gender + " " + height)
//      writer.write(System.getProperty("line.separator"));
//    }
//    writer.flush()
//    writer.close()
//    println("People Information File generated successfully.")
//  }
//
//  def getRandomGender(): String = {
//    val random = new Random();
//    val number = random.nextInt(2) + 1
//    if (number % 2 == 0) {
//      "M"
//    }
//    else {
//      "F"
//    }
//  }
//
//}
