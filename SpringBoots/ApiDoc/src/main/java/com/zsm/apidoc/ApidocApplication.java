package com.zsm.apidoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


@SpringBootApplication
public class ApidocApplication
{
    public static void main(String[] args)
        throws Exception
    {
        SpringApplication.run(ApidocApplication.class, args);
//        String filePath = "C:\\Users\\Administrator\\Desktop\\攀钢轨梁\\待整理\\实体\\domain";
//        String[] titles = {"编号", "表名", "类名", "类说明", "字段", "字段类型", "字段说明",
//            "columnName", "length", "unique", "nullable", "precision", "scale"};
//        AnalysisFile.analysisJavaFile(filePath, "D:\\api.xls", titles);
//
//        String tableFiles = "C:\\Users\\Administrator\\Desktop\\攀钢轨梁\\待整理\\tables";
//
////        String[] classNames = AnalysisFile.recursionAllFiles(filePath, (dir, name) -> name.endsWith(".java"));
//        String[] tablesNames = AnalysisFile.recursionAllFiles(tableFiles, (dir, name) -> name.endsWith(".sql"));
//
//        ArrayList<String> compare = new ArrayList<>();
//        String temp;
////        for (int i = 0; i < classNames.length; i++)
////        {
////            temp = classNames[i];
////            classNames[i] = temp.substring(0, temp.lastIndexOf("."));
////        }
//        for (int i = 0; i < tablesNames.length; i++)
//        {
//            temp = tablesNames[i];
//            tablesNames[i] = temp.substring(temp.indexOf(".") + 1, temp.lastIndexOf("."));
//        }
//
////        List<String> classNameList = Arrays.asList(classNames);
//
//        List<String> tableNameList = new ArrayList<>();
//        for (String s : tablesNames)
//        {
//            tableNameList.add(s);
//        }
//
//        //tableNameList.retainAll(AnalysisFile.TABLE_NAME);
//        ArrayList<String> tableName = AnalysisFile.TABLE_NAME;
////        tableName.removeAll(tableNameList);
//
//        writeTxt("D:\\SQL表.txt", tableName);

    }

    public static void writeTxt(String filePath, List data)
    {
        File file = new File(filePath);
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file)))
        {
            for (int i = 0; i < data.size(); i++)
            {
                bw.write(String.valueOf(data.get(i)));
                bw.newLine();
                bw.flush();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
