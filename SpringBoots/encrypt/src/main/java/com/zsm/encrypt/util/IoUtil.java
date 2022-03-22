package com.zsm.encrypt.util;


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.util.Objects;


/**
 * @Author: zengsm.
 * @Date:Created in 2021-04-06 22:34.
 * @Description:
 */
public class IoUtil
{
    /**
     * 默认缓存大小
     */
    public static final int DEFAULT_BUFFER_SIZE = 1024;

    /**
     * 默认缓存大小
     */
    public static final int DEFAULT_LARGE_BUFFER_SIZE = 4096;

    /**
     * 数据流末尾
     */
    public static final int EOF = -1;

    private IoUtil()
    {
    }

    /**
     * 将Reader中的内容复制到Writer中 使用默认缓存大小
     *
     * @param reader Reader
     * @param writer Writer
     * @return 拷贝的字节数
     * @throws IOException IO异常
     */
    public static long copy(Reader reader, Writer writer)
        throws IOException
    {
        return copy(reader, writer, DEFAULT_BUFFER_SIZE, null);
    }

    /**
     * 将Reader中的内容复制到Writer中
     *
     * @param reader     Reader
     * @param writer     Writer
     * @param bufferSize 缓存大小
     * @return 传输的byte数
     * @throws IOException IO异常
     */
    public static long copy(Reader reader, Writer writer, int bufferSize)
        throws IOException
    {
        return copy(reader, writer, bufferSize, null);
    }

    /**
     * 将Reader中的内容复制到Writer中
     *
     * @param reader           Reader
     * @param writer           Writer
     * @param bufferSize       缓存大小
     * @param progressNotifier 进度处理器
     * @return 传输的byte数
     * @throws IOException IO异常
     */
    public static long copy(Reader reader, Writer writer, int bufferSize, ProgressNotifier progressNotifier)
        throws IOException
    {
        char[] buffer = new char[bufferSize];
        long size = 0;
        int readSize;
        if (null != progressNotifier)
        {
            progressNotifier.start();
        }
        while ((readSize = reader.read(buffer, 0, bufferSize)) != EOF)
        {
            writer.write(buffer, 0, readSize);
            size += readSize;
            writer.flush();
            if (null != progressNotifier)
            {
                progressNotifier.progressed(size);
            }
        }
        if (null != progressNotifier)
        {
            progressNotifier.finish(size);
        }
        return size;
    }

    /**
     * 拷贝流，使用默认Buffer大小
     *
     * @param in  输入流
     * @param out 输出流
     * @return 传输的byte数
     * @throws IOException IO异常
     */
    public static long copy(InputStream in, OutputStream out)
        throws IOException
    {
        return copy(in, out, DEFAULT_BUFFER_SIZE, null);
    }

    /**
     * 拷贝流
     *
     * @param in         输入流
     * @param out        输出流
     * @param bufferSize 缓存大小
     * @return 传输的byte数
     * @throws IOException IO异常
     */
    public static long copy(InputStream in, OutputStream out, int bufferSize)
        throws IOException
    {
        return copy(in, out, bufferSize, null);
    }

    /**
     * 拷贝流
     *
     * @param in               输入流
     * @param out              输出流
     * @param bufferSize       缓存大小
     * @param progressNotifier 进度条
     * @return 传输的byte数
     * @throws IOException IO异常
     */
    public static long copy(InputStream in, OutputStream out, int bufferSize, ProgressNotifier progressNotifier)
        throws IOException
    {
        Objects.requireNonNull(in, "InputStream is null !");
        Objects.requireNonNull(out, "OutputStream is null !");
        if (bufferSize <= 0)
        {
            bufferSize = DEFAULT_BUFFER_SIZE;
        }

        byte[] buffer = new byte[bufferSize];
        long size = 0;
        if (null != progressNotifier)
        {
            progressNotifier.start();
        }
        for (int readSize = -1; (readSize = in.read(buffer)) != EOF; )
        {
            out.write(buffer, 0, readSize);
            size += readSize;
            out.flush();
            if (null != progressNotifier)
            {
                progressNotifier.progressed(size);
            }
        }
        if (null != progressNotifier)
        {
            progressNotifier.finish(size);
        }
        return size;
    }

    /**
     * 拷贝流 thanks to: https://github.com/venusdrogon/feilong-io/blob/master/src/main/java/com/feilong/io/IOWriteUtil.java
     *
     * @param in               输入流
     * @param out              输出流
     * @param bufferSize       缓存大小
     * @param progressNotifier 进度条
     * @return 传输的byte数
     * @throws IOException IO异常
     */
    public static long copyByNIO(InputStream in, OutputStream out, int bufferSize, ProgressNotifier progressNotifier)
        throws IOException
    {
        return copy(Channels.newChannel(in), Channels.newChannel(out), bufferSize, progressNotifier);
    }

    /**
     * 拷贝文件流，使用NIO
     *
     * @param in  输入
     * @param out 输出
     * @return 拷贝的字节数
     * @throws IOException IO异常
     */
    public static long copy(FileInputStream in, FileOutputStream out)
        throws IOException
    {
        Objects.requireNonNull(in, "FileInputStream is null!");
        Objects.requireNonNull(out, "FileOutputStream is null!");

        final FileChannel inChannel = in.getChannel();
        final FileChannel outChannel = out.getChannel();

        return inChannel.transferTo(0, inChannel.size(), outChannel);
    }

    /**
     * 拷贝流，使用NIO，不会关闭流
     *
     * @param in               {@link ReadableByteChannel}
     * @param out              {@link WritableByteChannel}
     * @param bufferSize       缓冲大小，如果小于等于0，使用默认
     * @param progressNotifier {@link ProgressNotifier}进度处理器
     * @return 拷贝的字节数
     * @throws IOException IO异常
     */
    public static long copy(ReadableByteChannel in, WritableByteChannel out, int bufferSize,
                            ProgressNotifier progressNotifier)
        throws IOException
    {
        Objects.requireNonNull(in, "InputStream is null !");
        Objects.requireNonNull(out, "OutputStream is null !");

        ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize <= 0 ? DEFAULT_BUFFER_SIZE : bufferSize);
        long size = 0;
        if (null != progressNotifier)
        {
            progressNotifier.start();
        }
        while (in.read(byteBuffer) != EOF)
        {
            byteBuffer.flip();// 写转读
            size += out.write(byteBuffer);
            byteBuffer.clear();
            if (null != progressNotifier)
            {
                progressNotifier.progressed(size);
            }
        }
        if (null != progressNotifier)
        {
            progressNotifier.finish(size);
        }

        return size;
    }

    /**
     * 获得一个文件读取器
     *
     * @param in          输入流
     * @param charsetName 字符集名称
     * @return BufferedReader对象
     */
    public static BufferedReader getReader(InputStream in, String charsetName)
    {
        return getReader(in, CharsetUtil.charset(charsetName));
    }

    /**
     * 获得一个Reader
     *
     * @param in      输入流
     * @param charset 字符集
     * @return BufferedReader对象
     */
    public static BufferedReader getReader(InputStream in, Charset charset)
    {
        if (null == in)
        {
            return null;
        }

        InputStreamReader reader = null;
        if (null == charset)
        {
            reader = new InputStreamReader(in);
        }
        else
        {
            reader = new InputStreamReader(in, charset);
        }

        return new BufferedReader(reader);
    }

    /**
     * 获得{@link BufferedReader}<br>
     * 如果是{@link BufferedReader}强转返回，否则新建。如果提供的Reader为null返回null
     *
     * @param reader 普通Reader，如果为null返回null
     * @return {@link BufferedReader} or null
     * @since 3.0.9
     */
    public static BufferedReader getReader(Reader reader)
    {
        if (null == reader)
        {
            return null;
        }
        return (reader instanceof BufferedReader) ? (BufferedReader)reader : new BufferedReader(reader);
    }

    /**
     * 获得{@link PushbackReader}<br>
     * 如果是{@link PushbackReader}强转返回，否则新建
     *
     * @param reader       普通Reader
     * @param pushBackSize 推后的byte数
     * @return {@link PushbackReader}
     * @since 3.1.0
     */
    public static PushbackReader getPushBackReader(Reader reader, int pushBackSize)
    {
        return (reader instanceof PushbackReader) ? (PushbackReader)reader : new PushbackReader(reader, pushBackSize);
    }

    /**
     * 获得一个Writer
     *
     * @param out         输入流
     * @param charsetName 字符集
     * @return OutputStreamWriter对象
     */
    public static OutputStreamWriter getWriter(OutputStream out, String charsetName)
    {
        return getWriter(out, Charset.forName(charsetName));
    }

    /**
     * 获得一个Writer
     *
     * @param out     输入流
     * @param charset 字符集
     * @return OutputStreamWriter对象
     */
    public static OutputStreamWriter getWriter(OutputStream out, Charset charset)
    {
        if (null == out)
        {
            return null;
        }

        if (null == charset)
        {
            return new OutputStreamWriter(out);
        }
        else
        {
            return new OutputStreamWriter(out, charset);
        }
    }

    /**
     * 从流中读取内容
     *
     * @param in          输入流
     * @param charsetName 字符集
     * @return 内容
     * @throws IOException IO异常
     */
    public static String read(InputStream in, String charsetName)
        throws IOException
    {
        FastByteArrayOutputStream out = read(in);
        return StrUtil.isBlank(charsetName) ? out.toString() : out.toString(charsetName);
    }

    /**
     * 从流中读取内容，读取完毕后并不关闭流
     *
     * @param in      输入流，读取完毕后并不关闭流
     * @param charset 字符集
     * @return 内容
     * @throws IOException IO异常
     */
    public static String read(InputStream in, Charset charset)
        throws IOException
    {
        FastByteArrayOutputStream out = read(in);
        return null == charset ? out.toString() : out.toString(charset);
    }

    /**
     * 从流中读取内容，读到输出流中
     *
     * @param in 输入流
     * @return 输出流
     * @throws IOException IO异常
     */
    public static FastByteArrayOutputStream read(InputStream in)
        throws IOException
    {
        final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        copy(in, out);
        return out;
    }

    /**
     * 从Reader中读取String，读取完毕后并不关闭Reader
     *
     * @param reader Reader
     * @return String
     * @throws IOException IO异常
     */
    public static String read(Reader reader)
        throws IOException
    {
        final StringBuilder builder = StrUtil.builder();
        final CharBuffer buffer = CharBuffer.allocate(DEFAULT_BUFFER_SIZE);
        while (-1 != reader.read(buffer))
        {
            builder.append(buffer.flip().toString());
        }
        return builder.toString();
    }

    public static InputStream getFileInputStream(String fileName)
    {
        try
        {
            return new FileInputStream(fileName);
        }
        catch (Exception e) {return null;}
    }

    public static OutputStream getFileOutputStream(String fileName)
    {
        try
        {
            return new FileOutputStream(fileName);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * 自己负责关闭流
     *
     * @param is 输入流
     * @return 自己数组
     * @throws IOException IO异常
     */
    public static byte[] stream2Bytes(InputStream is)
        throws IOException
    {
        //ByteArrayOutputStream stream = new ByteArrayOutputStream();
        FastByteArrayOutputStream stream = new FastByteArrayOutputStream();
        byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
        int len = is.read(bytes);
        while (len != -1)
        {
            stream.write(bytes, 0, len);
            len = is.read(bytes);
        }
        return stream.toByteArray();
    }

    public static File copy2File(InputStream inputStream, File file)
        throws IOException
    {
        OutputStream stream = new FileOutputStream(file);
        IoUtil.copy(inputStream, stream);
        IoUtil.close(stream);
        return file;
    }

    /**
     * 关闭Closable
     *
     * @param closeable 可关闭的
     */
    public static void close(Closeable closeable)
    {
        if (null != closeable)
        {
            try
            {
                closeable.close();
            }
            catch (Exception e) {}
        }
    }

    /**
     * 关闭多个Closable
     *
     * @param closeable 第一个
     * @param others    其他的
     */
    public static void closeAll(Closeable closeable, Closeable... others)
    {
        close(closeable);
        for (Closeable other : others)
        {
            close(other);
        }
    }

    /**
     * 一个空的流，主要为了防止空指针
     */
    public static InputStream emptyStream()
    {
        return new ByteArrayInputStream(new byte[] {});
    }
}
