//package com.zsm.bigdata.scala
//
//import java.io.{File, FileReader}
//
//import scala.io.Source
//
///*
//  * @Author: zengsm.
//  * @Description:
//  * @Date:Created in 2018/2/28 18:02.
//  * @Modified By:
//  */
//object StartRun {
//  def main(args: Array[String]): Unit = {
//    println("Start Run!");
//
//    val file = new File("D:\\test.txt")
//    //读取文件内容
//    val it = Source.fromFile(file).getLines();
//    var countHeight: scala.collection.mutable.Map[Int, Int] = scala.collection.mutable.Map[Int, Int]();
//    while (it.hasNext) {
//      val line = it.next()
//      val height = Integer.parseInt(line.split(" ")(2))
//      if (countHeight.contains(height)) {
//        countHeight(height) += 1
//      }
//      else {
//        countHeight += (height -> 1)
//      }
//    }
//    println(countHeight)
//  }
//}
