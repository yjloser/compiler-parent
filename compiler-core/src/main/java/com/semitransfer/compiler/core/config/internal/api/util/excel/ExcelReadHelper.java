package com.semitransfer.compiler.core.config.internal.api.util.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * <p>
 * 解析Excel，支持2003、2007
 * </p>
 *
 * @author Mr.Yang
 * @since 2019/4/20
 */
public class ExcelReadHelper {

    /**
     * 解析Excel 支持2003、2007<br>
     * 利用反射技术完成propertis到obj对象的映射，并将相对应的值利用相对应setter方法设置到obj对象中最后add到list集合中<br>
     * properties、obj需要符合如下规则：<br>
     * 1、obj对象必须存在默认构造函数，且属性需存在setter方法<br>
     * 2、properties中的值必须是在obj中存在的属性，且obj中必须存在这些属性的setter方法。<br>
     * 3、properties中值得顺序要与Excel中列相相应，否则值会设置错：<br>
     * excel:编号    姓名         年龄       性别<br>
     * properties:id  name  age  sex<br>
     *
     * @param file       待解析的Excel文件
     * @param properties 与Excel相对应的属性
     * @param obj        反射对象的Class
     * @throws Exception
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static List<Object> excelRead(File file, String[] properties, Class obj) throws Exception {
        Workbook book;
        try {
            //解析2003
            book = new XSSFWorkbook(new FileInputStream(file));
        } catch (Exception e) {
            //解析2007
            book = new HSSFWorkbook(new FileInputStream(file));
        }
        return getExcelContent(book, properties, obj);
    }

    /**
     * 解析Excel 支持2003、2007<br>
     * 利用反射技术完成propertis到obj对象的映射，并将相对应的值利用相对应setter方法设置到obj对象中最后add到list集合中<br>
     * properties、obj需要符合如下规则：<br>
     * 1、obj对象必须存在默认构造函数，且属性需存在setter方法<br>
     * 2、properties中的值必须是在obj中存在的属性，且obj中必须存在这些属性的setter方法。<br>
     * 3、properties中值得顺序要与Excel中列相相应，否则值会设置错：<br>
     * excel：编号    姓名         年龄       性别<br>
     * properties：id  name  age  sex<br>
     *
     * @param filePath   待解析的Excel文件的路径
     * @param properties 与Excel相对应的属性
     * @param obj        反射对象的Class
     * @return 返回解析结果信息
     * @throws Exception
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static List<Object> excelRead(String filePath, String[] properties, Class obj) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new Exception("指定的文件不存在");
        }
        return excelRead(file, properties, obj);
    }

    /**
     * 根据params、object解析Excel，并且构建list集合
     *
     * @param book       WorkBook对象，他代表了待将解析的Excel文件
     * @param properties 需要参考Object的属性
     * @param obj        构建的Object对象，每一个row都相当于一个object对象
     * @return 返回解析结果信息
     * @throws Exception
     * @author Mr.Yang
     * @date 2018/12/1
     */
    private static List<Object> getExcelContent(Workbook book, String[] properties,
                                                Class obj) throws Exception {
        //初始化结果
        List<Object> resultList = new ArrayList<>();
        Map<String, Method> methodMap = getObjectSetterMethod(obj);
        Map<String, Field> fieldMap = getObjectField(obj);
        for (int numSheet = 0; numSheet < book.getNumberOfSheets(); numSheet++) {
            Sheet sheet = book.getSheetAt(numSheet);
            //谨防中间空一行
            if (sheet == null) {
                continue;
            }
            //一个row就相当于一个Object
            for (int numRow = 1; numRow <= sheet.getLastRowNum(); numRow++) {
                Row row = sheet.getRow(numRow);
                if (row == null) {
                    continue;
                }
                resultList.add(getObject(row, properties, methodMap, fieldMap, obj));
            }
        }
        return resultList;
    }

    /**
     * 获取row的数据，利用反射机制构建Object对象
     *
     * @param row        row对象
     * @param properties Object参考的属性
     * @param methodMap  object对象的setter方法映射
     * @param fieldMap   object对象的属性映射
     * @throws Exception
     * @author Mr.Yang
     * @date 2018/12/1
     */
    private static Object getObject(Row row, String[] properties,
                                    Map<String, Method> methodMap, Map<String, Field> fieldMap, Class obj) throws Exception {
        Object object = obj.newInstance();
        for (int numCell = 0; numCell < row.getLastCellNum(); numCell++) {
            Cell cell = row.getCell(numCell);
            if (cell == null) {
                continue;
            }
            String cellValue = getValue(cell);
            String property = properties[numCell].toLowerCase();
            //该property在object对象中对应的属性
            Field field = fieldMap.get(property);
            //该property在object对象中对应的setter方法
            Method method = methodMap.get(property);
            setObjectPropertyValue(object, field, method, cellValue);
        }
        return object;
    }

    /**
     * 根据指定属性的的setter方法给object对象设置值
     *
     * @param obj    object对象
     * @param field  object对象的属性
     * @param method object对象属性的相对应的方法
     * @param value  需要设置的值
     * @throws Exception
     * @author Mr.Yang
     * @date 2018/12/1
     */
    private static void setObjectPropertyValue(Object obj, Field field,
                                               Method method, String value) throws Exception {
        Object[] oo = new Object[1];
        String type = field.getType().getName();
        if ("java.lang.String".equals(type) || "String".equals(type)) {
            oo[0] = value;
        } else if ("java.lang.Integer".equals(type) || "java.lang.int".equals(type) || "Integer".equals(type) || "int".equals(type)) {
            if (value.length() > 0) {
                oo[0] = Integer.valueOf(value);
            }
        } else if ("java.lang.Float".equals(type) || "java.lang.float".equals(type) || "Float".equals(type) || "float".equals(type)) {
            if (value.length() > 0) {
                oo[0] = Float.valueOf(value);
            }
        } else if ("java.lang.Double".equals(type) || "java.lang.double".equals(type) || "Double".equals(type) || "double".equals(type)) {
            if (value.length() > 0) {
                oo[0] = Double.valueOf(value);
            }
        } else if ("java.math.BigDecimal".equals(type) || "BigDecimal".equals(type)) {
            if (value.length() > 0) {
                oo[0] = new BigDecimal(value);
            }
        } else if ("java.util.Date".equals(type) || "Date".equals(type)) {
            //当长度为19(yyyy-MM-dd HH24:mm:ss)或者为14(yyyyMMddHH24mmss)时 格式转换为yyyyMMddHH24mmss
            if (value.length() > 0) {
                if (value.length() == 19 || value.length() == 14) {
                    oo[0] = string2Date(value, "yyyy-MM-dd HH:mm:ss");
                } else {
                    //其余全部转换为yyyyMMdd格式
                    oo[0] = string2Date(value, "yyyy-MM-dd");
                }
            }
        } else if ("java.sql.Timestamp".equals(type)) {
            if (value.length() > 0) {
                oo[0] = formatDate(value, "yyyy-MM-dd HH:mm:ss");
            }
        } else if ("java.lang.Boolean".equals(type) || "Boolean".equals(type)) {
            if (value.length() > 0) {
                oo[0] = Boolean.valueOf(value);
            }
        } else if ("java.lang.Long".equals(type) || "java.lang.long".equals(type) || "Long".equals(type) || "long".equals(type)) {
            if (value.length() > 0) {
                oo[0] = Long.valueOf(value);
            }
        }
        try {
            method.invoke(obj, oo);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * 将字符串(格式符合规范)转换成Date
     *
     * @param value  需要转换的字符串
     * @param format 日期格式
     * @return Date
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static Date string2Date(String value, String format) {
        if (value == null || "".equals(value)) {
            return null;
        }
        SimpleDateFormat sdf = getFormat(format);
        Date date = null;
        try {
            value = formatDate(value, format);
            date = sdf.parse(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取日期显示格式，为空默认为yyyy-mm-dd HH:mm:ss
     *
     * @param format 时间格式
     * @author Mr.Yang
     * @date 2018/12/1
     */
    protected static SimpleDateFormat getFormat(String format) {
        if (format == null || "".equals(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        return new SimpleDateFormat(format);
    }

    /**
     * 字符串时间格式化
     *
     * @param date 时间
     * @return 返回格式化后字符串时间
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static String formatDate(String date, String format) {
        if (date == null || "".equals(date)) {
            return "";
        }
        Date dt;
        SimpleDateFormat inFmt;
        SimpleDateFormat outFmt;
        ParsePosition pos = new ParsePosition(0);
        date = date.replace("-", "").replaceAll(" ", "").replace(":", "");
        if ("".equals(date.trim())) {
            return "";
        }
        try {
            if (Long.parseLong(date) == 0L) {
                return "";
            }
        } catch (Exception nume) {
            return date;
        }
        try {
            switch (date.trim().length()) {
                case 14:
                    inFmt = new SimpleDateFormat("yyyyMMddHHmmss");
                    break;
                case 12:
                    inFmt = new SimpleDateFormat("yyyyMMddHHmm");
                    break;
                case 10:
                    inFmt = new SimpleDateFormat("yyyyMMddHH");
                    break;
                case 8:
                    inFmt = new SimpleDateFormat("yyyyMMdd");
                    break;
                case 6:
                    inFmt = new SimpleDateFormat("yyyyMM");
                    break;
                case 7:
                case 9:
                case 11:
                case 13:
                default:
                    return date;
            }
            if ((dt = inFmt.parse(date, pos)) == null) {
                return date;
            }
            if ((format == null) || ("".equals(format.trim()))) {
                outFmt = new SimpleDateFormat("yyyy年MM月dd日");
            } else {
                outFmt = new SimpleDateFormat(format);
            }
            return outFmt.format(dt);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return date;
    }


    /**
     * 根据传入的值判断类型后转换返回
     *
     * @param cell 单元格
     * @return 返回对应类型的值
     * @author Mr.Yang
     * @date 2018/12/2
     */
    @SuppressWarnings("static-access")
    private static String getValue(Cell cell) {
        if (cell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == CellType.NUMERIC) {
            //获取数据类型
            short format = cell.getCellStyle().getDataFormat();
            double value = cell.getNumericCellValue();
            if (format == 14 || format == 31 || format == 57 || format == 58) {
                //日期
                return new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.getJavaDate(value));
            } else if (format == 20 || format == 32) {
                //时间
                return new SimpleDateFormat("HH:mm").format(DateUtil.getJavaDate(value));
            } else if (format == 22 || format == 177) {
                //时间
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(DateUtil.getJavaDate(value));
            }
            return NumberToTextConverter.toText(cell.getNumericCellValue());
        } else {
            return String.valueOf(cell.getStringCellValue());
        }
    }

    /**
     * 获取object对象所有属性的Setter方法，并构建map对象，结构为Map<'field','method'>
     *
     * @param object object对象
     * @return 返回map
     * @author Mr.Yang
     * @date 2018/12/1
     */
    @SuppressWarnings("rawtypes")
    private static Map<String, Method> getObjectSetterMethod(Class object) {
        //获取object对象的所有属性
        Field[] fields = object.getDeclaredFields();
        //获取object对象的所有方法
        Method[] methods = object.getDeclaredMethods();
        Map<String, Method> methodMap = new HashMap<>(12);
        for (Field field : fields) {
            String attri = field.getName();
            for (Method method : methods) {
                String meth = method.getName();
                //匹配set方法 
                if (meth != null && "set".equals(meth.substring(0, 3)) &&
                        Modifier.isPublic(method.getModifiers()) &&
                        ("set" + Character.toUpperCase(attri.charAt(0)) + attri.substring(1)).equals(meth)) {
                    //将匹配的setter方法加入map对象中
                    methodMap.put(attri.toLowerCase(), method);
                    break;
                }
            }
        }
        return methodMap;
    }

    /**
     * 获取object对象的所有属性，并构建map对象，对象结果为Map<'field','field'>
     *
     * @param object 对象
     * @return 返回map
     * @author Mr.Yang
     * @date 2018/12/1
     */
    @SuppressWarnings("rawtypes")
    private static Map<String, Field> getObjectField(Class object) {
        //获取object对象的所有属性
        Field[] fields = object.getDeclaredFields();
        Map<String, Field> fieldMap = new HashMap<>(12);
        for (Field field : fields) {
            String attri = field.getName();
            fieldMap.put(attri.toLowerCase(), field);
        }
        return fieldMap;
    }
}
