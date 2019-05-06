package com.liuzhichao.redis.utils.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 刘志超
 * @date 2019-04-26 上午10:23:37
 * @version 类说明 读取配置文件
 */
@Component
public class RedisConfig {

	@Value("${edw.sdp.spring.redis.hostName}")
	private String hostName;

	@Value("${edw.sdp.spring.redis.port}")
	private Integer port;

	@Value("${edw.sdp.spring.redis.password}")
	private String password;

	@Value("${edw.sdp.spring.redis.timeout}")
	private Integer timeout;

	@Value("${edw.sdp.spring.redis.jedis.pool.max-idle}")
	private Integer maxIdle;
	
	@Value("${edw.sdp.spring.redis.jedis.pool.min-idle}")
	private Integer minIdle;

	@Value("${edw.sdp.spring.redis.jedis.pool.max-active}")
	private Integer maxActive;

	@Value("${edw.sdp.spring.redis.jedis.pool.max-wait}")
	private Integer maxWaitMillis;

	@Value("${edw.sdp.spring.redis.jedis.pool.minEvictableIdleTimeMillis}")
	private Integer minEvictableIdleTimeMillis;

	@Value("${edw.sdp.spring.redis.jedis.pool.numTestsPerEvictionRun}")
	private Integer numTestsPerEvictionRun;

	@Value("${edw.sdp.spring.redis.jedis.pool.timeBetweenEvictionRunsMillis}")
	private long timeBetweenEvictionRunsMillis;

	@Value("${edw.sdp.spring.redis.jedis.pool.testOnBorrow}")
	private Boolean testOnBorrow;

	@Value("${edw.sdp.spring.redis.jedis.pool.testWhileIdle}")
	private Boolean testWhileIdle;

	@Value("${edw.sdp.spring.redis.cluster.nodes}")
	private String clusterNodes;

	@Value("${edw.sdp.spring.redis.cluster.max-redirects}")
	private Integer mmaxRedirectsac;

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public Integer getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(Integer maxIdle) {
		this.maxIdle = maxIdle;
	}

	public Integer getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}

	public Integer getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(Integer maxActive) {
		this.maxActive = maxActive;
	}

	public Integer getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(Integer maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public Integer getMinEvictableIdleTimeMillis() {
		return minEvictableIdleTimeMillis;
	}

	public void setMinEvictableIdleTimeMillis(Integer minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	public Integer getNumTestsPerEvictionRun() {
		return numTestsPerEvictionRun;
	}

	public void setNumTestsPerEvictionRun(Integer numTestsPerEvictionRun) {
		this.numTestsPerEvictionRun = numTestsPerEvictionRun;
	}

	public long getTimeBetweenEvictionRunsMillis() {
		return timeBetweenEvictionRunsMillis;
	}

	public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public Boolean getTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(Boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public Boolean getTestWhileIdle() {
		return testWhileIdle;
	}

	public void setTestWhileIdle(Boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}

	public String getClusterNodes() {
		return clusterNodes;
	}

	public void setClusterNodes(String clusterNodes) {
		this.clusterNodes = clusterNodes;
	}

	public Integer getMmaxRedirectsac() {
		return mmaxRedirectsac;
	}

	public void setMmaxRedirectsac(Integer mmaxRedirectsac) {
		this.mmaxRedirectsac = mmaxRedirectsac;
	}

}
