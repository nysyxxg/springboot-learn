package bat.ke.qq.com.zklock.lock.zk;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

/**
 */
public class MyZkSerializer implements ZkSerializer {
    @Override
    public byte[] serialize(Object o) throws ZkMarshallingError {
        return new byte[0];
    }

    @Override
    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
        return null;
    }
}