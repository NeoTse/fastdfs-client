package me.johntse.fastdfs.conn;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * 默认Tracker选择策略.
 *
 * @author johntse
 * @since 0.1.0
 */
public class DefaultSelectStrategyTest {
    private ConnectionManager.DefaultSelectStrategy strategy;

    @Before
    public void setUp() throws Exception {
        strategy = new ConnectionManager.DefaultSelectStrategy();
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

        Assert.assertEquals(0, strategy.preIndex.get());
        Assert.assertEquals(one, strategy.alloc(addresses));
        Assert.assertEquals(1, strategy.preIndex.get());
        Assert.assertEquals(two, strategy.alloc(addresses));

        // round
        Assert.assertEquals(0, strategy.preIndex.get());
        Assert.assertEquals(one, strategy.alloc(addresses));
    }

}