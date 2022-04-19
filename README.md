### 集群搭建

以redis-6.2.6为例,集群搭建最少需要3个master节点

#### 环境依赖

`yum install -y gcc && yum install -y make`

#### 搭建流程

- redis 编译

`tar -xvf redis-6.2.6.tar.gz && cd redis-6.2.6 && make `

- 设置本地环境

    - mkdir -p /usr/local/redis && mv redis-6.2.6 /usr/local/redis
    - ln -s /usr/local/redis/redis-6.2.6/src/redis-cli /usr/bin/redis-cli
    - ln -s /usr/local/redis/redis-6.2.6/src/redis-server /usr/bin/redis-server

- 文件配置

| 选项                   | 设置                                 | 描述                 |
|:---------------------|:-----------------------------------|:-------------------|
| port                 | 7010                               | redis节点端口          |
| protected-mode       | yes                                |                    |
| daemonize            | yes                                | 后台运行               |
| pidfile              | /var/run/redis_7010.pid            |                    |
| masterauth           | Ali@2019                           | 主机密码               |
| requirepass          | Ali@2019                           | 密码                 |
| appendonly           | yes                                | 开启AOF              |
| appendfsync          | everysec                           | AOF方式 每秒           |
| cluster-enabled      | yes                                | 开启集群模式             |
| cluster-config-file  | nodes-7010.conf                    |                    |
| dir                  | /usr/local/redis/redis-6.2.6/7010/ | 节点数据目录             |
| aof-use-rdb-preamble | yes                                | 开启混合刷盘模式           |
| maxmemory            |                                    | 设置最大内存 单位(bytes)   |

- 启动方式

  `redis-6.2.6/src/redis-server redis.conf`
- 集群创建

  ``redis-cli --cluster create   11.163.70.5:7010  11.163.70.5:7011  11.163.70.5:7012  11.163.70.5:7013  11.163.70.5:7014  11.163.70.5:7015 --cluster-replicas 1 -a Ali@2019
  ``

- 客户端进入方式

  ``redis-cli -c -a Ali@2019 -p 7010 --raw
  ``

#### 问题汇总

- 通过 redisson 获取到对应key的值一直为 null

  `问题排查到为集群搭建时, masterauth 选项未配置`

