/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package tk.mybatis.mapper.provider;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.xmltags.*;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * SqlServerProvider实现类，特殊方法实现类
 *
 * @author liuzh
 */
public class SqlServerProvider extends MapperTemplate {

    public SqlServerProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 插入
     *
     * @param ms
     */
    public String insert(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        EntityTable table = EntityHelper.getEntityTable(entityClass);
        //开始拼sql
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ");
        sql.append(getDynamicTableName(entityClass));
        sql.append("(");
        boolean first = true;
        for (EntityColumn column : table.getEntityClassColumns()) {
            if (column.isId()) {
                continue;
            }
            if(!first) {
                sql.append(",");
            }
            sql.append(column.getColumn());
            first = false;
        }
        sql.append(") values(");
        first = true;
        for (EntityColumn column : table.getEntityClassColumns()) {
            if (column.isId()) {
                continue;
            }
            if(!first) {
                sql.append(",");
            }
            sql.append(column.getColumnHolder());
            first = false;
        }
        sql.append(")");
        return sql.toString();
    }

    /**
     * 插入不为null的字段
     *
     * @param ms
     * @return
     */
    public SqlNode insertSelective(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        List<SqlNode> sqlNodes = new ArrayList<SqlNode>();
        //insert into table
        sqlNodes.add(new StaticTextSqlNode("INSERT INTO "));
        sqlNodes.add(getDynamicTableNameNode(entityClass));

        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        List<SqlNode> ifNodes = new ArrayList<SqlNode>();
        //Identity列只能有一个
        Boolean hasIdentityKey = false;
        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (EntityColumn column : columnList) {
            //当使用序列时
            if (!column.isId()) {
                ifNodes.add(getIfNotNull(column, new StaticTextSqlNode(column.getColumn() + ",")));
            }
        }
        //将动态的列加入sqlNodes
        sqlNodes.add(new TrimSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes), "(", null, ")", ","));

        ifNodes = new ArrayList<SqlNode>();
        //处理values(#{property},#{property}...)
        for (EntityColumn column : columnList) {
            //当参数中的属性值不为空的时候,使用传入的值
            if (!column.isId()) {
                ifNodes.add(new IfSqlNode(new StaticTextSqlNode(column.getColumnHolder() +","), column.getProperty() + " != null "));
            }
        }
        //values(#{property},#{property}...)
        sqlNodes.add(new TrimSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes), "VALUES (", null, ")", ","));
        return new MixedSqlNode(sqlNodes);
    }
}
