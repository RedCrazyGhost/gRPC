/**
 @author: RedCrazyGhost
 @date: 2023/10/15

**/

package main

import (
	"context"
	"flag"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
	"log"
	"time"

	pb "gRPC-go/test"
)

var (
	addr = flag.String("addr", "localhost:5001", "grpc server addr")
	name = flag.String("name", "Golang-grpc-client", "Client Name")
)

func main() {
	flag.Parse()
	conn, err := grpc.Dial(*addr, grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		log.Fatalf("Go Client Bad Connect: %v", err)
	}
	defer conn.Close()
	c := pb.NewTestClient(conn)
	ctx, cancel := context.WithTimeout(context.Background(), time.Second)
	defer cancel()
	response, err := c.GetTestData(ctx, &pb.GetTestDataRequest{
		Name:       *name,
		Technology: "Golang",
		Type:       "Client",
		Version:    "1.0.0"})
	if err != nil {
		log.Fatalf("请求失败:%v", err)
	}
	log.Printf("%s", response)
}
