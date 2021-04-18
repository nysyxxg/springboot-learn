package com.lnjecit.distributed.lock.domain;

public class DbLock {
	/**
	 * 基于数据库实现分布式锁
	 * 基于缓存实现分布式锁 
	 * 基于Zookeeper实现分布式锁
	 * 
	 要求:
	 {
	 	可以保证在分布式部署的应用集群中，同一个方法在同一时间只能被一台机器上的一个线程执行。
		这把锁要是一把可重入锁（避免死锁）
		这把锁最好是一把阻塞锁（根据业务需求考虑要不要这条）
		有高可用的获取锁和释放锁功能
		获取锁和释放锁的性能要好
	 }
	 
	 1.基于数据库实现分布式锁
	 	a)当我们要锁住某个方法或资源时，我们就在该表中增加一条记录，想要释放锁的时候就删除这条记录。
	 	CREATE TABLE `methodLock` (
		 `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
		 `method_name` varchar(64) NOT NULL DEFAULT '' COMMENT '锁定的方法名',
		 `desc` varchar(1024) NOT NULL DEFAULT '备注信息',
		 `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '保存数据时间，自动生成',
		 PRIMARY KEY (`id`),
		 UNIQUE KEY `uidx_method_name` (`method_name `) USING BTREE
		) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='锁定中的方法';
		b)当我们想要锁住某个方法时，执行以下SQL：
		insert into methodLock(method_name,desc) values (‘method_name’,‘desc’)
		因为我们对method_name做了唯一性约束，这里如果有多个请求同时提交到数据库的话，
		数据库会保证只有一个操作可以成功，那么我们就可以认为操作成功的那个线程获得了该方法的锁，可以执行方法体内容。
		当方法执行完毕之后，想要释放锁的话，需要执行以下Sql:
		delete from methodLock where method_name ='method_name'
		{
			1、这把锁强依赖数据库的可用性，数据库是一个单点，一旦数据库挂掉，会导致业务系统不可用。
			2、这把锁没有失效时间，一旦解锁操作失败，就会导致锁记录一直在数据库中，其他线程无法再获得到锁。
			3、这把锁只能是非阻塞的，因为数据的insert操作，一旦插入失败就会直接报错。没有获得锁的线程并不会进入排队队列，要想再次获得锁就要再次触发获得锁操作。
			4、这把锁是非重入的，同一个线程在没有释放锁之前无法再次获得该锁。因为数据中数据已经存在了。
			
	      当然，我们也可以有其他方式解决上面的问题。
			1.数据库是单点？搞两个数据库，数据之前双向同步。一旦挂掉快速切换到备库上。
			2.没有失效时间？只要做一个定时任务，每隔一定时间把数据库中的超时数据清理一遍。
			3.非阻塞的？搞一个while循环，直到insert成功再返回成功。
			4.非重入的？在数据库表中加个字段，记录当前获得锁的机器的主机信息和线程信息，
			那么下次再获取锁的时候先查询数据库，如果当前机器的主机信息和线程信息在数据库可以查到的话，直接把锁分配给他就可以了。
		}
		2.基于数据库排他锁
			public boolean lock(){
			   connection.setAutoCommit(false)
			   while(true){
			       try{
			           result = select * from methodLock where method_name=xxx for update;
			           if(result==null){
			               return true;
			           }
			       }catch(Exception e){
			
			       }
			       sleep(1000);
			   }
			   return false;
			}
			for update语句会在执行成功后立即返回，在执行失败时一直处于阻塞状态，直到成功。
			public void unlock(){
			   connection.commit();
			}
			通过connection.commit()操作来释放锁
	 	
	 
	 */
}