package redcrazyghost;

import io.grpc.*;

import java.util.concurrent.TimeUnit;

/**
 * 客户端启动类
 *
 * @author RedCrazyGhost - wenxingzhan
 * @date 2023/10/14 18:05
 **/
public class ClientMain {
    private final HelloWorldGrpc.HelloWorldBlockingStub blockingStub;

    public ClientMain(Channel channel){
        blockingStub=HelloWorldGrpc.newBlockingStub(channel);
    }

    public void sayHello(String message){
        HelloWorldProto.Request request= HelloWorldProto.Request
                .newBuilder()
                .setMessage(message)
                .build();
        HelloWorldProto.Response response;
        try{
            response=blockingStub.sayHello(request);
        }catch (StatusRuntimeException e){
            e.printStackTrace();
            return;
        }
        System.out.println(response.getMessage());
    }

    public static void main(String[] args) throws InterruptedException {
        String target="localhost:5001";
        ManagedChannel channel= Grpc
                .newChannelBuilder(target, InsecureChannelCredentials.create())
                .build();
        try {
            ClientMain client=new ClientMain(channel);
            client.sayHello("Java Client send message >20000514<");
        }finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
