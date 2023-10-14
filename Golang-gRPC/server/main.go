/**
 @author: RedCrazyGhost
 @date: 2023/10/15

**/

package main

import (
	"context"
	"flag"
	"fmt"
	pb "gRPC-go/test"
	"google.golang.org/grpc"
	"log"
	"net"
)

var (
	port = flag.Int("port", 5001, "The Server port")
)

type server struct {
	pb.UnimplementedTestServer
}

func (s *server) GetTestData(ctx context.Context, in *pb.GetTestDataRequest) (*pb.GetTestDataResponse, error) {
	log.Printf("结果：%v", in)
	return &pb.GetTestDataResponse{Name: "Golang-grpc-server",
		Version:    "1.0.0",
		Technology: "Golang",
		Port:       5001}, nil
}

// 最低使用版本 go1.19
func main() {
	flag.Parse()
	listen, err := net.Listen("tcp", fmt.Sprintf(":%d", *port))
	if err != nil {
		log.Fatalf("Golang服务器启动失败！原因:%v", err)
	}
	s := grpc.NewServer()
	pb.RegisterTestServer(s, &server{})
	log.Printf("服务器监听结果:%v", listen.Addr())
	if err := s.Serve(listen); err != nil {
		log.Fatalf("获取请求失败！原因：%v", err)
	}
}
