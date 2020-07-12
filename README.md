problem with local rabbitmq not sending USER_JOINED and oother actions

probleme cu dependintele

Host(s) 	baboon.rmq.cloudamqp.com (Load balanced)
baboon-01.rmq.cloudamqp.com
User & Vhost 	dhkruxqt
Password 	oUwLJACFqSKb1Y8ylzjJoBwHmOfYypkA
AMQP URL 	amqp://dhkruxqt:oUwLJACFqSKb1Y8ylzjJoBwHmOfYypkA@baboon.rmq.cloudamqp.com/dhkruxqt
morning-savannah-69089

1: mvn package
2: heroku deploy:jar -j target/startup-websocket-client-0.0.1-SNAPSHOT.jar -i Procfile --app morning-savannah-69089
3: heroku logs --app morning-savannah-69089 --tail


curl --include \
     --no-buffer \
     --header "Connection: Upgrade" \
     --header "Upgrade: websocket" \
     --header "Host: example.com:80" \
     --header "Origin: http://example.com:80" \
     --header "Sec-WebSocket-Key: SGVsbG8sIHdvcmxkIQ==" \
     --header "Sec-WebSocket-Version: 13" \
http://example.com:80/

on connection:

i:inboundChatService a new session id is generated
o:ouboundChatService a new session id is generated

i and o are bound by a query parameter ?user={user}

but they are running on different threads and endpoints:

i = /app/chatMessage.new
o = /topic/chatMessage.new

Every time a user connect a UserEvent is created then the connection is verified;
validateUserFromInboundSocketConnection();
if the user (?user={user}) is the same from i to one from the o then all UserEvents are passed
is not efficient because if a user connects multiple times with the same username then the events are sent duplicated times depending on the number of connected users

If we remove the validateUserFromInboundSocketConnection(); then the messages are sent duplicated depending on connected users;


  binders:
        local_rabbit:
          type: rabbit
          environment:
            spring:
              rabbitmq:
                host: <host>
                port: 5672
                username: <username>
                password: <password>
                virtual-host: /
                
        
        ENSURE THAT THE MESSAGE ARE RECEIVED        
RabbitMQ, ActiveMQ, etc) for the actual broadcasting of messages. In that case Spring maintains TCP connections to the broker, relays messages to it, and also passes messages from it down to connected WebSocket clients.

1. Has Webflux websockets, to channels inbound to send and outbound to receive
2. Inbound receives the ws message and makes the necessary events inside, 
    saves the user client to redis, 
    sends to the broker RabbitMQ the message
    
3. Outbound receives the message from the RabbitMQ and then makes the necessary events inside
    read from redis
    filter the messages if private
    sends to the outbound channel


RABBITMQ: https://hub.docker.com/r/bitnami/rabbitmq/
http://localhost:15673/#/
user pass:
user: user
pass: bitnami

