package me.johntse.fastdfs.conn;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 负载均衡分配策略测试用例.
 *
 * @author johntse
 * @since 0.1.0
 */
public class LoadBalanceStrategyTest {
    private ConnectionManager.LoadBalanceStrategy strategy;
    private ConnectionPoolMock poolMock;

    @Before
    public void setUp() throws Exception {
        poolMock = new ConnectionPoolMock();
        strategy = new ConnectionManager.LoadBalanceStrategy(
                new ConnectionManager(poolMock,
                        new InetSocketAddress("127.0.0.1", 80)));
    }

    @Test
    public void alloc() throws Exception {
        Map<String, Integer> activeCounts = new HashMap<>();
        List<InetSocketAddress> addresses = new ArrayList<>();

        // only one
        InetSocketAddress one = new InetSocketAddress("172.10.1.1", 443);
        activeCounts.put(one.toString(), 1);
        addresses.add(one);
        poolMock.setActiveCounts(activeCounts);

        Assert.assertEquals(one, strategy.alloc(addresses));
        Assert.assertEquals(0, strategy.addresses.size()); // no cache

        // load balance select
        InetSocketAddress two = new InetSocketAddress("172.10.1.10", 22);
        activeCounts.put(two.toString(), 2);
        addresses.add(two);
        poolMock.setActiveCounts(activeCounts);

        Assert.assertEquals(one, strategy.alloc(addresses));
        Assert.assertEquals(1, strategy.addresses.size()); // cache one

        // active changed
        activeCounts.put(two.toString(), 0);
        poolMock.setActiveCounts(activeCounts);

        Assert.assertEquals(two, strategy.alloc(addresses));
        Assert.assertEquals(2, strategy.addresses.size()); // cache two
    }

    static class ConnectionPoolMock extends ConnectionPool {
        private Map<String, Integer> activeCounts;

        public ConnectionPoolMock() {
            super(new TcpConnectionPoolFactory(30, 30, Charset.forName("utf8")));
        }

        public void setActiveCounts(Map<String, Integer> activeCounts) {
            this.activeCounts = Collections.unmodifiableMap(activeCounts);
        }

        @Override
        public Map<String, Integer> getNumActivePerKey() {
            return activeCounts;
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }

}