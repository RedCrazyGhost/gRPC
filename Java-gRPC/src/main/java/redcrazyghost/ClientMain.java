package redcrazyghost;

import io.grpc.*;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * 客户端启动类
 *
 * @author RedCrazyGhost - wenxingzhan
 * @date 2023/10/14 18:05
 **/
public class ClientMain {
    private final TestGrpc.TestBlockingStub blockingStub;
    private static final Logger logger = Logger.getGlobal();

    private static final String TARGET="localhost:5001";
    private static final String TYPE="Client";
    private static final String NAME="Java-gRPC-Client";
    private static final String VERSION="1.0.0";
    private static final String TECHNOLOGY="Java";
    public ClientMain(Channel channel){
        blockingStub=TestGrpc.newBlockingStub(channel);
    }


    public void getTestData(){
        GetTestDataRequest request= GetTestDataRequest
                .newBuilder()
                .setType(TYPE)
                .setName(NAME)
                .setTechnology(TECHNOLOGY)
                .setVersion(VERSION)
                .build();
        GetTestDataResponse response;
        try{
            response=blockingStub.getTestData(request);
        }catch (StatusRuntimeException e){
            logger.warning("1");
            return;
        }
        logger.info(response.toString());
    }

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel= Grpc
                .newChannelBuilder(TARGET, InsecureChannelCredentials.create())
                .build();
        try {
            ClientMain client=new ClientMain(channel);
            client.getTestData();
        }finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
