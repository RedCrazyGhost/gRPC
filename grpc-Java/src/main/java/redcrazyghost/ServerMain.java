package redcrazyghost;

import com.google.type.DateTime;
import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 服务器启动类
 *
 * @author RedCrazyGhost - wenxingzhan
 * @date 2023/10/14 18:04
 **/
public class ServerMain {
    private Server server;
    public static void main(String[] args) throws InterruptedException, IOException {
        final ServerMain server=new ServerMain();
        server.start();
        server.blockUntilShutdown();
    }

    private void start() throws IOException {

        server= Grpc.newServerBuilderForPort(5001, InsecureServerCredentials.create())
                .addService(new HelloWorldImpl())
                .build()
                .start();
        System.out.println("Java gRPC Server started, listening on "+ 5001);
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


    static class HelloWorldImpl extends HelloWorldGrpc.HelloWorldImplBase{

        @Override
        public void  sayHello(HelloWorldProto.Request request, StreamObserver<HelloWorldProto.Response> responseStreamObserver){
            System.out.println(LocalDateTime.now().toString()+"Get Client Message:"+request);
            HelloWorldProto.Response response= HelloWorldProto.Response.newBuilder()
                    .setMessage("Java Server Get:"+request.getMessage())
                    .build();
            responseStreamObserver.onNext(response);
            responseStreamObserver.onCompleted();

        }
    }

}
