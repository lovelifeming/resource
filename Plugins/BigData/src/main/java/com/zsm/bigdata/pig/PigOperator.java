package com.zsm.bigdata.pig;

import org.apache.pig.PigServer;

import java.io.IOException;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/3/6 12:33.
 * @Modified By:
 */
public class PigOperator
{
    public static void excutePig(String execTypeString, String jarPath, String input, String output)
        throws IOException
    {
        PigServer pigServer = new PigServer(execTypeString);
        pigServer.registerJar(jarPath);
        //String input = "/opt/sf/input.txt";
        //String output = "/opt/sf/output.txt";
        pigServer.registerQuery("A = load'" + input + "' using TextLoader();");
        pigServer.registerQuery("B = foreach A generate flatten(tokenize($0));");
        pigServer.registerQuery("C = group B by $1");
        pigServer.registerQuery("D = foreach C generate flatten(group),COUNT(B.$0)");
        pigServer.store("D", output);

    }
}
