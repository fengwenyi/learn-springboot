package com.fengwenyi.springbootnettyhello;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author Erwin Feng
 * @since 2019-03-22 10:02
 */
public class DemoHandler extends ChannelInboundHandlerAdapter {

    //public Logger log = Logger.getLogger(this.getClass());
    private final AttributeKey<String> clientInfo = AttributeKey.valueOf("clientInfo");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //MessageBase msgBase = (MessageBase)msg;

        //log.info(msgBase.getData());

        System.out.println(msg);

        ChannelFuture cf = ctx.writeAndFlush("hello");
        /* 上一条消息发送成功后，立马推送一条消息 */
        cf.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()){
                    ctx.writeAndFlush("rs hello");
                }
            }
        });
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

    }

    @SuppressWarnings("deprecation")
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Attribute<String> attr = ctx.attr(clientInfo);
        String clientId = attr.get();
        System.out.println("Connection closed, client is " + clientId);

        //log.error("Connection closed, client is " + clientId);
        cause.printStackTrace();
    }

}
