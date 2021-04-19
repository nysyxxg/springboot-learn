package bat.ke.qq.com.zklock.lock.zk;

import bat.ke.qq.com.zklock.lock.Lock;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import java.util.concurrent.CountDownLatch;

/**
 * 分布式锁
 */
public class ZKdistributeLockV2 implements Lock {

    private String lockPath;
    
    private  String config;

    private ZkClient client;

    public ZKdistributeLockV2(String lockPath, String config){
        super();
        this.config = config;
        this.lockPath = lockPath;
        client = new ZkClient(config);
        client.setZkSerializer(new MyZkSerializer());
    }
    
    public ZKdistributeLockV2(String lockPath) {
        this.lockPath = lockPath;
    }
    
    @Override
    public void lock() {
        //如果获取不到锁 阻塞等待
        if(!tryLock()) {
            //没获得锁 阻塞自己
            waitForLock();
            lock();
        }
    }

    private void waitForLock() {  // 等待获取锁
        CountDownLatch countDownLatch = new CountDownLatch(1);
        IZkDataListener listener = new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                //删除节点 阻塞计数减一
                System.out.println("----收到节点被删除了");
                countDownLatch.countDown();
            }
            @Override
            public void handleDataDeleted(String s) throws Exception {
            }
        };
        //注册节点
        client.subscribeChildChanges(lockPath, (IZkChildListener) listener);
        if (this.client.exists(lockPath)) {
            try {
                countDownLatch.await();//计数加一
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //取消注册
        client.unsubscribeChildChanges(lockPath, (IZkChildListener) listener);
    }

    public boolean tryLock() {  //不会阻塞
        //创建节点
        try {
            client.createEphemeral(lockPath);// 如果创建成功，说明就获取锁
        } catch (ZkNodeExistsException e) {
            return false;//  如果这个节点，没有创建成功，就失败
        }
        return true;
    }


    @Override
    public void unlock() {
        client.delete(lockPath);
    }

    
}