package com.example.core;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * A data class representing HTTP Post parameter
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class HttpParameter implements Comparable<HttpParameter>, java.io.Serializable {
    private static final long serialVersionUID = 4046908449190454692L;
    private String name = null;
    private String value = null;
    private File file = null;
    private InputStream fileBody = null;

    public HttpParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public HttpParameter(String name, File file) {
        this.name = name;
        this.file = file;
    }

    public HttpParameter(String name, String fileName, InputStream fileBody) {
        this.name = name;
        this.file = new File(fileName);
        this.fileBody = fileBody;
    }

    public HttpParameter(String name, int value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public HttpParameter(String name, long value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public HttpParameter(String name, double value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public HttpParameter(String name, boolean value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public File getFile() {
        return file;
    }

    public InputStream getFileBody() {
        return fileBody;
    }

    public boolean isFile() {
        return file != null;
    }

    public boolean hasFileBody() {
        return fileBody != null;
    }

    private static final String JPEG = "image/jpeg";
    private static final String GIF = "image/gif";
    private static final String PNG = "image/png";
    private static final String OCTET = "application/octet-stream";

    /**
     * @return content-type
     */
    public String getContentType() {
        if (!isFile()) {
            throw new IllegalStateException("not a file");
        }
        String contentType;
        String extensions = file.getName();
        int index = extensions.lastIndexOf(".");
        if (-1 == index) {
            // no extension
            contentType = OCTET;
        } else {
            extensions = extensions.substring(extensions.lastIndexOf(".") + 1).toLowerCase();
            if (extensions.length() == 3) {
                if ("gif".equals(extensions)) {
                    contentType = GIF;
                } else if ("png".equals(extensions)) {
                    contentType = PNG;
                } else if ("jpg".equals(extensions)) {
                    contentType = JPEG;
                } else {
                    contentType = OCTET;
                }
            } else if (extensions.length() == 4) {
                if ("jpeg".equals(extensions)) {
                    contentType = JPEG;
                } else {
                    contentType = OCTET;
                }
            } else {
                contentType = OCTET;
            }
        }
        return contentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HttpParameter that = (HttpParameter) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        if (file != null ? !file.equals(that.file) : that.file != null) return false;
        return fileBody != null ? fileBody.equals(that.fileBody) : that.fileBody == null;

    }

    public static boolean containsFile(HttpParameter[] params) {
        boolean containsFile = false;
        if (null == params) {
            return false;
        }
        for (HttpParameter param : params) {
            if (param.isFile()) {
                containsFile = true;
                break;
            }
        }
        return containsFile;
    }

    /*package*/
    static boolean containsFile(List<HttpParameter> params) {
        boolean containsFile = false;
        for (HttpParameter param : params) {
            if (param.isFile()) {
                containsFile = true;
                break;
            }
        }
        return containsFile;
    }

    public static HttpParameter[] getParameterArray(String name, String value) {
        return new HttpParameter[]{new HttpParameter(name, value)};
    }

    public static HttpParameter[] getParameterArray(String name, int value) {
        return getParameterArray(name, String.valueOf(value));
    }

    public static HttpParameter[] getParameterArray(String name1, String value1
            , String name2, String value2) {
        return new HttpParameter[]{new HttpParameter(name1, value1)
                , new HttpParameter(name2, value2)};
    }

    public static HttpParameter[] getParameterArray(String name1, int value1
            , String name2, int value2) {
        return getParameterArray(name1, String.valueOf(value1), name2, String.valueOf(value2));
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (file != null ? file.hashCode() : 0);
        result = 31 * result + (fileBody != null ? fileBody.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HttpParameter{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", file=" + file +
                ", fileBody=" + fileBody +
                '}';
    }

    @Override
    public int compareTo(HttpParameter o) {
        int compared = 0;
        if (name != null) {
            compared = name.compareTo(o.name);
        }
        if (0 == compared) {
            if (value != null) {
                compared = value.compareTo(o.value);
            }
        }
        return compared;
    }

    public static String encodeParameters(HttpParameter[] httpParams) {
        if (null == httpParams) {
            return "";
        }
        StringBuilder buf = new StringBuilder();
        for (int j = 0; j < httpParams.length; j++) {
            if (httpParams[j].isFile()) {
                throw new IllegalArgumentException("parameter [" + httpParams[j].name + "]should be text");
            }
            if (j != 0) {
                buf.append("&");
            }
            buf.append(encode(httpParams[j].name))
                    .append("=").append(encode(httpParams[j].value));
        }
        return buf.toString();
    }

    /**
     * @param value string to be encoded
     * @return encoded string
     * @see <a href="http://wiki.oauth.net/TestCases">OAuth / TestCases</a>
     * @see <a href="http://groups.google.com/group/oauth/browse_thread/thread/a8398d0521f4ae3d/9d79b698ab217df2?hl=en&lnk=gst&q=space+encoding#9d79b698ab217df2">Space encoding - OAuth | Google Groups</a>
     * @see <a href="http://tools.ietf.org/html/rfc3986#section-2.1">RFC 3986 - Uniform Resource Identifier (URI): Generic Syntax - 2.1. Percent-Encoding</a>
     */
    public static String encode(String value) {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
        }
        StringBuilder buf = new StringBuilder(encoded.length());
        char focus;
        for (int i = 0; i < encoded.length(); i++) {
            focus = encoded.charAt(i);
            if (focus == '*') {
                buf.append("%2A");
            } else if (focus == '+') {
                buf.append("%20");
            } else if (focus == '%' && (i + 1) < encoded.length()
                    && encoded.charAt(i + 1) == '7' && encoded.charAt(i + 2) == 'E') {
                buf.append('~');
                i += 2;
            } else {
                buf.append(focus);
            }
        }
        return buf.toString();
    }

    /**
     * @param value string to be decoded. The natural opposite of encode() above.
     * @return encoded string
     * @see <a href="http://wiki.oauth.net/TestCases">OAuth / TestCases</a>
     * @see <a href="http://groups.google.com/group/oauth/browse_thread/thread/a8398d0521f4ae3d/9d79b698ab217df2?hl=en&lnk=gst&q=space+encoding#9d79b698ab217df2">Space encoding - OAuth | Google Groups</a>
     * @see <a href="http://tools.ietf.org/html/rfc3986#section-2.1">RFC 3986 - Uniform Resource Identifier (URI): Generic Syntax - 2.1. Percent-Encoding</a>
     */
    public static String decode(String value) {
        value = value.replace("%2A", "*");
        value = value.replace("%2a", "*");
        value = value.replace("%20", " ");

        String decoded=null;
        try {
            decoded = URLDecoder.decode(value, "UTF-8");
        }
        catch(UnsupportedEncodingException ignore) {
        }

        return decoded;
    }

    /**
     * Parses a query string without the leading "?"
     *
     * @param queryParameters a query parameter string, like a=hello&amp;b=world
     * @return decoded parameters
     */
    public static List<HttpParameter> decodeParameters(String queryParameters) {
        List<HttpParameter> result=new ArrayList<>();
        for (String pair : queryParameters.split("&")) {
            String[] parts=pair.split("=", 2);
            if(parts.length == 2) {
                String name=decode(parts[0]);
                String value=decode(parts[1]);
                if(!name.equals("") && !value.equals(""))
                    result.add(new HttpParameter(name, value));
            }
        }
        return result;
    }
}
