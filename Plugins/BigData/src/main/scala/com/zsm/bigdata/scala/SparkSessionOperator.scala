//package com.zsm.bigdata.scala
//
//import java.text.SimpleDateFormat
//
//import org.apache.spark.sql.{Row, SparkSession}
//import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
//
//import scala.collection.mutable
//
///*
//  * @Author: zengsm.
//  * @Description:
//  * @Date:Created in 2018/3/1 18:12.
//  * @Modified By: SparkSession基本操作：创建，建表，导入数据，转换成表，表操作
//  * student_tb (学生表)
//  * 字段名				数据类型			    可否为空		含 义
//  * Sno					Varchar2(3)			否				学号（主键）
//  * Sname				Varchar2(8)			否				生姓名
//  * Ssex				Varchar2(2)			否				学生性别
//  * Sbirthday		Date				    可				学生出生年月
//  * Sclass			Varchar2(5)		  可				学生所在班级
//  *
//  * course_tb（课程表）
//  * 属性名				数据类型			    可否为空		含 义
//  * Cno					Varchar2(5)			否				课程号（主键）
//  * Cname				Varchar(10)			否				课程名称
//  * Tno					Varchar2(3)			否				教工编号（外键）
//  *
//  * score_tb(成绩表)
//  * 属性名				数据类型			    可否为空		含 义
//  * Sno					Varchar2(3)			否				学号（外键）
//  * Cno					Varchar2(5)			否				课程号（外键）
//  * Degree			Number(4,1)			可				成绩
//  *
//  * teacher_tb(教师表)
//  * 属性名				数据类型			可否为空		含 义
//  * Tno					Varchar2(3)			否				教工编号（主键）
//  * Tname				Varchar2(4)			否				教工姓名
//  * Tsex				Varchar2(2)			否				教工性别
//  * Tbirthday		Date				    可				教工出生年月
//  * Prof				Varchar2(6)			可				职称
//  * Depart			Varchar(10)			否				教工所在部门
//  *
//  *
//  */
//class SparkSessionOperator {
//
//  /**
//    *
//    * @param master
//    * @param appName
//    * @param config
//    * @param jobNum
//    */
//  def initSparkSession(master: String, appName: String, config: String, jobNum: String): Unit = {
//    /*创建SparkSession对象*/
//    val spark = SparkSession.builder().master(master).appName(appName).config(config, jobNum).getOrCreate()
//
//    /*创建表结构：val tableSchema=StructType(mutable.ArraySeq(StructField("colName",StringType,nullable =false)))*/
//    val studentSchema: StructType = StructType(mutable.ArraySeq( //学生表
//      StructField("Sno", StringType, nullable = false), //学号
//      StructField("Sname", StringType, nullable = false), //学生姓名
//      StructField("Ssex", StringType, nullable = false), //学生性别
//      StructField("Sbirthday", StringType, nullable = true), //学生出生年月
//      StructField("Sclass", StringType, nullable = true) //学生所在班级
//    ));
//    val courseSchema: StructType = StructType(mutable.ArraySeq( //课程表
//      StructField("Cno", StringType, nullable = false), //课程号
//      StructField("Cname", StringType, nullable = false), //课程名称
//      StructField("Tno", StringType, nullable = false) //教工编号
//    ));
//    val scoreSchema: StructType = StructType(mutable.ArraySeq( //成绩表
//      StructField("Sno", StringType, nullable = false), //学号（外键）
//      StructField("Cno", StringType, nullable = false), //课程号（外键）
//      StructField("Degree", IntegerType, nullable = true) //成绩
//    ));
//    val teacherSchema: StructType = StructType(mutable.ArraySeq( //教师表
//      StructField("Tno", StringType, nullable = false), //教工编号（主键）
//      StructField("Tname", StringType, nullable = false), //教工姓名
//      StructField("Tsex", StringType, nullable = false), //教工性别
//      StructField("Tbirthday", StringType, nullable = true), //教工出生年月
//      StructField("Prof", StringType, nullable = true), //职称
//      StructField("Depart", StringType, nullable = false) //教工所在部门
//    ));
//
//    /*导入数据*/
//    val studentData = spark.sparkContext.textFile("input/sqltable/Student").map {
//      lines =>
//        val line = lines.split(",")
//        Row(line(0), line(1), line(2), line(3), line(4));
//    }
//    val courseData = spark.sparkContext.textFile("input/sqltable/Course").map {
//      lines =>
//        val line = lines.split(",")
//        Row(line(0), line(1), line(2))
//    }
//    val scoreData = spark.sparkContext.textFile("input/sqltable/Score").map {
//      lines =>
//        val line = lines.split(",")
//        Row(line(0), line(1), line(2).toInt)
//    }
//    val teacherData = spark.sparkContext.textFile("input/sqltable/Teacher").map {
//      lines =>
//        val line = lines.split(",")
//        Row(line(0), line(1), line(2), line(3), line(4), line(5))
//    }
//
//    /*创建表，首先要构建表结构和配置数据源*/
//    val studentTable = spark.createDataFrame(studentData, studentSchema)
//    studentTable.createOrReplaceGlobalTempView("student_tb")
//    val courseTable = spark.createDataFrame(courseData, courseSchema)
//    courseTable.createOrReplaceGlobalTempView("course_tb")
//    val scoreTable = spark.createDataFrame(scoreData, scoreSchema)
//    scoreTable.createOrReplaceGlobalTempView("score_tb")
//    val teacherTable = spark.createDataFrame(teacherData, teacherSchema)
//    teacherTable.createOrReplaceGlobalTempView("teacher_tb")
//
//    /*SparkSession表操作(表名，字段名区分大小写)：spark.sql("sql operator").show();*/
//    //1、 查询student_tb表中的所有记录的Sname、Ssex和Sclass列。
//    spark.sql("SELECT Sname, Ssex, Sclass FROM student_tb").show()
//
//    //2、 查询教师所有的单位即不重复的Depart列。
//    spark.sql("SELECT DISTINCT Depart FROM teacher_tb").show()
//
//    //3、 查询student_tb表的所有记录
//    spark.sql("SELECT * FROM student_tb").show()
//
//    //4、 查询score_tb表中成绩在60到80之间的所有记录。
//    //spark.sql("SELECT * FROM score_tb WHERE Degree BETWEEN 60 and 80").show()
//    spark.sql("SELECT * FROM score_tb WHERE Degree >= 60 and Degree <= 80").show()
//
//    //5、 查询score_tb表中成绩为85，86或88的记录。
//    spark.sql("SELECT * FROM score_tb WHERE Degree = '85' OR Degree = '86' OR Degree = '88'").show()
//
//    //6、 查询student_tb表中“95031”班或性别为“女”的同学记录。
//    spark.sql("SELECT * FROM student_tb WHERE Sclass = '95031' OR Ssex = 'female'").show()
//
//    //7、 以Class降序,升序查询student_tb表的所有记录。
//    spark.sql("SELECT * FROM student_tb ORDER BY Sclass DESC").show()
//    spark.sql("SELECT * FROM student_tb ORDER BY Sclass").show()
//
//    //8、 以Cno升序、Degree降序查询score_tb表的所有记录。
//    spark.sql("SELECT * FROM score_tb t ORDER BY t.Sno ASC, t.Degree DESC").show()
//
//    //9、 查询“95031”班的学生人数。
//    spark.sql("SELECT t.Sclass totalnum FROM student_tb t WHERE Sclass = '95031'").show()
//    spark.sql("SELECT t.Sclass AS totalnum FROM student_tb t WHERE Sclass = '95031'").show()
//
//    //10、 查询score_tb表中的最高分的学生学号和课程号。（子查询或者排序）
//    // oracle    =>  WHERE rownum = 1
//    // spark sql =>  LIMIT 1
//    spark.sql("SELECT * FROM (SELECT * FROM score_tb ORDER BY Degree DESC LIMIT 1)").show()
//    spark.sql("SELECT t.Sno, t.Cno FROM score_tb t ORDER BY Degree DESC").show()
//    spark.sql("SELECT * FROM score_tb WHERE Degree IN(SELECT MAX(Degree) FROM score_tb t)").show()
//
//    //11、 查询每门课的平均成绩。
//    spark.sql("SELECT AVG(Degree) average FROM score_tb t WHERE Cno = '3-245'").show()
//    spark.sql("SELECT AVG(Degree) average FROM score_tb WHERE Cno = '6-166'").show()
//    spark.sql("SELECT Cno, AVG(Degree) FROM score_tb t GROUP BY Cno").show()
//
//    //12、查询score_tb表中至少有5名学生选修的并以3开头的课程的平均分数。
//    spark.sql("SELECT Cno, AVG(Degree) FROM score_tb WHERE Cno LIKE '3%' GROUP BY Cno HAVING COUNT(1) >= 5").show()
//
//    //13、查询分数大于70，小于90的Sno列。
//    spark.sql("SELECT Sno FROM score_tb WHERE Degree BETWEEN 70 AND 90").show()
//
//    //14、查询所有学生的Sname、Cno和Degree列。
//    spark.sql("SELECT s.Sname, t.Cno, t.Degree FROM score_tb t, student_tb s WHERE t.Sno = s.Sno").show()
//    spark.sql("SELECT s.Sname, t.Cno, t.Degree FROM score_tb t JOIN student_tb s ON t.Sno = s.Sno").show()
//
//    //15、查询所有学生的Sno、Cname和Degree列。
//    spark.sql("SELECT s.Sname, t.Cno, t.Degree FROM score_tb t JOIN student_tb s ON t.Sno = s.Sno").show()
//
//    //16、查询所有学生的Sname、Cname和Degree列。
//    spark.sql("SELECT s.Sname, t.Degree, c.Cname FROM score_tb t, student_tb s, Course c WHERE t.Sno = s.Sno AND t.Cno = c.Cno").show()
//    spark.sql("SELECT s.Sname, t.Degree, c.Cname FROM score_tb t " +
//      "JOIN student_tb s on t.Sno = s.Sno JOIN Course c on c.Cno = t.Cno").show()
//
//    //17、 查询“95033”班学生的平均分。
//    spark.sql("SELECT AVG(Degree) average FROM score_tb WHERE Sno IN (SELECT Sno FROM student_tb WHERE Sclass = '95033')").show()
//
//    //19、  查询选修“3-105”课程的成绩高于“109”号同学成绩的所有同学的记录。
//    spark.sql("SELECT * FROM score_tb WHERE Cno = '3-105' AND Degree > (SELECT Degree FROM score_tb WHERE Sno = '109' AND Cno = '3-105')").show()
//
//    //20、查询score_tb中选学多门课程的同学中分数为非最高分成绩的记录。
//    spark.sql("SELECT * FROM score_tb WHERE Sno IN " +
//      "(SELECT Sno FROM score_tb t GROUP BY t.Sno HAVING COUNT(1) > 1) AND Degree != (SELECT MAX(Degree) FROM score_tb)").show()
//    spark.sql("SELECT * FROM score_tb WHERE Degree != (SELECT MAX(Degree) FROM score_tb)").show()
//
//    //21、 查询成绩高于学号为“109”、课程号为“3-105”的成绩的所有记录。
//    spark.sql("SELECT * FROM score_tb t WHERE t.Degree > (SELECT Degree FROM score_tb WHERE Sno = '109' AND Cno = '3-105')").show()
//
//    //22、查询和学号为108的同学同年出生的所有学生的Sno、Sname和Sbirthday列。
//    // oracle    =>  to_char(t.sbirthday,'yyyy')
//    // spark sql =>  substring(t.sbirthday, 0, 4)
//    spark.sql("SELECT Sno, Sname , Sbirthday FROM student_tb WHERE substring(sbirthday, 0, 4) = ( " +
//      "SELECT substring(t.sbirthday, 0, 4) FROM student_tb t WHERE Sno = '108')").show()
//
//    //23、查询“张旭“教师任课的学生成绩。
//    spark.sql("SELECT t.Tno, c.Cno, c.Cname, s.Degree FROM teacher_tb t " +
//      "JOIN course_tb c ON t.Tno = c.Tno JOIN score_tb s ON c.Cno = s.Cno WHERE t.Tname = 'Zhang xu'").show()
//
//    //24、查询选修某课程的同学人数多于5人的教师姓名。
//    spark.sql("SELECT Tname FROM teacher_tb e JOIN course_tb c ON e.Tno = c.Tno " +
//      "JOIN(SELECT Cno FROM score_tb GROUP BY Cno HAVING COUNT(Cno) > 5) t ON c.Cno = t.Cno").show()
//
//    //25、查询95033班和95031班全体学生的记录。
//    spark.sql("SELECT * FROM student_tb WHERE Sclass IN('95031', '95033')").show()
//    spark.sql("SELECT * FROM student_tb WHERE Sclass LIKE '9503%'").show()
//
//    //26、查询存在有85分以上成绩的课程Cno.
//    spark.sql("SELECT Cno FROM score_tb WHERE Degree > 85 GROUP BY Cno").show()
//
//    //27、查询出“计算机系“教师所教课程的成绩表。
//    spark.sql("SELECT t.Sno, t.Cno, t.Degree FROM score_tb t " +
//      "JOIN course_tb c ON t.Cno = c.Cno " +
//      "JOIN teacher_tb e ON c.Tno = e.Tno WHERE e.Depart = 'Department of computer'").show()
//
//    //28、查询“计算机系”与“电子工程系“不同职称的教师的Tname和Prof。
//    spark.sql("SELECT Tname, Prof FROM teacher_tb WHERE Prof NOT IN (SELECT a.Prof " +
//      "FROM (SELECT Prof FROM teacher_tb WHERE Depart = 'Department of computer') a JOIN (SELECT Prof " +
//      "FROM teacher_tb WHERE Depart = 'Department of electronic engineering') b ON a.Prof = b.Prof) ").show()
//    spark.sql("SELECT Tname, Prof FROM teacher_tb WHERE Depart = 'Department of electronic engineering' " +
//      "AND Prof NOT IN (SELECT Prof FROM teacher_tb WHERE Depart = 'Department of computer') " +
//      "OR Depart = 'Department of computer' AND Prof NOT IN (SELECT Prof FROM teacher_tb " +
//      "WHERE Depart = 'Department of electronic engineering')").show()
//
//    //29、查询选修编号为“3-105“课程且成绩至少高于选修编号为“3-245”的同学的Cno、Sno和Degree,并按Degree从高到低次序排序。
//    spark.sql("SELECT t.Sno, t.Cno, Degree FROM score_tb t WHERE Degree > (SELECT MIN(Degree) " +
//      "FROM score_tb WHERE Cno = '3-245') AND t.Cno = '3-105' ORDER BY Degree DESC").show()
//
//    //30、查询选修编号为“3-105”且成绩高于选修编号为“3-245”课程的同学的Cno、Sno和Degree.
//    // oracle方式 spark.sql("select t.Sno, t.Cno, t.Degree from score_tb t where t.Degree > (select Degree from score_tb where Cno='3-245' or Cno='3-105')").show()
//    spark.sql("SELECT t.Sno, t.Cno, t.Degree FROM score_tb t WHERE t.Degree > (SELECT MAX(Degree) FROM score_tb WHERE Cno = '3-245' ) AND t.Cno = '3-105'").show()
//
//    //31、 查询所有教师和同学的name、sex和birthday.
//    spark.sql("SELECT Sname, Ssex, Sbirthday FROM student_tb UNION SELECT Tname, Tsex, Tbirthday FROM teacher_tb").show()
//
//    // 32、查询所有“女”教师和“女”同学的name、sex和birthday. union
//    spark.sql("SELECT Sname, Ssex, Sbirthday FROM student_tb WHERE Ssex = 'female' UNION " +
//      "SELECT Tname, Tsex, Tbirthday FROM teacher_tb WHERE Tsex = 'female'").show()
//
//    //33、 查询成绩比该课程平均成绩低的同学的成绩表。
//    spark.sql("SELECT s.* FROM score_tb s WHERE s.Degree < (SELECT AVG(Degree) FROM score_tb c WHERE s.Cno = c.Cno)").show()
//
//    //34、 查询所有任课教师的Tname和Depart. in
//    spark.sql("SELECT Tname, Depart FROM teacher_tb t WHERE t.Tno IN (SELECT Tno FROM course_tb c " +
//      "WHERE c.Cno IN (SELECT Cno FROM score_tb))").show()
//
//    //35 、 查询所有未讲课的教师的Tname和Depart. not in
//    spark.sql("SELECT Tname, Depart FROM teacher_tb t WHERE t.Tno NOT IN ( SELECT Tno " +
//      "FROM course_tb c WHERE c.Cno IN ( SELECT Cno FROM score_tb))").show()
//
//    //36、查询至少有2名男生的班号。  group by, having count
//    spark.sql("SELECT Sclass FROM student_tb t WHERE Ssex = 'male' GROUP BY SClass HAVING COUNT(Ssex) >= 2").show()
//
//    //37、查询student_tb表中不姓“王”的同学记录。 not like
//    spark.sql("SELECT * FROM student_tb t WHERE Sname NOT LIKE('Wang%')").show()
//
//    //38、查询student_tb表中每个学生的姓名和年龄。
//    //将函数运用到spark sql中去计算，可以直接拿String的类型计算不需要再转换成数值型 默认是会转换成Double类型计算
//    spark.sql("SELECT Sname, (" + getDate("yyyy") + " - substring(Sbirthday, 0, 4)) AS age FROM student_tb t").show()
//    //浮点型转整型
//    spark.sql("SELECT Sname, (CAST(" + getDate("yyyy") + " AS INT) - CAST(substring(Sbirthday, 0, 4) AS INT)) AS age " +
//      "FROM student_tb t").show()
//
//    //39、查询student_tb表中最大和最小的Sbirthday日期值。 时间格式最大值,最小值
//    spark.sql("SELECT MAX(t.Sbirthday) AS maximum FROM student_tb t").show()
//    spark.sql("SELECT MIN(t.Sbirthday) AS minimum FROM student_tb t").show()
//
//    //40、以班号和年龄从大到小的顺序查询student_tb表中的全部记录。 查询结果排序
//    spark.sql("SELECT * FROM student_tb ORDER BY SClass DESC, CAST(" +
//      getDate("yyyy") + " AS INT) - CAST(substring(Sbirthday, 0, 4) AS INT) DESC").show()
//
//    //41、查询“男”教师及其所上的课程。 select join
//    spark.sql("SELECT Tsex, Cname FROM teacher_tb t JOIN course_tb c ON t.Tno = c.Tno WHERE Tsex = 'male'").show()
//
//    //42、查询最高分同学的Sno、Cno和Degree列。 子查询
//    spark.sql("SELECT * FROM score_tb WHERE Degree = (SELECT MAX(Degree) FROM score_tb t)").show()
//
//    //43、查询和“李军”同性别的所有同学的Sname.
//    spark.sql("SELECT Sname FROM student_tb t WHERE Ssex IN (SELECT Ssex FROM student_tb WHERE Sname = 'Liu Jun')").show()
//
//    //44、查询和“李军”同性别并同班的同学Sname.
//    spark.sql("SELECT Sname FROM student_tb t WHERE Ssex IN (SELECT Ssex FROM student_tb " +
//      "WHERE Sname = 'Liu Jun') AND Sclass IN (SELECT Sclass FROM student_tb WHERE Sname = 'Liu Jun')").show()
//
//    //45、查询所有选修“计算机导论”课程的“男”同学的成绩表。
//    spark.sql("SELECT t.Sno, t.Cno, t.Degree FROM score_tb t JOIN course_tb c ON t.Cno = c.Cno " +
//      "JOIN student_tb s ON s.Sno = t.Sno WHERE s.Ssex = 'male' AND c.Cname = 'Introduction to computer'").show()
//
//  }
//
//  def getDate(time: String): String = {
//    val now: Long = System.currentTimeMillis()
//    val sdf: SimpleDateFormat = new SimpleDateFormat(time)
//    sdf.format(now)
//  }
//}
