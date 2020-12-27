package bat.ke.qq.com.zklock.lock.zk;

import bat.ke.qq.com.zklock.lock.AbstractLock;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
/**
 * 分布式锁
 */
public class ZkDistributedLock extends AbstractLock implements IZkDataListener {

    private ZkClient zkclient;
    private String path="/lock";
    private CountDownLatch countDownLatch;
    private String config;

    public ZkDistributedLock(String path, String config) {
        zkclient=new ZkClient(config);
        this.path = path;
        this.config = config;
    }

    @Override
    public boolean tryLock() {
        try {
            if(zkclient==null){
                //建立连接
                zkclient=new ZkClient(config);
            }
            //创建临时节点
            zkclient.createEphemeral(path);
        }catch (Exception e){
            //存在节点
            return false;
        }
        return true;
    }

    @Override
    public void waitLock() {
        //注册监听
        zkclient.subscribeDataChanges(path,this);
        if(zkclient.exists(path)){
            countDownLatch=new CountDownLatch(1);//闭锁
            try {
                countDownLatch.await(50, TimeUnit.MILLISECONDS);//计数器变为0之前都会阻塞
                countDownLatch=null;
                //解除监听
                zkclient.unsubscribeDataChanges(path,this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void deLock(){
        if(zkclient!=null){
            //删除临时节点
            zkclient.delete(path);
            System.out.println("---------------释放锁资源---------------");
        }
    }

    @Override
    public void handleDataChange(String dataPath, Object data) throws Exception {

    }

    @Override
    public void handleDataDeleted(String dataPath) throws Exception {
        if(countDownLatch!=null){
            countDownLatch.countDown();//计数器自减1 即 节点删除之后 计数器变为0

        }

    }
}
