package io.vertx.ext.sql.assist.core;

import java.lang.reflect.Constructor;
import java.util.List;

import io.vertx.ext.sql.assist.anno.Table;
import io.vertx.ext.sql.assist.anno.TableColumn;
import io.vertx.ext.sql.assist.anno.TableId;
import io.vertx.ext.sql.assist.sql.MySQLStatementSQL;

/**
 * SQL执行语句
 *
 * @author <a href="http://szmirren.com">Mirren</a>
 *
 */
public interface SQLStatement {
	/** SQLStatement的使用哪个类的key */
	final static String PROVIDER_CLASS_KEY = "VQA_SQL_STATEMENT_PROVIDER_CLASS_NAME";

	/**
	 * 设置SQL语句使用哪个类实现
	 *
	 * @param statementImplClass
	 */
	static void register(Class<?> statementImplClass) {
		System.setProperty(PROVIDER_CLASS_KEY, statementImplClass.getName());
	}

	/**
	 * 获取SQL语句使用哪个类实现
	 */
	static String getStatementClassName() {
		return System.getProperty(PROVIDER_CLASS_KEY);
	}

	/**
	 * 设置SQL语句使用哪个类实现
	 *
	 * @param className
	 */
	static void register(String className) {
		System.setProperty(PROVIDER_CLASS_KEY, className);
	}
	/**
	 * 创建实例,默认使用MySQL规范实现,如果使用其他数据库可以使用{@link #register(Class) }注册或者使用{@link #create(Class, Class)}方法
	 *
	 * @param entityClass
	 *          实体类,类必须包含{@link Table} {@link TableId} {@link TableColumn}注解
	 * @return
	 */
	static SQLStatement create(Class<?> entityClass) {
		String className = System.getProperty(PROVIDER_CLASS_KEY);
		if (className == null) {
			return new MySQLStatementSQL(entityClass);
		}
		try {
			Class<?> forClass = Class.forName(className);
			Constructor<?> constructor = forClass.getConstructor(Class.class);
			return (SQLStatement) constructor.newInstance(entityClass);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 创建实例
	 *
	 * @param entityClass
	 *          实体类,类必须包含{@link Table} {@link TableId} {@link TableColumn}注解
	 * @param statementClass
	 *          实现了{@link SQLStatement}的类
	 * @return
	 */
	static SQLStatement create(Class<?> entityClass, Class<?> statementClass) {
		try {
			Constructor<?> constructor = statementClass.getConstructor(Class.class);
			return (SQLStatement) constructor.newInstance(entityClass);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获得数据总行数SQL语句<br>
	 *
	 * @param assist
	 * @return 返回:sql or sql与params
	 */
	SqlAndParams getCountSQL(SqlAssist assist);



	/**
	 * 获得数据总行数SQL语句<br>
	 *
	 * @param assist
	 * @return 返回:sql or sql与params
	 */
	SqlAndParams getExistSQL(SqlAssist assist);

	/**
	 * 获得查询全部数据SQL语句与参数<br>
	 *
	 * @param assist
	 *          查询工具
	 * @return 返回:sql or sql与params
	 */
	SqlAndParams selectAllSQL(SqlAssist assist);

	/**
	 * 通过主键查询一个对象<br>
	 * 返回:sql or sql与params
	 *
	 * @param <S>
	 * @param primaryValue
	 *          主键的值
	 * @param resultColumns
	 *          指定返回列 格式 [table.]列名 [as 类的属性名字],...
	 * @param joinOrReference
	 *          多表查询或表连接的语句,示例 as t inner join table2 as t2 on t.id=t2.id
	 * @return
	 */
	<S> SqlAndParams selectByIdSQL(S primaryValue, String resultColumns, String joinOrReference);
	/**
	 * 将对象属性不为null的属性作为条件查询出数据
	 *
	 * @param obj
	 *          对象
	 * @param resultColumns
	 *          自定义返回列
	 * @param joinOrReference
	 *          多表查询或表连接的语句,示例 as t inner join table2 as t2 on t.id=t2.id
	 * @param single
	 *          是否支取一条数据true支取一条,false取全部
	 * @return 返回sql 或 sql与params
	 *
	 */
	<T> SqlAndParams selectByObjSQL(T obj, String resultColumns, String joinOrReference, boolean single);


	/**
	 * 插入一个对象包括属性值为null的值<br>
	 *
	 * @param obj
	 * @return 返回:sql 或者 sql与params
	 */
	<T> SqlAndParams insertAllSQL(T obj);

	/**
	 * 插入一个对象包括属性值为null的值<br>
	 *
	 * @param obj
	 * @param dupCol
     * @return 返回:sql 或者 sql与params
	 */
	<T> SqlAndParams upsertAllSQL(T obj, String dupCol);


	/**
	 * 插入一个对象,只插入对象中值不为null的属性<br>
	 *
	 * @param obj
	 *          对象
	 * @return 返回:sql 或 sql与params
	 */
	<T> SqlAndParams insertNonEmptySQL(T obj);


	/**
	 * 插入一个对象,只插入对象中值不为null的属性<br>
	 *
	 * @param obj
	 *          对象
	 * @param dupCol
	 * @return 返回:sql 或 sql与params
	 */
	<T> SqlAndParams upsertNonEmptySQL(T obj, String dupCol);


	/**
	 * 插入一个对象,只插入对象中值不为null的属性<br>
	 *
	 * @param obj
	 *          对象
	 * @return 返回:sql 或 sql与params
	 */
	<T> SqlAndParams insertNonEmptySQLReturnId(T obj);

	/**
	 * 更新一个对象中所有的属性包括null值,条件为对象中的主键值
	 *
	 * @param obj
	 * @return 返回:sql or sql与params, 如果对象中的id为null将会返回SQL:"there is no primary key
	 *         in your SQL statement"
	 */
	<T> SqlAndParams updateAllByIdSQL(T obj);
	/**
	 * 更新一个对象中所有的属性包括null值,条件为SqlAssist条件集<br>
	 *
	 * @param obj
	 * @param assist
	 * @return 返回:sql or
	 *         sql与params如果SqlAssist对象或者对象的Condition为null将会返回SQL:"SqlAssist or
	 *         SqlAssist.condition is null"
	 */
	<T> SqlAndParams updateAllByAssistSQL(T obj, SqlAssist assist);
	/**
	 * 更新一个对象中属性不为null值,条件为对象中的主键值
	 *
	 * @param obj
	 *          对象
	 * @return 返回:sql or sql与params , 如果id为null或者没有要更新的数据将返回SQL:"there is no
	 *         primary key in your SQL statement"
	 */
	<T> SqlAndParams updateNonEmptyByIdSQL(T obj);

	/**
	 * 将对象中属性值不为null的进行更新,条件为SqlAssist条件集
	 *
	 * @param obj
	 *          对象
	 * @param assist
	 *          查询工具
	 * @return 返回:sql or sql与params , 如果assist为null将会返回sql:"SqlAssist or
	 *         SqlAssist.condition is null"
	 */
	<T> SqlAndParams updateNonEmptyByAssistSQL(T obj, SqlAssist assist);
	/**
	 * 通过id将指定列设置为null
	 *
	 * @param <S>
	 * @param primaryValue
	 *          id
	 * @param columns
	 *          要设置为空的列
	 * @return
	 */
	<S> SqlAndParams updateSetNullByIdSQL(S primaryValue, List<String> columns);
	/**
	 *
	 * 将指定的列设置为空,条件为SqlAssist条件集
	 *
	 * @param assist
	 *          查询工具
	 * @param columns
	 *          要设置为空的列
	 * @return 返回:sql or sql与params , 如果assist为null将会返回sql:"SqlAssist or
	 *         SqlAssist.condition is null"
	 * @return
	 */
	<S> SqlAndParams updateSetNullByAssistSQL(SqlAssist assist, List<String> columns);
	/**
	 * 通过主键值删除对应的数据行
	 *
	 * @param primaryValue
	 *          id值
	 * @return 返回:sql or sql与params , 如果id为null或者没有要更新的数据将返回SQL:"there is no
	 *         primary key in your SQL statement"
	 */
	<S> SqlAndParams deleteByIdSQL(S primaryValue);
	/**
	 * 通过SqlAssist条件集删除对应的数据行
	 *
	 * @param assist
	 *          查询工具
	 * @return 返回:sql or sql与params , 如果assist为null将会返回sql: "SqlAssist or
	 *         SqlAssist.condition is null"
	 */
	SqlAndParams deleteByAssistSQL(SqlAssist assist);
}
