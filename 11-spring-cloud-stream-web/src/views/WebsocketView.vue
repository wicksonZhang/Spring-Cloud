<template>
    <div class="chat-app">
        <div class="producer-section">
            <el-button size="medium" type="success" @click="sendMessage" class="send-button">发送消息</el-button>
            <div class="card">
                <h1 class="section-title">消息生产者</h1>
                <div class="message-container">
                    <el-divider class="message-divider"></el-divider>
                    <div class="message-list">
                        <div v-for="(msg, index) in producerMessages.slice().reverse()" :key="index" class="message">{{ msg }}</div>
                    </div>
                </div>
            </div>
        </div>

        <div class="consumer-section">
            <div class="card" v-for="(messages, index) in [consumer1Messages, consumer2Messages]" :key="index">
                <h1 class="section-title">消费者{{ index + 1 }}</h1>
                <div class="message-container">
                    <el-divider class="message-divider"></el-divider>
                    <div class="message-list">
                        <div v-for="(msg, index) in messages.slice().reverse()" :key="index" class="message">{{ msg }}</div>
                    </div>
                </div>
            </div>
        </div>
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
                        const currentTime = new Date().toLocaleString();
                        this.producerMessages.push(`${currentTime}: ${msg.body}`);
                    }
                });
            },
            /**
             * 订阅主题
             */
            subscribeToConsumer1(topicPath) {
                this.stompClient.subscribe(topicPath, (msg) => {
                    if (msg !== "") {
                        const currentTime = new Date().toLocaleString();
                        this.consumer1Messages.push(`${currentTime}: ${msg.body}`);
                    }
                });
            },
            /**
             * 订阅主题
             */
            subscribeToConsumer2(topicPath) {
                this.stompClient.subscribe(topicPath, (msg) => {
                    if (msg !== "") {
                        const currentTime = new Date().toLocaleString()
                        this.consumer2Messages.push(`${currentTime}: ${msg.body}`);
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
    .chat-app {
        display: flex;
        flex-direction: column;
        gap: 20px;
        padding: 20px;
    }

    .producer-section {
        display: flex;
        gap: 20px;
        align-items: center;
    }

    .card,
    .send-button {
        flex: 1;
    }

    .card {
        border: 1px solid #ccc;
        border-radius: 10px;
        padding: 20px;
        background-color: #fff;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        transition: box-shadow 0.3s ease;
    }

    .card:hover {
        box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
    }

    .section-title {
        font-size: 24px;
        margin-bottom: 10px;
    }

    .send-button {
        font-size: 18px;
        margin-top: 20px;
    }

    .consumer-section {
        display: flex;
        gap: 20px;
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