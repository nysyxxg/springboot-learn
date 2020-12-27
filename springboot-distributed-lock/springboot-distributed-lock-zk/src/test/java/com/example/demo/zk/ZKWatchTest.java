package com.example.demo.zk;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ZKWatchTest {
    private ZkClient client=new ZkClient("127.0.0.1:2181",1000,1000,new MyZkSerializer());

    @Test
    public void test()throws InterruptedException{
        String path="/name";
        //递归删除节点
        client.deleteRecursive(path);
        if(!client.exists(path)){
            //创建一个持久节点
            client.createPersistent(path);
        }
        //给节点添加一个监听器
        client.subscribeDataChanges(path, new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println("监听到"+dataPath+" 节点变更为："+data);
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {

                System.out.println("监听到"+dataPath+"节点被删除----------");
            }
        });

        //创建一个临时有序的节点
        client.createEphemeral(path+"/test",1);
        //给节点添加子节点的监听器
        client.subscribeChildChanges(path, new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println("监听到"+parentPath+"节点的子节点---------");
                if (currentChilds!=null&&currentChilds.size()>0){
                    for(String Str:currentChilds){
                        System.out.println(Str);
                    }
                }
            }
        });


        Thread.currentThread().join();

    }
}
