package com.open.web.plugin;

import com.open.web.plugin.bean.PageBean;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Properties;

/**
 * @Description
 * @auther 程佳伟
 * @create 2020-01-14 16:31
 */
@Intercepts({
        //无论是query还是update（增删改）动作，都会执行statementhandler子类对象中的prepare
        //，所以我们在statementhandler中拦截可以满足大部分需求，当然具体的拦截对象以及拦截方法根据具体需求而定
        @Signature(type= StatementHandler.class,method="prepare",args={Connection.class,Integer.class})
})
public class PagingInterceptor implements Interceptor {
    /**
     *方法参数Invocation就是我们具体拦截到的对象（Executor、StatementHandler、ParameterHandler 和 ResultSetHandler）
     *Invocation 就是这个对象，Invocation 里面有三个参数 target method args
     *          target 就是 Executor
     *          method 就是 update
     *          args   就是 MappedStatement ms, Object parameter
     */
    public Object intercept(Invocation invocation) throws Exception{
        //很多需求其实是可以通过不同的拦截对象实现的，为了使得说明更加全面，以下分别通过
        //Executor和StatementHandler对象实现拦截进行分页处理*/
        Invocation retInvocation=null;
        //通过拦截Executor实现
        //retInvocation=ByExecutor(invocation);

        //通过拦截StatementHandler实现
        retInvocation=ByStatementHandler(invocation);

        return  retInvocation.proceed();
    }

    /*
     * 用当前这个拦截器生成对目标target的代理，实际是通过Plugin.wrap(target,this)
     *来完成的，把目标target和拦截器this传给了包装函数，并返回代理对象
     */
    public Object plugin(Object target) {
        //先判断一下目标类型，是本插件要拦截的对象才执行
        //Plugin.wrap方法，否则直接返回目标对象，这样可以减少目标被代理的次数。
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }

    }

    /*如何使用？
     * 只需要在 mybatis 配置文件中加入类似如下配置
     *      <plugin interceptor="com.intercept.MyInterceptor">
     *           <property name="username" value="fitz_fu"/>
     *           <property name="password" value="123456"/>
     *      </plugin>
     *方法中获取参数：properties.getProperty("username");
     *若mybatis与spring一起用，则可以通过@Value("${}")从application.properties文件获取
     */
    public void setProperties(Properties properties) {
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
    }

    private Invocation ByStatementHandler(Invocation invocation) throws Exception{
        StatementHandler statementHandler = (StatementHandler)invocation.getTarget();
        //因为StatementHandler中部分方法为protected，所以通过MetaObject优雅访问对象的属性
        //，这里是访问statementHandler的属性
        MetaObject metaObject = MetaObject.forObject(statementHandler
                , SystemMetaObject.DEFAULT_OBJECT_FACTORY
                , SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY
                , new DefaultReflectorFactory());

        if("prepare".equals(invocation.getMethod().getName())){

            BoundSql boundSql = statementHandler.getBoundSql();
            //mapper.xml中sql的参数是何种类型，这个地方就用什么类型来接收参数，比如下面的String或者是Map
            //String params = (String)boundSql.getParameterObject();
            Map<String,Object> params = (Map<String,Object>)boundSql.getParameterObject();
            //在SQL的参数中获取分页对象
            PageBean page = (PageBean)params.get("page");
            String initialSql = boundSql.getSql();//获取原始sql  
            String countSql = "select count(*) from ( "+initialSql+" ) a";

            Connection connection = (Connection) invocation.getArgs()[0];
            PreparedStatement countStatement = connection.prepareStatement(countSql);
            //当sql带有参数时，下面的这句话就是获取查询条件的参数 
            ParameterHandler parameterHandler = (ParameterHandler) metaObject.getValue("delegate.parameterHandler");
            //经过set方法，就可以正确的执行sql语句  
            parameterHandler.setParameters(countStatement);
            ResultSet rs = countStatement.executeQuery();
            //当结果集中有值时，表示页面数量大于等于1 
            if(rs.next()){
                //根据业务需要对分页对象进行设置
                page.setTotal(rs.getInt(1));
            }
            //构造并执行分页sql
            String pageSql = initialSql+" limit "+page.getPageNum()+","+page.getTotal(); //Mysql 
            /*String pageSql = "SELECT * FROM ( SELECT ROWNUM ROWNUMS,TMP.* FROM ("+initialSql
                    +" ) TMP ) WHERE ROWNUMS <= "+page.getTotal()+" AND ROWNUMS >"+page.getPageNum();*/  //Oracle
            metaObject.setValue("delegate.boundSql.sql", pageSql);
        }
        return  invocation;
    }
}
