package com.fengwenyi.springboot.netty.protobuf.server.handler;

import com.fengwenyi.springboot.netty.protobuf.common.protobuf.Command.CommandType;
import com.fengwenyi.springboot.netty.protobuf.common.protobuf.Message.MessageBase;
import com.fengwenyi.springboot.netty.protobuf.server.ChannelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * 业务逻辑handler
 * @author Ke Shanqiang
 *
 */
@Component
@Qualifier("logicServerHandler")
@ChannelHandler.Sharable
@Slf4j
public class LogicServerHandler extends ChannelInboundHandlerAdapter{
	//public Logger log = Logger.getLogger(this.getClass());
	private final AttributeKey<String> clientInfo = AttributeKey.valueOf("clientInfo");

	@Autowired
	@Qualifier("channelRepository")
	ChannelRepository channelRepository;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MessageBase msgBase = (MessageBase)msg;

		log.info(msgBase.getData());

		ChannelFuture cf = ctx.writeAndFlush(
				MessageBase.newBuilder()
				.setClientId(msgBase.getClientId())
				.setCmd(CommandType.UPLOAD_DATA_BACK)
				.setData("This is upload data back msg")
				.build()
				);
		/* 上一条消息发送成功后，立马推送一条消息 */
		cf.addListener(new GenericFutureListener<Future<? super Void>>() {
			@Override
			public void operationComplete(Future<? super Void> future) throws Exception {
				if (future.isSuccess()){
					ctx.writeAndFlush(
							MessageBase.newBuilder()
							.setClientId(msgBase.getClientId())
							.setCmd(CommandType.PUSH_DATA)
							.setData("This is a push msg")
							.build()
							);
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
		log.error("Connection closed, client is " + clientId);
		cause.printStackTrace();
	}
}
