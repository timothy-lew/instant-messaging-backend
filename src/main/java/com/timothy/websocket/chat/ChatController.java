package com.timothy.websocket.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Controller
@RequiredArgsConstructor
public class ChatController {

        private final SimpMessagingTemplate messagingTemplate;
        private final ChatMessageService chatMessageService;

        @MessageMapping("/chat")
        public void processMessage(@Payload ChatMessage chatMessage) {
                System.out.println(chatMessage.getRecipientId());
                System.out.println(chatMessage.getSenderId());
                System.out.println(chatMessage.getTimestamp());
                System.out.println(chatMessage.getChatId());
                System.out.println(chatMessage.getContent());

                ChatMessage savedMsg = chatMessageService.save(chatMessage);

                // john/queue/messages (john is receipient)
                messagingTemplate.convertAndSendToUser(
                                chatMessage.getRecipientId(), "/queue/messages",
                                new ChatNotification(
                                                savedMsg.getId(),
                                                savedMsg.getSenderId(),
                                                savedMsg.getRecipientId(),
                                                savedMsg.getContent(),
                                                // savedMsg.getTimestamp()));
                                                formatTimestamp(savedMsg.getTimestamp())));
        }

        private String formatTimestamp(Date timestamp) {
                Instant instant = timestamp.toInstant();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                                .withZone(ZoneId.systemDefault());
                return formatter.format(instant);
        }

        @GetMapping("/messages/{senderId}/{recipientId}")
        public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable String senderId,
                        @PathVariable String recipientId) {
                return ResponseEntity
                                .ok(chatMessageService.findChatMessages(senderId, recipientId));
        }
}
