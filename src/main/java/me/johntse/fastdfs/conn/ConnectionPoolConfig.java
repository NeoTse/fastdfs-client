package me.johntse.fastdfs.conn;

/**
 * 连接池配置.
 *
 * @author johntse
 * @since 0.1.0
 */
public final class ConnectionPoolConfig {
    /**
     * 是否在空闲时进行连接可用性测试.
     * 默认开启
     */
    public final boolean testWhileIdle;

    /**
     * 当连接池耗尽，无可用连接时，是否阻塞，否则抛出异常.
     * 必须配置项
     */
    public final boolean blockWhenExhausted;

    /**
     * 连接池中最大可以连接数.
     * 必须配置项
     */
    public final int maxTotal;

    /**
     * 连接池中每个地址对应的最大空闲连接数.
     * 默认为8
     */
    public final int maxPerIdle;

    /**
     * 连接池中每个地址对应的最小空闲连接数，即系统保证至少这些数目的空闲线程不会被回收.
     * 当minIdle设置值大于maxIdle时，使用maxIdle替换
     * 默认为1
     */
    public final int minPerIdle;

    /**
     * 当连接池耗尽设置为阻塞时（blockWhenExhausted=true），此配置项可用，表示阻塞多长时间。
     * 如果设置为-1，则表示无限期阻塞.
     * 默认为1秒，最大值不允许超过10分钟
     */
    public final long maxWaitMillis;

    /**
     * 连接变为淘汰对象时所需的最小空闲时间，设置为负数时表示连接空闲时不淘汰.
     * 默认为3分钟，最大值不允许超过1个小时
     */
    public final long minEvictableIdleTimeMillis;

    /**
     * 设置了淘汰线程运行周期，设置为负数时表示不启动淘汰线程.
     * 默认为1分钟，最大值不允许超过1个小时
     */
    public final long timeBetweenEvictionRunsMillis;


    /**
     * 如果启动了淘汰线程，设置为正数，表示每次运行时，淘汰对象数目为设置值和空闲连接中的最小值。
     * 设置为负数时表示需要检查多少个空闲连接.
     * 默认为-1，即检查所有空闲连接
     */
    public final int numTestsPerEvictionRun;

    private ConnectionPoolConfig(Builder builder) {
        testWhileIdle = builder.testWhileIdle;
        blockWhenExhausted = builder.blockWhenExhausted;

        maxTotal = builder.maxTotal;
        maxPerIdle = builder.maxPerIdle;
        minPerIdle = builder.minPerIdle;
        numTestsPerEvictionRun = builder.numTestsPerEvictionRun;

        maxWaitMillis = builder.maxWaitMillis;
        minEvictableIdleTimeMillis = builder.minEvictableIdleTimeMillis;
        timeBetweenEvictionRunsMillis = builder.timeBetweenEvictionRunsMillis;
    }

    public static class Builder {
        private boolean testWhileIdle = true;
        private boolean blockWhenExhausted;

        private int maxTotal;
        private int maxPerIdle;
        private int minPerIdle;
        private int numTestsPerEvictionRun = -1;

        private long maxWaitMillis = 1000;
        private long minEvictableIdleTimeMillis = 3L * 60 * 1000;
        private long timeBetweenEvictionRunsMillis = 60L * 1000;

        /**
         * 连接池配置对象构造器，简化配置流程，提高配置灵活性.
         *
         * @param maxTotal           连接池中缓存的最大连接数。必需设置项
         * @param blockWhenExhausted 连接池中连接数耗尽或者达到最大连接数时，再次请求连接时是否阻塞。必需设置项
         */
        public Builder(int maxTotal, boolean blockWhenExhausted) {
            if (maxTotal <= 0 || maxTotal > 1024) {
                throw new IllegalArgumentException("Invalid argument " + maxTotal
                        + "Connection pool config: maxTotal should be in (0, 1024]");
            }

            this.maxTotal = maxTotal;
            this.blockWhenExhausted = blockWhenExhausted;
        }

        /**
         * 设置单个地址上连接的最大空闲数.
         * 其中最大空闲数合法取值范围为[0, maxTotal]
         *
         * @param maxPerIdle 最大空闲数。可选配置项
         * @return Builder
         */
        public Builder setMaxPerIdle(int maxPerIdle) {
            if (maxPerIdle > maxTotal || maxPerIdle <= 0) {
                throw new IllegalArgumentException("Invalid argument " + maxPerIdle
                        + "Connection pool config: maxPerIdle should be in (0, "
                        + maxTotal + "].");
            }

            this.maxPerIdle = maxPerIdle;
            return this;
        }

        /**
         * 设置单个地址上连接的最小空闲数.
         * 其中最小空闲数合法取值范围为[0, maxPerIdle]
         *
         * @param minPerIdle 最小空闲数。可选配置项
         * @return Builder
         */
        public Builder setMinPerIdle(int minPerIdle) {
            if (minPerIdle > maxPerIdle || minPerIdle < 0) {
                throw new IllegalArgumentException("Invalid argument " + minPerIdle
                        + "Connection pool config: maxPerIdle should be in [0, "
                        + maxPerIdle + "].");
            }

            this.minPerIdle = minPerIdle;
            return this;
        }

        /**
         * 连接空闲时是否进行合法性测试.
         *
         * @param testWhileIdle 是否进行合法性测试。可选配置项
         * @return Builder
         */
        public Builder setTestWhileIdle(boolean testWhileIdle) {
            this.testWhileIdle = testWhileIdle;
            return this;
        }

        /**
         * 每次测试数量设置.
         *
         * @param numTestsPerEvictionRun 测试数量。可选配置项
         * @return Builder
         */
        public Builder setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
            if (numTestsPerEvictionRun > 64) {
                throw new IllegalArgumentException("Invalid argument " + numTestsPerEvictionRun
                        + "Connection pool config: numTestsPerEvictionRun should be less than 64.");
            }

            this.numTestsPerEvictionRun = numTestsPerEvictionRun;

            return this;
        }

        /**
         * 阻塞等待超时时长.
         * 只有当<code>blockWhenExhausted</code>设置为true时，才能生效。最大超时时间不能超过1个小时
         *
         * @param maxWaitMillis 超时时长。可选配置项
         * @return Builder
         */
        public Builder setMaxWaitMillis(long maxWaitMillis) {
            if (blockWhenExhausted) {
                if (maxWaitMillis > 10L * 60 * 1000) {
                    throw new IllegalArgumentException("Invalid argument " + maxWaitMillis
                            + "Connection pool config: maxWaitMillis should be less than 10 min.");
                }
                this.maxWaitMillis = maxWaitMillis;
            }

            return this;
        }

        /**
         * 连接空闲至少多长时间后变为淘汰对象.
         * 空闲时间设置上限最大不能超过1个小时
         *
         * @param minEvictableIdleTimeMillis 最小空闲时间。可选配置项
         * @return Builder
         */
        public Builder setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
            if (minEvictableIdleTimeMillis > 60L * 60 * 1000) {
                throw new IllegalArgumentException("Invalid argument " + minEvictableIdleTimeMillis
                        + "Connection pool config: minEvictableIdleTimeMillis should be less than 1 hours.");
            }

            this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;

            return this;
        }

        /**
         * 设置两次淘汰运行间隔时长.
         * 最长时间不超过1个小时
         *
         * @param timeBetweenEvictionRunsMillis 间隔时长。可选配置项
         * @return Builder
         */
        public Builder setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
            if (timeBetweenEvictionRunsMillis > 60L * 60 * 1000) {
                throw new IllegalArgumentException("Invalid argument " + timeBetweenEvictionRunsMillis
                        + "Connection pool config: timeBetweenEvictionRunsMillis should be less than 1 hours.");
            }

            this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;

            return this;
        }

        public ConnectionPoolConfig build() {
            return new ConnectionPoolConfig(this);
        }

    }
}
