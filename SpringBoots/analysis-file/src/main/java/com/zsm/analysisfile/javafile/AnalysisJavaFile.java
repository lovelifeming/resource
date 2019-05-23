package com.zsm.analysisfile.javafile;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;


/**
 * 解析 Javabean 文件，输出数据字典详细信息
 *
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2019/1/4 10:32.
 * @Modified By:
 */
public class AnalysisJavaFile
{
    public static String[] recursionAllFiles(String dirPath, FilenameFilter filter)
    {
        ArrayList<String> files = new ArrayList<>();
        File file = new File(dirPath);
        if (file.isFile())
        {
            files.add(file.getName());
            return files.toArray(new String[files.size()]);
        }
        else if (file.isDirectory())
        {
            recursionAllFiles(file, files, filter);
            return files.toArray(new String[files.size()]);
        }
        return new String[] {"There is problem with the file path entered!"};
    }

    private static List<String> recursionAllFiles(File file, List<String> data, FilenameFilter filter)
    {
        if (!file.exists())
        {
            return data;
        }
        File[] fileList = file.listFiles(filter);
        for (File f : fileList)
        {
            if (f.isDirectory())
            {
                recursionAllFiles(f, data, filter);
            }
            else
            {
                data.add(f.getName());
            }
        }
        if (file.isFile())
        {
            data.add(file.getName());
        }
        return data;
    }

    /**
     * 解析 .java文件入口，输出数据字典文件
     *
     * @param filePath
     * @param outputFile
     * @param titles
     * @return
     * @throws IOException
     */
    public static List<ClassInfo> analysisJavaFile(String filePath, String outputFile, String[] titles)
        throws IOException
    {
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists())
        {
            new RuntimeException("This file path was not found or does not exist!");
        }
        if (file.isFile() && filePath.endsWith(".java"))
        {
            //解析文件
            analysisJavaFile(classInfos, file);
        }
        else if (file.isDirectory())
        {
            //解析文件夹，遍历所有 .java文件
            File[] files = file.listFiles(pathname -> pathname.getName().endsWith(".java"));
            for (File f : files)
            {
                analysisJavaFile(classInfos, f);
            }
        }
        else
        {
            new RuntimeException("This file path was not found or does not exist!");
        }
        //输出到excel文件
        writeToExcel(classInfos, outputFile, titles);
        return classInfos;
    }

    /**
     * 解析java 文件
     *
     * @param classInfos 类信息包装类
     * @param file       解析文件
     * @throws IOException
     */
    private static void analysisJavaFile(ArrayList<ClassInfo> classInfos, File file)
        throws IOException
    {
        String line;
        BufferedReader reader = new BufferedReader(new FileReader(file));
        ClassInfo dic = new ClassInfo();
        //缓存行信息
        String[] before = {"", "", "", "", "", ""};
        while ((line = reader.readLine()) != null)
        {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("//"))
            {
                continue;
            }
            //字段解析
            if (line.contains("private ") && line.endsWith(";") && !line.contains("private static final long"))
            {
                Field field = analysisFieldInfo(line, before);
                before[0] = before[1] = before[2] = before[3] = before[4] = before[5] = "";
                dic.getField().add(field);
            }
            //类解析
            else if (line.contains(" class "))
            {
                analysisClassInfo(line, dic, before);
                before[0] = before[1] = before[2] = before[3] = before[4] = before[5] = "";
            }
            //段注释转换为行注释
            else if (line.startsWith("/**") && !line.endsWith("*/"))
            {
                String sum = combineComments(reader, line);
                moveLines(before, sum);
            }
            else
            {
                moveLines(before, line);
            }
        }
        if (dic.getClassName() != null)
        {
            classInfos.add(dic);
        }
    }

    /**
     * 解析字段名、类型、注解、注释信息
     *
     * @param line   行字符串
     * @param before 当前行以前的行字符串数组
     * @return
     */
    private static Field analysisFieldInfo(String line, String[] before)
    {
        Field field = new Field();
        String[] strings = line.trim().split(" ");
        String fld = strings[2];
        field.setName(fld.contains(";") ? fld.substring(0, fld.indexOf(";")) : fld);
        field.setType(strings[1]);

        for (String b : before)
        {
            if ((b.startsWith("/**") || b.startsWith("/*")) && b.endsWith("*/"))
            {
                int start = b.contains("/**") ? 3 : 2;
                field.setComment(b.substring(start, b.lastIndexOf("*")).trim());
            }
            if (b.contains("@Column"))
            {
                String substring = b.trim().substring(b.indexOf("(") + 1, b.lastIndexOf(")"));
                String[] columns = substring.split(",");
                fillFieldValue(field, columns);
            }
        }
        return field;
    }

    /**
     * 解析类名、类注解、类注释信息
     *
     * @param line   行字符串
     * @param dic    类信息字典
     * @param before 当前行以前的行字符串数组
     */
    private static void analysisClassInfo(String line, ClassInfo dic, String[] before)
    {
        String substring = line.substring(line.indexOf(" class ") + 7);
        String name = substring.substring(0, substring.indexOf(" "));
        dic.setClassName(name);

        for (String b : before)
        {
            if (b.startsWith("@Table"))
            {
                String substring1 = b.substring(b.indexOf("\"") + 1);
                int index = substring1.indexOf("\"");
                String table = substring1.substring(0, substring1.indexOf("\""));
                dic.setTableName(table);
            }
            if ((b.startsWith("/**") || b.startsWith("/*")) && b.endsWith("*/"))
            {
                int start = b.contains("/**") ? 3 : 2;
                dic.setClassComment(b.substring(start, b.lastIndexOf("*")).trim());
            }
        }
    }

    /**
     * 合并段注释，将多行注释合并成一行注释
     *
     * @param reader 阅读器
     * @param sum    注释行
     * @return
     * @throws IOException
     */
    private static String combineComments(BufferedReader reader, String sum)
        throws IOException
    {
        String next;
        while ((next = reader.readLine()) != null)
        {
            next = next.trim();
            if (next.endsWith("*/"))
            {
                sum += next;
                break;
            }
            if (next.startsWith("*") && !next.contains("@"))
            {
                String temp = next.substring(next.indexOf("*") + 1).trim();
                sum += temp;
            }
        }
        return sum;
    }

    /**
     * 移动行字符串，游标每向前读一行，字符串数组全部需要向前移动一行
     *
     * @param before 当前行以前的行字符串数组
     * @param sum    注释字符串
     */
    private static void moveLines(String[] before, String sum)
    {
        before[5] = before[4];
        before[4] = before[3];
        before[3] = before[2];
        before[2] = before[1];
        before[1] = before[0];
        before[0] = sum;
    }

    /**
     * 解析字段 @Column 信息，填充对应注解名、字段值
     *
     * @param field 字段实体
     * @param names
     */
    private static void fillFieldValue(Field field, String[] names)
    {
        for (String n : names)
        {
            String[] splits = n.split("=");
            String name = splits[0].trim();
            String value = splits[1].trim();
            String val = value.startsWith("\"") ?
                value.substring(value.indexOf("\"") + 1, value.lastIndexOf("\"")) : value;
            if ("name".equals(name))
            {
                field.setColumnName(val);
            }
            if ("length".equals(name))
            {
                field.setLength(val);
            }
            if ("unique".equals(name))
            {
                field.setUnique(val);
            }
            if ("nullable".equals(name))
            {
                field.setNullable(val);
            }
            if ("precision".equals(name))
            {
                field.setPrecision(val);
            }
            if ("scale".equals(name))
            {
                field.setScale(val);
            }
        }
    }

    /**
     * 输出解析结果，将结果写入excel文件内
     *
     * @param dataDics   数据字典集合
     * @param outputFile 输出文件
     * @param titles     表头
     */
    public static void writeToExcel(ArrayList<ClassInfo> dataDics, String outputFile, String[] titles)
    {
        //创建excel工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建工作表sheet
        HSSFSheet sheet = workbook.createSheet();
        //创建第一行
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = null;
        //插入第一行数据的表头
        for (int i = 0; i < titles.length; i++)
        {
            cell = row.createCell(i);
            cell.setCellValue(titles[i]);
        }
        //写入数据
        int size = dataDics.size();
        int i = 0;
        for (int index = 1; ; )
        {
            if (i >= size)
            {
                break;
            }
            ClassInfo dic = dataDics.get(i);
            i++;
            int len = dic.getField().size();
            for (int j = 0; j < len; j++)
            {
                Field field = dic.getField().get(j);
//表名", "类名", "类说明", "字段", "字段类型", "字段说明", "数据库列名", "字段长度", "unique", "nullable","precision","scale"
                HSSFRow nrow = sheet.createRow(index);
                HSSFCell ncell = nrow.createCell(0);
                ncell.setCellValue("" + index);
                index++;
                ncell = nrow.createCell(1);
                ncell.setCellValue(dic.getTableName());
                ncell = nrow.createCell(2);
                ncell.setCellValue(dic.getClassName());
                ncell = nrow.createCell(3);
                ncell.setCellValue(dic.getClassComment());
                ncell = nrow.createCell(4);
                ncell.setCellValue(field.getName());
                ncell = nrow.createCell(5);
                ncell.setCellValue(field.getType());
                ncell = nrow.createCell(6);
                ncell.setCellValue(field.getComment());

                ncell = nrow.createCell(7);
                ncell.setCellValue(field.getColumnName());
                ncell = nrow.createCell(8);
                ncell.setCellValue(field.getLength());
                ncell = nrow.createCell(9);
                ncell.setCellValue(field.getUnique());
                ncell = nrow.createCell(10);
                ncell.setCellValue(field.getNullable());
                ncell = nrow.createCell(11);
                ncell.setCellValue(field.getPrecision());
                ncell = nrow.createCell(12);
                ncell.setCellValue(field.getScale());
            }
        }
        //创建输出文件
        File file = new File(outputFile);
        try (FileOutputStream stream = new FileOutputStream(file);)
        {
            file.createNewFile();
            //将数据写入excel
            workbook.write(stream);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 获取 .class 文件里的类对象
     *
     * @param classPath class所处的位置(classes顾名思义就是编译后的.class文件所在位置)
     *                  比如: D:/work/springboot/target/classes/com/zsm/apidoc
     * @param className 获取的类名称  比如: ClassInfo
     * @return
     */
    public static Class<?> findClass(String classPath, String className)
    {
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        //获取路径classes的路径
        String prefix = classPath.substring(0, classPath.indexOf("classes") + 8);
        //这里得到的就是 com.zsm.apidoc 包的全名
        String packageName = classPath.substring(classPath.indexOf("classes") + 8).replaceAll("/", ".");
        try
        {
            URL classes = new URL("file:///" + classPath);
            ClassLoader loader = new URLClassLoader(new URL[] {classes}, systemClassLoader);
            Class<?> clazz = loader.loadClass(packageName + "." + className);
            return clazz;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
