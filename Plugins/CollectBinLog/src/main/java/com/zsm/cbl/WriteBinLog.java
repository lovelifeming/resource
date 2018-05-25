package com.zsm.cbl;

import com.alibaba.fastjson.JSONObject;
import com.zsm.cbl.model.BinlogAllInfo;
import com.zsm.cbl.model.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * 表格信息写操作类
 *
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/4/26.
 * @Modified By:
 */
public class WriteBinLog
{
    private static final Logger LOGGER = LoggerFactory.getLogger(WriteBinLog.class);

    private String TABLE_INFO_PATH = "/opt/rh/tableinfo.log";

    private String ALL_INFO_PATH = "/opt/rh/allinfo.log";

    public void setTABLE_INFO_PATH(String tableInfoPath)
    {
        this.TABLE_INFO_PATH = tableInfoPath;
    }

    public void setALL_INFO_PATH(String allInfoPath)
    {
        this.ALL_INFO_PATH = allInfoPath;
    }

    public void write(BinlogAllInfo binlogAllInfo)
    {
        write(ALL_INFO_PATH, binlogAllInfo);
    }

    public void write(TableInfo tableInfo)
    {
        write(TABLE_INFO_PATH, tableInfo);
    }

    public void write(String filePath, Object object)
    {
        boolean flag = validateFile(filePath);
        if (flag)
        {
            try
            {
                BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true));
                bw.write(JSONObject.toJSONString(object));
                bw.newLine();
                bw.flush();
                bw.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                LOGGER.error(e.getMessage());
            }
        }
    }

    private boolean validateFile(String filePath)
    {
        File file = new File(filePath);
        if (!file.exists())
        {
            try
            {
                file.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                LOGGER.error(e.getMessage());
                return false;
            }
        }
        return true;
    }
}
