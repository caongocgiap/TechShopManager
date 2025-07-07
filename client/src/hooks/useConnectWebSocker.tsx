import { useState } from "react";
import { useStompClient, useSubscription } from "react-stomp-hooks";

const useConnectWebSocket = () => {
  const [lastMessage, setLastMessage] = useState("No message received yet");

  const [isConnected, setIsConnected] = useState(false);

  useSubscription("/topic/message", (message) => {
    setLastMessage(message.body);
    setIsConnected(true);
  });

  const stompClient = useStompClient();

  const publishMessage = () => {
    if (stompClient) {
      stompClient.publish({
        destination: "/sendMessage",
        body: "Hello User",
      });
    }
  };

  return {
    lastMessage,
    isConnected,
    publishMessage,
  };
};

export default useConnectWebSocket;
