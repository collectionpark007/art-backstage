package com.art.mapper;

import com.art.support.annotation.MyColumn;
import com.art.support.annotation.MyId;
import com.art.support.annotation.MyTable;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 基本的sql生成类
 * <p>
 *
 * @Author: never
 * @Date: 2019/04/22 10:32
 */
public class BaseSqlProvider {

    /**
     * 插入实体的SQL
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public String insert(Object obj) throws Exception {
        SQL sql = new SQL();
        Class<?> clazz = obj.getClass();
        MyTable table = clazz.getAnnotation(MyTable.class);
        sql.INSERT_INTO(table.value());
        List<Field> list = getAllField(obj);

        for (Field field : list) {
            field.setAccessible(true);
            Object colValue = field.get(obj);
            MyColumn column = field.getAnnotation(MyColumn.class);
            if (column != null && colValue == null) {
                if (column.value().equalsIgnoreCase("create_time") ||
                        column.value().equalsIgnoreCase("update_time")) {
                    field.setAccessible(true);
                    field.set(obj, new Date());
                } else {
                    continue;
                }
            }
            String colName = "";
            if (column != null) {
                colName = StringUtils.isBlank(column.value()) ? field.getName() : column.value();
            }
            sql.VALUES(String.format("`%s`", colName), String.format("#{%s}", field.getName()));
        }
        return sql.toString();
    }

    /**
     * 更新实体的SQL
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public String update(Object obj) throws Exception {
        SQL sql = new SQL();
        MyTable table = obj.getClass().getAnnotation(MyTable.class);
        sql.UPDATE(table.value());
        List<Field> list = getAllField(obj);
        Field idField = null;
        for (Field field : list) {
            field.setAccessible(true);
            Object colValue = field.get(obj);
            MyColumn column = field.getAnnotation(MyColumn.class);
            MyId id = field.getAnnotation(MyId.class);
            if (column != null && colValue == null)
                if (column.value().equalsIgnoreCase("update_time")) {
                    field.setAccessible(true);
                    field.set(obj, new Date());
                } else {
                    continue;
                }
            if (id != null) {
                idField = field;
                continue;
            }
            String colName = StringUtils.isBlank(column.value()) ? field.getName() : column.value();
            sql.SET(String.format("%s = #{%s}", colName, field.getName()));
        }
        if (idField != null) {
            MyId id = idField.getAnnotation(MyId.class);
            long idColName = id != null ? Long.parseLong(idField.getName()) : id.value();
            sql.WHERE(String.format("`%s` = #{%s}", idColName, idField.getName()));
        }
        return sql.toString();
    }

    /**
     * 通过ID获取实体的sql
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public String getById(Object obj) throws Exception {
        SQL sql = new SQL();
        MyTable table = obj.getClass().getAnnotation(MyTable.class);
        sql.SELECT(getAllFieldString(obj));
        sql.FROM(table.value());
        Field idField = getIdField(obj);
        idField.setAccessible(true);
        MyId id = idField.getAnnotation(MyId.class);
        long idColName = id != null ? Long.parseLong(idField.getName()) : id.value();
        sql.WHERE(String.format("`%s` = #{%s}", idColName, idField.getName()));
        return sql.toString();
    }

    /**
     * 根据ID删除的sql
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public String deleteById(Object obj) throws Exception {
        SQL sql = new SQL();
        MyTable table = obj.getClass().getAnnotation(MyTable.class);
        sql.DELETE_FROM(table.value());
        Field idField = getIdField(obj);
        idField.setAccessible(true);
        MyId id = idField.getAnnotation(MyId.class);
        long idColName = id != null ? Long.parseLong(idField.getName()) : id.value();
        sql.WHERE(String.format("`%s` = #{%s}", idColName, idField.getName()));
        return sql.toString();
    }

    /**
     * 模糊查询的SQL
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public String seleteVague(Object obj) throws Exception {
        SQL sql = new SQL();
        Class<?> clazz = obj.getClass();
        MyTable table = clazz.getAnnotation(MyTable.class);
        sql.SELECT(getAllFieldString(obj));
        sql.FROM(table.value());
        List<Field> list = getAllField(obj);
        for (Field field : list) {
            field.setAccessible(true);
            Object colValue = field.get(obj);
            MyColumn column = field.getAnnotation(MyColumn.class);
            if (column != null && colValue != null) {
                if ((colValue instanceof String) && StringUtils.isBlank(String.valueOf(colValue)))
                    continue;
                String colName = StringUtils.isBlank(column.value()) ? field.getName() : column.value();
                sql.WHERE(String.format("`%s` like %s", colName, "'%" + String.valueOf(colValue) + "%'"));
            }
        }
        return sql.toString();
    }

    /**
     * 精确查询的sql生成
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public String seleteAccuracy(Object obj) throws Exception {
        SQL sql = new SQL();
        Class<?> clazz = obj.getClass();
        MyTable table = clazz.getAnnotation(MyTable.class);
        sql.SELECT(getAllFieldString(obj));
        sql.FROM(table.value());
        List<Field> list = getAllField(obj);
        for (Field field : list) {
            field.setAccessible(true);
            Object colValue = field.get(obj);
            MyColumn column = field.getAnnotation(MyColumn.class);
            if (column != null && colValue != null) {
                if ((colValue instanceof String) && StringUtils.isBlank(String.valueOf(colValue)))
                    continue;
                String colName = StringUtils.isBlank(column.value()) ? field.getName() : column.value();
                sql.WHERE(String.format("`%s` = #{%s}", colName, field.getName()));
            }
        }
        return sql.toString();
    }

    /**
     * 查询所有的sql
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public String findAll(Object obj) throws Exception {
        SQL sql = new SQL();
        Class<?> clazz = obj.getClass();
        MyTable table = clazz.getAnnotation(MyTable.class);
        sql.SELECT(getAllFieldString(obj));
        sql.FROM(table.value());
        return sql.toString();
    }

    /**
     * 删除所有的SQL
     *
     * @param obj
     * @return
     */
    public String deleteAll(Object obj) throws Exception {
        SQL sql = new SQL();
        Class<?> clazz = obj.getClass();
        MyTable table = clazz.getAnnotation(MyTable.class);
        sql.DELETE_FROM(table.value());
        return sql.toString();
    }

    /**
     * 模糊匹配删除的SQL
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public String deleteVague(Object obj) throws Exception {
        SQL sql = new SQL();
        Class<?> clazz = obj.getClass();
        MyTable table = clazz.getAnnotation(MyTable.class);
        sql.DELETE_FROM(table.value());
        List<Field> list = getAllField(obj);
        for (Field field : list) {
            field.setAccessible(true);
            Object colValue = field.get(obj);
            MyColumn column = field.getAnnotation(MyColumn.class);
            if (column != null && colValue != null) {
                if ((colValue instanceof String) && StringUtils.isBlank(String.valueOf(colValue)))
                    continue;
                String colName = StringUtils.isBlank(column.value()) ? field.getName() : column.value();
                sql.WHERE(String.format("`%s` like %s", colName, "'%" + String.valueOf(colValue) + "%'"));
            }
        }
        return sql.toString();
    }

    /**
     * 精确匹配删除的SQL
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public String deleteAccuracy(Object obj) throws Exception {
        SQL sql = new SQL();
        Class<?> clazz = obj.getClass();
        MyTable table = clazz.getAnnotation(MyTable.class);
        sql.DELETE_FROM(table.value());
        List<Field> list = getAllField(obj);
        for (Field field : list) {
            field.setAccessible(true);
            Object colValue = field.get(obj);
            MyColumn column = field.getAnnotation(MyColumn.class);
            if (column != null && colValue != null) {
                if ((colValue instanceof String) && StringUtils.isBlank(String.valueOf(colValue)))
                    continue;
                String colName = StringUtils.isBlank(column.value()) ? field.getName() : column.value();
                sql.WHERE(String.format("`%s` = #{%s}", colName, field.getName()));
            }
        }
        return sql.toString();
    }

    /**
     * 统计总数的SQL
     *
     * @param obj
     * @return
     */
    public String countAll(Object obj) {
        SQL sql = new SQL();
        MyTable table = obj.getClass().getAnnotation(MyTable.class);
        Field idField = getIdField(obj);
        idField.setAccessible(true);
        MyId id = idField.getAnnotation(MyId.class);
        long idColName = id != null ? Long.parseLong(idField.getName()) : id.value();
        sql.SELECT("count(`" + idColName + "`)");
        sql.FROM(table.value());
        return sql.toString();
    }

    /**
     * 模糊匹配统计总数
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public String countVague(Object obj) throws Exception {
        SQL sql = new SQL();
        MyTable table = obj.getClass().getAnnotation(MyTable.class);
        Field idField = getIdField(obj);
        idField.setAccessible(true);
        MyId id = idField.getAnnotation(MyId.class);
        long idColName = id != null ? Long.parseLong(idField.getName()) : id.value();
        sql.SELECT("count(`" + idColName + "`)");
        sql.FROM(table.value());
        List<Field> list = getAllField(obj);
        for (Field field : list) {
            field.setAccessible(true);
            Object colValue = field.get(obj);
            MyColumn column = field.getAnnotation(MyColumn.class);
            if (column != null && colValue != null) {
                if ((colValue instanceof String) && StringUtils.isBlank(String.valueOf(colValue)))
                    continue;
                String colName = StringUtils.isBlank(column.value()) ? field.getName() : column.value();
                sql.WHERE(String.format("`%s` like %s", colName, "'%" + String.valueOf(colValue) + "%'"));
            }
        }
        return sql.toString();
    }

    /**
     * 精确查询统计总数
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public String countAccuracy(Object obj) throws Exception {
        SQL sql = new SQL();
        MyTable table = obj.getClass().getAnnotation(MyTable.class);
        Field idField = getIdField(obj);
        idField.setAccessible(true);
        MyId id = idField.getAnnotation(MyId.class);
        long idColName = id != null ? Long.parseLong(idField.getName()) : id.value();
        sql.SELECT("count(`" + idColName + "`)");
        sql.FROM(table.value());
        List<Field> list = getAllField(obj);
        for (Field field : list) {
            field.setAccessible(true);
            Object colValue = field.get(obj);
            MyColumn column = field.getAnnotation(MyColumn.class);
            if (column != null && colValue != null) {
                if ((colValue instanceof String) && StringUtils.isBlank(String.valueOf(colValue)))
                    continue;
                String colName = StringUtils.isBlank(column.value()) ? field.getName() : column.value();
                sql.WHERE(String.format("`%s` = #{%s}", colName, field.getName()));
            }
        }
        return sql.toString();
    }

    /**
     * 获取所有的带有@MyId和@MyColumn的列
     *
     * @param obj
     * @return
     */
    private List<Field> getAllField(Object obj) {
        Class<?> clazz = obj.getClass();
        List<Field> list = new ArrayList<>();
        while (true) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                MyColumn column = field.getAnnotation(MyColumn.class);
                MyId id = field.getAnnotation(MyId.class);
                if (column != null || id != null)
                    list.add(field);
            }
            if (clazz.getSuperclass() == Object.class)
                break;
            clazz = clazz.getSuperclass();
        }
        return list;
    }

    /**
     * 获取所有的带有@MyId和@MyColumn的列的，inser，select字符串
     *
     * @param obj
     * @return
     */
    private String getAllFieldString(Object obj) {
        StringBuilder sb = new StringBuilder(300);
        Class<?> clazz = obj.getClass();
        while (true) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                MyColumn column = field.getAnnotation(MyColumn.class);
                MyId id = field.getAnnotation(MyId.class);
                if (column != null)
                    sb.append("`").append(StringUtils.isBlank(column.value()) ? field.getName() : column.value()).append("`");
                else if (id != null)
                    sb.append("`").append(id != null ? field.getName() : id.value()).append("`");
                if (column != null || id != null)
                    sb.append(" as ").append(field.getName()).append(",");
            }
            if (clazz.getSuperclass() == Object.class)
                break;
            clazz = clazz.getSuperclass();
        }
        sb.setCharAt(sb.length() - 1, ' ');
        return sb.toString();
    }

    /**
     * 获取带有@MyId的field
     *
     * @param obj
     * @return
     */
    private Field getIdField(Object obj) {
        Class<?> clazz = obj.getClass();
        Field idField = null;
        while (true) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                MyId id = field.getAnnotation(MyId.class);
                if (id != null) {
                    idField = field;
                    break;
                }
            }
            if (idField != null || clazz.getSuperclass() == Object.class)
                break;
            clazz = clazz.getSuperclass();
        }
        return idField;
    }
}
