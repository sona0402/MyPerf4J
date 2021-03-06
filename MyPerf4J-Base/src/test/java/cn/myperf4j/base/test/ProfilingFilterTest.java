package cn.myperf4j.base.test;

import cn.myperf4j.base.config.ProfilingFilter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by LinShunkang on 2018/10/28
 */
public class ProfilingFilterTest {

    @Before
    public void init() {
        ProfilingFilter.addIncludePackage("org.junit");
        ProfilingFilter.addExcludePackage("org.junit.rules");
        ProfilingFilter.addExcludeMethods("hello");
        ProfilingFilter.addExcludeClassLoader("org.apache.catalina.loader.WebappClassLoader");
        ProfilingFilter.addExcludeMethods("Demo.getId1(long)");
        ProfilingFilter.addExcludeMethods("Demo.getId1(long,int)");
        ProfilingFilter.addExcludeMethods("Demo.getId1()");
        ProfilingFilter.addExcludeMethods("Demo.getId1(ClassA$ClassB,long)");
    }

    @Test
    public void test() {
        Assert.assertFalse(ProfilingFilter.isNeedInject("org.junit.Before"));
        Assert.assertTrue(ProfilingFilter.isNeedInject("org/junit/Before"));
        Assert.assertTrue(ProfilingFilter.isNotNeedInject("org/junit/rules/ErrorCollector"));

        Assert.assertTrue(ProfilingFilter.isNotNeedInjectMethod("toString"));
        Assert.assertTrue(ProfilingFilter.isNotNeedInjectMethod("hello"));
        Assert.assertFalse(ProfilingFilter.isNotNeedInjectMethod("assertFalse"));

        Assert.assertFalse(ProfilingFilter.isNotNeedInjectMethod("Demo.getId1(ClassA$ClassB)"));
        Assert.assertTrue(ProfilingFilter.isNotNeedInjectMethod("Demo.getId1()"));
        Assert.assertTrue(ProfilingFilter.isNotNeedInjectMethod("Demo.getId1(ClassA$ClassB,long)"));
        Assert.assertTrue(ProfilingFilter.isNotNeedInjectMethod("Demo.getId1(long)"));
        Assert.assertTrue(ProfilingFilter.isNotNeedInjectMethod("Demo.getId1(long,int)"));


        Assert.assertTrue(ProfilingFilter.isNotNeedInjectClassLoader("org.apache.catalina.loader.WebappClassLoader"));
        Assert.assertFalse(ProfilingFilter.isNotNeedInjectClassLoader("org.springframework.boot.loader.LaunchedURLClassLoader"));
    }

    @Test
    public void testWildcardMatch() {
        Assert.assertFalse(ProfilingFilter.isNeedInject("cn/junit/test/a"));
        Assert.assertFalse(ProfilingFilter.isNeedInject("cn/junit/test2"));

        ProfilingFilter.addIncludePackage("cn.junit.test.*");
        Assert.assertTrue(ProfilingFilter.isNeedInject("cn/junit/test/a"));
        Assert.assertTrue(ProfilingFilter.isNeedInject("cn/junit/test/2"));



        Assert.assertFalse(ProfilingFilter.isNotNeedInject("com/junit/test/a"));
        Assert.assertFalse(ProfilingFilter.isNotNeedInject("com/junit/test2"));

        ProfilingFilter.addExcludePackage("com.junit.test.*");
        Assert.assertTrue(ProfilingFilter.isNotNeedInject("com/junit/test/a"));
        Assert.assertTrue(ProfilingFilter.isNotNeedInject("com/junit/test/2"));
    }

}
