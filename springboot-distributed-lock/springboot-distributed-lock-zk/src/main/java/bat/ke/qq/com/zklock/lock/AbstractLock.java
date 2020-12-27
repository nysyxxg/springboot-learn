package bat.ke.qq.com.zklock.lock;

public abstract class AbstractLock implements Lock {
    @Override
    public void lock() {
        if(tryLock()){
            System.out.println("获取锁-------");
        }else {
            waitLock();
            lock();
        }

    }

    @Override
    public void unlock() {
        deLock();
    }

    //尝试获取锁
    public abstract boolean tryLock();

    //等待锁
    public abstract void waitLock();

    //删除锁
    public abstract void deLock();
}
