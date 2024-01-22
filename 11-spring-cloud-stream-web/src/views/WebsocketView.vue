<template>
    <div>
        <h1 class="section-title">消费者处于不同一个组</h1>
        <!-- 输入消息框和发送按钮 -->
        <div class="input-container">
            <el-button size="medium" type="success" @click="sendMessage" class="send-button">点击按钮 - 生产者生产消息</el-button>
        </div>
        <!-- 生产者 -->
        <section>
            <h1 class="section-title">生产者生产消息 - 消息列表</h1>
            <div class="message-container">
                <el-divider class="message-divider"></el-divider>
                <!-- 用户消息列表 -->
                <div class="message-list">
                    <div v-for="(msg, index) in producerMessages" :key="index" class="message">{{ msg }}</div>
                </div>
            </div>
        </section>

        <!-- 消费者1 - 消费消息 -->
        <section>
            <h1 class="section-title">消费者1消费消息 - 消息列表</h1>
            <div class="message-container">
                <el-divider class="message-divider"></el-divider>
                <!-- 消费者1消息列表 -->
                <div class="message-list">
                    <div v-for="(msg, index) in consumer1Messages" :key="index" class="message">{{ msg }}</div>
                </div>
            </div>
        </section>

        <!-- 消费者2 - 消费消息 -->
        <section>
            <h1 class="section-title">消费者2消费消息 - 消息列表</h1>
            <div class="message-container">
                <el-divider class="message-divider"></el-divider>
                <!-- 消费者2消息列表 -->
                <div class="message-list">
                    <div v-for="(msg, index) in consumer2Messages" :key="index" class="message">{{ msg }}</div>
                </div>
            </div>
        </section>

    </div>
</template>


<script>
    import SockJS from 'sockjs-client'; // 导入 SockJS
    import WebStomp from 'webstomp-client'; // 导入 WebStomp

    export default {
        data() {
            return {
                producerMessages: [], // 生产者消息列表
                consumer1Messages: [], // 消息列表
                consumer2Messages: [], // 消息列表
                stompClient: null, // Stomp 客户端
                reconnectionAttempts: 0, // 重连尝试次数
            };
        },
        mounted() {
            this.initWebSocket();
        },
        methods: {
            /**
             * 初始化 WebSocket
             */
            initWebSocket() {
                this.connectWebSocket();
            },
            /**
             * 连接 WebSocket
             */
            connectWebSocket() {
                const socket = new SockJS("http://192.168.10.221:11400/websocket/server"); // 创建新的 SockJS 连接
                this.stompClient = WebStomp.over(socket); // 创建新的 Stomp 客户端
                let headers = {};
                this.stompClient.connect(headers, this.onWebSocketConnect, this.onWebSocketError); // 连接 Stomp 客户端

                // 监听WebSocket事件
                this.stompClient.onclose = this.reconnectWebSocket;
            },
            /**
             * 连接 Stomp 客户端
             */
            onWebSocketConnect() {
                this.subscribeToTopics();
                this.reconnectionAttempts = 0; // 连接成功后重置重连尝试次数
            },
            /**
             * 订阅主题
             */
            subscribeToTopics() {
                this.subscribeToProducer(`/producer/producer-message`);
                this.subscribeToConsumer1(`/consumer1/receive-message`);
                this.subscribeToConsumer2(`/consumer2/receive-message`);
            },
            /**
             * 订阅主题
             */
            subscribeToProducer(topicPath) {
                this.stompClient.subscribe(topicPath, (msg) => {
                    if (msg !== "") {
                        this.producerMessages.push(msg.body);
                    }
                });
            },
            /**
             * 订阅主题
             */
            subscribeToConsumer1(topicPath) {
                this.stompClient.subscribe(topicPath, (msg) => {
                    if (msg !== "") {
                        this.consumer1Messages.push(msg.body);
                    }
                });
            },
            /**
             * 订阅主题
             */
            subscribeToConsumer2(topicPath) {
                this.stompClient.subscribe(topicPath, (msg) => {
                    if (msg !== "") {
                        this.consumer2Messages.push(msg.body);
                    }
                });
            },
            /**
             * Stomp 客户端连接失败
             */
            onWebSocketError(err) {
                console.log('stompClient 连接失败: ', err);
                this.reconnectWebSocket(); // 关闭 WebSocket
            },
            /**
             * 监听 WebSocket 的重连次数
             */
            reconnectWebSocket() {
                if (this.reconnectionAttempts <= 3) { // 设置最大重连次数
                    this.reconnectionAttempts++;
                    console.log("WebSocket 重连中 ...");
                    setTimeout(() => this.connectWebSocket(), Math.pow(2, this.reconnectionAttempts) * 1000);
                } else {
                    console.log("WebSocket 连接尝试次数已达到最大限制");
                }
            },
            /**
             * 发送消息
             */
            sendMessage() {
                this.stompClient.send(`/server/from-client`); // 发送消息
            },
            /**
             * WebSocket 连接关闭
             */
            closeWebSocket() {
                console.log('WebSocket 连接关闭'); // WebSocket 连接关闭
                this.stompClient.disconnect(); // 断开 Stomp 客户端连接
            },
        },
        destroyed() {
            this.closeWebSocket(); // 关闭 WebSocket
        },
    };
</script>

<style>
    .section-title {
        font-size: 24px;
        margin-bottom: 10px;
    }

    .send-button {
        width: 300px;
        font-size: 18px;
        margin-top: 20px;
    }

    .message-divider {
        margin: 20px 0;
    }

    .message-list {
        max-height: 300px;
        overflow-y: auto;
    }

    .message {
        border: 1px solid #ccc;
        padding: 10px;
        margin-bottom: 10px;
        border-radius: 5px;
    }
</style>