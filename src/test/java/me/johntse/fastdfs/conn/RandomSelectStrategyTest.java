package me.johntse.fastdfs.conn;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * 随机分配策略测试用例.
 *
 * @author johntse
 * @since 0.1.0
 */
public class RandomSelectStrategyTest {
    private ConnectionManager.RandomSelectStrategy strategy;

    @Before
    public void setUp() throws Exception {
        strategy = new ConnectionManager.RandomSelectStrategy();
    }

    @Test
    public void alloc() throws Exception {
        List<InetSocketAddress> addresses = new ArrayList<>();
        InetSocketAddress one = new InetSocketAddress("127.0.0.1", 80);
        addresses.add(one);

        // just only one
        Assert.assertEquals(one, strategy.alloc(addresses));

        InetSocketAddress two = new InetSocketAddress("172.10.1.1", 8080);
        addresses.add(two);

        Assert.assertEquals(true, addresses.contains(strategy.alloc(addresses)));
        Assert.assertEquals(true, addresses.contains(strategy.alloc(addresses)));
    }

}