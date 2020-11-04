# SpringBoot + socket(STOMP + sockJS)
- 순수 웹소켓과의 차이
    1. 클라이언트 단에서 sockJS와 stomp.js 와 같은 외부라이브러리를 사용한다.
    2. sockJS 같은 경우는 안드로이드나 IOS와 같은 다른 플랫폼에서 어느정도 호환이 된다.
    3. 웹 표준은 아니지만, IE8 까지 지원하는 기술이다. (일반 웹소켓은 MDN에서 제공하는 웹표준을 따르지만, IE10 까지만 지원 한다.)
    4. 복잡한 Handler 로직을 Receiver, Subscriber 개념으로 쉽게 풀어간다.

## Tutorial
   1. webSocketMessageBrokerConfiguerer 설정
        - receiver와 subscriber 사이에 오가는 데이터들을 관리해주는 'Broker'를 등록한다.
        - 전송되는 메세지가 subscriber로 가기 전에 메세지를 뿌려줄 url의 접두어(prefix)를 등록한다
            - 참고로 일반 웹소켓 핸들러로 이용할 경우. 모든 세션을 검색해서, 전달하고 싶은 세션을 구분하여, 메세지를 보내는 방식을 사용해야한다.
            - STOMP, SockJS를 이용하면, url로 구분 짓는 것 만으로 쉡게 분류할 수 있다.
        - Endpoint 역시 지정해주면 되는데, Endpoint는 웹소켓 프로토콜에서, 생명주기 및 상호작용 활동을 관리하는 개체이다.
           ```java
            @RequiredArgsConstructor
            @Configuration
            @EnableWebSocketMessageBroker
            public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
                @Override
                public void configureMessageBroker(MessageBrokerRegistry config) {
                    config.enableSimpleBroker("/topic");
                    config.setApplicationDestinationPrefixes("/app");
                }
            
                @Override
                public void registerStompEndpoints(StompEndpointRegistry registry) {
                    registry.addEndpoint("/gs-guide-websocket").withSockJS();
                }
            
            }
           ``` 

   2. MessageMapping
        - @MessageMapping 으로 전송하는 메세지의 매핑 경로를 조절한다.
        - SpringBoot에는 JackSon 라이브러리가 내장되어 있기 때문에, 엔티티 객체 자체를 리턴하면, 알아서, Json으로 매핑이 된다.
        - 다만, 매핑되는 Json과 자바 객체간의 필드가 일치 해야 하는데, 데이터를 커스터마이징하고 전송 할 경우 (클라이언트 단에 전달하지 말아야할 데이터가 있어서 몇가지 데이터를 생략한 경우)
        SimpMessageSendingOperations 의 convertAndSend를 사용하여, 보내야 할 것이다.
        
        ```java
        @Controller
        @RequiredArgsConstructor
        public class GreetController {
            @Autowired
            private final SimpMessageSendingOperations messageTemplate;
            
            /* 기본적인 방법 -1- */
            @MessageMapping("/hello")
            @SendTo("/topic/greetings")
            public void greeting(HelloMessage message) throws Exception {
                Thread.sleep(1000); // simulated delay      
                return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
            }
            
            /* @SendTo 대신에 SimpMessageSendingOperations 를 활용한 방법  -2- */
            @MessageMapping("/hello")
            public void greeting(HelloMessage helloMessage) throws Exception {
                messageTemplate.convertAndSend("/topic/greetings", helloMessage.getName());
            }
        
            @MessageMapping("/bye")
            public void gret(HelloMessage helloMessage) {
                messageTemplate.convertAndSend("/topic/greetings", helloMessage.getName() + " bye");
            }
        }
        ``` 
   
   3. 클라이언트단 소켓 구현하기
        - Stomp 에는 크게 `connect`, `disconnect`, `send`, `subscribe` 함수가 있다.
        - `connect` : 클라이언트와 서버 사이를 `handShaking`을 하게 해주는 함수이다. 순수 socket 프로그래밍에서는 `ws://location.host` 와 
        같이 호스트를 지정해서 소켓을 사용하는 모든 세션들을 spring의 handler로 일일이 관리하는 방식으로 진행을 하지만, Stomp는 Endpoint를 기준으로 SockJS를 socket을 넘기고
        소켓을 stomp.over(socket)을 통해, subscriber와 receiver를 기준으로 소켓을 관리한다.
        - `disconnect` : 소켓 서버와의 연결을 끊는 함수이다.
        - `send(messageMappingUrl, header, callback)` : 클라이언트에서 서버로 데이터를 보낼 때 사용하는 함수이다. url 방식으로 서버에 전송하고 @MessageMapping("/hello")을 
        통해 자바로 메세지 데이터를 받을 수 있기 때문에 Controller 단에서 POJO 방식으로 작업이 가능하다.
        - `subscribe` : Stomp 내장함수인 `subscribe`의 첫번째 파라미터에 `webSocketMessageBrokerConfigure` 를 통해 브로커 라우터를 
        `/topic/`에서 subscriber를 구분하는 `greetings`인 `/topic/greetings`로 값을 넘겼다면, 해당 url로 등록된 모든 subscriber 들에게 해당 클라이언트 화면에
        메세지를 전송한다. 보통 그 콜백 함수 안에 구현하고자 하는 비즈니스 로직을 구현한다.
        
        ```html
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Document</title>
            <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.0/sockjs.min.js" integrity="sha512-5yJ548VSnLflcRxWNqVWYeQZnby8D8fJTmYRLyvs445j1XmzR8cnWi85lcHx3CUEeAX+GrK3TqTfzOO6LKDpdw==" crossorigin="anonymous"></script>
            <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js" integrity="sha512-iKDtgDyTHjAitUDdLljGhenhPwrbBfqTKWO1mkhSFH3A7blITC9MhYon6SjnMhp4o0rADGw9yAC6EW4t5a4K3g==" crossorigin="anonymous"></script>
        </head>
        <body>
            <ul id="jsMsgList">
        
            </ul>
            <form method="POST" id="jsForm">
                <fieldset>
                    <input type="text" id=name name="name" placeholder="input message...">
                    <button id = jsSend type="submit">send</button>
                    <button id = jsSend2 type="submit">send</button>
                </fieldset>
            </form>
        
            <script>
                let stompClient;

                const connect = () => {
                    let socket = new SockJS('/gs-guide-websocket');
                    stompClient = Stomp.over(socket);
                    stompClient.connect({}, (frame) => {
                        console.log('Connected: ' + frame);
                        stompClient.subscribe('/topic/greetings', (e) => {
                            const li = document.createElement("li");
                            li.innerText = e.body;
                            document.getElementById("jsMsgList").appendChild(li);
                        });
                    });
                };
        
                connect();
        
                document.getElementById("jsForm").addEventListener("submit", (e) => {
                    e.preventDefault();
                    document.getElementById("name").value = "";
                });
                document.getElementById("jsSend").addEventListener("click", () => {
                    sendMsg();
                });
                document.getElementById("jsSend2").addEventListener("click", () => {
                    sendMsg2();
                });
                const sendMsg = () => {
                    let name = {
                        "name" : `${document.getElementById("name").value}`
                    };
                    stompClient.send("/app/hello", {}, JSON.stringify(name));
                };
                const sendMsg2 = () => {
                    let name = {
                        "name" : `${document.getElementById("name").value}`
                    };
                    stompClient.send("/app/bye", {}, JSON.stringify(name));
                };
        
                
            </script>
        </body>
        </html>
        ```
      # 정리
      ### Stomp와 SockJS를 통해 기존 웹소켓 프로그래밍을 좀더 POJO스럽게 진행을 할 수 있게 되었으며, 브라우저의 호환성을 높일 수 있는 이점을 가질 수 있다.
        