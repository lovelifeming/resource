package com.zsm.analysisfile.javafile;

import java.util.ArrayList;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2019/1/2 14:29.
 * @Modified By:
 */
public class ClassInfo
{
    private String tableName;

    private String className;

    private String classComment;

    private ArrayList<Field> field = new ArrayList<>();

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public String getClassComment()
    {
        return classComment;
    }

    public void setClassComment(String classComment)
    {
        this.classComment = classComment;
    }

    public ArrayList<Field> getField()
    {
        return field;
    }

    public void setField(ArrayList<Field> field)
    {
        this.field = field;
    }
}


class Field
{
    private String name;

    private String type;

    private String comment;

    private String columnName;

    private String length;

    private String unique;

    private String nullable;

    private String precision;

    private String scale;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public String getColumnName()
    {
        return columnName;
    }

    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }

    public String getLength()
    {
        return length;
    }

    public void setLength(String length)
    {
        this.length = length;
    }

    public String getUnique()
    {
        return unique;
    }

    public void setUnique(String unique)
    {
        this.unique = unique;
    }

    public String getNullable()
    {
        return nullable;
    }

    public void setNullable(String nullable)
    {
        this.nullable = nullable;
    }

    public String getPrecision()
    {
        return precision;
    }

    public void setPrecision(String precision)
    {
        this.precision = precision;
    }

    public String getScale()
    {
        return scale;
    }

    public void setScale(String scale)
    {
        this.scale = scale;
    }
}