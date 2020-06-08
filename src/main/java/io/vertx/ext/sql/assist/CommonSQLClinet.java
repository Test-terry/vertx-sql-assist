package io.vertx.ext.sql.assist;

import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * 通用的数据库操作客户端
 * 
 * @author <a href="http://szmirren.com">Mirren</a>
 *
 * @param <C>
 *          SQL执行器的客户端类型,比如JDBCClient
 */
public interface CommonSQLClinet<C> {
	/**
	 * 获取客户端
	 * 
	 * @return
	 */
	C getDbClient();

	/**
	 * 执行查询
	 * 
	 * @param qp
	 *          SQL语句与参数
	 * @return future
	 *          返回结果
	 */
	Future<JsonObject> queryAsObj(SqlAndParams qp);

	/**
	 * 执行查询
	 * 
	 * @param qp
	 *          SQL语句与参数
	 * @return future
	 *          返回结果
	 */
	Future<List<JsonObject>> queryAsListObj(SqlAndParams qp);

	/**
	 * 执行查询
	 * 
	 * @param qp
	 *          SQL语句与参数
	 * @return future
	 *          返回结果
	 */
	Future<List<JsonArray>> queryAsListArray(SqlAndParams qp);

	/**
	 * 执行更新等操作得到受影响的行数
	 * 
	 * @param qp
	 *          SQL语句与参数
	 *
	 * @return future
	 */
	Future<Integer> update(SqlAndParams qp);

	/**
	 * 批量操作
	 * 
	 * @param qp
	 *          SQL语句与批量参数
	 *
	 * @return future
	 *          返回结果
	 */
	Future<List<Integer>> batch(SqlAndParams qp);

	/**
	 * 获得数据总行数
	 *
	 * @return future
	 *          返回数据总行数
	 */
	Future<Long> getCount();

	/**
	 * 获取数据总行数
	 * 
	 * @param assist
	 *          查询工具,如果没有可以为null
	 * @return future
	 *          返回数据总行数
	 */
	Future<Long> getCount(SqlAssist assist);

	/**
	 * 查询所有数据
	 *
	 * @return future
	 *          结果集
	 */
	Future<List<JsonObject>> selectAll();

	/**
	 * 通过查询工具查询所有数据
	 * 
	 * @param assist
	 *          查询工具帮助类
	 * @return future
	 *          结果集
	 */
	Future<List<JsonObject>> selectAll(SqlAssist assist);

	/**
	 * 分页查询,默认page=1,rowSize=15(取第一页,每页取15行数据)
	 * 
	 * @param assist
	 *          查询工具(注意:startRow在该方法中无效,最后会有page转换为startRow)
	 * @return future
	 *          返回结果为(JsonObject)格式为: {@link SqlLimitResult#toJson()}
	 */
	Future<JsonObject> limitAll(final SqlAssist assist);

	/**
	 * 通过ID查询出数据
	 * 
	 * @param primaryValue
	 *          主键值
	 * @return future
	 *          返回结果:如果查询得到返回JsonObject如果查询不到返回null
	 */
	<S> Future<JsonObject> selectById(S primaryValue);

	/**
	 * 通过ID查询出数据
	 * 
	 * @param primaryValue
	 *          主键值
	 * @param resultColumns
	 *          自定义返回列
	 * @return future
	 *          返回结果:如果查询得到返回JsonObject如果查询不到返回null
	 */
	<S> Future<JsonObject> selectById(S primaryValue, String resultColumns);

	/**
	 * 通过ID查询出数据,并自定义返回列
	 * 
	 * @param primaryValue
	 *          主键值
	 * @param resultColumns
	 *          自定义返回列
	 * @param joinOrReference
	 *          多表查询或表连接的语句,示例 as t inner join table2 as t2 on t.id=t2.id
	 * @return future
	 *          返回结果:如果查询得到返回JsonObject如果查询不到返回null
	 */
	<S> Future<JsonObject> selectById(S primaryValue, String resultColumns, String joinOrReference);

	/**
	 * 将对象属性不为null的属性作为条件查询出数据,只取查询出来的第一条数据;
	 * 
	 * @param obj
	 *          对象
	 *
	 * @return future
	 *          结果:如果存在返回JsonObject,不存在返回null
	 */
	<T> Future<JsonObject> selectSingleByObj(T obj);

	/**
	 * 将对象属性不为null的属性作为条件查询出数据,只取查询出来的第一条数据
	 * 
	 * @param obj
	 *          对象
	 * @param resultColumns
	 *          自定义返回列
	 *
	 * @return future
	 *          结果:如果存在返回JsonObject,不存在返回null
	 */
	<T> Future<JsonObject> selectSingleByObj(T obj, String resultColumns);

	/**
	 * 将对象属性不为null的属性作为条件查询出数据,只取查询出来的第一条数据
	 * 
	 * @param obj
	 *          对象
	 * @param resultColumns
	 *          自定义返回列
	 * @param joinOrReference
	 *          多表查询或表连接的语句,示例 as t inner join table2 as t2 on t.id=t2.id
	 * @return future
	 *          结果:如果存在返回JsonObject,不存在返回null
	 */
	<T> Future<JsonObject> selectSingleByObj(T obj, String resultColumns, String joinOrReference);

	/**
	 * 将对象属性不为null的属性作为条件查询出数据
	 * 
	 * @param obj
	 *          对象
	 * 
	 * @return future
	 *          返回结果集
	 */
	<T> Future<List<JsonObject>> selectByObj(T obj);

	/**
	 * 将对象属性不为null的属性作为条件查询出数据
	 * 
	 * @param obj
	 *          对象
	 * @param resultColumns
	 *          自定义返回列
	 * 
	 * @return future
	 *          返回结果集
	 */
	<T> Future<List<JsonObject>> selectByObj(T obj, String resultColumns);

	/**
	 * 将对象属性不为null的属性作为条件查询出数据
	 * 
	 * @param obj
	 *          对象
	 * @param resultColumns
	 *          自定义返回列
	 * @param joinOrReference
	 *          多表查询或表连接的语句,示例 as t inner join table2 as t2 on t.id=t2.id
	 * @return future
	 *          返回结果集
	 */
	<T> Future<List<JsonObject>> selectByObj(T obj, String resultColumns, String joinOrReference);

	/**
	 * 插入一个对象包括属性值为null的值
	 * 
	 * @param obj
	 *          对象
	 * @return future
	 *          返回操作结果
	 */
	<T> Future<Integer> insertAll(T obj);


	<T> Future<Integer> upsertAll(T obj);

	/**
	 * 插入一个对象包括属性值为null的值
	 *
	 * @param obj
	 *          对象
	 * @return future
	 *          返回操作结果
	 */
	<T> Future<JsonArray> insertAllReturnId(T obj);

	<T> Future<JsonArray> upsertAllReturnId(T obj);

	/**
	 * 插入一个对象,只插入对象中值不为null的属性
	 * 
	 * @param obj
	 *          对象
	 * @return future
	 *          返回操作结果
	 */
	<T> Future<Integer> insertNonEmpty(T obj);


	<T> Future<Integer> upsertNonEmpty(T obj);

	/**
	 * 插入一个对象,只插入对象中值不为null的属性
	 *
	 * @param obj
	 *          对象
	 * @return future
	 *          返回操作结果
	 */
	<T> Future<JsonArray> insertNonEmptyReturnId(T obj);

	<T> Future<JsonArray> upsertNonEmptyReturnId(T obj);

	/**
	 * 批量添加全部所有字段
	 * 
	 * @param list
	 *          对象
	 * @return future
	 *          成功返回受影响的行数,如果对象为null或空则返回0
	 */
	<T> Future<Long> insertBatch(List<T> list);

	/**
	 * 批量添加自定字段
	 * 
	 * @param columns
	 *          字段的名称示例:["id","name",...]
	 * @param params
	 *          字段对应的参数示例:[["id","name"],["id","name"]...]
	 * @return future
	 *          成功返回受影响的行数,如果字段或字段参数为null或空则返回0
	 */
	Future<Long> insertBatch(List<String> columns, List<JsonArray> params);

	/**
	 * 插入一个对象,如果该对象不存在就新建如果该对象已经存在就更新
	 * 
	 * @param obj
	 *          对象
	 * 
	 * @return future
	 *          结果集受影响的行数
	 */
	<T> Future<Integer> replace(T obj);

	/**
	 * 更新一个对象中所有的属性包括null值,条件为对象中的主键值
	 * 
	 * @param obj
	 *          对象
	 * 
	 * @return future
	 *          返回操作结果
	 */
	<T> Future<Integer> updateAllById(T obj);

	/**
	 * 更新一个对象中所有的属性包括null值,条件为SqlAssist条件集<br>
	 * 
	 * @param obj
	 *          对象
	 * @param assist
	 *          sql帮助工具
	 * 
	 * @return future
	 *          返回操作结果
	 */
	<T> Future<Integer> updateAllByAssist(T obj, SqlAssist assist);

	/**
	 * 更新一个对象中属性不为null值,条件为对象中的主键值
	 * 
	 * @param obj
	 *          对象
	 * @return future
	 *          返回操作结果
	 */
	<T> Future<Integer> updateNonEmptyById(T obj);

	/**
	 * 更新一个对象中属性不为null值,条件为SqlAssist条件集
	 * 
	 * @param obj
	 *          对象
	 * @param assist
	 *          查询工具
	 * @return future
	 *          返回操作结果
	 */
	<T> Future<Integer> updateNonEmptyByAssist(T obj, SqlAssist assist);

	/**
	 * 通过主键值设置指定的列为空
	 * 
	 * @param <S>
	 * @param primaryValue
	 *          主键
	 * @param columns
	 *          要设置为null的列
	 * 
	 * @return future
	 *          返回操作结果
	 */
	<S> Future<Integer> updateSetNullById(S primaryValue, List<String> columns);

	/**
	 * 通过Assist作为条件设置指定的列为空
	 * 
	 * @param assist
	 *          sql帮助工具
	 * @param columns
	 *          要设置为null的列
	 * 
	 * @return future
	 *          返回操作结果
	 */
	<T> Future<Integer> updateSetNullByAssist(SqlAssist assist, List<String> columns);

	/**
	 * 通过主键值删除对应的数据行
	 * 
	 * @param primaryValue
	 *          主键值
	 * @return future
	 *          返回操作结果
	 */
	<S> Future<Integer> deleteById(S primaryValue);

	/**
	 * 通过SqlAssist条件集删除对应的数据行
	 * 
	 * @param assist
	 *          条件集
	 * 
	 * @return future
	 *          返回操作结果
	 */
	Future<Integer> deleteByAssist(SqlAssist assist);

}
