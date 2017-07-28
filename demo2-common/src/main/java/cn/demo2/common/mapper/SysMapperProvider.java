package cn.demo2.common.mapper;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.DELETE_FROM;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Table;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.StaticTextSqlNode;

import com.github.abel533.mapper.MapperProvider;
import com.github.abel533.mapperhelper.EntityHelper;
import com.github.abel533.mapperhelper.MapperHelper;

public class SysMapperProvider extends MapperProvider {

    public SysMapperProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public SqlNode deleteByIDS(MappedStatement ms) {
        Class<?> entityClass = getSelectReturnType(ms);
        Set<EntityHelper.EntityColumn> entityColumns = EntityHelper.getPKColumns(entityClass);
        EntityHelper.EntityColumn column = null;
        for (EntityHelper.EntityColumn entityColumn : entityColumns) {
            column = entityColumn;
            break;
        }
        
        List<SqlNode> sqlNodes = new ArrayList<SqlNode>();
        // 开始拼sql
        BEGIN();
        // delete from table
        DELETE_FROM(tableName(entityClass));
        // 得到sql
        String sql = SQL();
        // 静态SQL部分
        sqlNodes.add(new StaticTextSqlNode(sql + " WHERE " + column.getColumn() + " IN "));
        // 构造foreach sql
        SqlNode foreach = new ForEachSqlNode(ms.getConfiguration(), new StaticTextSqlNode("#{"
                + column.getProperty() + "}"), "ids", "index", column.getProperty(), "(", ")", ",");
        sqlNodes.add(foreach);
        return new MixedSqlNode(sqlNodes);
    }
    
    /**
     * 方法名称与接口方法名称一一对应
     * 1.Mybatis 最终将通用mapper解析为sql语句 交个mysql执行
     * 2.sqlNode 将接口方法最后转化为sql语句
     * 3.获取ItemMapper
     * @return
     * @throws ClassNotFoundException 
     */
    public SqlNode findCount(MappedStatement ms) throws ClassNotFoundException{
    	 //1.代表具体的方法路径
    	String path = ms.getId();        
    	
    	//2.获取ItemMapper的路径
    	String classpath = path.substring(0, path.lastIndexOf("."));
    	
    	//3.获取ItemMapper class 类型 反射
    	Class<?> targetClass = Class.forName(classpath);
    	
    	//4.获取上级 java.lang.reflect.Type,  Type是java中的超级接口
    	Type[] types = targetClass.getGenericInterfaces();
    	
    	//4.获取继承的直接类型
    	Type type = types[0];
    	
    	//5.获取泛型的类型 先判断是否为泛型
    	if(type instanceof ParameterizedType){
    		//转化为泛型
    		ParameterizedType parameterizedType = (ParameterizedType)type;
    		
    		//6.获取泛型的参数
    		Type[] argsTypes = parameterizedType.getActualTypeArguments();
    		
    		//
    		Class<?> argClass = (Class<?>) argsTypes[0];
    		
    		//7.获取类型上的注解
    		Table table = argClass.getAnnotation(Table.class);
    		
    		//8.获取注解中的name参数
    		String tableName = table.name();
    		
    		//编辑sql语句 from 后要一个空格
    		String sql = "select count(*) from "+ tableName;
    		
    		//新建SqlNode对象
    		SqlNode sqlNode = new StaticTextSqlNode(sql);
    		
    		return sqlNode;    		   		
    	}   	   	   	
    	return null;
    }
    
    
    
    
    
    
    

}
