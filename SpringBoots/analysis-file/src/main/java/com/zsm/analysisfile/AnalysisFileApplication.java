package com.zsm.analysisfile;

import com.zsm.analysisfile.javafile.AnalysisJavaFile;
import com.zsm.analysisfile.javafile.ClassInfo;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


@SpringBootApplication
public class AnalysisFileApplication
{

    public static void main(String[] args)
        throws IOException
    {
        //SpringApplication.run(AnalysisFileApplication.class, args);
        String filePath = "C:\\Users\\Administrator\\Desktop\\攀钢轨梁\\业务系统数据库脚本\\MES实体";
        String[] titles = {"编号", "表名", "类名", "类说明", "字段", "字段类型", "字段说明",
            "columnName", "length", "unique", "nullable", "precision", "scale"};
        List<ClassInfo> classInfos = AnalysisJavaFile.analysisJavaFile(filePath, "D:\\api.xls", titles);

//        String tableFiles = "C:\\Users\\Administrator\\Desktop\\攀钢轨梁\\待整理\\tables";

//        String[] classNames = AnalysisJavaFile.recursionAllFiles(filePath, (dir, name) -> name.endsWith(".java"));
        //String[] tablesNames = AnalysisJavaFile.recursionAllFiles(tableFiles, (dir, name) -> name.endsWith(".sql"));
//
//        ArrayList<String> compare = new ArrayList<>();
//        String temp;
//        for (int i = 0; i < classNames.length; i++)
//        {
//            temp = classNames[i];
//            classNames[i] = temp.substring(0, temp.lastIndexOf("."));
//        }
//        for (int i = 0; i < tablesNames.length; i++)
//        {
//            temp = tablesNames[i];
//            tablesNames[i] = temp.substring(temp.indexOf(".") + 1, temp.lastIndexOf("."));
//        }

//        List<String> classNameList = Arrays.asList(classNames);
//        List<String> tableNameList = new ArrayList<>();
//        for (String s : tablesNames)
//        {
//            tableNameList.add(s);
//        }

        //tableNameList.retainAll(AnalysisJavaFile.TABLE_NAME);
//        ArrayList<String> tableName = AnalysisJavaFile.TABLE_NAME;
//        tableName.removeAll(tableNameList);

//        List<String> obj = new ArrayList<>();
//        for (ClassInfo info : classInfos)
//        {
//            String tmp = info.getClassName() + "\t" + info.getClassComment() + "\t" + info.getTableName();
//            obj.add(tmp);
//        }
//
//        writeTxt("D:\\MES实体.txt", obj);

    }

    public static void writeTxt(String filePath, List data)
    {
        File file = new File(filePath);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file)))
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
