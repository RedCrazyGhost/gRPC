package redcrazyghost;

import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.stub.StreamObserver;

import javax.naming.Name;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * 服务器启动类
 *
 * @author RedCrazyGhost - wenxingzhan
 * @date 2023/10/14 18:04
 **/
public class ServerMain {
    private Server server;
    private static final Logger logger = Logger.getGlobal();
    private static final int PORT=5001;
    private static final String NAME="Java-gRPC-Server";
    private static final String VERSION="1.0.0";
    private static final String TECHNOLOGY="Java";

    public static void main(String[] args)  {
        final ServerMain server=new ServerMain();
        logger.info(NAME+" starting...");
        try {
            server.start();
        }catch (IOException e){
            logger.warning(NAME+" dead start！reason:"+e.getMessage());
            return;
        }
        logger.info(NAME+" listening...");
        try {
            server.blockUntilShutdown();
        }catch (InterruptedException e){
            logger.warning(NAME +" InterruptedException！reason:"+e.getMessage());
        }
    }

    private void start() throws IOException {
        server= Grpc.newServerBuilderForPort(PORT, InsecureServerCredentials.create())
                .addService(new TestImpl())
                .build()
                .start();
    }



    private void stop() throws InterruptedException {
        if (server!=null){
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server!=null){
            server.awaitTermination();
        }
    }


    static class TestImpl extends TestGrpc.TestImplBase{

        @Override
        public void getTestData(GetTestDataRequest request,StreamObserver<GetTestDataResponse> responseStreamObserver){
            logger.info(request.toString());
            GetTestDataResponse response= GetTestDataResponse.newBuilder()
                    .setName(NAME)
                    .setPort(PORT)
                    .setTechnology(TECHNOLOGY)
                    .setVersion(VERSION)
                    .build();
            responseStreamObserver.onNext(response);
            responseStreamObserver.onCompleted();
        }
    }

}
