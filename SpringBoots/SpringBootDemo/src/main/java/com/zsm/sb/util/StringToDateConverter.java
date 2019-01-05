package com.zsm.sb.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/4/17.
 * @Modified By:
 */
public class StringToDateConverter implements Converter<String, Date>
{
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String SHORT_DATE_FORMAT = "yyyy-MM-dd";

    private static final String DATE_FORMAT2 = "yyyy/MM/dd HH:mm:ss";

    private static final String SHORT_DATE_FORMAT2 = "yyyy/MM/dd";

    private static final String BACKSLASH = "/";

    private static final String HYPHEN = "-";

    private static final String COLON = ":";

    @Nullable
    @Override
    public Date convert(String source)
    {
        if (StringUtils.isEmpty(source))
        {
            return null;
        }
        source = source.trim();
        Date date;
        try
        {
            if (source.contains(HYPHEN))
            {
                date = getDate(source, DATE_FORMAT, SHORT_DATE_FORMAT);
                return date;
            }
            else if (source.contains(BACKSLASH))
            {
                date = getDate(source, DATE_FORMAT2, SHORT_DATE_FORMAT2);
                return date;
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            throw new RuntimeException(String.format("parser %s to Date fail", source));
        }
        throw new RuntimeException(String.format("parser %s to Date fail", source));
    }

    private Date getDate(String source, String dateFormat, String shortDateFormat)
        throws ParseException
    {
        SimpleDateFormat format;
        Date date;
        if (source.contains(COLON))
        {
            format = new SimpleDateFormat(dateFormat);
            date = format.parse(source);
        }
        else
        {
            format = new SimpleDateFormat(shortDateFormat);
            date = format.parse(source);
        }
        return date;
    }
}
